package com.adrian.twic.services;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.adrian.twic.constants.TwicConstants;
import com.adrian.twic.domain.ParseOperationResult;
import com.adrian.twic.domain.PgnChessGame;
import com.adrian.twic.enums.OperationType;

@Service
public final class TwicPgnParseService {

	/**
	 * Parse the pgn file with the given number.
	 * 
	 * @param pgnFileNumber
	 * @return parse operation result as the parse status and a list of
	 *         {@link PgnChessGame} objects if the parsing was successful
	 */
	public ParseOperationResult parsePgnFile(final int pgnFileNumber) {
		final var basePath = URLDecoder.decode(this.getClass().getClassLoader().getResource(".").getFile(),
				Charset.forName(TwicConstants.DEFAULT_CHARSET));

		final var fullPath = FilenameUtils.concat(basePath, TwicConstants.PGN_FOLDER_NAME);

		String pgnFileFullPath = FilenameUtils.concat(fullPath,
				String.format(TwicConstants.PGN_FILE_NAME, pgnFileNumber));

		try {
			Path path = Paths.get(pgnFileFullPath);

			try (Stream<String> lines = Files.lines(path)) {
				String data = lines.collect(Collectors.joining("\n"));
				System.out.println(data);
			}
		} catch (IOException e) {
			return ParseOperationResult.of(TwicConstants.PARSE_FAIL_CODE,
					String.format(TwicConstants.PARSE_FAIL_MESSAGE, pgnFileNumber), OperationType.PARSE_PGN, null);
		}

		return ParseOperationResult.of(TwicConstants.SUCCESS_CODE,
				String.format(TwicConstants.SUCCESS_MESSAGE, pgnFileNumber), OperationType.PARSE_PGN, null);
	}
}
