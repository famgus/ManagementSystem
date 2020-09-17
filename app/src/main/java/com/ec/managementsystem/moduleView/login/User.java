package com.ec.managementsystem.moduleView.login;


public class User {

    private String username;
    private String password;
    private Integer rol;

    public User(String user, String pass, Integer rol) {

        this.username = user;
        this.password = pass;
        this.rol = rol;
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

    public Integer getRol() {
        return rol;
    }

    public void setRol(Integer rol) {
        this.rol = rol;
    }


}
