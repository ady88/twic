package com.adrian.twic.domain;

import java.util.List;

import com.adrian.twic.enums.OperationType;

/**
 * Holds all of the pgn parsed chess game information and the status message of
 * the parsing operation.
 */
public class ParseOperationResult extends OperationStatus {
	private List<PgnChessGame> chessGames;

	private ParseOperationResult(final int code, final String text, final OperationType operationType,
			List<PgnChessGame> chessGames) {
		super(code, text, operationType);
		this.chessGames = chessGames;
	}

	public List<PgnChessGame> getChessGames() {
		return chessGames;
	}

	public void setChessGames(List<PgnChessGame> chessGames) {
		this.chessGames = chessGames;
	}

	public static ParseOperationResult of(final int code, final String text, final OperationType operationType,
			List<PgnChessGame> chessGames) {
		return new ParseOperationResult(code, text, operationType, chessGames);
	}
}
