package FC;

import java.sql.*;

public class Connect {
    private final String menuTable = "menu";
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
            String createTableStmt = "CREATE TABLE " + title + " (term STRING NOT NULL, def STRING NOT NULL);";
            executeCud(createTableStmt);
            addDeckToMenuTable(title);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addDeckToMenuTable(String title) throws SQLException {
        String insertToMenuStmt = "INSERT INTO " + menuTable + "VALUES (" + title +");";
        executeCud(insertToMenuStmt);
    }

    /**
     * Delete a card/row from a given deck/table
     */
    public void deleteCard(Card card, String table) {
        try {
            String removeCardStmt = "DELETE FROM " + table + " WHERE rowid = " + card.getId() + ";";
            executeCud(removeCardStmt);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update a given card's term
     */
    public void updateTerm(String table, Card card) {
        try {
            String updateTermStmt = "UPDATE " + table + "SET term = " + card.getTerm() + "WHERE rowid = " + card.getId() + ";";
            executeCud(updateTermStmt);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update a given card's def
     */
    public void updateDef(String table, Card card) {
        try {
            String updateDefStmt = "UPDATE " + table + "SET def = " + card.getDef() + "WHERE rowid = " + card.getId() + ";";
            executeCud(updateDefStmt);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete a table/deck in the database
     */
    public void deleteDeck(String table) {
        try {
            String deleteDeckStmt = "DROP TABLE " + table + ";";
            executeCud(deleteDeckStmt);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Add card to an existing table/deck
     */
    public void insertCard(String table, String term, String def) {
        try {
            String insertCardStmt = "INSERT INTO " + table + "VALUES (" + term + ", " + def + ");";
            executeCud(insertCardStmt);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void executeCud(String cudStatement) throws SQLException {
        this.connect().createStatement().execute(cudStatement);
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
            String selectStmt = "SELECT * FROM " + table;
            ResultSet results = this.connect().createStatement().executeQuery(selectStmt);

            while (results.next()) {
                addCardFromDB(deck, results);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addCardFromDB(Deck deck, ResultSet rs) throws SQLException {
        deck.addCard(new Card(rs.getString("rowid"),
                rs.getString("term"), rs.getString("def")));
    }
}
