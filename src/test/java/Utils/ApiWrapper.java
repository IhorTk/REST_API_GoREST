package Utils;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.example.AuthenticationFilter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiWrapper {
    private final static int DEFAULT_STATUS_CODE_GET = 200;
    private final static int DEFAULT_STATUS_CODE_PATCH = 200;
    private final static int DEFAULT_STATUS_CODE_POST = 201;
    private final static int DEFAULT_STATUS_CODE_PUT = 200;
    private final static int DEFAULT_STATUS_CODE_DELETE = 204;
    public final static String TOKEN = "056f978451fd22e35ef5745d1e5e5c660b5eac7eab756769808c4fa27765aa44";

    public static <T> T sendPostRequest(RequestSpecification requestSpecification,
                                        String endpoint,
                                        T requestBody,
                                        int statusCode,
                                        Class<T> responseType) {
        return given()
                .filter(new AuthenticationFilter(TOKEN))
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .assertThat()
                .statusCode(statusCode)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .extract().as(responseType);
    }

    public static <T> T sendPostRequest(String endpoint, T requestBody, Class<T> responseType) {

        return sendPostRequest(given(), endpoint, requestBody, DEFAULT_STATUS_CODE_POST, responseType);
    }

    public static <T> T sendPostRequest(String endpoint, T requestBody, int statusCode, Class<T> responseType) {

        return sendPostRequest(given(), endpoint, requestBody, statusCode, responseType);
    }

    public static <T> T sendPostRequest(RequestSpecification requestSpecification, String endpoint, T requestBody, Class<T> responseType) {

        return sendPostRequest(requestSpecification, endpoint, requestBody, DEFAULT_STATUS_CODE_POST, responseType);
    }


    public static <T> T sendPutRequest(RequestSpecification requestSpecification,
                                       String endpoint,
                                       T requestBody,
                                       Class<T> responseType) {
        return given()
                .filter(new AuthenticationFilter(TOKEN))
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(endpoint)
                .then()
                .assertThat()
                .statusCode(DEFAULT_STATUS_CODE_PUT)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .extract().as(responseType);
    }


    public static ValidatableResponse sendPatchRequest(RequestSpecification requestSpecification,
                                                       String nameCheckedField,
                                                       String valueCheckedField,
                                                       String callPath,
                                                       int statusCode) {
        return given()
                .filter(new AuthenticationFilter(TOKEN))
                .spec(requestSpecification)
                .body("{ \"" + nameCheckedField + "\": \"" + valueCheckedField + "\" }")
                .contentType(ContentType.JSON)
                .when()
                .patch(callPath)
                .then()
                .statusCode(statusCode)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .body(nameCheckedField, equalTo(valueCheckedField));
    }


    public static ValidatableResponse sendPatchRequest(RequestSpecification requestSpecification,
                                                       String nameCheckedField,
                                                       String valueCheckedField,
                                                       String callPath
    ) {
        return sendPatchRequest(requestSpecification,
                nameCheckedField,
                valueCheckedField,
                callPath,
                DEFAULT_STATUS_CODE_PATCH);
    }


    public static ValidatableResponse sendGetRequest(RequestSpecification requestSpecification,
                                                     String callPath,
                                                     int statusCode) {
        return given()
                .spec(requestSpecification)
                .when()
                .get(callPath)
                .then()
                .statusCode(statusCode)
                .contentType(ContentType.JSON)
                .log().ifValidationFails();
    }

    public static ValidatableResponse sendGetRequest(String callPath, int statusCode) {
        return sendGetRequest(given(), callPath, statusCode);
    }

    public static ValidatableResponse sendGetRequest(RequestSpecification requestSpecification, String callPath) {
        return sendGetRequest(requestSpecification, callPath, DEFAULT_STATUS_CODE_GET);
    }

    public static ValidatableResponse sendGetRequest(String callPath) {
        return sendGetRequest(given(), callPath, DEFAULT_STATUS_CODE_GET);
    }

    public static void deleteRequest(RequestSpecification requestSpecification,
                                     String callPath,
                                     int statusCode) {
        given()
                .filter(new AuthenticationFilter(TOKEN))
                .spec(requestSpecification)
                .when()
                .delete(callPath)
                .then()
                .log().ifValidationFails()
                .statusCode(statusCode);
    }

    public static void deleteRequest(RequestSpecification requestSpecification, String callPath) {
        deleteRequest(requestSpecification, callPath, DEFAULT_STATUS_CODE_DELETE);
    }
}
