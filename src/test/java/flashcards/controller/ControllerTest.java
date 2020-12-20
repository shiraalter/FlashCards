package flashcards.controller;

import flashcards.Card;
import flashcards.Deck;
import flashcards.controller.Controller;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Run tests one by one.
 * When run all at once, database file becomes locked
 * and cannot access database, so some fail due to that.
 */

public class ControllerTest {

    @Test
    public void addDeck() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing";

        //when
        controller.addDeck(title);

        //then
        assertNotNull(controller.getDeck(title));
        controller.deleteDeck(title);
    }


    @Test
    public void addDeckWithTwoWords() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Two words";

        //when
        controller.addDeck(title);

        //then
        assertNotNull(controller.getDeck(title));
        controller.deleteDeck(title);
    }


    @Test
    public void addDeckApostrophe() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing apostrophe'";

        //when
        controller.addDeck(title);

        //then
        assertNotNull(controller.getDeck(title));
        controller.deleteDeck(title);
    }

    @Test
    public void deleteCard() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing";
        String term = "term";
        String def = "definition";

        //when
        controller.addDeck(title);
        controller.insertCard(title, term, def);
        Deck deck = controller.getDeck(title);
        Card card = deck.getCard(0);
        controller.deleteCard(card, title);
        deck = controller.getDeck(title);

        //then
        assertEquals(0, deck.getSize());
        controller.deleteDeck(title);
    }


    @Test
    public void deleteCardApostrophe() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing";
        String term = "term apostrophe '";
        String def = "definition apostrophe '";

        //when
        controller.addDeck(title);
        controller.insertCard(title, term, def);
        Deck deck = controller.getDeck(title);
        Card card = deck.getCard(0);
        controller.deleteCard(card, title);
        deck = controller.getDeck(title);

        //then
        assertEquals(0, deck.getSize());
        controller.deleteDeck(title);
    }

    @Test
    public void updateTerm() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing";
        String term = "term";
        String def = "definition";

        //when
        controller.addDeck(title);
        controller.insertCard(title, term, def);
        Deck deck = controller.getDeck(title);
        Card card = deck.getCard(0);
        String newTerm = "newTerm";
        card.setTerm(newTerm);

        //then
        controller.updateTerm(title, card);
        assertEquals("newTerm", card.getTerm());
        controller.deleteDeck(title);
    }

    @Test
    public void updateDef() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing";
        String term = "term";
        String def = "definition";

        //when
        controller.addDeck(title);
        controller.insertCard(title, term, def);
        Deck deck = controller.getDeck(title);
        Card card = deck.getCard(0);
        String newDef = "newDef";
        card.setDef(newDef);

        //then
        controller.updateDef(title, card);
        assertEquals("newDef", card.getDef());
        controller.deleteDeck(title);
    }

    @Test()
    public void deleteDeck() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing";


        //when
        controller.addDeck(title);
        controller.getDeck(title);
        controller.deleteDeck(title);

        //then
        assertFalse(controller.getAllDecks().contains(title));
        assertEquals(1, controller.getAllDecks().size());
    }


    @Test()
    public void deleteDeckApostrophe() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing apostrophe '";


        //when
        controller.addDeck(title);
        controller.getDeck(title);
        controller.deleteDeck(title);

        //then
        assertFalse(controller.getAllDecks().contains(title));
        assertEquals(1, controller.getAllDecks().size());
    }

    @Test
    public void insertCard() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing";
        String term = "term";
        String definition = "definition";

        //when
        controller.addDeck(title);
        controller.insertCard(title, term, definition);
        Deck deck = controller.getDeck(title);

        //then
        assertNotNull(deck.getCard(0));
        controller.deleteDeck(title);
    }


    @Test
    public void insertCardApostrophe() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing";
        String term = "term apostrophe '";
        String definition = "definition apostrophe '";

        //when
        controller.addDeck(title);
        controller.insertCard(title, term, definition);
        Deck deck = controller.getDeck(title);

        //then
        assertNotNull(deck.getCard(0));
        controller.deleteDeck(title);
    }

    @Test
    public void insertCards() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing";
        String term = "term one";
        String definition = "definition one";
        String term2 = "term 2";
        String def2 = "definition with a lot of words to test out that it can handle it";

        //when
        controller.addDeck(title);
        controller.insertCard(title, term, definition);
        controller.insertCard(title, term2, def2);
        Deck deck = controller.getDeck(title);

        //then
        assertNotNull(deck.getCard(0));
        assertEquals(2, deck.getSize());
        controller.deleteDeck(title);
    }

    @Test
    public void getAllDecks() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Testing";

        //when
        controller.addDeck(title);
        ArrayList<String> allDecks = controller.getAllDecks();

        //then
        assertEquals(2, allDecks.size());
        controller.deleteDeck(title);
    }

    @Test
    public void selectAll() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Sample";

        //when


        //then
        assertNotNull(controller.selectAll(title));
    }

    @Test
    public void getDeck() throws SQLException {
        //given
        Controller controller = new Controller();
        String title = "Sample";

        //when
        Deck deck = controller.getDeck(title);

        //then
        assertEquals(5, deck.getSize());
    }
}
