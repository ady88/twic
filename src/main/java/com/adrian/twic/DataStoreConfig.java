package com.adrian.twic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.util.ResourceUtils;

import com.adrian.twic.services.TwicPgnZipDownloadService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageClass;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

@Configuration
public class DataStoreConfig {
	private static final String ERROR_CONNECTING_TO_DATASTORE_MESSAGE = "There was an issue connecting to the google data store. Exception message is: %s";

	private static final String COULD_NOT_FIND_CREDENTIALS_MESSAGE = "Could not find specified credentials file.";

	private static final String COULD_NOT_CREATE_BUCKET_MESSAGE = "Could not create a new bucket. Exception message is %s";

	private static final String STORAGE_LOCATION = "us-west1";

	private static final String BUCKET_ALREADY_EXISTS_MESSAGE = "Bucket already exists and will not be recreated.";

	private static final String CLOUD_CONNECTION_ERROR_MESSAGE = "Could not connect to google cloud storage to check if a bucket already exists";

	private static final Logger LOG = Logger.getLogger(TwicPgnZipDownloadService.class.getName());

	private static final String GOOGLE_CREDENTIALS_FILE_PATH = "crypto-meridian-240313-ec17bd7d996d.json";

	@Value("${google.cloud.storage.bucket.name:twic}")
	public String bucketName;

	@Value("${google.cloud.storage.project.id:crypto-meridian-240313}")
	public String projectId;

	private Storage storage;

	@Bean
	public Storage storage() {
		StorageOptions options = null;

		try {
			File credentialFile = ResourceUtils.getFile(String.format("classpath:%s", GOOGLE_CREDENTIALS_FILE_PATH));
			options = StorageOptions.newBuilder().setProjectId(projectId)
					.setCredentials(GoogleCredentials.fromStream(new FileInputStream(credentialFile))).build();
		} catch (FileNotFoundException e) {
			LOG.severe(COULD_NOT_FIND_CREDENTIALS_MESSAGE);
		} catch (IOException e) {
			LOG.severe(String.format(ERROR_CONNECTING_TO_DATASTORE_MESSAGE, e.getMessage()));
		}

		storage = options.getService();

		return storage;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void setupBucket() {
		boolean bucketExists = false;
		try {
			bucketExists = storage.get(bucketName, Storage.BucketGetOption.fields()) != null;
		} catch (StorageException e1) {
			LOG.severe(CLOUD_CONNECTION_ERROR_MESSAGE);
		}

		if (bucketExists) {
			LOG.info(BUCKET_ALREADY_EXISTS_MESSAGE);
			return;
		}

		try {
			storage.create(BucketInfo.newBuilder(bucketName)
					// See here for possible values: http://g.co/cloud/storage/docs/storage-classes
					.setStorageClass(StorageClass.REGIONAL)
					// Possible values: http://g.co/cloud/storage/docs/bucket-locations#location-mr
					.setLocation(STORAGE_LOCATION)
					.setDefaultAcl(Arrays.asList(Acl.of(User.ofAllAuthenticatedUsers(), Acl.Role.OWNER))).build());
		} catch (StorageException e) {
			LOG.severe(String.format(COULD_NOT_CREATE_BUCKET_MESSAGE, e.getMessage()));
		}
	}
}
