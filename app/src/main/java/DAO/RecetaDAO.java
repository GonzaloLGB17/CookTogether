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

    public void insertarValoracion(int usuarioId, int recetaId, double puntuacion) throws SQLException {
        if (!initDBConnection()) {
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        boolean existe = comprobarValoracion(usuarioId,recetaId);
        if(existe){
            actualizarValoracion(usuarioId,recetaId,puntuacion);
        }else{
            String insertValoracionQuery = "INSERT INTO valoraciones (usuario_id, receta_id, puntuacion) VALUES (?, ?, ?)";
            try {
                // Inserta la valoración
                PreparedStatement sentencia = connection.prepareStatement(insertValoracionQuery);
                sentencia.setInt(1, usuarioId);
                sentencia.setInt(2, recetaId);
                sentencia.setDouble(3, puntuacion);
                sentencia.execute();
            } catch (SQLException e) {
                throw new SQLException("Error al insertar la valoración y actualizar la puntuación media.");
            }
        }
        insertarValoracionMedia(recetaId);
        closeDBConnection();
    }

    public void insertarValoracionMedia(int recetaId) throws SQLException {
        if (!initDBConnection()) {
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        String insertarVal = "UPDATE recetas\n" +
                "    SET puntuacion_media = (\n" +
                "        SELECT AVG(puntuacion)\n" +
                "        FROM valoraciones\n" +
                "        WHERE receta_id = ?\n" +
                "    )\n" +
                "    WHERE id = ?;";
        try {
            PreparedStatement sentencia = connection.prepareStatement(insertarVal);
            sentencia.setInt(1,recetaId);
            sentencia.setInt(2,recetaId);
            sentencia.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar la valoración y actualizar la puntuación media.", e);
        } finally {
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
    }

    public RecetaModel buscarReceta(String titulo, String usuario) throws SQLException {
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        RecetaModel recetaModel = null;
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        String query = "SELECT * FROM recetas WHERE titulo = ? AND usuario = ?";
        sentencia = connection.prepareStatement(query);
        sentencia.setString(1, titulo);
        sentencia.setString(2, usuario);
        rs = sentencia.executeQuery();
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
        String query = "";
        if(filtro.equals("Mayor puntuacion")){
            query = "SELECT * FROM recetas ORDER BY puntuacion_media DESC;";
        }else{
            query = "SELECT * FROM recetas ORDER BY fecha_hora DESC;";
        }
        RecetaModel recetaModel = null;
        ArrayList<RecetaModel> recetas = new ArrayList<>();
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

    public ArrayList<RecetaModel> obtenerRecetasCategorias(String categoria) throws SQLException{
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        RecetaModel recetaModel = null;
        ArrayList<RecetaModel> recetas = new ArrayList<>();
        String query = "SELECT * FROM recetas WHERE categoria = ? ORDER BY puntuacion_media DESC;";
        PreparedStatement sentencia = connection.prepareStatement(query);
        sentencia.setString(1, categoria);
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

    public void eliminarValoracion(int recetaId) throws SQLException {
        if (!initDBConnection()) {
            throw new SQLException("No se pudo conectar a la base de datos.");
        }

        String query = "DELETE FROM valoraciones WHERE receta_id = ?";
        try {
            PreparedStatement sentencia = connection.prepareStatement(query);
            sentencia.setInt(1, recetaId);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar la valoración.", e);
        } finally {
            closeDBConnection();
        }
    }


    public boolean comprobarValoracion(int usuarioId, int recetaId) throws SQLException {
        if (!initDBConnection()) {
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        boolean existe = false;
        String query = "SELECT * FROM valoraciones WHERE usuario_id = ? AND receta_id = ?";

        try {
            PreparedStatement sentencia = connection.prepareStatement(query);
            sentencia.setInt(1, usuarioId);
            sentencia.setInt(2, recetaId);
            ResultSet rs = sentencia.executeQuery();
            if(rs.next()){
                existe = true;
            }
        } catch (SQLException e) {
            throw new SQLException("Error al comprobar la receta.");
        }
        return existe;
    }

    

    public void actualizarValoracion(int usuarioId, int recetaId, double puntuacion) throws SQLException {
        if (!initDBConnection()) {
            throw new SQLException("No se pudo conectar a la base de datos.");
        }

        String query = "UPDATE valoraciones SET puntuacion = ? WHERE usuario_id = ? AND receta_id = ?";
        try {
            PreparedStatement sentencia = connection.prepareStatement(query);
            sentencia.setDouble(1, puntuacion);
            sentencia.setInt(2, usuarioId);
            sentencia.setInt(3, recetaId);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la valoración.");
        } finally {
            closeDBConnection();
        }
    }

    public String obtenerPuntuacionReceta(int idReceta) throws SQLException{
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        double puntuacion = 0;
        // Inicializar la conexión a la base de datos
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        // Preparar y ejecutar la consulta SQL
        String query = "SELECT AVG(puntuacion) AS puntuacion_media FROM valoraciones WHERE receta_id = ? ";
        sentencia = connection.prepareStatement(query);
        sentencia.setInt(1, idReceta);
        rs = sentencia.executeQuery();
        // Verificar si se encontró el usuario y crear el objeto UserModel
        if (rs.next()) {
            puntuacion = rs.getDouble("puntuacion_media");
        } else {
            throw new SQLException("No existe el usuario indicado.");
        }
        closeDBConnection();

        return String.valueOf(String.format("%.1f", puntuacion));
    }
}
