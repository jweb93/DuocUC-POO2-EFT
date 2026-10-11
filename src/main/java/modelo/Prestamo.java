package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Prestamo {
    private int id;
    private Estudiante estudiante;
    private Libro libro;
    private LocalDate fecha_prestamo;
    private LocalDate fecha_devolucion;
    private Boolean devuelto;

    // Constructor cuando el objeto ya posee un registro en bbdd (un id)
    public Prestamo(int id, Estudiante estudiante, Libro libro, LocalDate fecha_prestamo, LocalDate fecha_devolucion, Boolean devuelto) {
        this.id = id;
        this.estudiante = estudiante;
        this.libro = libro;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_devolucion = fecha_devolucion;
        this.devuelto = devuelto; // Para ser consistentes con el valor que tiene por defecto en bbdd
    }

    // Constructor cuando el objeto aun no posee registro en bbdd
    public Prestamo(Estudiante estudiante, Libro libro, LocalDate fecha_prestamo, LocalDate fecha_devolucion) {
        this.estudiante = estudiante;
        this.libro = libro;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_devolucion = fecha_devolucion;
        this.devuelto = false;
    }

    // Constructor cuando el objeto será eliminado mediante su id
    public Prestamo(int id) {
        this.id = id;
    }

    // Getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public LocalDate getFecha_prestamo() {
        return fecha_prestamo;
    }

    public void setFecha_prestamo(LocalDate fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }

    public LocalDate getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(LocalDate fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    public Boolean getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(Boolean devuelto) {
        this.devuelto = devuelto;
    }

    /**
     * Metodo para calcular los días de atraso en la devolución de un libro
     * @return atraso
     */
    public int getAtraso(){

        // Si aun no se ha devuelto el libro, sólo existe atraso cuando: hoy > prestamo + 7 días
        LocalDate fecha_limite = fecha_prestamo.plusDays(7);
        if (fecha_devolucion == null) {
            if (LocalDate.now().isAfter(fecha_limite)) {
                return (int) ChronoUnit.DAYS.between(fecha_limite, LocalDate.now());
            } else {
                return 0;
            }
        }
        // Si ya se devolvió el libro, existe atraso cuando: devolucion > prestamo + 7 días
        else{
            if(fecha_devolucion.isAfter(fecha_limite)){
                return (int) ChronoUnit.DAYS.between(fecha_limite, fecha_devolucion);
            }
            else{
                return 0;
            }
        }

    }
}
