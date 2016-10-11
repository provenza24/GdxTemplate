package com.game.core.collision.tilemap;

import com.badlogic.gdx.math.Vector2;
import com.game.core.collision.CollisionPoint;
import com.game.core.sprite.AbstractSprite;
import com.game.core.tilemap.TmxCell;
import com.game.core.tilemap.TmxMap;

public abstract class AbstractTilemapCollisionHandler<T extends AbstractSprite> implements ITilemapCollisionHandler<T> {

	public AbstractTilemapCollisionHandler() {		
	}
	
	@Override
	public abstract void collideWithTilemap(TmxMap tileMap, T sprite);
	
	protected void checkMapCollision(TmxMap tilemap, AbstractSprite sprite) {
						
		sprite.reinitMapCollisionEvent();
		sprite.getMapCollisionEvent().reinitCollisionPoints();		

		Vector2 middleTop = new Vector2(sprite.getX() + sprite.getWidth()/2, sprite.getY() + sprite.getHeight() - 0.5f);
		Vector2 middleBottom = new Vector2(sprite.getX() + sprite.getWidth()/2, sprite.getY() + 0.5f);
		
		Vector2 middleRight = new Vector2(sprite.getX() + sprite.getWidth() - 0.5f, sprite.getY() + sprite.getHeight()/2);
		Vector2 middleLeft = new Vector2(sprite.getX() + 0.5f, sprite.getY() + sprite.getHeight()/2);
		
		int x = (int) middleBottom.x;
		int y = (int) middleBottom.y;
		boolean isCollision = tilemap.isCollisioningTileAt(x, y);
		sprite.getMapCollisionEvent().setCollidingBottom(isCollision);
		if (isCollision) {
			sprite.getMapCollisionEvent().getCollisionPoints().add(new CollisionPoint(middleBottom, new TmxCell(tilemap.getTileAt(x, y), x, y)));
		}		
		
		x = (int) middleTop.x;
		y = (int) middleTop.y;
		isCollision = tilemap.isCollisioningTileAt(x, y);
		sprite.getMapCollisionEvent().setCollidingTop(isCollision);
		if (isCollision) {
			sprite.getMapCollisionEvent().getCollisionPoints().add(new CollisionPoint(middleTop, new TmxCell(tilemap.getTileAt(x, y), x, y)));
		}	
		
		x = (int) middleRight.x;
		y = (int) middleRight.y;
		isCollision = tilemap.isCollisioningTileAt(x, y);
		sprite.getMapCollisionEvent().setCollidingRight(isCollision);
		if (isCollision) {
			sprite.getMapCollisionEvent().getCollisionPoints().add(new CollisionPoint(middleRight, new TmxCell(tilemap.getTileAt(x, y), x, y)));
		}
		
		x = (int) middleLeft.x;
		y = (int) middleLeft.y;
		isCollision = tilemap.isCollisioningTileAt(x, y);
		sprite.getMapCollisionEvent().setCollidingLeft(isCollision);
		if (isCollision) {
			sprite.getMapCollisionEvent().getCollisionPoints().add(new CollisionPoint(middleLeft, new TmxCell(tilemap.getTileAt(x, y), x, y)));
		}	
		
		
	}

}
