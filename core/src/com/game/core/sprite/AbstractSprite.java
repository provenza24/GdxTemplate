package com.game.core.sprite;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.game.core.collision.CollisionEvent;
import com.game.core.collision.tilemap.ITilemapCollisionHandler;
import com.game.core.collision.tilemap.impl.BasicTilemapCollisionHandler;
import com.game.core.tilemap.TmxCell;
import com.game.core.tilemap.TmxMap;
import com.game.core.util.RectangleUtil;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.DirectionEnum;
import com.game.core.util.enums.SpriteMoveEnum;

public abstract class AbstractSprite extends Actor implements IMoveable, IDrawable, ICollisionable {
			
	/** Animations state time */
	protected static float commonStateTime; 
	
	protected float stateTime;
		
	/** Offset */
	protected Vector2 offset;
	
	/** Rendering size */
	protected Vector2 renderingSize;
	
	protected float halfWidth;
	
	protected float halfHeight;
	
	/** Sprite will be visible when player will reach this absciss value */
	protected float xAlive;
	
	protected float yAlive;
	
	/** States */
	protected DirectionEnum direction;
	
	protected SpriteMoveEnum state;	
		
	/** Physics : default is 0.018f */
	protected float GRAVITY_COEF = 0.014f;
	
	protected Vector2 acceleration;

	protected Vector2 oldPosition;
	
	protected Vector2 oldAcceleration;	

	protected Rectangle bounds;
	
	protected Polygon polygonBounds;
	
	/** Collisions */
	protected ITilemapCollisionHandler tilemapCollisionHandler;
	
	protected CollisionEvent mapCollisionEvent;
	
	protected List<TmxCell> collidingCells;

	/** Textures and animations */
	protected Texture spriteSheet;
	
	protected TextureRegion currentFrame;
	
	protected Animation currentAnimation;
	
	protected Animation killedAnimation;
	
	/** Booleans */
	protected boolean visible;
	
	protected boolean alive;
	
	protected boolean deletable;
	
	protected boolean gravitating;
	
	protected boolean moveable;
	
	protected boolean collidableWithTilemap;
	
	protected boolean killed;
	
	protected boolean bumped;
	
	protected boolean isAnimated;
	
	protected boolean isAnimationLooping;
	
	protected boolean onFloor;
	
	public AbstractSprite(float x, float y, Vector2 size, Vector2 offset) {
		
		this.setPosition(x, y);		
		this.oldPosition = new Vector2(x, y);
		this.offset = new Vector2(offset.x, offset.y);
		this.setSize(size.x - 2*offset.x, size.y - offset.y);		
		this.renderingSize = new Vector2(size.x, size.y);
		this.bounds = new Rectangle(getX() + offset.x, getY(), getWidth(), getHeight());		
		this.acceleration = new Vector2();
		this.oldAcceleration = new Vector2();
			
		this.mapCollisionEvent = new CollisionEvent();
		this.collidingCells = new ArrayList<TmxCell>();				
		
		this.isAnimationLooping = true;		
		this.xAlive = getX() - ScreenConstants.MAP_UNIT_PIXELS/2 ;
		
		this.initializeAnimations();
		this.isAnimated = true;
		
		this.tilemapCollisionHandler = new BasicTilemapCollisionHandler();				
	}
	
	public AbstractSprite(float x, float y) {		
		this(x, y, new Vector2(1,1), new Vector2());
	}
	
	protected void initTileObjectSprite(MapObject mapObject, Vector2 offset) {
		
		this.offset.x = offset.x;
		this.offset.y = offset.y;
						
		float spriteWidth = (Float) mapObject.getProperties().get("width")/ScreenConstants.MAP_UNIT_PIXELS;
		float spriteHeight = (Float) mapObject.getProperties().get("height")/ScreenConstants.MAP_UNIT_PIXELS;		
		
		setSize(spriteWidth - offset.x * 2, spriteHeight - offset.y);
		this.halfWidth = getWidth()/2;
		this.halfHeight = getHeight()/2;
		setRenderingSize(spriteWidth, spriteHeight);		
		setY(getY()+getHeight());
		bounds=new Rectangle(getX() + offset.x, getY(), getWidth(), getHeight());
		
		String xAliveString = (String) mapObject.getProperties().get("xAlive");
		xAlive = xAliveString!=null ? Float.parseFloat(xAliveString) / ScreenConstants.MAP_UNIT_PIXELS : getX() - ScreenConstants.NB_HORIZONTAL_TILES/2 - 1;
		String yAliveString = (String) mapObject.getProperties().get("yAlive");
		yAlive = yAliveString!=null ? Float.parseFloat(yAliveString) / ScreenConstants.MAP_UNIT_PIXELS : getY() - ScreenConstants.NB_VERTICAL_TILES/2 - 1;
	}
		
	public Rectangle getBounds() {
        return this.bounds;
    }
	
	public abstract void initializeAnimations();

	public void update(TmxMap tileMap, OrthographicCamera camera, float deltaTime) {
							
		if (alive) {
			// The sprite is alive, we first update its animation			
			updateAnimation(deltaTime);
			if (getActions().size>0) {
				// If sprite is acting (ex: sprite is an enemy, and has just been killed, an animation is playing to simulate its death) 
				act(deltaTime);
			} else {
				// else sprite is not acting
				if (isMoveable()) {
					// if sprite is moveable, we move it
					move(deltaTime);
				}
				if (isCollidableWithTilemap()) {
					// if sprite collides with tilemap, collide it
					tilemapCollisionHandler.collideWithTilemap(tileMap, this);
				}				
			}
			// Update sprite bounds (for future collisions)
			updateBounds();
			// Update visible / deletable booleans	
			visible = Math.abs(getX() - camera.position.x)<=11 && Math.abs(getY() - camera.position.y)<=9;
		} else {			
			alive = camera.position.x>=xAlive && camera.position.y>=yAlive;							
		}				
	}

	public void collideWithTilemap(TmxMap tilemap) {
		tilemapCollisionHandler.collideWithTilemap(tilemap, this);
	}
	
	protected void updateBounds() {
		bounds.setX(getX()+offset.x);
		bounds.setY(getY());
	}
	
	protected void applyGravity() {
		if (isGravitating()) {
			this.acceleration.y = this.acceleration.y > -0.5f ? this.acceleration.y - GRAVITY_COEF : -0.5f;
		}		
	}

	protected void updateAnimation(float delta) {
		if (isAnimated) {
			stateTime = stateTime + delta;
			currentFrame = currentAnimation.getKeyFrame(stateTime, isAnimationLooping);
		}				
	}
	
	protected void storeOldPosition() {
		oldPosition.x = getX();
		oldPosition.y = getY();		
		oldAcceleration.x = acceleration.x;
		oldAcceleration.y = acceleration.y;
	}
	
	public void move(float deltaTime) {
		
		storeOldPosition();
		
		float xVelocity = deltaTime * acceleration.x;
		xVelocity = direction == DirectionEnum.LEFT ? -xVelocity : xVelocity;
		setX(getX() + xVelocity);
		
		applyGravity();
		setY(getY() + acceleration.y);		
	}
	
	public void render(Batch batch) {
		batch.begin();		
		batch.draw(currentFrame, getX(), getY(), renderingSize.x, renderingSize.y);
		batch.end();
	}

	public void reinitMapCollisionEvent() {				
		mapCollisionEvent.setCollidingTopLeft(false);
		mapCollisionEvent.setCollidingTopRight(false);		
		mapCollisionEvent.setCollidingBottomLeft(false);
		mapCollisionEvent.setCollidingBottomRight(false);
		mapCollisionEvent.setCollidingMiddleLeft(false);
		mapCollisionEvent.setCollidingMiddleRight(false);		
	}	

	/** Getters / Setters */
	public DirectionEnum getDirection() {
		return direction;
	}

	public void setDirection(DirectionEnum direction) {
		this.direction = direction;
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2 acceleration) {
		this.acceleration = acceleration;
	}

	public Animation getKilledAnimation() {
		return killedAnimation;
	}

	public void setKilledAnimation(Animation killedAnimation) {
		this.killedAnimation = killedAnimation;
	}

	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public boolean isOnFloor() {
		return onFloor;
	}

	public void setOnFloor(boolean onFloor) {
		this.onFloor = onFloor;
	}

	public CollisionEvent getMapCollisionEvent() {
		return mapCollisionEvent;
	}

	public void setMapCollisionEvent(CollisionEvent mapCollisionEvent) {
		this.mapCollisionEvent = mapCollisionEvent;
	}

	public Animation getCurrentAnimation() {
		return currentAnimation;
	}

	public void setCurrentAnimation(Animation currentAnimation) {
		this.currentAnimation = currentAnimation;
	}

	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(TextureRegion currentFrame) {
		this.currentFrame = currentFrame;
	}

	public Vector2 getRenderingSize() {
		return renderingSize;
	}

	public void setRenderingSize(Vector2 renderingSize) {
		this.renderingSize = renderingSize;
	}

	public Texture getSpriteSheet() {
		return spriteSheet;
	}

	public void setSpriteSheet(Texture spriteSheet) {
		this.spriteSheet = spriteSheet;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public boolean isKilled() {
		return killed;
	}

	public void setKilled(boolean killed) {
		this.killed = killed;
	}
		
	public void kill() {
		this.killed = true;			
	}

	public boolean isCollidableWithTilemap() {
		return collidableWithTilemap;
	}

	public void setCollidableWithTilemap(boolean collidableWithTilemap) {
		this.collidableWithTilemap = collidableWithTilemap;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public boolean isGravitating() {
		return gravitating;
	}

	public List<TmxCell> getCollidingCells() {
		return collidingCells;
	}

	public void setCollidingCells(List<TmxCell> collidingCells) {
		this.collidingCells = collidingCells;
	}
	
	public void addCollidingCell(TmxCell tmxCell) {
		collidingCells.add(tmxCell);
	}

	public void setGravitating(boolean gravitating) {
		this.gravitating = gravitating;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public float getxAlive() {
		return xAlive;
	}

	public void setxAlive(float xAlive) {
		this.xAlive = xAlive;
	}
	
	
	public Vector2 getOldPosition() {
		return oldPosition;
	}

	public void setOldPosition(Vector2 oldPosition) {
		this.oldPosition = oldPosition;
	}

	public boolean isDeletable() {
		return deletable;
	}

	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	public Vector2 getOffset() {
		return offset;
	}

	public void setOffset(Vector2 offset) {
		this.offset = offset;
	}	

	public void setRenderingSize(float px, float py) {
		renderingSize.x = px;
		renderingSize.y = py;
	}
	
	public void dispose() {
		spriteSheet.dispose();		
	}

	public void bump() {
		killed = true;
		bumped = true;
	}

	public boolean isBumped() {
		return bumped;
	}

	public void setBumped(boolean bumped) {
		this.bumped = bumped;
	}

	public Vector2 getOldAcceleration() {
		return oldAcceleration;
	}

	public void setOldAcceleration(Vector2 oldAcceleration) {
		this.oldAcceleration = oldAcceleration;
	}

	public SpriteMoveEnum getState() {
		return state;
	}

	public void setState(SpriteMoveEnum state) {
		this.state = state;
	}

	public Polygon getPolygonBounds() {
		return polygonBounds;
	}

	public void setPolygonBounds(Polygon polygonBounds) {
		this.polygonBounds = polygonBounds;
	}
	
	public boolean overlaps(AbstractSprite sprite) {
		return RectangleUtil.overlaps(this.getBounds(), sprite.getBounds());	
	}
	
	public static void updateCommonStateTime(float delta) {		
		commonStateTime = commonStateTime>10 ? 0 : commonStateTime + delta;
		
	}

	public ITilemapCollisionHandler getTilemapCollisionHandler() {
		return tilemapCollisionHandler;
	}

	public void setTilemapCollisionHandler(ITilemapCollisionHandler tilemapCollisionHandler) {
		this.tilemapCollisionHandler = tilemapCollisionHandler;
	}

	public float getHalfWidth() {
		return halfWidth;
	}

	public void setHalfWidth(float halfWidth) {
		this.halfWidth = halfWidth;
	}

	public float getHalfHeight() {
		return halfHeight;
	}

	public void setHalfHeight(float halfHeight) {
		this.halfHeight = halfHeight;
	}

}
