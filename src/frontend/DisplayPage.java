package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * Displays quiz results and player activity frequency.
 */
public class DisplayPage extends JFrame {
    private JTable table;

    public DisplayPage() {
        setTitle("Quiz Results and Player Frequency");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Quiz Results and Player Frequency", SwingConstants.CENTER);
        titleLabel.setBackground(new Color(255, 183, 70));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        getContentPane().add(titleLabel, BorderLayout.NORTH);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        loadScoresAndFrequency();
    }

    /**
     * Loads quiz scores and player frequency from the database.
     */
    private void loadScoresAndFrequency() {
        String url = "jdbc:mysql://localhost:3306/quiz_app";
        String user = "root";
        String password = "";

        String query = "SELECT p.name, MAX(s.score) AS max_score, MAX(s.timestamp) AS latest_date, " +
                       "COUNT(s.player_id) AS play_count, " +
                       "(SELECT s1.difficulty FROM scores s1 WHERE s1.player_id = p.player_id ORDER BY s1.timestamp DESC LIMIT 1) AS last_difficulty " +
                       "FROM scores s JOIN players p ON s.player_id = p.player_id " +
                       "GROUP BY p.name, p.player_id ORDER BY play_count DESC";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Player");
            model.addColumn("Highest Score");
            model.addColumn("Latest Difficulty");
            model.addColumn("Latest Attempt");
            model.addColumn("Times Played");

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                String name = rs.getString("name");
                int maxScore = rs.getInt("max_score");
                String difficulty = rs.getString("last_difficulty");
                Timestamp latestDate = rs.getTimestamp("latest_date");
                int playCount = rs.getInt("play_count");

                model.addRow(new Object[]{name, maxScore, difficulty, latestDate, playCount});
            }

            if (!hasData) {
                JOptionPane.showMessageDialog(this, "No quiz data available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

            table.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
