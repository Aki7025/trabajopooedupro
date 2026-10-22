package vitalfit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Conexion {
    private static final String URL =
        "jdbc:mysql://localhost:3306/colegio_poo?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "Root";
    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
        return conexion;    }}
