
package pe.edu.upeu.luxury12.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import pe.edu.upeu.luxury12.Entidad.Habitacion;
import pe.edu.upeu.luxury12.Entidad.TipodeHabitacion;
import pe.edu.upeu.luxury12.implementacion.HabitacionDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IHabitacionDao;

public class HabitacionFormController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNumero;
    @FXML private TextField txtPrecio;

    @FXML private ComboBox<TipodeHabitacion> cmbTipo;
    @FXML private ComboBox<String> cmbEstado;

    private final IHabitacionDao dao = new HabitacionDaoImpl();

    private Habitacion habitacionEditar;
    private boolean guardado = false;

    public boolean isGuardado() {
        return guardado;
    }

    @FXML
    public void initialize() {
        cmbTipo.setItems(
                FXCollections.observableArrayList(
                        TipodeHabitacion.values()
                )
        );

        cmbEstado.setItems(
                FXCollections.observableArrayList(
                        "Disponible",
                        "Ocupada",
                        "Mantenimiento"
                )
        );

        cmbEstado.setValue("Disponible");
    }

    public void cargarHabitacion(Habitacion habitacion) {
        habitacionEditar = habitacion;

        txtCodigo.setText(habitacion.getCod_hab());
        txtNumero.setText(habitacion.getNum_hab());
        txtPrecio.setText(String.valueOf(habitacion.getPrecio()));

        cmbTipo.setValue(habitacion.getTipoHabitacion());
        cmbEstado.setValue(habitacion.getEstado());
    }

    @FXML
    private void guardarHabitacion() {

        String codigo = txtCodigo.getText().trim();
        String numero = txtNumero.getText().trim();
        String precioTexto = txtPrecio.getText().trim();

        TipodeHabitacion tipo = cmbTipo.getValue();
        String estado = cmbEstado.getValue();

        if (codigo.isEmpty() || numero.isEmpty()
                || precioTexto.isEmpty()
                || tipo == null || estado == null) {
            mostrarAlerta("Completa todos los campos.");
            return;
        }

        double precio;

        try {
            precio = Double.parseDouble(precioTexto);

            if (!Double.isFinite(precio) || precio <= 0) {
                mostrarAlerta("El precio debe ser mayor que cero.");
                return;
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Ingresa un precio válido.");
            return;
        }

        Habitacion existente = dao.buscarPorCodigo(codigo);

        if (existente != null &&
                (habitacionEditar == null ||
                        existente.getIdHabitacion() !=
                                habitacionEditar.getIdHabitacion())) {
            mostrarAlerta("Este código de habitación ya existe.");
            return;
        }

        Habitacion habitacion = new Habitacion(
                habitacionEditar == null
                        ? 0
                        : habitacionEditar.getIdHabitacion(),
                codigo,
                numero,
                tipo,
                precio,
                estado
        );

        boolean resultado;

        if (habitacionEditar == null) {
            resultado = dao.registrar(habitacion);
        } else {
            resultado = dao.editar(habitacion);
        }

        if (resultado) {
            guardado = true;
            cerrarVentana();
        } else {
            mostrarAlerta("No se pudo guardar la habitación. Verifica que el número y código no estén repetidos.");
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage ventana = (Stage) txtCodigo.getScene().getWindow();
        ventana.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("HOTEL LUXURY");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
