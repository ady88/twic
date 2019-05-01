package com.adrian.twic.domain.es;

import com.adrian.twic.domain.PgnChessGame;
import com.adrian.twic.domain.PgnTwicMetadata;

/**
 * Holds information about a chess game, this information will be stored in
 * elasticsearch.
 */
public class EsPgnChessGame {
	private final PgnChessGame pgnGameData;
	private final PgnTwicMetadata twicMetadata;

	private EsPgnChessGame(final PgnChessGame pgnGameData, final PgnTwicMetadata metadata) {
		this.pgnGameData = pgnGameData;
		this.twicMetadata = metadata;
	}

	public static EsPgnChessGame of(final PgnChessGame pgnGameData, final PgnTwicMetadata metadata) {
		return new EsPgnChessGame(pgnGameData, metadata);
	}

	public PgnChessGame getPgnGameData() {
		return pgnGameData;
	}

	public PgnTwicMetadata getTwicMetadata() {
		return twicMetadata;
	}
}