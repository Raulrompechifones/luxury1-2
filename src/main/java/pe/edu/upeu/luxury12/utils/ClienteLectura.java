package pe.edu.upeu.luxury12.utils;

import pe.edu.upeu.luxury12.Entidad.Cliente;
import pe.edu.upeu.luxury12.implementacion.ClienteDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IClienteDao;

public class ClienteLectura {

    static Lectura leer = new Lectura();
    static IClienteDao dao = new ClienteDaoImpl();

    public void leerRegistrarCliente() {
        System.out.println("***REGISTRAR CLIENTE***");

        System.out.println("ID_CLIENTE: ");
        int idCliente = leer.entero();

        System.out.println("NOMBRE: ");
        String nombre = leer.cadena();

        System.out.println("APELLIDO: ");
        String apellido = leer.cadena();

        System.out.println("DNI: ");
        String dni = leer.cadena();

        System.out.println("TELEFONO: ");
        String telefono = leer.cadena();

        System.out.println("EMAIL: ");
        String email = leer.cadena();

        System.out.println("CANTIDAD DE PERSONAS: ");
        int cantidaP = leer.entero();

        Cliente c = new Cliente(idCliente, telefono, email, cantidaP, nombre, apellido, dni);
        dao.registrar(c);
    }

    public void leerListarCliente() {
        if (dao.listar().isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            System.out.println("----- LISTA DE CLIENTES -----");
            for (Cliente c : dao.listar()) {
                System.out.println("ID: " + c.getIdCliente());
                System.out.println("Nombre: " + c.getNombre());
                System.out.println("Apellido: " + c.getApellido());
                System.out.println("DNI: " + c.getDni());
                System.out.println("Telefono: " + c.getTelefono());
                System.out.println("Email: " + c.getEmail());
                System.out.println("Cantidad de personas: " + c.getCantidaP());
                System.out.println("-----------------------------");
            }
        }
    }

    public void leerActualizarCliente() {
        System.out.println("Ingrese el Id del cliente a editar: ");
        int id = leer.entero();
        Cliente anterior = dao.buscarPorId(id);
        if (anterior != null) {
            System.out.println("NOMBRE: ");
            String nombre = leer.cadena();

            System.out.println("APELLIDO: ");
            String apellido = leer.cadena();

            System.out.println("DNI: ");
            String dni = leer.cadena();

            System.out.println("TELEFONO: ");
            String telefono = leer.cadena();

            System.out.println("EMAIL: ");
            String email = leer.cadena();

            System.out.println("CANTIDAD DE PERSONAS: ");
            int cantidaP = leer.entero();

            Cliente nuevo = new Cliente(id, telefono, email, cantidaP, nombre, apellido, dni);
            dao.editar(nuevo);
        } else {
            System.out.println("No existe un cliente con ese Id");
        }
    }

    public void leerEliminarCliente() {
        System.out.println("Ingrese el Id del cliente a eliminar: ");
        int id = leer.entero();
        Cliente c = dao.buscarPorId(id);
        if (c != null) {
            dao.eliminar(c);
        } else {
            System.out.println("No existe un cliente con ese Id");
        }
    }

    public void leerBuscarPorDniCliente() {
        System.out.println("Ingrese el DNI a buscar: ");
        String dni = leer.cadena();
        Cliente c = dao.buscarPorDNI(dni);
        if (c != null) {
            System.out.println("----- CLIENTE ENCONTRADO -----");
            System.out.println("ID: " + c.getIdCliente());
            System.out.println("Nombre: " + c.getNombre());
            System.out.println("Apellido: " + c.getApellido());
            System.out.println("Telefono: " + c.getTelefono());
        } else {
            System.out.println("No se encontró un cliente con el DNI: " + dni);
        }
    }
}
