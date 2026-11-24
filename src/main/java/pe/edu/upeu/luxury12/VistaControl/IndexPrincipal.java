package pe.edu.upeu.luxury12.VistaControl;

import pe.edu.upeu.luxury12.utils.Lectura;

public class IndexPrincipal {
    static Lectura leer = new Lectura();

    public void iniciar() {
        int opcion;

        do {
            System.out.println("\n==============================");
            System.out.println("  BIENVENIDO A LUXURY HOTEL");
            System.out.println("==============================");
            System.out.println("1. Cliente");
            System.out.println("2. Usuario");
            System.out.println("3. Habitacion");
            System.out.println("4. Reserva");
            System.out.println("5. Salir");
            System.out.println("==============================");
            System.out.println("Ingrese una opcion:");

            opcion = leer.entero();

            switch (opcion) {
                case 1:
                    ClienteIndex ci = new ClienteIndex();
                    ci.menuCliente();
                    break;
                case 2:
                    UsuarioIndex ui = new UsuarioIndex();
                    ui.menuUsuario();
                    break;
                case 3:
                    HabitacionIndex hi = new HabitacionIndex();
                    hi.menuHabitacion();
                    break;
                case 4:
                    ReservaIndex ri = new ReservaIndex();
                    ri.menuReserva();
                    break;
                case 5:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }

        } while (opcion != 5);
    }

    public static void main(String[] args) {
        IndexPrincipal index = new IndexPrincipal();
        index.iniciar();
    }
}
