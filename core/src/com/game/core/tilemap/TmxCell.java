package com.game.core.tilemap;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class TmxCell {

	public TmxCell(Cell cell, int x, int y) {
		super();
		this.cell = cell;
		this.x = x;
		this.y = y;
	}

	private Cell cell;
	
	private int x;
	
	private int y;

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
