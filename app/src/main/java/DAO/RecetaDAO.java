package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.ConexionBBDD;
import models.RecetaModel;

public class RecetaDAO {
    private ConexionBBDD conexionBBDD = new ConexionBBDD();
    private Connection connection;
    private boolean initDBConnection(){
        connection = conexionBBDD.conectarDB();
        if (connection != null) {
            return true;
        }else {
            return false;
        }
    }

    private void closeDBConnection(){
        try {
            conexionBBDD.cerrarConexion(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insertarReceta(RecetaModel receta) throws SQLException {
        boolean insertarReceta = false;
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        String query="INSERT INTO recetas (titulo, descripcion, ingredientes, instrucciones, usuario_id, puntuacion_media, foto_receta)\n" +
                "VALUES (?,?,?,?,?,?,?)";
        try{
            PreparedStatement sentencia = connection.prepareStatement(query);
            sentencia.setString(1,receta.getTitulo());
            sentencia.setString(2, receta.getDescripcion());
            sentencia.setString(3, receta.getIngredientes());
            sentencia.setString(4, receta.getInstrucciones());
            sentencia.setInt(5, receta.getUsuarioId());
            sentencia.setDouble(6,receta.getPuntuacionMedia());
            sentencia.setBytes(7, receta.getFotoReceta());
            insertarReceta = true;
            sentencia.execute();
        }catch (SQLException e){
            throw new SQLException("Error al insertar la receta.");
        }finally {
            closeDBConnection();
        }
        return insertarReceta;
    }
}
