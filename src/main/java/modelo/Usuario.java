package modelo;

/**
 * Representa un usuario que accede al sistema
 */

public class Usuario {
    private int id;
    private String nombre;
    private String rut;
    private String correo;
    private String contrasenia;
    private Rol rol;

    // Constructor cuando el objeto ya posee un registro en bbdd (un id)
    public Usuario(int id, String nombre, String rut, String correo, String contrasenia, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.rut = rut;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    // Constructor cuando el objeto aun no posee registro en bbdd
    public Usuario(String nombre, String rut, String correo, String contrasenia, Rol rol) {
        this.nombre = nombre;
        this.rut = rut;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    // Constructor cuando el objeto será eliminado mediante su id
    public Usuario(int id) {
        this.id = id;
    }

    // Getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
