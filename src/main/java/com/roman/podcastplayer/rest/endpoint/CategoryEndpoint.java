package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.entity.Category;
import com.roman.podcastplayer.manage.CategoryManager;
import com.roman.podcastplayer.rest.dto.CategoryDto;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories() {
        List<Category> categories = categoryManager.getCategories();
        List<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());

        return Response.ok(categoryDtos).build();
    }

}
