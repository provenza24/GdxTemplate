package com.game.core.board;

import com.game.core.util.enums.GameMode;

public class Scoring {

	private int level;
	
	private int high;
	
	private int nbLines;
	
	private GameMode gameMode;
	
	public Scoring(int level, int high, int nbLines, GameMode gameMode) {
		super();
		this.level = level;
		this.high = high;
		this.nbLines = nbLines;
		this.gameMode = gameMode;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public int getNbLines() {
		return nbLines;
	}

	public void setNbLines(int nbLines) {
		this.nbLines = nbLines;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}
	
	public void incLines(int nbLines) {
		this.nbLines += nbLines;
	}
	
	public void incLevel() {
		this.level++;
	}
	
	
}
