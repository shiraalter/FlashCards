package FC;

import java.sql.SQLException;
import java.util.Random;

public class StudyController extends Controller{
    private Deck unMastered;
    private final Random rand = new Random();

    public StudyController() throws SQLException {
    }

    /**
     * Returns the next card to study
     */
    public Card getNextToStudy() {
        return unMastered.getCard(rand.nextInt(unMastered.getSize()) - 1);
    }

    /**
     * Moves card from unMastered to mastered
     */
    public void masterCard(Card card) {
        unMastered.removeCard(card);
    }

    /**
     * Loads new deck to unMastered and clears mastered
     */
    public void startNewStudySession(String deckName) throws SQLException {
        unMastered = super.getDeck(deckName);
    }
}
