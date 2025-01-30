package com.jrl.juego.minijuegos.rompecabezas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jrl.juego.BaseScreen;
import com.jrl.juego.Pantallas;
import com.jrl.juego.Principal;
import com.jrl.juego.minijuegos.PantallaGanasteMonedaScreen;
import com.jrl.juego.minijuegos.rompecabezas.PuzzleShuffler;
import com.jrl.juego.minijuegos.rompecabezas.Settings;
import com.jrl.juego.minijuegos.rompecabezas.play.EmpyPlace;
import com.jrl.juego.minijuegos.rompecabezas.play.PuzzlePiece;
import com.jrl.juego.minijuegos.rompecabezas.play.SavedInfo;

import java.util.ArrayList;

public class PlayingScreen extends BaseScreen {


    private Stage stage;
    private Texture image;
    private PuzzlePiece draggedPiece;
    private PuzzlePiece lastDraggedPiece;
    private ArrayList<PuzzlePiece> pieces;
    private long startTime;
    private Label time;
    private TextButton menuButton;
    private BitmapFont font;

    int minutes=0;

    @Override
    public void show() {
        principal.reproducirMusica(1);
        stage =  new Stage(new StretchViewport(800, 900));;
        if (Settings.getImage() == null) {
            image = new Texture("default.jpeg");
            Settings.setImage(image);
        } else {
            image = Settings.getImage();
        }
        Texture figuras=new Texture("rosado.png");

        pieces = new ArrayList<>();
        Gdx.input.setInputProcessor(stage);
        for (int i = 0; i < Settings.getPuzzleSize(); i++) {
            for (int j = 0; j < Settings.getPuzzleSize(); j++) {
                //lugares
                EmpyPlace ep = new EmpyPlace(i, j, figuras,stage);
                stage.addActor(ep);
            }
        }
        for (int i = 0; i < Settings.getPuzzleSize(); i++) {
            for (int j = 0; j < Settings.getPuzzleSize(); j++) {

                PuzzlePiece newPiece = new PuzzlePiece(i, j, image.getWidth() / Settings.getPuzzleSize(), image, this,stage);
                stage.addActor(newPiece);
                pieces.add(newPiece);


            }
        }

        new PuzzleShuffler(pieces,stage).shuffle();
        startTime = System.currentTimeMillis();

        // Cargar fuente preexportada
        font = new BitmapFont(Gdx.files.internal("fuente_arial/font-export.fnt"));
        LabelStyle ls = new LabelStyle(font, Color.WHITE);

        time = new Label("0:00:00", ls);
        time.setPosition(stage.getWidth() / 2 - time.getWidth() / 2, stage.getHeight() - time.getHeight() * .85f);
        Skin skin = new Skin(Gdx.files.internal("skin/uskin.json"));
        BitmapFont customFont = new BitmapFont(Gdx.files.internal("fuente_arial/font-export.fnt"), Gdx.files.internal("fuente_arial/font-export.png"), false);

        // Crear estilo de etiqueta con la fuente personalizada
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = customFont;
        labelStyle.fontColor = Color.WHITE;
        TextButton volverButton = new TextButton("Menú", skin);
        volverButton.getLabel().setStyle(labelStyle);
        volverButton.setSize(100,50);
        volverButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                principal.setScreen(Pantallas.MENUJUEGOS.getPantalla()); // Cambia por tu pantalla principal
            }
        });
        volverButton.setPosition(0, stage.getHeight() - volverButton.getHeight() * .85f);

        stage.addActor(time);
        stage.addActor(volverButton);


    }

    public void loadGame() {
        SavedInfo save = new SavedInfo(pieces, startTime,stage);
        //save.load(puzzle);
        startTime = save.getStartTime();

        stage.clear();

        for (int i = 0; i < Settings.getPuzzleSize(); i++) {
            for (int j = 0; j < Settings.getPuzzleSize(); j++) {
                EmpyPlace ep = new EmpyPlace(i, j, image,stage);
                stage.addActor(ep);
            }
        }

        pieces = save.getPieces();
        for (PuzzlePiece piece : pieces) {
            stage.addActor(piece);
        }

        stage.addActor(time);
        stage.addActor(menuButton);
    }

    public void save() {
        SavedInfo save = new SavedInfo(pieces, startTime,stage);
        save.save();
    }

    public void setDraggedPiece(PuzzlePiece draggedPiece) {
        this.draggedPiece = draggedPiece;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.0f, .9f, .6f, .9f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) ((millis / (1000 * 60)) % 60);
        int hours = (int) ((millis / (1000 * 60 * 60)) % 24);
        time.setText(String.format("%d:%02d:%02d", hours, minutes, seconds));

        stage.draw();
        stage.act();

        if (draggedPiece != null) {
            stage.getBatch().begin();
            draggedPiece.draw(stage.getBatch(), 1);
            stage.getBatch().end();
            lastDraggedPiece = draggedPiece;
        } else if (lastDraggedPiece != null) {
            stage.addActor(lastDraggedPiece);
            lastDraggedPiece = null;
        }
    }

    public void checkSolved() {
        boolean allSolved = true;
        for (PuzzlePiece pp : this.pieces) {
            if (!pp.isOnRightPlace()) {
                allSolved = false;
                break;
            }
        }
        if (allSolved) {
            if(minutes<=12&&Settings.getPuzzleSize()>3) {
                principal.setScreen(new PantallaGanasteMonedaScreen(principal, 40));

            } else if (minutes<=18&&Settings.getPuzzleSize()>3) {
                principal.setScreen(new PantallaGanasteMonedaScreen(principal, 30));
            }
            else if (minutes<=29&&Settings.getPuzzleSize()>3) {
                principal.setScreen(new PantallaGanasteMonedaScreen(principal, 20));
            }
            else if (minutes<=6) {
                principal.setScreen(new PantallaGanasteMonedaScreen(principal, 10));
            }
            else{
                principal.setScreen(new PantallaGanasteMonedaScreen(principal, 5));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    public Texture getImage() {
        return image;
    }
}
