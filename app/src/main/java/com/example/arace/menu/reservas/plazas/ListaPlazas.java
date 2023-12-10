package com.example.arace.menu.reservas.plazas;

public class ListaPlazas {
    String lugar;
    String disponibilidad;
    int plaza;
    byte[] imagen; // Nueva línea para almacenar la imagen en formato byte array

    public ListaPlazas(String lugar, String disponibilidad) {

        this.lugar = lugar;
        this.disponibilidad = disponibilidad;
        this.imagen = imagen;
    }
    public ListaPlazas(){

    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getLugar() {
        return lugar;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;

    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public int getPlaza() {

        return plaza;
    }

    public void setPlaza(int plaza) {

        this.plaza = plaza;
    }


}
