package FC;

import java.sql.*;

public class Connect {
    /**
     * est. connection to cards database
     */
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
    public Deck getDeck(String table) {
        Deck deck = new Deck();
        getDeckData(table, deck);
        return deck;
    }

    private void getDeckData(String table, Deck deck) {
        try {
            String sql = "SELECT * FROM " + table;
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql);

            while (results.next()) {
                addCardFromDB(deck, results);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addCardFromDB(Deck deck, ResultSet rs) throws SQLException {
        deck.addCard(new Card(rs.getString("id"),
                rs.getString("term"), rs.getString("def")));
    }
}
