package vista;

import controlador.ControladorEstudiantes;
import controlador.ControladorLibros;
import controlador.ControladorPrestamos;
import controlador.ControladorReportes;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import modelo.reportes.ReporteLibrosMasPrestados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaReportes extends JFrame {
    private JPanel panelReportes;
    private JLabel lblReporte2;
    private JComboBox cmbEstudiante;
    private JButton btnConsultarReporte2;
    private JLabel lblEstudiante;
    private JTable tblReporte2;
    private JLabel lblReporte1;
    private JButton btnConsultarReporte1;
    private JTable tblReporte1;
    private JButton btnConsultarReporte3;
    private JTable tblReporte3;
    private JLabel lblReporte3;

    private ControladorLibros controladorLibros;
    private ControladorEstudiantes controladorEstudiantes;
    private ControladorPrestamos controladorPrestamos;
    private ControladorReportes controladorReportes;
    private DefaultTableModel modeloTabla1;
    private DefaultTableModel modeloTabla2;
    private DefaultTableModel modeloTabla3;

    public VentanaReportes() {
        controladorPrestamos = new ControladorPrestamos();
        controladorEstudiantes = new ControladorEstudiantes();
        controladorLibros = new ControladorLibros();
        controladorReportes = new ControladorReportes();

        // Configuración base de la ventana
        setContentPane(panelReportes); // mostrar el contenido del UI Form
        setTitle("Reportes - Biblioteca");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Selectores
        cargaSelectores();

        // Configuración de Tabla Reporte 1
        String[] columnas1 = {"Ranking", "Título", "Autor", "Prestamos"};
        modeloTabla1 = new DefaultTableModel(columnas1,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false; // Las celdas no son editables
            }
        };
        tblReporte1.setModel(modeloTabla1);
        tblReporte1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblReporte1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo se puede seleccionar 1 fila

        // Configuración de Tabla Reporte 2
        String[] columnas2 = {"N°", "Libro", "Fecha Préstamo", "Fecha Devolución", "Devolución"};
        modeloTabla2 = new DefaultTableModel(columnas2,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false; // Las celdas no son editables
            }
        };
        tblReporte2.setModel(modeloTabla2);
        tblReporte2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblReporte2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo se puede seleccionar 1 fila

        // Configuración de Tabla Reporte 3
        String[] columnas3 = {"N°", "Estudiante", "Libro", "Fecha Préstamo", "Fecha Devolución"};
        modeloTabla3 = new DefaultTableModel(columnas3,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false; // Las celdas no son editables
            }
        };
        tblReporte3.setModel(modeloTabla3);
        tblReporte3.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblReporte3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo se puede seleccionar 1 fila

        // Configuración de los botones
        btnConsultarReporte1.addActionListener(e -> consultarReporte1());
        btnConsultarReporte2.addActionListener(e -> consultarReporte2());
        btnConsultarReporte3.addActionListener(e -> consultarReporte3());

    }

    /**
     * Actualiza el contenido de la tabla Reporte 1 utilizando los registros actualmente
     * almacenados en la base de datos.
     */
    public void consultarReporte1(){
        // Primero borramos los registros cargados
        modeloTabla1.setRowCount(0);

        // Luego se cargan los registros
        int i = 1;
        for(ReporteLibrosMasPrestados reporteLibrosMasPrestados : controladorReportes.getReporte1()){
            modeloTabla1.addRow(new Object[]{
                    i,
                    reporteLibrosMasPrestados.getLibro().getTitulo(),
                    reporteLibrosMasPrestados.getLibro().getAutor(),
                    reporteLibrosMasPrestados.getCantidadPrestamos()
            });
            i++;
        }
    }


    /**
     * Actualiza el contenido de la tabla de Reporte 2 utilizando los registros actualmente
     * almacenados en la base de datos.
     */
    public void consultarReporte2(){

        // Validar formulario
        if (!validarCampos()) return;

        // Borramos los registros cargados
        modeloTabla2.setRowCount(0);

        // Obtener valores
        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();

        // Luego se cargan los registros
        int i = 1;
        for(Prestamo prestamo : controladorReportes.getReporte2(estudiante)){
            modeloTabla2.addRow(new Object[]{
                    i,
                    prestamo.getLibro().getTitulo(),
                    prestamo.getFecha_prestamo(),
                    prestamo.getFecha_devolucion(),
                    prestamo.getDevuelto(),
            });
            i++;
        }
    }


    /**
     * Actualiza el contenido de la tabla de Reporte 3 utilizando los registros actualmente
     * almacenados en la base de datos.
     */
    public void consultarReporte3(){
        // Primero borramos los registros cargados
        modeloTabla3.setRowCount(0);

        // Luego se cargan los registros
        int i = 1;
        for(Prestamo prestamo : controladorReportes.getReporte3()){
            modeloTabla3.addRow(new Object[]{
                    i,
                    prestamo.getEstudiante().getNombre(),
                    prestamo.getLibro().getTitulo(),
                    prestamo.getFecha_prestamo(),
                    prestamo.getFecha_devolucion()
            });
            i++;
        }
    }

    /**
     * Verifica que los campos necesarios para realizar una consulta
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

        return true;
    }

    /**
     * Permite poblar los selector de Estudiante.
     */
    public void cargaSelectores(){
        // Eliminar elementos anteriores
        cmbEstudiante.removeAllItems();

        // Consultar y poblar selector
        for(Estudiante estudiante: controladorEstudiantes.getEstudiantes()){
            cmbEstudiante.addItem(estudiante);
        }
        cmbEstudiante.setSelectedIndex(-1);
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
