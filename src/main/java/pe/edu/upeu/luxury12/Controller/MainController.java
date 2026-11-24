package pe.edu.upeu.luxury12.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import pe.edu.upeu.luxury12.Entidad.Reserva;
import pe.edu.upeu.luxury12.Entidad.Usuarios;

import pe.edu.upeu.luxury12.Entidad.Habitacion;
import pe.edu.upeu.luxury12.implementacion.HabitacionDaoImpl;
import java.util.List;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ProgressIndicator;
import javafx.geometry.Pos;
import pe.edu.upeu.luxury12.implementacion.ClienteDaoImpl;
import pe.edu.upeu.luxury12.implementacion.ReservaDaoImpl;
import pe.edu.upeu.luxury12.implementacion.UsuariosDaoImpl;

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

        mostrarInicio();

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

        long disponibles = habitaciones.stream()
                .filter(h -> "DISPONIBLE".equalsIgnoreCase(
                        h.getEstado() == null ? "" : h.getEstado().trim()))
                .count();

        double porcentaje = total == 0 ? 0 : (ocupadas * 100.0 / total);

        VBox contenido = new VBox(25);
        contenido.setAlignment(Pos.TOP_LEFT);
        contenido.setStyle("-fx-padding: 30;");

        String rol = usuarioActual == null
                ? "Usuario"
                : usuarioActual.getTipoUsuario();

        String nombreRol = rol.substring(0, 1).toUpperCase()
                + rol.substring(1).toLowerCase();

        Label bienvenida = new Label("Bienvenido, " + nombreRol);
        bienvenida.setStyle(
                "-fx-font-size: 27px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #071827;"
        );

        Label subtitulo = new Label(
                "Sistema de Gestión Hotelera - Hotel Luxury"
        );
        subtitulo.setStyle("-fx-text-fill: #64748B;");


        int totalClientes = new ClienteDaoImpl().listar().size();

        int totalUsuarios = new UsuariosDaoImpl().listar().size();

        long reservasActivas = new ReservaDaoImpl().listar().stream()
                .filter(r -> r.getEstado() != null &&
                        (
                                r.getEstado().equalsIgnoreCase("Confirmada") ||
                                        r.getEstado().equalsIgnoreCase("Pendiente") ||
                                        r.getEstado().equalsIgnoreCase("Ocupada")
                        ))
                .count();


        HBox tarjetas = new HBox(20);

        tarjetas.getChildren().addAll(
                crearTarjeta("CLIENTES REGISTRADOS",
                        String.valueOf(totalClientes)),

                crearTarjeta("HABITACIONES TOTALES",
                        String.valueOf(total)),

                crearTarjeta("RESERVAS ACTIVAS",
                        String.valueOf(reservasActivas)),

                crearTarjeta("USUARIOS DEL SISTEMA",
                        String.valueOf(totalUsuarios))
        );

        VBox panelOcupacion = new VBox(15);
        panelOcupacion.setAlignment(Pos.CENTER);
        panelOcupacion.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 25;"
        );

        Label tituloOcupacion = new Label("OCUPACIÓN DE HABITACIONES");
        tituloOcupacion.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #071827;"
        );

        ProgressIndicator indicador = new ProgressIndicator(porcentaje / 100.0);
        indicador.setPrefSize(150, 150);
        indicador.setMinSize(150, 150);
        indicador.setMaxSize(150, 150);

        Label porcentajeTexto = new Label(
                String.format("%.2f%%", porcentaje)
        );
        porcentajeTexto.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #D4AF37;"
        );

        panelOcupacion.getChildren().addAll(
                tituloOcupacion,
                indicador,
                porcentajeTexto
        );

        // ÚLTIMAS RESERVAS REGISTRADAS
        Label tituloReservas = new Label("ÚLTIMAS RESERVAS REGISTRADAS");
        tituloReservas.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #071827;"
        );

        TableView<Reserva> tablaReservas = new TableView<>();

        TableColumn<Reserva, Integer> colId = new TableColumn<>("CÓDIGO");
        colId.setCellValueFactory(
                new PropertyValueFactory<>("idReserva")
        );

        TableColumn<Reserva, String> colIngreso = new TableColumn<>("INGRESO");
        colIngreso.setCellValueFactory(
                new PropertyValueFactory<>("fecha_ingreso")
        );

        TableColumn<Reserva, String> colSalida = new TableColumn<>("SALIDA");
        colSalida.setCellValueFactory(
                new PropertyValueFactory<>("fecha_salida")
        );

        TableColumn<Reserva, Double> colTotal = new TableColumn<>("TOTAL");
        colTotal.setCellValueFactory(
                new PropertyValueFactory<>("total")
        );

        TableColumn<Reserva, String> colEstado = new TableColumn<>("ESTADO");
        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado")
        );

        tablaReservas.getColumns().addAll(
                colId, colIngreso, colSalida, colTotal, colEstado
        );

        tablaReservas.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tablaReservas.setPrefHeight(220);

// Obtener las últimas 5 reservas
        List<Reserva> ultimasReservas = new ReservaDaoImpl().listar();

        tablaReservas.setItems(
                FXCollections.observableArrayList(
                        ultimasReservas.subList(
                                0, Math.min(5, ultimasReservas.size())
                        )
                )
        );

        VBox panelReservas = new VBox(15);
        panelReservas.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 20;"
        );

        panelReservas.getChildren().addAll(
                tituloReservas,
                tablaReservas
        );

// AGREGAR TODO AL DASHBOARD
        contenido.getChildren().addAll(
                bienvenida,
                subtitulo,
                tarjetas,
                panelOcupacion,
                panelReservas
        );

        panelContenido.getChildren().setAll(contenido);
    }

    private VBox crearTarjeta(String titulo, String valor) {

        VBox tarjeta = new VBox(12);
        tarjeta.setAlignment(Pos.CENTER_LEFT);
        tarjeta.setPrefWidth(190);
        tarjeta.setMinWidth(190);
        tarjeta.setMaxWidth(190);

        tarjeta.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 22;" +
                        "-fx-effect: dropshadow(gaussian, #D1D5DB, 8, 0.2, 0, 3);"
        );

        Label lblTitulo = new Label(titulo);
        lblTitulo.setWrapText(true);
        lblTitulo.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #64748B;"
        );

        Label lblValor = new Label(valor);
        lblValor.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #071827;"
        );

        tarjeta.getChildren().addAll(lblTitulo, lblValor);

        return tarjeta;
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
