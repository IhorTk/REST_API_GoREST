package Tests;

import PODJO.User;
import Utils.ApiWrapper;
import Utils.ConfigurationReader;
import Utils.PODJODataHelper;

import org.junit.jupiter.api.Test;

import static Utils.ApiWrapper.*;
import static Utils.TestDataHelper.getId;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserTests extends BaseTestCase {

    @Test
    public void schemeUserValidationTest() {
        int userId = getId("userPath", "id");
        sendGetRequest(
                given().pathParams("id", userId),
                ConfigurationReader.get("userIdPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemaUser.json"));
    }


    @Test
    public void createNewUserTest() {

        User newUser = PODJODataHelper.createUser();

        User responseUser =
                ApiWrapper.sendPostRequest(
                        ConfigurationReader.get("userPath"),
                        newUser,
                        User.class);

        assertEquals(responseUser, newUser);
    }

    @Test
    public void renameUserTest() {
        int userId = getId("userPath", "id");

        String nameField = ConfigurationReader.get("userFieldName");
        String valueField = ConfigurationReader.get("userFieldValue");

        sendPatchRequest(
                given().pathParams("id", userId),
                nameField,
                valueField,
                ConfigurationReader.get("userIdPath"));
    }


    @Test
    public void updateUserDetailsTest() {

        int userId = getId("userPath", "id");

        User newUser = PODJODataHelper.createUser();
        newUser.setName(ConfigurationReader.get("userName"));

        User actualClient =
                ApiWrapper.sendPutRequest(
                        given().pathParams("id", userId),
                        ConfigurationReader.get("userIdPath"),
                        newUser,
                        User.class);
        assertEquals(actualClient, newUser);
    }
    
    
    @Test
    public void getListUsersTest() {
        sendGetRequest(
                ConfigurationReader.get("userPath"))
                .assertThat()
                .body("$", hasSize(10));
    }

    @Test
    public void getListUsersByParametersTest() {
        String numPage = ConfigurationReader.get("userNumPage");
        String countUsers = ConfigurationReader.get("userCount");

        sendGetRequest(
                given().pathParams("numPage", numPage,
                        "countUsers", countUsers),
                (ConfigurationReader.get("userPath") + "?page={numPage}&per_page={countUsers}"))
                .assertThat()
                .body("$", hasSize(Integer.parseInt(countUsers)));
    }


    @Test
    public void deleteUserTest() {

        int userId = getId("userPath", "id");

        deleteRequest(
                given().pathParams("id", userId),
                ConfigurationReader.get("userIdPath"));
    }

}
