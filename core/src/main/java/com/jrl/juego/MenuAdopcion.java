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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MenuAdopcion extends BaseScreen {

    private Stage stage;
    private Texture fondoTexture;
    private Image fondoImage;

    // Texturas para la animación del perrito triste
    private Texture perritoOjosAbiertosTexture;
    private Texture perritoOjosCerradosTexture;

    // Texturas para el gatito triste
    private Texture gatitoOjosAbiertosTexture;
    private Texture gatitoOjosCerradosTexture;

    // Texturas para el perrito cuando está seleccionado
    private Texture perritoSeleccionadoAbiertoTexture;
    private Texture perritoSeleccionadoCerradoTexture;

    // Texturas para el gatito cuando está seleccionado
    private Texture gatitoSeleccionadoAbiertoTexture;
    private Texture gatitoSeleccionadoCerradoTexture;

    // Animaciones del perrito (normal y seleccionado)
    private Animation<TextureRegion> perritoAnimacion;
    private Animation<TextureRegion> perritoSeleccionadoAnimacion;

    // Animaciones del gatito (normal y seleccionado)
    private Animation<TextureRegion> gatitoAnimacion;
    private Animation<TextureRegion> gatitoSeleccionadoAnimacion;

    private boolean isSelectedPerrito;  // Estado de selección del perrito
    private boolean isSelectedGatito;  // Estado de selección del gatito

    private float stateTime;

    private Image boton1;  // Botón para el perrito
    private Image boton2;  // Botón para el gatito

    @Override
    public void show() {
        super.show();

        // Configurar el stage y el fondo
        stage = new Stage(new StretchViewport(800, 600));  // Usar StretchViewport para mantener la relación de aspecto
        fondoTexture = new Texture(Gdx.files.internal("zona_de_adopcion.png"));
        fondoImage = new Image(fondoTexture);
        fondoImage.setFillParent(true);  // Hace que la imagen de fondo llene el stage

        // Cargar las texturas del perrito
        perritoOjosAbiertosTexture = new Texture(Gdx.files.internal("perro/perrito_triste_despierto.png"));
        perritoOjosCerradosTexture = new Texture(Gdx.files.internal("perro/perrito_triste_durmiendo.png"));

        // Cargar las texturas del gatito
        gatitoOjosAbiertosTexture = new Texture(Gdx.files.internal("gato/gato_triste_despierto.png"));
        gatitoOjosCerradosTexture = new Texture(Gdx.files.internal("gato/gato_triste_durmiendo.png"));

        // Cargar las texturas del perrito cuando está seleccionado
        perritoSeleccionadoAbiertoTexture = new Texture(Gdx.files.internal("perro/perrito_triste_seleccionado.png"));
        perritoSeleccionadoCerradoTexture = new Texture(Gdx.files.internal("perro/perrito_triste_durmiendo_seleccionado.png"));

        // Cargar las texturas del gatito cuando está seleccionado
        gatitoSeleccionadoAbiertoTexture = new Texture(Gdx.files.internal("gato/gato_triste_despierto_seleccionado.png"));
        gatitoSeleccionadoCerradoTexture = new Texture(Gdx.files.internal("gato/gato_triste_durmiendo_seleccionado.png"));

        // Crear la animación del perrito (abre y cierra los ojos)
        TextureRegion[] perritoFrames = new TextureRegion[2];
        perritoFrames[0] = new TextureRegion(perritoOjosAbiertosTexture);  // Ojos abiertos
        perritoFrames[1] = new TextureRegion(perritoOjosCerradosTexture);  // Ojos cerrados

        perritoAnimacion = new Animation<TextureRegion>(0.5f, perritoFrames);  // 0.5 segundos entre cuadros

        // Crear la animación del perrito cuando está seleccionado
        TextureRegion[] perritoSeleccionadoFrames = new TextureRegion[2];
        perritoSeleccionadoFrames[0] = new TextureRegion(perritoSeleccionadoAbiertoTexture);  // Seleccionado y ojos abiertos
        perritoSeleccionadoFrames[1] = new TextureRegion(perritoSeleccionadoCerradoTexture);  // Seleccionado y ojos cerrados

        perritoSeleccionadoAnimacion = new Animation<TextureRegion>(0.5f, perritoSeleccionadoFrames);  // 0.5 segundos entre cuadros

        // Crear la animación del gatito (abre y cierra los ojos)
        TextureRegion[] gatitoFrames = new TextureRegion[2];
        gatitoFrames[0] = new TextureRegion(gatitoOjosAbiertosTexture);  // Ojos abiertos
        gatitoFrames[1] = new TextureRegion(gatitoOjosCerradosTexture);  // Ojos cerrados

        gatitoAnimacion = new Animation<TextureRegion>(0.5f, gatitoFrames);  // 0.5 segundos entre cuadros

        // Crear la animación del gatito cuando está seleccionado
        TextureRegion[] gatitoSeleccionadoFrames = new TextureRegion[2];
        gatitoSeleccionadoFrames[0] = new TextureRegion(gatitoSeleccionadoAbiertoTexture);  // Seleccionado y ojos abiertos
        gatitoSeleccionadoFrames[1] = new TextureRegion(gatitoSeleccionadoCerradoTexture);  // Seleccionado y ojos cerrados

        gatitoSeleccionadoAnimacion = new Animation<TextureRegion>(0.5f, gatitoSeleccionadoFrames);  // 0.5 segundos entre cuadros

        stateTime = 0f;
        isSelectedPerrito = false;  // Inicialmente, el perrito no está seleccionado
        isSelectedGatito = false;   // Inicialmente, el gatito no está seleccionado

        // Crear el botón 1 que será el perrito animado
        boton1 = new Image(perritoOjosAbiertosTexture);
        boton1.setTouchable(Touchable.enabled);  // Asegurarse de que el botón sea tocable

        // Crear el botón 2 que será el gatito animado
        boton2 = new Image(gatitoOjosAbiertosTexture);
        boton2.setTouchable(Touchable.enabled);  // Asegurarse de que el botón sea tocable

        // Añadir listeners para cambiar la imagen cuando el mouse entra y sale del área de los botones
        boton1.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isSelectedPerrito = true;  // Cambiar el estado a seleccionado cuando el mouse entra
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                isSelectedPerrito = false;  // Cambiar el estado a no seleccionado cuando el mouse sale
            }
        });

        boton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Lógica cuando el perrito es seleccionado
                principal.getJugador().getMascota().setTipo("perro");
                principal.setScreen(Pantallas.MENUINTERACCION.getPantalla());
            }
        });

        boton2.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isSelectedGatito = true;  // Cambiar el estado a seleccionado cuando el mouse entra
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                isSelectedGatito = false;  // Cambiar el estado a no seleccionado cuando el mouse sale
            }
        });

        boton2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Lógica cuando el gatito es seleccionado
                principal.getJugador().getMascota().setTipo("gato");
                principal.setScreen(Pantallas.MENUINTERACCION.getPantalla());
            }
        });

        // Usar una tabla para organizar los botones en el centro con espacio entre ellos
        Table table = new Table();
        table.setFillParent(true);  // Hace que la tabla ocupe todo el espacio disponible
        table.center();  // Centra los elementos de la tabla

        // Añadir los botones a la tabla con espacio entre ellos
        table.add(boton1).padRight(50);  // Separar los botones con 50 pixeles
        table.add(boton2).padLeft(50);

        // Añadir la tabla al stage
        stage.addActor(fondoImage);
        stage.addActor(table);

        // Establecer el stage como el input processor
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);  // Establece un fondo blanco
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Borra el buffer de color

        stateTime += delta;  // Actualizar el tiempo del estado para la animación

        // Dependiendo de si el botón del perrito está seleccionado o no, usar la animación correspondiente
        TextureRegion currentFramePerrito;
        if (isSelectedPerrito) {
            currentFramePerrito = perritoSeleccionadoAnimacion.getKeyFrame(stateTime, true);  // Animación del perrito seleccionado
        } else {
            currentFramePerrito = perritoAnimacion.getKeyFrame(stateTime, true);  // Animación del perrito normal
        }

        // Dependiendo de si el botón del gatito está seleccionado o no, usar la animación correspondiente
        TextureRegion currentFrameGatito;
        if (isSelectedGatito) {
            currentFrameGatito = gatitoSeleccionadoAnimacion.getKeyFrame(stateTime, true);  // Animación del gatito seleccionado
        } else {
            currentFrameGatito = gatitoAnimacion.getKeyFrame(stateTime, true);  // Animación del gatito normal
        }

        // Actualizar las imágenes de los botones
        boton1.setDrawable(new Image(currentFramePerrito).getDrawable());
        boton2.setDrawable(new Image(currentFrameGatito).getDrawable());
        boton1.setSize(330,250);
        boton1.setPosition(90,160);
        boton2.setSize(330,200);
        boton2.setPosition(400,140);
        // Dibuja el stage
        stage.act(delta);  // Actualiza el estado del stage
        stage.draw();
    }

    @Override
    public void dispose() {
        // Liberar recursos de texturas
        fondoTexture.dispose();
        perritoOjosAbiertosTexture.dispose();
        perritoOjosCerradosTexture.dispose();
        gatitoOjosAbiertosTexture.dispose();
        gatitoOjosCerradosTexture.dispose();
        perritoSeleccionadoAbiertoTexture.dispose();
        perritoSeleccionadoCerradoTexture.dispose();
        gatitoSeleccionadoAbiertoTexture.dispose();
        gatitoSeleccionadoCerradoTexture.dispose();
        stage.dispose();  // Liberar recursos del stage
    }
}
