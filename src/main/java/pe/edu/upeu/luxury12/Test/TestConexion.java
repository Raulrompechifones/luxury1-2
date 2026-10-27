
package pe.edu.upeu.luxury12.Test;

import pe.edu.upeu.luxury12.utils.ConexionDB;
import java.sql.Connection;

public class TestConexion {

    public static void testConexion() {

        try (Connection conexion = ConexionDB.getConnection()) {

            if (conexion != null && !conexion.isClosed()) {

                System.out.println("CONEXION EXITOSA A SQLITE - HOTEL LUXURY");

            } else {
                System.out.println("ERROR: NO SE PUDO ESTABLECER LA CONEXION");
            }

        } catch (Exception e) {

            System.out.println("ERROR DE CONEXION: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        testConexion();
    }
}
