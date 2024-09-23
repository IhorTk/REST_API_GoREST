package PODJO;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Comments {
    private int id;
    private int postID;
    private String name;
    private String email;
    private String body;

    public int getID() {
        return id;
    }

    public void setID(int value) {
        this.id = value;
    }

    public int getPostID() {
        return postID;
    }

    @JsonSetter("post_id")
    public void setPostID(int value) {
        this.postID = value;
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

        Comments comments = (Comments) obj;

        return getPostID() == comments.getPostID() &&
                getName().equals(comments.getName()) &&
                getBody().equals(comments.getBody()) &&
                getEmail().equals(comments.getEmail());
    }

    @Override
    public String toString() {
        return "Comments{" +
                "id=" + id +
                ", postID=" + postID +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
