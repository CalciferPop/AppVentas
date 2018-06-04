package com.example.hugopc.appventas;

public class Producto {
    private int id_producto;
    private String nombre_producto;
    private String descripcion;
    private Double precio;
    private int num_existencia;

    public Producto() {

    }

    public Producto(int id_producto, String nombre_producto, String descripcion, Double precio, int num_existencia) {
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.num_existencia = num_existencia;
    }

    public  int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getNum_existencia() {
        return num_existencia;
    }

    public void setNum_existencia(int num_existencia) {
        this.num_existencia = num_existencia;
    }
}
