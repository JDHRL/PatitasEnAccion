package com.jrl.juego.entidades;

public class Jugador {
    String name;
    long monedas;
    int nivel;
    long puntos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMonedas() {
        return monedas;
    }

    public void setMonedas(long monedas) {
        this.monedas = monedas;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public long getPuntos() {
        return puntos;
    }

    public void setPuntos(long puntos) {
        this.puntos = puntos;
    }
}
