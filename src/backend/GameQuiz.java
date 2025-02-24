package backend;

import java.sql.*;

/**
 * Handles the quiz game logic and score management.
 */
public class GameQuiz {
    private int playerID;
    private int score;
    private String difficulty;
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Constructor for GameQuiz.
     * @param playerID The ID of the player.
     * @param score The score achieved.
     * @param difficulty The difficulty level.
     */
    public GameQuiz(int playerID, int score, String difficulty) {
        this.playerID = playerID;
        this.score = score;
        this.difficulty = difficulty;
    }

    /**
     * Saves the player's score and updates reports.
     */
    public void saveScore() {
        String insertScoreQuery = "INSERT INTO scores (player_id, score, difficulty, timestamp) VALUES (?, ?, ?, NOW())";
        String updateScoreQuery = "UPDATE scores SET score = ?, timestamp = NOW() WHERE player_id = ? AND difficulty = ?";
        String checkQuery = "SELECT highest_score FROM scores WHERE player_id = ? AND difficulty = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, playerID);
            checkStmt.setString(2, difficulty);
            ResultSet rs = checkStmt.executeQuery();
            boolean scoreExists = rs.next();

            if (scoreExists) {
                /**
                 * Update existing score
                 */
                try (PreparedStatement updateStmt = conn.prepareStatement(updateScoreQuery)) {
                    updateStmt.setInt(1, score);
                    updateStmt.setInt(2, playerID);
                    updateStmt.setString(3, difficulty);
                    updateStmt.executeUpdate();
                }
            } else {
                /**
                 *  Insert new score
                 */
                try (PreparedStatement insertStmt = conn.prepareStatement(insertScoreQuery)) {
                    insertStmt.setInt(1, playerID);
                    insertStmt.setInt(2, score);
                    insertStmt.setString(3, difficulty);
                    insertStmt.executeUpdate();
                }
            }

            /**
             *  Update the reports table
             */
            updateReports(playerID, score);

            System.out.println(" Score and report saved successfully!");

        } catch (SQLException e) {
            System.out.println(" Error saving score: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the reports table with total games played, highest score, and average score.
     * @param playerID The player's ID.
     * @param newScore The player's new score.
     */
    private void updateReports(int playerID, int newScore) {
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

    /**
     * Calculates the number of correct answers a player has given.
     * @param playerID The player's ID.
     * @param difficulty The difficulty level.
     * @return The number of correct answers.
     */
    private int calculateCorrectAnswers(int playerID, String difficulty) {
        String query = "SELECT COUNT(*) AS correct_count FROM player_answers WHERE player_id = ? AND difficulty = ? AND is_correct = 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, playerID);
            stmt.setString(2, difficulty);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("correct_count");
            }
        } catch (SQLException e) {
            System.out.println(" Error calculating correct answers: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
