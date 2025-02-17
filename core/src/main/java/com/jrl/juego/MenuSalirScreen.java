package com.jrl.juego;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.io.IOException;

public class MenuSalirScreen extends BaseScreen {
    Skin skin;
    Stage stage;
    TextButton botonSalir, botonGuardar;
    TextField contrasenaField;
    boolean contrasenaGuardada = false;

    @Override
    public void show() {

        // Cargar el skin
        skin = new Skin(Gdx.files.internal("skin/uskin.json"));

        // Crear el campo de texto para ingresar la contraseña
        contrasenaField = new TextField("", skin);
        contrasenaField.setMessageText("Ingrese Clave Secreta");

        // Crear los botones de "Salir" y "Guardar Contraseña"
        botonSalir = new TextButton("Salir", skin);
        botonGuardar = new TextButton("Guardar Clave", skin);

        // Configurar el stage y el fondo
        stage = new Stage(new StretchViewport(800, 600));


        // Usar una tabla para organizar los elementos
        Table table = new Table();
        table.setFillParent(true);
        table.center();





        // Verificar si el archivo "contraseña.dsk" ya existe
        FileHandle contrasenaFile = Gdx.files.local("contraseña.dsk");
        if (!contrasenaFile.exists()) {
            table.add(contrasenaField).width(300).height(40).padBottom(20); // Añadir campo de contraseña
            table.row();
            table.add(botonGuardar).width(200).height(50).padBottom(20); // Botón para guardar la contraseña
            table.row();

            // Si no existe el archivo, habilitar el ingreso de la contraseña
            botonGuardar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    try {
                        // Guardar la contraseña en el archivo "contraseña.dsk"
                        saveTextToFile(contrasenaField.getText(), "contraseña.dsk");
                        Gdx.app.log("Contraseña", "Contraseña guardada correctamente.");
                        if(!contrasenaGuardada) {
                            table.add(botonSalir).width(200).height(50).padBottom(20); // Botón para salir
                            table.row();
                        }
                        contrasenaGuardada = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Acción para salir del juego
            botonSalir.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (contrasenaGuardada) {
                        Gdx.app.exit();
                        // Cerrar la aplicación si la contraseña está guardada
                    }
                }
            });
        } else {
            // Si el archivo ya existe, desactivar la opción de guardar
            contrasenaGuardada=true;
            Gdx.app.exit();

        }
        stage.addActor(table);


        // Establecer el input processor
        Gdx.input.setInputProcessor(stage);
    }

    private void saveTextToFile(String text, String fileName) throws IOException {
        // Guardar el texto en un archivo en el sistema local
        FileHandle fileHandle = Gdx.files.local(fileName);
            fileHandle.writeString(text, false);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0.34f,0.25f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    }
}
