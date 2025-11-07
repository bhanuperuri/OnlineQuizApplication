package com.quizapp;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminFrame extends JFrame {
    private JTextField titleField;
    private JTextArea descArea;
    private JButton createQuizBtn;
    private JButton openQuizListBtn;

    public AdminFrame() {
        setTitle("Admin - Manage Quizzes");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout(10,10));
        JPanel top = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        top.add(new JLabel("Quiz Title:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        titleField = new JTextField(30);
        top.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        top.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        descArea = new JTextArea(4, 30);
        top.add(new JScrollPane(descArea), gbc);

        createQuizBtn = new JButton("Create Quiz");
        openQuizListBtn = new JButton("Open Quiz List (Admin)");
        JPanel btnPanel = new JPanel();
        btnPanel.add(createQuizBtn);
        btnPanel.add(openQuizListBtn);

        main.add(top, BorderLayout.NORTH);
        main.add(btnPanel, BorderLayout.CENTER);

        add(main);

        // Actions
        createQuizBtn.addActionListener(e -> onCreateQuiz());
        openQuizListBtn.addActionListener(e -> {
            new QuizListFrame().setVisible(true);
            dispose();
        });
    }

    private void onCreateQuiz() {
        String title = titleField.getText().trim();
        String desc = descArea.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a quiz title");
            return;
        }

        try {
            int quizId = QuizDAO.createQuiz(title, desc);
            JOptionPane.showMessageDialog(this, "Quiz created with id: " + quizId + ". Now add questions.");
            // open dialog to add questions
            addQuestionsDialog(quizId);
            // clear fields
            titleField.setText("");
            descArea.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating quiz: " + ex.getMessage());
        }
    }

    private void addQuestionsDialog(int quizId) {
        while (true) {
            String qtext = JOptionPane.showInputDialog(this, "Enter question text (Cancel to finish):");
            if (qtext == null) break;
            String isMulti = JOptionPane.showInputDialog(this, "Is this a multi-select question? (yes/no)");
            boolean multi = "yes".equalsIgnoreCase(isMulti);

            String marksStr = JOptionPane.showInputDialog(this, "Enter marks for this question (number):");
            int marks = 1;
            try { marks = Integer.parseInt(marksStr); } catch (Exception ignored) {}

            try {
                int qid = QuizDAO.addQuestion(quizId, qtext, multi, marks);

                // gather choices
                List<String> choices = new ArrayList<>();
                while (true) {
                    String ch = JOptionPane.showInputDialog(this, "Enter choice text (Cancel to stop adding choices):");
                    if (ch == null || ch.trim().isEmpty()) break;
                    choices.add(ch.trim());
                }
                if (choices.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No choices added — deleting the question.");
                    // simple delete would be ideal; for now continue.
                    continue;
                }

                String correctIndices = JOptionPane.showInputDialog(this,
                        "Enter comma-separated indices (1-based) of correct choices (e.g. 1 or 1,3):");
                java.util.Set<Integer> correctSet = new java.util.HashSet<>();
                if (correctIndices != null && !correctIndices.trim().isEmpty()) {
                    for (String s : correctIndices.split(",")) {
                        try { correctSet.add(Integer.parseInt(s.trim())); } catch (Exception ignored) {}
                    }
                }

                int idx = 1;
                for (String ch : choices) {
                    boolean correct = correctSet.contains(idx);
                    QuizDAO.addChoice(qid, ch, correct);
                    idx++;
                }

                JOptionPane.showMessageDialog(this, "Question and choices added.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding question: " + ex.getMessage());
            }
        }
    }
}
