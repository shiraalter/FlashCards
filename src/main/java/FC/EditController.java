package FC;

import java.sql.SQLException;

public class EditController extends Controller{
    Deck editDeck;


    public EditController() throws SQLException {

    }
    public void insertCard(String table, String term, String def) throws SQLException {
        editDeck = super.getDeck(table);
        String insertCardStmt = "INSERT INTO " + table + "VALUES (" + term + ", " + def + ");";
        executeCUD(insertCardStmt);
//        //fix what id is
//        String id = "";
//        Card card = new Card(id, term, def);
//        editDeck.addCard(card);
    }
    //combined delete methods
    public void deleteCard(Card card, String deckName) throws SQLException {
        editDeck.removeCard(card);
        super.deleteCard(card, deckName);
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
}
