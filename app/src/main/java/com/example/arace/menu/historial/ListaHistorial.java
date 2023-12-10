package com.example.arace.menu.historial;

import android.widget.Button;

public class ListaHistorial {
    String Zona;
    String Fecha_Inicio;
    String Fecha_Fin;
    String Dia;
    String nombre;
    Button cancelar;
    public ListaHistorial() {

    }

    public String getZona() {
        return Zona;
    }

    public void setZona(String zona) {
        Zona = zona;
    }

    public String getFecha_Inicio() {
        return Fecha_Inicio;
    }

    public void setFecha_Inicio(String fecha_Inicio) {
        Fecha_Inicio = fecha_Inicio;
    }

    public String getFecha_Fin() {
        return Fecha_Fin;
    }

    public void setFecha_Fin(String fecha_Fin) {
        Fecha_Fin = fecha_Fin;
    }

    public String getDia() {
        return Dia;
    }

    public void setDia(String dia) {
        Dia = dia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ListaHistorial(String zona, String fecha_inicio,String fecha_fin,String dia,Button cancelar) {
        Zona = zona;
        Fecha_Inicio = fecha_inicio;
        Fecha_Fin=fecha_fin;
        Dia=dia;
        this.cancelar=cancelar;
    }



}
