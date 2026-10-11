package controlador;

import dao.UsuarioDAO;
import dao.impl.UsuarioDAOImpl;
import modelo.Usuario;

import java.util.ArrayList;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con usuarios.
 * Actúa como intermediario entre las vistas de la aplicación y la capa de
 * acceso a datos de usuarios.
 */
public class ControladorUsuarios {
    private UsuarioDAOImpl usuarioDAOImpl;

    /**
     * Crea un controlador de usuarios e inicializa su objeto de acceso a datos.
     */
    public ControladorUsuarios(){
        usuarioDAOImpl = new UsuarioDAOImpl();
    }

    /**
     * Obtiene todos los usuarios registrados en la base de datos.
     *
     * @return lista con los usuarios registrados
     */
    public ArrayList<Usuario> getUsuarios() {
        return usuarioDAOImpl.readAll();
    }

    /**
     * Solicita el registro de un nuevo usuario en la base de datos.
     *
     * @param usuario usuario que se desea registrar
     * @return mensaje con el resultado de la operación
     */
    public String agregarNuevoUsuario(Usuario usuario){
        return usuarioDAOImpl.create(usuario);
    }

    /**
     * Solicita la actualización de un usuario existente.
     *
     * @param usuario usuario con los datos que se desean actualizar
     * @return mensaje con el resultado de la operación
     */

    public String actualizarUsuario(Usuario usuario){
        return usuarioDAOImpl.update(usuario);
    }

    /**
     * Solicita la eliminación de un usuario de la base de datos.
     *
     * @param usuario usuario que identifica el registro que se desea eliminar
     * @return mensaje con el resultado de la operación
     */
    public String eliminaUsuario(Usuario usuario){
        return usuarioDAOImpl.delete(usuario);
    }

    /**
     * Método para autenticar credenciales de usuario comparando rut y contrasenña
     * @param rut
     * @param contrasenia
     * @return usuario
     */
    public Usuario autenticar(String rut, String contrasenia){
        for (Usuario u : getUsuarios()){
            if(u.getRut().equals(rut) && u.getContrasenia().equals(contrasenia)){
                return u;
            }
        }
        return null;
    }

}
