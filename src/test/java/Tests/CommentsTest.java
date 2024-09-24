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
        sendGetRequest(ConfigurationReader.get("objectCommentsPostPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("comments-schema.json"));
    }

    @Test
    public void getListParamObjectsComments() {
        String page = "5";
        String perPage = "50";

        sendGetRequest(
                given().pathParams("page",
                        page, "perPage", perPage),
                 ConfigurationReader.get("objectCommentsPostPath")
                        + "?page={page}&per_page={perPage}")
                .assertThat()
                .body("$", hasSize(Integer.parseInt(perPage)));
    }

    @Test
    public void newCommentPostsCreation() {
        int postId = getId("objectCommentsPostPath", "post_id");

        Comments newPCommentsPost = TestDataHelper.createComments(postId);

        Comments actualComments =
                ApiWrapper.sendPostRequest(
                        given().pathParams("id", postId),
                        ConfigurationReader.get("objectPostIdPath")
                                + ConfigurationReader.get("objectCommentsPostPath"),
                        newPCommentsPost,
                        Comments.class);
        assertEquals(actualComments, newPCommentsPost);
    }

    @Test
    public void deleteComment() {

        int postId = getId("objectCommentsPostPath", "id");

        deleteRequest(given().pathParams("id", postId),
                ConfigurationReader.get("objectCommentsIdPath"));
    }

    @Test
    public void patchNameComment() {
        int postId = getId("objectCommentsPostPath", "id");

        String nameCheckedField = "name";
        String valueCheckedField = "JOHN_DOE";

        sendPatchRequest(
                given().pathParams("id", postId),
                nameCheckedField,
                valueCheckedField,
                ConfigurationReader.get("objectCommentsIdPath"));
    }

    @Test
      public void putNameComment() {

        Response response = getListId("objectCommentsPostPath");
        int id = response.jsonPath().getInt("[0]."+"id");
        int postId = response.jsonPath().getInt("[0]."+"post_id");

        Comments comments = TestDataHelper.createComments(postId);

        comments.setName("John_Doe");


        Comments actualComments =
                ApiWrapper.sendPutRequest(
                        given().pathParams("id", id),
                        ConfigurationReader.get("objectCommentsIdPath"),
                        comments,
                        Comments.class);

        assertEquals(actualComments, comments);
    }
}
