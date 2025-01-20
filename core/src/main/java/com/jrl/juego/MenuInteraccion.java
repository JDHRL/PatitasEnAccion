package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.io.IOException;

public class MenuInteraccion extends BaseScreen {

    private Stage stage;
    private Texture fondoTexture;
    private Image fondoImage;

    private Texture animalOjosAbiertosTexture;
    private Texture animalOjosCerradosTexture;

    private Animation<TextureRegion> animalAnimacion;
    private float stateTime;

    private Image animalButton;
    private Image energiaButton;
    private Image huesoButton;
    private Image juegosButton;

    private float energia;

    private float huesoPosX = 550;
    private float huesoPosY = 350;
    private float juegosPosX = 0;
    private float juegosPosY = 350;

    private ProgressBar progressBar;
    private Skin skin; // Declara la variable skin
    private DogMoodPredictor predictor;
    private double predictedMood;
    private EmotionRecognition emotionRecognition;
    private String tipoanimal;
    public MenuInteraccion(Principal principal) {
        super(principal);
        tipoanimal=principal.getTipo();
        predictor= new DogMoodPredictor();
        predictor.moodHistory.add(new double[]{9, 1});
        predictor.moodHistory.add(new double[]{10, 4});
        stage = new Stage(new StretchViewport(800, 600));
        fondoTexture = new Texture(Gdx.files.internal("background.png"));
        fondoImage = new Image(fondoTexture);
        fondoImage.setFillParent(true);
        emotionRecognition = new EmotionRecognition();
        emotionRecognition.setup();
        try {
            saveTextToFile(tipoanimal, "tipo.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        energia = 100f;
        tipoanimal = tipoanimal.replaceAll("[\n]", "");
        if (tipoanimal.equals("perro")) {
            animalOjosAbiertosTexture = new Texture(Gdx.files.internal("perro/perrito_feliz_despierto.png"));
            animalOjosCerradosTexture = new Texture(Gdx.files.internal("perro/perrito_feliz_durmiendo.png"));
        } else if (tipoanimal.equals("gato")) {
            animalOjosAbiertosTexture = new Texture(Gdx.files.internal("gato/gato_feliz_despierto.png"));
            animalOjosCerradosTexture = new Texture(Gdx.files.internal("gato/gato_feliz_durmiendo.png"));
        }

        TextureRegion[] animalFrames = new TextureRegion[2];
        animalFrames[0] = new TextureRegion(animalOjosAbiertosTexture);
        animalFrames[1] = new TextureRegion(animalOjosCerradosTexture);

        animalAnimacion = new Animation<TextureRegion>(0.5f, animalFrames);
        stateTime = 0f;

        animalButton = new Image(animalOjosAbiertosTexture);

        String finalTipoAnimal = tipoanimal;
        animalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println(finalTipoAnimal + " seleccionado");
            }
        });

        energiaButton = new Image(new Texture(Gdx.files.internal("energia/posion_100_porciento.png")));

        huesoButton = new Image(new Texture(Gdx.files.internal("alimentos/huesito.png")));
        huesoButton.setPosition(huesoPosX, huesoPosY);
        huesoButton.setSize(250, 250);
        juegosButton = new Image(new Texture(Gdx.files.internal("control.png")));
        juegosButton.setPosition(juegosPosX, juegosPosY);
        juegosButton.setSize(250, 250);
        juegosButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                predictor.shutdown();
                emotionRecognition.videoCapture.release();
                principal.setTipo(tipoanimal);
                principal.setScreen(new MenuJuegosScreen(principal));

            }
        });
        huesoButton.addListener(new ClickListener() {
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                huesoButton.moveBy(x - huesoButton.getWidth() / 2, y - huesoButton.getHeight() / 2);
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isHuesoOverAnimal()) {
                    energia = 100;
                }
                huesoButton.setPosition(huesoPosX, huesoPosY);
            }
        });

        // Cargar la skin desde el archivo JSON
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Crear la barra de progreso usando la skin
        progressBar = new ProgressBar(0, 100, 1, false, skin);
        progressBar.setValue(energia);
        progressBar.setWidth(250);
        progressBar.setHeight(25);
        progressBar.setPosition(275, 200); // Posición de la barra

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(energiaButton).padBottom(50);
        table.row();
        table.add(animalButton).padBottom(10);
        table.row();
        table.add(progressBar).padBottom(50); // Agrega la barra de progreso

        stage.addActor(fondoImage);
        stage.addActor(juegosButton);
        stage.addActor(table);
        stage.addActor(huesoButton);

        Gdx.input.setInputProcessor(stage);
        this.tipoanimal=tipoanimal;
    }

    private boolean isHuesoOverAnimal() {
        float huesoCenterX = huesoButton.getX() + huesoButton.getWidth() / 2;
        float huesoCenterY = huesoButton.getY() + huesoButton.getHeight() / 2;

        return huesoCenterX > animalButton.getX() &&
            huesoCenterX < animalButton.getX() + animalButton.getWidth() &&
            huesoCenterY > animalButton.getY() &&
            huesoCenterY < animalButton.getY() + animalButton.getHeight();
    }

    public void saveTextToFile(String text, String fileName) throws IOException {
        FileHandle fileHandle = Gdx.files.local(fileName);
        fileHandle.writeString(text, false);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;

        energia -= delta * 5;
        if (energia < 0) {
            energia = 0;
        }

        // Actualiza la barra de progreso con el valor de energía

        String detectedEmotion = emotionRecognition.captureAndRecognize();
        int valor=1;

        switch (detectedEmotion.toLowerCase()) {
            case "enojo":
                valor = 1;
                break;
            case "tristeza":
                valor = 2;
                break;
            case "felicidad":
                valor = 3;
                break;
            case "sorpresa":
                valor = 4;
                break;
            default:
                 // Salir si la emoción no es válida
        }
        if (energia > 80) {
            energiaButton.setDrawable(new Image(new Texture(Gdx.files.internal("energia/posion_100_porciento.png"))).getDrawable());
        } else if (energia > 50) {
            energiaButton.setDrawable(new Image(new Texture(Gdx.files.internal("energia/posion_80_porciento.png"))).getDrawable());
        } else if (energia > 10) {
            energiaButton.setDrawable(new Image(new Texture(Gdx.files.internal("energia/posion_50_porciento.png"))).getDrawable());
        } else {
            energiaButton.setDrawable(new Image(new Texture(Gdx.files.internal("energia/posion_10_porciento.png"))).getDrawable());
        }
        predictedMood = predictor.predictDogMood(energia, valor);
        progressBar.setValue(Math.round(predictedMood));
        energiaButton.setSize(250, 250);
        energiaButton.setPosition(280, 350);

        TextureRegion currentFrame = animalAnimacion.getKeyFrame(stateTime, true);
        animalButton.setDrawable(new Image(currentFrame).getDrawable());

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        fondoTexture.dispose();
        animalOjosAbiertosTexture.dispose();
        animalOjosCerradosTexture.dispose();
        stage.dispose();
        skin.dispose(); // Asegúrate de liberar la skin
        predictor.shutdown();
        emotionRecognition.videoCapture.release();
    }
    @Override
    public void resize(int width, int height) {
        // Actualizar el viewport para que coincidan las posiciones de entrada
        stage.getViewport().update(width, height, true);
    }
}
