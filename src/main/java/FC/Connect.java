package FC;

import java.sql.*;

public class Connect {
    /**
     * est. connection to cards database
     */
    private Connection connect() {
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
     * Create a new table/deck in the database
     */
    public void addDeck(String title) {
        try {
            String createTable = "CREATE TABLE " + title + " (id STRING NOT NULL, term STRING NOT NULL, def STRING NOT NULL);";
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(createTable);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Delete a table/deck in the database
     */
    public void deleteDeck(String table){
        try {
            String deleteTable = "DROP TABLE " + table +";";
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(deleteTable);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Add card to an existing table/deck
     */
    public void insertCard(String table, String id, String term, String def){
        try {
            String insertCard = "INSERT INTO " + table + " VALUES (" + id +", " + term + ", " + def + ");";
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(insertCard);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
