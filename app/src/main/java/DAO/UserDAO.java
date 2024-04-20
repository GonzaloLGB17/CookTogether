package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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

    private void closeDBConnection(){
        try {
            conexionBBDD.cerrarConexion(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserModel buscarUsuario(String username) throws SQLException {
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        UserModel usuario = null;
        // Inicializar la conexión a la base de datos
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        // Preparar y ejecutar la consulta SQL
        String query = "SELECT id, nombre, apellidos, correo, username, password, puntuacion, foto_usuario FROM usuarios WHERE username = ?";
        sentencia = connection.prepareStatement(query);
        sentencia.setString(1, username);
        rs = sentencia.executeQuery();
        // Verificar si se encontró el usuario y crear el objeto UserModel
        if (rs.next()) {
            usuario = new UserModel(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getString("correo"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getDouble("puntuacion"),
                    rs.getBytes("foto_usuario")
            );
        } else {
            throw new SQLException("No existe el usuario indicado.");
        }
        closeDBConnection();
        return usuario;
    }

    public boolean comprobarUsuario(String username) throws SQLException {
        boolean comprobar = false;
        ArrayList<UserModel> usuarios = obtenerUsuarios();
        for(int i = 0;i<usuarios.size();i++){
            if(usuarios.get(i).getUsername().equals(username)){
                throw new SQLException("El usuario ya existe.");
            }
        }
        comprobar = true;
        return comprobar;
    }

    public ArrayList<UserModel> obtenerUsuarios() throws SQLException{
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        UserModel usuario = null;
        ArrayList<UserModel> usuarios = new ArrayList<>();
        String query = "SELECT * FROM Usuario;";
        PreparedStatement sentencia = connection.prepareStatement(query);
        ResultSet rs = sentencia.executeQuery();
        while (rs.next()){
            usuario = new UserModel(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getString("correo"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getDouble("puntuacion"),
                    rs.getBytes("foto_usuario")
            );

            if(usuario!=null){
                usuarios.add(usuario);
            }
        }
        closeDBConnection();
        return usuarios;
    }

    public boolean actualizarPuntuacion(String username, double puntuacion) throws SQLException {
        Connection connection = null;
        PreparedStatement sentencia = null;
        boolean actualizarPuntos = false;
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        // Preparar y ejecutar la consulta SQL
        String query = "UPDATE usuarios SET puntuacion = ? WHERE username = ?";
        sentencia = connection.prepareStatement(query);
        sentencia.setDouble(1, puntuacion);
        sentencia.setString(2, username);
        int result = sentencia.executeUpdate();
        if(result>0){
            actualizarPuntos = true;
        }
        return actualizarPuntos;
    }

    public boolean insertarUsuario(UserModel user) throws SQLException {
        boolean insertarUsuario = false;
        int result;
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }

        try {
            String query = "{? = call agregar_usuario(?,?,?,?,?,?,?)}";
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, user.getNombre());
            callableStatement.setString(3, user.getApellidos());
            callableStatement.setString(4, user.getCorreo());
            callableStatement.setString(5, user.getUsername());
            callableStatement.setString(6, user.getPassword());
            callableStatement.setDouble(7, user.getPuntuacion());
            callableStatement.setBytes(8, user.getFotoUsuario());
            callableStatement.execute();
            result = callableStatement.getInt(1);
        } catch (SQLException e) {
            throw new SQLException("Error al registrar el usuario");
        } finally {
            closeDBConnection();
        }

        if (result == 1) {
            throw new SQLException("El nombre de usuario ya existe");
        } else if (result == 2) {
            throw new SQLException("El correo ya existe");
        }else{
            insertarUsuario = true;
        }

        return insertarUsuario;
    }


    public UserModel iniciarSesion(String usernameMail, String passwd) throws SQLException {
        UserModel user = null;
        int result = 0;

        if (!initDBConnection()) throw new SQLException("Error al conectar con la base de datos");

        try {
            String query = "{? = call iniciar_sesion(?,?)}";

            CallableStatement callableStatement = connection.prepareCall(query);

            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, usernameMail);
            callableStatement.setString(3, passwd);

            callableStatement.execute();

            result = callableStatement.getInt(1);

            if (result == 0) {
                // Inicio de sesión exitoso, obtener detalles del usuario
                String getUserQuery = "SELECT nombre, apellidos, correo, username, puntuacion, foto_usuario FROM usuarios WHERE username = ? OR correo = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(getUserQuery);
                preparedStatement.setString(1, usernameMail);
                preparedStatement.setString(2, usernameMail);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    user = new UserModel();
                    user.setNombre(resultSet.getString("nombre"));
                    user.setApellidos(resultSet.getString("apellidos"));
                    user.setCorreo(resultSet.getString("correo"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPuntuacion(resultSet.getDouble("puntuacion"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al iniciar sesión");
        } finally {
            closeDBConnection();
        }

        if (result != 0) {
            throw new SQLException("Credenciales incorrectas");
        }

        return user;
    }


}
