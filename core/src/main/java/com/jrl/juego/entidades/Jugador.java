package com.jrl.juego.entidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Jugador {
    String name;
    long monedas;
    Map<String,Alimento> alimentos;
    Map<String,Medicina> medicinas;


    Animal mascota;
    public Jugador(){
        alimentos=new HashMap<>();
        medicinas=new HashMap<>();
        mascota=new Animal();
        monedas=2000;
        mascota.setSalud(100);
        mascota.setEnergia(100);
        mascota.setEstado(Estados.SALUDABLE.toString());
    }
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

    public Map<String,Alimento> getAlimentos() {
        return alimentos;
    }

    public void setAlimentos(Map<String,Alimento> alimentos) {
        this.alimentos = alimentos;
    }

    public Animal getMascota() {
        return mascota;
    }

    public void setMascota(Animal mascota) {
        this.mascota = mascota;
    }

    public Map<String, Medicina> getMedicinas() {
        return medicinas;
    }

    public void setMedicinas(Map<String, Medicina> medicinas) {
        this.medicinas = medicinas;
    }

}
