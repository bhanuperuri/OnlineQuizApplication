package com.quizapp.frame;

import com.quizapp.Main;
import com.quizapp.model.User;
import com.quizapp.dao.UserDAO;

import javax.swing.*;
import java.awt.*;

/**
 * Full LoginFrame with Register button.
 * Expects: UserDAO.loginUser(String username, String password) -> User or null
 *          UserDAO.userExists(String username) and/or UserDAO.registerUser(...) used by RegisterFrame.
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;

    public LoginFrame() {
        setTitle("Online Quiz - Login");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("Welcome — Please login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        // Username
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(18);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(18);
        panel.add(passwordField, gbc);

        // Buttons: Login + Register
        gbc.gridy++;
        gbc.gridx = 0;
        loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        gbc.gridx = 1;
        registerButton = new JButton("Register");
        panel.add(registerButton, gbc);

        // Status label (spans 2 columns)
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(Color.RED);
        panel.add(statusLabel, gbc);

        add(panel);

        // Actions
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> {
            // open register window (non-modal)
            RegisterFrame rf = new RegisterFrame();
            rf.setVisible(true);
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        char[] pwChars = passwordField.getPassword();
        String password = new String(pwChars); // convert for DAO call; wipe later

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠️ Enter username and password");
            java.util.Arrays.fill(pwChars, '\0');
            return;
        }

        try {
            // Call into your DAO. Expectation: returns User on success, null otherwise.
            User user = UserDAO.loginUser(username, password);

            if (user != null) {
                Main.CURRENT_USER = user;
                statusLabel.setForeground(new Color(0,128,0));
                statusLabel.setText("Login successful — Welcome " + user.getUsername() + "!");
                // short delay then open next screen
                Timer t = new Timer(700, ev -> {
                    // close login and open next frame
                    dispose();
                    if (user.isAdmin()) {
                        new AdminFrame().setVisible(true);
                    } else {
                        new QuizListFrame().setVisible(true);
                    }
                });
                t.setRepeats(false);
                t.start();
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("❌ Invalid username or password");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Login error: " + ex.getMessage());
        } finally {
            // best-effort wipe
            java.util.Arrays.fill(pwChars, '\0');
        }
    }
}


