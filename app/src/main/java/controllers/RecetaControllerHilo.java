package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import models.RecetaModel;
import DAO.RecetaDAO;

public class RecetaControllerHilo implements Runnable {
    public RecetaModel recetaModel;
    private String titulo;
    private String usuario;
    private String filtro;
    private RecetaDAO recetaDAO = new RecetaDAO();
    private String action;
    public ArrayList<RecetaModel> recetas = new ArrayList<>();

    public RecetaControllerHilo(){

    }

    public RecetaControllerHilo(RecetaModel recetaModel, String action) {
        this.recetaModel = recetaModel;
        this.action = action;
    }

    public RecetaControllerHilo(String titulo, String usuario, String action) {
        this.titulo = titulo;
        this.usuario = usuario;
        this.action = action;
    }

    public RecetaControllerHilo(String filtro, String action) {
        this.filtro = filtro;
        this.action = action;
    }

    public RecetaControllerHilo(String action) {
        this.action = action;
    }

    @Override
    public void run() {
        try {
            switch (action) {
                case "insertar":
                    recetaDAO.insertarReceta(recetaModel);
                    break;
                case "buscar":
                    recetaModel = recetaDAO.buscarReceta(titulo, usuario);
                    break;
                case "obtenerRecetasFiltros":
                    recetas = recetaDAO.obtenerRecetasFiltros(filtro);
                case "obtenerRecetas":
                    recetas = recetaDAO.obtenerRecetas();
                    break;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
