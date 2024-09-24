package Tests;


import PODJO.User;
import Utils.ApiWrapper;
import Utils.ConfigurationReader;
import Utils.TestDataHelper;
import org.junit.jupiter.api.Test;

import static Utils.ApiWrapper.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserTests extends BaseTestCase {
    @Test
    public void getListOfObjects() {
        sendGetRequest(
                ConfigurationReader.get("endPointUsers"))
                .assertThat()
                .body("$", hasSize(10));
    }

    @Test
    public void getListParamObjectsUsers() {
        String page = "12";
        String perPage = "33";

        sendGetRequest(
                given().pathParams("page", page,
                        "perPage", perPage),
                (ConfigurationReader.get("endPointUsers") + "?page={page}&per_page={perPage}"))
                .assertThat()
                .body("$", hasSize(Integer.parseInt(perPage)));
    }


    @Test
    public void schemeUserValidationTest() {
        int userId = getId("endPointUsers", "id");
        sendGetRequest(
                given().pathParams("id", userId),
                ConfigurationReader.get("userIdPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemaUser.json"));
    }

    @Test
    public void newUserCreation() {

        User newUser = TestDataHelper.createUser();

        User actualClient =
                ApiWrapper.sendPostRequest(
                        ConfigurationReader.get("endPointUsers"),
                        newUser,
                        User.class);

        assertEquals(actualClient, newUser);
    }

    @Test
    public void deleteUser() {

        int userId = getId("endPointUsers", "id");

        deleteRequest(
                given().pathParams("id", userId),
                ConfigurationReader.get("userIdPath"));
    }

    @Test
    public void patchNameUsers() {
        int userId = getId("endPointUsers", "id");

        String nameCheckedField = "name";
        String valueCheckedField = "John_Doe";

        sendPatchRequest(
                given().pathParams("id", userId),
                nameCheckedField,
                valueCheckedField,
                ConfigurationReader.get("userIdPath"));
    }

    @Test
    public void putNameUser() {

        int userId = getId("endPointUsers", "id");

        User newUser = TestDataHelper.createUser();
        newUser.setName("John_Doe");

        User actualClient =
                ApiWrapper.sendPutRequest(
                        given().pathParams("id", userId),
                        ConfigurationReader.get("userIdPath"),
                        newUser,
                        User.class);
        assertEquals(actualClient, newUser);
    }

}
