package Tests;

import Utils.ConfigurationReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;

public class BaseTestCase {
    @BeforeEach

    public void setUp() {
        RestAssured.baseURI = ConfigurationReader.get("baseURI");
//        RestAssured.basePath = ConfigurationReader.get("apiVersion");
        RestAssured.requestSpecification = given().accept(ContentType.ANY);
        RestAssured.filters(new AllureRestAssured());
    }

    public static int getId(String endpoint, String nameId) {

        String path = "[0]." + nameId;
        return
                given()
                        .when()
                        .get(ConfigurationReader.get("apiVersion") +
                                ConfigurationReader.get(endpoint))
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
                .get(ConfigurationReader.get("apiVersion") +
                        ConfigurationReader.get(endpoint))
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
                        .get(ConfigurationReader.get("apiVersion") +
                                ConfigurationReader.get(endpoint))
                        .then()
                        .statusCode(200)
                        .log().ifValidationFails()
                        .extract()
                        .jsonPath()
                        .get(path);
    }


    @AfterEach
    public void down() {
        RestAssured.reset();
    }
}
