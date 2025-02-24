package backend;

import java.sql.*;

/**
 * Manages player scores and updates reports.
 */
public class ScoreManager {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Saves the player's score and updates the reports table.
     *
     * @param playerID  The ID of the player.
     * @param newScore  The score achieved.
     * @param difficulty The difficulty level.
     */
    public static void saveScore(int playerID, int newScore, String difficulty) {
        String insertScoreQuery = "INSERT INTO scores (player_id, score, difficulty, timestamp) VALUES (?, ?, ?, NOW())";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement insertStmt = conn.prepareStatement(insertScoreQuery)) {

            insertStmt.setInt(1, playerID);
            insertStmt.setInt(2, newScore);
            insertStmt.setString(3, difficulty);
            insertStmt.executeUpdate();

            // ✅ Update Reports Table
            updateReports(playerID, newScore);

            System.out.println(" Score and report updated successfully!");

        } catch (SQLException e) {
            System.out.println(" Error saving score: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the reports table with total games played, highest score, and average score.
     *
     * @param playerID The ID of the player.
     * @param newScore The new score achieved.
     */
    private static void updateReports(int playerID, int newScore) {
        String updateReportQuery = "INSERT INTO reports (player_id, total_games_played, highest_score, average_score) " +
                                   "VALUES (?, 1, ?, ?) ON DUPLICATE KEY UPDATE " +
                                   "total_games_played = total_games_played + 1, " +
                                   "highest_score = GREATEST(highest_score, VALUES(highest_score)), " +
                                   "average_score = (average_score * (total_games_played - 1) + VALUES(average_score)) / total_games_played";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement reportStmt = conn.prepareStatement(updateReportQuery)) {

            reportStmt.setInt(1, playerID);
            reportStmt.setInt(2, newScore);
            reportStmt.setDouble(3, newScore);
            reportStmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(" Error updating reports: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
