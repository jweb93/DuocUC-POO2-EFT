package vista;

import util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/**
 * Ventana principal para un usuario con rol Estudiante.
 */
public class VentanaPrincipalEstudiante extends JFrame {
    private JButton btnGestionarPrestamos;

    public VentanaPrincipalEstudiante() {
        // Configuración base de la ventana
        setTitle("Menú Principal- Biblioteca");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cierre del conector a BBDD al cerrar esta ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                try {
                    DatabaseConnection.getInstancia().closeConnection();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                dispose();
                System.exit(0);
            }
        });

        // En su interior tendrá un panel central con 1 botones
        setLayout(new BorderLayout(10, 10));

        // El panel de botones tendrá una estructura de 1 filas y 1 columna
        JPanel panelBotones = new JPanel(new GridLayout(1, 1));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnGestionarPrestamos = new JButton("Gestión de Préstamos");

        panelBotones.add(btnGestionarPrestamos);

        add(panelBotones, BorderLayout.CENTER);

        // Acciones de los botones
        btnGestionarPrestamos.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                VentanaGestionPrestamos ventanaGestionPrestamos = new VentanaGestionPrestamos();
                ventanaGestionPrestamos.setLocationRelativeTo(this);
                ventanaGestionPrestamos.setVisible(true);
            });
        });

    }
}
