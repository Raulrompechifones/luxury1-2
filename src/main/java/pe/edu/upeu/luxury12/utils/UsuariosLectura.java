package pe.edu.upeu.luxury12.utils;

import pe.edu.upeu.luxury12.Entidad.Usuarios;
import pe.edu.upeu.luxury12.implementacion.UsuariosDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IUsuarioDao;

public class UsuariosLectura {
    static Lectura leer = new Lectura();
    static IUsuarioDao dao = new UsuariosDaoImpl();

    public void leerRegistrarUsuario() {
        System.out.println("***REGISTRAR USUARIO***");

        System.out.println("ID_USUARIO: ");
        int idUsuario = leer.entero();

        System.out.println("NOMBRE: ");
        String nombre = leer.cadena();

        System.out.println("APELLIDO: ");
        String apellido = leer.cadena();

        System.out.println("DNI: ");
        String dni = leer.cadena();

        System.out.println("USUARIO (login): ");
        String usuario = leer.cadena();

        System.out.println("CLAVE: ");
        String clave = leer.cadena();

        System.out.println("TIPO DE USUARIO (ej: ADMIN, RECEPCIONISTA): ");
        String tipoUsuario = leer.cadena();

        Usuarios u = new Usuarios(idUsuario, usuario, clave, tipoUsuario, nombre, apellido, dni);
        dao.registrar(u);
    }

    public void leerListarUsuario() {
        if (dao.listar().isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            System.out.println("----- LISTA DE USUARIOS -----");
            for (Usuarios u : dao.listar()) {
                System.out.println("ID: " + u.getIdUsuario());
                System.out.println("Nombre: " + u.getNombre());
                System.out.println("Apellido: " + u.getApellido());
                System.out.println("Usuario: " + u.getUsuario());
                System.out.println("Tipo: " + u.getTipoUsuario());
                System.out.println("-----------------------------");
            }
        }
    }

    public void leerActualizarUsuario() {
        System.out.println("Ingrese el Id del usuario a editar: ");
        int id = leer.entero();
        Usuarios anterior = dao.buscarPorId(id);
        if (anterior != null) {
            System.out.println("NOMBRE: ");
            String nombre = leer.cadena();

            System.out.println("APELLIDO: ");
            String apellido = leer.cadena();

            System.out.println("DNI: ");
            String dni = leer.cadena();

            System.out.println("USUARIO (login): ");
            String usuario = leer.cadena();

            System.out.println("CLAVE: ");
            String clave = leer.cadena();

            System.out.println("TIPO DE USUARIO: ");
            String tipoUsuario = leer.cadena();

            Usuarios nuevo = new Usuarios(id, usuario, clave, tipoUsuario, nombre, apellido, dni);
            dao.editar(nuevo);
        } else {
            System.out.println("No existe un usuario con ese Id");
        }
    }

    public void leerEliminarUsuario() {
        System.out.println("Ingrese el Id del usuario a eliminar: ");
        int id = leer.entero();
        Usuarios u = dao.buscarPorId(id);
        if (u != null) {
            dao.eliminar(u);
        } else {
            System.out.println("No existe un usuario con ese Id");
        }
    }

    public void leerLogin() {
        System.out.println("USUARIO: ");
        String user = leer.cadena();
        System.out.println("CLAVE: ");
        String pass = leer.cadena();

        Usuarios u = dao.login(user, pass);
        if (u != null) {
            System.out.println("----- LOGIN EXITOSO! Bienvenido " + u.getNombre() + " -----");
        } else {
            System.out.println("Usuario o clave incorrectos");
        }
    }
}
