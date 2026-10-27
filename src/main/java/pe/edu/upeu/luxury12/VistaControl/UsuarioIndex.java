package pe.edu.upeu.luxury12.VistaControl;

import pe.edu.upeu.luxury12.utils.Lectura;
import pe.edu.upeu.luxury12.utils.UsuariosLectura;

public class UsuarioIndex {
    static Lectura leer = new Lectura();
    static UsuariosLectura UL = new UsuariosLectura();

    public void menuUsuario() {
        int opcion;

        do {
            System.out.println("\n==============================");
            System.out.println("        MENU USUARIO");
            System.out.println("==============================");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Listar usuarios");
            System.out.println("3. Editar usuario");
            System.out.println("4. Eliminar usuario");
            System.out.println("5. Probar login");
            System.out.println("6. Regresar");
            System.out.println("==============================");
            System.out.println("Seleccione una opcion:");

            opcion = leer.entero();

            switch (opcion) {
                case 1:
                    UL.leerRegistrarUsuario();
                    break;
                case 2:
                    UL.leerListarUsuario();
                    break;
                case 3:
                    UL.leerActualizarUsuario();
                    break;
                case 4:
                    UL.leerEliminarUsuario();
                    break;
                case 5:
                    UL.leerLogin();
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
