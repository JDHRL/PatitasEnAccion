package com.jrl.juego.entidades;

public class Medicina {
    private String nombre;
    private int salud;
    private int energia;
    private String imagen;
    private int precio;
    private String descripcion;
    public Medicina(String nombre,String descripcion, int salud, int energia, String imagen, int precio) {
        this.nombre = nombre;
        this.salud = salud;
        this.energia = energia;
        this.imagen = imagen;
        this.precio = precio;
        this.descripcion=descripcion;
    }

    public Medicina() {
    }

    public int getSalud() {
        return salud;
    }

    public void setSalud(int salud) {
        this.salud = salud;
    }

    public int getEnergia() {
        return energia;
    }

    public void setEnergia(int energia) {
        this.energia = energia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
