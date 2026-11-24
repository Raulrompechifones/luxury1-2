package pe.edu.upeu.luxury12.implementacion;

import pe.edu.upeu.luxury12.Entidad.Habitacion;
import pe.edu.upeu.luxury12.Entidad.TipodeHabitacion;
import pe.edu.upeu.luxury12.interfaces.IHabitacionDao;
import pe.edu.upeu.luxury12.utils.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class HabitacionDaoImpl implements IHabitacionDao {
    static ArrayList<Habitacion> lista = new ArrayList<>();

    @Override
    public boolean registrar(Habitacion h) {

        String sql = """
            INSERT INTO habitaciones
            (cod_hab, num_hab, tipo_habitacion, precio, estado)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, h.getCod_hab());
            ps.setString(2, h.getNum_hab());
            ps.setString(3, h.getTipoHabitacion().name());
            ps.setDouble(4, h.getPrecio());
            ps.setString(5, h.getEstado());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("HABITACIÓN REGISTRADA");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar habitación: " + e.getMessage());
        }

        return false;
    }


    @Override
    public List<Habitacion> listar() {

        List<Habitacion> habitaciones = new ArrayList<>();

        String sql = "SELECT * FROM habitaciones";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Habitacion h = new Habitacion();

                h.setIdHabitacion(rs.getInt("id_habitacion"));
                h.setCod_hab(rs.getString("cod_hab"));
                h.setNum_hab(rs.getString("num_hab"));

                h.setTipoHabitacion(
                        TipodeHabitacion.valueOf(
                                rs.getString("tipo_habitacion")
                        )
                );

                h.setPrecio(rs.getDouble("precio"));
                h.setEstado(rs.getString("estado"));

                habitaciones.add(h);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al listar habitaciones: " + e.getMessage()
            );
        }

        return habitaciones;
    }



    @Override
    public boolean editar(Habitacion h) {

        String sql = """
            UPDATE habitaciones
            SET cod_hab = ?,
                num_hab = ?,
                tipo_habitacion = ?,
                precio = ?,
                estado = ?
            WHERE id_habitacion = ?
            """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, h.getCod_hab());
            ps.setString(2, h.getNum_hab());
            ps.setString(3, h.getTipoHabitacion().name());
            ps.setDouble(4, h.getPrecio());
            ps.setString(5, h.getEstado());
            ps.setInt(6, h.getIdHabitacion());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("HABITACIÓN ACTUALIZADA");
                return true;
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al editar habitación: " + e.getMessage()
            );
        }

        return false;
    }



    @Override
    public boolean eliminar(Habitacion h) {

        String sql = "DELETE FROM habitaciones WHERE id_habitacion = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, h.getIdHabitacion());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("HABITACIÓN ELIMINADA");
                return true;
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al eliminar habitación: " + e.getMessage()
            );
        }

        return false;
    }



    @Override
    public Habitacion buscarPorId(int id) {

        String sql = "SELECT * FROM habitaciones WHERE id_habitacion = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Habitacion h = new Habitacion();

                    h.setIdHabitacion(rs.getInt("id_habitacion"));
                    h.setCod_hab(rs.getString("cod_hab"));
                    h.setNum_hab(rs.getString("num_hab"));

                    h.setTipoHabitacion(
                            TipodeHabitacion.valueOf(
                                    rs.getString("tipo_habitacion")
                            )
                    );

                    h.setPrecio(rs.getDouble("precio"));
                    h.setEstado(rs.getString("estado"));

                    return h;
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al buscar habitación por ID: " + e.getMessage()
            );
        }

        return null;
    }



    @Override
    public Habitacion buscarPorCodigo(String cod_hab) {

        String sql = "SELECT * FROM habitaciones WHERE cod_hab = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cod_hab);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Habitacion h = new Habitacion();

                    h.setIdHabitacion(rs.getInt("id_habitacion"));
                    h.setCod_hab(rs.getString("cod_hab"));
                    h.setNum_hab(rs.getString("num_hab"));

                    h.setTipoHabitacion(
                            TipodeHabitacion.valueOf(
                                    rs.getString("tipo_habitacion")
                            )
                    );

                    h.setPrecio(rs.getDouble("precio"));
                    h.setEstado(rs.getString("estado"));

                    return h;
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al buscar habitación por código: " + e.getMessage()
            );
        }

        return null;
    }

}
