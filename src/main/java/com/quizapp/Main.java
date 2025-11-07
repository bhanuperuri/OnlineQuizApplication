package com.quizapp;

import javax.swing.SwingUtilities;

public class Main {
    public static User CURRENT_USER = null;

    public static void main(String[] args) {
        // 1️⃣ Initialize database (creates tables automatically)
        DBHelper.init();

        // 2️⃣ Seed a default admin user (only once)
        try {
            if (UserDAO.findByUsername("admin") == null) {
                // Replace password here if you want a different one
                UserDAO.createUser("admin", "Bhanu@7842805122".toCharArray(), true);
                System.out.println("✅ Seeded admin user (username: admin).");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to seed admin user:");
            e.printStackTrace();
        }

        // 3️⃣ Open the login window
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
