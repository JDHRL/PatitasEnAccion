package com.jrl.juego.minijuegos.rompecabezas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jrl.juego.BaseScreen;
import com.jrl.juego.Pantallas;
import com.jrl.juego.Principal;
import com.jrl.juego.minijuegos.rompecabezas.Settings;

import java.util.ArrayList;

public class NivelesRompeCabezas extends BaseScreen {

    private Stage stage;
    private Table mainTable;
    private Table gridTable;
    private ScrollPane scrollPane;
    private Skin skin;
    private BitmapFont fuente;
    ArrayList<Texture> pantallas;
    @Override
    public void show() {
        super.show();
        pantallas=new ArrayList<>();
        pantallas.add(new Texture(Gdx.files.internal("rompecabezas/niño1.jpeg")));
        pantallas.add(new Texture(Gdx.files.internal("rompecabezas/niño2.jpeg")));
        pantallas.add(new Texture(Gdx.files.internal("rompecabezas/niño3.jpeg")));
        pantallas.add(new Texture(Gdx.files.internal("rompecabezas/niño4.jpeg")));
        pantallas.add(new Texture(Gdx.files.internal("rompecabezas/niño5.jpeg")));
        pantallas.add(new Texture(Gdx.files.internal("rompecabezas/niño6.jpeg")));
        pantallas.add(new Texture(Gdx.files.internal("rompecabezas/niño7.jpeg")));
        pantallas.add(new Texture(Gdx.files.internal("rompecabezas/niño8.jpeg")));
        pantallas.add(new Texture(Gdx.files.internal("rompecabezas/niño9.jpeg")));
        stage = new Stage(new StretchViewport(800, 600));
        Gdx.input.setInputProcessor(stage);

        // Cargar la skin para UI
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Crear la tabla principal
        mainTable = new Table();
        mainTable.setFillParent(true);

        // Cargar la fuente personalizada
        fuente = new BitmapFont(Gdx.files.internal("fuente_arial/fuente_con_borde.fnt"), Gdx.files.internal("fuente_arial/fuente_con_borde.png"), false);

        // Crear estilo de etiqueta con la fuente personalizada
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fuente;
        labelStyle.fontColor = Color.WHITE;

        // Crear el Label superior
        Label titleLabel = new Label("Niveles de Rompecabezas", labelStyle);
        titleLabel.setFontScale(2);
        mainTable.add(titleLabel).expandX().center().pad(20);
        mainTable.row();

        // Crear la grilla
        gridTable = new Table();
        gridTable.defaults().pad(10).size(150, 150); // Tamaño de cada celda

        // Llenar la grilla con 12 niveles numerados
        for (int i = 1; i <= pantallas.size(); i++) {
            final int nivel = i; // Necesario para usar en el listener
            Label levelLabel = new Label(String.valueOf(i), labelStyle);
            levelLabel.setFontScale(2);

            // Crear botón para cada nivel
            Button levelButton = new Button(skin);
            levelButton.add(levelLabel).center();
            levelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Settings.setPuzzleSize(nivel+1);
                    ((PantallaNivelAArmarScreen)Pantallas.ROMPECABEZAIMAGEN.getPantalla()).setImageTexture(pantallas.get(nivel-1));
                    principal.setScreen(Pantallas.ROMPECABEZAIMAGEN.getPantalla());
                    // principal.setScreen(new PantallaNivel(principal, nivel));
                }
            });

            gridTable.add(levelButton);

            // Nueva fila después de cada 3 niveles
            if (i % 3 == 0) {
                gridTable.row();
            }
        }

        // Crear el ScrollPane para la grilla
        scrollPane = new ScrollPane(gridTable, skin);
        scrollPane.setScrollingDisabled(true, false); // Solo desplazamiento vertical
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollBarPositions(false, true);

        mainTable.add(scrollPane).expand().fill();

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
}
