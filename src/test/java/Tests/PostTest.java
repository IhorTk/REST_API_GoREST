package Tests;

import PODJO.Post;
import Utils.ApiWrapper;
import Utils.ConfigurationReader;
import Utils.TestDataHelper;
import Utils.PODJODataHelper;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static Utils.ApiWrapper.*;
import static Utils.TestDataHelper.getListId;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostTest extends BaseTestCase {

    @Test
    public void schemePostValidationTest() {
        int postUserId = TestDataHelper.getId("postPath", "user_id");

        sendGetRequest(
                given().pathParams("id", postUserId),
                ConfigurationReader.get("userIdPath")
                        + ConfigurationReader.get("postPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemaPost.json"));
    }

    @Test
    public void createPostTest() {
        int postUserId = TestDataHelper.getId("postPath", "user_id");

        Post newPost = PODJODataHelper.createPost(postUserId);
        Post responsePost =
                sendPostRequest(
                        given().pathParams("id", postUserId),
                        ConfigurationReader.get("userIdPath")
                                + ConfigurationReader.get("postPath"),
                        newPost,
                        Post.class);

        assertEquals(responsePost, newPost);
    }
    
    
    @Test
    public void getListPostsByParameterTest() {
        String numPage = ConfigurationReader.get("postNumPage");
        String countPosts = ConfigurationReader.get("postCount");

        sendGetRequest(
                given().pathParams("numPage", numPage,
                        "countPosts", countPosts),
                ConfigurationReader.get("postPath")
                        + "?page={numPage}&per_page={countPosts}")
                .assertThat()
                .body("$", hasSize(Integer.parseInt(countPosts)));
    }
    

    @Test
    public void changeTitlePost() {

        int postId = TestDataHelper.getId("postPath", "id");
        String nameField = ConfigurationReader.get("postFieldName");
        String valueField = ConfigurationReader.get("postFieldValue");

        sendPatchRequest(
                given().pathParams("id", postId),
                nameField,
                valueField,
                ConfigurationReader.get("postIdPath"));
    }

    @Test

    public void changePost() {

        Response response = getListId("postPath");
        int userId = response.jsonPath().getInt("[0]."+"user_id");
        int id = response.jsonPath().getInt("[0]."+"id");
        
        Post newPost = PODJODataHelper.createPost(id);
        newPost.setTitle(ConfigurationReader.get("postFieldValue"));
        newPost.setUserId(userId);

        Post responsePost =
                ApiWrapper.sendPutRequest(
                        given().pathParams("id", id),
                        ConfigurationReader.get("postIdPath"),
                        newPost,
                        Post.class);
        assertEquals(responsePost, newPost);
    }

    @Test
    public void deletePostTest() {

        int postId = TestDataHelper.getId("postPath", "id");

        deleteRequest(
                given().pathParams("id", postId),
                ConfigurationReader.get("postIdPath"));
    }
    
}