package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.entity.Podcast;
import com.roman.podcastplayer.manage.PodcastManager;
import com.roman.podcastplayer.rest.dto.PodcastDto;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Stateless
@Path("/podcasts")
public class PodcastEndpoint {

    @Inject
    private PodcastManager podcastManager;

    @GET
    @Path("/{podcastId}")
    public Response getPodcast(@PathParam("podcastId") Integer podcastId) {
        Podcast podcast = podcastManager.findPodcastById(podcastId);

        if (podcast == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        PodcastDto podcastDto = new PodcastDto(podcast);

        return Response.ok(podcastDto).build();
    }

    @POST
    @Path("/{podcastId}")
    public Response setStarred(@PathParam("podcastId") Integer podcastId, @QueryParam("s") boolean starred) {
        podcastManager.setStarred(podcastId, starred);
        return Response.ok().build();
    }

    @PUT
    @Path("/{podcastId}/categories/{categoryId}")
    public Response addCategory(@PathParam("podcastId") Integer podcastId, @PathParam("categoryId") Integer categoryId) {

        podcastManager.addCategory(podcastId, categoryId);

        return Response.ok().build();

    }

    @DELETE
    @Path("/{podcastId}/categories/{categoryId}")
    public Response removeCategory(@PathParam("podcastId") Integer podcastId,
                                   @PathParam("categoryId") Integer categoryId) {
        podcastManager.removeCategory(podcastId, categoryId);

        return Response.ok().build();
    }
}
