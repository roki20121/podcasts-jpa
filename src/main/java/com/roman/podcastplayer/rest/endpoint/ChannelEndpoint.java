package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.entity.Podcast;
import com.roman.podcastplayer.manage.ChannelManager;
import com.roman.podcastplayer.manage.PodcastManager;
import com.roman.podcastplayer.parser.UrlChannelParserConverter;
import com.roman.podcastplayer.rest.dto.ChannelDto;
import com.roman.podcastplayer.rest.dto.PodcastDto;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Path("/channels")
public class ChannelEndpoint {

    @Inject
    private ChannelManager manager;

    @Inject
    private PodcastManager podcastManager;
    private UrlChannelParserConverter converter = new UrlChannelParserConverter();

    @GET
    public Response getChannels() {
        List<Channel> allChannels = manager.getAllChannels();
        List<ChannelDto> channelDtos = allChannels.stream()
                .map(ChannelDto::new)
                .collect(Collectors.toList());
        return Response.ok(channelDtos).build();
    }

    @POST
    @Path("/subscribe")
    public Response subscribe(@QueryParam("url") String url, @Context UriInfo uriInfo) {
        try {
            Integer newChannelId = manager.subscribe(url, converter);

            UriBuilder pathBuilder = uriInfo.getBaseUriBuilder();
            pathBuilder.path(ChannelEndpoint.class);
            pathBuilder.path(newChannelId.toString());
            return Response.created(pathBuilder.build())
                    .build();
        } catch (IOException e) {
            return Response.serverError().build();
        }

    }

    @GET
    @Path("/{channelId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChannel(@PathParam("channelId") Integer channelId) {
        Channel channel = manager.findChannelById(channelId);

        if (channel == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ChannelDto channelDto = new ChannelDto(channel);
        return Response.ok(channelDto).build();
    }

    @DELETE
    @Path("/{channelId}")
    public Response deleteChannel(@PathParam("channelId") Integer channelId) {

        try {
            manager.unsubscribe(channelId);
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GET
    @Path("/{channelId}/updates")
    public Response getUpdatesForChannel(@PathParam("channelId") Integer channelId) {
        try {
            Channel channel = manager.getWithUpdates(channelId);
            ChannelDto channelDto = new ChannelDto(channel);
            return Response.ok(channelDto).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{channelId}/podcasts")
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

}
