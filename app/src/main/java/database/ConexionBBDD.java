package database;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBBDD {

    private Connection conexion = null;
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://10.0.2.2:5432/cooktogether";
    private static final String USUARIO = "postgres";
    String hola = "";
    private static final String PASSWORD = "admin";

    //Creamos nuestra funcion para conectarnos a Postgresql
    public Connection conectarDB(){

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        }catch (SQLException e){
            System.out.println("Error en la conexión a BBDD");
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            System.out.println("Error con el driver de BBDD");
            e.printStackTrace();
        }
        return conexion;
    }

    public void cerrarConexion(Connection conexion)throws Exception{
        conexion.close();
    }




}
