package POJO;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Comment {
    private int id;
    private int postId;
    private String name;
    private String email;
    private String body;

    public int getID() {
        return id;
    }

    public void setID(int value) {
        this.id = value;
    }

    public int getPostId() {
        return postId;
    }

    @JsonSetter("post_id")
    public void setPostId(int value) {
        this.postId = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String value) {
        this.body = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Comment comments = (Comment) obj;

        return getPostId() == comments.getPostId() &&
                getName().equals(comments.getName()) &&
                getBody().equals(comments.getBody()) &&
                getEmail().equals(comments.getEmail());
    }

    @Override
    public String toString() {
        return "Comments{" +
                "id=" + id +
                ", postID=" + postId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
