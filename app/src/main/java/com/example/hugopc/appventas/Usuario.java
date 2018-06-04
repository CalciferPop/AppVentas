package com.example.hugopc.appventas;

public class Usuario {


    private int id_Usuario;
    private String nickname;


    public Usuario() {

    }

    public Usuario(int id_Usuario, String nickname) {
        this.id_Usuario = id_Usuario;
        this.nickname = nickname;
    }

    public int getid_Usuario() {
        return id_Usuario;
    }

    public void setid_Usuario(int id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public String getnickname() {
        return nickname;
    }

    public void setnickname(String nickname) {
        this.nickname = nickname;
    }
}
