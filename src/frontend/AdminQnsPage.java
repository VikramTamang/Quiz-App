package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

/**
 * Class class.
 * This class is responsible for handling specific functionalities.
 */
public class AdminQnsPage extends JFrame {

    protected JTextField questionField;
    protected JTextField optionAField, optionBField, optionCField, optionDField;
    protected JComboBox<String> correctOptionBox, difficultyBox;
    protected JButton addButton;

    private static final String URL = "jdbc:mysql://localhost:3306/quiz_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

/**
 * Method AdminQnsPage.
 * Description: This method performs a specific function.
 */
    public AdminQnsPage() {
	getContentPane().setBackground(new Color(255, 183, 70));
        setTitle("Admin - Add Quiz Questions");
        setSize(519, 529);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        // UI Labels & Input Fields
        JLabel label = new JLabel("Question:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        label.setBounds(0, 2, 252, 61);
        getContentPane().add(label);
        questionField = new JTextField();
        questionField.setBounds(252, 2, 252, 61);
        questionField.setBackground(new Color(192, 192, 192));
        getContentPane().add(questionField);

        JLabel label_1 = new JLabel("Option A:");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        label_1.setBounds(0, 63, 252, 61);
        getContentPane().add(label_1);
        optionAField = new JTextField();
        optionAField.setBounds(252, 63, 252, 61);
        optionAField.setBackground(new Color(192, 192, 192));
        getContentPane().add(optionAField);

        JLabel label_2 = new JLabel("Option B:");
        label_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        label_2.setBounds(0, 124, 252, 61);
        getContentPane().add(label_2);
        optionBField = new JTextField();
        optionBField.setBounds(252, 124, 252, 61);
        optionBField.setBackground(new Color(192, 192, 192));
        getContentPane().add(optionBField);

        JLabel label_3 = new JLabel("Option C:");
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        label_3.setFont(new Font("Tahoma", Font.BOLD, 14));
        label_3.setBounds(0, 185, 252, 61);
        getContentPane().add(label_3);
        optionCField = new JTextField();
        optionCField.setBounds(252, 185, 252, 61);
        optionCField.setBackground(new Color(192, 192, 192));
        getContentPane().add(optionCField);

        JLabel label_4 = new JLabel("Option D:");
        label_4.setFont(new Font("Tahoma", Font.BOLD, 14));
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        label_4.setBounds(0, 246, 252, 61);
        getContentPane().add(label_4);
        optionDField = new JTextField();
        optionDField.setBounds(252, 246, 252, 61);
        optionDField.setBackground(new Color(192, 192, 192));
        getContentPane().add(optionDField);

        JLabel label_5 = new JLabel("Correct Option:");
        label_5.setFont(new Font("Tahoma", Font.BOLD, 14));
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        label_5.setBounds(0, 307, 252, 61);
        getContentPane().add(label_5);
        correctOptionBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        correctOptionBox.setBounds(252, 307, 252, 61);
        correctOptionBox.setBackground(new Color(192, 192, 192));
        getContentPane().add(correctOptionBox);

        JLabel label_6 = new JLabel("Difficulty Level:");
        label_6.setHorizontalAlignment(SwingConstants.CENTER);
        label_6.setFont(new Font("Tahoma", Font.BOLD, 14));
        label_6.setBounds(0, 368, 252, 61);
        getContentPane().add(label_6);
        difficultyBox = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"});
        difficultyBox.setBounds(252, 368, 252, 61);
        difficultyBox.setBackground(new Color(192, 192, 192));
        getContentPane().add(difficultyBox);

        addButton = new JButton("Add Question");
        addButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        addButton.setBounds(127, 431, 252, 61);
        addButton.setBackground(new Color(232, 133, 69));
        getContentPane().add(addButton);

        // Action Listener to Save Question
        addButton.addActionListener(this::saveQuestion);

        setVisible(true);
    }

    private void saveQuestion(ActionEvent e) {
        String question = questionField.getText();
        String optionA = optionAField.getText();
        String optionB = optionBField.getText();
        String optionC = optionCField.getText();
        String optionD = optionDField.getText();
        String correctOption = correctOptionBox.getSelectedItem().toString();
        String difficulty = difficultyBox.getSelectedItem().toString();

        if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        String query = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, difficulty) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, question);
            pstmt.setString(2, optionA);
            pstmt.setString(3, optionB);
            pstmt.setString(4, optionC);
            pstmt.setString(5, optionD);
            pstmt.setString(6, correctOption);
            pstmt.setString(7, difficulty);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Question added successfully!");
            clearFields();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding question.");
        }
    }

    protected void clearFields() {
        questionField.setText("");
        optionAField.setText("");
        optionBField.setText("");
        optionCField.setText("");
        optionDField.setText("");
        correctOptionBox.setSelectedIndex(0);
        difficultyBox.setSelectedIndex(0);
    }

    // Public method for testing purposes
    public void clearFieldsPublic() {
        clearFields();
    }

    // Getters for Testing
    public JTextField getQuestionField() { return questionField; }
    public JTextField getOptionAField() { return optionAField; }
    public JTextField getOptionBField() { return optionBField; }
    public JTextField getOptionCField() { return optionCField; }
    public JTextField getOptionDField() { return optionDField; }
    public JComboBox<String> getCorrectOptionBox() { return correctOptionBox; }
    public JComboBox<String> getDifficultyBox() { return difficultyBox; }
    public JButton getAddButton() { return addButton; }

    public static void main(String[] args) {
        new AdminQnsPage();
    }
}
