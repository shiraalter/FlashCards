package flashcards.controller;

import flashcards.controller.Connector;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectorTest {

    @Test
    public void connect() throws SQLException {
        //given
        String DB_FILE = "flash_cards.db";
        Connector connector = new Connector(DB_FILE);

        //when
        Connection connection = connector.connect();

        //then
        assert(connection.isValid(0));
    }
}
