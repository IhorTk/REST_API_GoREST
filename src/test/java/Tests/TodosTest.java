package Tests;

import Utils.ApiWrapper;
import Utils.TestDataHelper;
import io.restassured.response.Response;
import org.example.Todos;
import org.junit.jupiter.api.Test;

import static Utils.ApiWrapper.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;

public class TodosTest extends BaseHomeWorkTest {

    @Test
    public void schemeTodosValidationTest() {
        int userId = getId("objectToDosPostPath", "user_id");

        sendGetRequest(
                given().pathParams("id", userId),
                getConfig("objectPathV2")
                        + getConfig("objectIdPath")
                        + getConfig("objectToDosPostPath")
        )
                .assertThat()
                .body(matchesJsonSchemaInClasspath("todos-schema.json"));
    }

    @Test
    public void getListParamObjectsToDos() {
        String page = "2";
        String perPage = "30";

        sendGetRequest(
                given().pathParams("page",
                        page, "perPage", perPage),
                getConfig("objectPathV2")
                        + getConfig("objectToDosPostPath")
                        + "?page={page}&per_page={perPage}"
        )
                .assertThat()
                .body("$", hasSize(Integer.parseInt(perPage)));
    }

    @Test
    public void newTodoUsersCreation() {

        int userId = getId("endPointUsers", "id");

        Todos newUsersTodo = TestDataHelper.createTodos(userId);

        Todos actualUsersTodo =
                ApiWrapper.sendPostRequest(
                        given().pathParams("id", userId),
                        getConfig("objectPathV2")
                                + getConfig("objectIdPath")
                                + getConfig("objectToDosPostPath"),
                        newUsersTodo,
                        Todos.class
                );
        assertEquals(actualUsersTodo, newUsersTodo);
    }

    @Test
    public void deleteToDo() {

        int userId = getId("objectToDosPostPath", "id");

        deleteRequest(
                given().pathParams("id", userId),
                getConfig("objectPathV2")
                        + getConfig("objectToDosPostIdPath")
        );
    }

    @Test
    public void patchTitleTodo() {
        int userId = getId("objectToDosPostPath", "id");

        String nameCheckedField = "title";
        String valueCheckedField = "New ToDos";

        sendPatchRequest(
                given().pathParams("id", userId),
                nameCheckedField,
                valueCheckedField,
                getConfig("objectPathV2")
                        + getConfig("objectToDosPostIdPath")
        );
    }

    @Test

    public void putTitleTodo() {

        Response response = getListId("objectToDosPostPath");
        int userId = response.jsonPath().getInt("[0]."+"user_id");
        int id = response.jsonPath().getInt("[0]."+"id");

        Todos newTodos = TestDataHelper.createTodos(userId);
        newTodos.setTitle("BORIS");
        newTodos.setID(id);
        Todos actualTodos =
                sendPutRequest(
                        given().pathParams("id", id),
                        getConfig("objectPathV2")
                                + getConfig("objectToDosPostIdPath"),
                        newTodos,
                        Todos.class
                );
        assertEquals(actualTodos, newTodos);
    }
}
