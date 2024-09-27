package Utils;

import PODJO.Comment;
import PODJO.Post;
import PODJO.Todos;
import PODJO.User;
import com.github.javafaker.Faker;


import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;


public class PODJODataHelper {
    static Faker faker = new Faker();

    public static User createUser() {
        User user = new User();
        user.setName(faker.name().firstName() + faker.name().lastName() + now());
        user.setEmail(faker.internet().emailAddress());
        user.setGender((faker.demographic().sex()).toLowerCase());
        user.setStatus(faker.random().nextBoolean() ? "active" : "inactive");

        return user;
    }

    public static Comment createComments(int postId) {
        Comment comments = new Comment();
        comments.setPostId(postId);
        comments.setName(faker.name().firstName() + faker.name().lastName() + now());
        comments.setEmail(faker.internet().emailAddress());
        comments.setBody(faker.lorem().sentence(faker.random().nextInt(10, 40)));

        return comments;
    }

    public static Post createPost(int userId) {
        Post post = new Post();
        post.setUserId(userId);
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


