package com.game.core.collision.tilemap.impl;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.game.core.collision.CollisionPoint;
import com.game.core.collision.tilemap.AbstractTilemapCollisionHandler;
import com.game.core.sprite.AbstractSprite;
import com.game.core.tilemap.TmxCell;
import com.game.core.tilemap.TmxMap;
import com.game.core.util.enums.SpriteMoveEnum;

public class BasicTilemapCollisionHandler extends AbstractTilemapCollisionHandler<AbstractSprite> {

	private static final float COLLISION_X_CORRECTIF = 10e-5F;
	
	public BasicTilemapCollisionHandler() {						
	}
			
	public void collideWithTilemap(TmxMap tileMap, AbstractSprite sprite) {
		
		sprite.setCollidingCells(new ArrayList<TmxCell>());
		
		boolean onFloorCorrection = false;
		Vector2 move = new Vector2(sprite.getAcceleration().y - sprite.getOldPosition().x, sprite.getY() - sprite.getOldPosition().y);
				
		checkBottomMapCollision(tileMap, sprite);		
		
		if (sprite.getOldAcceleration().y == 0 && sprite.getMapCollisionEvent().isCollidingBottom()) {			
			sprite.setOnFloor(true);
			sprite.setY((int) sprite.getY() + 1);
			sprite.getOldPosition().y = sprite.getY();
			sprite.getAcceleration().y = 0;
			onFloorCorrection = true;
		}
		
		move = new Vector2(sprite.getX() - sprite.getOldPosition().x, sprite.getY() - sprite.getOldPosition().y);		
		Vector2 newPosition = new Vector2(sprite.getX(), sprite.getY());
		
		checkMapCollision(tileMap, sprite);				
										
		boolean reverseAcceleration = false;
		
		if (sprite.getMapCollisionEvent().getCollisionPoints().size()>0) {
												
			int i=0;
			
			while (sprite.getMapCollisionEvent().getCollisionPoints().size()>0) {			
				
				for (CollisionPoint collisionPoint : sprite.getMapCollisionEvent().getCollisionPoints()) {
					
					if (move.y==0 && move.x!=0) {						
						newPosition.x = move.x>0 ? (int) (sprite.getX() + sprite.getOffset().x) + sprite.getOffset().x - COLLISION_X_CORRECTIF : (int) (sprite.getX() + sprite.getWidth() + sprite.getOffset().x) - sprite.getOffset().x + COLLISION_X_CORRECTIF;						
						reverseAcceleration = true;						
					}
					
					if (move.y<0 && move.x==0) {						
						newPosition.y = (int) sprite.getY() + 1f;
						sprite.getAcceleration().y = 0;												
						sprite.setOnFloor(true);
						if (isFalling(sprite)) {
							sprite.setState(SpriteMoveEnum.WALKING);
						}
					}
					
					if (move.y>0 && move.x==0) {
							
						newPosition.y = (int) sprite.getY();
						sprite.getAcceleration().y = 10e-5F;																					
					}
					
					if (move.x>0 && move.y>0) {
						
						if (sprite.getMapCollisionEvent().isBlockedRight()) {
							newPosition.x = (int) (sprite.getX() + sprite.getOffset().x) + sprite.getOffset().x - COLLISION_X_CORRECTIF;						
							reverseAcceleration = true;						
						} else {
							float xDelta = collisionPoint.getPoint().x - collisionPoint.getCell().getX();
							float yDelta = collisionPoint.getPoint().y - collisionPoint.getCell().getY();
																		
							if (xDelta>yDelta) {
								newPosition.y = (int) sprite.getY();
								sprite.getAcceleration().y = 10e-5F;	
								sprite.setOnFloor(true);
								if (isFalling(sprite)) {
									sprite.setState(SpriteMoveEnum.WALKING);
								}
							} else {								
								newPosition.x = (int) (sprite.getX() + sprite.getOffset().x) + sprite.getOffset().x - COLLISION_X_CORRECTIF;																				
							}
						}						
						
					}
					
					if (move.x>0 && move.y<0) {
					
						if (sprite.getMapCollisionEvent().isBlockedRight()) {
							newPosition.x = (int) (sprite.getX() + sprite.getOffset().x) + sprite.getOffset().x - COLLISION_X_CORRECTIF;						
							reverseAcceleration = true;					
						} else {	
							float xDelta = collisionPoint.getPoint().x - collisionPoint.getCell().getX();
							float yDelta = (collisionPoint.getCell().getY() + 1) - collisionPoint.getPoint().y;
							if (xDelta>yDelta) {				
								newPosition.y = (int) sprite.getY() + 1f;						
								sprite.getAcceleration().y = 0;
								sprite.setOnFloor(true);
								if (isFalling(sprite)) {
									sprite.setState(SpriteMoveEnum.WALKING);
								}
							} else {
								newPosition.x = (int) (sprite.getX() + sprite.getOffset().x) + sprite.getOffset().x - COLLISION_X_CORRECTIF;														
							}
						}
												
					}
					
					if (move.x<0 && move.y<0) {	
						
						if (sprite.getMapCollisionEvent().isBlockedLeft()) {						
							newPosition.x = (int) (sprite.getX() + sprite.getWidth() + sprite.getOffset().x) - sprite.getOffset().x + COLLISION_X_CORRECTIF;					
							reverseAcceleration = true;									
						} else {
							float xDelta = (collisionPoint.getCell().getX()+1) - collisionPoint.getPoint().x;
							float yDelta = (collisionPoint.getCell().getY()+1) - collisionPoint.getPoint().y;
							if (xDelta>yDelta) {
								newPosition.y = (int) sprite.getY() + 1f;
								sprite.getAcceleration().y = 0;
								sprite.setOnFloor(true);
								if (isFalling(sprite)) {
									sprite.setState(SpriteMoveEnum.WALKING);
								}
							} else {
								newPosition.x = (int) (sprite.getX() + sprite.getWidth() + sprite.getOffset().x) - sprite.getOffset().x + COLLISION_X_CORRECTIF;								
							}
						}												
					}
					
					if (move.x<0 && move.y>0) {
					
						if (sprite.getMapCollisionEvent().isBlockedLeft()) {
							newPosition.x = (int) (sprite.getX() + sprite.getWidth() + sprite.getOffset().x) - sprite.getOffset().x + COLLISION_X_CORRECTIF;							
							reverseAcceleration = true;
						} else {
							float xDelta = (collisionPoint.getCell().getX()+1) - collisionPoint.getPoint().x;
							float yDelta = collisionPoint.getPoint().y - (collisionPoint.getCell().getY());
							if (xDelta>yDelta) {
								newPosition.y = (int) sprite.getY();
								sprite.getAcceleration().y = 10e-5F;
								if (isFalling(sprite)) {
									sprite.setState(SpriteMoveEnum.WALKING);
								}
							} else {
								newPosition.x = (int) (sprite.getX() + sprite.getWidth() + sprite.getOffset().x) - sprite.getOffset().x + COLLISION_X_CORRECTIF;																
							}
						}
												
					}									
														
				}
				sprite.setX(newPosition.x);
				sprite.setY(newPosition.y);
				checkMapCollision(tileMap, sprite);
				i++;
				if (i>10) {
					System.out.println("Erreur de collision ?"+i);
				}
				
			}	
											
		}  else {
			if (move.y < 0 && !onFloorCorrection) {				
				sprite.setOnFloor(false);				
				sprite.setState(SpriteMoveEnum.FALLING);				
			}
		}	
		
		if (reverseAcceleration && !isFalling(sprite)) {				
			sprite.getAcceleration().x = - sprite.getAcceleration().x;
		}
		
	}	
	
	private boolean isFalling(AbstractSprite sprite) {
		return sprite.getState() == SpriteMoveEnum.FALLING;
	}

}
