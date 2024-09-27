package Tests;

import PODJO.Comment;
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

public class CommentsTest extends BaseTestCase {

    @Test
    public void schemeCommentsValidationTest() {
        sendGetRequest(ConfigurationReader.get("commentsPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemaComment.json"));
    }

    @Test
    public void createNewCommentForPostTest() {
        int postId = TestDataHelper.getId("commentsPath", "post_id");

        Comment newComment = PODJODataHelper.createComments(postId);

        Comment responseComment =
                ApiWrapper.sendPostRequest(
                        given().pathParams("id", postId),
                        ConfigurationReader.get("postIdPath")
                                + ConfigurationReader.get("commentsPath"),
                        newComment,
                        Comment.class);
        assertEquals(responseComment, newComment);
    }

    @Test
    public void getListCommentsByParametersTest() {
        String numPage = ConfigurationReader.get("commentNumPage");
        String countComments = ConfigurationReader.get("commentCount");

        sendGetRequest(
                given().pathParams("numPage",
                        numPage, "countComments", countComments),
                 ConfigurationReader.get("commentsPath")
                        + "?page={numPage}&per_page={countComments}")
                .assertThat()
                .body("$", hasSize(Integer.parseInt(countComments)));
    }


    @Test
    public void changeNameCommentTest() {
        int postId = TestDataHelper.getId("commentsPath", "id");

        String nameField = ConfigurationReader.get("commentFieldName");
        String valueField = ConfigurationReader.get("commentFieldValue");

        sendPatchRequest(
                given().pathParams("id", postId),
                nameField,
                valueField,
                ConfigurationReader.get("commentsIdPath"));
    }

    @Test
      public void updateCommentTest() {

        Response response = getListId("commentsPath");
        int id = response.jsonPath().getInt("[0]."+"id");
        int postId = response.jsonPath().getInt("[0]."+"post_id");

        Comment comments = PODJODataHelper.createComments(postId);
        comments.setName("John_Doe");

        Comment responseComment =
                ApiWrapper.sendPutRequest(
                        given().pathParams("id", id),
                        ConfigurationReader.get("commentsIdPath"),
                        comments,
                        Comment.class);

        assertEquals(responseComment, comments);
    }

    @Test
    public void deleteCommentTest() {

        int postId = TestDataHelper.getId("commentsPath", "id");

        deleteRequest(given().pathParams("id", postId),
                ConfigurationReader.get("commentsIdPath"));
    }
}
