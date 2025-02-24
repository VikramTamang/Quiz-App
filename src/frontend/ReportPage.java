package frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * Displays player reports and statistics.
 */
public class ReportPage extends JFrame {
    private JTable table;

    /**
     * Constructor for ReportPage.
     * Sets up the UI and loads report data.
     */
    public ReportPage() {
        setTitle("Player Reports and Statistics");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Player Reports and Statistics", SwingConstants.CENTER);
        titleLabel.setBackground(new Color(255, 183, 70));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        getContentPane().add(titleLabel, BorderLayout.NORTH);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        loadReports();  // Fetch reports from the database
    }

    /**
     * Loads player reports from the database.
     */
    private void loadReports() {
        String url = "jdbc:mysql://localhost:3306/quiz_app";
        String user = "root";
        String password = "";
        
        // FIX: Corrected column names to match reports table
        String query = "SELECT p.name, r.total_games_played, r.highest_score, r.average_score " +
                       "FROM reports r JOIN players p ON r.player_id = p.player_id " +
                       "ORDER BY r.highest_score DESC";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Player");
            model.addColumn("Total Games Played");
            model.addColumn("Highest Score");
            model.addColumn("Average Score");

            while (rs.next()) {
                String name = rs.getString("name");
                int gamesPlayed = rs.getInt("total_games_played");
                int highestScore = rs.getInt("highest_score");
                double averageScore = rs.getDouble("average_score");

                model.addRow(new Object[]{name, gamesPlayed, highestScore, averageScore});
            }

            table.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
