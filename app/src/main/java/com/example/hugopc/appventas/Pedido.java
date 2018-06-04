package com.example.hugopc.appventas;

import java.util.Date;

public class Pedido {

    private int id_cliente;
    private String nombre_cliente;
    private String domicilio;
    private int id_pedido;
    private int cantidad;
    private String fecha_compra;
    private int id_producto;
    private String nombre_producto;
    private double precio;
    private double importe;

    public Pedido() {

    }

    public Pedido(int id_cliente, String nombre_cliente, String domicilio, int id_pedido, int cantidad, String fecha_compra, int id_producto, String nombre_producto, double precio, double importe) {
        this.id_cliente = id_cliente;
        this.nombre_cliente = nombre_cliente;
        this.domicilio = domicilio;
        this.id_pedido = id_pedido;
        this.cantidad = cantidad;
        this.fecha_compra = fecha_compra;
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.precio = precio;
        this.importe = importe;
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

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(String fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public int getId_producto() {
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
}
