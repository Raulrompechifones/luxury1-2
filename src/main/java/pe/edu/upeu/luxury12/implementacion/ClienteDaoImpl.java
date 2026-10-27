
package pe.edu.upeu.luxury12.implementacion;

import pe.edu.upeu.luxury12.Entidad.Cliente;
import pe.edu.upeu.luxury12.interfaces.IClienteDao;
import pe.edu.upeu.luxury12.utils.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDaoImpl implements IClienteDao {

    @Override
    public boolean registrar(Cliente c) {
        String sql = """
                INSERT INTO clientes
                (nombre, apellido, dni, telefono, email, cantidad_personas)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getDni());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getEmail());
            ps.setInt(6, c.getCantidaP());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY id_cliente";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(convertirCliente(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    @Override
    public boolean editar(Cliente c) {
        String sql = """
                UPDATE clientes SET
                nombre = ?, apellido = ?, dni = ?,
                telefono = ?, email = ?, cantidad_personas = ?
                WHERE id_cliente = ?
                """;

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getDni());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getEmail());
            ps.setInt(6, c.getCantidaP());
            ps.setInt(7, c.getIdCliente());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al editar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(Cliente c) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getIdCliente());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertirCliente(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }

        return null;
    }

    @Override
    public Cliente buscarPorDNI(String dni) {
        String sql = "SELECT * FROM clientes WHERE dni = ?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertirCliente(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar DNI: " + e.getMessage());
        }

        return null;
    }

    private Cliente convertirCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id_cliente"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getInt("cantidad_personas"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("dni")
        );
    }
}
