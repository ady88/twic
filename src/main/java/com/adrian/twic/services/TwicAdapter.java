package com.adrian.twic.services;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.adrian.twic.constants.TwicConstants;

/**
 * Component used for downloading zip pgn files from
 * http://theweekinchess.com/twic.
 */
@Component
public final class TwicAdapter {

	/**
	 * Download the specified archived pgn file and save the unzipped response to
	 * resources/twic_pgns folder.
	 * 
	 * @param pgnFileNumber
	 * @return the result status of the download and unzip operation.
	 * @throws IOException
	 */
	public byte[] getTwicResponseForFile(final int pgnFileNumber) throws IOException {
		// before downloading check if the file already exists...

		final var twicPgnDownloader = new RestTemplate();
		byte[] downloadedBytes = null;
		downloadedBytes = twicPgnDownloader.getForObject(String.format(TwicConstants.PGN_ZIP_BASE_URL, pgnFileNumber),
				byte[].class);

		return downloadedBytes;
	}
}
