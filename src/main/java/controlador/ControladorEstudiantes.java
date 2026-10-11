package controlador;

import dao.impl.EstudianteDAOImpl;
import modelo.Estudiante;

import java.util.ArrayList;
/**
 * Controlador encargado de gestionar las operaciones relacionadas con estudiantes.
 * Actúa como intermediario entre las vistas de la aplicación y la capa de
 * acceso a datos de estudiantes.
 */
public class ControladorEstudiantes {
    private EstudianteDAOImpl estudianteDAOImpl;

    /**
     * Crea un controlador de estudiantes e inicializa su objeto de acceso a datos.
     */
    public ControladorEstudiantes(){
        estudianteDAOImpl = new EstudianteDAOImpl();
    }

    /**
     * Obtiene todos los estudiantes registrados en la base de datos.
     *
     * @return lista con los estudiantes registrados
     */
    public ArrayList<Estudiante> getEstudiantes() {
        return estudianteDAOImpl.readAll();
    }

    /**
     * Solicita el registro de un nuevo estudiante en la base de datos.
     *
     * @param estudiante estudiante que se desea registrar
     * @return mensaje con el resultado de la operación
     */
    public String agregarNuevoEstudiante(Estudiante estudiante){
        return estudianteDAOImpl.create(estudiante);
    }

    /**
     * Solicita la actualización de un estudiante existente.
     *
     * @param estudiante estudiante con los datos que se desean actualizar
     * @return mensaje con el resultado de la operación
     */

    public String actualizarEstudiante(Estudiante estudiante){
        return estudianteDAOImpl.update(estudiante);
    }

    /**
     * Solicita la eliminación de un estudiante de la base de datos.
     *
     * @param estudiante estudiante que identifica el registro que se desea eliminar
     * @return mensaje con el resultado de la operación
     */
    public String eliminaEstudiante(Estudiante estudiante){
        return estudianteDAOImpl.delete(estudiante);
    }


}
