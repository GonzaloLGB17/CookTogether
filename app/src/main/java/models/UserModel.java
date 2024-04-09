package models;

public class UserModel {
    private int id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String username;
    private String password;
    private double puntuacion;
    public UserModel(int id, String nombre, String apellidos, String correo, String username, String password, double puntuacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.username = username;
        this.password = password;
        this.puntuacion = puntuacion;
    }

    public UserModel(String nombre, String apellidos, String correo, String username, String password, double puntuacion) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.username = username;
        this.password = password;
        this.puntuacion = puntuacion;
    }

    public UserModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }
}
