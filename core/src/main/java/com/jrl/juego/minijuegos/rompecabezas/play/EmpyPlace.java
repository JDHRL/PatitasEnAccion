package com.jrl.juego.minijuegos.rompecabezas.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jrl.juego.minijuegos.rompecabezas.Settings;

public class EmpyPlace extends Actor {

	private int boardX;
	private int boardY;
	private Texture texture;
	private float scretch = .9f;

	public EmpyPlace(int x, int y, Texture image, Stage stage) {
		texture = image;
		setSize(stage.getHeight() / (Settings.getPuzzleSize() + 1),
				stage.getHeight() / (Settings.getPuzzleSize() + 1));

		boardY = ((int)stage.getHeight() / (Settings.getPuzzleSize() + 1) / 2);
		boardX = (int)(stage.getWidth() - stage.getHeight()) / 2
				+ boardY;

		setPosition(x * getWidth() + boardX, y * getHeight() + boardY);
		setY(stage.getHeight() - getY() - 2 * boardY);

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX() + getWidth() * ((1 - scretch) / 2), getY()
				/*+ getHeight() * ((1 - scretch) / 4)*/, getWidth() * scretch,
				getHeight() * scretch);
	}

}
