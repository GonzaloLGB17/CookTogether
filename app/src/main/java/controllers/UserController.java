package controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import DAO.UserDAO;
import models.UserModel;

public class UserController {
    public boolean insertarUsuario(UserModel userModel) throws SQLException {
        return new UserDAO().insertarUsuario(userModel);
    }

    public UserModel buscarUsuario(String username) throws SQLException{
        return new UserDAO().buscarUsuario(username);
    }


    public boolean comprobarUsuario(String username) throws SQLException {
        return new UserDAO().comprobarUsuario(username);
    }

    public UserModel iniciarSesion(String usernameMail, String passwd) throws SQLException {
        return new UserDAO().iniciarSesion(usernameMail,passwd);
    }

    public String obtenerPuntuacionUsuario(String usuario) throws SQLException{
        return new UserDAO().obtenerPuntuacionUsuario(usuario);
    }

    public String obtenerRecetasUsuario(String usuario) throws SQLException{
        return new UserDAO().obtenerRecetasUsuario(usuario);
    }
}
