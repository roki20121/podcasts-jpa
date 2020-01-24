package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.EntityManagerFactoryProducer;
import com.roman.podcastplayer.manage.ChannelManager;
import com.roman.podcastplayer.parser.UrlChannelParserConverter;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

@Path("/channel")
public class ChannelEndpoint {

    private ChannelManager manager;
    private UrlChannelParserConverter converter;

    @PostConstruct
    public void init() {
        EntityManagerFactory entityManagerFactory = EntityManagerFactoryProducer.getFactory();
        manager = new ChannelManager(entityManagerFactory);
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

}
