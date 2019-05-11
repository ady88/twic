package com.adrian.twic.services;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.adrian.twic.constants.TwicConstants;
import com.adrian.twic.domain.OperationStatus;
import com.adrian.twic.enums.OperationType;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;

/**
 * Service used to download and unzip pgns from
 * "http://theweekinchess.com/twic".
 */
@Service
public final class TwicPgnZipDownloadService {

	private static final String COULD_NOT_STORE_ON_THE_CLOUD_MESSAGE = "Could not store twic file number '%s' on the cloud.";

	private static final String STARTED_DOWNLOAD_MESSAGE = "Started download of all pgn files.";

	private static final String FINISHED_DOWNLOAD_MESSAGE = "Finished download of all pgn files";

	private static final int BUFFER_SIZE = 4096;

	private static final Logger LOG = Logger.getLogger(TwicPgnZipDownloadService.class.getName());

	@Value("${twic.files.basePath}")
	private String basePath;

	@Inject
	private TwicAdapter twicAdapter;

	@Inject
	private Storage cloudStorage;

	@Inject
	private TwicAppMetadataService metadataService;

	@Value("${google.cloud.storage.bucket.name:twic}")
	public String bucketName;

	/**
	 * Download specified pgn file from twic.
	 * 
	 * @param pgnFileNumber
	 * @return status of the pgn file download
	 */
	public OperationStatus downloadTwic(final int pgnFileNumber) {
		byte[] downloadedBytes;

//		final var pgnFolderPath = basePath + TwicConstants.PGN_FOLDER_NAME;
//		final var path = FilenameUtils.concat(pgnFolderPath, String.format(TwicConstants.PGN_FILE_NAME, pgnFileNumber));

		if (cloudStorage.get(bucketName, Integer.toString(pgnFileNumber)) != null) {
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
			unzip(downloadedBytes, Integer.toString(pgnFileNumber));
		} catch (IOException e) {
			LOG.severe("Unzip operation failed with message: " + e.getMessage());

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
	@Scheduled(cron = "0 0/25 * * * *")
	public OperationStatus downloadTwicAll() {
		var pgnNumber = TwicConstants.START_PGN_ZIP_COUNTER;
		var lastSuccessfull = 0;
		var failNumber = 0;

		LOG.info(STARTED_DOWNLOAD_MESSAGE);

		while (failNumber < TwicConstants.PGN_DOWNLOAD_FAIL_BREAKER) {
			var status = downloadTwic(pgnNumber);

			// Check if file was not successfully downloaded or if it does not already exist
			if (status.getCode() != 200 && status.getCode() != 201) {
				failNumber++;
			} else {
				lastSuccessfull = pgnNumber;
			}

			if (status.getCode() != TwicConstants.PGN_EXISTS_CODE) {
				LOG.info(status.toString());
			}

			pgnNumber++;
		}

		LOG.info("Last successful pgn file download is " + lastSuccessfull);
		metadataService.getTwicAppMetadata().get().setLatestDownloadedPgnFileNumber(lastSuccessfull);
		LOG.info(FINISHED_DOWNLOAD_MESSAGE);
		return OperationStatus.of(TwicConstants.SUCCESS_CODE, TwicConstants.SUCCESS_MESSAGE,
				OperationType.DOWNLOAD_AND_EXTRACT_ZIP_PGN_ALL);
	}

	private void unzip(byte[] data, String pgnFileNumber) throws IOException {

		try (final var zipIn = new ZipInputStream(new ByteArrayInputStream(data))) {
			var entry = zipIn.getNextEntry();

			while (entry != null) {

				if (!entry.isDirectory()) {
					// if the entry is a file, extracts it
					saveToStorage(zipIn, pgnFileNumber);
				}

				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
		}
	}


	private void saveToStorage(ZipInputStream zipIn, String pgnFileNumber) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (var bos = new BufferedOutputStream(byteArrayOutputStream)) {
			byte[] bytesIn = new byte[BUFFER_SIZE];
			var read = 0;
			while ((read = zipIn.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}
		}

		BlobId blobId = BlobId.of(bucketName, pgnFileNumber);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
		try {
			cloudStorage.create(blobInfo, byteArrayOutputStream.toByteArray());
		} catch (StorageException e) {
			LOG.severe(String.format(COULD_NOT_STORE_ON_THE_CLOUD_MESSAGE, pgnFileNumber));
		}
	}

}
