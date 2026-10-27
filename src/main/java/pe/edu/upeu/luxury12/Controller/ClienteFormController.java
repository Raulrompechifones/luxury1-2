
package pe.edu.upeu.luxury12.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import pe.edu.upeu.luxury12.Entidad.Cliente;
import pe.edu.upeu.luxury12.implementacion.ClienteDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IClienteDao;

public class ClienteFormController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDni;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPersonas;

    private final IClienteDao dao = new ClienteDaoImpl();
    private Cliente clienteEditar = null;


    private boolean guardado = false;

    public boolean isGuardado() {
        return guardado;
    }


    public void cargarCliente(Cliente cliente) {

        clienteEditar = cliente;

        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtDni.setText(cliente.getDni());
        txtTelefono.setText(cliente.getTelefono());
        txtEmail.setText(cliente.getEmail());
        txtPersonas.setText(
                String.valueOf(cliente.getCantidaP())
        );
    }

    @FXML
    private void guardarCliente() {

        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String personasTexto = txtPersonas.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty()
                || dni.isEmpty() || personasTexto.isEmpty()) {

            mostrarAlerta("Completa los campos obligatorios.");
            return;
        }

        int cantidad;

        try {
            cantidad = Integer.parseInt(personasTexto);

            if (cantidad <= 0) {
                mostrarAlerta(
                        "La cantidad de personas debe ser mayor que cero."
                );
                return;
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(
                    "La cantidad de personas debe ser un número."
            );
            return;
        }


        Cliente existente = dao.buscarPorDNI(dni);

        if (existente != null &&
                (clienteEditar == null ||
                        existente.getIdCliente() != clienteEditar.getIdCliente())) {

            mostrarAlerta("Este DNI ya está registrado.");
            return;
        }

        Cliente cliente = new Cliente(
                clienteEditar == null
                        ? 0
                        : clienteEditar.getIdCliente(),
                telefono,
                email,
                cantidad,
                nombre,
                apellido,
                dni
        );

        boolean resultado;

        if (clienteEditar == null) {
            resultado = dao.registrar(cliente);
        } else {
            resultado = dao.editar(cliente);
        }

        if (resultado) {
            guardado = true;
            cerrarVentana();
        } else {
            mostrarAlerta(
                    "No se pudo guardar la información del cliente."
            );
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("HOTEL LUXURY");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
