
package pe.edu.upeu.luxury12.Test;

import pe.edu.upeu.luxury12.Entidad.Cliente;
import pe.edu.upeu.luxury12.implementacion.ClienteDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IClienteDao;

import java.util.List;

public class TestCliente {

    static IClienteDao cl = new ClienteDaoImpl();

    // LISTAR CLIENTES
    public static void listar() {

        List<Cliente> lista = cl.listar();

        if (lista != null && !lista.isEmpty()) {

            for (Cliente cliente : lista) {

                System.out.println(
                        "ID_CLIENTE: " + cliente.getIdCliente()
                );

                System.out.println(
                        "NOMBRE: " + cliente.getNombre()
                );

                System.out.println(
                        "APELLIDO: " + cliente.getApellido()
                );

                System.out.println(
                        "DNI: " + cliente.getDni()
                );

                System.out.println(
                        "TELEFONO: " + cliente.getTelefono()
                );

                System.out.println(
                        "EMAIL: " + cliente.getEmail()
                );

                System.out.println(
                        "CANTIDAD PERSONAS: " + cliente.getCantidaP()
                );

                System.out.println("--------------------------");
            }

        } else {
            System.out.println("No hay clientes registrados");
        }
    }


    public static void insertar() {

        Cliente c = new Cliente();

        c.setNombre("Arianna");
        c.setApellido("sanchez");
        c.setDni("76547895");
        c.setTelefono("945695236");
        c.setEmail("arianna@gmail.com");
        c.setCantidaP(2);

        boolean result = cl.registrar(c);

        if (result) {
            System.out.println("CLIENTE REGISTRADO CORRECTAMENTE");
        } else {
            System.out.println("ERROR AL REGISTRAR CLIENTE");
        }
    }


    public static void eliminar(int idCliente) {

        Cliente c = new Cliente();

        c.setIdCliente(idCliente);

        boolean result = cl.eliminar(c);

        if (result) {
            System.out.println("CLIENTE ELIMINADO CORRECTAMENTE");
        } else {
            System.out.println("ERROR AL ELIMINAR CLIENTE");
        }
    }


    public static void buscarPorDNI(String dni) {

        Cliente c = cl.buscarPorDNI(dni);

        if (c != null) {
            System.out.println("CLIENTE ENCONTRADO");
            System.out.println("ID: " + c.getIdCliente());
            System.out.println("NOMBRE: " + c.getNombre());
            System.out.println("APELLIDO: " + c.getApellido());
            System.out.println("DNI: " + c.getDni());
        } else {
            System.out.println("CLIENTE NO ENCONTRADO");
        }
    }

    public static void main(String[] args) {

        System.out.println("***************");
        System.out.println("LISTA DE CLIENTES");
        listar();

        System.out.println("***************");
        System.out.println("INSERTAR CLIENTE");
        insertar();

        System.out.println("***************");
        System.out.println("LISTA ACTUALIZADA");
        listar();

        System.out.println("***************");
        System.out.println("BUSCAR CLIENTE");
        buscarPorDNI("76547895");


    }
}
