package com.quizapp;

public class User {
    public int id;
    public String username;
    public String passwordHash;
    public String salt;
    public boolean isAdmin;

    // Optional convenience constructor
    public User() {}

    public User(int id, String username, String passwordHash, String salt, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', isAdmin=" + isAdmin + "}";
    }
}
