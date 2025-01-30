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
import com.jrl.juego.entidades.Alimento;

public class MenuAlimentosAlimentarScreen extends BaseScreen {

    private Stage stage;
    private Table mainTable;
    private Table gridTable;
    private ScrollPane scrollPane;
    private Skin skin;
    private String tipoanimal;
    private Label monedasLabel;
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
        Label titleLabel = new Label("Alimentos elige 1 para alimentar", labelStyle);
        titleLabel.setFontScale(2);

        // Crear el Label de monedas que se actualizará
        monedasLabel = new Label("Monedas:" + principal.getJugador().getMonedas(), labelStyle);
        monedasLabel.setFontScale(1.5f);

        // Agregar los labels a la tabla
        mainTable.add(titleLabel).expandX().left().padLeft(20);
        mainTable.add(monedasLabel).expandX().right().padRight(20);
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

        mainTable.add(button).padTop(10).padRight(20).size(50, 50).right().top();
        // Nuevo botón al lado del botón casa
        Texture textura_boton_compras = new Texture(Gdx.files.internal("sesta_tienda.png"));
        Button boton_compras = new Button(new TextureRegionDrawable(textura_boton_compras));
        boton_compras.setSize(50, 50);

        boton_compras.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                //pantalla para comprar objetos
                principal.setScreen(Pantallas.MENUCOMPRAALIMENTOS.getPantalla());
                return true;
            }
            return false;
        });
        mainTable.add(boton_compras).size(50,50);
        mainTable.row();

        // Crear la grilla
        gridTable = new Table();
        gridTable.defaults().pad(10).height(250);
        //principal.getJugador().getAlimentos().add(new Alimento("Huesito cortado",0,0,0,0,"alimentos/huesito_cortado.png",false));
        for(Alimento alimento :principal.getJugador().getAlimentos().values()){
            if(alimento.getCantidad()==0){
                principal.getJugador().getAlimentos().remove(alimento.getNombre());
            }
            else {
                agregarElementoAGrilla(alimento.getImagen(), alimento.getNombre() + "(" + alimento.getCantidad() + ")", labelStyle, new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        principal.setSeleccionado(alimento);
                        principal.setScreen(Pantallas.MENUINTERACCION.getPantalla());
                    }
                });
            }

        }

        // Crear el ScrollPane para la grilla
        scrollPane = new ScrollPane(gridTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollBarPositions(false, true);

        mainTable.add(scrollPane).expand().fill().colspan(2);

        // Añadir la tabla principal al escenario
        stage.addActor(mainTable);
    }

    private void agregarElementoAGrilla(String imagePath, String nombre, Label.LabelStyle labelStyle,ClickListener click) {
        // Crear una tabla interna para la imagen y el nombre
        Table itemTable = new Table();

        // Crear la imagen
        Image image = new Image(new Texture(Gdx.files.internal(imagePath)));
        image.addListener(click);

        // Crear el Label para el nombre
        Label nameLabel = new Label(nombre, labelStyle);
        nameLabel.setFontScale(1.2f);

        // Agregar la imagen y el nombre a la tabla interna
        itemTable.add(image).size(150, 150).row();
        itemTable.add(nameLabel).padTop(5);

        // Agregar la tabla interna a la grilla
        gridTable.add(itemTable);
        // Nueva fila después de 3 elementos
        if (gridTable.getColumns()+1 % 3 == 0) {
            gridTable.row();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.240f, 0.210f, 0.079f, 1);
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
