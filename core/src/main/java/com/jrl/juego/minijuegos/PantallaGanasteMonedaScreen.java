package com.jrl.juego.minijuegos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jrl.juego.BaseScreen;
import com.jrl.juego.MenuJuegosScreen;
import com.jrl.juego.Pantallas;
import com.jrl.juego.Principal;

public class PantallaGanasteMonedaScreen extends BaseScreen {

    private final Stage stage;
    private final Skin skin;
    private final BitmapFont customFont;
    private Principal principal;

    public PantallaGanasteMonedaScreen(Principal principal, long monedas) {
        this.principal = principal;
        principal.reproducirMusica(1);
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
        Table table = new Table();
        table.setFillParent(true);

        // === Imagen de la moneda ===
        Texture monedaTexture = new Texture(Gdx.files.internal("moneda.png"));
        Image monedaImage = new Image(monedaTexture);

        // === Etiqueta Felicidades ===
        Label felicidadesLabel = new Label("¡Felicidades! Ganaste "+monedas+" monedas", labelStyle);
        felicidadesLabel.setAlignment(Align.center);

        // Añadir elementos a la tabla
        table.add(monedaImage).size(100, 100).padBottom(20).row();
        table.add(felicidadesLabel).padTop(10).row();

        // Crear el botón de "Continuar"
        TextButton continuarButton = new TextButton("Continuar", skin);
        continuarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Acción al presionar el botón Continuar
                principal.getJugador().setMonedas(principal.getJugador().getMonedas()+monedas);
                principal.setScreen(Pantallas.MENUJUEGOS.getPantalla()); // Puedes reemplazar con la pantalla que quieras
            }
        });

        table.add(continuarButton).height(60).width(200).padTop(20);

        // Añadir la tabla al escenario
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 0, 1);
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
