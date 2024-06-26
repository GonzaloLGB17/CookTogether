package models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class RecetaModel implements Serializable {
    private String titulo;
    private String descripcion;
    private String ingredientes;
    private String instrucciones;
    private String usuario;
    private double puntuacionMedia;
    private byte[] fotoReceta;
    private int idReceta;
    private String categoria;
    private Timestamp fechaHora;

    public RecetaModel(String titulo, String descripcion, String ingredientes, String instrucciones, String usuario, double puntuacionMedia, byte[] fotoReceta, int idReceta, String categoria, Timestamp fechaHora) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.usuario = usuario;
        this.puntuacionMedia = puntuacionMedia;
        this.fotoReceta = fotoReceta;
        this.idReceta = idReceta;
        this.categoria = categoria;
        this.fechaHora = fechaHora;
    }

    public RecetaModel(String titulo, String descripcion, String ingredientes, String instrucciones, String usuario, double puntuacionMedia, byte[] fotoReceta, String categoria, Timestamp fechaHora) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.usuario = usuario;
        this.puntuacionMedia = puntuacionMedia;
        this.fotoReceta = fotoReceta;
        this.categoria = categoria;
        this.fechaHora = fechaHora;
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
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }
    public String getTiempoTranscurrido() {
        LocalDateTime ahora = LocalDateTime.now();
        //Convierte timestamp a LocalDateTime
        LocalDateTime publicacion = getFechaHora().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        Duration duracion = Duration.between(publicacion, ahora);

        long segundos = duracion.getSeconds();

        if (segundos < 60) {
            return "Hace " + segundos + " segundos.";
        } else if (segundos < 3600) {
            long minutos = segundos / 60;
            return "Hace " + minutos + (minutos == 1 ? " minuto." : " minutos.");
        } else if (segundos < 86400) {
            long horas = segundos / 3600;
            return "Hace " + horas + (horas == 1 ? " hora." : " horas.");
        } else {
            long dias = segundos / 86400;
            return "Hace " + dias + (dias == 1 ? " día." : " días.");
        }
    }
}