package com.quizapp;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class QuizListFrame extends JFrame {
    private JTable quizTable;
    private JButton takeQuizBtn;
    private JButton refreshBtn;
    private JButton backBtn;

    public QuizListFrame() {
        setTitle("Available Quizzes");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadQuizzes();
    }

    private void initUI() {
        setLayout(new BorderLayout(10,10));

        // Table
        quizTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(quizTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel bottomPanel = new JPanel();
        takeQuizBtn = new JButton("Take Quiz");
        refreshBtn = new JButton("Refresh");
        backBtn = new JButton("Back");

        bottomPanel.add(takeQuizBtn);
        bottomPanel.add(refreshBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        takeQuizBtn.addActionListener(e -> onTakeQuiz());
        refreshBtn.addActionListener(e -> loadQuizzes());
        backBtn.addActionListener(e -> {
            if (Main.CURRENT_USER != null && Main.CURRENT_USER.isAdmin) {
                new AdminFrame().setVisible(true);
            }
            dispose();
        });
    }

    private void loadQuizzes() {
        try {
            List<Quiz> quizzes = QuizDAO.getAllQuizzes();
            String[] cols = {"ID", "Title", "Description"};
            Object[][] data = new Object[quizzes.size()][3];
            for (int i = 0; i < quizzes.size(); i++) {
                Quiz q = quizzes.get(i);
                data[i][0] = q.id;
                data[i][1] = q.title;
                data[i][2] = q.description;
            }
            quizTable.setModel(new javax.swing.table.DefaultTableModel(data, cols));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading quizzes: " + e.getMessage());
        }
    }

    private void onTakeQuiz() {
        int row = quizTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a quiz to take");
            return;
        }
        int quizId = (int) quizTable.getValueAt(row, 0);
        String quizTitle = (String) quizTable.getValueAt(row, 1);
        new TakeQuizFrame(quizId, quizTitle).setVisible(true);
        dispose();
    }
}
