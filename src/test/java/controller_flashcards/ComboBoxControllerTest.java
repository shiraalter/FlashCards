package controller_flashcards;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class ComboBoxControllerTest {

    @Test
    public void getAllDecks() throws SQLException {
        //given
        ComboBoxController comboBoxController = new ComboBoxController();

        //when

        //then
        assertEquals(1, comboBoxController.getAllDecks().size());
    }
}