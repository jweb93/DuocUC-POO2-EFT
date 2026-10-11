package controlador;

import dao.impl.LibroDAOImpl;
import modelo.Libro;

import java.util.ArrayList;
/**
 * Controlador encargado de gestionar las operaciones relacionadas con libros.
 * Actúa como intermediario entre las vistas de la aplicación y la capa de
 * acceso a datos de libros.
 */
public class ControladorLibros {
    private LibroDAOImpl libroDAOImpl;

    /**
     * Crea un controlador de libros e inicializa su objeto de acceso a datos.
     */
    public ControladorLibros(){
        libroDAOImpl = new LibroDAOImpl();
    }

    /**
     * Obtiene todos los libros registrados en la base de datos.
     *
     * @return lista con los libros registrados
     */
    public ArrayList<Libro> getLibros() {
        return libroDAOImpl.readAll();
    }

    /**
     * Solicita el registro de un nuevo libro en la base de datos.
     *
     * @param libro libro que se desea registrar
     * @return mensaje con el resultado de la operación
     */
    public String agregarNuevoLibro(Libro libro){
        return libroDAOImpl.create(libro);
    }

    /**
     * Solicita la actualización de un libro existente.
     *
     * @param libro libro con los datos que se desean actualizar
     * @return mensaje con el resultado de la operación
     */

    public String actualizarLibro(Libro libro){
        return libroDAOImpl.update(libro);
    }

    /**
     * Solicita la eliminación de un libro de la base de datos.
     *
     * @param libro libro que identifica el registro que se desea eliminar
     * @return mensaje con el resultado de la operación
     */
    public String eliminaLibro(Libro libro){
        return libroDAOImpl.delete(libro);
    }


}
