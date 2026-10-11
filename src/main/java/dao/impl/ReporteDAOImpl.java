package dao.impl;

import dao.ReporteDAO;
import modelo.Categoria;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import modelo.reportes.ReporteLibrosMasPrestados;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implementación de las operaciones de consulta de reportes.
 */
public class ReporteDAOImpl implements ReporteDAO {

    /**
     * Reporte 1: Obtiene los libros ordenados según la cantidad
     * de veces que han sido prestados.
     *
     * @return lista con los libros y su cantidad de préstamos
     */
    @Override
    public ArrayList<ReporteLibrosMasPrestados> getLibrosMasPrestados() {

        String sql = "SELECT " +
                "l.id AS libro_id, " +
                "l.titulo, " +
                "l.autor, " +
                "l.isbn, " +
                "l.editorial, " +
                "l.stock, " +
                "c.id AS categoria_id, " +
                "c.nombre AS categoria_nombre, " +
                "COUNT(p.id) AS numero_prestamos " +
                "FROM libros l " +
                "LEFT JOIN categorias c ON l.id_categoria = c.id " +
                "LEFT JOIN prestamos p ON p.id_libro = l.id " +
                "GROUP BY l.id, l.titulo, l.autor, l.isbn, l.editorial, " +
                "l.stock, c.id, c.nombre " +
                "ORDER BY numero_prestamos DESC " +
                "Limit 3";

        ArrayList<ReporteLibrosMasPrestados> reporte = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Categoria categoria = new Categoria(
                            rs.getInt("categoria_id"),
                            rs.getString("categoria_nombre")
                    );

                    Libro libro = new Libro(
                            rs.getInt("libro_id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("isbn"),
                            rs.getString("editorial"),
                            rs.getInt("stock"),
                            categoria
                    );

                    reporte.add(new ReporteLibrosMasPrestados(
                            libro,
                            rs.getInt("numero_prestamos")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reporte;
    }


    /**
     * Reporte 2: Obtiene el historial de préstamos correspondiente
     * a un estudiante determinado.
     *
     * @param estudiante estudiante cuyo historial se desea consultar
     * @return lista de préstamos realizados por el estudiante
     */
    @Override
    public ArrayList<Prestamo> getHistorialEstudiante(Estudiante estudiante) {

        String sql = "SELECT " +
                "p.id AS prestamo_id, " +
                "p.fecha_prestamo, " +
                "p.fecha_devolucion, " +
                "p.devuelto, " +
                "l.id AS libro_id, " +
                "l.titulo, " +
                "l.autor, " +
                "l.isbn, " +
                "l.editorial, " +
                "l.stock, " +
                "c.id AS categoria_id, " +
                "c.nombre AS categoria_nombre " +
                "FROM prestamos p " +
                "JOIN libros l ON p.id_libro = l.id " +
                "LEFT JOIN categorias c ON l.id_categoria = c.id " +
                "WHERE p.id_estudiante = ? " +
                "ORDER BY p.fecha_prestamo DESC";

        ArrayList<Prestamo> prestamos = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, estudiante.getId());

                try (ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {

                        Categoria categoria = new Categoria(
                                rs.getInt("categoria_id"),
                                rs.getString("categoria_nombre")
                        );

                        Libro libro = new Libro(
                                rs.getInt("libro_id"),
                                rs.getString("titulo"),
                                rs.getString("autor"),
                                rs.getString("isbn"),
                                rs.getString("editorial"),
                                rs.getInt("stock"),
                                categoria
                        );

                        Prestamo prestamo = new Prestamo(
                                rs.getInt("prestamo_id"),
                                estudiante,
                                libro,
                                rs.getObject(
                                        "fecha_prestamo",
                                        java.time.LocalDate.class
                                ),
                                rs.getObject(
                                        "fecha_devolucion",
                                        java.time.LocalDate.class
                                ),
                                rs.getBoolean("devuelto")
                        );

                        prestamos.add(prestamo);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prestamos;
    }


    /**
     * Reporte 3: Obtiene todos los libros que actualmente
     * se encuentran en préstamo.
     *
     * @return lista de préstamos que aún no han sido devueltos
     */
    @Override
    public ArrayList<Prestamo> getLibrosActualmentePrestados() {

        String sql = "SELECT " +
                "p.id AS prestamo_id, " +
                "p.fecha_prestamo, " +
                "p.fecha_devolucion, " +
                "p.devuelto, " +
                "e.id AS estudiante_id, " +
                "e.nombre AS estudiante_nombre, " +
                "e.rut AS estudiante_rut, " +
                "e.curso AS estudiante_curso, " +
                "e.correo AS estudiante_correo, " +
                "l.id AS libro_id, " +
                "l.titulo, " +
                "l.autor, " +
                "l.isbn, " +
                "l.editorial, " +
                "l.stock, " +
                "c.id AS categoria_id, " +
                "c.nombre AS categoria_nombre " +
                "FROM prestamos p " +
                "JOIN estudiantes e ON p.id_estudiante = e.id " +
                "JOIN libros l ON p.id_libro = l.id " +
                "LEFT JOIN categorias c ON l.id_categoria = c.id " +
                "WHERE p.devuelto = FALSE " +
                "ORDER BY p.fecha_prestamo ASC";

        ArrayList<Prestamo> prestamos = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Estudiante estudiante = new Estudiante(
                            rs.getInt("estudiante_id"),
                            rs.getString("estudiante_nombre"),
                            rs.getString("estudiante_rut"),
                            rs.getString("estudiante_curso"),
                            rs.getString("estudiante_correo")
                    );

                    Categoria categoria = new Categoria(
                            rs.getInt("categoria_id"),
                            rs.getString("categoria_nombre")
                    );

                    Libro libro = new Libro(
                            rs.getInt("libro_id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("isbn"),
                            rs.getString("editorial"),
                            rs.getInt("stock"),
                            categoria
                    );

                    Prestamo prestamo = new Prestamo(
                            rs.getInt("prestamo_id"),
                            estudiante,
                            libro,
                            rs.getObject(
                                    "fecha_prestamo",
                                    java.time.LocalDate.class
                            ),
                            rs.getObject(
                                    "fecha_devolucion",
                                    java.time.LocalDate.class
                            ),
                            rs.getBoolean("devuelto")
                    );

                    prestamos.add(prestamo);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prestamos;
    }
}