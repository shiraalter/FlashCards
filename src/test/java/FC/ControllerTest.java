package FC;

import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void startNewStudySession() throws SQLException {
        //GIVEN
        Controller controller = new Controller();

        //WHEN
        controller.startNewStudySession("Sample");

        //THEN
        assertNotNull(controller.getMastered());
        assertTrue(controller.getMastered().getSize() == 0);

    }


    @Test
    public void getNextToStudy() {
    }

    @Test
    public void masterCard() {
    }


}