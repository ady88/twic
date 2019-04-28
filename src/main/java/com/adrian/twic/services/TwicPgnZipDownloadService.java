package com.adrian.twic.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.adrian.twic.constants.TwicConstants;
import com.adrian.twic.domain.OperationStatus;
import com.adrian.twic.enums.OperationType;
import com.adrian.twic.helpers.ZipHelper;

/**
 * Service used to download and unzip pgns from
 * "http://theweekinchess.com/twic".
 */
@Service
public final class TwicPgnZipDownloadService {

	private static final Logger LOG = Logger.getLogger(TwicPgnZipDownloadService.class.getName());

	@Value("${twic.files.basePath}")
	private String basePath;

	@Inject
	private TwicAdapter twicAdapter;

	/**
	 * Download specified pgn file from twic.
	 * 
	 * @param pgnFileNumber
	 * @return status of the pgn file download
	 */
	public OperationStatus downloadTwic(final int pgnFileNumber) {
		byte[] downloadedBytes;

		final var pgnFolderPath = basePath + TwicConstants.PGN_FOLDER_NAME;
		final var path = FilenameUtils.concat(pgnFolderPath, String.format(TwicConstants.PGN_FILE_NAME, pgnFileNumber));

		if (Files.exists(Paths.get(path))) {
			final var fileExistsStatus = OperationStatus.of(TwicConstants.PGN_EXISTS_CODE,
					String.format(TwicConstants.PGN_EXISTS_MESSAGE, pgnFileNumber),
					OperationType.DOWNLOAD_AND_EXTRACT_ZIP_PGN);
			return fileExistsStatus;
		}

		try {
			downloadedBytes = twicAdapter.getTwicResponseForFile(pgnFileNumber);
		} catch (IOException | RestClientException e) {
			final var downloadFailStatus = OperationStatus.of(TwicConstants.DOWNLOAD_FAIL_CODE,
					String.format(TwicConstants.DOWNLOAD_FAIL_MESSAGE, pgnFileNumber),
					OperationType.DOWNLOAD_AND_EXTRACT_ZIP_PGN);
			return downloadFailStatus;
		}

		try {
			ZipHelper.unzip(downloadedBytes, pgnFolderPath);
		} catch (IOException e) {
			return OperationStatus.of(TwicConstants.UNZIP_FAIL_CODE,
					String.format(TwicConstants.UNZIP_FAIL_MESSAGE, pgnFileNumber),
					OperationType.DOWNLOAD_AND_EXTRACT_ZIP_PGN);
		}

		return OperationStatus.of(TwicConstants.SUCCESS_CODE, TwicConstants.SUCCESS_MESSAGE,
				OperationType.DOWNLOAD_AND_EXTRACT_ZIP_PGN);
	}

	/**
	 * Download pgn files from twic until a breaker condition is met. The breaker
	 * condition is that a predefined number of pgn downloads fail. If a pgn
	 * download fails the pgn file number will be incremented.
	 * 
	 * @param pgnFileNumber
	 * @return status of the pgn file download
	 */
	public OperationStatus downloadTwicAll() {
		var pgnNumber = TwicConstants.START_PGN_ZIP_COUNTER;
		var lastSuccessfull = 0;
		var failNumber = 0;

		while (failNumber < TwicConstants.PGN_DOWNLOAD_FAIL_BREAKER) {
			var status = downloadTwic(pgnNumber);

			// Check if file was not successfully downloaded or if it does not already exist
			if (status.getCode() != 200 && status.getCode() != 201) {
				failNumber++;
			} else {
				lastSuccessfull = pgnNumber;
			}

			LOG.info(status.toString());

			pgnNumber++;
		}

		LOG.info("Last successful pgn file download is " + lastSuccessfull);

		return OperationStatus.of(TwicConstants.SUCCESS_CODE, TwicConstants.SUCCESS_MESSAGE,
				OperationType.DOWNLOAD_AND_EXTRACT_ZIP_PGN_ALL);
	}

}
