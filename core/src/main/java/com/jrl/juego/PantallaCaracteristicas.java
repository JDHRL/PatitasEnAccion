package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jrl.juego.entidades.Alimento;
import com.jrl.juego.entidades.Medicina;

public class PantallaCaracteristicas extends BaseScreen {

    private Stage stage;
    private Skin skin;
    private Table table;
    private BitmapFont customFont;
    private String tipo;
    private Medicina medicina;
    private Alimento alimento;

    @Override
    public void show() {
        super.show();
        principal.reproducirMusica(2);
        tipo = principal.getJugador().getMascota().getTipo();
        stage = new Stage(new StretchViewport(800, 900));
        Gdx.input.setInputProcessor(stage);

        // Cargar el Skin
        skin = new Skin(Gdx.files.internal("skin/uskin.json"));

        // Cargar la fuente desde el archivo .fnt y la textura correspondiente
        customFont = new BitmapFont(Gdx.files.internal("fuente_arial/fuente_con_borde.fnt"), Gdx.files.internal("fuente_arial/fuente_con_borde.png"), false);

        // Crear estilo de etiqueta con la fuente personalizada
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = customFont;
        labelStyle.fontColor = Color.WHITE;

        // Crear la tabla principal
        table = new Table();
        table.setFillParent(true);

        // Etiqueta y barra de progreso para Salud
        String porcentajeSalud;
        String porcentajeEnergia;
        if(alimento==null){
            porcentajeSalud=medicina.getSalud()+"";
            porcentajeEnergia=medicina.getEnergia()+"";
        }
        else{
            porcentajeSalud=alimento.getSalud()+"";
            porcentajeEnergia=alimento.getEnergia()+"";
        }
        Label healthLabel = new Label("Salud:("+porcentajeSalud+"%)", labelStyle);
        healthLabel.setFontScale(1.5f);
        ProgressBar healthBar = new ProgressBar(0, 100, 1, false, skin);
        if (alimento == null) {
            healthBar.setValue(medicina.getSalud());
        }
        else{
            healthBar.setValue(alimento.getSalud());
        }
        healthBar.setAnimateDuration(0.5f);

        table.add(healthLabel).left().padLeft(20).padTop(10);
        table.row();
        table.add(healthBar).width(400).height(30).padLeft(20).padBottom(10).padTop(5);
        table.row();

        // Etiqueta y barra de progreso para Energía
        Label energyLabel = new Label("Energía("+porcentajeEnergia+"%):", labelStyle);
        energyLabel.setFontScale(1.5f);
        ProgressBar energyBar = new ProgressBar(0, 100, 1, false, skin);
        if (alimento == null) {
            energyBar.setValue(medicina.getEnergia());
        }else{
            energyBar.setValue(alimento.getEnergia());
        }
        energyBar.setAnimateDuration(0.5f);

        table.add(energyLabel).left().padLeft(20).padTop(10);
        table.row();
        table.add(energyBar).width(400).height(30).padLeft(20).padBottom(10).padTop(5);
        table.row();

        Texture backgroundTexture;
        if (alimento == null) {
            backgroundTexture = new Texture(Gdx.files.internal(medicina.getImagen()));
        } else {
            backgroundTexture = new Texture(Gdx.files.internal(alimento.getImagen()));
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
        textLabel.setFontScale(1f);
        textStack.add(textBackground);
        textStack.add(textLabel);
        table.add(textStack).height(300).fillX().padTop(10).row();

        // Botón de volver al menú principal
        TextButton volverButton = new TextButton("Volver al Menú", skin);
        volverButton.getLabel().setStyle(labelStyle);
        volverButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (alimento != null) {
                    principal.setScreen(Pantallas.MENUCOMPRAALIMENTOS.getPantalla()); // Cambia por tu pantalla principal
                }else {
                    principal.setScreen(Pantallas.MENUADMINISTRACIONMEDICINA.getPantalla());
                }
            }
        });
        table.add(volverButton).padTop(20).center().row();

        // Añadir la tabla al escenario
        stage.addActor(table);

        if (alimento == null) {
            textLabel.setText(medicina.getDescripcion());
        } else {
            textLabel.setText(alimento.getDescripcion());
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.255f, 0.239f, 1, 1);
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

    public Medicina getMedicina() {
        return medicina;
    }

    public void setMedicina(Medicina medicina) {
        this.medicina = medicina;
        this.alimento=null;
    }

    public Alimento getAlimento() {
        return alimento;
    }

    public void setAlimento(Alimento alimento) {
        this.medicina=null;
        this.alimento = alimento;
    }
}
