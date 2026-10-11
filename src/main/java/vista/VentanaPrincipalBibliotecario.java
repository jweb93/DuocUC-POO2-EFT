package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal para un usuario con rol Bibliotecario.
 */
public class VentanaPrincipalBibliotecario extends JFrame {
    private JButton btnGestionarLibros;
    private JButton btnGestionarEstudiantes;
    private JButton btnGestionarPrestamos;
    private JButton btnReportes;

    public VentanaPrincipalBibliotecario() {
        // Configuración base de la ventana
        setTitle("Menú Principal- Biblioteca");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // En su interior tendrá un panel central con 4 botones
        setLayout(new BorderLayout(10, 10));

        // El panel de botones tendrá una estructura de 4 filas y 1 columna
        JPanel panelBotones = new JPanel(new GridLayout(4, 1));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnGestionarLibros = new JButton("Gestión de Libros");
        btnGestionarEstudiantes = new JButton("Gestión de Estudiantes");
        btnGestionarPrestamos = new JButton("Gestión de Préstamos");
        btnReportes = new JButton("Reportes");


        panelBotones.add(btnGestionarLibros);
        panelBotones.add(btnGestionarEstudiantes);
        panelBotones.add(btnGestionarPrestamos);
        panelBotones.add(btnReportes);

        add(panelBotones, BorderLayout.CENTER);

        // Acciones de los botones

        btnGestionarLibros.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                VentanaGestionLibros ventanaGestionLibros = new VentanaGestionLibros();
                ventanaGestionLibros.setLocationRelativeTo(this);
                ventanaGestionLibros.setVisible(true);
            });
        });

        btnGestionarEstudiantes.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                VentanaGestionEstudiantes ventanaGestionEstudiantes = new VentanaGestionEstudiantes();
                ventanaGestionEstudiantes.setLocationRelativeTo(this);
                ventanaGestionEstudiantes.setVisible(true);
            });
        });

        btnGestionarPrestamos.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                VentanaGestionPrestamos ventanaGestionPrestamos = new VentanaGestionPrestamos();
                ventanaGestionPrestamos.setLocationRelativeTo(this);
                ventanaGestionPrestamos.setVisible(true);
            });
        });

        btnReportes.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                VentanaReportes ventanaReportes = new VentanaReportes();
                ventanaReportes.setLocationRelativeTo(this);
                ventanaReportes.setVisible(true);
            });
        });


    }
}
