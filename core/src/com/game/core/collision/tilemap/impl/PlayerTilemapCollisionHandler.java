package com.game.core.collision.tilemap.impl;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.game.core.collision.CollisionPoint;
import com.game.core.collision.tilemap.AbstractTilemapCollisionHandler;
import com.game.core.sprite.AbstractSprite;
import com.game.core.sprite.impl.player.Player;
import com.game.core.tilemap.TmxCell;
import com.game.core.tilemap.TmxMap;
import com.game.core.util.enums.DirectionEnum;
import com.game.core.util.enums.SpriteMoveEnum;

public class PlayerTilemapCollisionHandler extends AbstractTilemapCollisionHandler<Player> {

	private static final float COLLISION_X_CORRECTIF = 10e-5F;
	
	public PlayerTilemapCollisionHandler() {		
	}
	
	public void collideWithTilemap(TmxMap tileMap, Player sprite) {
						
		sprite.setCollidingCells(new ArrayList<TmxCell>());
		
		boolean onFloorCorrection = false;
		sprite.setMove(new Vector2(sprite.getX() - sprite.getOldPosition().x, sprite.getY() - sprite.getOldPosition().y));
				
		checkBottomMapCollision(tileMap, sprite);		
		
		if (sprite.getOldAcceleration().y == 0 && sprite.getMapCollisionEvent().isCollidingBottom()) {
			// Player is on a plateform and is still on it
			if (sprite.getState()==SpriteMoveEnum.FALLING) {
				sprite.setState(SpriteMoveEnum.IDLE);
			}
			sprite.setOnFloor(true);			
			sprite.setY((int) sprite.getY() + 1 + COLLISION_X_CORRECTIF);
			sprite.getOldPosition().y = sprite.getY();
			sprite.getAcceleration().y = 0;
			onFloorCorrection = true;
		}
		
		sprite.setMove(new Vector2(sprite.getX() - sprite.getOldPosition().x, sprite.getY() - sprite.getOldPosition().y));		
		Vector2 newPosition = new Vector2(sprite.getX(), sprite.getY());
		
		checkMapCollision(tileMap, sprite);				
										
		if (sprite.getMapCollisionEvent().getCollisionPoints().size()>0) {
							
			int i=0;
			
			while (sprite.getMapCollisionEvent().getCollisionPoints().size()>0) {
			
				for (CollisionPoint collisionPoint : sprite.getMapCollisionEvent().getCollisionPoints()) {
					
					if (sprite.getMove().y==0 && sprite.getMove().x!=0) {
						newPosition.x = sprite.getMove().x>0 ? (int) (sprite.getX() + sprite.getOffset().x) +  sprite.getOffset().x - COLLISION_X_CORRECTIF : (int) (sprite.getX() + sprite.getWidth() +  sprite.getOffset().x) -  sprite.getOffset().x + COLLISION_X_CORRECTIF;						
						sprite.getAcceleration().x = 0;	
						if (sprite.getState()!=SpriteMoveEnum.FALLING 
								&& sprite.getState()!=SpriteMoveEnum.JUMPING) {
							sprite.setState(SpriteMoveEnum.IDLE);
						}
					}
					
					if (sprite.getMove().y<0 && sprite.getMove().x==0) {						
						newPosition.y = (int) sprite.getY() + 1f;
						sprite.getAcceleration().y = 0;
						sprite.setState(SpriteMoveEnum.IDLE);
						sprite.setOnFloor(true);					
					}
					
					if (sprite.getMove().y>0 && sprite.getMove().x==0) {					
						sprite.addCollidingCell(collisionPoint.getCell());						
						newPosition.y = (int) sprite.getY();
						sprite.getAcceleration().y = 10e-5F;						
						sprite.setState(SpriteMoveEnum.FALLING);						
					}
					
					if (sprite.getMove().x>0 && sprite.getMove().y>0) {
											
						if (sprite.getMapCollisionEvent().isBlockedRight()) {
							newPosition.x = (int) (sprite.getX() + sprite.getOffset().x) + sprite.getOffset().x - COLLISION_X_CORRECTIF;						
							sprite.getAcceleration().x = 0;									
						} else {
							float xDelta = collisionPoint.getPoint().x - collisionPoint.getCell().getX();
							float yDelta = collisionPoint.getPoint().y - collisionPoint.getCell().getY();
																		
							if (xDelta>yDelta) {
								sprite.addCollidingCell(collisionPoint.getCell());
								newPosition.y = (int) sprite.getY();
								sprite.getAcceleration().y = 10e-5F;								
								if (sprite.getState()!=SpriteMoveEnum.FALLING && sprite.getState()!=SpriteMoveEnum.JUMPING) {
									sprite.setState(SpriteMoveEnum.IDLE);
									sprite.setOnFloor(true);		
								} else if (sprite.getState()==SpriteMoveEnum.JUMPING) {
									sprite.setState(SpriteMoveEnum.FALLING);
								}						
							} else {								
								newPosition.x = (int) (sprite.getX() + sprite.getOffset().x) + sprite.getOffset().x - COLLISION_X_CORRECTIF;						
								sprite.getAcceleration().x = 0;					
							}
						}						
						
					}
					
					if (sprite.getMove().x>0 && sprite.getMove().y<0) {
					
						if (sprite.getMapCollisionEvent().isBlockedRight()) {
							newPosition.x = (int) (sprite.getX() + sprite.getOffset().x) + sprite.getOffset().x - COLLISION_X_CORRECTIF;						
							sprite.getAcceleration().x = 0;									
						} else {	
							float xDelta = collisionPoint.getPoint().x - collisionPoint.getCell().getX();
							float yDelta = (collisionPoint.getCell().getY() + 1) - collisionPoint.getPoint().y;														
							if (sprite.getMapCollisionEvent().getCollisionPoints().size()>1 || xDelta>yDelta) {								
								newPosition.y = (int) sprite.getY() + 1f;						
								sprite.getAcceleration().y = 0;
								sprite.setOnFloor(true);		
								sprite.setState(SpriteMoveEnum.IDLE);
							} else {								
								newPosition.x = (int) (sprite.getX() + sprite.getOffset().x) + sprite.getOffset().x - COLLISION_X_CORRECTIF;						
								sprite.getAcceleration().x = 0;										
							}
						}
												
					}
					
					if (sprite.getMove().x<0 && sprite.getMove().y<0) {							
												
						if (sprite.getMapCollisionEvent().isBlockedLeft()) {						
							newPosition.x = (int) (sprite.getX() + sprite.getWidth() + sprite.getOffset().x) - sprite.getOffset().x + COLLISION_X_CORRECTIF;					
							sprite.getAcceleration().x = 0;				
							
						} else {
							float xDelta = (collisionPoint.getCell().getX()+1) - collisionPoint.getPoint().x;
							float yDelta = (collisionPoint.getCell().getY()+1) - collisionPoint.getPoint().y;
							if (sprite.getMapCollisionEvent().getCollisionPoints().size()>1 || xDelta>yDelta) {
								newPosition.y = (int) sprite.getY() + 1f;
								sprite.getAcceleration().y = 0;
								sprite.setOnFloor(true);
								sprite.setState(SpriteMoveEnum.IDLE);
							} else {
								newPosition.x = (int) (sprite.getX() + sprite.getWidth() + sprite.getOffset().x) - sprite.getOffset().x + COLLISION_X_CORRECTIF;					
								sprite.getAcceleration().x = 0;					
							}
						}												
					}
					
					if (sprite.getMove().x<0 && sprite.getMove().y>0) {
												
						if (sprite.getMapCollisionEvent().isBlockedLeft()) {
							newPosition.x = (int) (sprite.getX() + sprite.getWidth() + sprite.getOffset().x) - sprite.getOffset().x + COLLISION_X_CORRECTIF;						
							sprite.getAcceleration().x = 0;			
						} else {
							float xDelta = (collisionPoint.getCell().getX()+1) - collisionPoint.getPoint().x;
							float yDelta = collisionPoint.getPoint().y - (collisionPoint.getCell().getY());
							if (xDelta>yDelta) {
																
								sprite.addCollidingCell(collisionPoint.getCell());
								newPosition.y = (int) sprite.getY();
								sprite.getAcceleration().y = 10e-5F;
								
								if (sprite.getState()!=SpriteMoveEnum.FALLING && sprite.getState()!=SpriteMoveEnum.JUMPING) {
									sprite.setState(SpriteMoveEnum.IDLE);
									sprite.setOnFloor(true);
								} else if (sprite.getState()==SpriteMoveEnum.JUMPING) {
									sprite.setState(SpriteMoveEnum.FALLING);
								}
																
							} else {								
								newPosition.x = (int) (sprite.getX() + sprite.getWidth() + sprite.getOffset().x) - sprite.getOffset().x + COLLISION_X_CORRECTIF;						
								sprite.getAcceleration().x = 0;					
							}
						}
						
					}
														
				}
				sprite.setX(newPosition.x);
				sprite.setY(newPosition.y);
				checkMapCollision(tileMap, sprite);
				i++;
				if (i>10) {
					System.out.println(sprite.getMove().x+"-"+sprite.getMove().y);
					System.out.println("Erreur de collision ?"+i);
				}
				
			}	
			// The collision has been handled, now fix player acceleration
			if (sprite.getMove().x<0 || (sprite.getMove().x==0 && sprite.getDirection()==DirectionEnum.LEFT)) {
				Vector2 leftBottomCorner = new Vector2(sprite.getX() + sprite.getOffset().x - 2*COLLISION_X_CORRECTIF, sprite.getY());
				Vector2 leftTopCorner = new Vector2(sprite.getX() + sprite.getOffset().x - 2*COLLISION_X_CORRECTIF, sprite.getY() + sprite.getHeight());				
				int x = (int) leftBottomCorner.x;
				int y = (int) leftBottomCorner.y;
				boolean isCollision = tileMap.isCollisioningTileAt(x, y);
				if (!isCollision) {
					x = (int) leftTopCorner.x;
					y = (int) leftTopCorner.y;
					isCollision = tileMap.isCollisioningTileAt(x, y);
					if (!isCollision) {
						Vector2 leftMiddle = new Vector2(sprite.getX() + sprite.getOffset().x - 2*COLLISION_X_CORRECTIF, sprite.getY() + sprite.getHeight() / 2);
						x = (int) leftMiddle.x;
						y = (int) leftMiddle.y;
						isCollision = tileMap.isCollisioningTileAt(x, y);
					}
				}				
				sprite.getAcceleration().x = isCollision ? 0 : sprite.getOldAcceleration().x;
			} else {
				Vector2 rightBottomCorner = new Vector2(sprite.getX() + sprite.getWidth() + sprite.getOffset().x + 2*COLLISION_X_CORRECTIF, sprite.getY());
				Vector2 rightTopCorner = new Vector2(sprite.getX() + sprite.getWidth() + sprite.getOffset().x + 2*COLLISION_X_CORRECTIF, sprite.getY() + sprite.getHeight());			
				int x = (int) rightBottomCorner.x;
				int y = (int) rightBottomCorner.y;
				boolean isCollision = tileMap.isCollisioningTileAt(x, y);
				if (!isCollision) {
					x = (int) rightTopCorner.x;
					y = (int) rightTopCorner.y;
					isCollision = tileMap.isCollisioningTileAt(x, y);
					if (!isCollision) {
						Vector2 rightMiddle = new Vector2(sprite.getX() + sprite.getWidth() + sprite.getOffset().x + 2*COLLISION_X_CORRECTIF, sprite.getY() + sprite.getHeight() / 2);
						x = (int) rightMiddle.x;
						y = (int) rightMiddle.y;
						isCollision = tileMap.isCollisioningTileAt(x, y);
					}
				}				
				sprite.getAcceleration().x = isCollision ? 0 : sprite.getOldAcceleration().x;
			}										
		}  else {
			
			if (sprite.getMove().y < 0 && !onFloorCorrection) {						
				sprite.setState(SpriteMoveEnum.FALLING);
				sprite.setOnFloor(false);
			} else {
				checkUpperMapCollision(tileMap, sprite);
				if (sprite.getState()==SpriteMoveEnum.JUMPING && sprite.getMapCollisionEvent().isCollidingUpperBlock()) {					
					TmxCell collidingCell = sprite.getMapCollisionEvent().getCollisionPoints().get(0).getCell();
					if (sprite.getY()-collidingCell.getY()<0.2f) {
						sprite.addCollidingCell(sprite.getMapCollisionEvent().getCollisionPoints().get(0).getCell());
						newPosition.y = (int) sprite.getY();
						sprite.setY(newPosition.y);
						sprite.getAcceleration().y = 10e-5F;
						sprite.setState(SpriteMoveEnum.FALLING);
					}														
				}
			}
		}		
				

	}
	
	@Override
	protected void checkMapCollision(TmxMap tilemap, AbstractSprite sprite) {		
		super.checkMapCollision(tilemap, sprite);			
	}
	
	protected void checkUpperMapCollision(TmxMap tilemap, AbstractSprite sprite) {
		
		sprite.reinitMapCollisionEvent();
		sprite.getMapCollisionEvent().reinitCollisionPoints();
		
		Vector2 leftTopCorner = new Vector2(sprite.getX() + sprite.getOffset().x, sprite.getY() + sprite.getHeight());
		Vector2 rightTopCorner = new Vector2(sprite.getX() + sprite.getWidth() + sprite.getOffset().x, sprite.getY() + sprite.getHeight());
		Vector2 middleTopCorner = new Vector2(sprite.getX() + sprite.getWidth()/2 + sprite.getOffset().x, sprite.getY() + sprite.getHeight());
		
		int x = (int) leftTopCorner.x;
		int y = (int) leftTopCorner.y;
		boolean isCollision = tilemap.isCollisioningTileAt(x, y);
		sprite.getMapCollisionEvent().setCollidingTopLeft(isCollision);
		if (isCollision) {
			sprite.getMapCollisionEvent().getCollisionPoints().add(new CollisionPoint(leftTopCorner, new TmxCell(tilemap.getTileAt(x, y), x, y)));
		}
		
		x = (int) rightTopCorner.x;
		y = (int) rightTopCorner.y;
		isCollision = tilemap.isCollisioningTileAt(x, y);
		sprite.getMapCollisionEvent().setCollidingTopRight(isCollision);
		if (isCollision) {
			sprite.getMapCollisionEvent().getCollisionPoints().add(new CollisionPoint(rightTopCorner, new TmxCell(tilemap.getTileAt(x, y), x, y)));
		}
		
		x = (int) middleTopCorner.x;
		y = (int) middleTopCorner.y;
		isCollision = tilemap.isCollisioningTileAt(x, y);
		sprite.getMapCollisionEvent().setCollidingMiddleTop(isCollision);
		if (isCollision) {
			sprite.getMapCollisionEvent().getCollisionPoints().add(new CollisionPoint(middleTopCorner, new TmxCell(tilemap.getTileAt(x, y), x, y)));
		}
	
	}


}
