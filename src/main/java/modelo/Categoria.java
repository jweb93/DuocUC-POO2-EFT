package modelo;

public class Categoria {
    private int id;
    private String nombre;

    // Constructor cuando el objeto ya posee un registro en bbdd (un id)
    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Constructor cuando el objeto aun no posee registro en bbdd
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // Constructor cuando el objeto será eliminado mediante su id
    public Categoria(int id) {
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

    // Método para retornar información resumida de una categoria.
    @Override
    public String toString() {
        return nombre;
    }
}
