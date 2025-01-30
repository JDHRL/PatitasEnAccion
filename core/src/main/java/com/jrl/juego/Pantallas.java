package com.jrl.juego;

import com.jrl.juego.minijuegos.BuscaRatonPelota.JuegoBuscaRatonPelotaScreen;
import com.jrl.juego.minijuegos.rompecabezas.screens.NivelesRompeCabezas;
import com.jrl.juego.minijuegos.rompecabezas.screens.PantallaNivelAArmarScreen;
import com.jrl.juego.minijuegos.rompecabezas.screens.PlayingScreen;

public enum Pantallas{
    MENUAUTORIZACION(new MenuAutorizacionScreen()),
    MENUJUEGO( new MenuJuegoScreen()),
    ENTRENAMIENTOCARAS(new EntrenadoScreen()),
    MENUINTERACCION(new MenuInteraccionScreen()),
    MENUJUEGOS(new MenuJuegosScreen()),
    JUEGOROMPECABEZAS(new NivelesRompeCabezas()),
    CAPTURAROSTROS(new CapturaScreen()),
    JUEGOBUSCARATONPELOTA(new JuegoBuscaRatonPelotaScreen()),
    NIVELROMPECABEZAS(new PlayingScreen()),
    ROMPECABEZAIMAGEN(new PantallaNivelAArmarScreen()),
    MENUADOPCION(new MenuAdopcion()),
    MENUALIMENTOS( new MenuAlimentosAlimentarScreen()),
    MENUCOMPRAALIMENTOS(new MenuAlimentosComprarScreen()),
    VACUNACIONMASCOTA(new PantallaVacunacionScreen()),
    MENUADMINISTRACIONMEDICINA(new MenuMedicinasAdministrarScreen()),
    CARACTERISTICAS_MASCOTA(new CaracteristicasScreen()),
    CARACTERISTICAS_OBJETO(new PantallaCaracteristicas()),
    MENUSALIDASCREEN(new MenuSalirScreen());


    private BaseScreen pantalla;
    Pantallas(BaseScreen screen){
        pantalla=screen;
    }

    public Principal getPrincipal() {
        return pantalla.getPrincipal();
    }

    public void setPrincipal(Principal principal) {
        this.pantalla.setPrincipal(principal);
    }

    public BaseScreen getPantalla() {
        return pantalla;
    }

    public void setPantalla(BaseScreen pantalla) {
        this.pantalla = pantalla;
    }

};
