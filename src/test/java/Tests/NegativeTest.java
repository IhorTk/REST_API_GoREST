package Tests;

import PODJO.User;
import Utils.AuthenticationFilter;
import Utils.ConfigurationReader;
import Utils.TestDataHelper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static Utils.ApiWrapper.TOKEN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NegativeTest extends BaseTestCase {

    @ParameterizedTest
    @ValueSource(strings = {"  ", ""})
    public void incorrectUserName(String input) {
        int userId = getId("userPath", "id");
        String nameCheckedField = "name";

        try {
            given()
                    .pathParams("id", userId)
                    .filter(new AuthenticationFilter(TOKEN))
                    .body("{ \"" + nameCheckedField + "\": \"" + input + "\" }")
                    .contentType(ContentType.JSON)
                    .when()
                    .patch(ConfigurationReader.get("userIdPath"))
                    .then()
                    .statusCode(422)
                    .contentType(ContentType.JSON)
                    .log().ifValidationFails()
                    .body("[0].field", equalTo("name"))
                    .body("[0].message", equalTo("can't be blank"));

        } catch (Exception e) {
            assertEquals(e.getClass(), com.fasterxml.jackson.databind.exc.MismatchedInputException.class);
            assertEquals(e.getMessage(), "Cannot deserialize value of type `org.example.NewUser` from Array value (token `JsonToken.START_ARRAY`)");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", ""})
    public void incorrectUserName1(String input) {
        User newUser = TestDataHelper.createUser();
        newUser.setName(input);
        System.out.println(newUser);

    }



    @ParameterizedTest
    @ValueSource(strings = {"  ", "", "m"})
    public void incorrectUserGender(String input) {
        int userId = getId("userPath", "id");
        String nameCheckedField = "gender";

        given()
                .pathParams("id", userId)
                .filter(new AuthenticationFilter(TOKEN))
                .body("{ \"" + nameCheckedField + "\": \"" + input + "\" }")
                .contentType(ContentType.JSON)
                .when()
                .patch(ConfigurationReader.get("userIdPath"))
                .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .body("[0].field", equalTo("gender"))
                .body("[0].message", equalTo("can't be blank, can be male of female"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"john_doe.gmail.com", "john_doe@gmail.com ", " john_doe@gmail.com", "@gmail.com"})
    public void incorrectUserEmail(String input) {
        int userId = getId("userPath", "id");
        String nameCheckedField = "email";

        given()
                .pathParams("id", userId)
                .filter(new AuthenticationFilter(TOKEN))
                .body("{ \"" + nameCheckedField + "\": \"" + input + "\" }")
                .contentType(ContentType.JSON)
                .when()
                .patch(ConfigurationReader.get("userIdPath"))
                .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .body("[0].field", equalTo("email"))
                .body("[0].message", equalTo("is invalid"));
    }

    @Test

    public void incorrectReturnUserEmail() {
        int userId = getId("userPath", "id");

        String nameCheckedField = "email";
        String valueCheckedField = getVolume("userPath", "email");

        given()
                .pathParams("id", userId)
                .filter(new AuthenticationFilter(TOKEN))
                .body("{ \"" + nameCheckedField + "\": \"" + valueCheckedField + "\" }")
                .contentType(ContentType.JSON)
                .when()
                .patch(ConfigurationReader.get("userIdPath"))
                .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .body("[0].field", equalTo("email"))
                .body("[0].message", equalTo("has already been taken"));
    }


    @Test
    public void createUserLessToken() {
        User newUser = TestDataHelper.createUser();

        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post(ConfigurationReader.get("userPath"))
                .then()
                .assertThat()
                .statusCode(401)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .body("message", equalTo("Authentication failed"));
    }

    @Test
    public void createUserLessName() {
        User newUser = TestDataHelper.createUser();
        newUser.setName(null);
        given()
                .filter(new AuthenticationFilter(TOKEN))
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post(ConfigurationReader.get("userPath"))
                .then()
                .assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .body("[0].field", equalTo("name"))
                .body("[0].message ", equalTo("can't be blank"));
    }
}