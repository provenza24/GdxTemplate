package com.game.core.util;

public class Level {

	private int worldNumber;
	
	private int levelNumber;
	
	private String tmxName;
		
	public Level(int worldNumber, int levelNumber, String tmxName) {
		super();
		this.worldNumber = worldNumber;
		this.levelNumber = levelNumber;
		this.tmxName = tmxName;		
	}

	public int getWorldNumber() {
		return worldNumber;
	}

	public void setWorldNumber(int worldNumber) {
		this.worldNumber = worldNumber;
	}

	public int getLevelNumber() {
		return levelNumber;
	}

	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}

	public String getTmxName() {
		return tmxName;
	}

	public void setTmxName(String tmxName) {
		this.tmxName = tmxName;
	}

}
