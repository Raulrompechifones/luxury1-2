package pe.edu.upeu.luxury12.VistaControl;

import pe.edu.upeu.luxury12.utils.ClienteLectura;
import pe.edu.upeu.luxury12.utils.Lectura;

public class ClienteIndex {
    static Lectura leer = new Lectura();
    static ClienteLectura CL = new ClienteLectura();

    public void menuCliente() {
        int opcion;

        do {
            System.out.println("\n==============================");
            System.out.println("        MENU CLIENTE");
            System.out.println("==============================");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Editar cliente");
            System.out.println("4. Eliminar cliente");
            System.out.println("5. Buscar por DNI");
            System.out.println("6. Regresar");
            System.out.println("==============================");
            System.out.println("Seleccione una opcion:");

            opcion = leer.entero();

            switch (opcion) {
                case 1:
                    CL.leerRegistrarCliente();
                    break;
                case 2:
                    CL.leerListarCliente();
                    break;
                case 3:
                    CL.leerActualizarCliente();
                    break;
                case 4:
                    CL.leerEliminarCliente();
                    break;
                case 5:
                    CL.leerBuscarPorDniCliente();
                    break;
                case 6:
                    System.out.println("Regresando al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }

        } while (opcion != 6);
    }
}
