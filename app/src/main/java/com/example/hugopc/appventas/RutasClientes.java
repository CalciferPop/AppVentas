package com.example.hugopc.appventas;

public class RutasClientes {

    private int id_ruta;
    private String descripcion;
    private int id_cliente;
    private String nombre_cliente;
    private String latitud;
    private String longitud;

    public RutasClientes() {

    }

    public RutasClientes(int id_ruta, String descripcion, int id_cliente, String nombre_cliente, String latitud, String longitud) {
        this.id_ruta = id_ruta;
        this.descripcion = descripcion;
        this.id_cliente = id_cliente;
        this.nombre_cliente = nombre_cliente;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId_ruta() {
        return id_ruta;
    }

    public void setId_ruta(int id_ruta) {
        this.id_ruta = id_ruta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
