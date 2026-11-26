package pe.edu.upeu.luxury12.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class ConexionDB {

    private static final String DB_FOLDER = "C:/sqlite/db";
    private static final String DB_PATH = DB_FOLDER + "/hotel_luxury.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    private static final int ITERACIONES = 600000;
    private static final int LONGITUD_CLAVE = 256;

    static {
        inicializarBaseDatos();
    }

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al conectar con SQLite: " + e.getMessage(), e
            );
        }
    }

    private static void inicializarBaseDatos() {
        try {
            Class.forName("org.sqlite.JDBC");

            File folder = new File(DB_FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String sqlUsuarios = """
                    CREATE TABLE IF NOT EXISTS usuarios (
                        id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                        nombre TEXT,
                        apellido TEXT,
                        dni TEXT UNIQUE,
                        usuario TEXT UNIQUE NOT NULL,
                        clave TEXT NOT NULL,
                        tipo_usuario TEXT,
                        estado TEXT DEFAULT 'ACTIVO'
                    );
                    """;

            String sqlClientes = """
                    CREATE TABLE IF NOT EXISTS clientes (
                        id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,
                        nombre TEXT,
                        apellido TEXT,
                        dni TEXT UNIQUE,
                        telefono TEXT,
                        email TEXT,
                        cantidad_personas INTEGER
                    );
                    """;

            String sqlHabitaciones = """
                    CREATE TABLE IF NOT EXISTS habitaciones (
                        id_habitacion INTEGER PRIMARY KEY AUTOINCREMENT,
                        cod_hab TEXT NOT NULL UNIQUE,
                        num_hab TEXT,
                        tipo_habitacion TEXT NOT NULL,
                        precio REAL NOT NULL,
                        estado TEXT DEFAULT 'DISPONIBLE'
                    );
                    """;

            String sqlReservas = """
                    CREATE TABLE IF NOT EXISTS reservas (
                        id_reserva INTEGER PRIMARY KEY AUTOINCREMENT,
                        fecha_ingreso TEXT NOT NULL,
                        fecha_reserva TEXT NOT NULL,
                        fecha_salida TEXT NOT NULL,
                        total REAL NOT NULL,
                        estado TEXT DEFAULT 'R',
                        id_cliente INTEGER NOT NULL,
                        id_usuario INTEGER,
                        id_habitacion INTEGER NOT NULL,
                        FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
                        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
                        FOREIGN KEY (id_habitacion) REFERENCES habitaciones(id_habitacion)
                    );
                    """;

            try (Connection conn = DriverManager.getConnection(URL);
                 Statement stmt = conn.createStatement()) {

                stmt.execute("PRAGMA foreign_keys = ON;");
                stmt.execute(sqlUsuarios);
                stmt.execute(sqlClientes);
                stmt.execute(sqlHabitaciones);
                stmt.execute(sqlReservas);

                insertarAdminInicial(conn);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al inicializar la base de datos: " + e.getMessage(), e
            );
        }
    }

    private static void insertarAdminInicial(Connection conn) throws Exception {
        String countSql = "SELECT COUNT(*) FROM usuarios";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = """
                        INSERT INTO usuarios
                        (nombre, apellido, dni, usuario, clave, tipo_usuario, estado)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """;

                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setString(1, "Administrador");
                    ps.setString(2, "Luxury");
                    ps.setString(3, "00000000");
                    ps.setString(4, "admin");
                    ps.setString(5, protegerClave("admin123"));
                    ps.setString(6, "ADMINISTRADOR");
                    ps.setString(7, "ACTIVO");
                    ps.executeUpdate();
                }

                System.out.println("Usuario inicial 'admin' (clave: admin123) creado.");
            }
        }
    }

    private static String protegerClave(String clave) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(
                clave.toCharArray(), salt, ITERACIONES, LONGITUD_CLAVE
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return ITERACIONES + ":" +
                    Base64.getEncoder().encodeToString(salt) + ":" +
                    Base64.getEncoder().encodeToString(hash);
        } finally {
            spec.clearPassword();
        }
    }
}