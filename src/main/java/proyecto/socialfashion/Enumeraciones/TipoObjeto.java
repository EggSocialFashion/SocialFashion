package proyecto.socialfashion.Enumeraciones;

public enum TipoObjeto {
    USUARIO, COMENTARIO, PUBLICACION;

    public static boolean isValid(String value) {
        for (TipoObjeto tipoObjeto : values()) {
            if (tipoObjeto.name().equals(value)) {
                return true;
            }
        }
        return false;

    }
}