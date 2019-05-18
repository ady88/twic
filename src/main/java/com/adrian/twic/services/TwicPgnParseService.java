package com.adrian.twic.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adrian.twic.constants.TwicConstants;
import com.adrian.twic.domain.ParseOperationResult;
import com.adrian.twic.domain.PgnChessGame;
import com.adrian.twic.domain.PgnChessGameMetadata;
import com.adrian.twic.enums.OperationType;
import com.adrian.twic.helpers.PGNMetadataParseHelper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;

@Service
public final class TwicPgnParseService {

	@Inject
	private Storage cloudStorage;

	@Value("${twic.files.basePath}")
	private String basePath;

	@Value("${google.cloud.storage.bucket.name:twic}")
	public String bucketName;

	/**
	 * Parse the pgn file with the given number.
	 * 
	 * @param pgnFileNumber
	 * @return parse operation result as the parse status and a list of
	 *         {@link PgnChessGame} objects if the parsing was successful
	 */
	public ParseOperationResult parsePgnFile(final int pgnFileNumber) {

		Blob blob = null;

		try {
			blob = cloudStorage.get(BlobId.of(bucketName, Integer.toString(pgnFileNumber)));
		} catch (StorageException e) {
			return ParseOperationResult.of(TwicConstants.COULD_NOT_RETRIEVE_FILE_FROM_STORAGE_CODE,
					String.format(TwicConstants.COULD_NOT_RETRIEVE_FILE_FROM_STORAGE_MESSAGE, pgnFileNumber),
					OperationType.PARSE_PGN, null);
		}

		if (blob == null) {
			return ParseOperationResult.of(TwicConstants.FILE_DOES_NOT_EXIST_IN_STORAGE_CODE,
					String.format(TwicConstants.FILE_DOES_NOT_EXIST_IN_STORAGE_MESSAGE, pgnFileNumber),
					OperationType.PARSE_PGN, null);
		}

		String savedDoc = new String(blob.getContent());

		final List<PgnChessGame> result = new ArrayList<PgnChessGame>();

		List<String> tempLines = savedDoc.lines().collect(Collectors.toList());
		result.addAll(getSeparateGames(tempLines));

		return ParseOperationResult.of(TwicConstants.SUCCESS_CODE,
				String.format(TwicConstants.SUCCESS_MESSAGE, pgnFileNumber), OperationType.PARSE_PGN, result);
	}

	private List<PgnChessGame> getSeparateGames(final List<String> lines) {
		boolean isEvenLineBreak = true;

		final var result = new ArrayList<PgnChessGame>();
		PgnChessGameMetadata metadata = new PgnChessGameMetadata();

		String game = null;

		for (String line : lines) {

			if (isEvenLineBreak) {
				if (line.equals(TwicConstants.EMPTY_STRING)) {
					isEvenLineBreak = !isEvenLineBreak;
					game = TwicConstants.EMPTY_STRING;
				} else {
					PGNMetadataParseHelper.parseMetadata(metadata, line);
				}
			} else {
				if (line.equals(TwicConstants.EMPTY_STRING)) {
					isEvenLineBreak = !isEvenLineBreak;
					PgnChessGame chessGameInfo = PgnChessGame.builder().withChessMovesRaw(game).withMetadata(metadata)
							.build();
					result.add(chessGameInfo);
					metadata = new PgnChessGameMetadata();
				} else {
					game += line;
					game += " ";
				}
			}
		}

		return result;
	}
}
