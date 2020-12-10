package FC;

import java.sql.SQLException;

public class CreateDeckController extends Controller {
    public CreateDeckController() throws SQLException {
    }
    /**
     * Adds a new deck to the database
     */
    public void initializeNewDeck(String deckName) throws SQLException {
        addDeck(deckName);
    }
    /**
     * Adds a new card to the deck
     */
    public void addCard(String deckName, String term, String def) throws SQLException {
        insertCard(deckName,term,def);
    }
}
