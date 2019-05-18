package com.adrian.twic.constants;

/**
 * Some constants used by the "twic" services application.
 */
public class TwicConstants {

	private TwicConstants() {
	};

	public static final int START_PGN_ZIP_COUNTER = 920;

	// Base URL where the zipped pgn files are located, this url contains a
	// placeholder for the number value of the pgn archive.
	public static final String PGN_ZIP_BASE_URL = "http://theweekinchess.com/zips/twic%sg.zip";

	public static final String PGN_FOLDER_NAME = "twic_pgns";

	public static final String PGN_FILE_NAME = "twic%s.pgn";

	public static final int PGN_DOWNLOAD_FAIL_BREAKER = 5;

	public static final String DEFAULT_CHARSET = "UTF-8";

	public static final String EMPTY_STRING = "";

	public static final String TWIC_ALIAS = "twic";

	public static final String CHESS_DATA = "chessdata";

	public static final String TWIC_NUMBER_FIELD_NAME = "twicMetadata.twicNumber";

	// Status codes ------------------------------------------
	public static final int SUCCESS_CODE = 200;
	public static final int PGN_EXISTS_CODE = 201;
	public static final int DOWNLOAD_FAIL_CODE = 300;
	public static final int UNZIP_FAIL_CODE = 301;
	public static final int PARSE_FAIL_CODE = 302;
	public static final int PARSE_FAIL_NO_FILE_CODE = 303;
	public static final int NO_PGN_FILES_CODE = 304;
	public static final int COULD_NOT_RETRIEVE_FILE_FROM_STORAGE_CODE = 305;
	public static final int FILE_DOES_NOT_EXIST_IN_STORAGE_CODE = 306;

	// Status messages ------------------------------------------
	public static final String SUCCESS_MESSAGE = "OK.";
	public static final String PGN_EXISTS_MESSAGE = "Pgn file %s already exists, will not override.";
	public static final String DOWNLOAD_FAIL_MESSAGE = "Download failed for pgn file number %s.";
	public static final String UNZIP_FAIL_MESSAGE = "Unzip failed for pgn file number %s.";
	public static final String PARSE_FAIL_MESSAGE = "Parse failed for pgn file number %s.";
	public static final String PARSE_FAIL_NO_FILE_MESSAGE = "Parse failed for pgn file number %s,because the pgn file to parse does not exist.";
	public static final String NO_PGN_FILES_MESSAGE = "No indexing will happn because no pgn files have been downloaded yet.";
	public static final String COULD_NOT_RETRIEVE_FILE_FROM_STORAGE_MESSAGE = "Could not retrieve file from storage.";
	public static final String FILE_DOES_NOT_EXIST_IN_STORAGE_MESSAGE = "Requested file does not exist in storage.";

}
