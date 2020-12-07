package FC;

import java.sql.*;

public class Connect {
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:cards.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }


    /**
     * select all rows in the given deck's table
     */
    public Deck getDeck(String table){
        String sql = "SELECT * FROM " + table;
        Deck deck = new Deck();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
//                System.out.println(rs.getInt("id") +  "\t" +
//                        rs.getString("term") + "\t" +
//                        rs.getString("def"));
                Card card = new Card();

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return deck;
    }
}
