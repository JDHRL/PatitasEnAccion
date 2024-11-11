package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MenuAutorizacion extends BaseScreen {
    private Stage stage;
    private Skin skin;

    public MenuAutorizacion(Principal principal) {
        super(principal);

        stage = new Stage(new StretchViewport(800, 600));
        skin = new Skin(Gdx.files.internal("skin/uskin.json"));

        // Crear una tabla para centrar el contenido
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Crear la etiqueta de autorización con ajuste de texto
        Label autorizacionLabel = new Label("AUTORIZAS EL USO DE TU CAMARA PARA PODER JUGAR ESTE JUEGO", skin);
        autorizacionLabel.setWrap(true); // Permitir ajuste de texto
        autorizacionLabel.setFontScale(2.0f); // Ajustar tamaño de fuente
        autorizacionLabel.setAlignment(1); // Centrar el texto

        // Crear los botones
        Button aceptarButton = new Button(skin);
        Label aceptar = new Label("SI ACEPTO", skin);
        aceptar.setFontScale(2.0f);
        aceptarButton.add(aceptar);

        Button noAceptarButton = new Button(skin);
        Label noaceptar = new Label("NO ACEPTO", skin);
        noaceptar.setFontScale(2.0f);
        noAceptarButton.add(noaceptar);

        // Agregar listeners a los botones
        aceptarButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Lógica para iniciar el juego
                principal.setScreen(new Captura(principal)); // Ir a la pantalla de captura
            }
        });

        noAceptarButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Lógica para manejar el rechazo
                Gdx.app.exit(); // Cerrar la aplicación o mostrar un mensaje
            }
        });

        // Añadir todo a la tabla
        table.add(autorizacionLabel).padBottom(20).expandX().fillX(); // Añadir etiqueta y ajustar
        table.row(); // Nueva fila
        table.add(aceptarButton).padBottom(10); // Añadir botón "SI ACEPTO"
        table.row(); // Nueva fila
        table.add(noAceptarButton); // Añadir botón "NO ACEPTO"

        // Añadir la tabla al stage
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage); // Establecer el procesador de entrada
    }

    @Override
    public void render(float delta) {
        // Establecer el color de fondo a amarillo (RGBA)
        Gdx.gl.glClearColor(1.0f, 1.0f, 0.0f, 1.0f); // RGB para amarillo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Limpiar la pantalla
        stage.act(); // Actualizar el stage
        stage.draw(); // Dibujar el stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Ajustar la vista del stage
    }

    @Override
    public void dispose() {
        stage.dispose(); // Liberar recursos del stage
        skin.dispose(); // Liberar recursos del skin
    }
}

