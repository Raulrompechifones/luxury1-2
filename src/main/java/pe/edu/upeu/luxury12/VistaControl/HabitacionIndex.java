package pe.edu.upeu.luxury12.VistaControl;

import pe.edu.upeu.luxury12.utils.HabitacionLectura;
import pe.edu.upeu.luxury12.utils.Lectura;

public class HabitacionIndex {
    static Lectura leer = new Lectura();
    static HabitacionLectura HL = new HabitacionLectura();

    public void menuHabitacion() {
        int opcion;

        do {
            System.out.println("\n==============================");
            System.out.println("      MENU HABITACION");
            System.out.println("==============================");
            System.out.println("1. Registrar habitacion");
            System.out.println("2. Listar habitaciones");
            System.out.println("3. Editar habitacion");
            System.out.println("4. Eliminar habitacion");
            System.out.println("5. Regresar");
            System.out.println("==============================");
            System.out.println("Seleccione una opcion:");

            opcion = leer.entero();

            switch (opcion) {
                case 1:
                    HL.leerRegistrarHabitacion();
                    break;
                case 2:
                    HL.leerListarHabitacion();
                    break;
                case 3:
                    HL.leerActualizarHabitacion();
                    break;
                case 4:
                    HL.leerEliminarHabitacion();
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
