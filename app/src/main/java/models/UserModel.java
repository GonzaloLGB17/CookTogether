package models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private int id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String username;
    private String password;

    private byte[] fotoUsuario;
    public UserModel(int id, String nombre, String apellidos, String correo, String username, String password, byte[] fotoUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.username = username;
        this.password = password;
        this.fotoUsuario = fotoUsuario;
    }

    public UserModel(String nombre, String apellidos, String correo, String username, String password, byte[] fotoUsuario) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.username = username;
        this.password = password;
        this.fotoUsuario = fotoUsuario;
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

    public byte[] getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(byte[] fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }
}
