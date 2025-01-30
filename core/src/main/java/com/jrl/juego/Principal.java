package com.jrl.juego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jrl.juego.entidades.Alimento;
import com.jrl.juego.entidades.Jugador;
import com.jrl.juego.entidades.Medicina;
import com.jrl.juego.guardado.GuardadoObjeto;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

import java.io.IOException;
import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Principal extends Game {
    private SpriteBatch batch;
    private Texture image;
    private ArrayList<Music> musicas;
    private Alimento seleccionado;
    private Jugador jugador;
    private ArrayList<Alimento>alimentostienda;
    private ArrayList<Medicina>medicinastienda;


    public Principal(){
        System.out.println("segundo"+System.getProperty("java.library.path"));
        alimentostienda=new ArrayList<>();

        alimentostienda.add(new Alimento("croquetas","Alimento poco saludable con\nquimicos recomendable dar poco",1,6, "alimentos/croquetas.png", 100,1));
        alimentostienda.add(new Alimento("sopita","Alimento saludable que deacuerdo a como se prepare\n es bueno ",15,20, "alimentos/sopita.png", 110,1));
        alimentostienda.add(new Alimento("huesito","Alimento no muy recomendable\npara la mascota ya sea gato o perro\nya que llega a astillar su boca",2,2, "alimentos/huesito.png", 10,1));
        medicinastienda=new ArrayList<>();
        medicinastienda.add(new Medicina("Vacuna Antiparasitaria","Vacuna que reduce la posibilidad de\n que la mascota llegue a tener parasitos",40,12,"vacunas/vacuna_anti_parasitaria.png",50));
        medicinastienda.add(new Medicina("Vacuna Pentabalente","Vacuna en la que se incluye proteccion\ncontra 5 enfermedades para la mascota",20,12,"vacunas/vacuna_pentavalente.png",80));
        medicinastienda.add(new Medicina("Vacuna Antirabica","Vacuna que proteje a la mascota contra\n el virus de la rabia",50,12,"vacunas/vacuna_anti_rabica.png",30));



        //inicializacion principal



    }
    @Override
    public void create() {
        for(Pantallas pantalla:Pantallas.values())
        {
            pantalla.setPrincipal(this);
        }

        GuardadoObjeto<Jugador>  guardado=new GuardadoObjeto<>(Jugador.class);

        jugador=guardado.cargar(Jugador.class);
        if (jugador == null) {
            jugador=new Jugador();

        }


        /*
        String emotionName = "Tristeza"; // Cambiar según la emoción deseada
        String dataPath = "./Data"; // Cambia a la ruta donde hayas almacenado Data
        //EmotionCapture emotionCapture = new EmotionCapture(dataPath);

        // Captura de emociones durante 10 segundos para cada emoción
        String[] emotions = {"Tristeza", "Enojo", "Felicidad", "Sorpresa"};
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
        musicas=new ArrayList<>();
        Music musicaInteraccion,musicaMenuJuegos,musicatienda;
            musicaInteraccion= Gdx.audio.newMusic(Gdx.files.internal("musicaDeFondo.mp3")); // Asegúrate de tener este archivo en tu carpeta assets
        musicaInteraccion.play();
        musicaInteraccion.setLooping(true);
        musicas.add(musicaInteraccion);
        musicaMenuJuegos = Gdx.audio.newMusic(Gdx.files.internal("music/musicaMenuJuegos.mp3")); // Asegúrate de tener este archivo en tu carpeta assets
        musicaMenuJuegos.setLooping(true);
        musicas.add(musicaMenuJuegos);
        musicatienda = Gdx.audio.newMusic(Gdx.files.internal("music/sonido_tienda.mp3")); // Asegúrate de tener este archivo en tu carpeta assets
        musicatienda.setLooping(true);
        musicas.add(musicatienda);
        FileHandle fileHandle = Gdx.files.local("tipo.txt");
        String tipo=readTextFile("tipo.txt");
        if(!fileHandle.exists()) {
            setScreen(Pantallas.MENUJUEGO.getPantalla());
        }
        else{
            this.jugador.getMascota().setTipo(tipo);

            setScreen(Pantallas.MENUINTERACCION.getPantalla());

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





    public void setMusicas(ArrayList<Music> musicas) {
        this.musicas = musicas;
    }
    public void reproducirMusica(int n){
        for(Music musica:musicas){
            if(musica.isPlaying()){
            musica.stop();
            }
        }
        musicas.get(n).play();
    }
     public void pausarMusica(){
        for(Music musica:musicas){
            if(musica.isPlaying()){
            musica.stop();
            }
        }
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Alimento getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Alimento seleccionado) {
        this.seleccionado = seleccionado;
    }
    public ArrayList<Alimento> getAlimentostienda() {
        return alimentostienda;
    }

    public void setAlimentostienda(ArrayList<Alimento> alimentostienda) {
        this.alimentostienda = alimentostienda;
    }

    public ArrayList<Medicina> getMedicinastienda() {
        return medicinastienda;
    }

    public void setMedicinastienda(ArrayList<Medicina> medicinastienda) {
        this.medicinastienda = medicinastienda;
    }

}
