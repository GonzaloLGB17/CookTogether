package controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import DAO.UserDAO;
import models.UserModel;

public class UserController {
    public boolean insertarUsuario(String nombre, String apellidos, String username, String password) throws SQLException {
        return new UserDAO().insertarUsuario(nombre,apellidos,username,password);
    }

    public UserModel buscarUsuario(String username) throws SQLException{
        return new UserDAO().buscarUsuario(username);
    }


    /*public boolean comprobarUsuario(String username) throws SQLException {
        return new UserDAO().comprobarUsuario(username);
    }*/

    /*public boolean actualizarPuntos(String username, int puntos) throws SQLException{
        return new UserDAO().actualizarPuntos(username, puntos);
    }*/




}
