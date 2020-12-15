package controller_flashcards;

import flashcards.Card;
import flashcards.Deck;

import java.sql.*;
import java.util.ArrayList;

public class Controller {

    private final String MENU_TABLE = "menu";
    private final String DB_FILE = "flash_cards.db";
    private final Connection CONNECTION = new Connector(DB_FILE).connect();

    public Controller() throws SQLException {
    }

    /**
     * Create a new table/deck in the database
     *
     * @param title
     * @throws SQLException
     */
    protected void addDeck(String title) throws SQLException {
        PreparedStatement createTableStmt = CONNECTION.prepareStatement("CREATE TABLE '" + escapeApostrophes(title) +
                        "' (id INTEGER PRIMARY KEY AUTOINCREMENT, term TEXT NOT NULL, def TEXT NOT NULL);");
        createTableStmt.execute();
        addDeckToMenuTable(title);
    }

    /**
     * Adds new deck name to the menu that supplies UI with deck list
     *
     * @param title
     * @throws SQLException
     */
    private void addDeckToMenuTable(String title) throws SQLException {
        PreparedStatement insertToMenuStmt = CONNECTION.prepareStatement("INSERT INTO '" + escapeApostrophes(MENU_TABLE)
                + "' (deck_title) VALUES (?);");
        insertToMenuStmt.setString(1, title);
        insertToMenuStmt.execute();
    }

    /**
     * Delete a card/row from a given deck/table
     *
     * @param card
     * @param table
     * @throws SQLException
     */
    protected void deleteCard(Card card, String table) throws SQLException {
        PreparedStatement removeCardStmt = CONNECTION.prepareStatement("DELETE FROM '" + escapeApostrophes(table) + "' WHERE id = ?;");
        removeCardStmt.setString(1, card.getId());
        removeCardStmt.execute();
    }


    /**
     * Update a given card's term
     */
    protected void updateTerm(String table, Card card) throws SQLException {
        PreparedStatement updateTermStmt = CONNECTION.prepareStatement("UPDATE '" + escapeApostrophes(table) +
                "' SET term = ? WHERE id = ?;");
        updateTermStmt.setString(1, card.getTerm());
        updateTermStmt.setString(2, card.getId());
        updateTermStmt.execute();
    }

    private String escapeApostrophes(String table) {
        return table.replaceAll("'", "''");
    }

    /**
     * Update a given card's def
     */
    protected void updateDef(String table, Card card) throws SQLException {
        PreparedStatement updateDefStmt = CONNECTION.prepareStatement("UPDATE '" + escapeApostrophes(table) + "' SET def = ? WHERE id = ?;");
        updateDefStmt.setString(1, card.getDef());
        updateDefStmt.setString(2, card.getId());
    }

    /**
     * Delete a table/deck in the database
     */
    protected void deleteDeck(String table) throws SQLException {
        PreparedStatement deleteDeckStmt = CONNECTION.prepareStatement("DROP TABLE '" + escapeApostrophes(table) + "';");
        deleteDeckStmt.execute();
        removeFromMenu(table);
    }

    /**
     * Removes deck name from the menu that supplies UI with deck list
     *
     * @param table
     * @throws SQLException
     */
    protected void removeFromMenu(String table) throws SQLException {
        PreparedStatement deleteMenuItemStmt = CONNECTION.prepareStatement("DELETE FROM '" + escapeApostrophes(MENU_TABLE) +
                "' WHERE deck_title = ?;");
        deleteMenuItemStmt.setString(1, table);
        deleteMenuItemStmt.execute();
    }

    /**
     * Add card to an existing table/deck
     *
     * @param table
     * @param term
     * @param def
     * @throws SQLException
     */
    protected void insertCard(String table, String term, String def) throws SQLException {
        PreparedStatement insertCardStmt = CONNECTION.prepareStatement("INSERT INTO '" + escapeApostrophes(table) +
                "' (term, def) VALUES (?, ?);");
        insertCardStmt.setString(1, term);
        insertCardStmt.setString(2, def);
        insertCardStmt.execute();
    }

    /**
     * @return ArrayList of all decks in the db
     */
    protected ArrayList<String> getAllDecks() throws SQLException {
        ResultSet results = selectAll(MENU_TABLE);
        ArrayList<String> deckList = new ArrayList<>();
        while (results.next()) {
            deckList.add(results.getString("deck_title"));
        }
        return deckList;
    }

    /**
     * Retrieves all the data from a given table
     *
     * @param table
     * @return
     * @throws SQLException
     */
    protected ResultSet selectAll(String table) throws SQLException {
        PreparedStatement selectStmt = CONNECTION.prepareStatement("SELECT * FROM '" + escapeApostrophes(table) + "';");
        return selectStmt.executeQuery();
    }

    /**
     * populate a deck object using given deck's table
     *
     * @param table
     * @return
     * @throws SQLException
     */
    protected Deck getDeck(String table) throws SQLException {
        Deck deck = new Deck();
        writeDataToDeck(table, deck);
        return deck;
    }

    /**
     * Writes query results to a Deck object
     *
     * @param table
     * @param deck
     * @throws SQLException
     */
    private void writeDataToDeck(String table, Deck deck) throws SQLException {
        ResultSet results = selectAll(table);
        while (results.next()) {
            addCardFromDB(deck, results);
        }
    }

    /**
     * Writes query results to Card objects
     *
     * @param deck
     * @param rs
     * @throws SQLException
     */
    private void addCardFromDB(Deck deck, ResultSet rs) throws SQLException {
        deck.addCard(new Card(rs.getString("id"),
                rs.getString("term"), rs.getString("def")));
    }
}