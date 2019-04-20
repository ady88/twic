package com.adrian.twic;

import org.junit.Assert;
import org.junit.Test;

import com.adrian.twic.domain.PgnChessGameId;
import com.adrian.twic.domain.PgnChessGameMetadata;
import com.adrian.twic.helpers.PGNMetadataParseHelper;

public class PgnMetadataParseTest {

	private static final String EVENT_NAME = "[Event \"Cez Trophy 2012\"]";
	private static final String EVENT_LOCATION = "[Site \"Prague CZE\"]";
	private static final String GAME_DATE = "[Date \"2012.06.20\"]";
	private static final String EVENT_DATE = "[EventDate \"2012.06.20\"]";
	private static final String EVENT_ROUND = "[Round \"1\"]";
	private static final String RESULT = "[Result \"1/2-1/2\"]";
	private static final String ECO = "[ECO \"B38\"]";
	private static final String OPENING = "[Opening \"Sicilian\"]";
	private static final String VARIATION = "[Variation \"accelerated fianchetto, Maroczy bind, 6.Be3\"]";
	private static final String WHITE_PLAYER_NAME = "[White \"Navara,D\"]";
	private static final String BLACK_PLAYER_NAME = "[Black \"Svidler,P\"]";
	private static final String WHITE_PLAYER_TITLE = "[WhiteTitle \"GM\"]";
	private static final String BLACK_PLAYER_TITLE = "[BlackTitle \"GM\"]";
	private static final String WHITE_PLAYER_ELO = "[WhiteElo \"2706\"]";
	private static final String BLACK_PLAYER_ELO = "[BlackElo \"2741\"]";
	private static final String WHITE_PLAYER_FIDE = "[WhiteFideId \"309095\"]";
	private static final String BLACK_PLAYER_FIDE = "[BlackFideId \"4102142\"]";
	private static final String ACTUAL_EVENT_NAME = "Cez Trophy 2012";
	private static final String ACTUAL_EVENT_LOCATION = "Prague CZE";
	private static final String ACTUAL_GAME_DATE = "2012.06.20";
	private static final String ACTUAL_EVENT_DATE = "2012.06.20";
	private static final String ACTUAL_EVENT_ROUND = "1";
	private static final String ACTUAL_WHITE_PLAYER_NAME = "Navara,D";
	private static final String ACTUAL_BLACK_PLAYER_NAME = "Svidler,P";
	private static final String ACTUAL_WHITE_PLAYER_TITLE = "GM";
	private static final String ACTUAL_BLACK_PLAYER_TITLE = "GM";
	private static final String ACTUAL_WHITE_PLAYER_ELO = "2706";
	private static final String ACTUAL_BLACK_PLAYER_ELO = "2741";
	private static final String ACTUAL_WHITE_PLAYER_FIDE = "309095";
	private static final String ACTUAL_BLACK_PLAYER_FIDE = "4102142";
	private static final String ACTUAL_RESULT = "1/2-1/2";
	private static final String ACTUAL_ECO = "B38";
	private static final String ACTUAL_OPENING = "Sicilian";
	private static final String ACTUAL_VARIATION = "accelerated fianchetto, Maroczy bind, 6.Be3";

	@Test
	public void testParseEventName() {
		PgnChessGameMetadata metadata = new PgnChessGameMetadata();

		PGNMetadataParseHelper.parseMetadata(metadata, EVENT_NAME);
		PGNMetadataParseHelper.parseMetadata(metadata, EVENT_LOCATION);
		PGNMetadataParseHelper.parseMetadata(metadata, GAME_DATE);
		PGNMetadataParseHelper.parseMetadata(metadata, EVENT_DATE);
		PGNMetadataParseHelper.parseMetadata(metadata, EVENT_ROUND);
		PGNMetadataParseHelper.parseMetadata(metadata, WHITE_PLAYER_NAME);
		PGNMetadataParseHelper.parseMetadata(metadata, BLACK_PLAYER_NAME);
		PGNMetadataParseHelper.parseMetadata(metadata, RESULT);
		PGNMetadataParseHelper.parseMetadata(metadata, WHITE_PLAYER_TITLE);
		PGNMetadataParseHelper.parseMetadata(metadata, BLACK_PLAYER_TITLE);
		PGNMetadataParseHelper.parseMetadata(metadata, WHITE_PLAYER_ELO);
		PGNMetadataParseHelper.parseMetadata(metadata, BLACK_PLAYER_ELO);
		PGNMetadataParseHelper.parseMetadata(metadata, WHITE_PLAYER_FIDE);
		PGNMetadataParseHelper.parseMetadata(metadata, BLACK_PLAYER_FIDE);
		PGNMetadataParseHelper.parseMetadata(metadata, ECO);
		PGNMetadataParseHelper.parseMetadata(metadata, OPENING);
		PGNMetadataParseHelper.parseMetadata(metadata, VARIATION);

		PgnChessGameId chessGameId = metadata.getChessGameId();
		Assert.assertNotNull(chessGameId);

		Assert.assertNotNull(chessGameId.getEventName());
		Assert.assertNotNull(chessGameId.getEventLocation());
		Assert.assertNotNull(chessGameId.getDate());
		Assert.assertNotNull(chessGameId.getRound());
		Assert.assertNotNull(chessGameId.getWhitePlayerName());
		Assert.assertNotNull(chessGameId.getBlackPlayerName());
		Assert.assertNotNull(metadata.getResult());
		Assert.assertNotNull(metadata.getWhiteTitle());
		Assert.assertNotNull(metadata.getBlackTitle());
		Assert.assertNotNull(metadata.getWhiteElo());
		Assert.assertNotNull(metadata.getBlackElo());
		Assert.assertNotNull(metadata.getWhiteFideId());
		Assert.assertNotNull(metadata.getBlackFideId());
		Assert.assertNotNull(metadata.getECO());
		Assert.assertNotNull(metadata.getOpening());
		Assert.assertNotNull(metadata.getVariation());
		Assert.assertNotNull(metadata.getEventDate());

		Assert.assertEquals(ACTUAL_EVENT_NAME, chessGameId.getEventName());
		Assert.assertEquals(ACTUAL_EVENT_LOCATION, chessGameId.getEventLocation());
		Assert.assertEquals(ACTUAL_GAME_DATE, chessGameId.getDate());
		Assert.assertEquals(ACTUAL_EVENT_ROUND, chessGameId.getRound());
		Assert.assertEquals(ACTUAL_WHITE_PLAYER_NAME, chessGameId.getWhitePlayerName());
		Assert.assertEquals(ACTUAL_BLACK_PLAYER_NAME, chessGameId.getBlackPlayerName());
		Assert.assertEquals(ACTUAL_RESULT, metadata.getResult());
		Assert.assertEquals(ACTUAL_WHITE_PLAYER_TITLE, metadata.getWhiteTitle());
		Assert.assertEquals(ACTUAL_BLACK_PLAYER_TITLE, metadata.getBlackTitle());
		Assert.assertEquals(ACTUAL_WHITE_PLAYER_ELO, metadata.getWhiteElo());
		Assert.assertEquals(ACTUAL_BLACK_PLAYER_ELO, metadata.getBlackElo());
		Assert.assertEquals(ACTUAL_WHITE_PLAYER_FIDE, metadata.getWhiteFideId());
		Assert.assertEquals(ACTUAL_BLACK_PLAYER_FIDE, metadata.getBlackFideId());
		Assert.assertEquals(ACTUAL_ECO, metadata.getECO());
		Assert.assertEquals(ACTUAL_OPENING, metadata.getOpening());
		Assert.assertEquals(ACTUAL_VARIATION, metadata.getVariation());
		Assert.assertEquals(ACTUAL_EVENT_DATE, metadata.getEventDate());
	}
}
