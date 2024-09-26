package Tests;

import PODJO.Post;
import Utils.ApiWrapper;
import Utils.ConfigurationReader;
import Utils.GetDataHelper;
import Utils.DataHelper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class NegativePostTest {

    @ParameterizedTest
    @ValueSource(strings = {"  ", ""})
    public void incorrectTitlePostTest(String input) {
        int postUserId = GetDataHelper.getId("userPath", "id");

        Post newPost = DataHelper.createPost(postUserId);
        newPost.setTitle(input);
        ApiWrapper.sendPostNegativeRequest(given()
                                .pathParams("id", postUserId), ConfigurationReader.get("userIdPath")
                                + ConfigurationReader.get("postPath"),
                        newPost)
                .body("[0].field", equalTo("title"))
                .body("[0].message", equalTo(ConfigurationReader.get("failTitle")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", ""})
    public void incorrectBodyPostTest(String input) {
        int userId = GetDataHelper.getId("userPath", "id");

        Post newPost = DataHelper.createPost(userId);
        newPost.setBody(input);
        ApiWrapper.sendPostNegativeRequest(given()
                                .pathParams("id", userId), ConfigurationReader.get("userIdPath")
                                + ConfigurationReader.get("postPath"),
                        newPost)
                .body("[0].field", equalTo("body"))
                .body("[0].message", equalTo(ConfigurationReader.get("failBody")));
    }

    @Test
    public void createPostLessToken() {
        int userId = GetDataHelper.getId("userPath", "id");

        Post newPost = DataHelper.createPost(userId);

        ApiWrapper.sendPostWithoutAuthRequest(given()
                        .pathParams("id", userId), ConfigurationReader.get("userIdPath")
                        + ConfigurationReader.get("postPath"),newPost)
                .body("message", equalTo(ConfigurationReader.get("failAuthentication")));
    }
}
