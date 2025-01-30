package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jrl.juego.entidades.Estados;

public class CaracteristicasScreen extends BaseScreen {

    private Stage stage;
    private Table mainTable;
    private ProgressBar healthBar;
    private ProgressBar energyBar;
    private Label healthLabel;
    private Label energyLabel;
    private Label titleLabel;
    private Label estadoLabel;
    private BitmapFont customFont;

    @Override
    public void show() {
        super.show();

        stage = new Stage(new StretchViewport(800, 600));
        Gdx.input.setInputProcessor(stage);

        // Cargar la skin para UI
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        customFont = new BitmapFont(Gdx.files.internal("fuente_arial/fuente_con_borde.fnt"), Gdx.files.internal("fuente_arial/fuente_con_borde.png"), false);

        // Crear estilo de etiquetas
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = customFont;
        labelStyle.fontColor = Color.WHITE;

        // Crear estilo para el estado
        Label.LabelStyle estadoStyle = new Label.LabelStyle();
        estadoStyle.font = customFont;
        estadoStyle.fontColor = Color.GREEN;

        // Tabla principal
        mainTable = new Table();
        mainTable.setFillParent(true);

        // Título
        titleLabel = new Label("Características del Personaje", labelStyle);
        titleLabel.setFontScale(2f);
        mainTable.add(titleLabel).colspan(2).padBottom(20).center();
        mainTable.row();

        // Salud
        healthLabel = new Label("Salud("+principal.getJugador().getMascota().getSalud()+"%)", labelStyle);
        healthLabel.setFontScale(1.5f);
        healthBar = new ProgressBar(0, 100, 1, false, skin);
        healthBar.setValue(principal.getJugador().getMascota().getSalud()); // Ejemplo: Salud inicial al 75%
        healthBar.setAnimateDuration(0.5f);
        mainTable.add(healthLabel).padBottom(10).left().padLeft(20);
        mainTable.add(healthBar).width(400).height(30).padBottom(10).padLeft(10).center();
        mainTable.row();



        // Energía
        energyLabel = new Label("Energía("+principal.getJugador().getMascota().getEnergia()+"%)", labelStyle);
        energyLabel.setFontScale(1.5f);
        energyBar = new ProgressBar(0, 100, 1, false, skin);
        energyBar.setValue(principal.getJugador().getMascota().getEnergia()); // Ejemplo: Energía inicial al 50%
        energyBar.setAnimateDuration(0.5f);
        mainTable.add(energyLabel).padBottom(10).left().padLeft(20);
        mainTable.add(energyBar).width(400).height(30).padBottom(10).padLeft(10).center();
        mainTable.row();
        // Etiqueta de estado
        String estado=principal.getJugador().getMascota().getEstado();
        if(estado.equals(Estados.SALUDABLE.toString())) {
            estadoStyle.fontColor = Color.GREEN;
            estadoLabel = new Label("Estado: " + Estados.SALUDABLE, estadoStyle);
        } else if (estado.equals(Estados.ENFERMO_PARASITOS.toString())) {
            estadoStyle.fontColor = Color.YELLOW;
            estadoLabel = new Label("Estado:"+Estados.ENFERMO_PARASITOS, estadoStyle);
        } else if (estado.equals(Estados.ENFERMO_DE_CLAMIDIA.toString())) {
            estadoStyle.fontColor = Color.YELLOW;
            estadoLabel = new Label("Estado:"+Estados.ENFERMO_DE_CLAMIDIA, estadoStyle);
        } else if (estado.equals(Estados.ENFERMO_DE_PARMOVIRUS.toString())) {
            estadoStyle.fontColor = Color.YELLOW;
            estadoLabel = new Label("Estado:"+Estados.ENFERMO_DE_PARMOVIRUS, estadoStyle);
        } else if (estado.equals(Estados.ENFERMO_DE_RABIA.toString())) {
            estadoStyle.fontColor = Color.RED;
            estadoLabel = new Label("Estado:"+Estados.ENFERMO_DE_RABIA, estadoStyle);
        }
       else {
            estadoStyle.fontColor = Color.RED;
            estadoLabel = new Label("Estado: Muerto", estadoStyle);
       }
        estadoLabel.setFontScale(1.5f);
        mainTable.add(estadoLabel).colspan(2).padBottom(15).center();
        mainTable.row();

        // Botón para volver
        Texture buttonTexture = new Texture(Gdx.files.internal("imagen_boton_casa.png"));
        Button backButton = new Button(new TextureRegionDrawable(buttonTexture));
        backButton.setSize(50, 50);
        backButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                principal.setScreen(Pantallas.MENUINTERACCION.getPantalla());
                return true;
            }
            return false;
        });

        mainTable.add(backButton).colspan(2).padTop(20).size(50, 50).center();

        // Agregar la tabla al escenario
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
