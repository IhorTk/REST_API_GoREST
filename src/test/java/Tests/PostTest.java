package Tests;

import Utils.ApiWrapper;
import Utils.TestDataHelper;
import io.restassured.response.Response;
import org.example.Post;
import org.junit.jupiter.api.Test;

import static Utils.ApiWrapper.deleteRequest;
import static Utils.ApiWrapper.sendPatchRequest;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;

public class PostTest extends BaseHomeWorkTest {

    @Test
    public void schemePostValidationTest() {
        int postUserId = getId("objectPostPath", "user_id");

        sendGetRequest(
                given().pathParams("id", postUserId),
                getConfig("objectPathV2")
                        + getConfig("objectIdPath")
                        + getConfig("objectPostPath")
        )
                .assertThat()
                .body(matchesJsonSchemaInClasspath("post-schema.json"));
    }

    @Test
    public void getListParamObjectsPosts() {
        String page = "4";
        String perPage = "80";

        sendGetRequest(
                given().pathParams("page", page,
                        "perPage", perPage),
                getConfig("objectPathV2")
                        + getConfig("objectPostPath")
                        + "?page={page}&per_page={perPage}"
        )
                .assertThat()
                .body("$", hasSize(Integer.parseInt(perPage)));
    }


    @Test
    public void newPostCreation() {
        int postUserId = getId("objectPostPath", "user_id");

        Post newPost = TestDataHelper.createPost(postUserId);
        Post actualPost =
                ApiWrapper.sendPostRequest(
                        given().pathParams("id", postUserId),
                        getConfig("objectPathV2")
                                + getConfig("objectIdPath")
                                + getConfig("objectPostPath"),
                        newPost,
                        Post.class
                );

        assertEquals(actualPost, newPost);
    }

    @Test
    public void deletePost() {

        int postId = getId("objectPostPath", "id");

        deleteRequest(
                given().pathParams("id", postId),
                getConfig("objectPathV2")
                        + getConfig("objectPostIdPath")
        );
    }

    @Test
    public void patchTitlePost() {

        int postId = getId("objectPostPath", "id");
        String nameCheckedField = "title";
        String valueCheckedField = "BORISZ";

        sendPatchRequest(
                given().pathParams("id", postId),
                nameCheckedField,
                valueCheckedField,
                getConfig("objectPathV2")
                        + getConfig("objectPostIdPath")
        );
    }

    @Test

    public void putTitlePost() {

        Response response = getListId("objectPostPath");
        int userId = response.jsonPath().getInt("[0]."+"user_id");
        int id = response.jsonPath().getInt("[0]."+"id");


        Post newPost = TestDataHelper.createPost(id);
        newPost.setTitle("BORISZ");
        newPost.setUserId(userId);

        Post actualPost =
                ApiWrapper.sendPutRequest(
                        given().pathParams("id", id),
                        getConfig("objectPathV2")
                                + getConfig("objectPostIdPath"),
                        newPost,
                        Post.class
                );
        assertEquals(actualPost, newPost);
    }
}