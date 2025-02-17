package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.jrl.juego.entidades.Jugador;
import com.jrl.juego.guardado.GuardadoObjeto;

import java.io.IOException;


/**
 *
 * @author jhamildrl
 */

public abstract class BaseScreen implements Screen{
    protected Principal principal;

    @Override
    public void show(){   }

    @Override
    public void render(float alpha){   }
    @Override
    public void resize(int i, int i1){}
    @Override
    public  void pause() {try {
        GuardadoObjeto<Jugador> guardado;
        guardado=new GuardadoObjeto<>(Jugador.class);
        guardado.guardar(principal.getJugador());
    } catch (IOException e) {
        throw new RuntimeException(e);
    }}

    @Override
    public void resume(){}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        try {
            GuardadoObjeto<Jugador> guardado;
            guardado=new GuardadoObjeto<>(Jugador.class);
            guardado.guardar(principal.getJugador());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
}
