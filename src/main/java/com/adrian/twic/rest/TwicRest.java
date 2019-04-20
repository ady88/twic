package com.adrian.twic.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.adrian.twic.services.TwicPgnParseService;
import com.adrian.twic.services.TwicPgnZipDownloadService;

/**
 * Holds all available TWIC REST endpoints.
 */
@Service
@Path("twic")
public class TwicRest {

	@Inject
	public TwicPgnZipDownloadService pgnDownloader;

	@Inject
	public TwicPgnParseService pgnParser;

	@GET
	@Path("download")
	@Produces("text/plain")
	public Response downloadAll() {

		var status = pgnDownloader.downloadTwicAll();

		return Response.ok(status.toString()).build();
	}

	@GET
	@Path("download/{pgnFileNumber}")
	@Produces("text/plain")
	public Response downloadPgn(@PathParam("pgnFileNumber") final int pgnFileNumber) {
		var status = pgnDownloader.downloadTwic(pgnFileNumber);

		return Response.ok(status.toString()).build();
	}

	@GET
	@Path("parse/{pgnFileNumber}")
	@Produces("text/plain")
	public Response parsePgn(@PathParam("pgnFileNumber") final int pgnFileNumber) {
		var status = pgnParser.parsePgnFile(pgnFileNumber);

		return Response.ok(status.toString()).build();
	}
}
