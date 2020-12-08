package FC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private final String menuTable = "menu";
    private final String dBFile = "flash_cards.db";
    private final Connector connector = new Connector(dBFile);


    /**
     * Create a new table/deck in the database
     */
    private void addDeck(String title) {
        try {
            String createTableStmt = "CREATE TABLE " + title + " (term TEXT NOT NULL, def TEXT NOT NULL);";
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
    private void deleteCard(Card card, String table) {
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
    private void updateTerm(String table, Card card) {
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
    private void updateDef(String table, Card card) {
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
    private void deleteDeck(String table) {
        try {
            String deleteDeckStmt = "DROP TABLE " + table + ";";
            executeCud(deleteDeckStmt);
            removeFromMenu(table);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeFromMenu(String table) throws SQLException {
        String deleteMenuItemStmt = "DELETE FROM " + menuTable + " WHERE deck_title = " + table + ";";
        executeCud(deleteMenuItemStmt);
    }

    /**
     * Add card to an existing table/deck
     */
    private void insertCard(String table, String term, String def) {
        try {
            String insertCardStmt = "INSERT INTO " + table + "VALUES (" + term + ", " + def + ");";
            executeCud(insertCardStmt);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void executeCud(String cudStatement) throws SQLException {
        connector.connect().createStatement().execute(cudStatement);
    }

    /**
     *
     * @return Array of all decks in the db
     */
    private String[] getAllDecks(){
        List<String> deckList = new ArrayList<>();
        try {
            String selectStmt = "SELECT * FROM menu";
            ResultSet results = connector.connect().createStatement().executeQuery(selectStmt);

            while (results.next()) {
                deckList.add(results.getString("deck_title"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return (String[]) deckList.toArray();
    }

    /**
     * populate a deck object using given deck's table
     */
    private Deck getDeck(String table) {
        Deck deck = new Deck();
        getDeckData(table, deck);
        return deck;
    }

    private void getDeckData(String table, Deck deck) {
        try {
            String selectStmt = "SELECT * FROM " + table;
            ResultSet results = connector.connect().createStatement().executeQuery(selectStmt);

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
