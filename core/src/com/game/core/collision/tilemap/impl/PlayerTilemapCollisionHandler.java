package com.game.core.collision.tilemap.impl;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.game.core.collision.CollisionPoint;
import com.game.core.collision.math.MathFunction;
import com.game.core.collision.tilemap.AbstractTilemapCollisionHandler;
import com.game.core.sprite.AbstractSprite;
import com.game.core.sprite.impl.player.Player;
import com.game.core.tilemap.TmxCell;
import com.game.core.tilemap.TmxMap;
import com.game.core.util.enums.DirectionEnum;
import com.game.core.util.enums.SpriteMoveEnum;

public class PlayerTilemapCollisionHandler extends AbstractTilemapCollisionHandler<Player> {

	private static final float COLLISION_X_CORRECTIF = 10e-5F;
	
	private static Cell previousCell;
	
	public PlayerTilemapCollisionHandler() {		
	}		
		 
	public void collideWithTilemap(TmxMap tileMap, Player sprite) {							
				
		boolean onFloorCorrection = false;
		
		sprite.setMove(new Vector2(sprite.getX() - sprite.getOldPosition().x, sprite.getY() - sprite.getOldPosition().y));
		
		if (sprite.getState()!=SpriteMoveEnum.JUMPING) {
			// Handle cloud tile case
			if (!handleCloudTile(tileMap, sprite)) {
				// Handle curved tile case
				if (!handleCurvedTile(tileMap, sprite)) {
					// Check if player was previously on a cloud/curved tile, update position in consequence
					leaveSpecialTile(sprite);
				}				
			}				
		}		
											
		if (!sprite.isOnCurvedTile() && !sprite.isOnCloudTile()) {
			// Player is not on a special tile (cloud/curved), handle collisions
			sprite.setCollidingCells(new ArrayList<TmxCell>());						
			sprite.setMove(new Vector2(sprite.getX() - sprite.getOldPosition().x, sprite.getY() - sprite.getOldPosition().y));
			// Check floor collision
			checkBottomMapCollision(tileMap, sprite);					
			if (sprite.getOldAcceleration().y == 0 && sprite.getMapCollisionEvent().isCollidingBottom()) {
				// Player was on a plateform and is still on it
				if (sprite.getState()==SpriteMoveEnum.FALLING) {
					sprite.setState(SpriteMoveEnum.IDLE);
				}
				sprite.setOnFloor(true);			
				sprite.setY((int) sprite.getY() + 1 + COLLISION_X_CORRECTIF);
				sprite.getOldPosition().y = sprite.getY();
				sprite.getAcceleration().y = 0;
				onFloorCorrection = true;						
			}
			// Check collisions			
			sprite.setMove(new Vector2(sprite.getX() - sprite.getOldPosition().x, sprite.getY() - sprite.getOldPosition().y));		
			Vector2 newPosition = new Vector2(sprite.getX(), sprite.getY());			
			checkMapCollision(tileMap, sprite);															
			if (sprite.getMapCollisionEvent().getCollisionPoints().size()>0) {
				// If collision point(s) are detected, handle it 
				int i=0;				
				while (sprite.getMapCollisionEvent().getCollisionPoints().size()>0) {					
					// Until there are collision points
					for (CollisionPoint collisionPoint : sprite.getMapCollisionEvent().getCollisionPoints()) {
						// Iterate on each collision point, do the collision
						collides(sprite, newPosition, collisionPoint);															
					}
					// Update player position
					sprite.setPosition(newPosition.x, newPosition.y);
					// Check if there are still collision points
					checkMapCollision(tileMap, sprite);
					i++;
					if (i>10) {						
						System.out.println("Erreur de collision ? "+i + " tours de boucle");
						System.exit(0);
					}					
				}	
				// All collisions are solved, fix player acceleration
				if (previousCell!=null) {
					fixPlayerAcceleration(tileMap, sprite);
				}			
			}  else {
				// No collision points detected,check if player is colliding floor or a block uppon his head (while jumping)
				checkFloorAndTopCollisions(tileMap, sprite, onFloorCorrection, newPosition);
			}
		}
		
	}

	private boolean handleCloudTile(TmxMap tileMap, Player sprite) {
		Vector2 position = new Vector2(sprite.getX() + sprite.getHalfWidth() + sprite.getOffset().x, sprite.getY());
		Cell cell = tileMap.getTileAt((int)position.x, (int)position.y);
		boolean isOnCloudTile = cell!=null && tileMap.isCloudTile(cell.getTile().getId());
		if (isOnCloudTile) {
			previousCell = cell;
			if (sprite.getState()==SpriteMoveEnum.FALLING) {
				sprite.setState(SpriteMoveEnum.IDLE);
			}
			sprite.setOnFloor(true);				
			sprite.setY((int)sprite.getY() + 1 + COLLISION_X_CORRECTIF);				
			sprite.getAcceleration().y = 0;
			sprite.setOnCloudTile(true);
		}
		return isOnCloudTile;
	}
	
	private boolean handleCurvedTile(TmxMap tileMap, Player sprite) {		
		
		boolean isThroughFloor=false;		
				
		float xPosition = sprite.getX() + sprite.getHalfWidth() + sprite.getOffset().x;
		float yPosition = sprite.getOldPosition().y;
		
		Cell cell = tileMap.getTileAt((int)xPosition, (int)yPosition);
		MathFunction mathFunction = cell!=null ? tileMap.getCurvedTilesFunctions().get(cell.getTile().getId()) : null;
				
		if (mathFunction==null) {
			yPosition = sprite.getOldPosition().y + 0.5f;
			cell = tileMap.getTileAt((int)xPosition, (int)yPosition);			
			mathFunction = cell!=null ? tileMap.getCurvedTilesFunctions().get(cell.getTile().getId()) : null;			
			isThroughFloor = mathFunction!=null;
			if (isThroughFloor) {
				Gdx.app.log("CURVED", "Pass throught");
			}				
		}
		if (mathFunction!=null) {
			float xDiff = xPosition - (int)xPosition;			
			float yFunc = mathFunction.compute(xDiff);					
			if (yPosition<=((int)yPosition + yFunc + 0.1f)) {
				previousCell = cell;
				if (sprite.getState()==SpriteMoveEnum.FALLING) {
					sprite.setState(SpriteMoveEnum.IDLE);
				}
				sprite.setOnFloor(true);			
				sprite.setY((int)yPosition + yFunc);				
				sprite.getAcceleration().y = 0;
				sprite.setOnCurvedTile(true);
			} else {				
				mathFunction = null;				
			}
		}
		return mathFunction!=null;
	}
	
	private void leaveSpecialTile(Player sprite) {
		if (sprite.isOnCurvedTile()) {						
			boolean ascending = previousCell.getTile().getId() <=198; // à partir de 199 -> pente en descente
			if (sprite.getDirection()==DirectionEnum.RIGHT) {
				sprite.setY(ascending ? (int)sprite.getOldPosition().y + 1 + COLLISION_X_CORRECTIF : (int)sprite.getY() + COLLISION_X_CORRECTIF);
			} else {
				sprite.setY(ascending ? (int)sprite.getOldPosition().y + COLLISION_X_CORRECTIF : (int)sprite.getY() + 1  + COLLISION_X_CORRECTIF);
			}
			previousCell = null;
		}	
		sprite.setOnCurvedTile(false);
		sprite.setOnCloudTile(false);
	}
	
	private void checkFloorAndTopCollisions(TmxMap tileMap, Player sprite, boolean onFloorCorrection, Vector2 newPosition) {
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

	private void fixPlayerAcceleration(TmxMap tileMap, Player sprite) {
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
	}

	private void collides(Player sprite, Vector2 newPosition, CollisionPoint collisionPoint) {
		if (sprite.getMove().y==0 && sprite.getMove().x!=0) {
			newPosition.x = sprite.getMove().x>0 ? (int) (sprite.getX() + sprite.getOffset().x) +  sprite.getOffset().x - COLLISION_X_CORRECTIF : 
				(int) (sprite.getX() + 1) -  sprite.getOffset().x + COLLISION_X_CORRECTIF;						
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
				newPosition.x = (int) (sprite.getX() + 1) - sprite.getOffset().x + COLLISION_X_CORRECTIF;					
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
					newPosition.x = (int) (sprite.getX() + 1) - sprite.getOffset().x + COLLISION_X_CORRECTIF;					
					sprite.getAcceleration().x = 0;					
				}
			}												
		}
		
		if (sprite.getMove().x<0 && sprite.getMove().y>0) {
									
			if (sprite.getMapCollisionEvent().isBlockedLeft()) {
				newPosition.x = (int) (sprite.getX() + 1) - sprite.getOffset().x + COLLISION_X_CORRECTIF;						
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
					newPosition.x = (int) (sprite.getX() + 1) - sprite.getOffset().x + COLLISION_X_CORRECTIF;						
					sprite.getAcceleration().x = 0;					
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
		Vector2 middleTopCorner = new Vector2(sprite.getX() + sprite.getHalfWidth() + sprite.getOffset().x, sprite.getY() + sprite.getHeight());
		
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
