package dao;

import modelo.Libro;

import java.util.ArrayList;

/**
 * Define las operaciones de acceso a datos disponibles para la entidad Libro.
 */
public interface LibroDAO {
    public String create(Libro libro);
    public ArrayList<Libro> readAll();
    public String update(Libro libro);
    public String delete(Libro libro);
}
