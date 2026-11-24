
package pe.edu.upeu.luxury12.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import pe.edu.upeu.luxury12.Entidad.Usuarios;
import pe.edu.upeu.luxury12.Entidad.Habitacion;
import pe.edu.upeu.luxury12.Entidad.Reserva;
import pe.edu.upeu.luxury12.implementacion.HabitacionDaoImpl;
import pe.edu.upeu.luxury12.implementacion.ClienteDaoImpl;
import pe.edu.upeu.luxury12.implementacion.ReservaDaoImpl;
import pe.edu.upeu.luxury12.implementacion.UsuariosDaoImpl;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private StackPane panelContenido;

    @FXML
    private Label lblUsuarioActual;

    @FXML
    private Label lblRolActual;

    @FXML
    private Button btnUsuarios;

    @FXML
    private Button btnReportes;

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

        mostrarInicio();

        System.out.println("Usuario conectado: " + usuario.getUsuario());
        System.out.println("Rol: " + usuario.getTipoUsuario());
    }

    @FXML
    public void initialize() {
        cambiarContenido("BIENVENIDO AL SISTEMA HOTEL LUXURY");

        Platform.runLater(() -> {
            if (panelContenido.getScene() != null) {
                Stage stage =
                        (Stage) panelContenido.getScene().getWindow();

                stage.setResizable(true);
                stage.setMaximized(true);
            }
        });
    }

    @FXML
    private void mostrarInicio() {
        cambiarContenido("BIENVENIDO AL SISTEMA HOTEL LUXURY");
        mostrarDashboard();
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
                    getClass().getResource(
                            "/pe/edu/upeu/luxury12/view/view/habitacion-view.fxml"
                    )
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

            Parent vistaReservas = loader.load();
            panelContenido.getChildren().setAll(vistaReservas);

        } catch (Exception e) {
            e.printStackTrace();

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("HOTEL LUXURY");
            alerta.setHeaderText("Error al abrir reservas");
            alerta.setContentText(
                    "No se pudo cargar la vista de reservas: " + e.getMessage()
            );
            alerta.showAndWait();
        }
    }



    @FXML
    private void mostrarReportes() {
        if (usuarioActual == null ||
                !"ADMINISTRADOR".equals(usuarioActual.getTipoUsuario())) {

            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("HOTEL LUXURY");
            alerta.setHeaderText("Acceso denegado");
            alerta.setContentText(
                    "Solo el administrador puede acceder a los reportes."
            );

            alerta.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/pe/edu/upeu/luxury12/view/view/reporte-view.fxml"
                    )
            );

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

            Alert alerta = new Alert(Alert.AlertType.WARNING);
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

    private void calcularEstadisticasHabitaciones() {
        HabitacionDaoImpl habitacionDao = new HabitacionDaoImpl();

        List<Habitacion> habitaciones = habitacionDao.listar();

        int totalHabitaciones = habitaciones.size();
        int habitacionesOcupadas = 0;
        int habitacionesDisponibles = 0;

        for (Habitacion habitacion : habitaciones) {
            String estado = habitacion.getEstado();

            if (estado != null) {
                if (estado.trim().equalsIgnoreCase("OCUPADA")) {
                    habitacionesOcupadas++;
                }

                if (estado.trim().equalsIgnoreCase("DISPONIBLE")) {
                    habitacionesDisponibles++;
                }
            }
        }

        double porcentajeOcupacion = 0;

        if (totalHabitaciones > 0) {
            porcentajeOcupacion =
                    (habitacionesOcupadas * 100.0) / totalHabitaciones;
        }

        System.out.println("===== ESTADÍSTICAS HOTEL LUXURY =====");
        System.out.println("Total de habitaciones: " + totalHabitaciones);
        System.out.println("Habitaciones ocupadas: " + habitacionesOcupadas);
        System.out.println("Habitaciones disponibles: " + habitacionesDisponibles);
        System.out.printf("Porcentaje de ocupación: %.2f%%%n",
                porcentajeOcupacion);
    }

    private void mostrarDashboard() {
        HabitacionDaoImpl dao = new HabitacionDaoImpl();
        List<Habitacion> habitaciones = dao.listar();

        int total = habitaciones.size();

        long ocupadas = habitaciones.stream()
                .filter(h -> "OCUPADA".equalsIgnoreCase(
                        h.getEstado() == null ? "" : h.getEstado().trim()))
                .count();

        int totalClientes = new ClienteDaoImpl().listar().size();

        int totalUsuarios = new UsuariosDaoImpl().listar().size();

        long reservasActivas = new ReservaDaoImpl().listar().stream()
                .filter(r -> r.getEstado() != null &&
                        (r.getEstado().equalsIgnoreCase("Confirmada") ||
                                r.getEstado().equalsIgnoreCase("Pendiente") ||
                                r.getEstado().equalsIgnoreCase("Ocupada")))
                .count();

        String rol = usuarioActual == null
                ? "Usuario"
                : usuarioActual.getTipoUsuario();

        String nombreRol = rol.substring(0, 1).toUpperCase()
                + rol.substring(1).toLowerCase();

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(25));
        contenido.setStyle("-fx-background-color: #F7F8FA;");

        Label bienvenida = new Label("Bienvenido, " + nombreRol);
        bienvenida.setStyle(
                "-fx-font-size: 27px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #102638;"
        );

        Label subtitulo = new Label(
                "Sistema de Gestión Hotelera - Hotel Luxury"
        );
        subtitulo.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #536575;"
        );

        HBox tarjetas = new HBox(16);
        tarjetas.setAlignment(Pos.CENTER);
        tarjetas.setMaxWidth(Double.MAX_VALUE);

        tarjetas.getChildren().addAll(
                crearTarjeta("♟", "Clientes registrados",
                        String.valueOf(totalClientes)),

                crearTarjeta("▰", "Habitaciones ocupadas",
                        String.valueOf(ocupadas)),

                crearTarjeta("▦", "Reservas activas",
                        String.valueOf(reservasActivas)),

                crearTarjeta("♙", "Usuarios del sistema",
                        String.valueOf(totalUsuarios))
        );

        StackPane banner = new StackPane();
        banner.setPrefHeight(330);
        banner.setMinHeight(280);
        banner.setMaxHeight(330);
        banner.setStyle(
                "-fx-background-color: #102638;" +
                        "-fx-background-radius: 8;"
        );

        java.net.URL recurso = getClass().getResource(
                "/pe/edu/upeu/luxury12/view/img/hotel_habitacion.png"
        );

        if (recurso != null) {
            Image imagen = new Image(recurso.toExternalForm());

            ImageView imagenHabitacion = new ImageView(imagen);
            imagenHabitacion.setPreserveRatio(false);
            imagenHabitacion.fitWidthProperty().bind(banner.widthProperty());
            imagenHabitacion.fitHeightProperty().bind(banner.heightProperty());

            banner.getChildren().add(imagenHabitacion);
        }

        Label frase = new Label("La mejor experiencia,\nsiempre.");
        frase.setStyle(
                "-fx-font-size: 29px;" +
                        "-fx-font-family: 'Georgia';" +
                        "-fx-font-style: italic;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #F2D58A;" +
                        "-fx-effect: dropshadow(gaussian, #000000, 5, 0.5, 1, 2);"
        );

        frase.setAlignment(Pos.CENTER_RIGHT);
        frase.setTextAlignment(TextAlignment.CENTER);

        StackPane.setAlignment(frase, Pos.CENTER_RIGHT);
        StackPane.setMargin(frase, new Insets(0, 35, 0, 0));

        banner.getChildren().add(frase);

        contenido.getChildren().addAll(
                bienvenida,
                subtitulo,
                tarjetas,
                banner
        );

        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle(
                "-fx-background: #F7F8FA;" +
                        "-fx-background-color: #F7F8FA;" +
                        "-fx-border-color: transparent;"
        );

        panelContenido.getChildren().setAll(scroll);
    }

    private VBox crearTarjeta(String icono, String titulo, String valor) {
        VBox tarjeta = new VBox(12);

        tarjeta.setAlignment(Pos.CENTER);
        tarjeta.setPrefWidth(200);
        tarjeta.setMinWidth(150);
        tarjeta.setPrefHeight(145);
        tarjeta.setMinHeight(145);
        tarjeta.setMaxHeight(145);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        tarjeta.setPadding(new Insets(18));

        tarjeta.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: #E5E8EC;" +
                        "-fx-border-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, #CBD1D8, 9, 0.15, 0, 3);"
        );

        Label lblIcono = new Label(icono);
        lblIcono.setStyle(
                "-fx-font-size: 34px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #A17A28;"
        );

        Label lblTitulo = new Label(titulo);
        lblTitulo.setWrapText(true);
        lblTitulo.setAlignment(Pos.CENTER);
        lblTitulo.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #435466;"
        );

        Label lblValor = new Label(valor);
        lblValor.setStyle(
                "-fx-font-size: 27px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #102638;"
        );

        tarjeta.getChildren().addAll(
                lblIcono,
                lblTitulo,
                lblValor
        );

        HBox.setHgrow(tarjeta, Priority.ALWAYS);

        return tarjeta;
    }

    @FXML
    private void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/pe/edu/upeu/luxury12/view/view/portada-view.fxml"
                    )
            );

            Parent vista = loader.load();

            javafx.scene.Scene scene = new javafx.scene.Scene(vista);

            Stage stage =
                    (Stage) panelContenido.getScene().getWindow();

            usuarioActual = null;

            stage.setMaximized(false);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("HOTEL LUXURY - Inicio de Sesión");
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();

            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("HOTEL LUXURY");
            alerta.setHeaderText("Error al cerrar sesión");
            alerta.setContentText(
                    "No se pudo regresar al inicio de sesión."
            );

            alerta.showAndWait();
        }
    }
}

