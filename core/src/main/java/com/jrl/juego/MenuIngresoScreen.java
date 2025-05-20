package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MenuIngresoScreen extends BaseScreen {
    private Skin skin;
    private Stage stage;
    private TextField contrasenaField;
    private TextButton botonIngresar, botonElegirMascota;
    private Label mensajeError;

    @Override
    public void show() {
        FileHandle contrasenaFile = Gdx.files.local("contraseña.dsk");
        if (!contrasenaFile.exists()) {
            Gdx.app.exit();
        }
        skin = new Skin(Gdx.files.internal("skin/uskin.json"));
        stage = new Stage(new StretchViewport(800, 600));
        Gdx.input.setInputProcessor(stage);

        contrasenaField = new TextField("", skin);
        contrasenaField.setMessageText("Ingrese Clave Secreta");

        botonIngresar = new TextButton("Ingresar", skin);
        botonElegirMascota = new TextButton("Elegir Mascota", skin);

        mensajeError = new Label("", skin);
        mensajeError.setColor(1, 0, 0, 1); // Rojo

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(contrasenaField).width(300).height(40).padBottom(20);
        table.row();
        table.add(botonIngresar).width(200).height(50).padBottom(20);
        table.row();
        table.add(mensajeError).padBottom(20);
        table.row();
        table.add(botonElegirMascota).width(200).height(50).padBottom(20);

        botonIngresar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                verificarClave();
            }
        });

        botonElegirMascota.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar a la pantalla de elección de mascota
                FileHandle contrasenaFile = Gdx.files.local("contraseña.dsk");
                if (contrasenaFile.exists()) {
                    String claveGuardada = contrasenaFile.readString().trim();
                    String claveIngresada = contrasenaField.getText().trim();
                    if (claveIngresada.equals(claveGuardada)) {
                        principal.setScreen(Pantallas.MENUADOPCION.getPantalla());
                    } else {
                        mensajeError.setText("Clave incorrecta");
                    }
                } else {
                    mensajeError.setText("No hay clave guardada");
                    Gdx.app.exit();
                }
            }
        });

        stage.addActor(table);
    }

    private void verificarClave() {
        FileHandle contrasenaFile = Gdx.files.local("contraseña.dsk");
        if (contrasenaFile.exists()) {
            String claveGuardada = contrasenaFile.readString().trim();
            String claveIngresada = contrasenaField.getText().trim();
            if (claveIngresada.equals(claveGuardada)) {
                principal.setScreen(Pantallas.MENUINTERACCION.getPantalla()); // Cambiar a la pantalla deseada si la
                                                                              // clave es correcta
            } else {
                mensajeError.setText("Clave incorrecta");
            }
        } else {
            mensajeError.setText("No hay clave guardada");
            Gdx.app.exit();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.34f, 0.25f, 1);
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
        skin.dispose();
    }
}
