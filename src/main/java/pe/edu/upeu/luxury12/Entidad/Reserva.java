package pe.edu.upeu.luxury12.Entidad;

import java.util.Date;

public class Reserva {
    private int idReserva;
    private Date fecha_ingreso;
    private Date fecha_reserva;
    private Date fecha_salida;
    private Double total;
    private String estado;
    private Cliente cliente;
    private Usuarios usuarios;
    private Habitacion habitacion;

    public Reserva() {
    }

    public Reserva(int idReserva, Date fecha_ingreso, Date fecha_reserva, Date fecha_salida, Double total, String estado, Cliente cliente, Usuarios usuarios, Habitacion habitacion) {
        this.idReserva = idReserva;
        this.fecha_ingreso = fecha_ingreso;
        this.fecha_reserva = fecha_reserva;
        this.fecha_salida = fecha_salida;
        this.total = total;
        this.estado = estado;
        this.cliente = cliente;
        this.usuarios = usuarios;
        this.habitacion = habitacion;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public Date getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(Date fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public Date getFecha_reserva() {
        return fecha_reserva;
    }

    public void setFecha_reserva(Date fecha_reserva) {
        this.fecha_reserva = fecha_reserva;
    }

    public Date getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(Date fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

}
