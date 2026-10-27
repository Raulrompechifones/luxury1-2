
package pe.edu.upeu.luxury12.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import pe.edu.upeu.luxury12.Entidad.Cliente;
import pe.edu.upeu.luxury12.Entidad.Habitacion;
import pe.edu.upeu.luxury12.Entidad.Reserva;
import pe.edu.upeu.luxury12.implementacion.ClienteDaoImpl;
import pe.edu.upeu.luxury12.implementacion.ReservaDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IClienteDao;

import pe.edu.upeu.luxury12.implementacion.HabitacionDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IHabitacionDao;
import pe.edu.upeu.luxury12.interfaces.IReservaDao;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservaFormController {

    @FXML
    private ComboBox<Cliente> cmbCliente;

    @FXML
    private ComboBox<Habitacion> cmbHabitacion;

    @FXML
    private DatePicker fechaIngreso;

    @FXML
    private DatePicker fechaSalida;

    @FXML
    private TextField txtTotal;

    @FXML
    private ComboBox<String> cmbEstado;

    private Reserva reservaEditar;
    private final IClienteDao clienteDao = new ClienteDaoImpl();
    private final IHabitacionDao habitacionDao = new HabitacionDaoImpl();
    private final IReservaDao reservaDao = new ReservaDaoImpl();

    @FXML
    public void initialize() {

        cmbEstado.getItems().addAll("R", "O", "C", "F");
        cmbEstado.setValue("R");

        cargarClientes();
        cargarHabitaciones();

        cmbHabitacion.valueProperty().addListener(
                (obs, anterior, nuevo) -> calcularTotal()
        );

        fechaIngreso.valueProperty().addListener(
                (obs, anterior, nuevo) -> calcularTotal()
        );

        fechaSalida.valueProperty().addListener(
                (obs, anterior, nuevo) -> calcularTotal()
        );
    }

    public void cargarReserva(Reserva reserva) {

        reservaEditar = reserva;

        if (reserva != null) {

            cmbCliente.setValue(reserva.getCliente());
            cmbHabitacion.setValue(reserva.getHabitacion());

            if (reserva.getFecha_ingreso() != null) {
                fechaIngreso.setValue(
                        new java.sql.Date(
                                reserva.getFecha_ingreso().getTime()
                        ).toLocalDate()
                );
            }

            if (reserva.getFecha_salida() != null) {
                fechaSalida.setValue(
                        new java.sql.Date(
                                reserva.getFecha_salida().getTime()
                        ).toLocalDate()
                );
            }

            txtTotal.setText(
                    String.valueOf(reserva.getTotal())
            );

            cmbEstado.setValue(reserva.getEstado());

            calcularTotal();
        }
    }

    @FXML
    private void guardar() {

        // Obtener los datos del formulario
        Cliente cliente = cmbCliente.getValue();
        Habitacion habitacion = cmbHabitacion.getValue();

        LocalDate ingreso = fechaIngreso.getValue();
        LocalDate salida = fechaSalida.getValue();

        String estado = cmbEstado.getValue();


        if (cliente == null || habitacion == null ||
                ingreso == null || salida == null ||
                estado == null) {

            mostrarAlerta("Completa todos los campos");
            return;
        }


        if (!salida.isAfter(ingreso)) {
            mostrarAlerta("La fecha de salida debe ser posterior al ingreso");
            return;
        }


        if ("Mantenimiento".equalsIgnoreCase(habitacion.getEstado())) {
            mostrarAlerta("La habitación está en mantenimiento");
            return;
        }


        calcularTotal();

        double total;

        try {
            total = Double.parseDouble(txtTotal.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("No se pudo calcular el total");
            return;
        }

        if (total <= 0) {
            mostrarAlerta("El total debe ser mayor que cero");
            return;
        }


        Reserva reserva = new Reserva();

        if (reservaEditar != null) {
            reserva.setIdReserva(reservaEditar.getIdReserva());
        }

        reserva.setCliente(cliente);
        reserva.setHabitacion(habitacion);

        reserva.setFecha_ingreso(Date.valueOf(ingreso));
        reserva.setFecha_salida(Date.valueOf(salida));

        reserva.setTotal(total);
        reserva.setEstado(estado);


        if (reservaDao.validarHabitacion(reserva)) {
            mostrarAlerta("La habitación no está disponible para esas fechas");
            return;
        }

        boolean resultado;


        if (reservaEditar == null) {

            reserva.setFecha_reserva(new java.util.Date());

            resultado = reservaDao.registrar(reserva);

        } else {

            resultado = reservaDao.editar(reserva);
        }

        if (resultado) {

            mostrarAlerta("Reserva guardada correctamente");

            Stage ventana = (Stage) txtTotal.getScene().getWindow();
            ventana.close();

        } else {

            mostrarAlerta("No se pudo guardar la reserva");
        }
    }

    @FXML
    private void cancelar() {

        Stage ventana = (Stage) txtTotal.getScene().getWindow();
        ventana.close();
    }

    private void cargarClientes() {

        cmbCliente.setItems(
                FXCollections.observableArrayList(clienteDao.listar())
        );

        cmbCliente.setCellFactory(lista -> new ListCell<Cliente>() {

            @Override
            protected void updateItem(Cliente cliente, boolean vacio) {
                super.updateItem(cliente, vacio);

                if (vacio || cliente == null) {
                    setText(null);
                } else {
                    setText(cliente.getNombre() + " " + cliente.getApellido());
                }
            }
        });

        cmbCliente.setButtonCell(new ListCell<Cliente>() {

            @Override
            protected void updateItem(Cliente cliente, boolean vacio) {
                super.updateItem(cliente, vacio);

                if (vacio || cliente == null) {
                    setText(null);
                } else {
                    setText(cliente.getNombre() + " " + cliente.getApellido());
                }
            }
        });
    }
    private void cargarHabitaciones() {

        cmbHabitacion.setItems(
                FXCollections.observableArrayList(habitacionDao.listar())
        );

        cmbHabitacion.setCellFactory(lista -> new ListCell<Habitacion>() {

            @Override
            protected void updateItem(Habitacion habitacion, boolean vacio) {
                super.updateItem(habitacion, vacio);

                if (vacio || habitacion == null) {
                    setText(null);
                } else {
                    setText(habitacion.getNum_hab() + " - " +
                            habitacion.getTipoHabitacion() + " - S/ " +
                            habitacion.getPrecio());
                }
            }
        });

        cmbHabitacion.setButtonCell(new ListCell<Habitacion>() {

            @Override
            protected void updateItem(Habitacion habitacion, boolean vacio) {
                super.updateItem(habitacion, vacio);

                if (vacio || habitacion == null) {
                    setText(null);
                } else {
                    setText(habitacion.getNum_hab() + " - " +
                            habitacion.getTipoHabitacion() + " - S/ " +
                            habitacion.getPrecio());
                }
            }
        });
    }

    private void calcularTotal() {

        Habitacion habitacion = cmbHabitacion.getValue();
        LocalDate ingreso = fechaIngreso.getValue();
        LocalDate salida = fechaSalida.getValue();

        if (habitacion == null || ingreso == null || salida == null) {
            txtTotal.setText("0.00");
            return;
        }

        long noches = ChronoUnit.DAYS.between(ingreso, salida);

        if (noches <= 0) {
            txtTotal.setText("0.00");
            return;
        }

        if (habitacion.getPrecio() == null) {
            txtTotal.setText("0.00");
            return;
        }

        double total = noches * habitacion.getPrecio();

        txtTotal.setText(String.format(java.util.Locale.US, "%.2f", total));
    }

    private void mostrarAlerta(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("HOTEL LUXURY");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}
