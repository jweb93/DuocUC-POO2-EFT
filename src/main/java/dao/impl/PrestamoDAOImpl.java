package dao.impl;

import dao.PrestamoDAO;
import modelo.Categoria;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
/**
 * Implementación de las operaciones de acceso a datos para prestamos.
 * Utiliza JDBC y conexiones a MySQL para realizar las operaciones CRUD
 * sobre la tabla de prestamos.
 */
public class PrestamoDAOImpl implements PrestamoDAO {

    // Crearemos un objeto de clase compartido que permita limitar el acceso a ciertos métodos
    // pensando en el consumo concurrente de hilos
    private static final Object BLOQUEO_PRESTAMOS = new Object();

    // El método create será synchronized usando este objeto compartido
    public String create(Prestamo prestamo) {
        synchronized (BLOQUEO_PRESTAMOS) {
            return ejecutarPrestamo(prestamo);
        }
    }

    // En la reación de un nuevo prestamo no se especificará el campo "devuelto" ya que al ser nuevo, debería tomar su
    // valor por defecto en bbdd que es false.
    // El método es privado para ser consistentes con el uso concurrente en hilos
    private String ejecutarPrestamo(Prestamo prestamo) {
        String sqlPrestamo = "INSERT INTO prestamos (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion) VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE libros SET stock = stock - 1 WHERE id = ? AND stock > 0";
        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try{
                conn.setAutoCommit(false);

                // 1. Descontar Stock si existe disponibilidad
                try (PreparedStatement stmt = conn.prepareStatement(sqlStock)) {
                    stmt.setInt(1, prestamo.getLibro().getId());

                    int cambios = stmt.executeUpdate();

                    if (cambios == 0) {
                        conn.rollback();
                        return "No existe stock disponible para este libro.";
                    }
                }

                // 2. Registrar préstamo
                try (PreparedStatement stmt = conn.prepareStatement(sqlPrestamo)) {
                    stmt.setInt(1, prestamo.getEstudiante().getId());
                    stmt.setInt(2, prestamo.getLibro().getId());
                    stmt.setObject(3, prestamo.getFecha_prestamo());
                    stmt.setObject(4, prestamo.getFecha_devolucion());

                    int cambios = stmt.executeUpdate();

                    if (cambios == 0) {
                        conn.rollback();
                        return "No fue posible registrar el préstamo.";
                    }
                }

                // 3. Confirmar ambas operaciones
                conn.commit();

                return "Préstamo registrado correctamente.";


            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return "Error al registrar el préstamo.";

            } finally {
                conn.setAutoCommit(true);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return "Error al guardar el préstamo en la base de datos.";
        }

    }

    public ArrayList<Prestamo> readAll() {
        String sql = "SELECT " +
                "p.id," +
                "p.id_estudiante," +
                "e.nombre," +
                "e.rut, " +
                "e.curso," +
                "e.correo," +
                "p.id_libro," +
                "l.titulo," +
                "l.autor," +
                "l.isbn," +
                "l.editorial," +
                "l.stock," +
                "l.id_categoria," +
                "c.nombre," +
                "p.fecha_prestamo," +
                "p.fecha_devolucion," +
                "p.devuelto " +
                "FROM ((prestamos as p left join estudiantes as e on p.id_estudiante=e.id) " +
                "left join libros as l on p.id_libro=l.id) " +
                "left join categorias as c on l.id_categoria=c.id";
        ArrayList<Prestamo> prestamos = new ArrayList<>();

        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    prestamos.add(new Prestamo(
                            rs.getInt("p.id"),
                            new Estudiante(
                                    rs.getInt("p.id_estudiante"),
                                    rs.getString("e.nombre"),
                                    rs.getString("e.rut"),
                                    rs.getString("e.curso"),
                                    rs.getString("e.correo")
                            ),
                            new Libro(
                                    rs.getInt("p.id_libro"),
                                    rs.getString("l.titulo"),
                                    rs.getString("l.autor"),
                                    rs.getString("l.isbn"),
                                    rs.getString("l.editorial"),
                                    rs.getInt("l.stock"),
                                    new Categoria(
                                            rs.getInt("l.id_categoria"),
                                            rs.getString("c.nombre")
                                    )),
                            rs.getObject("fecha_prestamo", LocalDate.class),
                            rs.getObject("fecha_devolucion", LocalDate.class),
                            rs.getBoolean("devuelto")
                    ));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    public String update(Prestamo prestamo) {
        String sqlDevolucion = "UPDATE prestamos SET devuelto = true, fecha_devolucion = ? WHERE id = ? AND devuelto = false";
        String sqlStock = "UPDATE libros SET stock = stock + 1 WHERE id = (SELECT id_libro FROM prestamos WHERE id = ?)";
        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try{
                conn.setAutoCommit(false);

                // 1. Registrar devolución
                try (PreparedStatement stmt = conn.prepareStatement(sqlDevolucion)) {
                    stmt.setObject(1, LocalDate.now());
                    stmt.setInt(2, prestamo.getId());

                    int cambios = stmt.executeUpdate();

                    if (cambios == 0) {
                        conn.rollback();
                        return "El préstamo no existe o ya fue devuelto.";
                    }
                }

                // 2. Aumentar stock del libro asociado
                try (PreparedStatement stmt = conn.prepareStatement(sqlStock)) {
                    stmt.setInt(1, prestamo.getId());

                    int cambios = stmt.executeUpdate();

                    if (cambios == 0) {
                        conn.rollback();
                        return "No fue posible actualizar el stock.";
                    }
                }

                // 3. Confirmar ambas operaciones
                conn.commit();

                return "Devolución registrada correctamente.";

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return "Error al registrar la devolución.";

            } finally {
                conn.setAutoCommit(true);
            }

        }catch (SQLException e) {
            e.printStackTrace();
            return "Error al actualizar el préstamo en la base de datos.";
        }

    }

    public String delete(Prestamo prestamo) {
        String sql = "DELETE FROM prestamos WHERE id=?";

        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, prestamo.getId());

                int cambios = stmt.executeUpdate();

                if (cambios > 0) {
                    return "Éxito al eliminar el préstamo de la base de datos.";
                } else {
                    return "No se encontró el préstamo en la base de datos.";
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return "Error al eliminar el préstamo en la base de datos.";
        }
    }
}
