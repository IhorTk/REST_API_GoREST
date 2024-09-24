package Tests;

import PODJO.Todos;
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

public class TodosTest extends BaseTestCase {

    @Test
    public void schemeTodosValidationTest() {
        int userId = getId("toDosPath", "user_id");

        sendGetRequest(
                given().pathParams("id", userId),
                ConfigurationReader.get("userIdPath")
                        + ConfigurationReader.get("toDosPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemaToDos.json"));
    }

    @Test
    public void getListParamObjectsToDos() {
        String page = "2";
        String perPage = "30";

        sendGetRequest(
                given().pathParams("page",
                        page, "perPage", perPage),
                ConfigurationReader.get("toDosPath")
                        + "?page={page}&per_page={perPage}")
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
                        ConfigurationReader.get("userIdPath")
                                + ConfigurationReader.get("toDosPath"),
                        newUsersTodo,
                        Todos.class);
        assertEquals(actualUsersTodo, newUsersTodo);
    }

    @Test
    public void deleteToDo() {

        int userId = getId("toDosPath", "id");

        deleteRequest(
                given().pathParams("id", userId),
                ConfigurationReader.get("toDosIdPath"));
    }

    @Test
    public void patchTitleTodo() {
        int userId = getId("toDosPath", "id");

        String nameCheckedField = "title";
        String valueCheckedField = "New ToDos";

        sendPatchRequest(
                given().pathParams("id", userId),
                nameCheckedField,
                valueCheckedField,
                ConfigurationReader.get("toDosIdPath"));
    }

    @Test

    public void putTitleTodo() {

        Response response = getListId("toDosPath");
        int userId = response.jsonPath().getInt("[0]."+"user_id");
        int id = response.jsonPath().getInt("[0]."+"id");

        Todos newTodos = TestDataHelper.createTodos(userId);
        newTodos.setTitle("John_Doe");
        newTodos.setId(id);
        Todos actualTodos =
                sendPutRequest(
                        given().pathParams("id", id),
                        ConfigurationReader.get("toDosIdPath"),
                        newTodos,
                        Todos.class);
        assertEquals(actualTodos, newTodos);
    }
}
