
package pe.edu.upeu.luxury12.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {

    private static final String URL =
            "jdbc:sqlite:C:/sqlite/db/hotel_luxury.db";

    public static Connection getConnection() {

        try {Connection connection = DriverManager.getConnection(URL);

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");}

            System.out.println("Conexión exitosa a HOTEL LUXURY");
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al conectar con SQLite: " + e.getMessage(), e
            );
        }
    }
}
