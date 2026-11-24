package pe.edu.upeu.luxury12.VistaControl;

import pe.edu.upeu.luxury12.utils.Lectura;
import pe.edu.upeu.luxury12.utils.ReservaLectura;

public class ReservaIndex {
    static Lectura leer = new Lectura();
    static ReservaLectura RL = new ReservaLectura();

    public void menuReserva() {
        int opcion;

        do {
            System.out.println("\n==============================");
            System.out.println("        MENU RESERVA");
            System.out.println("==============================");
            System.out.println("1. Registrar reserva");
            System.out.println("2. Listar reservas");
            System.out.println("3. Cambiar estado de reserva");
            System.out.println("4. Eliminar reserva");
            System.out.println("5. Regresar");
            System.out.println("==============================");
            System.out.println("Seleccione una opcion:");

            opcion = leer.entero();

            switch (opcion) {
                case 1:
                    RL.leerRegistrarReserva();
                    break;
                case 2:
                    RL.leerListarReserva();
                    break;
                case 3:
                    RL.leerActualizarEstadoReserva();
                    break;
                case 4:
                    RL.leerEliminarReserva();
                    break;
                case 5:
                    System.out.println("Regresando al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }

        } while (opcion != 5);
    }
}
