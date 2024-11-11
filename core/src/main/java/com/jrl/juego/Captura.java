package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class Captura extends BaseScreen {
    private Skin skin;
    private Stage stage;
    private Texture triste, enojado, feliz, asombrado;
    private Image tristeI, enojadoI, felizI, asombradoI;
    private ProgressBar progressBar;
    private Label progressLabel;
    private Label instructionLabel;
    private Label warningLabel; // Nueva etiqueta para el mensaje adicional
    private float progress = 0f;
    private String[] emotionName = {"Tristeza", "Felicidad", "Enojo", "Sorpresa"};
    private EmotionCapture emotionCapture;

    public Captura(Principal principal) {
        super(principal);
        String dataPath = "./Data";
        emotionCapture = new EmotionCapture(dataPath);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(new StretchViewport(800, 600));

        // Cargar texturas y crear imágenes de fondo
        triste = new Texture(Gdx.files.internal("niñotriste.jpeg"));
        enojado = new Texture(Gdx.files.internal("niñoenojado.jpeg"));
        feliz = new Texture(Gdx.files.internal("niñosonriendo.jpeg"));
        asombrado = new Texture(Gdx.files.internal("niñoasombrado.jpeg"));

        tristeI = new Image(triste);
        tristeI.setFillParent(true);
        enojadoI = new Image(enojado);
        enojadoI.setFillParent(true);
        felizI = new Image(feliz);
        felizI.setFillParent(true);
        asombradoI = new Image(asombrado);
        asombradoI.setFillParent(true);

        // Agregar todos los fondos al stage, pero solo mostrar uno a la vez
        stage.addActor(tristeI);
        stage.addActor(enojadoI);
        stage.addActor(felizI);
        stage.addActor(asombradoI);

        // Inicialmente solo el fondo de "triste" será visible
        setBackgroundVisible(tristeI);

        // Crear barra de progreso y etiquetas
        progressBar = new ProgressBar(0, 100, 1, false, skin);
        progressBar.setValue(progress);
        progressBar.setAnimateDuration(0.1f);

        progressLabel = new Label("Progreso: 0%", skin);
        instructionLabel = new Label("Por favor pon tu cara triste", skin);
        instructionLabel.setFontScale(3.0f);
        progressLabel.setFontScale(4.0f);
        instructionLabel.setColor(Color.GOLD);

        // Crear una tabla para el fondo de la instrucción
        Table instructionTable = new Table();
        instructionTable.setBackground(skin.newDrawable("black", Color.BLACK)); // Fondo negro
        instructionTable.add(instructionLabel).pad(10); // Añadir la etiqueta de instrucciones con padding

        // Crear una nueva etiqueta para el mensaje de advertencia
        warningLabel = new Label("", skin);
        warningLabel.setFontScale(2.0f);
        warningLabel.setColor(Color.GOLD);

        // Crear una tabla para el fondo de la advertencia
        Table warningTable = new Table();
        warningTable.setBackground(skin.newDrawable("black", Color.BLACK)); // Fondo negro
        warningTable.add(warningLabel).pad(10); // Añadir la etiqueta de advertencia con padding

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(20);

        table.add(instructionTable).padBottom(10); // Agregar tabla de instrucciones
        table.row();
        table.add(progressBar).width(400).height(20).padBottom(10);
        table.row();
        table.add(progressLabel).padBottom(10);
        table.row();
        table.add(warningTable); // Agregar tabla de advertencia

        // Agregar tabla y otros elementos al stage
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    int n = 0;

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (n < emotionName.length) {
            if (progress <= 100) {

                progressBar.setValue(progress);
                progressLabel.setText("Progreso: " + (int) progress + "%");
                if(emotionCapture.captureEmotions(emotionName[n])){
                    progress += 1;
                    warningLabel.setText("");
                }
                else {
                    warningLabel.setText("Por favor posiciona tu cara bien");
                }

                switch (n) {
                    case 0:
                        instructionLabel.setText("Por favor pon tu cara triste");
                        setBackgroundVisible(tristeI);
                        break;
                    case 1:
                        instructionLabel.setText("Por favor pon tu cara feliz");
                        setBackgroundVisible(felizI);
                        break;
                    case 2:
                        instructionLabel.setText("Por favor pon tu cara enojada");
                        setBackgroundVisible(enojadoI);
                        break;
                    case 3:
                        instructionLabel.setText("Por favor pon tu cara sorprendida");
                        setBackgroundVisible(asombradoI);
                        break;
                }
            } else {
                n++;
                progress = 0;
            }
        }
        else {
            if(progress>100){
                progress=100;
            }
            emotionCapture.release();
            principal.setScreen(new Entrenado(principal));
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private void setBackgroundVisible(Image visibleImage) {
        tristeI.setVisible(visibleImage == tristeI);
        enojadoI.setVisible(visibleImage == enojadoI);
        felizI.setVisible(visibleImage == felizI);
        asombradoI.setVisible(visibleImage == asombradoI);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        triste.dispose();
        feliz.dispose();
        enojado.dispose();
        asombrado.dispose();
        skin.dispose();
    }
}
