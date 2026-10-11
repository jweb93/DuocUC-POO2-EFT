package vista;

import controlador.ControladorCategorias;
import controlador.ControladorEstudiantes;
import controlador.ControladorLibros;
import controlador.ControladorPrestamos;
import modelo.Categoria;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class VentanaGestionPrestamos extends JFrame{
    private JPanel panelPrestamos;
    private JComboBox cmbLibro;
    private JComboBox cmbEstudiante;
    private JButton btnRegistrarPrestamo;
    private JButton btnRegistrarDevolucion;
    private JTable tblPrestamos;

    private ControladorLibros controladorLibros;
    private ControladorEstudiantes controladorEstudiantes;
    private ControladorPrestamos controladorPrestamos;
    private DefaultTableModel modeloTabla;
    private int registroSeleccionado = -1; // Índice del registro seleccionado en tabla

    public VentanaGestionPrestamos() {
        controladorPrestamos = new ControladorPrestamos();
        controladorEstudiantes = new ControladorEstudiantes();
        controladorLibros = new ControladorLibros();

        // Configuración base de la ventana
        setContentPane(panelPrestamos); // mostrar el contenido del UI Form
        setTitle("Gestión de préstamos - Biblioteca");
        setSize(1000, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Selectores
        cargaSelectores();

        // Configuración de tabla
        String[] columnas = {"ID", "Estudiante", "Libro", "Fecha Préstamo", "Fecha Devolución", "Devolución", "Días de atraso"};
        modeloTabla = new DefaultTableModel(columnas,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false; // Las celdas no son editables
            }
        };

        tblPrestamos.setModel(modeloTabla);
        tblPrestamos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo se puede seleccionar 1 fila
        tblPrestamos.getColumnModel().getColumn(0).setPreferredWidth(20);  // ID
        tblPrestamos.getColumnModel().getColumn(1).setPreferredWidth(180); // Estudiante
        tblPrestamos.getColumnModel().getColumn(2).setPreferredWidth(250); // Libro

        tblPrestamos.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int fila = tblPrestamos.getSelectedRow();
                if (fila >= 0){
                    registroSeleccionado = tblPrestamos.convertRowIndexToModel(fila);
                }
            }
        });

        // Configuración de los botones
        btnRegistrarPrestamo.addActionListener(e -> registrarPrestamo());
        btnRegistrarDevolucion.addActionListener(e -> registrarDevolucion());

        // Poblamiento de la tabla
        actualizarTabla();
        limpiar();
    }

    /**
     * Permite poblar los selectores de Estudiante y Libros con los registros actualizados.
     */
    public void cargaSelectores(){
        // Eliminar elementos anteriores
        cmbEstudiante.removeAllItems();
        cmbLibro.removeAllItems();

        // Consultar y poblar selectores
        for(Estudiante estudiante: controladorEstudiantes.getEstudiantes()){
            cmbEstudiante.addItem(estudiante);
        }
        for(Libro libro: controladorLibros.getLibros()){
            cmbLibro.addItem(libro);
        }

        cmbEstudiante.setSelectedIndex(-1);
        cmbLibro.setSelectedIndex(-1);
    }

    /**
     * Valida los datos ingresados y actualiza el registro actualmente
     * seleccionado en la tabla.
     */
    public void registrarDevolucion(){
        // Validar que exista selección de registro
        if(registroSeleccionado == -1){
            alertarInconsistencia("Selecciona un registro para editar.");
            return;
        }

        // Obtener ID y estado del préstamo
        int id = (Integer) modeloTabla.getValueAt(registroSeleccionado, 0);
        boolean devuelto = (Boolean) modeloTabla.getValueAt(registroSeleccionado, 5);

        // Validar estado
        if (devuelto) {
            alertarInconsistencia("Este préstamo ya fue devuelto.");
            return;
        }

        // Confirmar devolución
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea registrar la devolución del préstamo N° " + id + "?",
                "Confirmar devolución",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // Actualizar préstamo
        String resultado = controladorPrestamos.actualizarPrestamo(
                new Prestamo(id)
        );

        informarResultado(resultado);

        limpiar();
    }

    /**
     * Valida los datos del formulario y crea un nuevo registro.
     */
    public void registrarPrestamo(){
        // Validar formulario
        if (!validarCampos()) return;

        // Obtener valores
        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();
        Libro libro = (Libro) cmbLibro.getSelectedItem();

        // Validar Stock
        if (libro.getStock() <= 0) {
            alertarInconsistencia("El libro seleccionado no tiene stock disponible.");
            return;
        }

        // Confirmar operación
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea registrar el siguiente préstamo?\n\n" +
                        "ESTUDIANTE\n" +
                        estudiante + "\n\n" +
                        "LIBRO\n" +
                        libro.resumen(),
                "Confirmar préstamo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // Registraremos el préstamo usando un hilo adicional para evitar el bloqueo de la ventana.
        // Además esto nos permite la gestión de concurrencia global de prestamos y devoluciones
        Prestamo prestamo = new Prestamo(
                estudiante,
                libro,
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        // Deshabilitar botón para evitar solicitudes duplicadas
        btnRegistrarPrestamo.setEnabled(false);

        Thread hilo = new Thread(() -> {
            String resultado;

            try {
                // Simular una operación lenta de 5 segundos
                Thread.sleep(5000);

                // Ejecutar operación en segundo plano
                resultado = controladorPrestamos.agregarNuevoPrestamo(prestamo);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                resultado = "Operación interrumpida.";

            } catch (Exception e) {
                resultado = "Error al registrar el préstamo.";
                e.printStackTrace();
            }

            // Actualizar interfaz en el hilo de Swing
            String mensajeFinal = resultado;

            SwingUtilities.invokeLater(() -> {
                try {
                    informarResultado(mensajeFinal);
                    limpiar();
                } finally {
                    btnRegistrarPrestamo.setEnabled(true);
                }
            });
        });
        hilo.start();

    }


    /**
     * Actualiza el contenido de la tabla utilizando los registros actualmente
     * almacenados en la base de datos.
     */
    public void actualizarTabla(){
        // Primero borramos los registros cargados
        modeloTabla.setRowCount(0);

        // Luego se cargan los registros
        for(Prestamo prestamo : controladorPrestamos.getPrestamos()){
            modeloTabla.addRow(new Object[]{
                    prestamo.getId(),
                    prestamo.getEstudiante(),
                    prestamo.getLibro(),
                    prestamo.getFecha_prestamo(),
                    prestamo.getFecha_devolucion(),
                    prestamo.getDevuelto(),
                    prestamo.getAtraso()
            });

        }
    }

    /**
     * Verifica que los campos necesarios para registrar o modificar un registro
     * contengan valores válidos.
     *
     * @return true si todos los campos son válidos; false en caso contrario
     */
    public boolean validarCampos(){

        // Validación de Estudiante seleccionada
        if (cmbEstudiante.getSelectedIndex() == -1){
            alertarInconsistencia("Debe seleccionar un Estudiante");
            return false;
        }

        // Validación de Libro seleccionado
        if (cmbLibro.getSelectedIndex() == -1){
            alertarInconsistencia("Debe seleccionar un Libro");
            return false;
        }

        return true;
    }

    /**
     * Restablece los campos del formulario y elimina la selección actual
     * de la tabla.
     */
    public void limpiar(){
        cargaSelectores();
        registroSeleccionado = -1;
        tblPrestamos.clearSelection();
        actualizarTabla();
    }


    /**
     * Muestra al usuario una advertencia asociada a una inconsistencia
     * detectada en los datos o en la operación solicitada.
     *
     * @param alerta mensaje que se desea mostrar
     */
    public void alertarInconsistencia(String alerta) {
        JOptionPane.showMessageDialog(
                this,
                alerta,
                "Alerta",
                JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Muestra al usuario un mensaje con el resultado de una operación.
     *
     * @param mensaje mensaje que se desea mostrar
     */
    private void informarResultado(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Resultado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
