package pe.edu.upeu.luxury12.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pe.edu.upeu.luxury12.Entidad.Usuarios;
import pe.edu.upeu.luxury12.implementacion.UsuariosDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IUsuarioDao;

import java.io.IOException;

public class UsuarioController {

    @FXML private TableView<Usuarios> tablaUsuarios;

    @FXML private TableColumn<Usuarios, Integer> colId;
    @FXML private TableColumn<Usuarios, String> colNombre;
    @FXML private TableColumn<Usuarios, String> colApellido;
    @FXML private TableColumn<Usuarios, String> colDni;
    @FXML private TableColumn<Usuarios, String> colUsuario;
    @FXML private TableColumn<Usuarios, String> colRol;
    @FXML private TableColumn<Usuarios, String> colEstado;

    private final IUsuarioDao usuarioDao = new UsuariosDaoImpl();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("idUsuario"));

        colNombre.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));

        colApellido.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("apellido"));

        colDni.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("dni"));

        colUsuario.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("usuario"));

        colRol.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("tipoUsuario"));
        colEstado.setCellValueFactory(
                new javafx.scene.control.cell.PropertyValueFactory<>("estado"));

        listarUsuarios();
    }

    @FXML
    private void registrarUsuario() {
        abrirFormulario(null);
    }


    @FXML
    private void editarUsuario() {

        Usuarios seleccionado =
                tablaUsuarios.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarMensaje("Seleccione un usuario para editar.");
            return;
        }

        abrirFormulario(seleccionado);
    }

    @FXML
    private void eliminarUsuario() {

        Usuarios seleccionado =
                tablaUsuarios.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarMensaje("Seleccione un usuario para eliminar.");
            return;
        }

        // Protección provisional de la cuenta principal
        if (seleccionado.getIdUsuario() == 1) {
            mostrarMensaje("No se puede eliminar la cuenta principal del administrador.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("HOTEL LUXURY");
        confirmacion.setHeaderText("Eliminar usuario");
        confirmacion.setContentText(
                "¿Está seguro de eliminar definitivamente al usuario "
                        + seleccionado.getUsuario() + "?"
        );

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        if (usuarioDao.eliminar(seleccionado)) {
            mostrarMensaje("Usuario eliminado correctamente.");
            listarUsuarios();
        } else {
            mostrarMensaje(
                    "No se pudo eliminar el usuario. " +
                            "Compruebe si tiene registros asociados."
            );
        }
    }

    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("HOTEL LUXURY");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void abrirFormulario(Usuarios usuario) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/pe/edu/upeu/luxury12/view/view/usuario-form.fxml"
                    )
            );

            Parent vista = loader.load();

            UsuarioFormController controller = loader.getController();

            if (usuario != null) {
                controller.cargarUsuario(usuario);
            }

            Stage ventana = new Stage();

            ventana.setTitle(
                    usuario == null
                            ? "HOTEL LUXURY - Registrar Usuario"
                            : "HOTEL LUXURY - Editar Usuario"
            );

            ventana.setScene(new Scene(vista));
            ventana.setResizable(false);
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.showAndWait();

            listarUsuarios();

        } catch (IOException e) {

            e.printStackTrace();
            mostrarMensaje("No se pudo abrir el formulario de usuario.");
        }
    }
    @FXML
    private void listarUsuarios() {
        tablaUsuarios.setItems(
                FXCollections.observableArrayList(usuarioDao.listar())
        );
    }
}