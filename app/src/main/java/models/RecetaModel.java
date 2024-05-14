package models;

public class RecetaModel {
    private String titulo;
    private String descripcion;
    private String ingredientes;
    private String instrucciones;
    private String usuario;
    private double puntuacionMedia;
    private byte[] fotoReceta;
    private int idReceta;
    private UserModel user;

    public RecetaModel(String titulo, String descripcion, String ingredientes, String instrucciones, String usuario, double puntuacionMedia, byte[] fotoReceta, int idReceta) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.usuario = usuario;
        this.puntuacionMedia = puntuacionMedia;
        this.fotoReceta = fotoReceta;
        this.idReceta = idReceta;
    }

    public RecetaModel(String titulo, String descripcion, String ingredientes, String instrucciones, String usuario, double puntuacionMedia, byte[] fotoReceta) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.usuario = usuario;
        this.puntuacionMedia = puntuacionMedia;
        this.fotoReceta = fotoReceta;
    }

    public RecetaModel() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public double getPuntuacionMedia() {
        return puntuacionMedia;
    }

    public void setPuntuacionMedia(double puntuacionMedia) {
        this.puntuacionMedia = puntuacionMedia;
    }

    public byte[] getFotoReceta() {
        return fotoReceta;
    }

    public void setFotoReceta(byte[] fotoReceta) {
        this.fotoReceta = fotoReceta;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }
}
