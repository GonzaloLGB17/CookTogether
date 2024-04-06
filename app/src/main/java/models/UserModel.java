package models;

public class UserModel {
    private String nombre;
    private String apellidos;
    private String username;
    private String password;
    private int id;
    private int puntos;

    private int nivelesDesbloqueados;
    public UserModel(int id,String nombre, String apellidos, String username, String password, int puntos, int nivelesDesbloqueados) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.username = username;
        this.password = password;
        this.id = id;
        this.puntos = puntos;
        this.nivelesDesbloqueados = nivelesDesbloqueados;
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

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    public int getNivelesDesbloqueados() {
        return nivelesDesbloqueados;
    }

    public void setNivelesDesbloqueados(int nivelesDesbloqueados) {
        this.nivelesDesbloqueados = nivelesDesbloqueados;
    }
}
