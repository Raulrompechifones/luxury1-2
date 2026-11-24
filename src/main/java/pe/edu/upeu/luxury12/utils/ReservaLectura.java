package pe.edu.upeu.luxury12.utils;

import pe.edu.upeu.luxury12.Entidad.Cliente;
import pe.edu.upeu.luxury12.Entidad.Habitacion;
import pe.edu.upeu.luxury12.Entidad.Reserva;
import pe.edu.upeu.luxury12.Entidad.Usuarios;
import pe.edu.upeu.luxury12.implementacion.ClienteDaoImpl;
import pe.edu.upeu.luxury12.implementacion.HabitacionDaoImpl;
import pe.edu.upeu.luxury12.implementacion.ReservaDaoImpl;
import pe.edu.upeu.luxury12.implementacion.UsuariosDaoImpl;
import pe.edu.upeu.luxury12.interfaces.IClienteDao;
import pe.edu.upeu.luxury12.interfaces.IHabitacionDao;
import pe.edu.upeu.luxury12.interfaces.IReservaDao;
import pe.edu.upeu.luxury12.interfaces.IUsuarioDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservaLectura {
    static Lectura leer = new Lectura();
    static IClienteDao clienteDao = new ClienteDaoImpl();
    static IUsuarioDao usuarioDao = new UsuariosDaoImpl();
    static IHabitacionDao habitacionDao = new HabitacionDaoImpl();
    static IReservaDao reservaDao = new ReservaDaoImpl();
    static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    public void leerRegistrarReserva() {
        System.out.println("***REGISTRAR RESERVA***");

        System.out.println("ID_RESERVA: ");
        int idReserva = leer.entero();

        System.out.println("Buscar Cliente por Id: ");
        int idCliente = leer.entero();
        Cliente cliente = clienteDao.buscarPorId(idCliente);
        if (cliente == null) {
            System.out.println("No existe ese cliente, se cancela el registro");
            return;
        }

        System.out.println("Buscar Usuario por Id: ");
        int idUsuario = leer.entero();
        Usuarios usuario = usuarioDao.buscarPorId(idUsuario);
        if (usuario == null) {
            System.out.println("No existe ese usuario, se cancela el registro");
            return;
        }

        System.out.println("Buscar Habitacion por Id: ");
        int idHabitacion = leer.entero();
        Habitacion habitacion = habitacionDao.buscarPorId(idHabitacion);
        if (habitacion == null) {
            System.out.println("No existe esa habitacion, se cancela el registro");
            return;
        }

        System.out.println("FECHA_INGRESO (ejemplo 10/09/2026): ");
        Date fechaIngreso;
        try {
            fechaIngreso = formato.parse(leer.cadena());
        } catch (ParseException ex) {
            System.out.println("La fecha esta mal escrita, se cancela el registro");
            return;
        }

        System.out.println("FECHA_SALIDA (ejemplo 15/09/2026): ");
        Date fechaSalida;
        try {
            fechaSalida = formato.parse(leer.cadena());
        } catch (ParseException ex) {
            System.out.println("La fecha esta mal escrita, se cancela el registro");
            return;
        }

        System.out.println("ESTADO (R = Reservada, O = Ocupada, C = Cancelada): ");
        String estado = leer.cadena();

        Reserva r = new Reserva(idReserva, fechaIngreso, null, fechaSalida, 0.0, estado, cliente, usuario, habitacion);


        if (reservaDao.validarHabitacion(r)) {
            System.out.println("Esa habitacion ya esta reservada/ocupada en esas fechas. No se registro.");
            return;
        }


        long milisegundos = fechaSalida.getTime() - fechaIngreso.getTime();
        double dias = milisegundos / (1000 * 60 * 60 * 24);

        if (dias > 0) {
            double total = dias * habitacion.getPrecio();
            r.setTotal(total);
            System.out.println("Dias de estadia: " + dias);
            System.out.println("Total calculado: " + total);
        } else {
            System.out.println("La fecha de salida debe ser posterior a la de ingreso, se cancela el registro");
            return;
        }

        reservaDao.registrar(r);
    }

    public void leerListarReserva() {
        if (reservaDao.listar().isEmpty()) {
            System.out.println("No hay reservas registradas.");
        } else {
            System.out.println("----- LISTA DE RESERVAS -----");
            for (Reserva r : reservaDao.listar()) {
                System.out.println("ID: " + r.getIdReserva());
                System.out.println("Cliente: " + r.getCliente().getNombre());
                System.out.println("Usuario: " + r.getUsuarios().getNombre());
                System.out.println("Habitacion: " + r.getHabitacion().getCod_hab());
                System.out.println("Fecha ingreso: " + r.getFecha_ingreso());
                System.out.println("Fecha salida: " + r.getFecha_salida());
                System.out.println("Total: " + r.getTotal());
                System.out.println("Estado: " + r.getEstado());
                System.out.println("Fecha reserva: " + r.getFecha_reserva());
                System.out.println("-----------------------------");
            }
        }
    }

    public void leerEliminarReserva() {
        System.out.println("Ingrese el Id de la reserva a eliminar: ");
        int id = leer.entero();
        Reserva r = reservaDao.buscarPorId(id);
        if (r != null) {
            reservaDao.eliminar(r);
        } else {
            System.out.println("No existe una reserva con ese Id");
        }
    }

    public void leerActualizarEstadoReserva() {
        System.out.println("Ingrese el Id de la reserva: ");
        int id = leer.entero();
        Reserva r = reservaDao.buscarPorId(id);
        if (r != null) {
            System.out.println("Nuevo ESTADO (R = Reservada, O = Ocupada, C = Cancelada): ");
            r.setEstado(leer.cadena());
            reservaDao.act_estado(r);
        } else {
            System.out.println("No existe una reserva con ese Id");
        }
    }
}
