package com.jrl.juego.entidades;

public class Alimento {
    private String nombre;
    private int salud;
    private int energia;
    private String imagen;
    private int precio;
    private int cantidad;
    private String descripcion;
    public Alimento(String nombre,String descripcion ,int salud, int energia, String imagen, int precio, int cantidad) {
        this.nombre = nombre;
        this.salud = salud;
        this.energia = energia;
        this.imagen = imagen;
        this.precio = precio;
        this.cantidad=cantidad;
        this.descripcion=descripcion;
    }

    public Alimento() {
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Alimento{" +
            "nombre='" + nombre + '\'' +
            ", salud=" + salud +
            ", energia=" + energia +
            ", imagen='" + imagen + '\'' +
            ", precio=" + precio +
            ", cantidad=" + cantidad +
            ", descripcion='" + descripcion + '\'' +
            '}';
    }
}
