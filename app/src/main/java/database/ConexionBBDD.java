package database;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBBDD {
    private Connection conexion = null;
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "postgres://fl0user:UuRl53nrfIeX@ep-gentle-pine-a2mzw25l.eu-central-1.aws.neon.fl0.io:5432/cooktogether?sslmode=require";
    private static final String USUARIO = "fl0user";
    private static final String PASSWORD = "UuRl53nrfIeX";

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

    /*public void cerrarConexion(Connection conexion)throws Exception{
        conexion.close();
    }*/
}
