package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.entity.Podcast;
import com.roman.podcastplayer.manage.ChannelManager;
import com.roman.podcastplayer.manage.PodcastManager;
import com.roman.podcastplayer.rest.dto.PodcastDto;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Path("/channels/{channelId}/podcasts")
public class PodcastEndpoint {

    @Inject
    private ChannelManager channelManager;

    @Inject
    private PodcastManager podcastManager;

    @GET
    public Response getPodcasts(@PathParam("channelId") Integer channelId, @QueryParam("s") Boolean starred) {

        List<Podcast> podcasts;
        if (starred != null) {
            podcasts = podcastManager.getStarredPodcasts(channelId, starred);
        } else {
            podcasts = podcastManager.getPodcasts(channelId);

        }

        List<PodcastDto> podcastDtos = podcasts.stream()
                .map(PodcastDto::new)
                .collect(Collectors.toList());

        return Response.ok(podcastDtos).build();
    }

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
