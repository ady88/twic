package com.adrian.twic.constants;

/**
 * Some constants used by the "twic" services application.
 */
public class TwicConstants {
	public static final int START_PGN_ZIP_COUNTER = 920;

	// Base URL where the zipped pgn files are located, this url contains a
	// placeholder for the number value of the pgn archive.
	public static final String PGN_ZIP_BASE_URL = "http://theweekinchess.com/zips/twic%sg.zip";

	public static final String PGN_FOLDER_NAME = "twic_pgns";

	public static final String PGN_FILE_NAME = "twic%s.pgn";

	public static final int PGN_DOWNLOAD_FAIL_BREAKER = 5;

	public static final String DEFAULT_CHARSET = "UTF-8";
	
	public static final String EMPTY_STRING = "";

	// Status codes ------------------------------------------
	public static final int SUCCESS_CODE = 200;
	public static final int PGN_EXISTS_CODE = 201;
	public static final int DOWNLOAD_FAIL_CODE = 300;
	public static final int UNZIP_FAIL_CODE = 301;
	public static final int PARSE_FAIL_CODE = 301;

	// Status messages ------------------------------------------
	public static final String SUCCESS_MESSAGE = "OK.";
	public static final String PGN_EXISTS_MESSAGE = "Pgn file %s already exists, will not override.";
	public static final String DOWNLOAD_FAIL_MESSAGE = "Download failed for pgn file number %s.";
	public static final String UNZIP_FAIL_MESSAGE = "Unzip failed for pgn file number %s.";
	public static final String PARSE_FAIL_MESSAGE = "Parse failed for pgn file number %s.";

}
