package com.jrl.juego.minijuegos.BuscaRatonPelota;

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
import com.jrl.juego.*;
import com.jrl.juego.minijuegos.PantallaGanasteMoneda;

public class JuegoBuscaRatonPelotaScreen extends BaseScreen {

    private final Stage stage;
    private final Skin skin;
    private final Table table;
    private final BitmapFont customFont;
    private String tipo;

    public JuegoBuscaRatonPelotaScreen(Principal principal) {
        super(principal);
        tipo = principal.getTipo();
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

        // Fondo del cuarto
        Texture backgroundTexture = new Texture(Gdx.files.internal("cuarto.jpeg"));
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
                principal.setScreen(new MenuJuegosScreen(principal)); // Cambia por tu pantalla principal
            }
        });
        table.add(volverButton).padTop(20).center().row();

        // Añadir la tabla al escenario
        stage.addActor(table);

        // Lógica para el objeto (ratón o pelota)
        Texture objetoTextura;
        Button objetoButton;
        if (tipo.equals("gato")) {
            textLabel.setText("¡Encuentra al ratón escondido\n en el cuarto!");
            objetoTextura = new Texture(Gdx.files.internal("raton_escondido.png"));
        } else {
            textLabel.setText("¡Encuentra la pelota escondida\n en el cuarto!");
            objetoTextura = new Texture(Gdx.files.internal("pelota_escondida.png"));
        }

        objetoButton = new Button(new TextureRegionDrawable(objetoTextura));
        objetoButton.setSize(50, 50);
        setRandomPosition(objetoButton, textBackground); // Posición inicial aleatoria

        ClickListener click = new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                nivel2(background, objetoButton, tipo, this, textLabel);
            }
        };
        objetoButton.addListener(click);
        stage.addActor(objetoButton);
    }

    private void nivel2(Image background, Button objetoButton, String tipo, ClickListener click, Label textLabel) {
        Texture fondo = new Texture(Gdx.files.internal("garaje.jpeg"));
        background.setDrawable(new Image(fondo).getDrawable());
        objetoButton.removeListener(click);
        setRandomPosition(objetoButton, background);

        if (tipo.equals("gato")) {
            textLabel.setText("¡Encuentra al ratón escondido\n en el garaje!");
        } else {
            textLabel.setText("¡Encuentra la pelota escondida\n en el garaje!");
        }

        click = new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                nivel3(background, objetoButton, tipo, this, textLabel);
            }
        };
        objetoButton.addListener(click);
    }

    private void nivel3(Image background, Button objetoButton, String tipo, ClickListener click, Label textLabel) {
        Texture fondo = new Texture(Gdx.files.internal("patio.jpeg"));
        background.setDrawable(new Image(fondo).getDrawable());
        objetoButton.removeListener(click);
        setRandomPosition(objetoButton, background);

        if (tipo.equals("gato")) {
            textLabel.setText("¡Encuentra al ratón escondido\n en el patio!");
        } else {
            textLabel.setText("¡Encuentra la pelota escondida\n en el patio!");
        }

        click = new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                principal.setScreen(new PantallaGanasteMoneda(principal,1));
            }
        };
        objetoButton.addListener(click);
    }

    private void setRandomPosition(Button objetoButton, Image background) {
        table.layout();
        float randomX = MathUtils.random(50, (int) background.getWidth()); // Limites dentro del escenario (800x900)
        float randomY = MathUtils.random(50, (int) background.getHeight());

        objetoButton.setPosition(randomX, (stage.getHeight() - randomY) - objetoButton.getHeight());
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

