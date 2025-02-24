package frontend;

import javax.swing.*;
import java.awt.*;

/**
 * The main homepage for the quiz application.
 */
public class TheHomePage extends JFrame {
    public TheHomePage() {
        setTitle("Quiz App - Homepage");
        setSize(615, 506);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 183, 100));
        getContentPane().setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome to the Quiz App", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setBounds(106, 10, 386, 50);
        getContentPane().add(welcomeLabel);

        JButton startQuizButton = new JButton("Start Quiz");
        startQuizButton.setBackground(new Color(232, 133, 69));
        startQuizButton.setBounds(106, 70, 386, 50);
        startQuizButton.addActionListener(e -> {
            new TheUserNamePage().setVisible(true);
            dispose();
        });
        getContentPane().add(startQuizButton);

        JButton viewScoresButton = new JButton("View Scores");
        viewScoresButton.setBackground(new Color(232, 133, 69));
        viewScoresButton.setBounds(106, 130, 386, 50);
        viewScoresButton.addActionListener(e -> {
            new DisplayPage().setVisible(true);
        });
        getContentPane().add(viewScoresButton);

        JButton viewReportsButton = new JButton("View Reports");
        viewReportsButton.setBackground(new Color(232, 133, 69));
        viewReportsButton.setBounds(106, 190, 386, 50);
        viewReportsButton.addActionListener(e -> {
            new ReportPage().setVisible(true);
        });
        getContentPane().add(viewReportsButton);

        JButton adminPanelButton = new JButton("Admin Panel");
        adminPanelButton.setBackground(new Color(232, 133, 69));
        adminPanelButton.setBounds(106, 250, 386, 50);
        adminPanelButton.addActionListener(e -> {
            String adminPass = JOptionPane.showInputDialog(this, "Enter Admin Password:");
            if ("1234".equals(adminPass)) {  
                new AdminQnsPage().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Incorrect Password!");
            }
        });
        getContentPane().add(adminPanelButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(232, 133, 69));
        exitButton.setBounds(106, 310, 386, 50);
        exitButton.addActionListener(e -> System.exit(0));
        getContentPane().add(exitButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TheHomePage().setVisible(true));
    }
}
