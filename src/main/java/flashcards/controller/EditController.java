package flashcards.controller;

import flashcards.Card;
import flashcards.Deck;

import java.sql.SQLException;
import java.util.List;

public class EditController extends Controller {
    private Deck editDeck;

    public EditController() throws SQLException { }

    /**
     * Creates a new card in the deck
     */
    public void insertCard(String deck, String term, String def) throws SQLException {
        super.insertCard(deck, term, def);
        editDeck = super.getDeck(deck);
    }

    /**
     * Deletes the selected card from the deck
     */
    public void deleteCard(Card card) throws SQLException {
        super.deleteCard(card);
        editDeck.removeCard(card);
    }

    /**
     * Adds a new deck to the database
     */
    public void initializeNewDeck(String deckName) throws SQLException {
//        super.addDeck(deckName);
        super.addDeck(deckName);
    }

    /**
     * Deletes a given deck from the database
     */
    public void deleteDeck(String deck) throws SQLException {
        super.deleteDeck(deck);
    }

    public List<Card> getTermsInDeck(String deckname) throws SQLException {
        editDeck = super.getDeck(deckname);
        return editDeck.getCardsList();
    }

    public int sizeOfCurrentDeck(String deckname) throws SQLException {
        editDeck = super.getDeck(deckname);
        return editDeck.getSize();

    }

    public Deck getEditDeck() {
        return editDeck;
    }
}
