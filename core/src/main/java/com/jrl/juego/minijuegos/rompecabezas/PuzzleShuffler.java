package com.jrl.juego.minijuegos.rompecabezas;

import com.badlogic.gdx.Gdx;
import com.jrl.juego.minijuegos.rompecabezas.play.PuzzlePiece;

import java.util.ArrayList;
import java.util.Collections;

public class PuzzleShuffler {
	private ArrayList<PuzzlePiece> pieces;
	private float xOfset = 1;

	public PuzzleShuffler(ArrayList<PuzzlePiece> pieces) {
		this.pieces = pieces;
	}

	public void shuffle() {

		Collections.shuffle(pieces);
		int unShuffledPieces = pieces.size();

		int columnsCount = unShuffledPieces / Settings.getPuzzleSize();
		int currentColumn = 0;
		int piecesPut = 0;
		boolean putPieceDown = false;


		for (PuzzlePiece piece : pieces) {
			unShuffledPieces--;
			piecesPut++;

			if (!putPieceDown) {
				piece.setPosition(xOfset + currentColumn * piece.getWidth(),
						Gdx.graphics.getHeight() - Gdx.graphics.getHeight()
								/ Settings.getPuzzleSize() * piecesPut);
				piece.setInMenuPosition(piece.getX(), piece.getY());
				if (piecesPut >= Settings.getPuzzleSize()) {
					currentColumn++;
					piecesPut = 0;
					if (currentColumn >= columnsCount / 2) {
						xOfset = Gdx.graphics.getWidth() - columnsCount * piece.getWidth();
					}
					if (currentColumn >= columnsCount) {
						putPieceDown = true;
					}
				}
				piece.setInMenuSize(piece.getWidth()*.8f);
				piece.setSize(piece.getWidth()*.8f, piece.getWidth()*.8f);
			} else {
				//ukladame spodny rad
			}

		}

	}

}
