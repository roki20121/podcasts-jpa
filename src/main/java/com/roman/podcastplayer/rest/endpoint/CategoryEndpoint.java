package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.manage.CategoryManager;
import com.roman.podcastplayer.manage.PodcastManager;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Stateless
@Path("/categories")
public class CategoryEndpoint {

    @Inject
    private CategoryManager categoryManager;

    @Inject
    private PodcastManager podcastManager;

    @POST
    public Response createCategory(@QueryParam("categoryName") String categoryName, @Context UriInfo uriInfo) {
        Integer newCategoryId = categoryManager.createCategory(categoryName);

        UriBuilder pathBuilder = uriInfo.getBaseUriBuilder();
        pathBuilder.path(CategoryEndpoint.class);
        pathBuilder.path(newCategoryId.toString());
        return Response.created(pathBuilder.build())
                .build();
    }


}
