package com.jrl.juego.minijuegos.rompecabezas.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.jrl.juego.minijuegos.rompecabezas.Settings;
import com.jrl.juego.minijuegos.rompecabezas.screens.PlayingScreen;

import java.io.Serializable;

public class PuzzlePiece extends Actor implements Serializable{

	private static final long serialVersionUID = 1L;
	private int boardX;
	private int boardY;
	private int textureX, textureY, pieceSize;
	private Texture texture;
	private PuzzlePiece pp;
	private boolean solvedX;
	private boolean solvedY;

	private float XpositionInmenu;
	private float YpositionInmenu;

	private float inMenuSize = 5;
    Stage stage;

	public PuzzlePiece(int x, int y, int size, Texture image, PlayingScreen pantalla, Stage stage) {
		textureX = x;
		textureY = y;
		pieceSize = size;
		texture = image;
        this.stage=stage;

		setSize(stage.getHeight() / (Settings.getPuzzleSize() + 1),
				stage.getHeight() / (Settings.getPuzzleSize() + 1));

		boardY = ((int) stage.getHeight() / (Settings.getPuzzleSize() + 1) / 2);
		boardX = ((int)stage.getWidth() - (int)stage.getHeight()) / 2
				+ boardY;

		setPosition(x * getWidth() + boardX, y * getHeight() + boardY);
		setY(stage.getHeight() - getY() - 2 * boardY);

		this.pp = this;
		addListener(new DragListener() {
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				pp.setOrigin(Gdx.input.getX(), Gdx.input.getY());
				pp.setPosition(getX() + x - getWidth() / 2, getY() + y
						- getHeight() / 2);
				setSize(stage.getHeight() / (Settings.getPuzzleSize() + 1),
						stage.getHeight() / (Settings.getPuzzleSize() + 1));
				pantalla.setDraggedPiece(pp);
			}
		});

		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				snapToGrid();
				pantalla.setDraggedPiece(null);
				pantalla.checkSolved();
				pantalla.save();
				super.clicked(event, x, y);
			}
		});
		setInMenuSize(getWidth()*.8f);
	}

	public boolean isOnRightPlace() {
		return solvedX && solvedY;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getWidth(), getHeight(), textureX
				* pieceSize, textureY * pieceSize, pieceSize, pieceSize, false,
				false);
	}

	public void snapToGrid() {
		int centerX = (int) (getWidth() / 2 + getX());
		int centerY = (int) (getHeight() / 2 + getY());
		solvedY = solvedX = false;
		if (centerX > boardX && centerX < stage.getWidth() - boardX) {
			if (centerY > boardY && centerY < stage.getHeight() - boardY) {
				snapHorizontally();
				snapVertically();
			}
		} else {
			setPosition(XpositionInmenu, YpositionInmenu);
			setSize(inMenuSize, inMenuSize);
		}

	}

	public void snapHorizontally() {
		int xCoor = 0;
		float X = getWidth() / 2 + getX() - boardX;
		while (X > 0) {
			X -= getWidth();
			xCoor++;
		}
		xCoor--;
		float snapX = xCoor * getWidth() + boardX;
		setX(snapX);

		if (textureX == xCoor)
			solvedX = true;
		else
			solvedX = false;
	}

	public void snapVertically() {
		int yCoor = 0;
		float Y = getHeight() / 2 + getY() - boardY;
		while (Y > 0) {
			Y -= getHeight();
			yCoor++;
		}
		yCoor--;
		float snapY = yCoor * getHeight() + boardY + 2;
		setY(snapY);

		if (Settings.getPuzzleSize() - textureY == yCoor + 1)
			solvedY = true;
		else
			solvedY = false;
	}

	public void setInMenuPosition(float f, float g){
		XpositionInmenu = f;
		YpositionInmenu = g;
	}

	public void setInMenuSize(float size) {
		this.inMenuSize = size;
		setX(getX() + (getWidth() - size)/2);
		setY(getY() + (getHeight() - size)/2);
		setInMenuPosition(getX(), getY());
		//setSize(size, size);
	}

	public int getTextureX() {
		return textureX;
	}

	public int getTextureY() {
		return textureY;
	}

	public int getPieceSize() {
		return pieceSize;
	}

	public int getBoardX() {
		return boardX;
	}

	public int getBoardY() {
		return boardY;
	}

	public float getInMenuSize() {
		return inMenuSize;
	}

	public float getXpositionInmenu() {
		return XpositionInmenu;
	}

	public float getYpositionInmenu() {
		return YpositionInmenu;
	}

	public void setPieceSize(int pieceSize) {
		this.pieceSize = pieceSize;
	}
	public void setBoardXY(int boardX,int boardY) {
		this.boardX = boardX;
		this.boardY = boardY;
	}

	public void setXYpositionInmenu(float xpositionInmenu,float ypositionInmenu) {
		XpositionInmenu = xpositionInmenu;
		YpositionInmenu = ypositionInmenu;
	}
}
