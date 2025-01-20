package com.jrl.juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jrl.juego.entidades.Jugador;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Principal extends Game {
    private SpriteBatch batch;
    private Texture image;
    private Music musica;
    private String tipo;
    private Jugador jugador;
    public Principal(){
        jugador=new Jugador();
    }
    @Override
    public void create() {
        String emotionName = "Tristeza"; // Cambiar según la emoción deseada
        String dataPath = "./Data"; // Cambia a la ruta donde hayas almacenado Data
        //EmotionCapture emotionCapture = new EmotionCapture(dataPath);

        // Captura de emociones durante 10 segundos para cada emoción
        /*String[] emotions = {"Tristeza", "Enojo", "Felicidad", "Sorpresa"};
        int duration = 10; // Duración de captura en segundos

        for (String emotion : emotions) {
            System.out.println("Capturando emociones de: " + emotion);
            Thread hilo=new Thread() {
                @Override
                public void run() {
                    emotionCapture.captureEmotions("Tristeza");
                }
            };
            hilo.start();

        }
*/
        musica = Gdx.audio.newMusic(Gdx.files.internal("musicaDeFondo.mp3")); // Asegúrate de tener este archivo en tu carpeta assets
        musica.play();
        musica.setLooping(true);
        FileHandle fileHandle = Gdx.files.local("tipo.txt");
        String tipo=readTextFile("tipo.txt");
        if(!fileHandle.exists()) {
            setScreen(new MenuJuegoScreen(this));
        }
        else{
            this.setTipo(tipo);
            setScreen(new MenuInteraccion(this));
        }

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

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public Music getMusica() {
        return musica;
    }

    public void setMusica(Music musica) {
        this.musica = musica;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Jugador getJugador() {
        return jugador;
    }
}
