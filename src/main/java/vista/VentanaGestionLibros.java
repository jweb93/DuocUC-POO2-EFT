package vista;

import controlador.ControladorCategorias;
import controlador.ControladorLibros;
import modelo.Categoria;
import modelo.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaGestionLibros extends JFrame{
    private JPanel panelLibros;
    private JButton btnEditar;
    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JTable tblLibros;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtISBN;
    private JTextField txtEditorial;
    private JSpinner spStock;
    private JLabel lblTitulo;
    private JLabel lblAutor;
    private JLabel lblISBN;
    private JLabel lblEditorial;
    private JLabel lblStock;
    private JComboBox<Categoria> cmbCategoria;

    private ControladorLibros controladorLibros;
    private ControladorCategorias controladorCategorias;
    private DefaultTableModel modeloTabla;
    private int registroSeleccionado = -1; // Índice del registro seleccionado en tabla

    public VentanaGestionLibros(){
        controladorLibros = new ControladorLibros();
        controladorCategorias = new ControladorCategorias();

        // Configuración base de la ventana
        setContentPane(panelLibros); // mostrar el contenido del UI Form
        setTitle("Gestión de libros - Biblioteca");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Selectores
        for(Categoria categoria: controladorCategorias.getCategorias()){
            cmbCategoria.addItem(categoria);
        }
        spStock.setModel(new SpinnerNumberModel(0, 0, 1000, 1));

        // Configuración de tabla
        String[] columnas = {"ID", "Título", "Autor", "ISBN", "Editorial", "Stock", "Categoría"};
        modeloTabla = new DefaultTableModel(columnas,0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false; // Las celdas no son editables
            }
        };

        tblLibros.setModel(modeloTabla);
        tblLibros.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Solo se puede seleccionar 1 fila
        tblLibros.getColumnModel().getColumn(0).setPreferredWidth(20);  // ID
        tblLibros.getColumnModel().getColumn(1).setPreferredWidth(200); // titulo
        tblLibros.getColumnModel().getColumn(2).setPreferredWidth(200); // autor
        tblLibros.getColumnModel().getColumn(2).setPreferredWidth(200); // isbn

        tblLibros.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int fila = tblLibros.getSelectedRow();
                if (fila >= 0){
                    registroSeleccionado = fila;

                    txtTitulo.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtAutor.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtISBN.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtEditorial.setText(modeloTabla.getValueAt(fila, 4).toString());
                    spStock.setValue(modeloTabla.getValueAt(fila, 5));

                    Categoria categoria = (Categoria) modeloTabla.getValueAt(fila, 6);

                    // Limpiar selección anterior
                    cmbCategoria.setSelectedIndex(-1);

                    // Buscar categoría por ID
                    for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
                        Categoria item = cmbCategoria.getItemAt(i);

                        if (item.getId() == categoria.getId()) {
                            cmbCategoria.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });

        // Configuración de los botones
        btnEditar.addActionListener(e -> editarLibro());
        btnAgregar.addActionListener(e -> agregarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiar());

        // Poblamiento de la tabla
        actualizarTabla();
        limpiar();

    }

    /**
     * Valida los datos ingresados y actualiza el registro actualmente
     * seleccionado en la tabla.
     */
    public void editarLibro(){
        // Validar que exista selección de registro
        if(registroSeleccionado == -1){
            alertarInconsistencia("Selecciona un registro para editar.");
            return;
        }

        // Validar formulario
        if (!validarCampos()) return;

        // Obtener valores
        int id = Integer.parseInt(modeloTabla.getValueAt(registroSeleccionado, 0).toString());
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtISBN.getText().trim();
        String editorial = txtEditorial.getText().trim();
        int stock = (Integer) spStock.getValue();
        Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();

        // Actualizar e informar resultado
        String resultado = controladorLibros.actualizarLibro(new Libro(id, titulo, autor, isbn, editorial, stock, categoria));
        informarResultado(resultado);

        limpiar();
        actualizarTabla();
    }

    /**
     * Valida los datos del formulario y crea un nuevo registro.
     */
    public void agregarLibro(){
        // Validar formulario
        if (!validarCampos()) return;

        // Obtener valores
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtISBN.getText().trim();
        String editorial = txtEditorial.getText().trim();
        int stock = (Integer) spStock.getValue();
        Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();

        String resultado = controladorLibros.agregarNuevoLibro(new Libro(titulo, autor, isbn, editorial, stock, categoria));
        informarResultado(resultado);

        limpiar();
        actualizarTabla();
    }

    /**
     * Solicita confirmación al usuario y elimina el registro seleccionado.
     */
    public void eliminarLibro(){
        int fila = tblLibros.getSelectedRow();

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
            String resultado = controladorLibros.eliminaLibro(new Libro(id));
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
        for(Libro libro : controladorLibros.getLibros()){
            modeloTabla.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getIsbn(),
                    libro.getEditorial(),
                    libro.getStock(),
                    libro.getCategoria()
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
        // Validación de titulo no vacío
        if (txtTitulo.getText().trim().isEmpty()){
            alertarInconsistencia("Debe completar el título del libro");
            return false;
        }

        // Validación de autor no vacío
        if (txtAutor.getText().trim().isEmpty()){
            alertarInconsistencia("Debe completar el autor del libro");
            return false;
        }

        // Validación de ISBN no vacío
        if (txtISBN.getText().trim().isEmpty()){
            alertarInconsistencia("Debe completar el ISBN del libro");
            return false;
        }

        // Validación de editorial no vacía
        if (txtEditorial.getText().trim().isEmpty()){
            alertarInconsistencia("Debe completar la editorial del libro");
            return false;
        }

        // Validación de stock no vacío, entero y positivo: esto es automatico con el uso de JSpinner y su instancia configurada

        // Validación de categoria seleccionada
        if (cmbCategoria.getSelectedIndex() == -1){
            alertarInconsistencia("Debe completar la categoria del libro");
            return false;
        }

        return true;
    }

    /**
     * Restablece los campos del formulario y elimina la selección actual
     * de la tabla.
     */
    public void limpiar(){
        txtTitulo.setText("");
        txtAutor.setText("");
        txtISBN.setText("");
        txtEditorial.setText("");
        spStock.setValue(0);
        cmbCategoria.setSelectedIndex(-1);

        registroSeleccionado = -1;
        tblLibros.clearSelection();
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
