package Tests;

import PODJO.Post;
import Utils.ApiWrapper;
import Utils.ConfigurationReader;
import Utils.TestDataHelper;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static Utils.ApiWrapper.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostTest extends BaseTestCase {

    @Test
    public void schemePostValidationTest() {
        int postUserId = getId("postPath", "user_id");

        sendGetRequest(
                given().pathParams("id", postUserId),
                ConfigurationReader.get("userIdPath")
                        + ConfigurationReader.get("postPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemaPost.json"));
    }

    @Test
    public void getListParamObjectsPosts() {
        String page = "4";
        String perPage = "80";

        sendGetRequest(
                given().pathParams("page", page,
                        "perPage", perPage),
                ConfigurationReader.get("postPath")
                        + "?page={page}&per_page={perPage}")
                .assertThat()
                .body("$", hasSize(Integer.parseInt(perPage)));
    }


    @Test
    public void newPostCreation() {
        int postUserId = getId("postPath", "user_id");

        Post newPost = TestDataHelper.createPost(postUserId);
        Post actualPost =
                ApiWrapper.sendPostRequest(
                        given().pathParams("id", postUserId),
                        ConfigurationReader.get("userIdPath")
                                + ConfigurationReader.get("postPath"),
                        newPost,
                        Post.class);

        assertEquals(actualPost, newPost);
    }

    @Test
    public void deletePost() {

        int postId = getId("postPath", "id");

        deleteRequest(
                given().pathParams("id", postId),
                ConfigurationReader.get("postIdPath"));
    }

    @Test
    public void patchTitlePost() {

        int postId = getId("postPath", "id");
        String nameCheckedField = "title";
        String valueCheckedField = "John_Doe";

        sendPatchRequest(
                given().pathParams("id", postId),
                nameCheckedField,
                valueCheckedField,
                ConfigurationReader.get("postIdPath"));
    }

    @Test

    public void putTitlePost() {

        Response response = getListId("postPath");
        int userId = response.jsonPath().getInt("[0]."+"user_id");
        int id = response.jsonPath().getInt("[0]."+"id");


        Post newPost = TestDataHelper.createPost(id);
        newPost.setTitle("John_Doe");
        newPost.setUserId(userId);

        Post actualPost =
                ApiWrapper.sendPutRequest(
                        given().pathParams("id", id),
                        ConfigurationReader.get("postIdPath"),
                        newPost,
                        Post.class);
        assertEquals(actualPost, newPost);
    }
}