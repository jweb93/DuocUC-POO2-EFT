package dao;

import modelo.Prestamo;

import java.util.ArrayList;

/**
 * Define las operaciones de acceso a datos disponibles para la entidad Prestamo.
 */
public interface PrestamoDAO {
    public String create(Prestamo prestamo);
    public ArrayList<Prestamo> readAll();
    public String update(Prestamo prestamo);
    public String delete(Prestamo prestamo);
}
