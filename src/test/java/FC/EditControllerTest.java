package FC;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class EditControllerTest {

    @Test
    public void insertCard() throws SQLException {
        //given
        String title = "Testing";
        String term = "term";
        String def = "def";
        EditController editController = new EditController();

        //when
        editController.addDeck(title);
        editController.insertCard(title, term, def);
        Deck deck = editController.getEditDeck();

        //then
        assertEquals(1, deck.getSize());
        editController.deleteDeck(title);

    }

    @Test
    public void insertCards() throws SQLException {
        //given
        String title = "Testing";
        String term = "term";
        String def = "def";
        String term2 = "term 2";
        String def2 = "definition with a lot of words to test out that it can handle it";
        EditController editController = new EditController();

        //when
        editController.addDeck(title);
        editController.insertCard(title, term, def);
        editController.insertCard(title, term2, def2);
        Deck deck = editController.getEditDeck();

        //then
        assertEquals(2, deck.getSize());
        editController.deleteDeck(title);

    }

    @Test
    public void deleteCard() throws SQLException {
        //given
        String title = "Testing";
        String term = "term";
        String def = "def";
        EditController editController = new EditController();

        //when
        editController.addDeck(title);
        editController.insertCard(title, term, def);
        Deck deck = editController.getEditDeck();
        Card card = deck.getCard(0);
        editController.deleteCard(card, title);

        //then
        assertEquals(0, deck.getSize());
        editController.deleteDeck(title);

    }

    @Test
    public void initializeNewDeck() throws SQLException {
        //given
        EditController editController = new EditController();
        String title = "Testing";

        //when
        editController.initializeNewDeck(title);

        //then
        assertNotNull(editController.getDeck(title));
        editController.deleteDeck(title);

    }


    @Test
    public void initializeNewDeckTwoWords() throws SQLException {
        //given
        EditController editController = new EditController();
        String title = "Testing two words";

        //when
        editController.initializeNewDeck(title);

        //then
        assertNotNull(editController.getDeck(title));
        editController.deleteDeck(title);

    }

    @Test
    public void deleteDeck() throws SQLException {
        //given
        EditController editController = new EditController();
        String title = "Testing";

        //when
        editController.initializeNewDeck(title);
        editController.deleteDeck(title);


        //then
        assertFalse(editController.getAllDecks().contains(title));

    }
}