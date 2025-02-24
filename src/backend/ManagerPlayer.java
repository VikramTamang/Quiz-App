package backend;

import java.sql.*;
import java.util.Scanner;

/**
 * Manages player registration, statistics, and reports.
 */
public class ManagerPlayer {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n1. Register Player");
            System.out.println("2. Generate Full Report");
            System.out.println("3. Display Top Performer");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    int playerID = registerOrRetrievePlayer(name, email, password);
                    if (playerID > 0) {
                        System.out.println(" Player logged in with ID: " + playerID);
                    }
                    break;
                case 2:
                    generateFullReport();
                    break;
                case 3:
                    displayTopPerformer();
                    break;
                case 4:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println(" Invalid choice, try again.");
            }
        }
        scanner.close();
    }

    /**
     * Registers a new player or retrieves an existing one.
     * @param name Player's name.
     * @param email Player's email.
     * @param password Player's password.
     * @return Player ID.
     */
    public static int registerOrRetrievePlayer(String name, String email, String password) {
        int playerID = getPlayerIDByEmail(email);
        if (playerID > 0) {
            System.out.println(" Welcome back, " + name + "!");
            return playerID;
        }

        String query = "INSERT INTO players (name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(" Error registering player: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Retrieves a player's ID using their email.
     * @param email Player's email.
     * @return Player ID.
     */
    private static int getPlayerIDByEmail(String email) {
        String query = "SELECT player_id FROM players WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("player_id");
            }
        } catch (SQLException e) {
            System.out.println(" Error retrieving player ID: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Generates a full report of all players' statistics.
     */
    public static void generateFullReport() {
        String query = "SELECT p.name, r.total_games_played, r.highest_score, r.average_score " +
                       "FROM reports r JOIN players p ON r.player_id = p.player_id " +
                       "ORDER BY r.highest_score DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n Player Reports:");
            System.out.println("---------------------------------------------------");
            while (rs.next()) {
                System.out.println("Name: " + rs.getString("name") +
                                   " | Games Played: " + rs.getInt("total_games_played") +
                                   " | Highest Score: " + rs.getInt("highest_score") +
                                   " | Average Score: " + rs.getDouble("average_score"));
            }
        } catch (SQLException e) {
            System.out.println(" Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the top-performing player.
     */
    public static void displayTopPerformer() {
        String query = "SELECT p.name, r.highest_score FROM reports r " +
                       "JOIN players p ON r.player_id = p.player_id " +
                       "ORDER BY r.highest_score DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                System.out.println("\n Top Performer: " + rs.getString("name") +
                                   " | Highest Score: " + rs.getInt("highest_score"));
            } else {
                System.out.println(" No top performer found.");
            }
        } catch (SQLException e) {
            System.out.println(" Error retrieving top performer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
