package dao.impl;

import dao.UsuarioDAO;
import modelo.Rol;
import modelo.Usuario;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implementación de las operaciones de acceso a datos para usuarios.
 * Utiliza JDBC y conexiones a MySQL para realizar las operaciones CRUD
 * sobre la tabla de usuarios.
 */
public class UsuarioDAOImpl implements UsuarioDAO {
    // Método para crear un nuevo usuario en BBDD
    public String create(Usuario usuario){
        String sql = "INSERT INTO usuarios (nombre, rut, correo, contraseña, rol) VALUES (?, ?, ?, ?, ?)";
        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, usuario.getNombre());
                stmt.setString(2, usuario.getRut());
                stmt.setString(3, usuario.getCorreo());
                stmt.setString(4, usuario.getContrasenia());
                stmt.setString(5, usuario.getRol().name()); // Con name() obtenemos el valor del Enum

                stmt.executeUpdate();
                return "Éxito al guardar el usuario en la base de datos.";

            }
        }catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 1062) { // Este codigo se generó al probar crear usuarios con rut ya existentes
                return "Error: ya existe un usuario registrado con ese RUT.";
            }
            return "Error al guardar el usuario en la base de datos.";
        }
    }

    // Método para obtener todos los Usuarios de BBDD en un arreglo
    public ArrayList<Usuario> readAll(){
        String sql = "SELECT * FROM usuarios";
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(new Usuario (
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("rut"),
                            rs.getString("correo"),
                            rs.getString("contraseña"),
                            Rol.valueOf(rs.getString("rol").toUpperCase())
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    // Método que nos permite actualizar un usuario
    public String update(Usuario usuario){
        String sql = "UPDATE usuarios SET nombre=?, rut=?, correo=?, contraseña=?, rol=? WHERE id=?";

        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try (
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, usuario.getNombre());
                stmt.setString(2, usuario.getRut());
                stmt.setString(3, usuario.getCorreo());
                stmt.setString(4, usuario.getContrasenia());
                stmt.setString(5, usuario.getRol().name());
                stmt.setInt(6, usuario.getId());

                int cambios = stmt.executeUpdate();

                if (cambios > 0) {
                    return "Éxito al actualizar el usuario en la base de datos.";
                } else {
                    return "No se encontró el usuario en la base de datos.";
                }

            }
        }catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 1062) { // Este codigo se generó al probar crear usuarios con rut ya existentes
                return "Error: ya existe un usuario registrado con ese RUT.";
            }
            return "Error al actualizar el usuario en la base de datos.";
        }

    }

    // Método que nos permite eliminar un usuario por su ID
    public String delete(Usuario usuario){
        String sql = "DELETE FROM usuarios WHERE id=?";

        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, usuario.getId());

                int cambios = stmt.executeUpdate();
                if (cambios > 0) {
                    return "Éxito al eliminar el usuario de la base de datos.";
                } else {
                    return "No se encontró el usuario en la base de datos.";
                }

            }
        }catch (SQLException e) {
            e.printStackTrace();
            return "Error al eliminar el usuario en la base de datos.";
        }
    }
}
