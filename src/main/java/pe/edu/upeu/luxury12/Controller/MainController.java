package pe.edu.upeu.luxury12.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import pe.edu.upeu.luxury12.Entidad.Usuarios;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane panelContenido;

    @FXML
    private Label lblUsuarioActual;

    @FXML
    private Label lblRolActual;

    @FXML
    private javafx.scene.control.Button btnUsuarios;

    @FXML
    private javafx.scene.control.Button btnReportes;

    private Usuarios usuarioActual;

    public void setUsuarioActual(Usuarios usuario) {

        this.usuarioActual = usuario;

        lblUsuarioActual.setText(usuario.getUsuario());
        lblRolActual.setText(usuario.getTipoUsuario());

        boolean esAdministrador =
                "ADMINISTRADOR".equals(usuario.getTipoUsuario());


        btnUsuarios.setVisible(esAdministrador);
        btnUsuarios.setManaged(esAdministrador);

        btnReportes.setVisible(esAdministrador);
        btnReportes.setManaged(esAdministrador);

        cambiarContenido(
                "BIENVENIDO A HOTEL LUXURY, " + usuario.getNombre()
        );

        System.out.println("Usuario conectado: " + usuario.getUsuario());
        System.out.println("Rol: " + usuario.getTipoUsuario());
    }

    @FXML
    public void initialize() {

        cambiarContenido("BIENVENIDO AL SISTEMA HOTEL LUXURY");

        javafx.application.Platform.runLater(() -> {

            javafx.stage.Stage stage =
                    (javafx.stage.Stage) panelContenido.getScene().getWindow();

            stage.setResizable(true);
            stage.setMaximized(true);

        });
    }

    @FXML
    private void mostrarInicio() {
        cambiarContenido("BIENVENIDO AL SISTEMA HOTEL LUXURY");
    }


    @FXML
    private void mostrarClientes() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/pe/edu/upeu/luxury12/view/view/cliente-view.fxml"
                    )
            );

            Parent vistaClientes = loader.load();

            panelContenido.getChildren().setAll(vistaClientes);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }



    @FXML
    private void mostrarHabitaciones() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pe/edu/upeu/luxury12/view/view/habitacion-view.fxml")
            );

            Parent vistaHabitaciones = loader.load();

            panelContenido.getChildren().setAll(vistaHabitaciones);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    @FXML
    private void mostrarReservas() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/pe/edu/upeu/luxury12/view/view/reserva-view.fxml"
                    )
            );

            Parent vista = loader.load();


            panelContenido.getChildren().setAll(vista);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarReportes() {

        if (usuarioActual == null ||
                !"ADMINISTRADOR".equals(usuarioActual.getTipoUsuario())) {

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.WARNING
                    );

            alerta.setTitle("HOTEL LUXURY");
            alerta.setHeaderText("Acceso denegado");
            alerta.setContentText(
                    "Solo el administrador puede acceder a los reportes."
            );

            alerta.showAndWait();
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/edu/upeu/luxury12/view/view/reporte-view.fxml"));

            Parent vistaReportes = loader.load();

            panelContenido.getChildren().setAll(vistaReportes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cambiarContenido(String titulo) {

        Label etiqueta = new Label(titulo);

        etiqueta.setStyle(
                "-fx-font-size: 25px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #101820;"
        );

        panelContenido.getChildren().setAll(etiqueta);
    }

    @FXML
    private void mostrarUsuarios() {

        if (usuarioActual == null ||
                !"ADMINISTRADOR".equals(usuarioActual.getTipoUsuario())) {

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.WARNING
                    );

            alerta.setTitle("HOTEL LUXURY");
            alerta.setHeaderText("Acceso denegado");
            alerta.setContentText(
                    "Solo el administrador puede acceder a la gestión de usuarios."
            );
            alerta.showAndWait();

            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/pe/edu/upeu/luxury12/view/view/usuario-view.fxml"
                    )
            );

            Parent vista = loader.load();

            panelContenido.getChildren().setAll(vista);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarSesion() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pe/edu/upeu/luxury12/view/view/portada-view.fxml"));

            Parent vista = loader.load();

            javafx.scene.Scene scene = new javafx.scene.Scene(vista);

            javafx.stage.Stage stage =
                    (javafx.stage.Stage) panelContenido.getScene().getWindow();


            usuarioActual = null;

            stage.setMaximized(false);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("HOTEL LUXURY - Inicio de Sesión");
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (IOException e) {

            e.printStackTrace();

            javafx.scene.control.Alert alerta =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.ERROR
                    );

            alerta.setTitle("HOTEL LUXURY");
            alerta.setHeaderText("Error al cerrar sesión");
            alerta.setContentText("No se pudo regresar al inicio de sesión.");
            alerta.showAndWait();
        }
    }


}
