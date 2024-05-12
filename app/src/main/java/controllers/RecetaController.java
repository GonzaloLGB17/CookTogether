package controllers;

import java.sql.SQLException;

import DAO.RecetaDAO;
import models.RecetaModel;

public class RecetaController {
    public boolean insertarReceta(RecetaModel receta) throws SQLException {
        return new RecetaDAO().insertarReceta(receta);
    }

    public RecetaModel buscarReceta(String titulo, String usuario) throws SQLException {
        return new RecetaDAO().buscarReceta(titulo,usuario);
    }
}
