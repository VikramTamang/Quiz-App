package backend;

import java.sql.*;
import java.util.Scanner;

/**
 * Handles admin functionalities such as viewing and managing players and scores.
 */
public class TheAdmin {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n Admin Panel:");
            System.out.println("1. View All Players");
            System.out.println("2. View All Scores");
            System.out.println("3. View Top Player");
            System.out.println("4. Search Player by ID");
            System.out.println("5. Delete a Player");
            System.out.println("6. Reset All Scores");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> viewAllPlayers();
                case 2 -> viewAllScores();
                case 3 -> viewTopPlayer();
                case 4 -> searchPlayerByID(scanner);
                case 5 -> deletePlayer(scanner);
                case 6 -> resetAllScores();
                case 7 -> {
                    System.out.println(" Exiting Admin Panel...");
                    scanner.close();
                    return;
                }
                default -> System.out.println(" Invalid choice, try again.");
            }
        }
    }

    /**
     * Establishes a database connection.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Displays all registered players.
     */
    public static void viewAllPlayers() {
        String query = "SELECT player_id, name, email FROM players";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n Registered Players:");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Email: %s%n",
                        rs.getInt("player_id"), rs.getString("name"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println(" Error fetching players.");
            e.printStackTrace();
        }
    }

    /**
     * Displays all scores.
     */
    public static void viewAllScores() {
        String query = "SELECT p.name, s.score, s.difficulty, s.timestamp " +
                       "FROM scores s JOIN players p ON s.player_id = p.player_id " +
                       "ORDER BY s.timestamp DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n All Scores:");
            while (rs.next()) {
                System.out.printf("Player: %s | Score: %d | Level: %s | Date: %s%n",
                        rs.getString("name"), rs.getInt("score"),
                        rs.getString("difficulty"), rs.getTimestamp("timestamp"));
            }
        } catch (SQLException e) {
            System.out.println(" Error fetching scores.");
            e.printStackTrace();
        }
    }

    /**
     * Displays the top player.
     */
    public static void viewTopPlayer() {
        String query = "SELECT p.name, MAX(s.score) AS highest_score " +
                       "FROM scores s JOIN players p ON s.player_id = p.player_id " +
                       "GROUP BY s.player_id ORDER BY highest_score DESC LIMIT 1";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                System.out.printf("\n Top Player: %s | Highest Score: %d%n",
                        rs.getString("name"), rs.getInt("highest_score"));
            } else {
                System.out.println(" No scores available.");
            }
        } catch (SQLException e) {
            System.out.println(" Error fetching top player.");
            e.printStackTrace();
        }
    }

    /**
     * Searches for a player by ID.
     */
    public static void searchPlayerByID(Scanner scanner) {
        System.out.print(" Enter Player ID: ");
        int playerID = scanner.nextInt();
        String query = "SELECT name, email FROM players WHERE player_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, playerID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.printf("\n Player Found: Name: %s | Email: %s%n",
                        rs.getString("name"), rs.getString("email"));
            } else {
                System.out.println(" Player not found.");
            }
        } catch (SQLException e) {
            System.out.println(" Error searching player.");
            e.printStackTrace();
        }
    }

    /**
     * Deletes a player and all related data.
     */
    public static void deletePlayer(Scanner scanner) {
        System.out.print(" Enter the Player ID to DELETE: ");
        int playerID = scanner.nextInt();

        String deleteQuery = "DELETE FROM players WHERE player_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setInt(1, playerID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println(" Player with ID " + playerID + " deleted successfully.");
            } else {
                System.out.println(" No player found with ID " + playerID);
            }

        } catch (SQLException e) {
            System.out.println(" Error deleting player: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Resets all player scores.
     */
    public static void resetAllScores() {
        String resetQuery = "DELETE FROM scores";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            int affectedRows = stmt.executeUpdate(resetQuery);
            System.out.println(" All scores have been reset. " + affectedRows + " records deleted.");

        } catch (SQLException e) {
            System.out.println(" Error resetting scores: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
