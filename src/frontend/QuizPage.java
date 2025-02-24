package frontend;

import backend.QuizData;
import backend.ScoreManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Handles quiz gameplay.
 */
public class QuizPage extends JFrame {
    private int playerID;
    private String difficulty;
    private int score = 0, questionIndex = 0;
    private List<QuizData.Question> questions;
    private JLabel questionLabel;
    private JRadioButton[] options = new JRadioButton[4];
    private ButtonGroup optionsGroup;
    private JButton nextButton;

    public QuizPage(int playerID, String difficulty) {
        this.playerID = playerID;
        this.difficulty = difficulty;

        // 🔎 Load Questions and Debug
        this.questions = QuizData.getRandomQuestions(difficulty, 10);
        System.out.println("Loaded Questions: " + questions);

        if (questions == null || questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions available for this difficulty.", "Error", JOptionPane.ERROR_MESSAGE);
            new TheDifficultyPage(playerID).setVisible(true);
            dispose();
            return;
        }

        setTitle("Quiz Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 183, 70));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BorderLayout());

        // Question Label
        questionLabel = new JLabel("Loading question...");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(questionLabel, BorderLayout.NORTH);

        // Options Panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1));
        optionsPanel.setBackground(Color.WHITE);
        optionsGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Arial", Font.PLAIN, 14));
            optionsGroup.add(options[i]);
            optionsPanel.add(options[i]);
        }

        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        // Next Button
        nextButton = new JButton("Next");
        nextButton.setBackground(new Color(232, 133, 69));
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.addActionListener(this::checkAnswer);
        mainPanel.add(nextButton, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        loadQuestion();
    }

    private void loadQuestion() {
        if (questionIndex < questions.size()) {
            QuizData.Question q = questions.get(questionIndex);
            System.out.println("Loading Question: " + q.text); // 🔎 Debug

            questionLabel.setText((questionIndex + 1) + ". " + q.text);
            options[0].setText("A) " + q.optionA);
            options[1].setText("B) " + q.optionB);
            options[2].setText("C) " + q.optionC);
            options[3].setText("D) " + q.optionD);

            optionsGroup.clearSelection();
        } else {
            endQuiz();
        }
    }

    private void checkAnswer(ActionEvent e) {
        if (questionIndex < questions.size()) {
            QuizData.Question q = questions.get(questionIndex);
            String correctOption = String.valueOf(q.correctOption);

            boolean answered = false;
            for (int i = 0; i < 4; i++) {
                if (options[i].isSelected()) {
                    answered = true;
                    String selectedOption = options[i].getText().substring(0, 1);
                    if (selectedOption.equals(correctOption)) {
                        score++;
                    }
                    break;
                }
            }

            if (!answered) {
                JOptionPane.showMessageDialog(this, "Please select an answer before proceeding.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            questionIndex++;
            loadQuestion();
        }
    }

    private void endQuiz() {
        ScoreManager.saveScore(playerID, score, difficulty);
        JOptionPane.showMessageDialog(this, "Quiz Over! Your score: " + score);

        int choice = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Quiz Finished", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            new QuizPage(playerID, difficulty).setVisible(true);
        } else {
            new TheHomePage().setVisible(true);
        }

        dispose();
    }
}
