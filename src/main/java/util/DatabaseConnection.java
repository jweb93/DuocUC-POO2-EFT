package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * clase utilitaria para establecer una única conexión a bbdd siguiendo patron Singleton:
 * 1. variable estática que guarda la única instancia
 * 2. constructor privado
 * 3. método estático y público para obtener la instancia
 * Incluye comentarios extensos para servir de notas personales de estudio
 */

public class DatabaseConnection {
    // -------- 1. Variable estática que guarda la única instancia
    // Esto puede sonar recursivo, pero declararemos una variable llamada "instancia" (podría tener cualquier
    // nombre en realidad) que será capáz de almacenar una referencia a un objeto de tipo DatabaseConnection
    // es decir, de la clase que estamos definiendo (esto parece recursivo).
    private static DatabaseConnection instancia;
    // Como aun no está instanciada, su valor es null.
    // Al ser privada, solo se podrá acceder desde esta clase
    // Al ser estatica, pertenece a la clase y no a cada objeto creado con esta clase (molde)

    // El singleton puede tener otros atributos propios.
    // En este caso, las credenciales a bbdd local, y el objeto Connection
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "root";
    private Connection connection;

    // -------- 2. Constructor privado
    private DatabaseConnection() throws SQLException{
        connection = DriverManager.getConnection(
                URL,
                USUARIO,
                CONTRASENA
        );
    }
    // El constructor de clase es privado para evitar que se creen otras instancias desde otras clases.
    // Lo anterior significa que no podremos hacer:  new DatabaseConnection();
    // desde otras clases, por ejemplo, la clase Main. Como cuando se hace: Usuario user1 = new Usuario();
    // Al ser privado, sólo se podrá usar dentro de esta misma clase (DatabaseConnection)

    // Entonces, la estrategia para crear la primera y única instancia y acceder posteriormente a la misma
    // desde otras clases, es creando un método que sea publico (accesible desde otras clases)
    //  y estático (no requiere una instancia para usarse), que invoque el constructor
    //  para crear la primera y única instancia almacenada en la variable
    // "instancia" que creamos al inicio.
    // Cuando se vuelva a invocar este método público, se retornará la instancia ya creada.

    // -------- 3. Método estático y público para obtener la primera y única instancia
    public static DatabaseConnection getInstancia() throws SQLException {
        if (instancia == null){
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    // Con todo lo anterior, es posible crear la primera y única instancia de DatabaseConnection
    // referenciada en la variable "instancia" desde otras clases haciendo: DatabaseConnection.getInstance()
    // Ese método retorna la referencia de "instancia" y puede almacenarse en una variable de clase externa:
    // DatabaseConnection variableExterna = DatabaseConnection.getInstance();

    // y para acceder a la variable "connection" que estará asociada a la instancia, creamos un método publico
    // que permita obtenerla

    public Connection getConnection() {
        return connection;
    }

    // Finalmente, es posible obtener esta única conexión haciendo:
    // DatabaseConnection variableExterna = DatabaseConnection.getInstance().getConnection();

    // Notar que el constructor puede generar una Excepcion SQL (si por ejemplo no fue posible la conexión).
    // Esta excepción se rescata con "throws SQLException" y se le pasa al método que use dicho constructor.
    // Justamente el método getInstancia() es quien usa al constructor y en caso de recibir esta Excepción,
    // nuevamente la levanta a quien invoque ese método, es decir, a las clases externas.
    // Por lo tanto, las clases externas tienen que gestionar esa excepción cuando busquen acceder a la instancia

    // Adicionalmente se agrega un método para cerrar formalmente la conexión. Esto ya que la obtención de la instancia
    // no forma parte de los try with resource de las clases DAOImpl y por tanto no tiene un cierre manual. A su vez,
    // la instancia no forma parte de esos try with resource porque no debe cerrarse luego de una interacción a bbdd, pues
    // otros métodos la necesitarán.
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
