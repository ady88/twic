package com.adrian.twic.domain;

import com.adrian.twic.enums.ChessMetadataType;

/**
 * Holds all pgn information regarding a chess game metadata.
 */
public class PgnChessGameMetadata {
	private PgnChessGameId chessGameId = new PgnChessGameId();

	private String result;

	private String whiteTitle;

	private String blackTitle;

	private String whiteElo;

	private String blackElo;

	private String ECO;

	private String opening;

	private String variation;

	private String whiteFideId;

	private String blackFideId;

	// format yyyy.mm.dd
	private String eventDate;

	public PgnChessGameId getChessGameId() {
		return chessGameId;
	}

	public void setChessGameId(PgnChessGameId chessGameId) {
		this.chessGameId = chessGameId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getWhiteTitle() {
		return whiteTitle;
	}

	public void setWhiteTitle(String whiteTitle) {
		this.whiteTitle = whiteTitle;
	}

	public String getBlackTitle() {
		return blackTitle;
	}

	public void setBlackTitle(String blackTitle) {
		this.blackTitle = blackTitle;
	}

	public String getWhiteElo() {
		return whiteElo;
	}

	public void setWhiteElo(String whiteElo) {
		this.whiteElo = whiteElo;
	}

	public String getBlackElo() {
		return blackElo;
	}

	public void setBlackElo(String blackElo) {
		this.blackElo = blackElo;
	}

	public String getECO() {
		return ECO;
	}

	public void setECO(String eCO) {
		ECO = eCO;
	}

	public String getOpening() {
		return opening;
	}

	public void setOpening(String opening) {
		this.opening = opening;
	}

	public String getVariation() {
		return variation;
	}

	public void setVariation(String variation) {
		this.variation = variation;
	}

	public String getWhiteFideId() {
		return whiteFideId;
	}

	public void setWhiteFideId(String whiteFideId) {
		this.whiteFideId = whiteFideId;
	}

	public String getBlackFideId() {
		return blackFideId;
	}

	public void setBlackFideId(String blackFideId) {
		this.blackFideId = blackFideId;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public void setField(final String fieldName, final String fieldValue) {
		final ChessMetadataType metadataType = ChessMetadataType.fromString(fieldName);

		switch (metadataType) {
		case BLACK:
			this.chessGameId.setBlackPlayerName(fieldValue);
			break;
		case BLACK_ELO:
			this.setBlackElo(fieldValue);
			break;
		case BLACK_FIDE_ID:
			this.setBlackFideId(fieldValue);
			break;
		case BLACK_TITLE:
			this.setBlackTitle(fieldValue);
			break;
		case DATE:
			this.chessGameId.setDate(fieldValue);
			break;
		case ECO:
			this.setECO(fieldValue);
			break;
		case EVENT:
			this.chessGameId.setEventName(fieldValue);
			break;
		case EVENT_DATE:
			this.setEventDate(fieldValue);
			break;
		case OPENING:
			this.setOpening(fieldValue);
			break;
		case RESULT:
			this.setResult(fieldValue);
			break;
		case ROUND:
			this.chessGameId.setRound(fieldValue);
			break;
		case SITE:
			this.chessGameId.setEventLocation(fieldValue);
			break;
		case UNKNOWN:
			break;
		case VARIATION:
			this.setVariation(fieldValue);
			break;
		case WHITE:
			this.chessGameId.setWhitePlayerName(fieldValue);
			break;
		case WHITE_ELO:
			this.setWhiteElo(fieldValue);
			break;
		case WHITE_FIDE_ID:
			this.setWhiteFideId(fieldValue);
			break;
		case WHITE_TITLE:
			this.setWhiteTitle(fieldValue);
			break;
		default:
			break;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ECO == null) ? 0 : ECO.hashCode());
		result = prime * result + ((blackElo == null) ? 0 : blackElo.hashCode());
		result = prime * result + ((blackFideId == null) ? 0 : blackFideId.hashCode());
		result = prime * result + ((blackTitle == null) ? 0 : blackTitle.hashCode());
		result = prime * result + ((chessGameId == null) ? 0 : chessGameId.hashCode());
		result = prime * result + ((eventDate == null) ? 0 : eventDate.hashCode());
		result = prime * result + ((opening == null) ? 0 : opening.hashCode());
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		result = prime * result + ((variation == null) ? 0 : variation.hashCode());
		result = prime * result + ((whiteElo == null) ? 0 : whiteElo.hashCode());
		result = prime * result + ((whiteFideId == null) ? 0 : whiteFideId.hashCode());
		result = prime * result + ((whiteTitle == null) ? 0 : whiteTitle.hashCode());
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
		PgnChessGameMetadata other = (PgnChessGameMetadata) obj;
		if (ECO == null) {
			if (other.ECO != null)
				return false;
		} else if (!ECO.equals(other.ECO))
			return false;
		if (blackElo == null) {
			if (other.blackElo != null)
				return false;
		} else if (!blackElo.equals(other.blackElo))
			return false;
		if (blackFideId == null) {
			if (other.blackFideId != null)
				return false;
		} else if (!blackFideId.equals(other.blackFideId))
			return false;
		if (blackTitle == null) {
			if (other.blackTitle != null)
				return false;
		} else if (!blackTitle.equals(other.blackTitle))
			return false;
		if (chessGameId == null) {
			if (other.chessGameId != null)
				return false;
		} else if (!chessGameId.equals(other.chessGameId))
			return false;
		if (eventDate == null) {
			if (other.eventDate != null)
				return false;
		} else if (!eventDate.equals(other.eventDate))
			return false;
		if (opening == null) {
			if (other.opening != null)
				return false;
		} else if (!opening.equals(other.opening))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		if (variation == null) {
			if (other.variation != null)
				return false;
		} else if (!variation.equals(other.variation))
			return false;
		if (whiteElo == null) {
			if (other.whiteElo != null)
				return false;
		} else if (!whiteElo.equals(other.whiteElo))
			return false;
		if (whiteFideId == null) {
			if (other.whiteFideId != null)
				return false;
		} else if (!whiteFideId.equals(other.whiteFideId))
			return false;
		if (whiteTitle == null) {
			if (other.whiteTitle != null)
				return false;
		} else if (!whiteTitle.equals(other.whiteTitle))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return chessGameId + "[Result \"" + result + "\"]\n[WhiteTitle \"" + whiteTitle + "\"]\n[BlackTitle \""
				+ blackTitle + "\"]\n[WhiteElo \"" + whiteElo + "\"]\n[BlackElo \"" + blackElo + "\"]\n[ECO \"" + ECO
				+ "\"]\n[Opening \"" + opening + "\"]\n[Variation \"" + variation + "\"]\n[WhiteFideId \"" + whiteFideId
				+ "\"]\n[BlackFideId \"" + blackFideId + "\"]\n[EventDate \"" + eventDate + "\"]\n";
	}

}
