package pe.edu.upeu.luxury12.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pe.edu.upeu.luxury12.Entidad.Cliente;
import pe.edu.upeu.luxury12.implementacion.ClienteDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IClienteDao;

import java.io.IOException;
import java.util.Optional;

public class ClienteController {

    @FXML private TableView<Cliente> tablaClientes;

    @FXML private TableColumn<Cliente, Number> colId;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colApellido;
    @FXML private TableColumn<Cliente, String> colDni;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, Number> colPersonas;
    @FXML private TableColumn<Cliente, Void> colAcciones;

    @FXML private TextField txtBuscar;

    private final IClienteDao clienteDao = new ClienteDaoImpl();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdCliente()));

        colNombre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombre()));

        colApellido.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getApellido()));

        colDni.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDni()));

        colTelefono.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTelefono()));

        colEmail.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEmail()));

        if (colPersonas != null) {
            colPersonas.setCellValueFactory(data ->
                    new SimpleIntegerProperty(data.getValue().getCantidaP()));
        }

        configurarColumnaAcciones();
        listarClientes();
    }

    private void configurarColumnaAcciones() {
        if (colAcciones == null) {
            return;
        }

        colAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("✎");
            private final Button btnEliminar = new Button("🗑");
            private final HBox contenedor = new HBox(10, btnEditar, btnEliminar);

            {
                contenedor.setAlignment(Pos.CENTER);
                btnEditar.setStyle("-fx-background-color: transparent; -fx-text-fill: #B48A3E; -fx-font-size: 15px; -fx-cursor: hand;");
                btnEliminar.setStyle("-fx-background-color: transparent; -fx-text-fill: #DC2626; -fx-font-size: 15px; -fx-cursor: hand;");

                btnEditar.setOnAction(event -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    if (cliente != null) {
                        tablaClientes.getSelectionModel().select(cliente);
                        editarCliente();
                    }
                });

                btnEliminar.setOnAction(event -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    if (cliente != null) {
                        tablaClientes.getSelectionModel().select(cliente);
                        eliminarCliente();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(contenedor);
                }
            }
        });
    }

    @FXML
    private void listarClientes() {
        tablaClientes.setItems(
                FXCollections.observableArrayList(clienteDao.listar())
        );
    }

    @FXML
    private void actualizarClientes() {
        listarClientes();
    }

    @FXML
    private void buscarCliente() {
        String dni = txtBuscar.getText().trim();

        if (dni.isEmpty()) {
            listarClientes();
            return;
        }

        Cliente cliente = clienteDao.buscarPorDNI(dni);

        if (cliente != null) {
            tablaClientes.setItems(FXCollections.observableArrayList(cliente));
        } else {
            tablaClientes.getItems().clear();
            mostrarAlerta("Cliente no encontrado");
        }
    }

    @FXML
    private void registrarCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pe/edu/upeu/luxury12/view/view/cliente-form.fxml")
            );
            Parent root = loader.load();

            ClienteFormController controller = loader.getController();

            Stage ventana = new Stage();
            ventana.setTitle("HOTEL LUXURY - Registrar Cliente");
            ventana.setScene(new Scene(root));
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setResizable(false);

            ventana.showAndWait();

            if (controller.isGuardado()) {
                listarClientes();
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir el formulario de clientes.");
        }
    }

    @FXML
    private void editarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Selecciona un cliente para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pe/edu/upeu/luxury12/view/view/cliente-form.fxml")
            );
            Parent root = loader.load();

            ClienteFormController controller = loader.getController();
            controller.cargarCliente(seleccionado);

            Stage ventana = new Stage();
            ventana.setTitle("HOTEL LUXURY - Editar Cliente");
            ventana.setScene(new Scene(root));
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setResizable(false);

            ventana.showAndWait();

            if (controller.isGuardado()) {
                listarClientes();
                mostrarAlerta("Cliente actualizado correctamente.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir el formulario de edición.");
        }
    }

    @FXML
    private void eliminarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Selecciona un cliente para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("HOTEL LUXURY");
        confirmacion.setHeaderText("Eliminar cliente");
        confirmacion.setContentText(
                "¿Estás seguro de eliminar a "
                        + seleccionado.getNombre()
                        + " "
                        + seleccionado.getApellido()
                        + "?"
        );

        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            boolean eliminado = clienteDao.eliminar(seleccionado);

            if (eliminado) {
                listarClientes();
                mostrarAlerta("Cliente eliminado correctamente.");
            } else {
                mostrarAlerta("No se pudo eliminar el cliente.");
            }
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("HOTEL LUXURY");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}