package com.jrl.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jrl.juego.minijuegos.BuscaRatonPelota.PantallaJuego;
import com.jrl.juego.minijuegos.rompecabezas.screens.NivelesRompeCabezas;

public class MenuJuegosScreen extends BaseScreen {

    private Stage stage;
    private Table mainTable;
    private Table gridTable;
    private ScrollPane scrollPane;
    private Skin skin;
    private String tipoanimal;
    private Label monedasLabel; // Para mostrar las monedas
    private BitmapFont customFont;
    @Override
    public void show() {
        super.show();
        principal.reproducirMusica(1);
        this.tipoanimal = principal.getJugador().getMascota().getTipo();
        stage = new Stage(new StretchViewport(800, 600));
        Gdx.input.setInputProcessor(stage);

        // Cargar la skin para UI
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        customFont = new BitmapFont(Gdx.files.internal("fuente_arial/fuente_con_borde.fnt"), Gdx.files.internal("fuente_arial/fuente_con_borde.png"), false);

        // Crear estilo de etiqueta con la fuente personalizada
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = customFont;
        labelStyle.fontColor = Color.WHITE;
        // Crear la tabla principal
        mainTable = new Table();
        mainTable.setFillParent(true);

        // Crear el Label superior con el título "Minijuegos"
        Label titleLabel = new Label("Minijuegos", labelStyle);
        titleLabel.setFontScale(2);

        // Crear el Label de monedas que se actualizará
        monedasLabel = new Label("Monedas:"+principal.getJugador().getMonedas(), labelStyle); // Valor inicial "0"
        monedasLabel.setFontScale(1.5f);

        // Agregar los labels a la tabla
        mainTable.add(titleLabel).expandX().left().padLeft(20); // Título a la izquierda
        mainTable.add(monedasLabel).expandX().right().padRight(20); // Monedas a la derecha
        mainTable.row();

        // Botón en la esquina superior derecha
        Texture buttonTexture = new Texture(Gdx.files.internal("imagen_boton_casa.png"));
        Button button = new Button(new TextureRegionDrawable(buttonTexture));
        button.setSize(50, 50);

        button.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                principal.getJugador().getMascota().setTipo(tipoanimal);
                principal.setScreen(Pantallas.MENUINTERACCION.getPantalla());
                return true;
            }
            return false;
        });

        // Colocar el botón en la esquina superior derecha
        mainTable.add(button).padTop(20).padRight(20).size(50, 50).right().top();
        mainTable.row();

        // Crear la grilla
        gridTable = new Table();
        gridTable.defaults().pad(10).height(250); // Espacio y tamaño predeterminado

        // Llenar la grilla con imágenes de ejemplo
        Image juegoBusqueda = new Image(new Texture(Gdx.files.internal("raton_pelota.jpeg")));
        juegoBusqueda.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                principal.getJugador().getMascota().setTipo(tipoanimal);
                principal.setScreen(new PantallaJuego(principal));

            }
        });
        gridTable.add(juegoBusqueda);
        Image juegoRompecabezas = new Image(new Texture(Gdx.files.internal("rompecabezas.jpeg")));
        juegoRompecabezas.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                principal.setScreen(Pantallas.JUEGOROMPECABEZAS.getPantalla());

            }
        });
        gridTable.add(juegoRompecabezas);

        if (gridTable.getColumns() % 3 == 0) {
            gridTable.row(); // Nueva fila después de 3 elementos
        }

        // Crear el ScrollPane para la grilla
        scrollPane = new ScrollPane(gridTable, skin);
        scrollPane.setScrollingDisabled(true, false); // Solo desplazamiento vertical
        scrollPane.setForceScroll(false, true); // Fuerza siempre el scroll vertical
        scrollPane.setScrollBarPositions(false, true); // Barra de desplazamiento siempre visible

        mainTable.add(scrollPane).expand().fill().colspan(2);

        // Añadir la tabla principal al escenario
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


}
