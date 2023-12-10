package com.example.arace.menu.reservas.zonas;

public class ListaZonas {
    String Zona;
    int Plaza;
    int imagenView;

    public ListaZonas(){

    }
    public int getPlaza() {

        return Plaza;
    }

    public void setPlaza(int plaza) {

        Plaza = plaza;
    }


    public ListaZonas(String zona, int plaza, int imagenView) {

        Zona = zona;
        Plaza=plaza;
        this.imagenView = imagenView;
    }


    public String getZona() {
        return Zona;
    }

    public void setZona(String zona) {
        Zona = zona;
    }




}
