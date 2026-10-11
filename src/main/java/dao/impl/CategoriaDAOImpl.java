package dao.impl;

import dao.CategoriaDAO;
import modelo.Categoria;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * Implementación de las operaciones de acceso a datos para categorias.
 * Utiliza JDBC y conexiones a MySQL para realizar las operaciones CRUD
 * sobre la tabla de categorias.
 */
public class CategoriaDAOImpl implements CategoriaDAO {

    public String create(Categoria categoria) {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, categoria.getNombre());

                stmt.executeUpdate();
                return "Éxito al guardar la categoría en la base de datos.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al guardar la categoría en la base de datos.";
        }
    }

    public ArrayList<Categoria> readAll() {
        String sql = "SELECT * FROM categorias";
        ArrayList<Categoria> categorias = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    categorias.add(new Categoria(
                            rs.getInt("id"),
                            rs.getString("nombre")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categorias;
    }

    public String update(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre=? WHERE id=?";

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, categoria.getNombre());
                stmt.setInt(2, categoria.getId());

                int cambios = stmt.executeUpdate();

                if (cambios > 0) {
                    return "Éxito al actualizar la categoría en la base de datos.";
                } else {
                    return "No se encontró la categoría en la base de datos.";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al actualizar la categoría en la base de datos.";
        }
    }

    public String delete(Categoria categoria) {
        String sql = "DELETE FROM categorias WHERE id=?";

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, categoria.getId());

                int cambios = stmt.executeUpdate();

                if (cambios > 0) {
                    return "Éxito al eliminar la categoría de la base de datos.";
                } else {
                    return "No se encontró la categoría en la base de datos.";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

            if (e.getErrorCode() == 1451) {
                return "No se puede eliminar la categoría porque existen libros asociados a ella.";
            }

            return "Error al eliminar la categoría en la base de datos.";
        }
    }
}