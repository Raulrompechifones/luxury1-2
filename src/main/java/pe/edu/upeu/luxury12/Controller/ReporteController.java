package pe.edu.upeu.luxury12.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import pe.edu.upeu.luxury12.Entidad.Reserva;
import pe.edu.upeu.luxury12.implementacion.ClienteDaoImpl;
import pe.edu.upeu.luxury12.implementacion.HabitacionDaoImpl;
import pe.edu.upeu.luxury12.implementacion.ReservaDaoImpl;

import java.util.List;

public class ReporteController {

    @FXML
    private Label lblClientes;

    @FXML
    private Label lblHabitaciones;

    @FXML
    private Label lblReservas;

    @FXML
    private Label lblActivas;

    @FXML
    private Label lblCanceladas;

    @FXML
    private Label lblIngresos;

    private final ClienteDaoImpl clienteDao = new ClienteDaoImpl();
    private final HabitacionDaoImpl habitacionDao = new HabitacionDaoImpl();
    private final ReservaDaoImpl reservaDao = new ReservaDaoImpl();

    @FXML
    public void initialize() {
        cargarReportes();
    }

    @FXML
    private void cargarReportes() {

        int clientes = clienteDao.listar().size();
        int habitaciones = habitacionDao.listar().size();

        List<Reserva> reservas = reservaDao.listar();

        int activas = 0;
        int canceladas = 0;
        double ingresos = 0;

        for (Reserva r : reservas) {

            String estado = r.getEstado();

            if ("R".equals(estado) || "O".equals(estado)) {
                activas++;
            }

            if ("C".equals(estado)) {
                canceladas++;
            }

            if (!"C".equals(estado) && r.getTotal() != null) {
                ingresos += r.getTotal();
            }
        }

        lblClientes.setText(String.valueOf(clientes));
        lblHabitaciones.setText(String.valueOf(habitaciones));
        lblReservas.setText(String.valueOf(reservas.size()));
        lblActivas.setText(String.valueOf(activas));
        lblCanceladas.setText(String.valueOf(canceladas));

        lblIngresos.setText(
                String.format(java.util.Locale.US, "S/ %.2f", ingresos)
        );
    }
}