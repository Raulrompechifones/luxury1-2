package pe.edu.upeu.luxury12.Entidad;

public class Cliente extends Persona {
    private int idCliente;
    private String telefono;
    private String email;
    private int cantidaP;

    public Cliente() {
    }

    public Cliente(int idCliente, String telefono, String email, int cantidaP, String nombre, String apellido, String dni) {
        super(nombre, apellido, dni);
        this.idCliente = idCliente;
        this.telefono = telefono;
        this.email = email;
        this.cantidaP = cantidaP;
    }



    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCantidaP() {
        return cantidaP;
    }

    public void setCantidaP(int cantidaP) {
        this.cantidaP = cantidaP;
    }

    @Override
    public String toString() {
        return getNombre() + " " + getApellido();
    }
}
