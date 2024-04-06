package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.ConexionBBDD;
import models.UserModel;

public class UserDAO {
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

    /*private void closeDBConnection(){
        try {
            conexionBBDD.cerrarConexion(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

    public boolean insertarUsuario(String nombre, String apellidos, String username, String password) throws SQLException {
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        boolean crearUsuario = false;
        try {
            String query = "INSERT INTO Usuario(nombre, apellidos, username, password, puntos, niveles_desbloqueados) VALUES (?,?,?,?,?,?);";
            PreparedStatement sentencia = connection.prepareStatement(query);
            sentencia.setString(1,nombre);
            sentencia.setString(2, apellidos);
            sentencia.setString(3, username);
            sentencia.setString(4, password);
            sentencia.setInt(5, 0);
            sentencia.setInt(6,1);
            sentencia.executeUpdate();
            crearUsuario = true;
        } catch (SQLException e) {
            throw new SQLException("Error al insertar un usuario.");
        }finally {
            //closeDBConnection();
        }
        return crearUsuario;
    }

    public UserModel buscarUsuario(String username) throws SQLException{
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        UserModel usuario = null;
        String query = "SELECT id,nombre,apellidos,username,password, puntos, niveles_desbloqueados FROM Usuario where username = ?;";
        PreparedStatement sentencia = connection.prepareStatement(query);
        sentencia.setString(1, username);
        ResultSet rs = sentencia.executeQuery();
        while (rs.next()){
            usuario = new UserModel(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4),
                        rs.getString(5), rs.getInt(6), rs.getInt(7));
        }
        if(usuario == null){
            throw new SQLException("No existe el usuario indicado.");
        }
        //closeDBConnection();
        return usuario;
    }
    /*public boolean actualizarPuntos(String username, int puntos) throws SQLException{
        boolean actualizarPuntos = false;
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        String query = "UPDATE Usuario SET puntos = ? WHERE username = ?;";
        PreparedStatement sentencia = connection.prepareStatement(query);
        sentencia.setInt(1, puntos);
        sentencia.setString(2, username);
        int result = sentencia.executeUpdate();
        if(result>0){
            actualizarPuntos = true;
        }
        return actualizarPuntos;
    }*/



    /*public ArrayList<UserModel> obtenerUsuarios() throws SQLException{
        initDBConnection();
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        UserModel usuario = null;
        ArrayList<UserModel> usuarios = new ArrayList<>();
        String query = "SELECT * FROM Usuario;";
        PreparedStatement sentencia = connection.prepareStatement(query);
        ResultSet rs = sentencia.executeQuery();
        while (rs.next()){
            usuario = new UserModel(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),
                    rs.getInt(6), rs.getInt(7));
            if(usuario!=null){
                usuarios.add(usuario);
            }
        }
        //closeDBConnection();
        return usuarios;
    }*/


    /*public boolean comprobarUsuario(String username) throws SQLException {
        boolean comprobar = false;
        ArrayList<UserModel> usuarios = obtenerUsuarios();
        for(int i = 0;i<usuarios.size();i++){
            if(usuarios.get(i).getUsername().equals(username)){
                throw new SQLException("El usuario ya existe.");
            }
        }
        comprobar = true;
        return comprobar;
    }*/

}
