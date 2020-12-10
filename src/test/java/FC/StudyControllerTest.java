package FC;

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
        assert (card.getDef() != null);
        assert (card.getTerm() != null);
        assert (card.getId() != null);
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
        assert (deck.getSize() == 4);

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
        deck = studyController.getUnMastered();

        //then
        assertEquals (5, deck.getSize());

    }
}