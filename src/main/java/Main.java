import controlador.ControladorUsuarios;
import util.DatabaseConnection;
import vista.VentanaLogin;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase principal de la aplicación SpeedFast.
 * Inicializa los controladores del sistema y muestra la ventana principal
 * de la aplicación mediante el hilo de eventos de Swing.
 * Además incorpora una validación de conexión a bbdd reportada por consola
 */
public class Main {
    //Validación de conexión a bbdd
    public static void main(String[] args) {
        // Validación de conexión a BBDD creada con archivo speedfast_db.sql
        try{
            Connection conn = DatabaseConnection.getInstancia().getConnection();
            System.out.println("✅ Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos:");
            e.printStackTrace();
        }

        // Inicialización de controladores
        ControladorUsuarios controladorUsuarios = new ControladorUsuarios();

        // Apertura de pantalla de login
        SwingUtilities.invokeLater(() -> {
            new VentanaLogin().setVisible(true);

        });
    }


}
