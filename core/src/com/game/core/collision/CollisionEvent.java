package com.game.core.collision;

import java.util.ArrayList;
import java.util.List;

public class CollisionEvent {

	private List<CollisionPoint> collisionPoints;
		
	boolean collidingTop;
	
	boolean collidingBottom;
	
	boolean collidingRight;
	
	boolean collidingLeft;
		
	public CollisionEvent() {
		collisionPoints = new ArrayList<CollisionPoint>();
	}
	
	public List<CollisionPoint> getCollisionPoints() {
		return collisionPoints;
	}

	public void setCollisionPoints(List<CollisionPoint> collisionPoints) {
		this.collisionPoints = collisionPoints;
	}

	public boolean isCollidingTop() {
		return collidingTop;
	}

	public void setCollidingTop(boolean collidingTop) {
		this.collidingTop = collidingTop;
	}

	public void reinitCollisionPoints() {
		collisionPoints = new ArrayList<CollisionPoint>();
	}

	public boolean isCollidingBottom() {
		return collidingBottom;
	}

	public void setCollidingBottom(boolean collidingBottom) {
		this.collidingBottom = collidingBottom;
	}

	public boolean isCollidingRight() {
		return collidingRight;
	}

	public void setCollidingRight(boolean collidingRight) {
		this.collidingRight = collidingRight;
	}

	public boolean isCollidingLeft() {
		return collidingLeft;
	}

	public void setCollidingLeft(boolean collidingLeft) {
		this.collidingLeft = collidingLeft;
	}



}
