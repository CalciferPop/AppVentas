package com.example.hugopc.appventas;

public class Genero {


    private int id_genero;
    private String desc_genero;


    public Genero() {

    }

    public Genero(int id_genero, String desc_genero) {
        this.id_genero = id_genero;
        this.desc_genero = desc_genero;
    }

    public int getid_genero() {
        return id_genero;
    }

    public void setid_genero(int id_genero) {
        this.id_genero = id_genero;
    }

    public String getdesc_genero() {
        return desc_genero;
    }

    public void setdesc_genero(String desc_genero) {
        this.desc_genero = desc_genero;
    }
}
