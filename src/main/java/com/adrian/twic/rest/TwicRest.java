package com.adrian.twic.rest;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.adrian.twic.services.TwicAppMetadataService;
import com.adrian.twic.services.TwicPgnEsService;
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
	public TwicPgnEsService esService;

	@Inject
	public TwicAppMetadataService metadataService;

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

	@DELETE
	@Path("remove/{pgnFileNumber}")
	@Produces("text/plain")
	public Response removePgnGames(@PathParam("pgnFileNumber") final int pgnFileNumber) {
		var status = esService.removePgnGames(pgnFileNumber);

		return Response.ok(status.toString()).build();
	}

	@GET
	@Path("index/{pgnFileNumber}")
	@Produces("text/plain")
	public Response indexPgn(@PathParam("pgnFileNumber") final int pgnFileNumber) {
		var status = esService.savePgnGamesFromPgnTwicFile(pgnFileNumber);

		return Response.ok(status.toString()).build();
	}

	@GET
	@Path("metadata")
	@Produces("application/json")
	public Response getAppMetadata(@PathParam("pgnFileNumber") final int pgnFileNumber) {
		var appMetadata = metadataService.getTwicAppMetadata().get();

		return Response.ok(appMetadata, MediaType.APPLICATION_JSON).build();
	}
}
