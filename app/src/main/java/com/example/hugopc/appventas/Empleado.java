package com.example.hugopc.appventas;

import java.util.Date;

public class Empleado {

    private int id_empleado;
    private String nombre_empleado;
    private String apellido_paterno;
    private String apellido_materno;
    private String fecha_nacimiento;
    private String email_empleado;
    private String telefono;
    private String RFC;
    private int id_genero;


    public Empleado() {

    }

    public Empleado(int id_empleado, String nombre_empleado, String apellido_paterno, String apellido_materno, String fecha_nacimiento, String email_empleado, String telefono, String RFC) {
        this.id_empleado = id_empleado;
        this.nombre_empleado = nombre_empleado;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.fecha_nacimiento = fecha_nacimiento;
        this.email_empleado = email_empleado;
        this.telefono = telefono;
        this.RFC = RFC;

    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public void setNombre_empleado(String nombre_empleado) {
        this.nombre_empleado = nombre_empleado;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getEmail_empleado() {
        return email_empleado;
    }

    public void setEmail_empleado(String email_empleado) {
        this.email_empleado = email_empleado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public int getId_genero() {
        return id_genero;
    }

    public void setId_genero(int id_genero) {
        this.id_genero = id_genero;
    }
}
