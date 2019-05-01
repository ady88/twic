package com.adrian.twic.domain;

/**
 * Unique identification for a chess game.
 */
public class PgnChessGameId {
	private String eventName;
	private String eventLocation;

	// format yyyy.mm.dd
	private String date;

	private String round;

	private String whitePlayerName;

	private String blackPlayerName;

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getWhitePlayerName() {
		return whitePlayerName;
	}

	public void setWhitePlayerName(String whitePlayerName) {
		this.whitePlayerName = whitePlayerName;
	}

	public String getBlackPlayerName() {
		return blackPlayerName;
	}

	public void setBlackPlayerName(String blackPlayerName) {
		this.blackPlayerName = blackPlayerName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blackPlayerName == null) ? 0 : blackPlayerName.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((eventLocation == null) ? 0 : eventLocation.hashCode());
		result = prime * result + ((eventName == null) ? 0 : eventName.hashCode());
		result = prime * result + ((round == null) ? 0 : round.hashCode());
		result = prime * result + ((whitePlayerName == null) ? 0 : whitePlayerName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PgnChessGameId other = (PgnChessGameId) obj;
		if (blackPlayerName == null) {
			if (other.blackPlayerName != null)
				return false;
		} else if (!blackPlayerName.equals(other.blackPlayerName))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (eventLocation == null) {
			if (other.eventLocation != null)
				return false;
		} else if (!eventLocation.equals(other.eventLocation))
			return false;
		if (eventName == null) {
			if (other.eventName != null)
				return false;
		} else if (!eventName.equals(other.eventName))
			return false;
		if (round == null) {
			if (other.round != null)
				return false;
		} else if (!round.equals(other.round))
			return false;
		if (whitePlayerName == null) {
			if (other.whitePlayerName != null)
				return false;
		} else if (!whitePlayerName.equals(other.whitePlayerName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[Event \"" + eventName + "\"]\n[Site \"" + eventLocation + "\"]\n[Date \"" + date + "\"]\n[Round \""
				+ round + "\"]\n[White \"" + whitePlayerName + "\"]\n[Black \"" + blackPlayerName + "\"]\n";
	}

}
