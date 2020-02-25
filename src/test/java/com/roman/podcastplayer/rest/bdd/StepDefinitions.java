package com.roman.podcastplayer.rest.bdd;

import com.roman.podcastplayer.TestUtils;
import com.roman.podcastplayer.entity.Category;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {

    private String restEndpointRoot = TestUtils.getProperty("app.server.restendpoint.root");
    private RequestSpecification requestSpecification;
    private Response response;
    private String endpoint;


    @Before
    public void init() {
        RestAssured.baseURI = restEndpointRoot;
        requestSpecification = RestAssured.given();
    }

    @Then("I should get {int} Status code")
    public void i_should_get_Status_code(Integer code) {
        assertEquals(code, response.getStatusCode(), response.getBody().prettyPrint());
    }


    @Given("I set queryParam {string} to {string}")
    public void i_set_queryParam_to(String param, String value) {
        requestSpecification = requestSpecification.queryParam(param, value);
    }

    @When("^I submit (.*) request to (.*) endpoint$")
    public void i_submit_request_to(String method, String dest) {
        switch (method) {
            case "POST":
                response = requestSpecification.post("/" + dest);
                break;
            case "GET":
                response = requestSpecification.get("/" + dest);
                break;
            case "DELETE":
                response = requestSpecification.delete("/" + dest);
                break;
            default:
                throw new IllegalArgumentException(method + " is not supported");
        }
    }

    @Then("I should get notnull uri in {string} header")
    public void i_should_get_notnull_uri_in_header(String header) {
        assertNotNull(response.getHeader(header));
    }

    @Then("I should get valid JSON in response body of categories")
    public void i_should_get_valid_JSON_in_response_body_of_categories() {
        JsonPath jsonPath = response.getBody().jsonPath();

        List<Category> list = jsonPath.getList("$", Category.class);
        for (Category category : list) {
            assertNotNull(category.getId());
            assertNotNull(category.getName());
        }
    }

    @When("Category with name {string} exists")
    public void category_with_name_exists(String name) {
        Response response = RestAssured.given().queryParam("categoryName", name).post("/categories");
        assertEquals(201, response.getStatusCode());
    }

    @Then("I should not see category {string} in the list")
    public void i_should_not_see_category_in_the_list(String name) {
        List<Category> list = RestAssured.get("/categories").getBody().jsonPath().getList("$", Category.class);
        Optional<Category> optional = list.stream()
                .filter(category -> category.getName().equals(name))
                .findAny();

        assertFalse(optional.isPresent());
    }

    @Given("Category with id {int} exists")
    public void category_with_id_exists(int id) {
        Response response = RestAssured.given().queryParam("categoryName", "_name_").post("/categories");
        assertEquals(201, response.getStatusCode());
    }

}
