package pe.edu.upeu.luxury12.implementacion;

import pe.edu.upeu.luxury12.Entidad.Cliente;
import pe.edu.upeu.luxury12.Entidad.Habitacion;
import pe.edu.upeu.luxury12.Entidad.Reserva;
import pe.edu.upeu.luxury12.interfaces.IReservaDao;
import pe.edu.upeu.luxury12.utils.ConexionDB;

import pe.edu.upeu.luxury12.interfaces.IClienteDao;
import pe.edu.upeu.luxury12.interfaces.IHabitacionDao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservaDaoImpl implements IReservaDao {

    private final IClienteDao clienteDao = new ClienteDaoImpl();

    private final IHabitacionDao habitacionDao = new HabitacionDaoImpl();

    static ArrayList<Reserva> lista = new ArrayList<>();


    @Override
    public boolean registrar(Reserva r) {

        if (r == null || r.getCliente() == null ||
                r.getHabitacion() == null ||
                r.getFecha_ingreso() == null ||
                r.getFecha_salida() == null ||
                r.getTotal() == null ||
                r.getEstado() == null) {
            System.out.println("Faltan datos de la reserva.");
            return false;
        }

        if (!r.getFecha_salida().after(r.getFecha_ingreso())) {
            System.out.println("La fecha de salida debe ser posterior al ingreso.");
            return false;
        }

        if (validarHabitacion(r)) {
            System.out.println("La habitación ya está reservada para esas fechas.");
            return false;
        }

        String sql = """
            INSERT INTO reservas
            (fecha_ingreso, fecha_reserva, fecha_salida,
             total, estado, id_cliente, id_usuario, id_habitacion)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, convertirFecha(r.getFecha_ingreso()));
            ps.setString(2, LocalDate.now().toString());
            ps.setString(3, convertirFecha(r.getFecha_salida()));
            ps.setDouble(4, r.getTotal());
            ps.setString(5, r.getEstado());

            ps.setInt(6, r.getCliente().getIdCliente());


            if (r.getUsuarios() == null) {
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {

                ps.setNull(7, java.sql.Types.INTEGER);
            }

            ps.setInt(8, r.getHabitacion().getIdHabitacion());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("RESERVA REGISTRADA");
                return true;
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al registrar reserva: " + e.getMessage()
            );
        }

        return false;
    }

    private String convertirFecha(Date fecha) {

        java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime());

        return fechaSQL.toString();
    }



    @Override
    public List<Reserva> listar() {

        List<Reserva> reservas = new ArrayList<>();

        String sql = """
            SELECT *
            FROM reservas
            ORDER BY id_reserva DESC
            """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Reserva r = new Reserva();

                r.setIdReserva(rs.getInt("id_reserva"));

                r.setFecha_ingreso(
                        java.sql.Date.valueOf(
                                rs.getString("fecha_ingreso")
                        )
                );

                r.setFecha_reserva(
                        java.sql.Date.valueOf(
                                rs.getString("fecha_reserva")
                        )
                );

                r.setFecha_salida(
                        java.sql.Date.valueOf(
                                rs.getString("fecha_salida")
                        )
                );

                r.setTotal(rs.getDouble("total"));
                r.setEstado(rs.getString("estado"));


                int idCliente = rs.getInt("id_cliente");

                Cliente cliente = clienteDao.buscarPorId(idCliente);
                r.setCliente(cliente);


                int idHabitacion = rs.getInt("id_habitacion");

                Habitacion habitacion =
                        habitacionDao.buscarPorId(idHabitacion);

                r.setHabitacion(habitacion);



                reservas.add(r);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al listar reservas: " + e.getMessage()
            );
        }

        return reservas;
    }




    @Override
    public boolean editar(Reserva r) {

        // Primero verificamos que los datos estén completos
        if (r == null || r.getCliente() == null ||
                r.getHabitacion() == null ||
                r.getFecha_ingreso() == null ||
                r.getFecha_salida() == null ||
                r.getTotal() == null ||
                r.getEstado() == null) {

            System.out.println("Faltan datos de la reserva");
            return false;
        }


        if (!r.getFecha_salida().after(r.getFecha_ingreso())) {
            System.out.println("Revisa las fechas de la reserva");
            return false;
        }


        if (validarHabitacion(r)) {
            System.out.println("La habitación ya está reservada");
            return false;
        }

        String sql = "UPDATE reservas SET fecha_ingreso=?, " +
                "fecha_salida=?, total=?, estado=?, " +
                "id_cliente=?, id_habitacion=? " +
                "WHERE id_reserva=?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, convertirFecha(r.getFecha_ingreso()));
            ps.setString(2, convertirFecha(r.getFecha_salida()));
            ps.setDouble(3, r.getTotal());
            ps.setString(4, r.getEstado());
            ps.setInt(5, r.getCliente().getIdCliente());
            ps.setInt(6, r.getHabitacion().getIdHabitacion());
            ps.setInt(7, r.getIdReserva());

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("Reserva actualizada correctamente");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al editar reserva: " + e.getMessage());
        }

        return false;
    }



    @Override
    public boolean eliminar(Reserva r) {

        String sql = "DELETE FROM reservas WHERE id_reserva = ?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getIdReserva());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al eliminar reserva: " + e.getMessage()
            );
            return false;
        }
    }



    @Override
    public Reserva buscarPorId(int id) {

        String sql = "SELECT * FROM reservas WHERE id_reserva = ?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Reserva reserva = new Reserva();

                    reserva.setIdReserva(rs.getInt("id_reserva"));

                    reserva.setFecha_ingreso(
                            java.sql.Date.valueOf(
                                    rs.getString("fecha_ingreso")
                            )
                    );

                    reserva.setFecha_reserva(
                            java.sql.Date.valueOf(
                                    rs.getString("fecha_reserva")
                            )
                    );

                    reserva.setFecha_salida(
                            java.sql.Date.valueOf(
                                    rs.getString("fecha_salida")
                            )
                    );

                    reserva.setTotal(rs.getDouble("total"));
                    reserva.setEstado(rs.getString("estado"));

                    int idCliente = rs.getInt("id_cliente");
                    int idHabitacion = rs.getInt("id_habitacion");

                    reserva.setCliente(
                            clienteDao.buscarPorId(idCliente)
                    );

                    reserva.setHabitacion(
                            habitacionDao.buscarPorId(idHabitacion)
                    );

                    return reserva;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar reserva: "
                    + e.getMessage());
        }

        return null;
    }


    @Override
    public boolean act_estado(Reserva r) {

        if (r == null || r.getEstado() == null) {
            return false;
        }

        String sql = "UPDATE reservas SET estado=? WHERE id_reserva=?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getEstado());
            ps.setInt(2, r.getIdReserva());

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("Estado de reserva actualizado");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado: " + e.getMessage());
        }

        return false;
    }



    @Override
    public boolean validarHabitacion(Reserva r) {

        if (r == null || r.getHabitacion() == null ||
                r.getFecha_ingreso() == null ||
                r.getFecha_salida() == null) {
            return true;
        }

        String sql = """
            SELECT COUNT(*)
            FROM reservas
            WHERE id_habitacion = ?
              AND estado IN ('R', 'O', 'Confirmada', 'Ocupada')
              AND fecha_ingreso < ?
              AND fecha_salida > ?
              AND id_reserva <> ?
            """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, r.getHabitacion().getIdHabitacion());
            ps.setString(2, convertirFecha(r.getFecha_salida()));
            ps.setString(3, convertirFecha(r.getFecha_ingreso()));
            ps.setInt(4, r.getIdReserva());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al validar habitación: " + e.getMessage()
            );

            // Si falla la consulta, bloqueamos la reserva
            // para evitar una posible doble asignación.
            return true;
        }

        return false;
    }

}
