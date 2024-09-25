package Utils;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetDataHelper {

    public static int getId(String endpoint, String nameId) {

        String path = "[0]." + nameId;
        return
                given()
                        .when()
                        .get(ConfigurationReader.get(endpoint))
                        .then()
                        .statusCode(200)
                        .log().ifValidationFails()
                        .extract()
                        .jsonPath()
                        .getInt(path);
    }

    public static Response getListId(String endpoint) {

        return given()
                .when()
                .get(ConfigurationReader.get(endpoint))
                .then()
                .statusCode(200)
                .log().ifValidationFails()
                .extract()
                .response();
    }

    public static String getVolume(String endpoint, String name) {
        String path = "[1]." + name;
        return
                given()
                        .when()
                        .get(ConfigurationReader.get(endpoint))
                        .then()
                        .statusCode(200)
                        .log().ifValidationFails()
                        .extract()
                        .jsonPath()
                        .get(path);
    }
}
