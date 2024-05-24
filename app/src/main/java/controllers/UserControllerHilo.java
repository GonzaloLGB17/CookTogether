package controllers;

import java.sql.SQLException;
import models.UserModel;
import DAO.UserDAO;

public class UserControllerHilo implements Runnable {
    private UserModel userModel;
    private String username;
    private int puntos;
    private UserDAO userDAO = new UserDAO();
    private String action;

    public UserControllerHilo(UserModel userModel, String action) {
        this.userModel = userModel;
        this.action = action;
    }

    public UserControllerHilo(String username, String action) {
        this.username = username;
        this.action = action;
    }

    public UserControllerHilo(String username, int puntos, String action) {
        this.username = username;
        this.puntos = puntos;
        this.action = action;
    }

    @Override
    public void run() {
        try {
            switch (action) {
                case "insertar":
                    userDAO.insertarUsuario(userModel);
                    break;
                case "buscar":
                    userDAO.buscarUsuario(username);
                    break;
                case "actualizarPuntuacion":
                    userDAO.actualizarPuntuacion(username, puntos);
                    break;
                // Otros casos según las operaciones que necesites
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error de alguna manera apropiada
        }
    }
}
