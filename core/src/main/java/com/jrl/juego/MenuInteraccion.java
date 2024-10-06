package com.jrl.juego;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MenuInteraccion extends BaseScreen {

    private Stage stage;
    private Texture fondoTexture;
    private Image fondoImage;

    // Texturas para la animación del animal
    private Texture animalOjosAbiertosTexture;
    private Texture animalOjosCerradosTexture;

    // Animaciones del animal
    private Animation<TextureRegion> animalAnimacion;
    private float stateTime;

    private Image animalButton;  // Botón para el animal (perro o gato)
    private Image energiaButton;  // Botón de imagen para mostrar la energía
    private Image huesoButton;  // Botón del hueso
    private Image juegosButton;  // Botón de juegos

    private float energia;  // Valor de la energía

    // Posiciones iniciales para los botones
    private float huesoPosX = 550;
    private float huesoPosY = 350;
    private float juegosPosX = 0;  // Posición a la derecha del hueso
    private float juegosPosY = 350;

    // Constructor que recibe la cadena "perro" o "gato"
    public MenuInteraccion(Principal principal, String tipoAnimal) {
        super(principal);

        // Configurar el stage y el fondo
        stage = new Stage(new StretchViewport(800, 600));
        fondoTexture = new Texture(Gdx.files.internal("background.png"));
        fondoImage = new Image(fondoTexture);
        fondoImage.setFillParent(true);

        // Inicializar energía al 100%
        energia = 100f;

        // Cargar las texturas según el tipo de animal
        if (tipoAnimal.equalsIgnoreCase("perro")) {
            animalOjosAbiertosTexture = new Texture(Gdx.files.internal("perro/perrito_feliz_despierto.png"));
            animalOjosCerradosTexture = new Texture(Gdx.files.internal("perro/perrito_feliz_durmiendo.png"));
        } else if (tipoAnimal.equalsIgnoreCase("gato")) {
            animalOjosAbiertosTexture = new Texture(Gdx.files.internal("gato/gato_feliz_despierto.png"));
            animalOjosCerradosTexture = new Texture(Gdx.files.internal("gato/gato_feliz_durmiendo.png"));
        }

        // Crear la animación para el animal (abre y cierra los ojos)
        TextureRegion[] animalFrames = new TextureRegion[2];
        animalFrames[0] = new TextureRegion(animalOjosAbiertosTexture);  // Ojos abiertos
        animalFrames[1] = new TextureRegion(animalOjosCerradosTexture);  // Ojos cerrados

        animalAnimacion = new Animation<TextureRegion>(0.5f, animalFrames);  // 0.5 segundos entre cuadros
        stateTime = 0f;

        // Crear el botón del animal con la primera imagen (ojos abiertos)
        animalButton = new Image(animalOjosAbiertosTexture);
        animalButton.setTouchable(Touchable.enabled);  // Hacer que el botón sea interactivo

        // Listener para interactuar con el animal
        animalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Lógica para la interacción cuando el botón es clicado
                System.out.println(tipoAnimal + " seleccionado");
            }
        });

        // Crear el botón de la energía con el valor inicial de 100%
        energiaButton = new Image(new Texture(Gdx.files.internal("energia/posion_100_porciento.png")));

        // Crear el botón del hueso
        huesoButton = new Image(new Texture(Gdx.files.internal("alimentos/huesito.png")));
        huesoButton.setPosition(huesoPosX, huesoPosY);
        huesoButton.setSize(250, 250);

        // Crear el nuevo botón de juegos
        juegosButton = new Image(new Texture(Gdx.files.internal("control.png"))); // Cambiar la ruta a tu icono
        juegosButton.setPosition(juegosPosX, juegosPosY);
        juegosButton.setSize(250, 250);



        // Hacer el botón del hueso interactivo con drag-and-drop
        DragAndDrop dragAndDrop = new DragAndDrop();
        dragAndDrop.addSource(new DragAndDrop.Source(huesoButton) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setDragActor(huesoButton);  // El actor que arrastramos
                return payload;
            }
        });

        dragAndDrop.addTarget(new DragAndDrop.Target(animalButton) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                // Detectar si el hueso está sobre el animal
                if (x >= animalButton.getX() && x <= animalButton.getWidth() && y >= animalButton.getY() && y <= animalButton.getHeight()) {
                    energia = 100;
                }
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                // Volver el hueso a su posición original cuando se suelta cerca del animal
                huesoButton.setPosition(huesoPosX, huesoPosY);
            }
        });

        // Usar una tabla para organizar los elementos en pantalla
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Añadir el botón de energía encima del animal
        table.add(energiaButton).padBottom(50);
        table.row();  // Pasar a la siguiente fila
        table.add(animalButton);

        // Añadir los actores al stage

        stage.addActor(fondoImage);
        stage.addActor(huesoButton);  // Añadir el botón del hueso al stage
        stage.addActor(juegosButton);
        stage.addActor(table);


        // Establecer el stage como el input processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;

        // Reducir la energía con el tiempo
        energia -= delta * 5;  // Reducir 5 unidades de energía por segundo
        if (energia < 0) {
            energia = 0;
        }

        // Actualizar la imagen de la energía según el porcentaje actual
        if (energia > 80) {
            energiaButton.setDrawable(new Image(new Texture(Gdx.files.internal("energia/posion_100_porciento.png"))).getDrawable());
        } else if (energia > 50) {
            energiaButton.setDrawable(new Image(new Texture(Gdx.files.internal("energia/posion_80_porciento.png"))).getDrawable());
        } else if (energia > 10) {
            energiaButton.setDrawable(new Image(new Texture(Gdx.files.internal("energia/posion_50_porciento.png"))).getDrawable());
        } else {
            energiaButton.setDrawable(new Image(new Texture(Gdx.files.internal("energia/posion_10_porciento.png"))).getDrawable());
        }
        energiaButton.setSize(250,250);
        energiaButton.setPosition(280,350);
        // Obtener el cuadro actual de la animación (ojos abiertos o cerrados)
        TextureRegion currentFrame = animalAnimacion.getKeyFrame(stateTime, true);  // Animación del animal
        animalButton.setDrawable(new Image(currentFrame).getDrawable());
        // Actualizar el estado del stage y dibujar
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        // Liberar recursos de texturas
        fondoTexture.dispose();
        animalOjosAbiertosTexture.dispose();
        animalOjosCerradosTexture.dispose();
        stage.dispose();
    }
}

