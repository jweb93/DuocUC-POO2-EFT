package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal para un usuario con rol Estudiante.
 */
public class VentanaPrincipalEstudiante extends JFrame {
    private JButton btnGestionarPrestamos;

    public VentanaPrincipalEstudiante() {
        // Configuración base de la ventana
        setTitle("Menú Principal- Biblioteca");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
