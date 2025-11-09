package com.quizapp.frame;

import com.quizapp.DBHelper;
import com.quizapp.model.User;
import com.quizapp.dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JButton registerBtn;
    private JLabel statusLabel;

    public RegisterFrame() {
        setTitle("Register - Online Quiz");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;

        JLabel title = new JLabel("Create an account", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(18);
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(18);
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Confirm:"), gbc);
        gbc.gridx = 1;
        confirmField = new JPasswordField(18);
        panel.add(confirmField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        registerBtn = new JButton("Register");
        panel.add(registerBtn, gbc);

        gbc.gridy++;
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(Color.RED);
        panel.add(statusLabel, gbc);

        add(panel);

        registerBtn.addActionListener(e -> onRegister());
    }

    private void onRegister() {
        String username = usernameField.getText().trim();
        char[] pw = passwordField.getPassword();
        char[] confirm = confirmField.getPassword();

        if (username.isEmpty()) {
            statusLabel.setText("Enter a username.");
            return;
        }
        if (pw.length == 0) {
            statusLabel.setText("Enter a password.");
            return;
        }
        if (!java.util.Arrays.equals(pw, confirm)) {
            statusLabel.setText("Passwords do not match.");
            java.util.Arrays.fill(confirm, '\0');
            return;
        }

        // basic password strength (optional)
        if (pw.length < 6) {
            statusLabel.setText("Password must be at least 6 characters.");
            java.util.Arrays.fill(confirm, '\0');
            return;
        }

        try {
            boolean created = UserDAO.registerUser(username, pw);
            if (created) {
                statusLabel.setForeground(new Color(0,128,0));
                statusLabel.setText("Registration successful! You may login.");
                // optionally auto-close after short delay:
                Timer t = new Timer(1200, ev -> dispose());
                t.setRepeats(false);
                t.start();
            } else {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Username already exists.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Registration error: " + ex.getMessage());
        } finally {
            java.util.Arrays.fill(pw, '\0');
            java.util.Arrays.fill(confirm, '\0');
        }
    }
}
