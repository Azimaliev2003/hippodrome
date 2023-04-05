package ippodprom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class db {
    public static void main(String[] args) throws IOException, SQLException {

    }
    private Connection connection;
    public db() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;encrypt=true;database=ippodrom;trustServerCertificate=true;","student","nurdoolot2003");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Connection getConnection() {
        return connection;
    }
}
