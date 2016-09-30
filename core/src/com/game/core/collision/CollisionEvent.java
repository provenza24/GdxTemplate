package com.game.core.collision;

import java.util.ArrayList;
import java.util.List;

public class CollisionEvent {

	private List<CollisionPoint> collisionPoints;
	
	boolean collidingBottomLeft;

	boolean collidingBottomRight;
	
	boolean collidingTopLeft;
	
	boolean collidingTopRight;
	
	boolean collidingMiddleRight;
	
	boolean collidingMiddleLeft;
	
	boolean collidingMiddleTop;
		
	public CollisionEvent() {
		collisionPoints = new ArrayList<CollisionPoint>();
	}
	
	public List<CollisionPoint> getCollisionPoints() {
		return collisionPoints;
	}

	public void setCollisionPoints(List<CollisionPoint> collisionPoints) {
		this.collisionPoints = collisionPoints;
	}
	
	public void reinitCollisionPoints() {
		this.collisionPoints = new ArrayList<CollisionPoint>();
	}

	public boolean isCollidingBottomLeft() {
		return collidingBottomLeft;
	}

	public void setCollidingBottomLeft(boolean collidingBottomLeft) {
		this.collidingBottomLeft = collidingBottomLeft;
	}

	public boolean isCollidingBottomRight() {
		return collidingBottomRight;
	}

	public void setCollidingBottomRight(boolean collidingBottomRight) {
		this.collidingBottomRight = collidingBottomRight;
	}

	public boolean isCollidingTopLeft() {
		return collidingTopLeft;
	}

	public void setCollidingTopLeft(boolean collidingTopLeft) {
		this.collidingTopLeft = collidingTopLeft;
	}

	public boolean isCollidingTopRight() {
		return collidingTopRight;
	}

	public void setCollidingTopRight(boolean collidingTopRight) {
		this.collidingTopRight = collidingTopRight;
	}

	// Collision TOP, BOTTOM, LEFT, RIGHT	
	public boolean isCollidingTop() {
		return collidingTopLeft || collidingTopRight;
	}

	public boolean isCollidingBottom() {
		return collidingBottomLeft || collidingBottomRight;
	}

	
	public boolean isCollidingLeft() {
		return collidingTopLeft || collidingBottomLeft;
	}

	public boolean isCollidingRight() {
		return collidingTopRight || collidingBottomRight;
	}

	public boolean isCollidingMiddleRight() {
		return collidingMiddleRight;
	}

	public void setCollidingMiddleRight(boolean collidingMiddleRight) {
		this.collidingMiddleRight = collidingMiddleRight;
	}

	public boolean isCollidingMiddleLeft() {
		return collidingMiddleLeft;
	}

	public void setCollidingMiddleLeft(boolean collidingMiddleLeft) {
		this.collidingMiddleLeft = collidingMiddleLeft;
	}
	
	public boolean isBlockedRight() {
		return (collidingTopRight && collidingBottomRight) 
				|| (collidingTopRight && collidingMiddleRight)
				|| (collidingMiddleRight && collidingBottomRight);				
	}
	
	public boolean isBlockedLeft() {
		return (collidingTopLeft && collidingBottomLeft) 
				|| (collidingTopLeft && collidingMiddleLeft)
				|| (collidingMiddleLeft && collidingBottomLeft);				
	}
	
	public boolean isBlockedTop() {
		return (collidingTopLeft && collidingTopRight);						
	}
	
	public boolean isBlockedBottom() {
		return (collidingBottomLeft && collidingBottomRight);						
	}

	public boolean isCollidingMiddleTop() {
		return collidingMiddleTop;
	}

	public void setCollidingMiddleTop(boolean collidingMiddleTop) {
		this.collidingMiddleTop = collidingMiddleTop;
	}
	
	public boolean isCollidingUpperBlock() {
		return (collidingTopLeft || collidingTopRight) && collidingMiddleTop;
	}
	
	public boolean isBlockedBottomOnly() {
		return (collidingBottomLeft || collidingBottomRight) 
				&& !collidingTopLeft && !collidingTopRight;
	}
}
