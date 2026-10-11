package dao;

import modelo.Categoria;

import java.util.ArrayList;

/**
 * Define las operaciones de acceso a datos disponibles para la entidad Categoria.
 */
public interface CategoriaDAO {
    public String create(Categoria categoria);
    public ArrayList<Categoria> readAll();
    public String update(Categoria categoria);
    public String delete(Categoria categoria);
}
