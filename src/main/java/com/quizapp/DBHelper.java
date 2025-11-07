package com.quizapp;

import java.sql.*;

public class DBHelper {

    // Connection details
    private static final String URL = "jdbc:mysql://localhost:3306/quizapp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "quizuser";      // your MySQL username
    private static final String PASSWORD = "quizpass";  // your MySQL password

    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver Loaded Successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found! Add the jar in your lib folder.");
            e.printStackTrace();
        }
    }

    // Get connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Initialize tables
    public static void init() {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(100) UNIQUE NOT NULL," +
                    "password_hash VARCHAR(255) NOT NULL," +
                    "salt VARCHAR(255) NOT NULL," +
                    "is_admin TINYINT DEFAULT 0" +
                    ")");

            s.execute("CREATE TABLE IF NOT EXISTS quizzes (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(255) NOT NULL," +
                    "description TEXT" +
                    ")");

            s.execute("CREATE TABLE IF NOT EXISTS questions (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "quiz_id BIGINT," +
                    "text TEXT NOT NULL," +
                    "is_multi TINYINT DEFAULT 0," +
                    "marks INT DEFAULT 1," +
                    "FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE" +
                    ")");

            s.execute("CREATE TABLE IF NOT EXISTS choices (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "question_id BIGINT," +
                    "text TEXT NOT NULL," +
                    "is_correct TINYINT DEFAULT 0," +
                    "FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE" +
                    ")");

            s.execute("CREATE TABLE IF NOT EXISTS attempts (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "user_id BIGINT," +
                    "quiz_id BIGINT," +
                    "score DOUBLE," +
                    "total DOUBLE," +
                    "started_at VARCHAR(50)," +
                    "finished_at VARCHAR(50)," +
                    "FOREIGN KEY (user_id) REFERENCES users(id)," +
                    "FOREIGN KEY (quiz_id) REFERENCES quizzes(id)" +
                    ")");

            s.execute("CREATE TABLE IF NOT EXISTS attempt_answers (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "attempt_id BIGINT," +
                    "question_id BIGINT," +
                    "selected TEXT," +
                    "is_correct TINYINT," +
                    "marks_awarded DOUBLE," +
                    "FOREIGN KEY (attempt_id) REFERENCES attempts(id)" +
                    ")");

            System.out.println("✅ Database tables created/verified successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Database initialization failed:");
            e.printStackTrace();
        }
    }
}
