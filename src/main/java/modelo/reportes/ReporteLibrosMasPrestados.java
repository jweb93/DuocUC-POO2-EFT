package modelo.reportes;

import modelo.Libro;

/**
 * Objeto que permite capturar la respuesta al reporte de Libros más prestados
 */
public class ReporteLibrosMasPrestados {
    private Libro libro;
    private int cantidadPrestamos;

    public ReporteLibrosMasPrestados(Libro libro, int cantidadPrestamos) {
        this.libro = libro;
        this.cantidadPrestamos = cantidadPrestamos;
    }

    public Libro getLibro() {
        return libro;
    }

    public int getCantidadPrestamos() {
        return cantidadPrestamos;
    }
}
