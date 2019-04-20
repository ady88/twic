package com.adrian.twic.enums;

/**
 * Holds chess metadata information.
 */
public enum ChessMetadataType {
	EVENT("Event"), SITE("Site"), DATE("Date"), ROUND("Round"), WHITE("White"), BLACK("Black"), RESULT("Result"),
	WHITE_TITLE("WhiteTitle"), BLACK_TITLE("BlackTitle"), WHITE_ELO("WhiteElo"), BLACK_ELO("BlackElo"), ECO("ECO"),
	OPENING("Opening"), VARIATION("Variation"), WHITE_FIDE_ID("WhiteFideId"), BLACK_FIDE_ID("BlackFideId"),
	EVENT_DATE("EventDate"), UNKNOWN("Unknown");

	private final String value;

	private ChessMetadataType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static ChessMetadataType fromString(String value) {
		for (ChessMetadataType metadata : ChessMetadataType.values()) {
			if (metadata.value.equalsIgnoreCase(value)) {
				return metadata;
			}
		}

		return ChessMetadataType.UNKNOWN;
	}
}
