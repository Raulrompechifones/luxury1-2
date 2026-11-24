package pe.edu.upeu.luxury12.interfaces;

import pe.edu.upeu.luxury12.Entidad.Habitacion;

import java.util.List;

public interface IHabitacionDao {

    public boolean registrar(Habitacion h);

    public List<Habitacion> listar();

    public boolean editar(Habitacion h);

    public boolean eliminar(Habitacion h);

    public Habitacion buscarPorId(int id);

    public Habitacion buscarPorCodigo (String cod_hab);

}
