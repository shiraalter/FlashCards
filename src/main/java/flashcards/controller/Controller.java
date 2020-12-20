package flashcards.controller;

import flashcards.Card;
import flashcards.Deck;

import java.sql.*;
import java.util.ArrayList;

public class Controller {

    private final String MENU_TABLE = "menu";
    private final String CARDS_TABLE = "cards";
    private final String DB_FILE = "flash_cards.db";
    private final Connection CONNECTION = new Connector(DB_FILE).connect();

    public Controller() throws SQLException { }

    /**
     * Adds new deck name to the menu that supplies UI with deck list
     *
     * @param title
     * @throws SQLException
     */
    public void addDeck(String title) throws SQLException {
        PreparedStatement insertToMenuStmt = CONNECTION.prepareStatement("INSERT INTO '" + escapeApostrophes(MENU_TABLE)
                + "' (deck_title) VALUES (?);");
        insertToMenuStmt.setString(1, title);
        insertToMenuStmt.execute();
    }

    /**
     * Delete a card from a given deck
     *
     * @param card
     * @throws SQLException
     */
    protected void deleteCard(Card card) throws SQLException {
        PreparedStatement removeCardStmt = CONNECTION.prepareStatement("DELETE FROM '" + escapeApostrophes(CARDS_TABLE) +
                "' WHERE card_id = ?;");
        removeCardStmt.setString(1, card.getId());
        removeCardStmt.execute();
    }
    
//    /**
//     * Update a given card's term
//     */
//    protected void updateTerm(String table, Card card) throws SQLException {
//        PreparedStatement updateTermStmt = CONNECTION.prepareStatement("UPDATE '" + escapeApostrophes(table) +
//                "' SET term = ? WHERE id = ?;");
//        updateTermStmt.setString(1, card.getTerm());
//        updateTermStmt.setString(2, card.getId());
//        updateTermStmt.execute();
//    }

//    /**
//     * Update a given card's def
//     */
//    protected void updateDef(String table, Card card) throws SQLException {
//        PreparedStatement updateDefStmt = CONNECTION.prepareStatement("UPDATE '" + escapeApostrophes(table) + "' SET def = ? WHERE id = ?;");
//        updateDefStmt.setString(1, card.getDef());
//        updateDefStmt.setString(2, card.getId());
//    }

    /**
     * Delete a table/deck in the database
     */
    protected void deleteDeck(String deck) throws SQLException {
        PreparedStatement deleteDeckStmt = CONNECTION.prepareStatement("DELETE from '" + escapeApostrophes(CARDS_TABLE) +
                "'Where deck_id = (select id from menu where deck_title = ?);");
        deleteDeckStmt.setString(1, deck);
        deleteDeckStmt.execute();
        removeFromMenu(deck);
    }

    /**
     * Removes deck name from the menu that supplies UI with deck list
     *
     * @param deck
     * @throws SQLException
     */
    protected void removeFromMenu(String deck) throws SQLException {
        PreparedStatement deleteMenuItemStmt = CONNECTION.prepareStatement("DELETE FROM '" + escapeApostrophes(MENU_TABLE) +
                "' WHERE deck_title = ?;");
        deleteMenuItemStmt.setString(1, deck);
        deleteMenuItemStmt.execute();
    }

    /**
     * Add card to an existing table/deck
     *
     * @param deck
     * @param term
     * @param def
     * @throws SQLException
     */
    protected void insertCard(String deck, String term, String def) throws SQLException {
        PreparedStatement insertCardStmt = CONNECTION.prepareStatement("INSERT INTO '" + escapeApostrophes(CARDS_TABLE) +
                "' (term, def, deck_id) VALUES (?, ?, (select id from menu where deck_title = ?));");
        insertCardStmt.setString(1, term);
        insertCardStmt.setString(2, def);
        insertCardStmt.setString(3, deck);
        insertCardStmt.execute();
    }

    /**
     * @return ArrayList of all decks in the db
     */
    protected ArrayList<String> getAllDecks() throws SQLException {
        ResultSet results = getAllDecksFromDB();
        ArrayList<String> deckList = new ArrayList<>();
        while (results.next()) {
            deckList.add(results.getString("deck_title"));
        }
        return deckList;
    }

    /**
     * Retrieves all the data from a given table
     *
     * @return ResultSet
     * @throws SQLException
     */
    protected ResultSet getAllDecksFromDB() throws SQLException {
        PreparedStatement selectStmt = CONNECTION.prepareStatement("SELECT deck_title FROM '" + escapeApostrophes(MENU_TABLE) + "';");
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
     * @param deckToGet
     * @param deckToGet
     * @throws SQLException
     */
    private void writeDataToDeck(String deckToGet, Deck deckToReturn) throws SQLException {
        ResultSet results = getAllCardsInDeck(deckToGet);
        while (results.next()) {
            addCardFromDB(deckToReturn, results);
        }
    }

    private ResultSet getAllCardsInDeck(String deckToGet) throws SQLException {
        PreparedStatement selectStmt = CONNECTION.prepareStatement("SELECT * FROM '" + escapeApostrophes(CARDS_TABLE) +
                "'Where deck_id = (select id from menu where deck_title = ?);");
        selectStmt.setString(1, deckToGet);
        return selectStmt.executeQuery();
    }

    /**
     * Writes query results to Card objects
     *
     * @param deck
     * @param rs
     * @throws SQLException
     */
    private void addCardFromDB(Deck deck, ResultSet rs) throws SQLException {
        deck.addCard(new Card(rs.getString("card_id"),
                rs.getString("term"), rs.getString("def")));
    }


    private String escapeApostrophes(String table) {
        return table.replaceAll("'", "''");
    }
}