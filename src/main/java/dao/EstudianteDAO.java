package dao;

import modelo.Estudiante;

import java.util.ArrayList;

/**
 * Define las operaciones de acceso a datos disponibles para la entidad Estudiante.
 */
public interface EstudianteDAO {
    public String create(Estudiante estudiante);
    public ArrayList<Estudiante> readAll();
    public String update(Estudiante estudiante);
    public String delete(Estudiante estudiante);
}
