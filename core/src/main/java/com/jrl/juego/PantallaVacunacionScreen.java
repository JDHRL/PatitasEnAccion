package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jrl.juego.minijuegos.PantallaGanasteMonedaScreen;

public class PantallaVacunacionScreen extends BaseScreen {

    private  Stage stage;
    private  Skin skin;
    private Table table;
    private BitmapFont customFont;
    private String tipo;

    @Override
    public void show() {
        super.show();
        principal.reproducirMusica(1);
        tipo = principal.getJugador().getMascota().getTipo();
        stage = new Stage(new StretchViewport(800, 900));
        Gdx.input.setInputProcessor(stage);

        // Cargar el Skin
        skin = new Skin(Gdx.files.internal("skin/uskin.json"));

        // Cargar la fuente desde el archivo .fnt y la textura correspondiente
        customFont = new BitmapFont(Gdx.files.internal("fuente_arial/font-export.fnt"), Gdx.files.internal("fuente_arial/font-export.png"), false);

        // Crear estilo de etiqueta con la fuente personalizada
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = customFont;
        labelStyle.fontColor = Color.WHITE;

        // Crear la tabla principal
        table = new Table();
        table.setFillParent(true);

        Texture backgroundTexture;
        if (tipo.equals("gato")) {
            backgroundTexture = new Texture(Gdx.files.internal("vacunacion_gato.png"));
        }else{
            backgroundTexture = new Texture(Gdx.files.internal("vacunacion_perro.png"));
        }
        Image background = new Image(backgroundTexture);
        table.add(background).expand().fill().row();

        // Texto y fondo
        Stack textStack = new Stack();
        Texture textBgTexture = new Texture(Gdx.files.internal("fondo_texto.jpeg"));
        Image textBackground = new Image(textBgTexture);
        textBackground.setFillParent(true);
        Label textLabel = new Label("", labelStyle);
        textLabel.setAlignment(Align.center);
        textStack.add(textBackground);
        textStack.add(textLabel);
        table.add(textStack).height(300).fillX().padTop(10).row();

        // Botón de volver al menú principal
        TextButton volverButton = new TextButton("Volver al Menú", skin);
        volverButton.getLabel().setStyle(labelStyle);
        volverButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                principal.setScreen(Pantallas.MENUINTERACCION.getPantalla()); // Cambia por tu pantalla principal
            }
        });
        table.add(volverButton).padTop(20).center().row();

        // Añadir la tabla al escenario
        stage.addActor(table);

        if (tipo.equals("gato")) {
            textLabel.setText("¡Felicidades se vacuno al felino!");
        } else {
            textLabel.setText("¡Felicidades se vacuno al canino!");
        }


    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        customFont.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}

