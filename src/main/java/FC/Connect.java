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
            executeCud(createTable);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete a card/row from a given deck/table
     */
    public void deleteCard(Card card, String table) {
        try {
            String removeCard = "DELETE FROM " + table + " WHERE id = " + card.getId() + ";";
            executeCud(removeCard);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update a given card's term
     */
    public void updateTerm(String table, Card card) {
        try {
            String updateTerm = "UPDATE " + table + "SET term = " + card.getTerm() + "WHERE id = " + card.getId() + ";";
            executeCud(updateTerm);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update a given card's def
     */
    public void updateDef(String table, Card card) {
        try {
            String updateDef = "UPDATE " + table + "SET def = " + card.getDef() + "WHERE id = " + card.getId() + ";";
            executeCud(updateDef);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete a table/deck in the database
     */
    public void deleteDeck(String table) {
        try {
            String deleteTable = "DROP TABLE " + table + ";";
            executeCud(deleteTable);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Add card to an existing table/deck
     */
    public void insertCard(String table, String term, String def) {
        try {
            String insertCard = "INSERT INTO " + table + "(term, def) VALUES (" + term + ", " + def + ");";
            executeCud(insertCard);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void executeCud(String cudStatement) throws SQLException {
        Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        stmt.execute(cudStatement);
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
            ResultSet results = this.connect().createStatement().executeQuery(sql);

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
