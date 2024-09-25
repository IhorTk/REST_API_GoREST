package Utils;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiWrapper {
    private final static int DEFAULT_STATUS_CODE_GET = 200;
    private final static int DEFAULT_STATUS_CODE_PATCH = 200;
    private final static int DEFAULT_STATUS_CODE_POST = 201;
    private final static int DEFAULT_STATUS_CODE_PUT = 200;
    private final static int DEFAULT_STATUS_CODE_DELETE = 204;
    private final static int DEFAULT_STATUS_CODE_INCORRECT_DATA_USER = 422;
    private final static int DEFAULT_STATUS_CODE_AUTHENTICATION_FAILED = 401;

    public final static String TOKEN = "3219774c6a08359bb949e19dc6b32eae3dcd8c1be5245554af65b3b6eab6aa43";

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
                                                       String nameField,
                                                       String valueField,
                                                       String callPath,
                                                       int statusCode) {
        return given()
                .filter(new AuthenticationFilter(TOKEN))
                .spec(requestSpecification)
                .body("{ \"" + nameField + "\": \"" + valueField + "\" }")
                .contentType(ContentType.JSON)
                .when()
                .patch(callPath)
                .then()
                .statusCode(statusCode)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .body(nameField, equalTo(valueField));
    }


    public static ValidatableResponse sendPatchRequest(RequestSpecification requestSpecification,
                                                       String nameField,
                                                       String valueField,
                                                       String callPath
    ) {
        return sendPatchRequest(requestSpecification,
                nameField,
                valueField,
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

    public static ValidatableResponse sendPostNegativeRequest(RequestSpecification requestSpecification,
                                                              String endpoint,
                                                              Object requestBody,
                                                              int statusCode) {
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
                .log().ifValidationFails();
    }

    public static ValidatableResponse sendPostNegativeRequest(String endpoint, Object requestBody) {
        return sendPostNegativeRequest(given(), endpoint, requestBody,  DEFAULT_STATUS_CODE_INCORRECT_DATA_USER);

    }

    public static ValidatableResponse sendPostWithoutAuthRequest(RequestSpecification requestSpecification,
                                                              String endpoint,
                                                              Object requestBody,
                                                              int statusCode) {
        return given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .assertThat()
                .statusCode(statusCode)
                .contentType(ContentType.JSON)
                .log().ifValidationFails();
    }

    public static ValidatableResponse sendPostWithoutAuthRequest(String endpoint, Object requestBody) {
        return sendPostWithoutAuthRequest(given(), endpoint, requestBody,  DEFAULT_STATUS_CODE_AUTHENTICATION_FAILED);
    }


}
