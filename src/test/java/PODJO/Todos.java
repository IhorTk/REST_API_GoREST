package PODJO;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Todos {
    private int id;
    private int userId;
    private String title;
    private String dueOn;
    private String status;

    public int getId() { return id; }
    public void setId(int value) { this.id = value; }

    public int getUserID() { return userId; }

    @JsonSetter("user_id")
    public void setUserID(int value) { this.userId = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    @JsonSetter("due_on")
    public String getDueOn() { return dueOn; }

    @JsonSetter("due_on")
    public void setDueOn(String value) { this.dueOn = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Todos todos = (Todos) obj;

        return getUserID() == todos.getUserID() &&
                getStatus().equals(todos.getStatus()) &&
                getTitle().equals(todos.getTitle())  &&
                getDueOn().contains(todos.getDueOn());
    }

    @Override
    public String toString() {
        return "Todos{" +
                "id=" + id +
                ", userID=" + userId +
                ", title='" + title + '\'' +
                ", dueOn='" + dueOn + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}

