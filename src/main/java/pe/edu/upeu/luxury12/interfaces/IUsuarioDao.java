package pe.edu.upeu.luxury12.interfaces;

import pe.edu.upeu.luxury12.Entidad.Usuarios;

import java.util.List;

public interface IUsuarioDao {

    public boolean registrar(Usuarios u);

    public List<Usuarios> listar();

    public boolean editar(Usuarios u);

    public boolean eliminar(Usuarios u);

    public Usuarios buscarPorId(int id);

    public Usuarios login (String user, String password);
}
