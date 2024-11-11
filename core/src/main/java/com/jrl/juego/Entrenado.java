package com.jrl.juego;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import org.bytedeco.opencv.opencv_core.Mat;

import java.util.ArrayList;
import java.util.List;

public class Entrenado extends BaseScreen {
    private Skin skin;
    private Stage stage;
    private Texture fondoTexture;
    private Image fondoImage;
    private ProgressBar progressBar;
    private Label progressLabel;
    private Label instructionLabel;
    private float progress = 0f;
    private int n = 0;
    private Entrenamiento entrenar;
    private String[] emotionsList=null;
    private List<Integer> labels = new ArrayList<>();
    private boolean crecer;
    public Entrenado(Principal principal) {
        super(principal);


        // Cargar el skin y configurar el stage
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(new StretchViewport(800, 600));

        // Configurar la textura de fondo y crear la imagen
        fondoTexture = new Texture(Gdx.files.internal("niñoconperro.jpeg"));
        fondoImage = new Image(fondoTexture);
        fondoImage.setFillParent(true); // Hacer que el fondo ocupe toda la pantalla
        stage.addActor(fondoImage);

        // Crear barra de progreso y etiquetas
        progressBar = new ProgressBar(0, 100, 1, false, skin);
        progressBar.setValue(progress);
        progressBar.setAnimateDuration(0.1f);

        progressLabel = new Label("Progreso: 0%", skin);
        instructionLabel = new Label("Tu mascota se esta entrenando para saber como son tus animos", skin);
        instructionLabel.setFontScale(2.0f);
        progressLabel.setFontScale(2.0f);

        // Organizar elementos en la tabla
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(20);

        table.add(instructionLabel).padBottom(10);
        table.row();
        table.add(progressBar).width(400).height(20).padBottom(10);
        table.row();
        table.add(progressLabel);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
        entrenar=new Entrenamiento();
        progress=10;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


            if (progress <= 100) {

                if(progress==10) {
                    emotionsList = entrenar.verificarDirectorioDatos();
                    progress = 50;

                }else if (progress==50) {
                    entrenar.listarEmociones(emotionsList);
                    crecer=true;
                }
                else if (progress>=90) {

                    List<Mat> facesData = entrenar.recopilarImagenesEtiquetas(emotionsList, labels);
                    entrenar.obtenerModelo(Entrenamiento.RECOGNIZER_METHOD, facesData, labels);
                    progress=100;
                    principal.setScreen(new MenuAdopcion(principal));

                }
                progressLabel.setText("Progreso: " + (int) progress + "%");
                progressBar.setValue(progress);

                // Cambiar el fondo y el texto basado en la emoción actual

            } else {
                n++;
                progress = 0;
            }

            if (crecer){
                progress+=delta*10;
            }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }



    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        fondoTexture.dispose();
        skin.dispose();
    }
}


