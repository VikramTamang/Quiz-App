package frontend;

import javax.swing.*;
import java.awt.*;

/**
 * Handles difficulty selection for the quiz.
 */
public class TheDifficultyPage extends JFrame {
    private int playerID;

    public TheDifficultyPage(int playerID) {
        this.playerID = playerID;

        setTitle("Select Difficulty");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));

        JButton beginnerBtn = new JButton("Beginner");
        JButton intermediateBtn = new JButton("Intermediate");
        JButton advancedBtn = new JButton("Advanced");

        beginnerBtn.addActionListener(e -> confirmStart("Beginner"));
        intermediateBtn.addActionListener(e -> confirmStart("Intermediate"));
        advancedBtn.addActionListener(e -> confirmStart("Advanced"));

        add(beginnerBtn);
        add(intermediateBtn);
        add(advancedBtn);
    }

    private void confirmStart(String difficulty) {
        int response = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to start the quiz on " + difficulty + " difficulty?", 
            "Confirm Difficulty", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            new QuizPage(playerID, difficulty).setVisible(true);
            dispose();
        }
    }
}
