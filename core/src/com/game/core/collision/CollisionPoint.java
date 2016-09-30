package com.game.core.collision;

import com.badlogic.gdx.math.Vector2;
import com.game.core.tilemap.TmxCell;

public class CollisionPoint {

	private Vector2 point;
	
	private TmxCell cell;
	
	public CollisionPoint(Vector2 point, TmxCell cell) {
		this.point = point;
		this.cell = cell;
		
	}

	public Vector2 getPoint() {
		return point;
	}

	public void setPoint(Vector2 point) {
		this.point = point;
	}

	public TmxCell getCell() {
		return cell;
	}

	public void setCell(TmxCell cell) {
		this.cell = cell;
	}

	
}
