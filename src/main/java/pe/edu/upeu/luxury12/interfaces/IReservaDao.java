package pe.edu.upeu.luxury12.interfaces;

import pe.edu.upeu.luxury12.Entidad.Reserva;

import java.util.List;

public interface IReservaDao {

    public boolean registrar(Reserva r);

    public List<Reserva> listar();

    public boolean editar(Reserva r);

    public boolean eliminar(Reserva r);

    public Reserva buscarPorId(int id);

    public boolean act_estado(Reserva r);

    public boolean validarHabitacion(Reserva r);
}
