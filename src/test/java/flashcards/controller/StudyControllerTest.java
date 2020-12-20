package flashcards.controller;

import flashcards.Card;
import flashcards.Deck;
import flashcards.controller.StudyController;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class StudyControllerTest {

    @Test
    public void getNextToStudy() throws SQLException {
        //given
        String deckName = "Sample";
        StudyController studyController = new StudyController(deckName);

        //when
        Card card = studyController.getNextToStudy();

        //then
        assertNotNull(card.getDef());
        assertNotNull(card.getTerm());
        assertNotNull(card.getId());
    }

    @Test
    public void masterCard() throws SQLException {
        //given
        String deckName = "Sample";
        StudyController studyController = new StudyController(deckName);
        Card card = studyController.getDeck(deckName).getCard(1);
        Deck deck = studyController.getUnMastered();

        //when
        studyController.masterCard(card);

        //then
        assertEquals(4, deck.getSize());
    }

    @Test
    public void startNewStudySession() throws SQLException {
        //given
        String deckName = "Sample";
        StudyController studyController = new StudyController(deckName);
        Deck deck = studyController.getUnMastered();
        int deckSize = deck.getSize();

        //when
        for (int i = 0; i < deckSize; i++) {
            Card card = studyController.getDeck(deckName).getCard(i);
            studyController.masterCard(card);
        }
        studyController.startNewStudySession(deckName);
        assertEquals(0, deck.getSize());
        deck = studyController.getUnMastered();

        //then
        assertEquals(5, deck.getSize());
    }
}