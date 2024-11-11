package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.io.DataOutputStream;
import java.io.IOException;

public class MenuJuego extends BaseScreen {
    Skin skin;
    Stage stage;
    TextButton botonStart, botonOpciones;
    Image botonSonido;
    Texture backgroundTexture, sonidoOnTexture, sonidoOffTexture;
    Image backgroundImage;
    boolean sonidoEncendido = true;
    TextField nombreJugador;  // Campo de texto para el nombre del jugador
    Music musica;  // Variable para la música

    public MenuJuego(Principal principal) {
        super(principal);

        // Cargar skin
        skin = new Skin(Gdx.files.internal("skin/uskin.json"));

        // Crear el campo de texto para el nombre del jugador
        nombreJugador = new TextField("", skin);
        nombreJugador.setMessageText("Nombre del jugador");  // Texto por defecto cuando está vacío

        // Crear los botones de "Start" y "Opciones"
        botonStart = new TextButton("Start", skin);
        botonOpciones = new TextButton("Opciones", skin);

        // Cargar las texturas para el botón de sonido (on y off)
        sonidoOnTexture = new Texture(Gdx.files.internal("boton_on_music.png"));  // Imagen de sonido activado
        sonidoOffTexture = new Texture(Gdx.files.internal("boton_off_music.png"));  // Imagen de sonido desactivado
        botonSonido = new Image(sonidoOnTexture);  // Imagen inicial es sonido activado

        // Configurar el stage y el fondo
        stage = new Stage(new StretchViewport(800, 600));  // Usar StretchViewport para mantener la relación de aspecto
        backgroundTexture = new Texture(Gdx.files.internal("libgdx.jpeg"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);  // Hace que la imagen de fondo llene el stage

        // Usar una tabla para alinear los elementos en una columna
        Table table = new Table();
        table.setFillParent(true);  // Hace que la tabla ocupe todo el espacio disponible
        table.center();  // Centra los elementos de la tabla

        // Añadir los elementos a la tabla con separación vertical
        table.add(nombreJugador).width(300).height(40).padBottom(20);  // Añadir campo de texto para el nombre del jugador
        table.row();  // Nueva fila
        table.add(botonStart).width(200).height(50).padBottom(20);  // Tamaño del botón Start
        table.row();  // Nueva fila
        table.add(botonOpciones).width(200).height(50);  // Tamaño del botón Opciones

        // Escalar el botón de sonido a un tamaño más pequeño (por ejemplo, 50x50)
        botonSonido.setSize(50, 50);  // Tamaño del botón de sonido

        // Posicionar el botón de sonido en la esquina inferior derecha con un pequeño padding
        botonSonido.setPosition(stage.getWidth() - botonSonido.getWidth() - 10, 10);  // Ajuste para que esté en la esquina

        // Añadir un ClickListener al botón de sonido para cambiar la imagen al ser presionado
        botonSonido.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar la imagen del botón de sonido y mutear/desmutear la música
                if (sonidoEncendido) {
                    botonSonido.setDrawable(new Image(sonidoOffTexture).getDrawable());  // Cambiar a sonido desactivado
                    musica.pause();  // Pausar la música
                    sonidoEncendido = false;
                } else {
                    botonSonido.setDrawable(new Image(sonidoOnTexture).getDrawable());  // Cambiar a sonido activado
                    musica.play();  // Reanudar la música
                    sonidoEncendido = true;
                }
            }
        });

        // ClickListener para el botón "Start" que abre el MenuAdopcion
        botonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    saveTextToFile(nombreJugador.getText(),"name.txt");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                FileHandle fileHandle = Gdx.files.local("tipo.txt");
                String tipo=readTextFile("tipo.txt");
                if(fileHandle.exists()){
                    principal.setScreen(new MenuInteraccion(principal,tipo));
                }else {
                    principal.setScreen(new MenuAutorizacion(principal)); // Cambia a MenuAdopcion
                }
            }
        });

        // Añadir los actores (fondo, tabla y botón de sonido) al stage
        stage.addActor(backgroundImage);  // Añadir fondo
        stage.addActor(table);  // Añadir la tabla con los botones
        stage.addActor(botonSonido);  // Añadir el botón de sonido

        // Cargar la música
        musica = Gdx.audio.newMusic(Gdx.files.internal("musicaDeFondo.mp3")); // Asegúrate de tener este archivo en tu carpeta assets
        musica.setLooping(true); // Repetir la música en bucle
        musica.play(); // Iniciar la música

        // Establecer el input processor
        Gdx.input.setInputProcessor(stage);
    }
    public void saveTextToFile(String text, String fileName) throws IOException {
        // Obtener el FileHandle del archivo en la carpeta local
        FileHandle fileHandle = Gdx.files.local(fileName);

        // Escribir el texto en el archivo
        fileHandle.writeString(text, false); // 'false' para sobrescribir el archivo si ya existe
    }
    private String readTextFile(String fileName) {
        // Obtener el FileHandle del archivo
        FileHandle fileHandle = Gdx.files.local(fileName); // Utiliza 'internal' si está en assets

        // Comprobar si el archivo existe
        if (!fileHandle.exists()) {
            return "El archivo no existe: " + fileName;
        }

        // Leer el contenido del archivo y devolverlo como String
        return fileHandle.readString(); // Devuelve el contenido del archivo como String
    }

    @Override
    public void render(float alpha) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar el stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Actualizar el viewport para que coincidan las posiciones de entrada
        stage.getViewport().update(width, height, true);

        // Volver a colocar el botón de sonido en la esquina inferior derecha después de redimensionar
        botonSonido.setPosition(stage.getWidth() - botonSonido.getWidth() - 10, 10);
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        sonidoOnTexture.dispose();
        sonidoOffTexture.dispose();
        musica.dispose(); // Liberar recursos de la música
    }
}
