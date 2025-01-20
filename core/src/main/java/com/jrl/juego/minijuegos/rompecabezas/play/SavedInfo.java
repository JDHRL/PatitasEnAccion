package com.jrl.juego.minijuegos.rompecabezas.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jrl.juego.minijuegos.rompecabezas.Settings;

import java.io.Serializable;
import java.util.ArrayList;

public class SavedInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2L;

	private ArrayList<PuzzlePiece> pieces;
	private long startTime;
    private Stage stage;

	public SavedInfo(ArrayList<PuzzlePiece> pieces, long startTime,Stage stage) {
		this.startTime = startTime;
		this.pieces = pieces;
        this.stage=stage;
	}

	public ArrayList<PuzzlePiece> getPieces() {
		return pieces;
	}

	public long getStartTime() {
		return startTime;
	}

	public void save() {
		Preferences prefs = Gdx.app.getPreferences("saved");
		prefs.putLong("startTime", startTime);
		prefs.putInteger("size", pieces.size());
		prefs.putInteger("puzzleSize", Settings.getPuzzleSize());
		int i = 0;
		for (PuzzlePiece piece : pieces) {
			prefs.putFloat("piece" + i + "X", piece.getX());
			prefs.putFloat("piece" + i + "Y", piece.getY());
			prefs.putFloat("piece" + i + "W", piece.getWidth());
			prefs.putFloat("piece" + i + "H", piece.getHeight());

			prefs.putInteger("piece" + i + "TX", piece.getTextureX());
			prefs.putInteger("piece" + i + "TY", piece.getTextureY());
			prefs.putInteger("piece" + i + "TS", piece.getPieceSize());

			prefs.putInteger("piece" + i + "BX", piece.getBoardX());
			prefs.putInteger("piece" + i + "BY", piece.getBoardY());

			prefs.putFloat("piece" + i + "Xmenu", piece.getXpositionInmenu());
			prefs.putFloat("piece" + i + "Ymenu", piece.getYpositionInmenu());
		i++;
		}
		prefs.flush();
	}



}
