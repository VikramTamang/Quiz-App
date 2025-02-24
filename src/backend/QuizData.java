package backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles quiz questions retrieval.
 */
public class QuizData {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Fetches 10 random questions based on difficulty level.
     *
     * @param difficulty The quiz difficulty level.
     * @param limit Number of questions to retrieve.
     * @return A list of questions.
     */
    public static List<Question> getRandomQuestions(String difficulty, int limit) {
        List<Question> questions = new ArrayList<>();

        String query = "SELECT question_id, question_text, option_a, option_b, option_c, option_d, correct_option " +
                       "FROM questions WHERE difficulty = ? ORDER BY RAND() LIMIT ?";  //  Randomized Fetch

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, difficulty);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                questions.add(new Question(
                        rs.getInt("question_id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_option").charAt(0)
                ));
            }

            if (questions.isEmpty()) {
                System.out.println(" Warning: No questions found for difficulty level: " + difficulty);
            }

        } catch (SQLException e) {
            System.out.println(" Database Error: Could not retrieve questions.");
            e.printStackTrace();
        }
        return questions;
    }

    /**
     * Represents a quiz question.
     */
    public static class Question {
        public int id;
        public String text, optionA, optionB, optionC, optionD;
        public char correctOption;

        public Question(int id, String text, String a, String b, String c, String d, char correct) {
            this.id = id;
            this.text = text;
            this.optionA = a;
            this.optionB = b;
            this.optionC = c;
            this.optionD = d;
            this.correctOption = correct;
        }
    }
}
