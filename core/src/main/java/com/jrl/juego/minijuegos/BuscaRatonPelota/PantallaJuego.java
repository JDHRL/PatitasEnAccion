package com.jrl.juego.minijuegos.BuscaRatonPelota;

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
import com.jrl.juego.Principal;

public class PantallaJuego extends BaseScreen {
    private Stage stage;
    private final Skin skin;

    private final Table table;
    private final BitmapFont customFont;

    public PantallaJuego(Principal principal) {
        super(principal);
        stage = new Stage(new StretchViewport(800, 900));
        skin = new Skin(Gdx.files.internal("skin/uskin.json"));

        // Cargar la fuente desde el archivo .fnt y la textura correspondiente
        customFont = new BitmapFont(Gdx.files.internal("fuente_arial/font-export.fnt"), Gdx.files.internal("fuente_arial/font-export.png"), false);

        // Crear estilo de etiqueta con la fuente personalizada
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = customFont;
        labelStyle.fontColor = Color.WHITE;

        String texto = "";
        if (principal.getTipo().equals("gato")) {
            texto = "Tu abuela diviso un ratón en\nlas inmediaciones,\n ayúdala a encontrarlo\n en los lugares ocultos\n para poder llevarlo\n a otro lado.";
        } else {
            texto = "Tu abuela perdió una pelota,\n ayúdala a buscarla.";
        }

        Label textLabel = new Label(texto, labelStyle);
        textLabel.setAlignment(Align.center);

        // Crear una tabla para organizar los elementos
        table = new Table();
        table.setFillParent(true);

        // Fondo del texto
        Texture textBgTexture = new Texture(Gdx.files.internal("fondo_texto.jpeg"));
        Image textBackground = new Image(textBgTexture);
        textBackground.setFillParent(true);

        Stack textStack = new Stack();
        textStack.add(textBackground); // Añadir fondo al Stack
        textStack.add(textLabel);

        table.add(textStack).fillY().fillX(); // Añadir espacio debajo del texto
        table.row();

        // Crear el botón de "Continuar"
        TextButton continuarButton = new TextButton("Continuar", skin);
        continuarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Acción al presionar el botón Continuar
                principal.setScreen(new JuegoBuscaRatonPelotaScreen(principal));
            }
        });

        table.add(continuarButton).height(60).width(200).padTop(20);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float alpha) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(alpha);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose(); // Liberar recursos del Skin
        customFont.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
