package com.quizapp.model;

public class Quiz {
    public int id;
    public String title;
    public String description;

    public Quiz() {}

    public Quiz(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Quiz{id=" + id + ", title='" + title + "', description='" + description + "'}";
    }
}
