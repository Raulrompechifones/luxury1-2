
package pe.edu.upeu.luxury12.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Modality;
import javafx.stage.Stage;
import pe.edu.upeu.luxury12.Entidad.Habitacion;
import pe.edu.upeu.luxury12.implementacion.HabitacionDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IHabitacionDao;

import java.io.IOException;

public class HabitacionController {

    @FXML
    private TableView<Habitacion> tablaHabitaciones;

    @FXML
    private TableColumn<Habitacion, Integer> colId;

    @FXML
    private TableColumn<Habitacion, String> colCodigo;

    @FXML
    private TableColumn<Habitacion, String> colNumero;

    @FXML
    private TableColumn<Habitacion, String> colTipo;

    @FXML
    private TableColumn<Habitacion, Double> colPrecio;

    @FXML
    private TableColumn<Habitacion, String> colEstado;

    private final IHabitacionDao habitacionDao =
            new HabitacionDaoImpl();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("idHabitacion"));

        colCodigo.setCellValueFactory(
                new PropertyValueFactory<>("cod_hab"));

        colNumero.setCellValueFactory(
                new PropertyValueFactory<>("num_hab"));

        colTipo.setCellValueFactory(
                new PropertyValueFactory<>("tipoHabitacion"));

        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio"));

        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado"));

        listarHabitaciones();
    }

    @FXML
    private void listarHabitaciones() {

        tablaHabitaciones.setItems(
                FXCollections.observableArrayList(
                        habitacionDao.listar()
                )
        );
    }

    @FXML
    private void actualizarHabitaciones() {

        System.out.println("BOTÓN ACTUALIZAR PRESIONADO");

        listarHabitaciones();
    }



    @FXML
    private void registrarHabitacion() {

        System.out.println("BOTÓN REGISTRAR PRESIONADO");

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/pe/edu/upeu/luxury12/view/view/habitacion-form.fxml"
                    )
            );

            Parent root = loader.load();

            HabitacionFormController controller =
                    loader.getController();

            Stage ventana = new Stage();
            ventana.setTitle("HOTEL LUXURY - Registrar habitación");
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setScene(new Scene(root));
            ventana.showAndWait();

            if (controller.isGuardado()) {
                listarHabitaciones();
                mostrarAlerta("Habitación registrada correctamente.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir el formulario.");
        }
    }



    @FXML
    private void editarHabitacion() {

        System.out.println("BOTÓN EDITAR PRESIONADO");

        Habitacion seleccionada =
                tablaHabitaciones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Selecciona una habitación para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/pe/edu/upeu/luxury12/view/view/habitacion-form.fxml"
                    )
            );

            Parent root = loader.load();

            HabitacionFormController controller =
                    loader.getController();

            controller.cargarHabitacion(seleccionada);

            Stage ventana = new Stage();
            ventana.setTitle("HOTEL LUXURY - Editar habitación");
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setScene(new Scene(root));
            ventana.showAndWait();

            if (controller.isGuardado()) {
                listarHabitaciones();
                mostrarAlerta("Habitación actualizada correctamente.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir el formulario.");
        }
    }


    @FXML
    private void eliminarHabitacion() {

        System.out.println("BOTÓN ELIMINAR PRESIONADO");

        Habitacion seleccionada =
                tablaHabitaciones.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Selecciona una habitación para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("HOTEL LUXURY");
        confirmacion.setHeaderText("Eliminar habitación");
        confirmacion.setContentText(
                "¿Estás seguro de eliminar la habitación "
                        + seleccionada.getCod_hab() + "?"
        );

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                if (habitacionDao.eliminar(seleccionada)) {
                    listarHabitaciones();
                    mostrarAlerta("Habitación eliminada correctamente.");
                } else {
                    mostrarAlerta("No se pudo eliminar la habitación.");
                }
            }
        });
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("HOTEL LUXURY");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
