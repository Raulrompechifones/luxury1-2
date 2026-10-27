package pe.edu.upeu.luxury12.Entidad;

public class Usuarios extends Persona {
    private int idUsuario;
    private String usuario;
    private String clave;
    private String tipoUsuario;
    private String estado;

    public Usuarios() {
    }

    public Usuarios(int idUsuario, String usuario, String clave, String tipoUsuario, String nombre, String apellido, String dni) {
        super(nombre, apellido, dni);
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.clave = clave;
        this.tipoUsuario = tipoUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
