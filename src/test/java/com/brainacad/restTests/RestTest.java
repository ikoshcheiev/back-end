package com.brainacad.restTests;


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

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RestTest {

    public static RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri("https://reqres.in/")
            .setContentType(ContentType.JSON)
            .setBasePath("/api/{action}/{id}")
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

    @BeforeTest(description = "Attaching Body to allure reports")
    public void setFilter(){
        RestAssured.filters(new AllureRestAssured());
    }

    @Severity(SeverityLevel.MINOR)
    @Description("Test description : check that status code is 200")
    @Story("Get requests")
    @Test(priority = 2, description = "Get request list users")
    public void checkGetResponseStatusCode() {
        //using ValidatableResponse for example
        ValidatableResponse resp = given()
                .spec(spec)
                .pathParam("action", "users")
                .pathParam("id", "")
                .param("page", 2)
                .when()
                .get()
                .then();
        //Fail for testing. Expect to receive 200
        resp.statusCode(201);
    }

    @Severity(SeverityLevel.TRIVIAL)
    @Description("Test description : check that is not Null")
    @Story("Get requests")
    @Test(priority = 3, description = "Get request list users")
    public void checkGetResponseBodyNotNull() {

        given()
                .spec(spec)
                .pathParam("action", "users")
                .pathParam("id", "")
                .param("page", 2)
                .when()
                .get()
                .then()
                .body("$", notNullValue());
    }

    @Severity(SeverityLevel.TRIVIAL)
    @Description("Test description : check that is not Null")
    @Story("Post requests")
    @Test(priority = 3, description = "Post request create user")
    public void checkPostResponseBodyNotNull() {
        given()
                .spec(spec)
                .pathParam("action", "users")
                .pathParam("id", "")
                .when()
                .body(userLeaderJob)
                .post()

                .then()
                .statusCode(201)
                .body("$", notNullValue())
                .body("$", hasKey("id"))
                .body("$", hasKey("createdAt"));
    }


    @Severity(SeverityLevel.BLOCKER)
    @Description("Test description : check that status code and response params are ok")
    @Story("Post requests")
    @Test(priority = 0, description = "Post request create user")
    public void checkPostResponseParams() {
        given()
                .spec(spec)
                .pathParam("action", "users")
                //Failed for testing. To make working need to uncomment next string with 'id'
                /*.pathParam("id", "")*/
                .body(userLeaderJob)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("$", hasEntry("name", "morpheus"))
                .body("$", hasValue("leader"));
    }


    @Severity(SeverityLevel.BLOCKER)
    @Description("Test description : check that status code and response params are ok")
    @Story("Put requests")
    @Test(priority = 0, description = "Put request update")
    public void checkPutResponse() {
        given()
                .spec(spec)
                .pathParam("action", "users")
                .pathParam("id", 2)
                .body(userZionJob)
                .when()
                .put()
                .then()
                .statusCode(200)
                .body("$", hasEntry("job", "zion resident"))
                .body("$", hasKey("updatedAt"));
    }


    @Severity(SeverityLevel.BLOCKER)
    @Description("Test description : check that status code and response params are ok")
    @Story("Get requests")
    @Test(priority = 0, description = "Get request single user")
    public void checkGetResponseSingleUser() {
        given()
                .spec(spec)
                .pathParam("action", "users")
                .pathParam("id", 2)
                .when()
                .get()
                .then()
                .spec(responseSpec)
                .body("data", hasEntry("id", 2));
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Test description : check that status code is 404")
    @Story("Get requests")
    @Test(priority = 2, description = "Get request single user not found")
    public void checkGetResponseSingleUserNotFound(){
        given()
                .spec(spec)
                .pathParam("action", "users")
                .pathParam("id", 32)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("Test description : check that status code and response params are ok")
    @Story("Get requests")
    @Test(priority = 0, description = "Get request list <Resource>")
    public void checkGetResponseList(){
        ValidatableResponse resp = given()
                .spec(spec)
                .pathParam("action", "unknown")
                .pathParam("id", "")
                .when()
                .get()
                .then();
        resp.spec(responseSpec);
        resp.body("data.size()", equalTo(resp.extract().body().path("per_page")));
        resp.body(matchesJsonSchemaInClasspath("unknownUsersResponseSchema.json"));
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("Test description : check that status code, id and response params are ok")
    @Story("Get requests")
    @Test(priority = 0, description = "Get request single <Resource>")
    public void checkGetResponseSingleResource(){
        given()
                .spec(spec)
                .pathParam("action", "unknown")
                .pathParam("id", 2)
                .when()
                .get()
                .then()
                .spec(responseSpec)
                .body("data", hasEntry("id", 2));
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Test description : check that status code is 404")
    @Story("Get requests")
    @Test(priority = 2, description = "Get request single <Resource> not found")
    public void checkGetResponseSingleResourceNotFound(){
        given()
                .spec(spec)
                .pathParam("action", "unknown")
                .pathParam("id", 23)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Test description : check that status code and response params are ok")
    @Story("Patch requests")
    @Test(priority = 1, description = "Patch request update user")
    public void checkPatchResponse() {
        given()
                .spec(spec)
                .pathParam("action", "users")
                .pathParam("id", 2)
                .body(userZionJob)
                .when()
                .patch()
                .then()
                .statusCode(200)
                .body("", hasKey("updatedAt"))
                .body("", hasEntry("job", "zion resident"));
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("Test description : check that status code is 204")
    @Story("Delete requests")
    @Test(priority = 0, description = "Delete request delete user")
    public void checkDeleteResponse() {
        given()
                .spec(spec)
                .pathParam("action", "users")
                .pathParam("id", 2)
                .when()
                .delete()
                .then()
                .statusCode(204);
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("Test description : check that status code and response params are ok")
    @Story("Post requests")
    @Test(priority = 0, description = "Post request register - successful")
    public void checkPostRegisterSuccessful(){
        given()
                .spec(spec)
                .pathParam("action", "register")
                .pathParam("id", "")
                .when()
                .body("{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}")
                .post()
                .then()
                .statusCode(200)
                .body("$", hasKey("token"))
                .body("", hasEntry("id", 4));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Test description : check that status code is 400")
    @Story("Post requests")
    @Test(priority = 1, description = "Post request register - unsuccessful")
    public void checkPostRegisterUnsuccessful() {
        given()
                .spec(spec)
                .pathParam("action", "register")
                .pathParam("id", "")
                .when()
                .body("{\"email\": \"sydney@fife\"}")
                .post()
                .then()
                .statusCode(400)
                .body("", hasEntry("error", "Missing password"));
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("Test description : check that status code and token are ok")
    @Story("Post requests")
    @Test(priority = 0, description = "Post request login - successful")
    public void checkPostLoginSuccessful(){
        given()
                .spec(spec)
                .pathParam("action", "login")
                .pathParam("id", "")
                .when()
                .body("{\"email\": \"eve.holt@reqres.in\",\"password\": \"cityslicka\"}")
                .post()
                .then()
                .statusCode(200)
                .body("", hasKey("token"));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Test description : check that status code is 400")
    @Story("Post requests")
    @Test(priority = 1, description = "Post request login - unsuccessful")
    public void checkPostLoginUnsuccessful(){
        given()
                .spec(spec)
                .pathParam("action", "login")
                .pathParam("id", "")
                .when()
                .body("{\"email\": \"peter@klaven\"}")
                .post()
                .then()
                .statusCode(400)
                .body("", hasEntry("error", "Missing password"));
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Test description : check that status code and respose params are ok")
    @Story("Get requests")
    @Test(priority = 2, description = "Get request delayed response")
    public void checkGetDelayedResponse(){
        given()
                .spec(spec)
                .pathParam("action", "users")
                .pathParam("id","")
                .param("delay=3")
                .when()
                .get()
                .then()
                .spec(responseSpec)
                .body(matchesJsonSchemaInClasspath("userResponseSchema.json"));
    }
}