package pe.edu.upeu.luxury12.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pe.edu.upeu.luxury12.Entidad.Usuarios;
import pe.edu.upeu.luxury12.implementacion.UsuariosDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IUsuarioDao;

public class RegistroUsuarioController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDni;
    @FXML private TextField txtUsuario;

    @FXML private PasswordField txtClave;
    @FXML private PasswordField txtConfirmarClave;

    private final IUsuarioDao usuarioDao = new UsuariosDaoImpl();

    @FXML
    private void registrar() {

        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni = txtDni.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String clave = txtClave.getText();
        String confirmar = txtConfirmarClave.getText();

        if (nombre.isEmpty() || apellido.isEmpty() ||
                dni.isEmpty() || usuario.isEmpty() ||
                clave.isEmpty() || confirmar.isEmpty()) {

            mostrarMensaje("Complete todos los campos.");
            return;
        }

        if (!dni.matches("\\d{8}")) {
            mostrarMensaje("El DNI debe tener 8 números.");
            return;
        }

        if (!clave.equals(confirmar)) {
            mostrarMensaje("Las contraseñas no coinciden.");
            return;
        }

        if (clave.length() < 8) {
            mostrarMensaje("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        Usuarios nuevo = new Usuarios();

        nuevo.setNombre(nombre);
        nuevo.setApellido(apellido);
        nuevo.setDni(dni);
        nuevo.setUsuario(usuario);
        nuevo.setClave(clave);

        // El registro público solo crea cuentas de recepcionista.
        nuevo.setTipoUsuario("RECEPCIONISTA");

        if (usuarioDao.registrar(nuevo)) {

            mostrarMensaje("Usuario registrado correctamente.");

            cerrarVentana();

        } else {
            mostrarMensaje(
                    "No se pudo registrar. Verifique que el DNI y usuario no estén repetidos."
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

    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("HOTEL LUXURY");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}