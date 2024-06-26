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
        String query = "SELECT id, nombre, apellidos, correo, username, password, foto_usuario FROM usuarios WHERE username = ?";
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
                    rs.getBytes("foto_usuario")
            );
        } else {
            throw new SQLException("No existe el usuario indicado.");
        }
        closeDBConnection();
        return usuario;
    }

    public boolean comprobarUsuario(String username) throws SQLException {
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        boolean usuario = false;
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        String query = "SELECT id, nombre, apellidos, correo, username, password, foto_usuario FROM usuarios WHERE username = ?";
        sentencia = connection.prepareStatement(query);
        sentencia.setString(1, username);
        rs = sentencia.executeQuery();
        if (rs.next()) {
            usuario = true;
        }
        closeDBConnection();
        return usuario;
    }

    public ArrayList<UserModel> obtenerUsuarios() throws SQLException{
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        UserModel usuario = null;
        ArrayList<UserModel> usuarios = new ArrayList<>();
        String query = "SELECT * FROM usuarios;";
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
                    rs.getBytes("foto_usuario")
            );

            if(usuario!=null){
                usuarios.add(usuario);
            }
        }
        closeDBConnection();
        return usuarios;
    }

    public ArrayList<UserModel> obtenerUsuariosBuscados(String username) throws SQLException{
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        UserModel usuario = null;
        ArrayList<UserModel> usuarios = new ArrayList<>();
        String query = "SELECT * FROM usuarios WHERE username LIKE ?";
        PreparedStatement sentencia = connection.prepareStatement(query);
        sentencia.setString(1, "%" + username + "%");
        ResultSet rs = sentencia.executeQuery();
        while (rs.next()){
            usuario = new UserModel(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getString("correo"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getBytes("foto_usuario")
            );

            if(usuario!=null){
                usuarios.add(usuario);
            }
        }
        closeDBConnection();
        return usuarios;
    }


    public boolean insertarUsuario(UserModel user) throws SQLException {
        boolean insertarUsuario = false;
        int result;
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }

        try {
            String query = "{? = call insertar_usuario(?,?,?,?,?,?)}";
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, user.getNombre());
            callableStatement.setString(3, user.getApellidos());
            callableStatement.setString(4, user.getCorreo());
            callableStatement.setString(5, user.getUsername());
            callableStatement.setString(6, user.getPassword());
            callableStatement.setBytes(7, user.getFotoUsuario());
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
                String getUserQuery = "SELECT nombre, apellidos, correo, username, foto_usuario FROM usuarios WHERE username = ? OR correo = ?";
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

    public String obtenerPuntuacionUsuario(String usuario) throws SQLException{
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        double puntuacion = 0;
        // Inicializar la conexión a la base de datos
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        // Preparar y ejecutar la consulta SQL
        String query = "SELECT AVG(puntuacion_media) AS puntuacion FROM recetas WHERE usuario = ?";
        sentencia = connection.prepareStatement(query);
        sentencia.setString(1, usuario);
        rs = sentencia.executeQuery();
        // Verificar si se encontró el usuario y crear el objeto UserModel
        if (rs.next()) {
            puntuacion = rs.getDouble("puntuacion");
        } else {
            throw new SQLException("No existe el usuario indicado.");
        }
        closeDBConnection();

        return String.valueOf(String.format("%.1f", puntuacion));
    }

    public String obtenerRecetasUsuario(String usuario) throws SQLException{
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        int recetas = 0;
        // Inicializar la conexión a la base de datos
        if(!initDBConnection()){
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        // Preparar y ejecutar la consulta SQL
        String query = "SELECT COUNT(usuario) AS recetas FROM recetas WHERE usuario = ? ";
        sentencia = connection.prepareStatement(query);
        sentencia.setString(1, usuario);
        rs = sentencia.executeQuery();
        // Verificar si se encontró el usuario y crear el objeto UserModel
        if (rs.next()) {
            recetas = rs.getInt("recetas");
        } else {
            throw new SQLException("No existe el usuario indicado.");
        }
        closeDBConnection();
        return String.valueOf(recetas);
    }

    public void actualizarUsuario(UserModel user, boolean actualizarPass, String nuevoUsername, byte[] nuevaFotoUsuario, String nuevaPass, String oldPass) throws SQLException {
        PreparedStatement sentencia = null;
        if (!initDBConnection()) {
            throw new SQLException("No se pudo conectar a la base de datos.");
        }
        try {
            String query = "UPDATE usuarios SET username = ?, foto_usuario = ? WHERE id = ?";
            sentencia = connection.prepareStatement(query);

            sentencia.setString(1, nuevoUsername);
            sentencia.setBytes(2, nuevaFotoUsuario);
            sentencia.setInt(3, user.getId());

            int filasActualizadas = sentencia.executeUpdate();
            if (filasActualizadas == 0) {
                throw new SQLException("No existe el usuario indicado o no se pudo actualizar.");
            }
        } finally {
            closeDBConnection();
        }
    }

    public void cambiarPass(String newPass, String oldPass,int id) throws SQLException {
        if(!initDBConnection()){
            throw new SQLException("Error al conectar con la BBDD");
        }
        boolean passCorrecta = comprobarPassword(oldPass,id);
        if(!passCorrecta){
            throw new SQLException("La contraseña actual es incorrecta");
        }else {
            PreparedStatement sentencia = null;
            try {
                String query = "UPDATE usuarios SET password = encode(digest(?, 'sha512'), 'hex') WHERE id = ?";
                sentencia = connection.prepareStatement(query);

                sentencia.setString(1, newPass);
                sentencia.setInt(2, id);

                sentencia.executeUpdate();
            } catch (SQLException e){
                throw new SQLException("Error al modificar la contraseña del usuario");
            }
        }
    }

    public boolean comprobarPassword(String password, int id) throws SQLException{
        PreparedStatement sentencia = null;
        ResultSet rs = null;
        boolean passwordCorrecta = false;
        if (!initDBConnection()) {
            throw new SQLException("Error al conectar con la BBDD");
        }

        try {
            String query = "SELECT * FROM usuarios WHERE id = ? AND password = encode(digest(?, 'sha512'), 'hex')";
            sentencia = connection.prepareStatement(query);
            sentencia.setInt(1, id);
            sentencia.setString(2,password);
            rs = sentencia.executeQuery();
            if(rs.next()){
                passwordCorrecta = true;
            }else {
                throw new SQLException("La contraseña actual introducida es incorrecta.");
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
        return passwordCorrecta;
    }
}



