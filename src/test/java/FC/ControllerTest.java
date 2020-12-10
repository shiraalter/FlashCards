package FC;

import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

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
        try {
            controller.getDeck(title);
        } catch (SQLException sqlException) {
            sqlException.getErrorCode();
            assertEquals(1, sqlException.getErrorCode());
        }
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
