package flashcards.controller;

import java.sql.SQLException;
import java.util.ArrayList;

public class ComboBoxController extends Controller {
    public ComboBoxController() throws SQLException {
    }
    /**
     * Loads list of decks in the DB for the UI to display upon opening
     */
    public ArrayList<String> getAllDecks() throws SQLException {
        return super.getAllDecks();
    }
}
