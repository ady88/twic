package com.adrian.twic.services;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adrian.twic.constants.TwicConstants;
import com.adrian.twic.domain.EsOperationResult;
import com.adrian.twic.domain.ParseOperationResult;
import com.adrian.twic.domain.PgnChessGame;
import com.adrian.twic.domain.PgnTwicMetadata;
import com.adrian.twic.domain.es.EsPgnChessGame;
import com.adrian.twic.enums.OperationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Holds methods for elasticsearch operations done on PGN objects.
 */
@Service
public final class TwicPgnEsService {

	private static final String UNMARSHALLING_UNEXPECTED_ERROR = "Unexpected error when unmarshalling elasticsearch chess game.";
	private static final String BULK_UNEXPECTED_ERROR = "Unexpected error when running the bulk index operation on pgn file %d.";

	private static final Logger LOG = Logger.getLogger(TwicPgnEsService.class.getName());

	@Value("${es.batch.size:50}")
	private int batchSize;

	@Inject
	private TwicPgnParseService parseService;

	@Inject
	private RestHighLevelClient client;

	public EsOperationResult savePgnGamesFromPgnTwicFile(final int pgnFileNumber) {
		// 1. check if the games do not already exist
		// TODO when ES setup is done

		// 2. parse games from saved twic file
		ParseOperationResult parseResult = parseService.parsePgnFile(pgnFileNumber);

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

			IndexRequest indexRequest = new IndexRequest(TwicConstants.TWIC_ALIAS)
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
