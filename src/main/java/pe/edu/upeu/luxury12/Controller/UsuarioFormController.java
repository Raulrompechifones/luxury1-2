package pe.edu.upeu.luxury12.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pe.edu.upeu.luxury12.Entidad.Usuarios;
import pe.edu.upeu.luxury12.implementacion.UsuariosDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IUsuarioDao;

public class UsuarioFormController {

    @FXML private Label lblTitulo;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDni;
    @FXML private TextField txtUsuario;

    @FXML private PasswordField txtClave;
    @FXML private PasswordField txtConfirmarClave;

    @FXML private ComboBox<String> cmbRol;

    private final IUsuarioDao usuarioDao = new UsuariosDaoImpl();

    private Usuarios usuarioEditar;

    @FXML
    public void initialize() {

        cmbRol.getItems().addAll(
                "ADMINISTRADOR",
                "RECEPCIONISTA",
                "ENCARGADO"
        );

        cmbRol.setValue("RECEPCIONISTA");
    }


    public void cargarUsuario(Usuarios usuario) {

        this.usuarioEditar = usuario;

        lblTitulo.setText("EDITAR USUARIO");

        txtNombre.setText(usuario.getNombre());
        txtApellido.setText(usuario.getApellido());
        txtDni.setText(usuario.getDni());
        txtUsuario.setText(usuario.getUsuario());

        cmbRol.setValue(usuario.getTipoUsuario());

        // En edición no se cambia la contraseña.
        txtClave.setDisable(true);
        txtConfirmarClave.setDisable(true);
    }


    @FXML
    private void guardar() {

        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni = txtDni.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String rol = cmbRol.getValue();

        if (nombre.isEmpty() || apellido.isEmpty() ||
                dni.isEmpty() || usuario.isEmpty() || rol == null) {

            mostrarMensaje("Complete todos los campos obligatorios.");
            return;
        }

        if (!dni.matches("\\d{8}")) {
            mostrarMensaje("El DNI debe tener 8 números.");
            return;
        }

        Usuarios u = new Usuarios();

        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setDni(dni);
        u.setUsuario(usuario);
        u.setTipoUsuario(rol);

        boolean resultado;

        if (usuarioEditar == null) {

            String clave = txtClave.getText();
            String confirmar = txtConfirmarClave.getText();

            if (clave.isEmpty() || confirmar.isEmpty()) {
                mostrarMensaje("Ingrese y confirme la contraseña.");
                return;
            }

            if (clave.length() < 8) {
                mostrarMensaje("La contraseña debe tener al menos 8 caracteres.");
                return;
            }

            if (!clave.equals(confirmar)) {
                mostrarMensaje("Las contraseñas no coinciden.");
                return;
            }

            u.setClave(clave);

            resultado = usuarioDao.registrar(u);

        } else {

            u.setIdUsuario(usuarioEditar.getIdUsuario());

            resultado = usuarioDao.editar(u);
        }

        if (resultado) {

            if (usuarioEditar == null) {
                mostrarMensaje("Usuario registrado correctamente.");
            } else {
                mostrarMensaje("Usuario actualizado correctamente.");
            }

            cerrarVentana();

        } else {
            mostrarMensaje(
                    "No se pudo guardar. Verifique que el DNI o usuario no estén repetidos."
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