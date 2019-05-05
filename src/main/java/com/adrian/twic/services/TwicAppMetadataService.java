package com.adrian.twic.services;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import com.adrian.twic.domain.TwicAppMetadataInfo;

/**
 * Holds some meta information for the main TWIC application.
 */
@Component
public class TwicAppMetadataService {

	private final AtomicReference<TwicAppMetadataInfo> twicAppMetadata = new AtomicReference<TwicAppMetadataInfo>(
			TwicAppMetadataInfo.of(0, 0, ""));

	public AtomicReference<TwicAppMetadataInfo> getTwicAppMetadata() {
		return twicAppMetadata;
	}

}
