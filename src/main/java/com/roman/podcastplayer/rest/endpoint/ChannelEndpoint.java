package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.entity.Channel;
import com.roman.podcastplayer.manage.ChannelManager;
import com.roman.podcastplayer.parser.UrlChannelParserConverter;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;

@Stateless
@Path("/channel")
public class ChannelEndpoint {

    @Inject
    private ChannelManager manager;
    private UrlChannelParserConverter converter;


    @PostConstruct
    public void init() {
        converter = new UrlChannelParserConverter();
    }

    @GET
    public Response channelMain() {
        return Response.ok().build();
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

    @PersistenceContext(unitName = "com.roman.podcasts")
    private EntityManagerFactory factory;

    @PersistenceContext(unitName = "com.roman.podcasts")
    private EntityManager em;

    @GET
    @Path("/{channelId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChannel(@PathParam("channelId") Integer channelId) {
        Channel channel = manager.findChannelById(channelId);

        if (channel == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(channel).build();
    }

}
