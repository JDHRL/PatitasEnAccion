package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jrl.juego.entidades.Animal;
import com.jrl.juego.entidades.Estados;
import com.jrl.juego.entidades.Jugador;
import com.jrl.juego.guardado.GuardadoObjeto;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuInteraccionScreen extends BaseScreen {

    private Stage stage;
    private Texture fondoTexture;
    private Image fondoImage;

    private Texture animalOjosAbiertosTexture;
    private Texture animalOjosCerradosTexture;

    private Animation<TextureRegion> animalAnimacion;
    private float stateTime,elapsedTime=0;

    private Image animalButton;
    private Image energiaButton;
    private Image huesoButton;
    private Image juegosButton;

    private float energia;

    private float huesoPosX = 550;
    private float huesoPosY = 350;
    private float juegosPosX = 0;
    private float juegosPosY = 350;

    //private ProgressBar progressBar;
    private Skin skin; // Declara la variable skin
    private DogMoodPredictor predictor;
    private EmotionRecognition emotionRecognition;
    private String tipoanimal;
    private Image energia100;
    private Image energia80;
    private Image energia50;
    private Image energia10;
    private Texture alimento_default;
    private int hora = 0;
    private int minuto = 0;
    private int segundo = 0;
    private float touchStartX, touchStartY; // Coordenadas iniciales
    private final float MOVE_THRESHOLD = 10f;
    private double predictedMood;
    private Texture enfermo;
    HashMap<String,Integer> emociones=new HashMap<>();
    public MenuInteraccionScreen(){
        emotionRecognition = new EmotionRecognition();
    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        super.show();
       emotionRecognition.inicializar_capturador();
        principal.reproducirMusica(0);

        energia100=new Image(new Texture(Gdx.files.internal("energia/posion_100_porciento.png")));
        energia50=new Image(new Texture(Gdx.files.internal("energia/posion_50_porciento.png")));
        energia80=new Image(new Texture(Gdx.files.internal("energia/posion_80_porciento.png")));
        energia10=new Image(new Texture(Gdx.files.internal("energia/posion_10_porciento.png")));
        tipoanimal=principal.getJugador().getMascota().getTipo();
        predictor= new DogMoodPredictor();
        predictor.moodHistory.add(new double[]{9, 1,1});
        predictor.moodHistory.add(new double[]{10, 4,1});
        stage = new Stage(new StretchViewport(800, 600));
        fondoTexture = new Texture(Gdx.files.internal("background.png"));
        fondoImage = new Image(fondoTexture);
        fondoImage.setFillParent(true);

        emotionRecognition.setup();
        try {
            saveTextToFile(tipoanimal, "tipo.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        energia =principal.getJugador().getMascota().getSalud();
        tipoanimal = tipoanimal.replaceAll("[\n]", "");
        if (tipoanimal.equals("perro")) {
            enfermo=new Texture(Gdx.files.internal("perro_rabia.png"));
            animalOjosAbiertosTexture = new Texture(Gdx.files.internal("perro/perrito_feliz_despierto.png"));
            animalOjosCerradosTexture = new Texture(Gdx.files.internal("perro/perrito_feliz_durmiendo.png"));
        } else if (tipoanimal.equals("gato")) {
            enfermo=new Texture(Gdx.files.internal("gatito_rabia.png"));
            animalOjosAbiertosTexture = new Texture(Gdx.files.internal("gato/gato_feliz_despierto.png"));
            animalOjosCerradosTexture = new Texture(Gdx.files.internal("gato/gato_feliz_durmiendo.png"));
        }

        TextureRegion[] animalFrames = new TextureRegion[2];
        animalFrames[0] = new TextureRegion(animalOjosAbiertosTexture);
        animalFrames[1] = new TextureRegion(animalOjosCerradosTexture);

        animalAnimacion = new Animation<TextureRegion>(0.5f, animalFrames);
        stateTime = 0f;

        animalButton = new Image(animalOjosAbiertosTexture);
        principal.getJugador().getMascota().setTipo(tipoanimal);
        animalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                predictor.shutdown();
                emotionRecognition.videoCapture.release();
                principal.setScreen(Pantallas.CARACTERISTICAS_MASCOTA.getPantalla());
            }
        });

        energiaButton = new Image(new Texture(Gdx.files.internal("energia/posion_100_porciento.png")));
        energiaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                predictor.shutdown();
                emotionRecognition.videoCapture.release();

            principal.setScreen(Pantallas.MENUADMINISTRACIONMEDICINA.getPantalla());

            }
        });
        alimento_default=new Texture(Gdx.files.internal("alimentos/huesito_cortado.png"));
       if(principal.getSeleccionado()==null){
            huesoButton = new Image(alimento_default);
        }
        else {
           Texture alimento_escogido=new Texture(Gdx.files.internal(principal.getSeleccionado().getImagen()));
           huesoButton=new Image(alimento_escogido);
        }

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
                principal.setScreen(Pantallas.MENUJUEGOS.getPantalla());

            }
        });

        huesoButton.addListener(new ClickListener() {
            private boolean movido=false;
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if(principal.getSeleccionado()!=null) {
                    huesoButton.moveBy(x - huesoButton.getWidth() / 2, y - huesoButton.getHeight() / 2);
                }
                super.touchDragged(event, x, y, pointer);
                movido=true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchStartX = x; // Guarda las coordenadas iniciales
                touchStartY = y;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isHuesoOverAnimal()) {
                    if(principal.getSeleccionado().getCantidad()>1) {
                        principal.getSeleccionado().setCantidad(principal.getSeleccionado().getCantidad() - 1);
                        Animal mascota=principal.getJugador().getMascota();
                        if(principal.getSeleccionado().getSalud()+mascota.getSalud()<=100) {
                            mascota.setSalud(principal.getSeleccionado().getSalud() + mascota.getSalud());
                        }else{
                            mascota.setSalud(100);
                        }
                        if(principal.getSeleccionado().getEnergia()+mascota.getEnergia()<=100){
                            mascota.setEnergia(principal.getSeleccionado().getEnergia()+mascota.getEnergia());
                        }
                        else {
                            mascota.setEnergia(100);
                        }


                    }else{
                        huesoButton.setDrawable(new Image(alimento_default).getDrawable());
                        principal.getJugador().getAlimentos().remove(principal.getSeleccionado().getNombre());
                        principal.setSeleccionado(null);
                    }


                }
                float deltaX = Math.abs(x - touchStartX); // Calcula la distancia horizontal
                float deltaY = Math.abs(y - touchStartY); // Calcula la distancia vertical
                if (deltaX < MOVE_THRESHOLD && deltaY < MOVE_THRESHOLD) {
                    predictor.shutdown();
                    emotionRecognition.videoCapture.release();
                    principal.getJugador().getMascota().setTipo(tipoanimal);
                    principal.setScreen(Pantallas.MENUALIMENTOS.getPantalla());
                }
                else{
                    movido=false;
                }
                huesoButton.setPosition(huesoPosX, huesoPosY);

            }
        });

        // Cargar la skin desde el archivo JSON
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));


        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(energiaButton).padBottom(50);
        table.row();
        table.add(animalButton).padBottom(10);
        table.row();
       // table.add(progressBar).padBottom(50); // Agrega la barra de progreso

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
    boolean eme =false;
    @Override
    public void render(float delta) {
        energia=principal.getJugador().getMascota().getSalud();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;
        elapsedTime+=delta;

        /*energia -= delta * 5;
        if (energia < 0) {
            energia = 0;
        }*/

        // Actualiza la barra de progreso con el valor de energía
        if(elapsedTime>=1.0f){
            elapsedTime=0;
            String detectedEmotion="no se detectó ninguna emoción";
            if(!eme) {
                try {
                    detectedEmotion = emotionRecognition.captureAndRecognize();
                } catch (Exception es) {
                    eme = true;
                    emotionRecognition.videoCapture.release();
                }
            }
            else{
                ArrayList<String> lista=new ArrayList<>();
                lista.add("Felicidad");
                lista.add("Enojo");
                lista.add("Sorpresa");
                lista.add("Tristeza");
                int numero= MathUtils.random(0, lista.size()-1);
                detectedEmotion=lista.get(numero);
            }
        if(!detectedEmotion.equals("no se detectó ninguna emoción")) {
            emociones.put(detectedEmotion.toLowerCase(), emociones.getOrDefault(detectedEmotion.toLowerCase(), 0) + 1);
        }
        if (energia > 80) {
            energiaButton.setDrawable(energia100.getDrawable());
        } else if (energia > 50) {
            energiaButton.setDrawable(energia80.getDrawable());
        } else if (energia > 10) {
            energiaButton.setDrawable(energia50.getDrawable());
        } else {
            energiaButton.setDrawable(energia10.getDrawable());
        }
        predictedMood = predictor.predictDogMood(principal.getJugador().getMascota().getSalud(), principal.getJugador().getMascota().getEnergia(),principal.getMedicinastienda().size());

        if((int) Math.round(predictedMood)==Estados.SALUDABLE.getIndice()) {
            principal.getJugador().getMascota().setEstado(Estados.SALUDABLE.toString());
        }
        else if ((int) Math.round(predictedMood)==Estados.ENFERMO_PARASITOS.getIndice()){

            principal.getJugador().getMascota().setEstado(Estados.ENFERMO_PARASITOS.toString());
        }
        else if ((int) Math.round(predictedMood)==Estados.ENFERMO_DE_CLAMIDIA.getIndice()){

            principal.getJugador().getMascota().setEstado(Estados.ENFERMO_DE_CLAMIDIA.toString());
        }
        else if ((int) Math.round(predictedMood)==Estados.ENFERMO_DE_PARMOVIRUS.getIndice()){

            principal.getJugador().getMascota().setEstado(Estados.ENFERMO_DE_PARMOVIRUS.toString());
        }
        else if ((int) Math.round(predictedMood)==Estados.ENFERMO_DE_RABIA.getIndice()){

            principal.getJugador().getMascota().setEstado(Estados.ENFERMO_DE_RABIA.toString());
        }
        energiaButton.setSize(250, 250);
        energiaButton.setPosition(280, 350);
            segundo++;
            if (segundo >= 60) {
                segundo = 0;
                minuto++;
                if (minuto >= 60) {
                    minuto = 0;
                    hora++;
                    if (hora >= 24) {
                        hora = 0; // Reinicia la hora al llegar a las 24
                    }
                }
            }

        if(hora%6==0&&hora!=0){
            if(!principal.getJugador().getMedicinas().isEmpty()) {
                principal.getJugador().getMedicinas().remove(0);
            }
        }
        if(segundo%15==0&&segundo!=0) {
            int maximo = 0;
            String emocionSalida = "enojo";
            for (String emocion : emociones.keySet()) {
                if (emociones.get(emocion) > maximo) {
                    maximo = emociones.get(emocion);
                    emocionSalida = emocion;
                }
            }
            Animal mascota = principal.getJugador().getMascota();
            switch (emocionSalida) {
                case "enojo":
                case "tristeza":
                    if (mascota.getEnergia() - 5 > 0) {
                        mascota.setEnergia(mascota.getEnergia() - 5);
                    } else {
                        mascota.setEnergia(0);
                    }
                    break;
                case "felicidad":
                case "sorpresa":
                    if (mascota.getEnergia() + 6 <= 100) {
                        mascota.setEnergia(mascota.getEnergia() + 6);
                    } else {
                        mascota.setEnergia(100);
                    }
                    break;
                default:

            }
        }
            if(segundo%20==0&&segundo!=0) {
                Animal mascota = principal.getJugador().getMascota();
                if (mascota.getEnergia() >=60) {
                    if( mascota.getSalud()-5>=0) {
                        mascota.setSalud(mascota.getSalud() - 5);
                    }
                    else {
                        mascota.setSalud(0);
                    }
                }else{
                    if( mascota.getSalud()-1>=0) {
                        mascota.setSalud(mascota.getSalud() - 1);
                    }
                    else {
                        mascota.setSalud(0);
                    }
                }
            }
        }
        TextureRegion currentFrame = animalAnimacion.getKeyFrame(stateTime, true);
        if(principal.getJugador().getMascota().getEstado().equals(Estados.SALUDABLE.toString())) {
            animalButton.setDrawable(new Image(currentFrame).getDrawable());
        }else{
            animalButton.setDrawable(new Image(enfermo).getDrawable());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            principal.setScreen(Pantallas.MENUSALIDASCREEN.getPantalla()); // Cambiar a la pantalla deseada
        }
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void dispose() {
        super.dispose();
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
