package com.example.hugopc.appventas;

class EmpleadosRoles {
    String nombre_empleado;
    String apellido_paterno;
    String apellido_materno;
    String nickname;
    String nombre_rol;
    int id_rol;
    int id_empleado;
    int id_usuario;

    public EmpleadosRoles(String nombre_empleado, String apellido_paterno, String apellido_materno, String nickname, String nombre_rol, int id_rol, int id_empleado, int id_usuario) {
        this.nombre_empleado = nombre_empleado;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.nickname = nickname;
        this.nombre_rol = nombre_rol;
        this.id_rol = id_rol;
        this.id_empleado = id_empleado;
        this.id_usuario = id_usuario;
    }

    public EmpleadosRoles() {

    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre_rol() {
        return nombre_rol;
    }

    public void setNombre_rol(String nombre_rol) {
        this.nombre_rol = nombre_rol;
    }
}
