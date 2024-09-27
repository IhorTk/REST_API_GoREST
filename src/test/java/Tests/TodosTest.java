package Tests;

import POJO.Todos;
import Utils.ApiWrapper;
import Utils.ConfigurationReader;
import Utils.POJODataHelper;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static Utils.ApiWrapper.*;
import static Utils.TestDataHelper.getId;
import static Utils.TestDataHelper.getListId;
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
    public void createToDosTest() {

        int userId = getId("userPath", "id");

        Todos newTodo = POJODataHelper.createTodos(userId);

        Todos responseTodo =
                ApiWrapper.sendPostRequest(
                        given().pathParams("id", userId),
                        ConfigurationReader.get("userIdPath")
                                + ConfigurationReader.get("toDosPath"),
                        newTodo,
                        Todos.class);
        assertEquals(responseTodo, newTodo);
    }


    @Test
    public void renameToDosTest() {
        int toDoId = getId("toDosPath", "id");

        String nameField = ConfigurationReader.get("toDosFieldName");
        String valueField = ConfigurationReader.get("toDosFieldValue");

        sendPatchRequest(
                given().pathParams("id", toDoId),
                nameField,
                valueField,
                ConfigurationReader.get("toDosIdPath"));
    }


    @Test
    public void changeTodoTest() {

        Response response = getListId("toDosPath");
        int userId = response.jsonPath().getInt("[0]."+"user_id");
        int id = response.jsonPath().getInt("[0]."+"id");

        Todos newTodos = POJODataHelper.createTodos(userId);
        newTodos.setTitle(ConfigurationReader.get("toDosTitle"));
        newTodos.setId(id);
        Todos actualTodos =
                sendPutRequest(
                        given().pathParams("id", id),
                        ConfigurationReader.get("toDosIdPath"),
                        newTodos,
                        Todos.class);
        assertEquals(actualTodos, newTodos);
    }


    @Test
    public void getListToDosByParametersTest() {
        String numPage = ConfigurationReader.get("toDosNumPage");
        String countToDos = ConfigurationReader.get("toDosCount");

        sendGetRequest(
                given().pathParams("numPage",
                        numPage, "countToDos", countToDos),
                ConfigurationReader.get("toDosPath")
                        + "?page={numPage}&per_page={countToDos}")
                .assertThat()
                .body("$", hasSize(Integer.parseInt(countToDos)));
    }


    @Test
    public void deleteToDosTest() {

        int userId = getId("toDosPath", "id");

        deleteRequest(
                given().pathParams("id", userId),
                ConfigurationReader.get("toDosIdPath"));
    }
}
