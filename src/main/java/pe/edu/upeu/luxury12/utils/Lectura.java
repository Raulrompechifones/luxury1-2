package pe.edu.upeu.luxury12.utils;

import java.util.Scanner;

public class Lectura {
    private Scanner teclado = new Scanner(System.in);

    public String cadena() {
        return teclado.nextLine();
    }

    public String cadenaMayuscula() {
        return cadena().toUpperCase();
    }

    public String cadenaMinuscula() {
        return cadena().toLowerCase();
    }

    public int entero() {
        while (true) {
            try {
                return Integer.parseInt(cadena());
            } catch (NumberFormatException e) {
                System.out.println("Eso no es un numero valido, intente nuevamente: ");
            }
        }
    }

    public double decimal() {
        while (true) {
            try {
                return Double.parseDouble(cadena());
            } catch (NumberFormatException e) {
                System.out.println("Eso no es un numero valido, intente nuevamente: ");
            }
        }
    }

    public char letra() {
        return cadena().charAt(0);
    }

    public void cerrar() {
        teclado.close();
    }
}
