package Tests;

import PODJO.Comments;
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

public class CommentsTest extends BaseTestCase {

    @Test
    public void schemeCommentsValidationTest() {
        sendGetRequest(ConfigurationReader.get("commentsPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemaComment.json"));
    }

    @Test
    public void getListParamObjectsComments() {
        String page = "5";
        String perPage = "50";

        sendGetRequest(
                given().pathParams("page",
                        page, "perPage", perPage),
                 ConfigurationReader.get("commentsPath")
                        + "?page={page}&per_page={perPage}")
                .assertThat()
                .body("$", hasSize(Integer.parseInt(perPage)));
    }

    @Test
    public void newCommentPostsCreation() {
        int postId = getId("commentsPath", "post_id");

        Comments newPCommentsPost = TestDataHelper.createComments(postId);

        Comments actualComments =
                ApiWrapper.sendPostRequest(
                        given().pathParams("id", postId),
                        ConfigurationReader.get("postIdPath")
                                + ConfigurationReader.get("commentsPath"),
                        newPCommentsPost,
                        Comments.class);
        assertEquals(actualComments, newPCommentsPost);
    }

    @Test
    public void deleteComment() {

        int postId = getId("commentsPath", "id");

        deleteRequest(given().pathParams("id", postId),
                ConfigurationReader.get("commentsIdPath"));
    }

    @Test
    public void patchNameComment() {
        int postId = getId("commentsPath", "id");

        String nameCheckedField = "name";
        String valueCheckedField = "JOHN_DOE";

        sendPatchRequest(
                given().pathParams("id", postId),
                nameCheckedField,
                valueCheckedField,
                ConfigurationReader.get("commentsIdPath"));
    }

    @Test
      public void putNameComment() {

        Response response = getListId("commentsPath");
        int id = response.jsonPath().getInt("[0]."+"id");
        int postId = response.jsonPath().getInt("[0]."+"post_id");

        Comments comments = TestDataHelper.createComments(postId);

        comments.setName("John_Doe");


        Comments actualComments =
                ApiWrapper.sendPutRequest(
                        given().pathParams("id", id),
                        ConfigurationReader.get("commentsIdPath"),
                        comments,
                        Comments.class);

        assertEquals(actualComments, comments);
    }
}
