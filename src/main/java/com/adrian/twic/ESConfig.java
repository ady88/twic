package com.adrian.twic;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import com.adrian.twic.constants.TwicConstants;
import com.adrian.twic.services.TwicPgnZipDownloadService;

@Configuration
public class ESConfig {

	private static final Logger LOG = Logger.getLogger(TwicPgnZipDownloadService.class.getName());

	private static final String ES_CLOSE_UNEXPECTED_ERROR_MESSAGE = "Unexpected error occured when trying to close the elasticsearch java client.";
	private static final String INDEX_CREATE_UNEXPECTED_ERROR_MESSAGE = "Unexpected error occured when trying to create an elasticsearch index.";
	private static final String ALIAS_EXISTS_UNEXPECTED_ERROR_MESSAGE = "Unexpected error occured when checking if an elasticsearch alias exists.";

	@Value("${elasticsearch.host:127.0.0.1}")
	public String host;

	@Value("${elasticsearch.port:9200}")
	public int port;

	private RestHighLevelClient client;

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	@Bean
	public RestHighLevelClient client() {
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("21uj9vm1f1", "8m6w07h5cu"));

		RestClientBuilder builder = RestClient.builder(new HttpHost(host, 9200))
				.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
					@Override
					public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
						return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
					}
				});

		client = new RestHighLevelClient(builder);

		return client;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void setupBucket() {
		boolean aliasExists = false;
		GetAliasesRequest aliasExistsRequest = new GetAliasesRequest(TwicConstants.TWIC_ALIAS);
		try {
			aliasExists = client.indices().existsAlias(aliasExistsRequest, RequestOptions.DEFAULT);
		} catch (IOException e1) {
			LOG.severe(ALIAS_EXISTS_UNEXPECTED_ERROR_MESSAGE);
		}

		if (aliasExists) {
			LOG.info("Index already exists and will not be recreated.");
			return;
		}

		LocalDate localDate = LocalDate.now();
		String date = localDate.format(DateTimeFormatter.ISO_DATE);
		CreateIndexRequest request = new CreateIndexRequest(String.format("%s_%s", TwicConstants.TWIC_ALIAS, date));
		request.alias(new Alias(TwicConstants.TWIC_ALIAS));

		try {
			client.indices().create(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			LOG.severe(INDEX_CREATE_UNEXPECTED_ERROR_MESSAGE);
		}
	}

	@PreDestroy
	public void closeEsClient() {
		try {
			client.close();
		} catch (IOException e) {
			LOG.severe(ES_CLOSE_UNEXPECTED_ERROR_MESSAGE);
		}
	}
}
