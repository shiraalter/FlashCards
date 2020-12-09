package FC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final String MENU_TABLE = "menu";
    private final String DB_FILE = "flash_cards.db";
    private final Connection CONNECTION = new Connector(DB_FILE).connect();
    private final int MAX_TABLES = 50; //Arbitrary but didn't know how else to do it


    public Controller() throws SQLException {
    }

    /**
     * Create a new table/deck in the database
     */
    protected void addDeck(String title) throws SQLException {
        String createTableStmt = "CREATE TABLE " + title + " (term TEXT NOT NULL, def TEXT NOT NULL);";
        executeCUD(createTableStmt);
        addDeckToMenuTable(title);
    }

    private void addDeckToMenuTable(String title) throws SQLException {
        String insertToMenuStmt = "INSERT INTO " + MENU_TABLE + "VALUES (" + title + ");";
        executeCUD(insertToMenuStmt);
    }

    /**
     * Delete a card/row from a given deck/table
     */
    protected void deleteCard(Card card, String table) throws SQLException {
        String removeCardStmt = "DELETE FROM " + table + " WHERE id = " + card.getId() + ";";
        executeCUD(removeCardStmt);
    }


    /**
     * Update a given card's term
     */
    protected void updateTerm(String table, Card card) throws SQLException {
        String updateTermStmt = "UPDATE " + table + "SET term = " + card.getTerm() + "WHERE id = " + card.getId() + ";";
        executeCUD(updateTermStmt);
    }

    /**
     * Update a given card's def
     */
    protected void updateDef(String table, Card card) throws SQLException {
        String updateDefStmt = "UPDATE " + table + "SET def = " + card.getDef() + "WHERE id = " + card.getId() + ";";
        executeCUD(updateDefStmt);
    }

    /**
     * Delete a table/deck in the database
     */

    protected void deleteDeck(String table) throws SQLException{
            String deleteDeckStmt = "DROP TABLE " + table + ";";
            executeCUD(deleteDeckStmt);
            removeFromMenu(table);
    }

    protected void removeFromMenu(String table) throws SQLException {
        String deleteMenuItemStmt = "DELETE FROM " + MENU_TABLE + " WHERE deck_title = " + table + ";";
        executeCUD(deleteMenuItemStmt);
    }

    /**
     * Add card to an existing table/deck
     */

    protected void insertCard(String table, String term, String def) throws SQLException {
        String insertCardStmt = "INSERT INTO " + table + "VALUES (" + term + ", " + def + ");";
        executeCUD(insertCardStmt);
    }



    protected void executeCUD(String cudStatement) throws SQLException {
        CONNECTION.createStatement().execute(cudStatement);
    }

    /**
     * @return Array of all decks in the db
     */
    protected String[] getAllDecks() throws SQLException {

        ResultSet results = selectAll(MENU_TABLE);
        String[] deckList = new String[MAX_TABLES];
        int index = 0;

        while (results.next()) {
            deckList[index] = results.getString("deck_title").toString();
            index ++;
        }

        return deckList;
    }

    protected ResultSet selectAll(String table) throws SQLException {
        String selectStmt = "SELECT * FROM " + table;
        return CONNECTION.createStatement().executeQuery(selectStmt);
    }

    /**
     * populate a deck object using given deck's table
     */
    protected Deck getDeck(String table) throws SQLException {
        List<Card> cards = new ArrayList<>();
        Deck deck = new Deck(cards);
        writeDataToDeck(table, deck);
        return deck;
    }


    private void writeDataToDeck(String table, Deck deck) throws SQLException {

        ResultSet results = selectAll(table);

        while (results.next()) {
            addCardFromDB(deck, results);
        }
    }

    private void addCardFromDB(Deck deck, ResultSet rs) throws SQLException {
        deck.addCard(new Card(rs.getString("id"),

                rs.getString("term"), rs.getString("def")));
    }

}