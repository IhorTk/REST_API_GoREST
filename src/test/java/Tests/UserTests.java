package Tests;


import PODJO.User;
import Utils.ApiWrapper;
import Utils.ConfigurationReader;
import Utils.TestDataHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.Argument;
import org.junit.jupiter.api.Test;

import java.util.List;

import static Utils.ApiWrapper.*;
import static io.restassured.RestAssured.form;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserTests extends BaseTestCase {
    @Test
    public void getListOfObjects() {
        sendGetRequest(
                getConfig("objectPathV2")
                        + getConfig("endPointUsers"))
                .assertThat()
                .body("$", hasSize(10)
                );
    }

    @Test
    public void getListParamObjectsUsers() {
        String page = "5";
        String perPage = "50";

        sendGetRequest(
                given().pathParams("page", page,
                        "perPage", perPage),
                (getConfig("objectPathV2")
                        + getConfig("endPointUsers") + "?page={page}&per_page={perPage}")
        )
                .assertThat()
                .body("$", hasSize(Integer.parseInt(perPage)));
    }


    @Test
    public void schemeUserValidationTest() {
        int userId = getId("endPointUsers", "id");
        sendGetRequest(
                given().pathParams("id", userId),
                getConfig("objectPathV2")
                        + getConfig("objectIdPath")
        )
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"));
    }

    @Test
    public void newUserCreation() {

        NewUser newUser = TestDataHelper.createUser();

        NewUser actualClient =
                ApiWrapper.sendPostRequest(
                        getConfig("objectPathV2")
                                + getConfig("endPointUsers"),
                        newUser,
                        NewUser.class
                );

        assertEquals(actualClient, newUser);
    }

    @Test
    public void deleteUser() {

        int userId = getId("endPointUsers", "id");

        deleteRequest(
                given().pathParams("id", userId),
                getConfig("objectPathV2")
                        + getConfig("objectIdPath")
        );
    }

    @Test
    public void patchNameUsers() {
        int userId = getId("endPointUsers", "id");

        String nameCheckedField = "name";
        String valueCheckedField = "BORISZ";

        sendPatchRequest(
                given().pathParams("id", userId),
                nameCheckedField,
                valueCheckedField,
                getConfig("objectPathV2")
                        + getConfig("objectIdPath")
        );
    }

    @Test
    public void putNameUser() {

        int userId = getId("endPointUsers", "id");

        NewUser newUser = TestDataHelper.createUser();
        newUser.setName("BORISZ");

        NewUser actualClient =
                ApiWrapper.sendPutRequest(
                        given().pathParams("id", userId),
                        getConfig("objectPathV2")
                                + getConfig("objectIdPath"),
                        newUser,
                        NewUser.class
                );
        assertEquals(actualClient, newUser);
    }

}
