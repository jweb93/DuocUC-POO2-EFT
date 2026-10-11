package controlador;

import dao.impl.EstudianteDAOImpl;
import dao.impl.ReporteDAOImpl;
import modelo.Estudiante;
import modelo.Prestamo;
import modelo.reportes.ReporteLibrosMasPrestados;

import java.util.ArrayList;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con reportes.
 * Actúa como intermediario entre las vistas de la aplicación y la capa de
 * acceso a datos de reportes.
 */
public class ControladorReportes {
    private ReporteDAOImpl reporteDAOImpl;

    /**
     * Crea un controlador de reportes e inicializa su objeto de acceso a datos.
     */
    public ControladorReportes(){
        reporteDAOImpl = new ReporteDAOImpl();
    }


    /**
     * Obtiene todos los registros del Reporte 1
     *
     * @return lista con los registros
     */
    public ArrayList<ReporteLibrosMasPrestados> getReporte1() {
        return reporteDAOImpl.getLibrosMasPrestados();
    }


    /**
     * Obtiene todos los registros del Reporte 2
     *
     * @return lista con los registros
     */
    public ArrayList<Prestamo> getReporte2(Estudiante estudiante) {
        return reporteDAOImpl.getHistorialEstudiante(estudiante);
    }


    /**
     * Obtiene todos los registros del Reporte 3
     *
     * @return lista con los registros
     */
    public ArrayList<Prestamo> getReporte3() {
        return reporteDAOImpl.getLibrosActualmentePrestados();
    }
}
