package com.adrian.twic.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.adrian.twic.constants.TwicConstants;
import com.adrian.twic.domain.EsOperationResult;
import com.adrian.twic.domain.PgnChessGame;
import com.adrian.twic.domain.PgnTwicMetadata;
import com.adrian.twic.domain.TwicAppMetadataInfo;
import com.adrian.twic.domain.es.EsPgnChessGame;
import com.adrian.twic.enums.OperationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Holds methods for elasticsearch operations done on PGN objects.
 */
@Service
public final class TwicPgnEsService {

	private static final String STARTED_SAVE_MESSAGE = "Started save of latest twic games.";
	private static final String FINISHED_SAVE_MESSAGE = "Finished save of latest twic games.";
	private static final String UNMARSHALLING_UNEXPECTED_ERROR = "Unexpected error when unmarshalling elasticsearch chess game.";
	private static final String BULK_UNEXPECTED_ERROR = "Unexpected error when running the bulk index operation on pgn file %d.";
	private static final String BULK_DELETE_UNEXPECTED_ERROR = "Unexpected error when running the bulk delete operation on pgn file %d.";

	private static final Logger LOG = Logger.getLogger(TwicPgnEsService.class.getName());

	@Value("${es.batch.size:50}")
	private int batchSize;

	@Value("${twic.latest:3}")
	private int numberOfLatestPgnFiles;

	@Inject
	private TwicPgnParseService parseService;

	@Inject
	private TwicAppMetadataService metadataService;

	@Inject
	private RestHighLevelClient client;

	@Scheduled(cron = "0 0/35 * * * *")
	public EsOperationResult saveLatestGamesFromTwic() {
		LOG.info(STARTED_SAVE_MESSAGE);
		
		final TwicAppMetadataInfo twicAppMetadataInfo = metadataService.getTwicAppMetadata().get();
		int latestDownloadedPgnFileNumber = twicAppMetadataInfo.getLatestDownloadedPgnFileNumber();
		if (latestDownloadedPgnFileNumber == 0) {
			return EsOperationResult.of(TwicConstants.NO_PGN_FILES_CODE, TwicConstants.NO_PGN_FILES_MESSAGE,
					OperationType.PARSE_AND_SAVE_PGN_TO_ES);
		}

		int latestIndexedPgnFileNumber = twicAppMetadataInfo.getLatestIndexedPgnFileNumber();

		for (int i = latestDownloadedPgnFileNumber, counter = 0; i > latestIndexedPgnFileNumber
				&& counter < numberOfLatestPgnFiles; i--, counter++) {

			// if there were already some indexed documents then be sure to remove the
			// oldest twic pgn docs
			if (latestIndexedPgnFileNumber != 0) {
				int pgnDocsToRemove = latestIndexedPgnFileNumber - numberOfLatestPgnFiles + counter + 1;
				LOG.info(String.format("Removing pgns for twic file number %d", pgnDocsToRemove));
				this.removePgnGames(pgnDocsToRemove);
			}

			LOG.info(String.format("Saving pgn games for twic file number %d", i));
			savePgnGamesFromPgnTwicFile(i);

		}

		if (latestDownloadedPgnFileNumber != latestIndexedPgnFileNumber) {
			twicAppMetadataInfo
					.setDateLatestIndexedPgnFileNumber(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
		}

		twicAppMetadataInfo.setLatestIndexedPgnFileNumber(latestDownloadedPgnFileNumber);

		LOG.info(FINISHED_SAVE_MESSAGE);
		final var result = EsOperationResult.of(TwicConstants.SUCCESS_CODE, TwicConstants.SUCCESS_MESSAGE,
				OperationType.PARSE_AND_SAVE_PGN_TO_ES);
		return result;
	}

	public EsOperationResult savePgnGamesFromPgnTwicFile(final int pgnFileNumber) {
		// 1. check if the games do not already exist
		// TODO when ES setup is done

		// 2. parse games from saved twic file
		final var parseResult = parseService.parsePgnFile(pgnFileNumber);

		final EsOperationResult result;
		if (TwicConstants.SUCCESS_CODE != parseResult.getCode()) {
			result = EsOperationResult.of(parseResult.getCode(), parseResult.getText(),
					OperationType.PARSE_AND_SAVE_PGN_TO_ES);
			return result;
		}

		final var games = parseResult.getChessGames();

		// batch processing of retrieved pgn info
		IntStream.range(0, (games.size() + batchSize - 1) / batchSize)
				.mapToObj(i -> games.subList(i * batchSize, Math.min(games.size(), (i + 1) * batchSize)))
				.forEach(batch -> esBulkPersist(batch, pgnFileNumber));

		result = EsOperationResult.of(TwicConstants.SUCCESS_CODE, TwicConstants.SUCCESS_MESSAGE,
				OperationType.PARSE_AND_SAVE_PGN_TO_ES);
		return result;
	}

	public EsOperationResult removePgnGames(final int pgnFileNumber) {
		DeleteByQueryRequest request = new DeleteByQueryRequest(TwicConstants.TWIC_ALIAS);
		request.setQuery(new TermQueryBuilder(TwicConstants.TWIC_NUMBER_FIELD_NAME, Integer.toString(pgnFileNumber)));
		request.setBatchSize(batchSize);

		try {
			client.deleteByQuery(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			LOG.severe(String.format(BULK_DELETE_UNEXPECTED_ERROR, pgnFileNumber));
		}

		final EsOperationResult result = EsOperationResult.of(TwicConstants.SUCCESS_CODE, TwicConstants.SUCCESS_MESSAGE,
				OperationType.REMOVE_PGN_GAMES);
		return result;
	}

	private void esBulkPersist(final List<PgnChessGame> pgnChessGames, final int pgnFileNumber) {
		final var esChessInfo = new ArrayList<EsPgnChessGame>();

		ObjectMapper objectMapper = new ObjectMapper();
		BulkRequest bulkRequest = new BulkRequest();

		for (PgnChessGame game : pgnChessGames) {
			PgnTwicMetadata twicMetadata = PgnTwicMetadata.of(String.format("%d", pgnFileNumber), game.toString());

			EsPgnChessGame esGame = EsPgnChessGame.of(game, twicMetadata);
			String jsonChessGame = null;
			try {
				jsonChessGame = objectMapper.writeValueAsString(esGame);
			} catch (JsonProcessingException e) {
				LOG.severe(UNMARSHALLING_UNEXPECTED_ERROR);
				continue;
			}

			@SuppressWarnings("deprecation")
			IndexRequest indexRequest = new IndexRequest(TwicConstants.TWIC_ALIAS, TwicConstants.CHESS_DATA)
					.id(String.format("%d", game.getMetadata().getChessGameId().hashCode()))
					.source(jsonChessGame, XContentType.JSON);
			bulkRequest.add(indexRequest);

			esChessInfo.add(esGame);
		}

		try {
			client.bulk(bulkRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			LOG.severe(String.format(BULK_UNEXPECTED_ERROR, pgnFileNumber));
		}
	}
}
