package Utils;

import com.github.javafaker.Faker;
import org.example.Comments;
import org.example.NewUser;
import org.example.Post;
import org.example.Todos;

import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;


public class TestDataHelper {
    static Faker faker = new Faker();

    public static NewUser createUser() {
        NewUser user = new NewUser();
        user.setName(faker.name().firstName() + faker.name().lastName() + now());
        user.setEmail(faker.internet().emailAddress());
        user.setGender((faker.demographic().sex()).toLowerCase());
        user.setStatus(faker.random().nextBoolean() ? "active" : "inactive");

        return user;
    }

    public static Comments createComments(int postId) {
        Comments comments = new Comments();
        comments.setPostID(postId);
        comments.setName(faker.name().firstName() + faker.name().lastName() + now());
        comments.setEmail(faker.internet().emailAddress());
        comments.setBody(faker.lorem().sentence(faker.random().nextInt(10, 40)));

        return comments;
    }

    public static Post createPost(int userID) {
        Post post = new Post();
        post.setUserId(userID);
        post.setTitle(faker.lorem().sentence(faker.random().nextInt(2, 6)));
        post.setBody(faker.lorem().sentence(faker.random().nextInt(8, 18)));

        return post;
    }

    public static Todos createTodos(int userID) {

        Todos todos = new Todos();

        todos.setUserID(userID);
        todos.setTitle(faker.lorem().sentence(faker.random().nextInt(2, 6)));
        todos.setStatus(faker.random().nextBoolean() ? "pending" : "completed");
        todos.setDueOn(now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));

        return todos;
    }
}


