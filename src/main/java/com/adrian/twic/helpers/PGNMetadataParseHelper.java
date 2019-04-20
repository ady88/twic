package com.adrian.twic.helpers;

import org.springframework.util.StringUtils;

import com.adrian.twic.domain.PgnChessGameMetadata;

/**
 * Helper class that holds methods used to parse PGN metadata information.
 */
public final class PGNMetadataParseHelper {
	private PGNMetadataParseHelper() {
	}

	/**
	 * Update the given {@link PgnChessGameMetadata} object with info from the given
	 * {@link String} object.
	 * 
	 * Pgn metadata line looks like: [XYZ fsadfasdf]
	 * 
	 * @param metadata
	 * @param line
	 */
	public static void parseMetadata(final PgnChessGameMetadata metadata, String line) {
		if (StringUtils.isEmpty(line)) {
			return;
		}

		// remove square brackets
		line = line.substring(1, line.length() - 1);
		String[] result = line.split(" ", 2);

		if (result.length != 2 || StringUtils.isEmpty(result[1])) {
			return;
		}

		// the value we need is in the second element of the array
		String resultValue = result[1].replaceAll("\"", "");
		metadata.setField(result[0], resultValue);
	}
}