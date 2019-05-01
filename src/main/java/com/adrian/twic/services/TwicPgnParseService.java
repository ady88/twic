package com.adrian.twic.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adrian.twic.constants.TwicConstants;
import com.adrian.twic.domain.ParseOperationResult;
import com.adrian.twic.domain.PgnChessGame;
import com.adrian.twic.domain.PgnChessGameMetadata;
import com.adrian.twic.enums.OperationType;
import com.adrian.twic.helpers.PGNMetadataParseHelper;

@Service
public final class TwicPgnParseService {

	@Value("${twic.files.basePath}")
	private String basePath;

	/**
	 * Parse the pgn file with the given number.
	 * 
	 * @param pgnFileNumber
	 * @return parse operation result as the parse status and a list of
	 *         {@link PgnChessGame} objects if the parsing was successful
	 */
	public ParseOperationResult parsePgnFile(final int pgnFileNumber) {

		final var pgnFolderPath = basePath + TwicConstants.PGN_FOLDER_NAME;
		final var path = FilenameUtils.concat(pgnFolderPath, String.format(TwicConstants.PGN_FILE_NAME, pgnFileNumber));

		if (!Files.exists(Paths.get(path))) {
			return ParseOperationResult.of(TwicConstants.PARSE_FAIL_NO_FILE_CODE,
					String.format(TwicConstants.PARSE_FAIL_NO_FILE_MESSAGE, pgnFileNumber), OperationType.PARSE_PGN,
					null);
		}

		final List<PgnChessGame> result = new ArrayList<PgnChessGame>();

		try {
			List<String> tempLines = Files.lines(Paths.get(path)).collect(Collectors.toList());
			result.addAll(getSeparateGames(tempLines));
		} catch (IOException e) {
			return ParseOperationResult.of(TwicConstants.PARSE_FAIL_CODE,
					String.format(TwicConstants.PARSE_FAIL_MESSAGE, pgnFileNumber), OperationType.PARSE_PGN, null);
		}

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
