package FC;

import java.sql.*;

public class Connector {
    private String dBFile;
    public Connector(String dBFile){
        this.dBFile = dBFile;
    }
    /**
     * est. connection to cards database
     */
     Connection connect() {
        String url = "jdbc:sqlite:"+ dBFile;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

}
