package pe.edu.upeu.luxury12.utils;

import pe.edu.upeu.luxury12.Entidad.Usuarios;

public final class SesionUsuario {

    public static final String ROL_ADMIN = "ADMIN";
    public static final String ROL_RECEPCIONISTA = "RECEPCIONISTA";

    private static Usuarios usuarioActual;

    private SesionUsuario() {

    }

    public static void iniciarSesion(Usuarios usuario) {
        usuarioActual = usuario;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static Usuarios getUsuarioActual() {
        return usuarioActual;
    }

    public static boolean estaAutenticado() {
        return usuarioActual != null;
    }

    public static Integer getIdUsuario() {
        return usuarioActual != null ? usuarioActual.getIdUsuario() : null;
    }

    public static String getNombreUsuario() {
        return usuarioActual != null ? usuarioActual.getUsuario() : "";
    }

    public static String getRol() {
        if (usuarioActual == null || usuarioActual.getTipoUsuario() == null) {
            return "";
        }
        return normalizarRol(usuarioActual.getTipoUsuario());
    }

    public static boolean esAdmin() {
        return ROL_ADMIN.equalsIgnoreCase(getRol());
    }

    public static boolean esRecepcionista() {
        return ROL_RECEPCIONISTA.equalsIgnoreCase(getRol());
    }

    public static boolean tieneRol(String... rolesPermitidos) {
        if (!estaAutenticado() || rolesPermitidos == null) {
            return false;
        }
        String rolActual = getRol();
        for (String rol : rolesPermitidos) {
            if (rol != null && rolActual.equalsIgnoreCase(normalizarRol(rol))) {
                return true;
            }
        }
        return false;
    }

    public static String normalizarRol(String rol) {
        if (rol == null) {
            return "";
        }
        String limpio = rol.trim().toUpperCase();
        if ("ADMINISTRADOR".equals(limpio)) {
            return ROL_ADMIN;
        }
        return limpio;
    }
}