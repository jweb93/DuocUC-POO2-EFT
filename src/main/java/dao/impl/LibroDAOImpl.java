package dao.impl;

import dao.LibroDAO;
import modelo.Categoria;
import modelo.Libro;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Implementación de las operaciones de acceso a datos para libros.
 * Utiliza JDBC y conexiones a MySQL para realizar las operaciones CRUD
 * sobre la tabla de libros.
 */
public class LibroDAOImpl implements LibroDAO {

    public String create(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, isbn, editorial, stock, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";

        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try (
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, libro.getTitulo());
                stmt.setString(2, libro.getAutor());
                stmt.setString(3, libro.getIsbn());
                stmt.setString(4, libro.getEditorial());
                stmt.setInt(5, libro.getStock());
                stmt.setInt(6, libro.getCategoria().getId());

                stmt.executeUpdate();
                return "Éxito al guardar el libro en la base de datos.";
            }
        }catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 1062) { // error que se genera por la constraint unique
                return "Error: ya existe un libro registrado con ese ISBN.";
            }
            return "Error al guardar el libro en la base de datos.";
        }

    }

    public ArrayList<Libro> readAll() {
        String sql = "SELECT " +
                "l.id," +
                "l.titulo," +
                "l.autor," +
                "l.isbn," +
                "l.editorial," +
                "l.stock," +
                "l.id_categoria," +
                "c.nombre " +
                "FROM libros as l left join categorias as c on l.id_categoria=c.id";
        ArrayList<Libro> libros = new ArrayList<>();

        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    libros.add(new Libro(
                            rs.getInt("l.id"),
                            rs.getString("l.titulo"),
                            rs.getString("l.autor"),
                            rs.getString("l.isbn"),
                            rs.getString("l.editorial"),
                            rs.getInt("l.stock"),
                            new Categoria(
                                    rs.getInt("l.id_categoria"),
                                    rs.getString("c.nombre")
                            )
                    ));
                }

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return libros;
    }

    public String update(Libro libro) {
        String sql = "UPDATE libros SET titulo=?, autor=?, isbn=?, editorial=?, stock=?, id_categoria=? WHERE id=?";

        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, libro.getTitulo());
                stmt.setString(2, libro.getAutor());
                stmt.setString(3, libro.getIsbn());
                stmt.setString(4, libro.getEditorial());
                stmt.setInt(5, libro.getStock());
                stmt.setInt(6, libro.getCategoria().getId());
                stmt.setInt(7, libro.getId());

                int cambios = stmt.executeUpdate();

                if (cambios > 0) {
                    return "Éxito al actualizar el libro en la base de datos.";
                } else {
                    return "No se encontró el libro en la base de datos.";
                }

            }
        }catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 1062) { // error que se genera por la constraint unique
                return "Error: ya existe un libro registrado con ese ISBN.";
            }
            return "Error al actualizar el libro en la base de datos.";
        }

    }

    public String delete(Libro libro) {
        String sql = "DELETE FROM libros WHERE id=?";

        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, libro.getId());

                int cambios = stmt.executeUpdate();

                if (cambios > 0) {
                    return "Éxito al eliminar el libro de la base de datos.";
                } else {
                    return "No se encontró el libro en la base de datos.";
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 1451) { // Error al eliminar libro cuando existen registros foráneos con el (prestamos)
                return "No se puede eliminar el libro porque existen préstamos asociados a él.";
            }
            return "Error al eliminar el libro en la base de datos.";
        }

    }
}