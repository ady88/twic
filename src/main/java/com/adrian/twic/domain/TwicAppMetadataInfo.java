package com.adrian.twic.domain;

public class TwicAppMetadataInfo {
	private int latestDownloadedPgnFileNumber;
	private int latestIndexedPgnFileNumber;
	private String dateLatestIndexedPgnFileNumber;

	private TwicAppMetadataInfo(final int latestDownloadedPgnFileNumber, final int latestIndexedPgnFileNumber,
			final String dateLatestIndexedPgnFileNumber) {
		this.latestDownloadedPgnFileNumber = latestDownloadedPgnFileNumber;
		this.dateLatestIndexedPgnFileNumber = dateLatestIndexedPgnFileNumber;
		this.latestIndexedPgnFileNumber = latestIndexedPgnFileNumber;
	}

	public static TwicAppMetadataInfo of(final int latestDownloadedPgnFileNumber, final int latestIndexedPgnFileNumber,
			final String dateLatestIndexedPgnFileNumber) {
		return new TwicAppMetadataInfo(latestDownloadedPgnFileNumber, latestIndexedPgnFileNumber,
				dateLatestIndexedPgnFileNumber);
	}

	public int getLatestDownloadedPgnFileNumber() {
		return latestDownloadedPgnFileNumber;
	}

	public void setLatestDownloadedPgnFileNumber(int latestDownloadedPgnFileNumber) {
		this.latestDownloadedPgnFileNumber = latestDownloadedPgnFileNumber;
	}

	public int getLatestIndexedPgnFileNumber() {
		return latestIndexedPgnFileNumber;
	}

	public void setLatestIndexedPgnFileNumber(int latestIndexedPgnFileNumber) {
		this.latestIndexedPgnFileNumber = latestIndexedPgnFileNumber;
	}

	public String getDateLatestIndexedPgnFileNumber() {
		return dateLatestIndexedPgnFileNumber;
	}

	public void setDateLatestIndexedPgnFileNumber(String dateLatestIndexedPgnFileNumber) {
		this.dateLatestIndexedPgnFileNumber = dateLatestIndexedPgnFileNumber;
	}

}
