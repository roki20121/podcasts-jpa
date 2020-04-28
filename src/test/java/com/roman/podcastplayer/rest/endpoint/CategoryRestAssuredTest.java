package com.roman.podcastplayer.rest.endpoint;

import com.roman.podcastplayer.TestUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;

public class CategoryRestAssuredTest {

    private String restEndpointRoot = TestUtils.getProperty("app.server.restendpoint.root");

    @Test
    void createCategory_anyCategoryName_newCategoryCreatedAndUriReturned() {
        String categoryName = "newTestCategory";

        RestAssured.given().
                when().
                queryParam("categoryName", categoryName).
                post(restEndpointRoot + "/categories").
                then().
                assertThat().
                statusCode(201).
                and().
                header("Location", StringContains.containsString("/categories/"));
    }

    @Test
    void getCategories_notEmptyListWithCorrectContentType() {
        RestAssured.given().
                when().
                get(restEndpointRoot + "/categories").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON).
                and().
                header("Content-Length", Integer::valueOf, Matchers.greaterThan(0));
    }

    @Test
    void removeCategory_anyId_returnsOK() {
        String id = "42";
        RestAssured.given().
                when().pathParam("id", id).
                delete(restEndpointRoot + "/categories/{id}").
                then().
                assertThat().
                statusCode(200);
    }

}
