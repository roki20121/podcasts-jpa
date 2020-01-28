package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.manage.ChannelManager;
import com.roman.podcastplayer.rest.dto.PodcastDto;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Path("/channels/{channelId}/podcasts")
public class PodcastEndpoint {

    @Inject
    private ChannelManager manager;

    @GET
    public Response getPodcasts(@PathParam("channelId") Integer channelId) {
        Channel channel = manager.findChannelById(channelId);

        List<PodcastDto> podcastDtos = channel.getPodcasts().stream()
                .map(PodcastDto::new)
                .collect(Collectors.toList());

        return Response.ok(podcastDtos).build();

    }


}
