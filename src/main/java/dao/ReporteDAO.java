package dao;

import modelo.Estudiante;
import modelo.Prestamo;
import modelo.reportes.ReporteLibrosMasPrestados;

import java.util.ArrayList;
/**
 * Define las operaciones de consulta para obtener reporteria
 */
public interface ReporteDAO {

    // Reporte 1: Libros más prestados
    public ArrayList<ReporteLibrosMasPrestados> getLibrosMasPrestados();

    // Reporte 2: Historial de prestamos por estudiante
    public ArrayList<Prestamo> getHistorialEstudiante(Estudiante estudiante);

    // Reporte 3: libros actualmente prestados
    public ArrayList<Prestamo> getLibrosActualmentePrestados();
}
