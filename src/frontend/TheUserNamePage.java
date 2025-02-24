package frontend;

import javax.swing.*;
import backend.ManagerPlayer;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Handles user registration or login.
 */
public class TheUserNamePage extends JFrame {
    private JTextField nameField, emailField;

    public TheUserNamePage() {
        getContentPane().setBackground(new Color(255, 183, 100));
        setTitle("Enter Your Details");
        setSize(458, 364);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        JLabel label = new JLabel("Name:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.BOLD, 16));
        label.setBounds(0, 0, 193, 71);
        getContentPane().add(label);
        nameField = new JTextField();
        nameField.setBounds(193, 11, 193, 50);
        getContentPane().add(nameField);

        JLabel label_1 = new JLabel("Email:");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setFont(new Font("Tahoma", Font.BOLD, 16));
        label_1.setBounds(0, 81, 193, 71);
        getContentPane().add(label_1);
        emailField = new JTextField();
        emailField.setBounds(193, 92, 193, 50);
        getContentPane().add(emailField);

        JButton nextBtn = new JButton("Next");
        nextBtn.setBackground(new Color(232, 133, 69));
        nextBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
        nextBtn.setBounds(146, 239, 168, 50);
        nextBtn.addActionListener(e -> registerPlayer());
        getContentPane().add(nextBtn);
    }

    private void registerPlayer() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Name and Email cannot be empty!");
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "❌ Enter a valid email address!");
            return;
        }

        int playerID = ManagerPlayer.registerOrRetrievePlayer(name, email, "default");
        if (playerID > 0) {
            new TheDifficultyPage(playerID).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Registration failed. Try again.");
        }
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matcher(email).matches();
    }
}
