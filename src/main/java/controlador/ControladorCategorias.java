package controlador;

import dao.impl.CategoriaDAOImpl;
import dao.impl.LibroDAOImpl;
import modelo.Categoria;
import modelo.Libro;

import java.util.ArrayList;
/**
 * Controlador encargado de gestionar las operaciones relacionadas con categorias.
 * Actúa como intermediario entre las vistas de la aplicación y la capa de
 * acceso a datos de categorias.
 */
public class ControladorCategorias {
    private CategoriaDAOImpl categoriaDAOIml;

    /**
     * Crea un controlador de categorias e inicializa su objeto de acceso a datos.
     */
    public ControladorCategorias(){
        categoriaDAOIml = new CategoriaDAOImpl();
    }

    /**
     * Obtiene todos las categorías registrados en la base de datos.
     *
     * @return lista con las categorías registrados
     */
    public ArrayList<Categoria> getCategorias() {
        return categoriaDAOIml.readAll();
    }

    /**
     * Solicita el registro de una nueva categoria en la base de datos.
     *
     * @param categoria categoria que se desea registrar
     * @return mensaje con el resultado de la operación
     */
    public String agregarNuevoCategoria(Categoria categoria){
        return categoriaDAOIml.create(categoria);
    }

    /**
     * Solicita la actualización de una categoría existente.
     *
     * @param categoria categoria con los datos que se desean actualizar
     * @return mensaje con el resultado de la operación
     */

    public String actualizarCategoria(Categoria categoria){
        return categoriaDAOIml.update(categoria);
    }

    /**
     * Solicita la eliminación de una categoria de la base de datos.
     *
     * @param categoria categoria que identifica el registro que se desea eliminar
     * @return mensaje con el resultado de la operación
     */
    public String eliminaCategoria(Categoria categoria){
        return categoriaDAOIml.delete(categoria);
    }
}
