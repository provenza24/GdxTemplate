package com.game.core.sprite.sfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.game.core.util.enums.DirectionEnum;

public abstract class AbstractSprite extends Actor implements IMoveable, IDrawable {
		
	protected static float commonStateTime; 
		
	protected float GRAVITY_COEF = 0.2f;

	protected DirectionEnum direction;

	protected Vector2 acceleration;

	protected float stateTime;
	
	protected Animation currentAnimation;

	protected TextureRegion currentFrame;
	
	protected Texture spriteSheet;
	
	protected Vector2 oldPosition;
	
	protected Vector2 oldAcceleration;
	
	protected float xAlive;
	
	protected Rectangle bounds;
	
	protected Polygon polygonBounds;
	
	protected Vector2 renderingSize;
	
	protected String image;
	
	protected Vector2 move = new Vector2();
		
	protected boolean alive;
	
	protected boolean deletable;
	
	protected boolean gravitating;
	
	protected boolean moveable;
		
	protected boolean isAnimationLooping;
	
	protected boolean blendingSprite;
	
	public AbstractSprite(float x, float y, Vector2 size) {
		
		this.setPosition(x, y);		
		this.oldPosition = new Vector2(x, y);
		this.setSize(size.x, size.y);		
		this.renderingSize = new Vector2(size.x, size.y);
		this.bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());		
		this.acceleration = new Vector2();
		this.oldAcceleration = new Vector2();
				
		this.isAnimationLooping = true;		
		this.xAlive = getX() - 16 ;
		
		this.blendingSprite = false;
		
		this.initializeAnimations();				
	}
	
	public AbstractSprite(float x, float y) {		
		this(x, y, new Vector2(1,1));
	}
		
	public Rectangle getBounds() {
        return this.bounds;
    }
	
	public void onDelete() {		
	}
	
	public abstract void initializeAnimations();

	public void update(float deltaTime) {
							
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
			}
			// Update booleans			
			alive = this.getY()>0 - this.getHeight();			
		} else {			
			deletable = true;
		}				
	}
	
	protected void applyGravity() {
		if (isGravitating()) {
			this.acceleration.y = this.acceleration.y - GRAVITY_COEF;
		}		
	}

	protected void updateAnimation(float delta) {
		stateTime = stateTime + delta;
		currentFrame = currentAnimation.getKeyFrame(stateTime, isAnimationLooping);		
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
	
	public void render(SpriteBatch batch) {
		batch.begin();
		//@TODO replace this by computing value at initialization		
		batch.draw(currentFrame, getX(), getY(), renderingSize.x, renderingSize.y);
		batch.end();
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

	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
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

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public boolean isGravitating() {
		return gravitating;
	}

	public void setGravitating(boolean gravitating) {
		this.gravitating = gravitating;
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

	public void setRenderingSize(float px, float py) {
		renderingSize.x = px;
		renderingSize.y = py;
	}
	
	public void dispose() {
		spriteSheet.dispose();		
	}

	public Vector2 getOldAcceleration() {
		return oldAcceleration;
	}

	public void setOldAcceleration(Vector2 oldAcceleration) {
		this.oldAcceleration = oldAcceleration;
	}

	public Vector2 getMove() {
		return move;
	}

	public void setMove(Vector2 move) {
		this.move = move;
	}
	
	public boolean isBlendingSprite() {
		return blendingSprite;
	}

	public void setBlendingSprite(boolean blendingSprite) {
		this.blendingSprite = blendingSprite;
	}

	public static void updateCommonStateTime(float delta) {		
		commonStateTime = commonStateTime>10 ? 0 : commonStateTime + delta;
		
	}	
}
