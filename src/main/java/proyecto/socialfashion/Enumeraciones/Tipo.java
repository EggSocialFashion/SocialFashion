package proyecto.socialfashion.Enumeraciones;

public enum Tipo {
    SPAM, CONTENIDO_OFENSIVO, INCUMPLIMIENTO_DE_REGLAS;
    public static boolean isValid(String value) {
        for (Tipo tipo : values()) {
            if (tipo.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}

