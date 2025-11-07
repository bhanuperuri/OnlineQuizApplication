package com.quizapp;

import java.util.List;

public class Question {
    public int id;
    public int quizId;
    public String text;
    public boolean isMulti;
    public int marks;
    public List<Choice> choices;

    public Question() {}
}
