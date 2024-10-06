package com.jrl.juego;

import com.badlogic.gdx.Screen;


/**
 *
 * @author jhamildrl
 */

public abstract class BaseScreen implements Screen{
    protected Principal principal;
    public BaseScreen(Principal principal){
        this.principal=principal;
    }

    @Override
    public void show(){}

    @Override
    public void render(float alpha){}
    @Override
    public void resize(int i, int i1){}
    @Override
    public  void pause() {}

    @Override
    public void resume(){}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
    }

}
