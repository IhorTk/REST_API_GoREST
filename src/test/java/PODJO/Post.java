package PODJO;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Post {
    private int id;
    private int userId;
    private String title;
    private String body;

    public int getID() { return id; }
    public void setID(int value) { this.id = value; }


    public int getUserId() {
        return userId;
    }
    @JsonSetter("user_id")
    public void setUserId(int userID) {
        this.userId = userID;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Post post = (Post) obj;

        return getUserId() == post.getUserId() &&
                getTitle().equals(post.getTitle()) &&
                getBody().equals(post.getBody());
    }

    @Override
    public String toString() {
        return "Post {" +
                "id = " + id + "'," +
                "userId = " + userId + "'," +
                "title = '" + title + "'," +
                "body = '" + body + '\'' +
                '}';
    }
}
