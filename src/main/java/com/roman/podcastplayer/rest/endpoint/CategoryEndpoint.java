package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.manage.CategoryManager;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Stateless
@Path("/categories")
public class CategoryEndpoint {

    @Inject
    private CategoryManager categoryManager;

    @POST
    public Response createCategory(@QueryParam("categoryName") String categoryName, @Context UriInfo uriInfo) {
        Integer newCategoryId = categoryManager.createCategory(categoryName);

        UriBuilder pathBuilder = uriInfo.getBaseUriBuilder();
        pathBuilder.path(CategoryEndpoint.class);
        pathBuilder.path(newCategoryId.toString());
        return Response.created(pathBuilder.build())
                .build();
    }

    @DELETE
    @Path("/{categoryId}")
    public Response removeCategory(@PathParam("categoryId") Integer categoryId) {
        categoryManager.deleteCategory(categoryId);
        return Response.ok().build();
    }


}
