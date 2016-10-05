package com.game.core.util;

public class Level {

	private int worldNumber;

	private String worldName;

	private int levelNumber;

	private String levelName;

	private String tmxName;
	
	private Level(Builder builder) {
		tmxName = builder.tmxName;
		worldNumber = builder.worldNumber;
		worldName = builder.worldName;
		levelNumber = builder.levelNumber;
		levelName = builder.levelName;
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

	public static class Builder {
		
		// Required parameters
		private String tmxName;

		// Optional parameters - initialized to default values
		private int worldNumber;

		private String worldName;

		private int levelNumber;

		private String levelName;

		public Builder(String tmxName) {
			this.tmxName = tmxName;			
		}

		public Builder worldNumber(int val) {
			worldNumber = val;
			return this;
		}
		
		public Builder worldName(String val) {
			worldName = val;
			return this;
		}
		
		public Builder levelNumber(int val) {
			levelNumber = val;
			return this;
		}

		public Builder levelName(String val) {
			levelName = val;
			return this;
		}		

		public Level build() {
			return new Level(this);
		}
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

}
