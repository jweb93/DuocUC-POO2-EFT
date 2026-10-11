package dao;

import modelo.Usuario;

import java.util.ArrayList;

/**
 * Define las operaciones de acceso a datos disponibles para la entidad Usuario.
 */
public interface UsuarioDAO {
    public String create(Usuario usuario);
    public ArrayList<Usuario> readAll();
    public String update(Usuario usuario);
    public String delete(Usuario usuario);
}
