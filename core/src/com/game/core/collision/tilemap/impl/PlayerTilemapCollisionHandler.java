package com.game.core.collision.tilemap.impl;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.game.core.collision.tilemap.AbstractTilemapCollisionHandler;
import com.game.core.sprite.AbstractSprite;
import com.game.core.sprite.impl.player.Player;
import com.game.core.tilemap.TmxCell;
import com.game.core.tilemap.TmxMap;
import com.game.core.util.enums.DirectionEnum;

public class PlayerTilemapCollisionHandler extends AbstractTilemapCollisionHandler<Player> {

	public PlayerTilemapCollisionHandler() {		
	}
	
	public void collideWithTilemap(TmxMap tileMap, Player sprite) {
						
		sprite.setCollidingCells(new ArrayList<TmxCell>());
			
		Vector2 newPosition = new Vector2(sprite.getX(), sprite.getY());
		
		checkMapCollision(tileMap, sprite);				
										
		if (sprite.getMapCollisionEvent().getCollisionPoints().size()>0) {

			if (sprite.getMapCollisionEvent().isCollidingRight()) {				
				if (sprite.getDirection()==DirectionEnum.RIGHT) {
					newPosition.x = (int) sprite.getX() + 0.5f;
					sprite.getAcceleration().x = 0;
				}					
			}
			if (sprite.getMapCollisionEvent().isCollidingLeft()) {				
				if (sprite.getDirection()==DirectionEnum.LEFT) {
					sprite.getAcceleration().x = 0;
					newPosition.x = (int) sprite.getX() + 0.5f;
				}
			}
			if (sprite.getMapCollisionEvent().isCollidingBottom()) {				
				if (sprite.getDirection()==DirectionEnum.DOWN) {
					newPosition.y = (int) sprite.getY() + 0.5f;
					sprite.getAcceleration().y = 0;
				}				
			}
			if (sprite.getMapCollisionEvent().isCollidingTop()) {
				if (sprite.getDirection()==DirectionEnum.UP) {
					newPosition.y = (int) sprite.getY() + 0.5f;					
					sprite.getAcceleration().y = 0;
				}
				
			}
				
			sprite.setX(newPosition.x);
			sprite.setY(newPosition.y);		
			
		}					

	}
	
	@Override
	protected void checkMapCollision(TmxMap tilemap, AbstractSprite sprite) {		
		super.checkMapCollision(tilemap, sprite);			
	}	

}
