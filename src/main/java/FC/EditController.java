package FC;

import java.sql.SQLException;
import java.util.List;

public class EditController extends Controller{
    Deck editDeck;

    public EditController() throws SQLException {
    }
    /**
     * Creates a new card in the deck
     */
    public void insertCard(String table, String term, String def) throws SQLException {
       super.insertCard(table, term, def);
       editDeck = super.getDeck(table);
    }
    /**
     * Deletes the selected card from the deck
     */
    public void deleteCard(Card card, String deckName) throws SQLException {
        super.deleteCard(card, deckName);
        editDeck.removeCard(card);
    }
    /**
     * Adds a new deck to the database
     */
    public void initializeNewDeck(String deckName) throws SQLException {
        addDeck(deckName);
    }
    /**
     * Deletes a given deck from the database
     */
    public void deleteDeck(String deckName) throws SQLException {
        super.deleteDeck(deckName);
    }

    public List<Card> getTermsInDeck(String deckname) throws SQLException {
        editDeck = super.getDeck(deckname);
        return editDeck.getTermList();
    }

    public int sizeOfEditDeck(String deckname) throws SQLException {
       editDeck = super.getDeck(deckname);
        return editDeck.getSize();
    }
}
