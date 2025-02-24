package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database setup for the quiz application.
 */
public class DatabaseSetup {
    public static void main(String[] args) {
        String databaseName = "quiz_app";
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Create database if it does not exist
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + databaseName);
            System.out.println(" Database 'quiz_app' ensured to exist.");

        } catch (SQLException e) {
            System.out.println(" Error creating database: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Now, reconnect using the created database
        String newUrl = "jdbc:mysql://localhost:3306/" + databaseName;

        try (Connection conn = DriverManager.getConnection(newUrl, user, password);
             Statement stmt = conn.createStatement()) {

            // Create Players Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS players (" +
                    "player_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL)");

            // Create Scores Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS scores (" +
                    "score_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "player_id INT, " +
                    "score INT NOT NULL, " +
                    "difficulty ENUM('Beginner', 'Intermediate', 'Advanced') NOT NULL, " +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE)");

            // ✅ FIXED: Ensure `highest_score` Column Exists in `reports` Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS reports (" +
                    "report_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "player_id INT, " +
                    "total_games_played INT DEFAULT 0, " +
                    "highest_score INT DEFAULT 0, " +
                    "average_score FLOAT DEFAULT 0, " +
                    "FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE)");
            
         // ✅ FIXED: Create Questions Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS questions (" +
                    "question_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "question_text TEXT NOT NULL, " +
                    "option_a VARCHAR(255) NOT NULL, " +
                    "option_b VARCHAR(255) NOT NULL, " +
                    "option_c VARCHAR(255) NOT NULL, " +
                    "option_d VARCHAR(255) NOT NULL, " +
                    "correct_option CHAR(1) NOT NULL CHECK (correct_option IN ('A', 'B', 'C', 'D')), " +
                    "difficulty ENUM('Beginner', 'Intermediate', 'Advanced') NOT NULL)");

            System.out.println(" Database and tables set up successfully!");

        } catch (SQLException e) {
            System.out.println(" Error setting up tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
