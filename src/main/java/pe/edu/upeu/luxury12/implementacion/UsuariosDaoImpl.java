package pe.edu.upeu.luxury12.implementacion;

import pe.edu.upeu.luxury12.Entidad.Usuarios;
import pe.edu.upeu.luxury12.interfaces.IUsuarioDao;
import pe.edu.upeu.luxury12.utils.ConexionDB;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UsuariosDaoImpl implements IUsuarioDao {

    private static final int ITERACIONES = 600000;
    private static final int LONGITUD_CLAVE = 256;

    private String protegerClave(String clave) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        byte[] hash = generarHash(clave, salt, ITERACIONES);

        return ITERACIONES + ":" +
                Base64.getEncoder().encodeToString(salt) + ":" +
                Base64.getEncoder().encodeToString(hash);
    }

    private byte[] generarHash(String clave, byte[] salt, int iteraciones)
            throws Exception {

        PBEKeySpec spec = new PBEKeySpec(
                clave.toCharArray(), salt, iteraciones, LONGITUD_CLAVE
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            return factory.generateSecret(spec).getEncoded();
        } finally {
            spec.clearPassword();
        }
    }

    private boolean verificarClave(String clave, String almacenada) {
        try {
            if (almacenada == null) {
                return false;
            }

            String[] partes = almacenada.split(":");

            if (partes.length != 3) {
                return false;
            }

            int iteraciones = Integer.parseInt(partes[0]);

            if (iteraciones < 100000 || iteraciones > 1000000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] hashGuardado = Base64.getDecoder().decode(partes[2]);

            byte[] hashIngresado = generarHash(
                    clave, salt, iteraciones
            );

            return MessageDigest.isEqual(hashGuardado, hashIngresado);

        } catch (Exception e) {
            return false;
        }
    }

    private String normalizarEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return "ACTIVO";
        }
        return estado.trim().toUpperCase();
    }

    private Usuarios convertirUsuario(ResultSet rs) throws SQLException {

        Usuarios u = new Usuarios();

        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setDni(rs.getString("dni"));
        u.setUsuario(rs.getString("usuario"));
        u.setTipoUsuario(rs.getString("tipo_usuario"));
        u.setEstado(rs.getString("estado"));
        u.setClave(null);

        return u;
    }

    @Override
    public boolean registrar(Usuarios u) {

        String sql = """
                INSERT INTO usuarios
                (nombre, apellido, dni, usuario, clave, tipo_usuario, estado)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        if (u == null || u.getClave() == null ||
                u.getClave().isBlank() ||
                u.getUsuario() == null ||
                u.getUsuario().isBlank()) {
            return false;
        }

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getDni());
            ps.setString(4, u.getUsuario().trim());
            ps.setString(5, protegerClave(u.getClave()));
            ps.setString(6, u.getTipoUsuario());
            ps.setString(7, normalizarEstado(u.getEstado()));

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuarios> listar() {

        List<Usuarios> lista = new ArrayList<>();

        String sql = """
                SELECT id_usuario, nombre, apellido, dni,
                       usuario, tipo_usuario, estado
                FROM usuarios
                ORDER BY id_usuario
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(convertirUsuario(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean editar(Usuarios u) {

        if (u == null || u.getIdUsuario() <= 0) {
            return false;
        }

        boolean cambiarClave = u.getClave() != null && !u.getClave().isBlank();

        String sql = cambiarClave
                ? """
                  UPDATE usuarios
                  SET nombre = ?, apellido = ?, dni = ?,
                      usuario = ?, clave = ?, tipo_usuario = ?, estado = ?
                  WHERE id_usuario = ?
                  """
                : """
                  UPDATE usuarios
                  SET nombre = ?, apellido = ?, dni = ?,
                      usuario = ?, tipo_usuario = ?, estado = ?
                  WHERE id_usuario = ?
                  """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int idx = 1;
            ps.setString(idx++, u.getNombre());
            ps.setString(idx++, u.getApellido());
            ps.setString(idx++, u.getDni());
            ps.setString(idx++, u.getUsuario());

            if (cambiarClave) {
                ps.setString(idx++, protegerClave(u.getClave()));
            }

            ps.setString(idx++, u.getTipoUsuario());
            ps.setString(idx++, normalizarEstado(u.getEstado()));
            ps.setInt(idx, u.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error al editar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(Usuarios u) {

        if (u == null) {
            return false;
        }

        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, u.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al eliminar usuario: " + e.getMessage()
            );
            return false;
        }
    }

    @Override
    public Usuarios buscarPorId(int id) {

        String sql = """
                SELECT id_usuario, nombre, apellido, dni,
                       usuario, tipo_usuario, estado
                FROM usuarios
                WHERE id_usuario = ?
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertirUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Usuarios login(String user, String password) {

        String sql = """
                SELECT *
                FROM usuarios
                WHERE usuario = ?
                AND estado = 'ACTIVO'
                """;

        if (user == null || password == null) {
            return null;
        }

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String claveGuardada = rs.getString("clave");

                    if (verificarClave(password, claveGuardada)) {
                        return convertirUsuario(rs);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
        }

        return null;
    }
}