package com.quizapp.dao;

import com.quizapp.model.Choice;
import com.quizapp.DBHelper;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    // Create a quiz and return generated id
    public static int createQuiz(String title, String description) throws SQLException {
        String sql = "INSERT INTO quizzes (title, description) VALUES (?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    // Return all quizzes (used by QuizListFrame)
    public static List<Quiz> getAllQuizzes() throws SQLException {
        List<Quiz> out = new ArrayList<>();
        String sql = "SELECT id, title, description FROM quizzes ORDER BY id DESC";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Quiz q = new Quiz();
                q.id = rs.getInt("id");
                q.title = rs.getString("title");
                q.description = rs.getString("description");
                out.add(q);
            }
        }
        return out;
    }

    // Add a question to a quiz and return question id
    public static int addQuestion(int quizId, String text, boolean isMulti, int marks) throws SQLException {
        String sql = "INSERT INTO questions (quiz_id, text, is_multi, marks) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, quizId);
            ps.setString(2, text);
            ps.setInt(3, isMulti ? 1 : 0);
            ps.setInt(4, marks);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    // Add a choice for a question
    public static void addChoice(int questionId, String text, boolean isCorrect) throws SQLException {
        String sql = "INSERT INTO choices (question_id, text, is_correct) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            ps.setString(2, text);
            ps.setInt(3, isCorrect ? 1 : 0);
            ps.executeUpdate();
        }
    }

    // Get list of questions for a quiz (each question includes its choices)
    public static List<Question> getQuestionsForQuiz(int quizId) throws SQLException {
        List<Question> out = new ArrayList<>();
        String qSql = "SELECT id, text, is_multi, marks FROM questions WHERE quiz_id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement qps = conn.prepareStatement(qSql)) {
            qps.setInt(1, quizId);
            try (ResultSet qrs = qps.executeQuery()) {
                while (qrs.next()) {
                    Question q = new Question();
                    q.id = qrs.getInt("id");
                    q.quizId = quizId;
                    q.text = qrs.getString("text");
                    q.isMulti = qrs.getInt("is_multi") == 1;
                    q.marks = qrs.getInt("marks");
                    q.choices = getChoicesForQuestion(conn, q.id);
                    out.add(q);
                }
            }
        }
        return out;
    }

    // Helper: get choices for a question (re-uses the same connection)
    private static List<Choice> getChoicesForQuestion(Connection conn, int questionId) throws SQLException {
        List<Choice> out = new ArrayList<>();
        String sql = "SELECT id, text, is_correct FROM choices WHERE question_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Choice c = new Choice();
                    c.id = rs.getInt("id");
                    c.questionId = questionId;
                    c.text = rs.getString("text");
                    c.isCorrect = rs.getInt("is_correct") == 1;
                    out.add(c);
                }
            }
        }
        return out;
    }
}
