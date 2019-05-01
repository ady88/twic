package com.adrian.twic.domain;

/**
 * Holds information about the twic file number and the actual pgn chess game
 * string.
 */
public class PgnTwicMetadata {
	private final String twicNumber;
	private final String rawPgn;

	private PgnTwicMetadata(final String twicNumber, final String rawPgn) {
		this.twicNumber = twicNumber;
		this.rawPgn = rawPgn;
	}

	public static PgnTwicMetadata of(final String twicNumber, final String rawPgn) {
		return new PgnTwicMetadata(twicNumber, rawPgn);
	}

	public String getTwicNumber() {
		return twicNumber;
	}

	public String getRawPgn() {
		return rawPgn;
	}
}
