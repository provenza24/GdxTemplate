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

	protected void checkBottomMapCollision(TmxMap tilemap, AbstractSprite sprite) {
		
		sprite.reinitMapCollisionEvent();
		sprite.getMapCollisionEvent().reinitCollisionPoints();
		
		Vector2 leftBottomCorner = new Vector2(sprite.getX() + sprite.getOffset().x, sprite.getY());		
		Vector2 rightBottomCorner = new Vector2(sprite.getX() + sprite.getWidth() + sprite.getOffset().x, sprite.getY());
		
		int x = (int) leftBottomCorner.x;
		int y = (int) leftBottomCorner.y;
		boolean isCollision = tilemap.isCollisioningTileAt(x, y);		
		sprite.getMapCollisionEvent().setCollidingBottomLeft(isCollision);
	
		x = (int) rightBottomCorner.x;
		y = (int) rightBottomCorner.y;
		isCollision = tilemap.isCollisioningTileAt(x, y);		
		sprite.getMapCollisionEvent().setCollidingBottomRight(sprite.getMapCollisionEvent().isCollidingBottom() || isCollision);
	}
	
	protected void checkMapCollision(TmxMap tilemap, AbstractSprite sprite) {
		
		sprite.reinitMapCollisionEvent();
		sprite.getMapCollisionEvent().reinitCollisionPoints();		

		Vector2 leftBottomCorner = new Vector2(sprite.getX() + sprite.getOffset().x, sprite.getY());
		Vector2 leftTopCorner = new Vector2(sprite.getX() + sprite.getOffset().x, sprite.getY() + sprite.getHeight());
		Vector2 rightBottomCorner = new Vector2(sprite.getX() + sprite.getWidth() + sprite.getOffset().x, sprite.getY());
		Vector2 rightTopCorner = new Vector2(sprite.getX() + sprite.getWidth() + sprite.getOffset().x, sprite.getY() + sprite.getHeight());

		int x = (int) leftBottomCorner.x;
		int y = (int) leftBottomCorner.y;
		boolean isCollision = tilemap.isCollisioningTileAt(x, y);
		sprite.getMapCollisionEvent().setCollidingBottomLeft(isCollision);
		if (isCollision) {
			sprite.getMapCollisionEvent().getCollisionPoints().add(new CollisionPoint(leftBottomCorner, new TmxCell(tilemap.getTileAt(x, y), x, y)));
		}

		x = (int) leftTopCorner.x;
		y = (int) leftTopCorner.y;
		isCollision = tilemap.isCollisioningTileAt(x, y);
		sprite.getMapCollisionEvent().setCollidingTopLeft(isCollision);
		if (isCollision) {
			sprite.getMapCollisionEvent().getCollisionPoints().add(new CollisionPoint(leftTopCorner, new TmxCell(tilemap.getTileAt(x, y), x, y)));
		}

		x = (int) rightBottomCorner.x;
		y = (int) rightBottomCorner.y;
		isCollision = tilemap.isCollisioningTileAt(x, y);
		sprite.getMapCollisionEvent().setCollidingBottomRight(isCollision);
		if (isCollision) {
			sprite.getMapCollisionEvent().getCollisionPoints().add(new CollisionPoint(rightBottomCorner, new TmxCell(tilemap.getTileAt(x, y), x, y)));
		}

		x = (int) rightTopCorner.x;
		y = (int) rightTopCorner.y;
		isCollision = tilemap.isCollisioningTileAt(x, y);
		sprite.getMapCollisionEvent().setCollidingTopRight(isCollision);
		if (isCollision) {
			sprite.getMapCollisionEvent().getCollisionPoints().add(new CollisionPoint(rightTopCorner, new TmxCell(tilemap.getTileAt(x, y), x, y)));
		}
		
	}

}
