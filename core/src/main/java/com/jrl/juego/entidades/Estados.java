package com.jrl.juego.entidades;

import com.badlogic.gdx.graphics.Color;

public enum Estados {
    SALUDABLE("Saludable",1),
    ENFERMO_PARASITOS("Enfermo de parasitos",2),
    ENFERMO_DE_CLAMIDIA("Enfermo de clamidia",3),
    ENFERMO_DE_PARMOVIRUS("Enfermo de parvovirus",4),
    ENFERMO_DE_RABIA("Enfermo de rabia",5),
    MUERTE("Muerte por no cuidado",6);
    

    String estado;
    int indice;
    Estados(String estado,int indice){
        this.estado=estado;
        this.indice=indice;
    }

    @Override
    public String toString() {
        return estado;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }
}
