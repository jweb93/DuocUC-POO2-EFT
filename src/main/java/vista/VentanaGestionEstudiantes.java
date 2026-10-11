package vista;

import controlador.ControladorEstudiantes;
import modelo.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaGestionEstudiantes extends JFrame{
    private JPanel panelEstudiantes;
    private JTextField txtCorreo;
    private JTextField txtRut;
    private JTextField txtNombre;
    private JTextField txtCurso;
    private JButton btnEditar;
    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JTable tblEstudiantes;
    private JLabel lblNombre;
    private JLabel lblRut;
    private JLabel lblCurso;
    private JLabel lblCorreo;

    private ControladorEstudiantes controladorEstudiantes;
    private DefaultTableModel modeloTabla;
    private int registroSeleccionado = -1; // Índice del registro seleccionado en tabla

    public VentanaGestionEstudiantes(){
        controladorEstudiantes = new ControladorEstudiantes();

        // Configuración base de la ventana
        setContentPane(panelEstudiantes); // mostrar el contenido del UI Form
        setTitle("Gestión de estudiantes - Biblioteca");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración de tabla
        String[] columnas = {"ID", "Nombre", "Rut", "Curso", "Correo"};
        modeloTabla = new DefaultTableModel(columnas,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false; // Las celdas no son editables
            }
        };

        tblEstudiantes.setModel(modeloTabla);
        tblEstudiantes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo se puede seleccionar 1 fila

        tblEstudiantes.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int fila = tblEstudiantes.getSelectedRow();
                if (fila >= 0){
                    registroSeleccionado = fila;

                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtRut.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtCurso.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtCorreo.setText(modeloTabla.getValueAt(fila, 4).toString());
                }
            }
        });

        // Configuración de los botones
        btnEditar.addActionListener(e -> editarEstudiante());
        btnAgregar.addActionListener(e -> agregarEstudiante());
        btnEliminar.addActionListener(e -> eliminarEstudiante());
        btnLimpiar.addActionListener(e -> limpiar());

        // Poblamiento de la tabla
        actualizarTabla();
        limpiar();

    }

    /**
     * Valida los datos ingresados y actualiza el registro actualmente
     * seleccionado en la tabla.
     */
    public void editarEstudiante(){
        // Validar que exista selección de registro
        if(registroSeleccionado == -1){
            alertarInconsistencia("Selecciona un registro para editar.");
            return;
        }

        // Validar formulario
        if (!validarCampos()) return;

        // Obtener valores
        int id = Integer.parseInt(modeloTabla.getValueAt(registroSeleccionado, 0).toString());
        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        // Actualizar e informar resultado
        String resultado = controladorEstudiantes.actualizarEstudiante(new Estudiante(id, nombre, rut, curso, correo));
        informarResultado(resultado);

        limpiar();
        actualizarTabla();
    }

    /**
     * Valida los datos del formulario y crea un nuevo registro.
     */
    public void agregarEstudiante(){
        // Validar formulario
        if (!validarCampos()) return;

        // Obtener valores
        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        String resultado = controladorEstudiantes.agregarNuevoEstudiante(new Estudiante(nombre, rut, curso, correo));
        informarResultado(resultado);

        limpiar();
        actualizarTabla();
    }

    /**
     * Solicita confirmación al usuario y elimina el registro seleccionado.
     */
    public void eliminarEstudiante(){
        int fila = tblEstudiantes.getSelectedRow();

        // Validar que exista selección de registro
        if(registroSeleccionado == -1){
            alertarInconsistencia("Selecciona un registro para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas eliminar este registro?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            String resultado = controladorEstudiantes.eliminaEstudiante(new Estudiante(id));
            informarResultado(resultado);

            limpiar();
            actualizarTabla();
        }
    }

    /**
     * Actualiza el contenido de la tabla utilizando los registros actualmente
     * almacenados en la base de datos.
     */
    public void actualizarTabla(){
        // Primero borramos los registros cargados
        modeloTabla.setRowCount(0);

        // Luego se cargan los registros
        for(Estudiante estudiante : controladorEstudiantes.getEstudiantes()){
            modeloTabla.addRow(new Object[]{
                    estudiante.getId(),
                    estudiante.getNombre(),
                    estudiante.getRut(),
                    estudiante.getCurso(),
                    estudiante.getCorreo()
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
        // Validación de nombre no vacío
        if (txtNombre.getText().trim().isEmpty()){
            alertarInconsistencia("Debe completar el nombre del estudiante");
            return false;
        }

        // Validación de Rut no vacío
        if (txtRut.getText().trim().isEmpty()){
            alertarInconsistencia("Debe completar el Rut del estudiante");
            return false;
        }

        // Validar formato Rut
        if (!txtRut.getText().matches("[0-9]+-[0-9kK]")){
            alertarInconsistencia("El formato Rut no es correcto");
            return false;
        }

        // Validación de curso no vacío
        if (txtCurso.getText().trim().isEmpty()){
            alertarInconsistencia("Debe completar el curso del estudiante");
            return false;
        }

        // Validación de correo no vacía
        if (txtCorreo.getText().trim().isEmpty()){
            alertarInconsistencia("Debe completar el correo del estudiante");
            return false;
        }

        // Validación del formato de correo con @ y punto posterior
        if (!txtCorreo.getText().matches(".+@.+\\..+")){
            alertarInconsistencia("El correo no es valido");
            return false;
        }

        return true;
    }

    /**
     * Restablece los campos del formulario y elimina la selección actual
     * de la tabla.
     */
    public void limpiar(){
        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");

        registroSeleccionado = -1;
        tblEstudiantes.clearSelection();
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
