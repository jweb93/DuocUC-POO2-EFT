package controlador;

import dao.impl.PrestamoDAOImpl;
import modelo.Prestamo;

import java.util.ArrayList;
/**
 * Controlador encargado de gestionar las operaciones relacionadas con prestamos.
 * Actúa como intermediario entre las vistas de la aplicación y la capa de
 * acceso a datos de prestamos.
 */
public class ControladorPrestamos {
    private PrestamoDAOImpl prestamoDAOImpl;

    /**
     * Crea un controlador de prestamos e inicializa su objeto de acceso a datos.
     */
    public ControladorPrestamos(){
        prestamoDAOImpl = new PrestamoDAOImpl();
    }

    /**
     * Obtiene todos los prestamos registrados en la base de datos.
     *
     * @return lista con los prestamos registrados
     */
    public ArrayList<Prestamo> getPrestamos() {
        return prestamoDAOImpl.readAll();
    }

    /**
     * Solicita el registro de un nuevo prestamo en la base de datos.
     *
     * @param prestamo prestamo que se desea registrar
     * @return mensaje con el resultado de la operación
     */
    public String agregarNuevoPrestamo(Prestamo prestamo){
        return prestamoDAOImpl.create(prestamo);
    }

    /**
     * Solicita la actualización de un prestamo existente.
     *
     * @param prestamo prestamo con los datos que se desean actualizar
     * @return mensaje con el resultado de la operación
     */

    public String actualizarPrestamo(Prestamo prestamo){
        return prestamoDAOImpl.update(prestamo);
    }

    /**
     * Solicita la eliminación de un prestamo de la base de datos.
     *
     * @param prestamo prestamo que identifica el registro que se desea eliminar
     * @return mensaje con el resultado de la operación
     */
    public String eliminaPrestamo(Prestamo prestamo){
        return prestamoDAOImpl.delete(prestamo);
    }


}
