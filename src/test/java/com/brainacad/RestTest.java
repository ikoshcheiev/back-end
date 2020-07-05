package com.brainacad;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class RestTest {

    public static RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri("https://reqres.in/")
            .setContentType(ContentType.JSON)
            .setBasePath("/api/users/{id}")
            .build();
    public static ResponseSpecification responseSpec =
            new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .expectResponseTime(lessThan(5000L))
                    .expectBody("$", hasKey("data"))
                    .expectBody("$", hasKey("ad"))
                    .build();

    public static User userLeaderJob = User.builder()
            .name("morpheus")
            .job("leader")
            .build();
    public static User userZionJob = User.builder()
            .name("morpheus")
            .job("zion resident")
            .build();

    @BeforeTest
    public void setFilter(){
        RestAssured.filters(new AllureRestAssured());
    }

    @Severity(SeverityLevel.MINOR)
    @Description("Test description : check that status code is 200")
    @Story("Get requests")
    @Test(priority = 0, description = "Get request status code")
    public void checkGetResponseStatusCode() {
        //using ValidatableResponse for example
        ValidatableResponse resp = given()
                .spec(spec)
                .pathParam("id", "")
                .param("page", 2)
                .log().body()
                .when()
                .get()

                .then()
                .log().body();
        resp.statusCode(200);
    }

    @Severity(SeverityLevel.TRIVIAL)
    @Description("Test description : check that is not Null")
    @Story("Get requests")
    @Test(priority = 0, description = "Get request not Null")
    public void checkGetResponseBodyNotNull() {

        given()
                .spec(spec)
                .pathParam("id", "")
                .param("page", 2)
                .log().body()
                .when()
                .get()

                .then()
                .body("$", notNullValue())
                .log().body();
    }

    @Severity(SeverityLevel.TRIVIAL)
    @Description("Test description : check that is not Null")
    @Story("Post requests")
    @Test(priority = 0, description = "Post request not Null")
    public void checkPostResponseBodyNotNull() {
        given()
                .spec(spec)
                .pathParam("id", "")
                .log().body()
                .when()
                .body(userLeaderJob)
                .post()

                .then()
                .statusCode(201)
                .body("$", notNullValue())
                .body("$", hasKey("id"))
                .body("$", hasKey("createdAt"))
                .log().body();
    }

    @Test
    public void checkPostResponseParams() {
        given()
                .spec(spec)
                .pathParam("id", "")
                .body(userLeaderJob)
                .log().body()
                .when()
                .post()

                .then()
                .statusCode(201)
                .log().body()
                .body("$", hasEntry("name", "morpheus"))
                .body("$", hasValue("leader"));
    }

    @Test
    public void checkPutResponse() {
        given()
                .spec(spec)
                .pathParam("id", 2)
                .log().body()
                .body(userZionJob)
                .when()
                .put()

                .then()
                .statusCode(200)
                .body("$", hasEntry("job", "zion resident"))
                .body("$", hasKey("updatedAt"))
                .log().body();
    }

    @Test
    public void checkGetResponseSingleUser() {
        given()
                .spec(spec)
                .pathParam("id", 2)
                .when()
                .get()

                .then()
                .log().body()
                .spec(responseSpec)
                .body("data", hasEntry("id", 2));
    }

    @Test
    public void checkGetResponseSingleUserNotFound(){
        given()
                .spec(spec)
                .pathParam("id", 32)
                .when()
                .get()

                .then()
                .log().body()
                .statusCode(404);
    }

    @Test
    public void checkGetResponseList(){
        ValidatableResponse resp = given()
                .spec(spec)
                .basePath("/api/unknown")
                .when()
                .get()

                .then()
                .log().body()
                .spec(responseSpec);
        resp.body("data.size()", equalTo(resp.extract().body().path("per_page")));
        resp.body(matchesJsonSchemaInClasspath("unknownUsersResponseSchema.json"));
    }

    @Test
    public void checkGetResponseSingleResource(){
        given()
                .spec(spec)
                .basePath("/api/unknown/{id}")
                .pathParam("id", 2)
                .when()
                .get()

                .then()
                .log().body()
                .spec(responseSpec)
                .body("data", hasEntry("id", 2));
    }

    @Test
    public void checkGetResponseSingleResourceNotFound(){
        given()
                .spec(spec)
                .basePath("/api/unknown/{id}")
                .pathParam("id", 23)
                .when()
                .get()

                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    public void checkPatchResponse() {
        given()
                .spec(spec)
                .pathParam("id", 2)
                .log().body()
                .body(userZionJob)
                .log().body()
                .when()
                .patch()

                .then()
                .log().body()
                .statusCode(200)
                .body("", hasKey("updatedAt"))
                .body("", hasEntry("job", "zion resident"));
    }

    @Test
    public void checkDeleteResponse() {
        given()
                .spec(spec)
                .pathParam("id", 2)
                .log().body()
                .when()
                .delete()

                .then()
                .log().body()
                .statusCode(204);
    }

    @Test
    public void checkPostRegisterSuccessful(){
        given()
                .spec(spec)
                .basePath("/api/register")
                .when()
                .body("{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}")
                .post()

                .then()
                .log().body()
                .statusCode(200)
                .body("$", hasKey("token"))
                .body("", hasEntry("id", 4));
    }

    @Test
    public void checkPostRegisterUnsuccessful() {
        given()
                .spec(spec)
                .basePath("/api/register")
                .when()
                .body("{\"email\": \"sydney@fife\"}")
                .post()

                .then()
                .log().body()
                .statusCode(400)
                .body("", hasEntry("error", "Missing password"));
    }

    @Test
    public void checkPostLoginSuccessful(){
        given()
                .spec(spec)
                .basePath("/api/login")
                .when()
                .body("{\"email\": \"eve.holt@reqres.in\",\"password\": \"cityslicka\"}")
                .post()

                .then()
                .log().body()
                .statusCode(200)
                .body("", hasKey("token"));
    }

    @Test
    public void checkPostLoginUnsuccessful(){
        given()
                .spec(spec)
                .basePath("/api/login")
                .when()
                .body("{\"email\": \"peter@klaven\"}")
                .post()

                .then()
                .log().body()
                .statusCode(400)
                .body("", hasEntry("error", "Missing password"));
    }

    @Test
    public void checkGetDelayedResponse(){
        given()
                .spec(spec)
                .pathParam("id","")
                .param("delay=3")
                .when()
                .get()

                .then()
                .log().body()
                .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("userResponseSchema.json"));
    }
}