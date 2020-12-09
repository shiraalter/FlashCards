package FC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {

    private final String menuTable = "menu";
    private final String dBFile = "flash_cards.db";
    private final Connection connection = new Connector(dBFile).connect();


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
        String insertToMenuStmt = "INSERT INTO " + menuTable + "VALUES (" + title + ");";
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
        String deleteMenuItemStmt = "DELETE FROM " + menuTable + " WHERE deck_title = " + table + ";";
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
        connection.createStatement().execute(cudStatement);
    }

    /**
     * @return Array of all decks in the db
     */


    protected String[] getAllDecks() throws SQLException {

        List<String> deckList = new ArrayList<>();
        ResultSet results = selectAll(menuTable);

        while (results.next()) {
            deckList.add(results.getString("deck_title"));
        }

        return (String[]) deckList.toArray();
    }

    protected ResultSet selectAll(String table) throws SQLException {
        String selectStmt = "SELECT * FROM " + table;
        return connection.createStatement().executeQuery(selectStmt);
    }

    /**
     * populate a deck object using given deck's table
     */

    protected Deck getDeck(String table) throws SQLException {

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
        deck.addCard(new Card(rs.getString("id"),

                rs.getString("term"), rs.getString("def")));
    }

}