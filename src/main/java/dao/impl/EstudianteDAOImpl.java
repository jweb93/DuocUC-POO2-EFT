package dao.impl;

import dao.EstudianteDAO;
import modelo.Estudiante;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implementación de las operaciones de acceso a datos para estudiantes.
 * Utiliza JDBC y conexiones a MySQL para realizar las operaciones CRUD
 * sobre la tabla de estudiantes.
 */
public class EstudianteDAOImpl implements EstudianteDAO {

    // Método para crear un nuevo estudiante en BBDD
    public String create(Estudiante estudiante) {
        String sql = "INSERT INTO estudiantes (nombre, rut, curso, correo) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, estudiante.getNombre());
                stmt.setString(2, estudiante.getRut());
                stmt.setString(3, estudiante.getCurso());
                stmt.setString(4, estudiante.getCorreo());

                stmt.executeUpdate();
                return "Éxito al guardar el estudiante en la base de datos.";
            }

        } catch (SQLException e) {
            e.printStackTrace();

            if (e.getErrorCode() == 1062) {
                return "Error: ya existe un estudiante registrado con ese RUT.";
            }

            return "Error al guardar el estudiante en la base de datos.";
        }
    }

    // Método para obtener todos los estudiantes de BBDD en un arreglo
    public ArrayList<Estudiante> readAll() {
        String sql = "SELECT * FROM estudiantes";
        ArrayList<Estudiante> estudiantes = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    estudiantes.add(new Estudiante(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("rut"),
                            rs.getString("curso"),
                            rs.getString("correo")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return estudiantes;
    }

    // Método que nos permite actualizar un estudiante
    public String update(Estudiante estudiante) {
        String sql = "UPDATE estudiantes SET nombre=?, rut=?, curso=?, correo=? WHERE id=?";

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, estudiante.getNombre());
                stmt.setString(2, estudiante.getRut());
                stmt.setString(3, estudiante.getCurso());
                stmt.setString(4, estudiante.getCorreo());
                stmt.setInt(5, estudiante.getId());

                int cambios = stmt.executeUpdate();

                if (cambios > 0) {
                    return "Éxito al actualizar el estudiante en la base de datos.";
                } else {
                    return "No se encontró el estudiante en la base de datos.";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

            if (e.getErrorCode() == 1062) {
                return "Error: ya existe un estudiante registrado con ese RUT.";
            }

            return "Error al actualizar el estudiante en la base de datos.";
        }
    }

    // Método que nos permite eliminar un estudiante por su ID
    public String delete(Estudiante estudiante) {
        String sql = "DELETE FROM estudiantes WHERE id=?";

        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, estudiante.getId());

                int cambios = stmt.executeUpdate();

                if (cambios > 0) {
                    return "Éxito al eliminar el estudiante de la base de datos.";
                } else {
                    return "No se encontró el estudiante en la base de datos.";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

            if (e.getErrorCode() == 1451) {
                return "No se puede eliminar el estudiante porque existen préstamos asociados a él.";
            }

            return "Error al eliminar el estudiante en la base de datos.";
        }
    }
}