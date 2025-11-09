package com.quizapp.frame;

import com.quizapp.model.Choice;
import com.quizapp.model.Question;
import com.quizapp.dao.QuizDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TakeQuizFrame extends JFrame {
    private int quizId;
    private String quizTitle;
    private List<Question> questions;
    private int currentIndex = 0;
    private List<List<Integer>> userAnswers = new ArrayList<>();

    private JLabel titleLabel;
    private JTextArea questionArea;
    private JPanel choicesPanel;
    private JButton nextButton;
    private JButton prevButton;
    private JButton submitButton;

    public TakeQuizFrame(int quizId, String quizTitle) {
        this.quizId = quizId;
        this.quizTitle = quizTitle;

        setTitle("Taking Quiz - " + quizTitle);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initUI();

        loadQuestions();
        if (questions != null && !questions.isEmpty()) {
            showQuestion(0);
        } else {
            JOptionPane.showMessageDialog(this, "No questions found for this quiz.");
            dispose();
        }
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("Quiz: " + quizTitle, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        questionArea = new JTextArea(4, 50);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setEditable(false);

        choicesPanel = new JPanel();
        choicesPanel.setLayout(new BoxLayout(choicesPanel, BoxLayout.Y_AXIS));

        JPanel btnPanel = new JPanel();
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit Quiz");
        btnPanel.add(prevButton);
        btnPanel.add(nextButton);
        btnPanel.add(submitButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(questionArea), BorderLayout.CENTER);
        add(new JScrollPane(choicesPanel), BorderLayout.EAST);
        add(btnPanel, BorderLayout.SOUTH);

        prevButton.addActionListener(e -> showPrevious());
        nextButton.addActionListener(e -> showNext());
        submitButton.addActionListener(e -> onSubmit());
    }

    private void loadQuestions() {
        try {
            questions = QuizDAO.getQuestionsForQuiz(quizId);
            for (int i = 0; i < questions.size(); i++) {
                userAnswers.add(new ArrayList<>());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading questions: " + e.getMessage());
        }
    }

    private void showQuestion(int index) {
        if (questions == null || index < 0 || index >= questions.size()) return;
        currentIndex = index;
        Question q = questions.get(index);

        questionArea.setText((index + 1) + ". " + q.text + " (" + (q.isMulti ? "Multiple" : "Single") + ")");
        choicesPanel.removeAll();

        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < q.choices.size(); i++) {
            Choice c = q.choices.get(i);
            JCheckBox cb = new JCheckBox(c.text);
            if (userAnswers.get(index).contains(i)) {
                cb.setSelected(true);
            }
            int choiceIndex = i;
            cb.addActionListener(e -> {
                if (q.isMulti) {
                    if (cb.isSelected()) userAnswers.get(index).add(choiceIndex);
                    else userAnswers.get(index).remove(Integer.valueOf(choiceIndex));
                } else {
                    userAnswers.get(index).clear();
                    if (cb.isSelected()) userAnswers.get(index).add(choiceIndex);
                    // unselect other checkboxes if single choice
                    Component[] comps = choicesPanel.getComponents();
                    for (Component comp : comps) {
                        if (comp instanceof JCheckBox && comp != cb) {
                            ((JCheckBox) comp).setSelected(false);
                        }
                    }
                }
            });
            choicesPanel.add(cb);
            if (!q.isMulti) group.add(cb);
        }
        choicesPanel.revalidate();
        choicesPanel.repaint();
    }

    private void showNext() {
        if (currentIndex < questions.size() - 1) {
            showQuestion(currentIndex + 1);
        }
    }

    private void showPrevious() {
        if (currentIndex > 0) {
            showQuestion(currentIndex - 1);
        }
    }

    private void onSubmit() {
        int score = 0;
        int total = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            total += q.marks;
            boolean correct = true;
            for (int j = 0; j < q.choices.size(); j++) {
                boolean shouldBe = q.choices.get(j).isCorrect;
                boolean selected = userAnswers.get(i).contains(j);
                if (shouldBe != selected) {
                    correct = false;
                    break;
                }
            }
            if (correct) score += q.marks;
        }
        JOptionPane.showMessageDialog(this, "You scored " + score + " out of " + total + "!");
        dispose();
        new QuizListFrame().setVisible(true);
    }
}
