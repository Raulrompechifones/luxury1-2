package pe.edu.upeu.luxury12.utils;

import pe.edu.upeu.luxury12.Entidad.Habitacion;
import pe.edu.upeu.luxury12.Entidad.TipodeHabitacion;
import pe.edu.upeu.luxury12.implementacion.HabitacionDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IHabitacionDao;

public class HabitacionLectura {
    static Lectura leer = new Lectura();
    static IHabitacionDao dao = new HabitacionDaoImpl();

    static final double PRECIO_SIMPLE = 100.0;
    static final double PRECIO_DOBLE = 150.0;
    static final double PRECIO_SUITE = 250.0;

    public void leerRegistrarHabitacion() {
        System.out.println("***REGISTRAR HABITACION***");

        System.out.println("ID_HABITACION: ");
        int idHabitacion = leer.entero();

        System.out.println("CODIGO (ej: H-101): ");
        String cod_hab = leer.cadena();

        System.out.println("NUMERO: ");
        String num_hab = leer.cadena();

        System.out.println("TIPO DE HABITACION:");
        System.out.println("1. Simple");
        System.out.println("2. Doble");
        System.out.println("3. Suite");
        int opcion = leer.entero();

        TipodeHabitacion tipo;
        double precio;
        switch (opcion) {
            case 1:
                tipo = TipodeHabitacion.SIMPLE;
                precio = PRECIO_SIMPLE;
                break;
            case 2:
                tipo = TipodeHabitacion.DOBLE;
                precio = PRECIO_DOBLE;
                break;
            case 3:
                tipo = TipodeHabitacion.SUITE;
                precio = PRECIO_SUITE;
                break;
            default:
                tipo = TipodeHabitacion.SIMPLE;
                precio = PRECIO_SIMPLE;
                break;
        }
        System.out.println("PRECIO HABITACION: " + precio);

        System.out.println("ESTADO:");
        System.out.println("1. Disponible");
        System.out.println("2. Ocupado");
        int opcionEstado = leer.entero();
        String estado;
        switch (opcionEstado) {
            case 1:
                estado = "Disponible";
                break;
            case 2:
                estado = "Ocupado";
                break;
            default:
                System.out.println("Opcion invalida, se pone Disponible por defecto");
                estado = "Disponible";
                break;
        }

        Habitacion h = new Habitacion(idHabitacion, cod_hab, num_hab, tipo, precio, estado);
        dao.registrar(h);
    }

    public void leerListarHabitacion() {
        if (dao.listar().isEmpty()) {
            System.out.println("No hay habitaciones registradas.");
        } else {
            System.out.println("----- LISTA DE HABITACIONES -----");
            for (Habitacion h : dao.listar()) {
                System.out.println("ID: " + h.getIdHabitacion());
                System.out.println("Codigo: " + h.getCod_hab());
                System.out.println("Numero: " + h.getNum_hab());
                System.out.println("Tipo: " + h.getTipoHabitacion());
                System.out.println("Precio: " + h.getPrecio());
                System.out.println("Estado: " + h.getEstado());
                System.out.println("-------------------------------");
            }
        }
    }

    public void leerActualizarHabitacion() {
        System.out.println("Ingrese el Id de la habitacion a editar: ");
        int id = leer.entero();
        Habitacion anterior = dao.buscarPorId(id);
        if (anterior != null) {
            System.out.println("CODIGO: ");
            String cod_hab = leer.cadena();

            System.out.println("NUMERO: ");
            String num_hab = leer.cadena();

            System.out.println("TIPO DE HABITACION:");
            System.out.println("1. Simple");
            System.out.println("2. Doble");
            System.out.println("3. Suite");
            int opcion = leer.entero();
            TipodeHabitacion tipo;
            switch (opcion) {
                case 1:
                    tipo = TipodeHabitacion.SIMPLE;
                    break;
                case 2:
                    tipo = TipodeHabitacion.DOBLE;
                    break;
                case 3:
                    tipo = TipodeHabitacion.SUITE;
                    break;
                default:
                    tipo = anterior.getTipoHabitacion();
                    break;
            }

            System.out.println("PRECIO: ");
            Double precio = leer.decimal();

            System.out.println("ESTADO: ");
            String estado = leer.cadena();

            Habitacion nuevo = new Habitacion(id, cod_hab, num_hab, tipo, precio, estado);
            dao.editar(nuevo);
        } else {
            System.out.println("No existe una habitacion con ese Id");
        }
    }

    public void leerEliminarHabitacion() {
        System.out.println("Ingrese el Id de la habitacion a eliminar: ");
        int id = leer.entero();
        Habitacion h = dao.buscarPorId(id);
        if (h != null) {
            dao.eliminar(h);
        } else {
            System.out.println("No existe una habitacion con ese Id");
        }
    }
}
