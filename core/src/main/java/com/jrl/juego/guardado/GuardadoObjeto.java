package com.jrl.juego.guardado;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.jrl.juego.entidades.Alimento;
import com.jrl.juego.entidades.Animal;
import com.jrl.juego.entidades.Jugador;
import com.jrl.juego.entidades.Medicina;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class GuardadoObjeto<T> {

    public GuardadoObjeto(Class<T> clase){
        this.FILE_PATH= clase.getSimpleName()+".json";
    }
    private  String FILE_PATH ; // Archivo donde se guardará


    public void guardar(T jugador) throws IOException {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json); // Formato legible
        registrarClases(json); // Registrar clases relacionadas
        String jsonString = json.toJson(jugador);

        // Guardar utilizando LibGDX
        FileHandle file = Gdx.files.local(FILE_PATH); // Cambia a `Gdx.files.external` si necesitas almacenamiento externo
        try {
            file.writeString(jsonString, false); // `false` para sobrescribir el archivo
        } catch (Exception e) {
            e.printStackTrace(); // Registrar el error
        }
    }


    public T cargar(Class<T> clase) {
        Json json = new Json();
        registrarClases(json);

        // Usamos FileHandle para leer el archivo
        FileHandle file = Gdx.files.local(FILE_PATH); // Cambiar a external si es necesario
        if (file.exists()) {
            String jsonString = file.readString(); // Leer el contenido como cadena
            return json.fromJson(clase, jsonString); // Convertir JSON a objeto
        } else {
            System.out.println("El archivo no existe");
            return null; // Retornar null si el archivo no existe
        }
    }

    private static void registrarClases(Json json) {
        json.addClassTag("Jugador", Jugador.class);
        json.addClassTag("Alimento", Alimento.class);
        json.addClassTag("Medicina", Medicina.class);
        json.addClassTag("Animal", Animal.class);
    }
}
