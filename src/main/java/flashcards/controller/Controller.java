package flashcards.controller;

import flashcards.Card;
import flashcards.Deck;

import java.sql.*;
import java.util.ArrayList;

public class Controller {

    private final String MENU_TABLE = "menu";
    private final String DB_FILE = "flash_cards.db";
    private final Connection CONNECTION = new Connector(DB_FILE).connect();
    private final PreparedStatement createTableStmt;
    private final PreparedStatement insertToMenuStmt;
    private final PreparedStatement removeCardStmt;
    private final PreparedStatement updateTermStmt;
    private final PreparedStatement updateDefStmt;
    private final PreparedStatement deleteDeckStmt;
    private final PreparedStatement deleteMenuItemStmt;
    private final PreparedStatement insertCardStmt;
    private final PreparedStatement selectStmt;

    public Controller() throws SQLException {
        createTableStmt = CONNECTION.prepareStatement("CREATE TABLE '" +  "?"  +
                "' (id INTEGER PRIMARY KEY AUTOINCREMENT, term TEXT NOT NULL, def TEXT NOT NULL);");
        insertToMenuStmt = CONNECTION.prepareStatement("INSERT INTO '" +  "?"  + "' (deck_title) VALUES (?);");
        removeCardStmt = CONNECTION.prepareStatement("DELETE FROM '" +  "?"  + "' WHERE id = ?;");
        updateTermStmt = CONNECTION.prepareStatement("UPDATE '" + "?" + "' SET term = ? WHERE id = ?;");
        updateDefStmt = CONNECTION.prepareStatement("UPDATE '" + "?" + "' SET def = ? WHERE id = ?;");
        deleteDeckStmt = CONNECTION.prepareStatement("DROP TABLE '" + "?" + "';");
        deleteMenuItemStmt = CONNECTION.prepareStatement("DELETE FROM '" + "?" + "' WHERE deck_title = ?;");
        insertCardStmt = CONNECTION.prepareStatement("INSERT INTO '" + "?" + "' (term, def) VALUES (?, ?);");
        selectStmt = CONNECTION.prepareStatement("SELECT * FROM '" + "?" + "';");
    }

    /**
     * Create a new table/deck in the database
     *
     * @param title
     * @throws SQLException
     */
    protected void addDeck(String title) throws SQLException {
        createTableStmt.setString(1, escapeApostrophes(title));
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
        insertToMenuStmt.setString(1, escapeApostrophes(MENU_TABLE));
        insertToMenuStmt.setString(2, title);
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
        removeCardStmt.setString(1, escapeApostrophes(table));
        removeCardStmt.setString(2, card.getId());
        removeCardStmt.execute();
    }

    /**
     * Update a given card's term
     */
    protected void updateTerm(String table, Card card) throws SQLException {
        updateTermStmt.setString(1, escapeApostrophes(table));
        updateTermStmt.setString(2, card.getTerm());
        updateTermStmt.setString(3, card.getId());
        updateTermStmt.execute();
    }

    private String escapeApostrophes(String table) {
        return table.replaceAll("'", "''");
    }

    /**
     * Update a given card's def
     */
    protected void updateDef(String table, Card card) throws SQLException {
        updateDefStmt.setString(1, escapeApostrophes(table));
        updateDefStmt.setString(2, card.getDef());
        updateDefStmt.setString(3, card.getId());
    }

    /**
     * Delete a table/deck in the database
     */
    protected void deleteDeck(String table) throws SQLException {
        deleteDeckStmt.setString(1, escapeApostrophes(table));
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
        deleteMenuItemStmt.setString(1, escapeApostrophes(MENU_TABLE));
        deleteMenuItemStmt.setString(2, table);
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
        insertCardStmt.setString(1, escapeApostrophes(table));
        insertCardStmt.setString(2, term);
        insertCardStmt.setString(3, def);
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
        selectStmt.setString(1, escapeApostrophes(table));
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