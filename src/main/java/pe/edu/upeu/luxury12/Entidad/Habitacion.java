package pe.edu.upeu.luxury12.Entidad;

public class Habitacion {
    private int idHabitacion;
    private String cod_hab;
    private String num_hab;
    private TipodeHabitacion tipodeHabitacion;
    private Double precio;
    private String estado;

    public Habitacion() {
    }

    public Habitacion(int idHabitacion, String cod_hab, String num_hab, TipodeHabitacion tipoHabitacion, Double precio, String estado) {
        this.idHabitacion = idHabitacion;
        this.cod_hab = cod_hab;
        this.num_hab = num_hab;
        this.tipodeHabitacion = tipoHabitacion;
        this.precio = precio;
        this.estado = estado;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public String getCod_hab() {
        return cod_hab;
    }

    public void setCod_hab(String cod_hab) {
        this.cod_hab = cod_hab;
    }

    public String getNum_hab() {
        return num_hab;
    }

    public void setNum_hab(String num_hab) {
        this.num_hab = num_hab;
    }

    public TipodeHabitacion getTipoHabitacion() {
        return tipodeHabitacion;
    }

    public void setTipoHabitacion(TipodeHabitacion tipoHabitacion) {
        this.tipodeHabitacion = tipoHabitacion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return getNum_hab() + " - " +
                getTipoHabitacion() + " - S/ " +
                getPrecio();
    }
}
