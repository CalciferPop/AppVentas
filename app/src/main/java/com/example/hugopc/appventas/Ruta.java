package com.example.hugopc.appventas;

public class Ruta {


    private int id_ruta;
    private String desc_ruta;


    public Ruta() {

    }

    public Ruta(int id_ruta, String desc_ruta) {
        this.id_ruta = id_ruta;
        this.desc_ruta = desc_ruta;
    }

    public int getId_ruta() {
        return id_ruta;
    }

    public void setId_ruta(int id_ruta) {
        this.id_ruta = id_ruta;
    }

    public String getDesc_ruta() {
        return desc_ruta;
    }

    public void setDesc_ruta(String desc_ruta) {
        this.desc_ruta = desc_ruta;
    }
}
