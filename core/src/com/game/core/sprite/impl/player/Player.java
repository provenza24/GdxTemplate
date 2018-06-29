package com.game.core.sprite.impl.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.game.core.collision.tilemap.impl.PlayerTilemapCollisionHandler;
import com.game.core.sprite.tileobject.AbstractTileObjectSprite;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.animation.AnimationBuilder;
import com.game.core.util.enums.DirectionEnum;
import com.game.core.util.enums.SpriteMoveEnum;

public class Player extends AbstractTileObjectSprite {

	private static final float X_OFFSET = 0.4f;
	
	private static final float Y_OFFSET = 0.2f;	

	private static final float ACCELERATION_COEF = 0.2f;
	
	private static final float ACCELERATION_MAX = 5f; // 7.5f;

	private static final float DECELERATION_COEF = 0.2f;	
	
	private SpriteMoveEnum previousState;
	
	private Vector2 move = new Vector2();
	
	private Animation idleAnimationRight;
	
	private Animation idleAnimationLeft;
	
	private Animation runningRightAnimation;
	
	private Animation runningLeftAnimation;
	
	private Animation jumpLeftAnimation;
	
	private Animation jumpRightAnimation;
	
	private Animation hitRightAnimation;
	
	private Animation hitLeftAnimation;
	
	private boolean onCurvedTile;
	
	private boolean onCloudTile;
	
	private boolean hiting;

	public Player(MapObject mapObject) {
		super(mapObject, new Vector2(X_OFFSET,Y_OFFSET));				
		direction = DirectionEnum.RIGHT;
		state = SpriteMoveEnum.IDLE;
		onFloor = true;
		gravitating = true;
		collidableWithTilemap = true;
		alive = true;
		moveable = true;
		tilemapCollisionHandler = new PlayerTilemapCollisionHandler();		
	}

	@Override
	public void initializeAnimations() {
		spriteSheet = ResourcesLoader.PLAYER;
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/20, spriteSheet.getHeight()/15);
		idleAnimationRight = AnimationBuilder.getInstance().build(textureRegions, new int[]{1}, 20, 1f);
		idleAnimationLeft = AnimationBuilder.getInstance().build(textureRegions, new int[]{2}, 20, 1f);
		runningRightAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{3,4,5,6,7,8}, 20, 0.075f);
		runningLeftAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{9,10,11,12,13,14}, 20, 0.075f);
		jumpRightAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{15,16,17,18}, 20, 0.15f);
		jumpLeftAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{20,21,22,23}, 20, 0.15f);
		hitRightAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{24,25,26,27,28}, 20, 0.075f);
		hitLeftAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{29,30,31,32,33}, 20, 0.075f);
		currentAnimation = idleAnimationRight;
	}

	public void render(Batch batch) {
		batch.begin();
		batch.draw(currentFrame, getX(), getY(), renderingSize.x, renderingSize.y);
		batch.end();
	}
	
	public void accelerate() {
		if (this.acceleration.x < ACCELERATION_MAX) {
			this.acceleration.x = this.acceleration.x + ACCELERATION_COEF;
		}
		if (this.acceleration.x > ACCELERATION_MAX) {
			this.acceleration.x = ACCELERATION_MAX;
		}
	}

	public void decelerate(float rate) {
		if (this.acceleration.x > 0) {
			this.acceleration.x = this.acceleration.x - DECELERATION_COEF * rate;
		}
		if (this.acceleration.x < 0) {
			this.acceleration.x = 0;
		}
	}
	
	public void updateAnimation(float delta) {

		boolean isLoopingAnimation = true;
		
		stateTime = stateTime > 10 ? 0 : stateTime + delta;

		if (isHiting()) {
			currentAnimation = direction == DirectionEnum.RIGHT ? hitRightAnimation : hitLeftAnimation;
			isLoopingAnimation = false;
			if (currentAnimation.isAnimationFinished(stateTime)) {
				setHiting(false);
			}			
		} else if (!onFloor) {
			currentAnimation = direction == DirectionEnum.RIGHT ? jumpRightAnimation : jumpLeftAnimation;
			isLoopingAnimation = false;
		} else {
			float xMove = getX() - getOldPosition().x;
			if (xMove == 0) {	
				if (getState() == SpriteMoveEnum.SLIDING_LEFT) {
					setDirection(DirectionEnum.RIGHT);
				} else if (getState() == SpriteMoveEnum.SLIDING_RIGHT) {
					setDirection(DirectionEnum.LEFT);
				}
				setState(SpriteMoveEnum.IDLE);
			}			
			currentAnimation = 						
				state == SpriteMoveEnum.IDLE ? direction == DirectionEnum.RIGHT ? idleAnimationRight : idleAnimationLeft :
					state == SpriteMoveEnum.RUNNING_RIGHT || state == SpriteMoveEnum.SLIDING_RIGHT ? runningRightAnimation :
						state == SpriteMoveEnum.RUNNING_LEFT || state == SpriteMoveEnum.SLIDING_LEFT ? runningLeftAnimation :							
							idleAnimationRight;
		}
		currentFrame = currentAnimation.getKeyFrame(stateTime, isLoopingAnimation);		
	}
	
	public void changeState(SpriteMoveEnum pstate) {
		if (state != SpriteMoveEnum.FALLING && state != SpriteMoveEnum.JUMPING ) {
			this.state = pstate;
		}
	}

	public SpriteMoveEnum getPreviousState() {
		return previousState;
	}

	public void setPreviousState(SpriteMoveEnum previousState) {
		this.previousState = previousState;
	}

	public Vector2 getMove() {
		return move;
	}

	public void setMove(Vector2 move) {
		this.move = move;
	}

	public boolean isOnCloudTile() {
		return onCloudTile;
	}

	public void setOnCloudTile(boolean onCloudTile) {
		this.onCloudTile = onCloudTile;
	}

	public boolean isOnCurvedTile() {
		return onCurvedTile;
	}

	public void setOnCurvedTile(boolean onCurvedTile) {
		this.onCurvedTile = onCurvedTile;
	}

	public boolean isHiting() {
		return hiting;
	}

	public void setHiting(boolean hiting) {
		this.hiting = hiting;
	}
}
