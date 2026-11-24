package pe.edu.upeu.luxury12.interfaces;

import pe.edu.upeu.luxury12.Entidad.Cliente;

import java.util.List;

public interface  IClienteDao {

    public boolean registrar(Cliente c);

    public List<Cliente> listar();

    public boolean editar(Cliente c);

    public boolean eliminar(Cliente c);

    public Cliente buscarPorId(int id);

    public Cliente buscarPorDNI(String dni);
}
