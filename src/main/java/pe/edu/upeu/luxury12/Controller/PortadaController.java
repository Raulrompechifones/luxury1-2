package pe.edu.upeu.luxury12.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import pe.edu.upeu.luxury12.Entidad.Usuarios;
import pe.edu.upeu.luxury12.implementacion.UsuariosDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IUsuarioDao;

import java.io.IOException;

public class PortadaController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtClave;

    private final IUsuarioDao usuarioDao = new UsuariosDaoImpl();


    @FXML
    private void iniciarSesion(ActionEvent event) {

        String usuario = txtUsuario.getText().trim();
        String clave = txtClave.getText();

        if (usuario.isEmpty() || clave.isEmpty()) {
            mostrarMensaje("Campos vacíos",
                    "Ingrese su usuario y contraseña.");
            return;
        }

        Usuarios usuarioEncontrado = usuarioDao.login(usuario, clave);

        if (usuarioEncontrado == null) {
            mostrarMensaje("Acceso denegado", "Usuario o contraseña incorrectos, o cuenta inactiva."
            );
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pe/edu/upeu/luxury12/view/view/main-view.fxml")
            );

            Parent vista = loader.load();

            MainController mainController = loader.getController();

            mainController.setUsuarioActual(usuarioEncontrado);

            Scene scene = new Scene(vista);

            Stage stage = (Stage)
                    ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(scene);
            stage.setTitle("HOTEL LUXURY - Menú Principal");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();

            mostrarMensaje(
                    "Error",
                    "No se pudo abrir el menú principal."
            );
        }
    }


    @FXML
    private void registrarUsuario() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pe/edu/upeu/luxury12/view/view/registro-usuario.fxml"));

            Scene scene = new Scene(loader.load());

            Stage ventana = new Stage();

            ventana.setTitle("HOTEL LUXURY - Registrar Usuario");
            ventana.setScene(scene);
            ventana.setResizable(false);
            ventana.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            ventana.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();

            mostrarMensaje(
                    "Error",
                    "No se pudo abrir el formulario de registro."
            );
        }
    }


    private void mostrarMensaje(String titulo, String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("HOTEL LUXURY");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}