
package pe.edu.upeu.luxury12.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pe.edu.upeu.luxury12.Entidad.Reserva;
import pe.edu.upeu.luxury12.implementacion.ReservaDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IReservaDao;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;

import pe.edu.upeu.luxury12.implementacion.ClienteDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IClienteDao;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class ReservaController {

    @FXML
    private TableView<Reserva> tablaReservas;

    @FXML
    private TableColumn<Reserva, Integer> colId;

    @FXML
    private TableColumn<Reserva, String> colCliente;

    @FXML
    private TableColumn<Reserva, String> colHabitacion;

    @FXML
    private TableColumn<Reserva, String> colIngreso;

    @FXML
    private TableColumn<Reserva, String> colSalida;

    @FXML
    private TableColumn<Reserva, Double> colTotal;

    @FXML
    private TableColumn<Reserva, String> colEstado;

    private final IReservaDao reservaDao = new ReservaDaoImpl();


    @FXML
    public void initialize() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("idReserva")
        );

        colCliente.setCellValueFactory(datos -> {
            Reserva r = datos.getValue();

            String nombre = "";

            if (r.getCliente() != null) {
                nombre = r.getCliente().getNombre() + " " +
                        r.getCliente().getApellido();
            }

            return new javafx.beans.property.SimpleStringProperty(nombre);
        });

        colHabitacion.setCellValueFactory(datos -> {
            Reserva r = datos.getValue();

            String numero = "";

            if (r.getHabitacion() != null) {
                numero = r.getHabitacion().getNum_hab();
            }

            return new javafx.beans.property.SimpleStringProperty(numero);
        });

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        colIngreso.setCellValueFactory(datos -> {
            Reserva r = datos.getValue();

            String fecha = r.getFecha_ingreso() == null
                    ? ""
                    : formato.format(r.getFecha_ingreso());

            return new javafx.beans.property.SimpleStringProperty(fecha);
        });

        colSalida.setCellValueFactory(datos -> {
            Reserva r = datos.getValue();

            String fecha = r.getFecha_salida() == null
                    ? ""
                    : formato.format(r.getFecha_salida());

            return new javafx.beans.property.SimpleStringProperty(fecha);
        });

        colTotal.setCellValueFactory(
                new PropertyValueFactory<>("total")
        );

        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado")
        );

        listarReservas();
    }

    private void listarReservas() {
        tablaReservas.setItems(
                FXCollections.observableArrayList(reservaDao.listar())
        );
    }

    @FXML
    private void actualizarReservas() {
        listarReservas();
    }

    @FXML
    private void registrarReserva() {
        abrirFormulario(null);
    }


    @FXML
    private void editarReserva() {

        Reserva seleccionada =
                tablaReservas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarMensaje("Selecciona una reserva para editar");
            return;
        }

        abrirFormulario(seleccionada);
    }


    @FXML
    private void eliminarReserva() {

        Reserva seleccionada =
                tablaReservas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setContentText("Seleccione una reserva para eliminar.");
            alerta.showAndWait();
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);

        confirmacion.setTitle("HOTEL LUXURY");
        confirmacion.setHeaderText("Eliminar reserva");
        confirmacion.setContentText(
                "¿Está seguro de eliminar definitivamente la reserva seleccionada?"
        );

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        if (reservaDao.eliminar(seleccionada)) {

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setContentText("Reserva eliminada correctamente.");
            alerta.showAndWait();

            listarReservas();

        } else {

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("No se pudo eliminar la reserva.");
            alerta.showAndWait();
        }
    }

    private void mostrarMensaje(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("HOTEL LUXURY");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void abrirFormulario(Reserva reserva) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/pe/edu/upeu/luxury12/view/view/reserva-form.fxml"
                    )
            );

            Parent root = loader.load();

            ReservaFormController controlador = loader.getController();

            if (reserva != null) {
                controlador.cargarReserva(reserva);
            }

            Stage ventana = new Stage();
            ventana.setTitle("HOTEL LUXURY - Reserva");
            ventana.setScene(new Scene(root));
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setResizable(false);

            ventana.showAndWait();

            listarReservas();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje("No se pudo abrir el formulario");
        }
    }

}
