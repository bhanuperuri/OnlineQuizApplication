package com.quizapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginFrame() {
        setTitle("Online Quiz Application - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel setup
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        gbc.gridy++;
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(Color.RED);
        panel.add(statusLabel, gbc);

        add(panel);

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (username.isEmpty() || password.length == 0) {
            statusLabel.setText("⚠️ Please enter username and password");
            return;
        }

        try {
            boolean valid = UserDAO.validateLogin(username, password);
            if (valid) {
                User user = UserDAO.findByUsername(username);
                Main.CURRENT_USER = user;

                JOptionPane.showMessageDialog(this, "Welcome, " + user.username + "!");
                dispose(); // Close login window

                // If admin -> open admin page
                if (user.isAdmin) {
                    new AdminFrame().setVisible(true);
                } else {
                    new QuizListFrame().setVisible(true);
                }
            } else {
                statusLabel.setText("❌ Invalid credentials");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("⚠️ Login error: " + ex.getMessage());
        } finally {
            java.util.Arrays.fill(password, '\0');
        }
    }
}

