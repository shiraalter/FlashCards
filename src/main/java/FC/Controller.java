package FC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {

    private final String menuTable = "menu";
    private final String dBFile = "flash_cards.db";
    private final Connection connection = new Connector(dBFile).connect();
    private Deck unMastered;
    private Deck mastered;
    private Deck editDeck;
    private final Random rand = new Random();

    public Controller() throws SQLException {
    }

//    /**
//     * Creates a new card in the deck
//     */
//    public void createCardInDeck(String term, String def, String deckName) throws SQLException {
//        editDeck = getDeck(deckName);
//        Card card = new Card();
//        insertCard(deckName, term, def);
//        editDeck.addCard(card);
//    }
//
//    /**
//     * Deletes the selected card from the deck
//     */
//    public void deleteCardFromDeck(Card card, String deckName) throws SQLException {
//        editDeck = getDeck(deckName);
//        deleteCard(editDeck.getCard(), deckName);
//        editDeck.removeCard(card);
//    }


    /**
     * Returns the next card to study
     */
    public Card getNextToStudy() {
        return unMastered.getCard(rand.nextInt(unMastered.getSize()) - 1);
    }

    /**
     * Moves card from unMastered to mastered
     */
    public void masterCard(Card card) {
        unMastered.removeCard(card);
        mastered.addCard(card);
    }

    /**
     * Loads new deck to unMastered and clears mastered
     */
    public void startNewStudySession(String deckName) throws SQLException {
        unMastered = getDeck(deckName);
        mastered.clear();
    }

    /**
     * Create a new table/deck in the database
     */
    private void addDeck(String title) throws SQLException {
        String createTableStmt = "CREATE TABLE " + title + " (term TEXT NOT NULL, def TEXT NOT NULL);";
        executeCUD(createTableStmt);
        addDeckToMenuTable(title);
    }

    private void addDeckToMenuTable(String title) throws SQLException {
        String insertToMenuStmt = "INSERT INTO " + menuTable + "VALUES (" + title + ");";
        executeCUD(insertToMenuStmt);
    }

    /**
     * Delete a card/row from a given deck/table
     */
    //combined delete methods
    public void deleteCard(Card card, String table) throws SQLException {
        editDeck = getDeck(table);
        String removeCardStmt = "DELETE FROM " + table + " WHERE rowid = " + card.getId() + ";";
        executeCUD(removeCardStmt);
        //make sure this works
        editDeck.removeCard(card);
    }

    /**
     * Update a given card's term
     */
    private void updateTerm(String table, Card card) throws SQLException {
        String updateTermStmt = "UPDATE " + table + "SET term = " + card.getTerm() + "WHERE rowid = " + card.getId() + ";";
        executeCUD(updateTermStmt);
    }

    /**
     * Update a given card's def
     */
    private void updateDef(String table, Card card) throws SQLException {
        String updateDefStmt = "UPDATE " + table + "SET def = " + card.getDef() + "WHERE rowid = " + card.getId() + ";";
        executeCUD(updateDefStmt);
    }

    /**
     * Delete a table/deck in the database
     */
    private void deleteDeck(String table) throws SQLException {
        String deleteDeckStmt = "DROP TABLE " + table + ";";
        executeCUD(deleteDeckStmt);
        removeFromMenu(table);
    }

    private void removeFromMenu(String table) throws SQLException {
        String deleteMenuItemStmt = "DELETE FROM " + menuTable + " WHERE deck_title = " + table + ";";
        executeCUD(deleteMenuItemStmt);
    }

    /**
     * Add card to an existing table/deck
     */
    //combined both and made public
    public void insertCard(String table, String term, String def) throws SQLException {
        editDeck = getDeck(table);
        String insertCardStmt = "INSERT INTO " + table + "VALUES (" + term + ", " + def + ");";
        executeCUD(insertCardStmt);
        //fix what id is
        String id = "";
        Card card = new Card(id, term, def);
        editDeck.addCard(card);
    }

    private void executeCUD(String cudStatement) throws SQLException {
        connection.createStatement().execute(cudStatement);
    }

    /**
     * @return Array of all decks in the db
     */
    private String[] getAllDecks() throws SQLException {
        List<String> deckList = new ArrayList<>();
        ResultSet results = selectAll(menuTable);

        while (results.next()) {
            deckList.add(results.getString("deck_title"));
        }

        return (String[]) deckList.toArray();
    }

    private ResultSet selectAll(String table) throws SQLException {
        String selectStmt = "SELECT * FROM " + table;
        return connection.createStatement().executeQuery(selectStmt);
    }

    /**
     * populate a deck object using given deck's table
     */
    private Deck getDeck(String table) throws SQLException {
        Deck deck = new Deck();
        getDeckData(table, deck);
        return deck;
    }

    private void getDeckData(String table, Deck deck) throws SQLException {
        ResultSet results = selectAll(table);

        while (results.next()) {
            addCardFromDB(deck, results);
        }
    }

    private void addCardFromDB(Deck deck, ResultSet rs) throws SQLException {
        deck.addCard(new Card(rs.getString("row id"),
                rs.getString("term"), rs.getString("def")));
    }

}
