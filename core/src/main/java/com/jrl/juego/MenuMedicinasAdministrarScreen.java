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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jrl.juego.entidades.Alimento;
import com.jrl.juego.entidades.Animal;
import com.jrl.juego.entidades.Jugador;
import com.jrl.juego.entidades.Medicina;

public class MenuMedicinasAdministrarScreen extends BaseScreen {

    private Stage stage;
    private Table mainTable;
    private Table gridTable;
    private ScrollPane scrollPane;
    private Skin skin;
    private String tipoanimal;
    private Label monedasLabel;
    private BitmapFont customFont;
    private Label textLabel; // Para el mensaje
    private Stack textStack;
    @Override
    public void show() {
        super.show();
       principal.reproducirMusica(2);
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
        // Crear el Stack para el texto y el fondo
        Stack textStack = new Stack();
        Texture textBgTexture = new Texture(Gdx.files.internal("fondo_texto.jpeg"));
        Image textBackground = new Image(textBgTexture);
        Label textLabel = new Label("", labelStyle);
        textLabel.setAlignment(Align.center);
        textStack.setSize(300,200);
        textBackground.setSize(300,200);
// Agregar fondo y texto al Stack
        textStack.add(textBackground);
        textStack.add(textLabel);

// Configurar el Stack para estar centrado
        textStack.setVisible(false); // Ocultar por defecto
        // Crear el Label superior con el título "Minijuegos"
        Label titleLabel = new Label("Medicina elige 1 para administrar", labelStyle);
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

        mainTable.row();

        // Crear la grilla
        gridTable = new Table();
        gridTable.defaults().pad(10).height(250);

        // Llenar la grilla con imágenes y nombres
        for(Medicina medicina :principal.getMedicinastienda()){
            agregarElementoAGrilla(medicina.getImagen(), medicina.getNombre()+"("+medicina.getPrecio()+"$)", labelStyle,new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (principal.getJugador().getMonedas() - medicina.getPrecio() < 0) {
                        textStack.setSize(300,200);
                        textBackground.setSize(300,200);
                        textLabel.setText("¡No te alcanza!"); // Actualizar el texto
                        textStack.setVisible(true); // Mostrar el texto con su fondo

                        // Ocultar después de 2 segundos
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                textStack.setVisible(false); // Ocultar el Stack
                            }
                        }, 2); // Tiempo en segundos
                    }
                    else{
                        Jugador jugador=principal.getJugador();
                        if(jugador.getMedicinas().containsKey(medicina.getNombre())){
                            textStack.setSize(500,200);
                            textBackground.setSize(500,200);
                            textLabel.setText("¡No esta permitido administrarle\nmas de una vacuna!"); // Actualizar el texto
                            textStack.setVisible(true); // Mostrar el texto con su fondo

                            // Ocultar después de 2 segundos
                            Timer.schedule(new Timer.Task() {
                                @Override
                                public void run() {
                                    textStack.setVisible(false); // Ocultar el Stack
                                }
                            }, 2); // Tiempo en segundos
                        }else {
                            jugador.setMonedas(jugador.getMonedas() - medicina.getPrecio());
                            monedasLabel.setText("Monedas:" + principal.getJugador().getMonedas());
                            jugador.getMedicinas().put(medicina.getNombre(),medicina);
                            Animal mascota=principal.getJugador().getMascota();
                            if(medicina.getSalud()+mascota.getSalud()<=100) {
                                mascota.setSalud(medicina.getSalud() + mascota.getSalud());
                            }else{
                                mascota.setSalud(100);
                            }
                            if(medicina.getEnergia()+mascota.getEnergia()<=100){
                                mascota.setEnergia(medicina.getEnergia()+mascota.getEnergia());
                            }
                            else {
                                mascota.setEnergia(100);
                            }
                            principal.setScreen(Pantallas.VACUNACIONMASCOTA.getPantalla());
                        }

                    }
                }
            },medicina);

        }

        // Crear el ScrollPane para la grilla
        scrollPane = new ScrollPane(gridTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollBarPositions(false, true);

        mainTable.add(scrollPane).expand().fill().colspan(2);

        // Añadir la tabla principal al escenario
        stage.addActor(mainTable);
        stage.addActor(textStack);
        textStack.setPosition((stage.getWidth() - textStack.getWidth()) / 2, (stage.getHeight() - textStack.getHeight()) / 2);

    }

    private void agregarElementoAGrilla(String imagePath, String nombre, Label.LabelStyle labelStyle,ClickListener click,Medicina medicina) {
        // Crear una tabla interna para la imagen y el nombre
        Table itemTable = new Table();
        TextButton descripcionButton = new TextButton("Descripción", skin);
        descripcionButton.getLabel().setFontScale(1f); // Escala del texto del botón
        descripcionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((PantallaCaracteristicas)Pantallas.CARACTERISTICAS_OBJETO.getPantalla()).setMedicina(medicina);
                principal.setScreen(Pantallas.CARACTERISTICAS_OBJETO.getPantalla());
            }
        });
        // Crear la imagen
        Image image = new Image(new Texture(Gdx.files.internal(imagePath)));
        image.addListener(click);

        // Crear el Label para el nombre
        Label nameLabel = new Label(nombre, labelStyle);
        nameLabel.setFontScale(1.2f);

        // Agregar la imagen y el nombre a la tabla interna
        itemTable.add(descripcionButton).padBottom(5).row(); // Agregar el botón "Descripción" arriba
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
        Gdx.gl.glClearColor(0.240f, 0.200f, 0.079f, 1);
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
