package Tests;

import Utils.ConfigurationReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;

public class BaseTestCase {
    @BeforeEach

    public void setUp() {
        RestAssured.baseURI = ConfigurationReader.get("baseURI");
        RestAssured.basePath = ConfigurationReader.get("apiVersion");
        RestAssured.requestSpecification = given().accept(ContentType.ANY);
        RestAssured.filters(new AllureRestAssured());
    }


    @AfterEach
    public void down() {
        RestAssured.reset();
    }
}
