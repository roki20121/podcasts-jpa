package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.manage.CategoryManager;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@Tag("unit-test")
class CategoryEndpointTest {

    @Test
    void createCategory_anyCategoryName_newCategoryCreatedAndUriReturned() {
        String categoryName = "newTestCategory";
        Integer id = 42;
        CategoryManager categoryService = mock(CategoryManager.class);
        CategoryEndpoint endpoint = new CategoryEndpoint(categoryService);
        UriInfo uriInfo = mock(UriInfo.class);

        when(categoryService.createCategory(categoryName)).thenReturn(id);
        when(uriInfo.getBaseUriBuilder()).thenReturn(new JerseyUriBuilder());

        Response response = endpoint.createCategory(categoryName, uriInfo);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        List<Object> headers = response.getHeaders().get("Location");
        assertEquals(1, headers.size());
        assertTrue(headers.get(0).toString().endsWith("/categories/" + id));
    }

    @Test
    void removeCategory_anyCategoryId_removed() {
        Integer id = 42;
        CategoryManager categoryService = mock(CategoryManager.class);
        CategoryEndpoint endpoint = new CategoryEndpoint(categoryService);

        Response response = endpoint.removeCategory(id);

        verify(categoryService, times(1)).deleteCategory(id);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

}