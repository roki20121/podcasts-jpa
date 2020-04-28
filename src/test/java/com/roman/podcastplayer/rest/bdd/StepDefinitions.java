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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StepDefinitions {

    private String restEndpointRoot = TestUtils.getProperty("app.server.restendpoint.root");
    private RequestSpecification requestSpecification;
    private Response response;
    private String endpoint;
    private int maxId;
    private Integer idToInteract;

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
        String path = "/" + dest + (idToInteract == null ? "" : "/" + idToInteract);
        switch (method) {
            case "POST":
                response = requestSpecification.post(path);
                break;
            case "GET":
                response = requestSpecification.get(path);
                break;
            case "DELETE":
                response = requestSpecification.delete(path);
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

    @Then("^I should not see any (.*) in the list$")
    public void i_should_not_see_any_in_the_list(String itemsName) {
        List<Object> list = RestAssured.get("/" + itemsName).getBody().jsonPath().getList("$");

        assertTrue(list.isEmpty());
    }

    @Given("Category with id {int} exists")
    public void category_with_id_exists(int id) {
        Response response = RestAssured.given().queryParam("categoryName", "_name_").post("/categories");
        assertEquals(201, response.getStatusCode());
    }

    @Given("^I remove all (.*) and save max_id")
    public void i_remove_all_and_save_max_id(String itemsName) {
        Response response = RestAssured.get(restEndpointRoot + "/" + itemsName);
        JsonPath jsonPath = response.getBody().jsonPath();

        List<Integer> list = jsonPath.getList("id", Integer.class);

        maxId = Collections.max(list);

        for (Integer id : list) {
            int statusCode = RestAssured.delete(restEndpointRoot + "/" + itemsName + "/" + id).getStatusCode();
            assertEquals(200, statusCode);
        }
    }

    @When("I want to interact with item with id max_id+1")
    public void i_want_to_interact_with_item_with_id_max_id_1() {
        idToInteract = maxId + 1;
    }

}
