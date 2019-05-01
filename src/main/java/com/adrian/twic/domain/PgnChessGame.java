package com.adrian.twic.domain;

import javax.annotation.Generated;

/**
 * Holds all pgn information regarding a chess game.
 */
public class PgnChessGame {
	private PgnChessGameMetadata metadata;

	// holds all chess moves in a single string
	private String chessMovesRaw;

	@Generated("SparkTools")
	private PgnChessGame(Builder builder) {
		this.metadata = builder.metadata;
		this.chessMovesRaw = builder.chessMovesRaw;
	}

	public PgnChessGameMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(PgnChessGameMetadata metadata) {
		this.metadata = metadata;
	}

	public String getChessMovesRaw() {
		return chessMovesRaw;
	}

	public void setChessMovesRaw(String chessMovesRaw) {
		this.chessMovesRaw = chessMovesRaw;
	}

	@Override
	public String toString() {
		return metadata + chessMovesRaw;
	}

	/**
	 * Creates builder to build {@link PgnChessGame}.
	 * 
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link PgnChessGame}.
	 */
	@Generated("SparkTools")
	public static final class Builder {
		private PgnChessGameMetadata metadata;
		private String chessMovesRaw;

		private Builder() {
		}

		public Builder withMetadata(PgnChessGameMetadata metadata) {
			this.metadata = metadata;
			return this;
		}

		public Builder withChessMovesRaw(String chessMovesRaw) {
			this.chessMovesRaw = chessMovesRaw;
			return this;
		}

		public PgnChessGame build() {
			return new PgnChessGame(this);
		}
	}
}
