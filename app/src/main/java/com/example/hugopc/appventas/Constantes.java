package com.example.hugopc.appventas;

public class Constantes {

    //CEL
    String IP_ADDRESS ="http://192.168.43.201:8080/Ventas/api/";
    //CASA
    //String IP_ADDRESS ="http://192.168.1.67:8080/Ventas/api/";


    public Constantes() {
       setIP_ADDRESS(IP_ADDRESS);
    }

    public String getIP_ADDRESS() {
        return IP_ADDRESS;
    }

    public void setIP_ADDRESS(String IP_ADDRES) {
        this.IP_ADDRESS = IP_ADDRES;
    }

}
