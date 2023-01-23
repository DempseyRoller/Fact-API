package org.acme;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class FactResourceTest {

    // a working id
    String realId = "627192ec6703b186831874e6";
    // a valid but not working id
    String wrongId = "627192ec6703b186831874e7";

    @Test
    public void testGetRandomFactWebEndpoint() {
        // GET /facts/web
        given().when().get("/facts/web").then().statusCode(200).toString();

    }

    @Test
    @Order(1)
    public void testGetFactByIdWebEndpoint() {
        // GET /facts/web/{id}
        // invalid id
        given().when().get("/facts/web/e24g").then().body(is(StatusConstants.INVALID_OBJECTID));
        // valid but not working
        given().pathParam("id", wrongId).when().get("/facts/web/{id}").then()
                .body(is("{\"message\":\"Fact not found\"}"))
                .toString();
        // working id and saving
        given().pathParam("id", realId).queryParam("save", true).when().get("/facts/web/{id}").then().statusCode(201)
                .toString();
        // doing it when id's already in the database
        given().pathParam("id", realId).queryParam("save", true).when().get("/facts/web/{id}").then().statusCode(409)
                .body(is(StatusConstants.ALREADY_IN_DATABASE)).toString();

    }

    @Test
    @Order(2)
    public void testPutFactEndpoint() {
        // PUT /facts/{id}
        given().pathParam("id", realId).when().put("/facts/{id}").then().statusCode(200).toString();

    }

    @Test
    @Order(3)
    public void testDeleteFact() {
        // DELETE /facts/{id}
        given().pathParam("id", realId).when().delete("/facts/{id}").then().statusCode(200);
    }

}
