package vista;

import controlador.ControladorUsuarios;
import modelo.Rol;
import modelo.Usuario;

import javax.swing.*;
/**
 * Ventana de VentanaLogin
 * Valida credenciales y apertura interfaz según rol de usuario
 */
public class VentanaLogin extends JFrame{
    private JPanel panelLogin;
    private JLabel lblLogin;
    private JTextField txtRut;
    private JPasswordField txtContrasenia;
    private JLabel lblRut;
    private JLabel lblContrasenia;
    private JButton btnAcceder;
    private JButton btnCancelar;

    private ControladorUsuarios controladorUsuarios;

    public VentanaLogin(){
        controladorUsuarios = new ControladorUsuarios();

        // Configuración base de la ventana
        setContentPane(panelLogin); // mostrar el contenido del UI Form
        setTitle("Login - Biblioteca");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración de los botones
        btnCancelar.addActionListener(e -> limpiar());
        btnAcceder.addActionListener(e -> acceder());

    }

    private void acceder() {
        if (!validarCampos()) return;

        // Obtener valores
        String rut = txtRut.getText().trim();
        String contrasenia = new String(txtContrasenia.getPassword()).trim();

        Usuario usuario = controladorUsuarios.autenticar(rut, contrasenia);

        if (usuario != null){
            informarResultado("Bienvenid@, " + usuario.getRol().getDescripcion());
            if (usuario.getRol() == Rol.BIBLIOTECARIO){
                new VentanaPrincipalBibliotecario().setVisible(true);
            }
            else{
                new VentanaPrincipalEstudiante().setVisible(true);
            }
            this.dispose();
        }else{
            informarResultado("Usuario o contraseña incorrectos");
            limpiar();
        }

    }

    private boolean validarCampos() {
        // Validación de rut no vacío
        if (txtRut.getText().trim().isEmpty()){
            alertarInconsistencia("Debe completar el rut del usuario");
            return false;
        }

        // Validación de contraseña no vacía (los espacios cuentan como caracter)
        if (new String(txtContrasenia.getPassword()).isEmpty()){
            alertarInconsistencia("Debe completar la contraseña del usuario");
            return false;
        }

        return true;
    }

    public void limpiar(){
        txtRut.setText("");
        txtContrasenia.setText("");
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
