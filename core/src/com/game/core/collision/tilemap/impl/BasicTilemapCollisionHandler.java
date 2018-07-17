package com.game.core.collision.tilemap.impl;

import java.util.ArrayList;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.game.core.collision.math.MathFunction;
import com.game.core.collision.tilemap.AbstractTilemapCollisionHandler;
import com.game.core.sprite.AbstractSprite;
import com.game.core.tilemap.TmxCell;
import com.game.core.tilemap.TmxMap;
import com.game.core.util.enums.DirectionEnum;

public class BasicTilemapCollisionHandler extends AbstractTilemapCollisionHandler<AbstractSprite> {

	private static final float COLLISION_X_CORRECTIF = 10e-5F;
	
	private Cell previousCell;
	
	public BasicTilemapCollisionHandler() {						
	}
			
	public void collideWithTilemap(TmxMap tileMap, AbstractSprite sprite) {
		
		sprite.setCollidingCells(new ArrayList<TmxCell>());
		
		if (!handleSlopeTile(tileMap, sprite)) {
			leaveSpecialTile(tileMap, sprite);
		}
		
		if (!sprite.isOnSlopeTile()) {
			// Player is not on a special tile (cloud/Slope), handle collisions
			sprite.setCollidingCells(new ArrayList<TmxCell>());						
			
			// Check floor collision
			checkBottomMapCollision(tileMap, sprite);				
			if (sprite.getMapCollisionEvent().isCollidingBottom()) {				
				sprite.setOnFloor(true);			
				sprite.setY((int) sprite.getY() + 1 + COLLISION_X_CORRECTIF);
				sprite.getOldPosition().y = sprite.getY();
				sprite.getAcceleration().y = 0;									
			}
			
			// Check collisions			
			checkMapCollision(tileMap, sprite);		
			if (sprite.getMapCollisionEvent().isBlockedLeft() || sprite.getMapCollisionEvent().isBlockedRight()) {
				sprite.setX(sprite.getOldPosition().x);				
				sprite.inverseDirection();
			}
		}		
		
	}
	
	private boolean handleSlopeTile(TmxMap tileMap, AbstractSprite sprite) {		
		
		MathFunction mathFunction = null;
		
		boolean startGoingDown = false;
		boolean startGoingUp = false;
		
		if (sprite.isOnFloor()) {
			float xPosition = sprite.getX() + sprite.getHalfWidth() + sprite.getOffset().x;
			float yPosition = sprite.getOldPosition().y;
			Cell cell = tileMap.getTileAt((int)xPosition, (int)yPosition-1);
			mathFunction = cell!=null ? tileMap.getSlopeTilesFunctions().get(cell.getTile().getId()) : null;		
			if (mathFunction!=null) {			
				boolean ascending = sprite.getDirection()==DirectionEnum.RIGHT ? cell.getTile().getId() <=198 : cell.getTile().getId() > 198;				
				if (!ascending) {
					startGoingDown = true;
					float xDiff = xPosition - (int)xPosition;
					float yFunc = mathFunction.compute(xDiff);
					previousCell = cell;					
					sprite.setOnFloor(true);			
					sprite.setY((int)yPosition - 1 + yFunc);				
					sprite.getAcceleration().y = 0;
					sprite.setOnSlopeTile(true);
					sprite.setPositiveSlopeTile(ascending);
				}			
			}
		}
		
		if (!startGoingDown && sprite.isOnFloor()) {
			float xPosition = sprite.getX() + sprite.getHalfWidth() + sprite.getOffset().x;
			float yPosition = sprite.getOldPosition().y;
			Cell cell = tileMap.getTileAt((int)xPosition, (int)yPosition+1);
			mathFunction = cell!=null ? tileMap.getSlopeTilesFunctions().get(cell.getTile().getId()) : null;		
			if (mathFunction!=null) {			
				boolean ascending = sprite.getDirection()==DirectionEnum.RIGHT ? cell.getTile().getId() <=198 : cell.getTile().getId() > 198;				
				if (ascending) {
					startGoingUp = true;
					float xDiff = xPosition - (int)xPosition;
					float yFunc = mathFunction.compute(xDiff);				
					sprite.setOnFloor(true);			
					sprite.setY((int)yPosition + 1 + yFunc);				
					sprite.getAcceleration().y = 0;
					sprite.setOnSlopeTile(true);
					sprite.setPositiveSlopeTile(ascending);
				}			
			}
		}
		
		if (!startGoingDown && !startGoingUp) {
			
			if (sprite.isOnFloor()) {
				boolean autoCorrection = false;
				
				float xPosition = sprite.getX() + sprite.getHalfWidth() + sprite.getOffset().x;
				float yPosition = sprite.getOldPosition().y;
				
				Cell cell = tileMap.getTileAt((int)xPosition, (int)yPosition);
				mathFunction = cell!=null ? tileMap.getSlopeTilesFunctions().get(cell.getTile().getId()) : null;
						
				if (mathFunction==null) {
					yPosition = sprite.getOldPosition().y + 0.8f;
					cell = tileMap.getTileAt((int)xPosition, (int)yPosition);			
					mathFunction = cell!=null ? tileMap.getSlopeTilesFunctions().get(cell.getTile().getId()) : null;
					autoCorrection = mathFunction!=null;
				}
				if (mathFunction!=null) {
					boolean ascending = sprite.getDirection()==DirectionEnum.RIGHT ? cell.getTile().getId() <=198 : cell.getTile().getId() > 198;
					float xDiff = xPosition - (int)xPosition;			
					float yFunc = mathFunction.compute(xDiff);					
					if (autoCorrection || yPosition<=((int)yPosition + yFunc + 0.1f)) {
						previousCell = cell;						
						sprite.setOnFloor(true);			
						sprite.setY((int)yPosition + yFunc);				
						sprite.getAcceleration().y = 0;
						sprite.setOnSlopeTile(true);
						sprite.setPositiveSlopeTile(ascending);
					} else {				
						mathFunction = null;				
					}
				}
			} else {				
				boolean autoCorrection = false;
				
				float xPosition = sprite.getX() + sprite.getHalfWidth() + sprite.getOffset().x;
				float yPosition = sprite.getY();
				
				Cell cell = tileMap.getTileAt((int)xPosition, (int)yPosition);
				mathFunction = cell!=null ? tileMap.getSlopeTilesFunctions().get(cell.getTile().getId()) : null;
						
				if (mathFunction==null) {
					yPosition += 1;
					cell = tileMap.getTileAt((int)xPosition, (int)yPosition);			
					mathFunction = cell!=null ? tileMap.getSlopeTilesFunctions().get(cell.getTile().getId()) : null;
					autoCorrection = mathFunction!=null;
				}
				if (mathFunction!=null) {
					boolean ascending = sprite.getDirection()==DirectionEnum.RIGHT ? cell.getTile().getId() <=198 : cell.getTile().getId() > 198;
					float xDiff = xPosition - (int)xPosition;			
					float yFunc = mathFunction.compute(xDiff);					
					if (autoCorrection || yPosition<=((int)yPosition + yFunc + 0.1f)) {
						previousCell = cell;						
						sprite.setOnFloor(true);			
						sprite.setY((int)yPosition + yFunc);				
						sprite.getAcceleration().y = 0;
						sprite.setOnSlopeTile(true);
						sprite.setPositiveSlopeTile(ascending);
					} else {				
						mathFunction = null;				
					}
				}				
			}			
			
		}
				
		return mathFunction!=null;
	}
	
	private boolean leaveSpecialTile(TmxMap tilemap, AbstractSprite sprite) {
		boolean onFloorCorrection = false;
		if (sprite.isOnSlopeTile() && !tilemap.isSlopeConstantTile(previousCell.getTile().getId())) {			
			onFloorCorrection = true;
			boolean ascending = previousCell.getTile().getId() <=198;
			if (sprite.getDirection()==DirectionEnum.RIGHT) {
				sprite.setY(ascending ? (int)sprite.getOldPosition().y + 1 + COLLISION_X_CORRECTIF : (int)sprite.getY() + COLLISION_X_CORRECTIF);
			} else {
				sprite.setY(ascending ? (int)sprite.getOldPosition().y + COLLISION_X_CORRECTIF : (int)sprite.getY() + 1  + COLLISION_X_CORRECTIF);
			}						
			previousCell = null;
		}	
		sprite.setOnSlopeTile(false);
		sprite.setOnCloudTile(false);
		return onFloorCorrection;
	}

}
