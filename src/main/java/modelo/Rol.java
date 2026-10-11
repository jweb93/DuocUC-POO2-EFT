package modelo;
/**
 * Define los posibles roles de un usuario dentro de la aplicación.
 * Cada rol posee una descripción utilizada para su representación.
 */
public enum Rol {
    BIBLIOTECARIO("bibliotecario"),
    ESTUDIANTE("estudiante");

    private final String descripcion;

    Rol(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion(){
        return descripcion;
    }

    // Este metodo se agrega para poder convertir de String a Rol desde BBDD
    public static Rol desdeTexto(String texto){
        return Rol.valueOf(texto.toUpperCase());
    }

    /**
     * Devuelve la descripción del rol usuario.
     *
     * @return descripción utilizada para representar el rol
     */
    @Override
    public String toString() {
        return descripcion;
    }
}
