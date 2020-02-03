package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.manage.CategoryManager;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Stateless
@Path("/categories")
@NoArgsConstructor
@RequiredArgsConstructor
public class CategoryEndpoint {

    @Inject
    @NonNull
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
