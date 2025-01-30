package com.jrl.juego.minijuegos.rompecabezas.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jrl.juego.BaseScreen;
import com.jrl.juego.Pantallas;
import com.jrl.juego.minijuegos.rompecabezas.Settings;

public class PantallaNivelAArmarScreen extends BaseScreen {

    private Stage stage;
    private Table mainTable;
    private Skin skin;
    private Texture imageTexture;
    Image topImage;

    @Override
    public void show() {
        super.show();

        // Configurar el escenario y la vista
        stage = new Stage(new StretchViewport(800, 600));
        Gdx.input.setInputProcessor(stage);

        // Cargar la skin para UI
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Crear la tabla principal
        mainTable = new Table();
        mainTable.setFillParent(true);

        // Crear una imagen para la parte superior
        if(imageTexture==null) {
            imageTexture = new Texture(Gdx.files.internal("default.jpeg"));
        }
        Settings.setImage(imageTexture);
        topImage = new Image(new TextureRegionDrawable(imageTexture));
        mainTable.add(topImage).center().expand();
        mainTable.row();

        // Crear el botón "Armar Rompecabezas"
        TextButton puzzleButton = new TextButton("Armar Rompecabezas", skin);
        puzzleButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Pantallas.NIVELROMPECABEZAS.setPantalla(new PlayingScreen());
                Pantallas.NIVELROMPECABEZAS.setPrincipal(principal);
                principal.setScreen(Pantallas.NIVELROMPECABEZAS.getPantalla());
            }
        });

        // Añadir el botón a la tabla principal
        mainTable.add(puzzleButton).size(300,100);

        // Añadir la tabla principal al escenario
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public Texture getImageTexture() {
        return imageTexture;
    }

    public void setImageTexture(Texture imageTexture) {
        this.imageTexture = imageTexture;
    }

    public Image getTopImage() {
        return topImage;
    }

    public void setTopImage(Image topImage) {
        this.topImage = topImage;
    }
}

