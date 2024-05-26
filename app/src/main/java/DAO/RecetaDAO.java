package DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import database.ConexionBBDD;
import models.RecetaModel;
import models.UserModel;

public class RecetaDAO{
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

    public void insertarReceta(RecetaModel receta) throws SQLException {
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        String query="INSERT INTO recetas (titulo, descripcion, ingredientes, instrucciones, usuario, puntuacion_media, foto_receta, categoria, fecha_hora)\n" +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement sentencia = connection.prepareStatement(query);
            sentencia.setString(1,receta.getTitulo());
            sentencia.setString(2, receta.getDescripcion());
            sentencia.setString(3, receta.getIngredientes());
            sentencia.setString(4, receta.getInstrucciones());
            sentencia.setString(5, receta.getUsuario());
            sentencia.setDouble(6,receta.getPuntuacionMedia());
            sentencia.setBytes(7, receta.getFotoReceta());
            sentencia.setString(8, receta.getCategoria());
            sentencia.setTimestamp(9, receta.getFechaHora());
            sentencia.execute();
        }catch (SQLException e){
            throw new SQLException("Error al insertar la receta.");
        }finally {
            closeDBConnection();
        }
    }

    public void editarReceta(RecetaModel receta) throws SQLException{
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        String query = "UPDATE recetas SET titulo = ?, descripcion = ?, ingredientes = ?, instrucciones = ?, foto_receta = ?, categoria = ?, fecha_hora = ? WHERE id = ?";
        try{
            PreparedStatement sentencia = connection.prepareStatement(query);
            sentencia.setString(1,receta.getTitulo());
            sentencia.setString(2, receta.getDescripcion());
            sentencia.setString(3, receta.getIngredientes());
            sentencia.setString(4, receta.getInstrucciones());
            sentencia.setBytes(5, receta.getFotoReceta());
            sentencia.setString(6, receta.getCategoria());
            sentencia.setTimestamp(7, receta.getFechaHora());
            sentencia.setInt(8, receta.getIdReceta());
            sentencia.execute();
        }catch (SQLException e){
            throw new SQLException("Error al editar la receta.");
        }finally {
            closeDBConnection();
        }
        closeDBConnection();
    }

    public RecetaModel buscarReceta(String titulo, String usuario) throws SQLException {
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        RecetaModel recetaModel = null;
        // Inicializar la conexión a la base de datos
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        // Preparar y ejecutar la consulta SQL
        String query = "SELECT * FROM recetas WHERE titulo = ? AND usuario = ?";
        sentencia = connection.prepareStatement(query);
        sentencia.setString(1, titulo);
        sentencia.setString(2, usuario);
        rs = sentencia.executeQuery();
        // Verificar si se encontró la receta y crear el objeto RecetaModel
        if (rs.next()) {
            recetaModel = new RecetaModel(
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("ingredientes"),
                    rs.getString("instrucciones"),
                    rs.getString("usuario"),
                    rs.getDouble("puntuacion_media"),
                    rs.getBytes("foto_receta"),
                    rs.getInt("id"),
                    rs.getString("categoria"),
                    rs.getTimestamp("fecha_hora"));
        } else {
            throw new SQLException("No existe la receta indicada.");
        }
        closeDBConnection();
        return recetaModel;
    }

    public ArrayList<RecetaModel> obtenerRecetasFiltros(String filtro) throws SQLException{
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        String query = "SELECT * FROM recetas;";
        RecetaModel recetaModel = null;
        ArrayList<RecetaModel> recetas = new ArrayList<>();
        if(filtro.equals("Recientes")){
            query = "SELECT * FROM recetas ORDER BY fecha_hora DESC;";
        }

        PreparedStatement sentencia = connection.prepareStatement(query);
        ResultSet rs = sentencia.executeQuery();
        while (rs.next()){
            recetaModel = new RecetaModel(
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("ingredientes"),
                    rs.getString("instrucciones"),
                    rs.getString("usuario"),
                    rs.getDouble("puntuacion_media"),
                    rs.getBytes("foto_receta"),
                    rs.getInt("id"),
                    rs.getString("categoria"),
                    rs.getTimestamp("fecha_hora"));

            if(recetaModel!=null){
                recetas.add(recetaModel);
            }
        }
        closeDBConnection();
        return recetas;
    }

    public ArrayList<RecetaModel> obtenerRecetas() throws SQLException{
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        RecetaModel recetaModel = null;
        ArrayList<RecetaModel> recetas = new ArrayList<>();
        String query = "SELECT * FROM recetas;";
        PreparedStatement sentencia = connection.prepareStatement(query);

        ResultSet rs = sentencia.executeQuery();
        while (rs.next()){
            recetaModel = new RecetaModel(
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("ingredientes"),
                    rs.getString("instrucciones"),
                    rs.getString("usuario"),
                    rs.getDouble("puntuacion_media"),
                    rs.getBytes("foto_receta"),
                    rs.getInt("id"),
                    rs.getString("categoria"),
                    rs.getTimestamp("fecha_hora"));

            if(recetaModel!=null){
                recetas.add(recetaModel);
            }
        }
        closeDBConnection();
        return recetas;
    }

    public ArrayList<RecetaModel> obtenerRecetasUsuario(String user) throws SQLException{
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        RecetaModel recetaModel = null;
        ArrayList<RecetaModel> recetas = new ArrayList<>();
        String query = "SELECT * FROM recetas WHERE usuario = ?;";
        PreparedStatement sentencia = connection.prepareStatement(query);
        sentencia.setString(1, user);
        ResultSet rs = sentencia.executeQuery();
        while (rs.next()){
            recetaModel = new RecetaModel(
                    rs.getString("titulo"),
                    rs.getString("descripcion"),
                    rs.getString("ingredientes"),
                    rs.getString("instrucciones"),
                    rs.getString("usuario"),
                    rs.getDouble("puntuacion_media"),
                    rs.getBytes("foto_receta"),
                    rs.getInt("id"),
                    rs.getString("categoria"),
                    rs.getTimestamp("fecha_hora"));

            if(recetaModel!=null){
                recetas.add(recetaModel);
            }
        }
        closeDBConnection();
        return recetas;
    }

    public void eliminarReceta(int recetaId) throws SQLException {
        if (!initDBConnection()) {
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        String query = "DELETE FROM recetas WHERE id = ?";
        try {
            PreparedStatement sentencia = connection.prepareStatement(query);
            sentencia.setInt(1, recetaId);
            sentencia.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la receta.");
        } finally {
            closeDBConnection();
        }
    }

    public void insertarValoracion(int usuarioId, int recetaId, double puntuacion) throws SQLException {
        if (!initDBConnection()) {
            throw new SQLException("No se pudo conectar a la base de datos.");
        }

        String query = "INSERT INTO valoraciones (usuario_id, receta_id, puntuacion) VALUES (?, ?, ?)";

        try {
            PreparedStatement sentencia = connection.prepareStatement(query);
            sentencia.setInt(1, usuarioId);
            sentencia.setInt(2, recetaId);
            sentencia.setDouble(3, puntuacion);
            sentencia.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al valorar la receta.");
        } finally {
            closeDBConnection();
        }
    }
}
