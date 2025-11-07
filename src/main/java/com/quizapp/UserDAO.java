package com.quizapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // ✅ Create a new user (register or seed admin)
    public static void createUser(String username, char[] password, boolean isAdmin) throws SQLException {
        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hash(password, salt);

        String sql = "INSERT INTO users (username, password_hash, salt, is_admin) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, hash);
            ps.setString(3, salt);
            ps.setBoolean(4, isAdmin);
            ps.executeUpdate();

            System.out.println("✅ User created successfully: " + username);
        } catch (SQLException e) {
            System.err.println("❌ Error creating user: " + e.getMessage());
            throw e;
        } finally {
            // wipe password array from memory
            java.util.Arrays.fill(password, '\0');
        }
    }

    // ✅ Find a user by username
    public static User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.id = rs.getInt("id");
                u.username = rs.getString("username");
                u.passwordHash = rs.getString("password_hash");
                u.salt = rs.getString("salt");
                u.isAdmin = rs.getBoolean("is_admin");
                return u;
            }
        }
        return null;
    }

    // ✅ Validate login credentials
    public static boolean validateLogin(String username, char[] password) throws SQLException {
        User u = findByUsername(username);
        if (u == null) return false;

        boolean match = PasswordUtil.verify(password, u.salt, u.passwordHash);

        // Always clear the password array for safety
        java.util.Arrays.fill(password, '\0');

        return match;
    }
}

