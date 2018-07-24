package com.game.core.sprite.impl.player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.game.core.collision.tilemap.impl.PlayerTilemapCollisionHandler;
import com.game.core.sprite.tileobject.AbstractTileObjectSprite;
import com.game.core.tilemap.TmxMap;
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
	
	private Animation jumpHitRightAnimation;
	
	private Animation jumpHitLeftAnimation;
	
	private Animation idleAnimation;
	
	private Animation cryAnimation;
	
	private boolean attacking;
	
	private float idleTimeCount;
	
	private Club club;
	
	private boolean isCrying;
	
	private boolean isStuckToLiana;

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
		
		club = new Club(getX(), getY(), this);
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
		jumpHitRightAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{34,35,36,37,38,39}, 20, 0.06f);
		jumpHitLeftAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{40,41,42,43,44,45}, 20, 0.06f);
		cryAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{46,47,48}, 20, 0.06f);
		idleAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{49,50,51,50,51,50,51,50}, 20, 0.3f);
		currentAnimation = idleAnimationRight;				
	}

	public void render(Batch batch) {
		if (isInvincible()) {
			batch.setColor(1, 1, 1, 0.5f);
			draw(batch);
			batch.setColor(1, 1, 1, 1);
		}  else {
			draw(batch);
		}				
	}
	
	private void draw(Batch batch) {
		batch.begin();
		club.render(batch);
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
	
	public void update(TmxMap tileMap, OrthographicCamera camera, float deltaTime) {
		if (isMoveable()) {
			move(deltaTime);
		}
		if (isCollidableWithTilemap()) {
			tilemapCollisionHandler.collideWithTilemap(tileMap, this);
		}
		if (!isStuckToLiana) {
			updateBounds();
		}
		updateInvincibleStatus(deltaTime);
		updateTimeCounters(deltaTime);
		updateAnimation(deltaTime);
	}
	
	private void updateTimeCounters(float deltaTime) {
		stateTime = stateTime > 240 ? 0 : stateTime + deltaTime;		
		idleTimeCount = state == SpriteMoveEnum.IDLE ? idleTimeCount + deltaTime : 0;				
	}

	private void updateInvincibleStatus(float deltaTime) {
		if (isInvincible) {
			invincibleTimeCount+=deltaTime;
			isInvincible = invincibleTimeCount<=3;
		}
	}
		
	public void updateAnimation(float delta) {

		boolean isLoopingAnimation = true;
		
		if (isCrying) {
			if (currentAnimation!=cryAnimation) {				
				stateTime = 0;
				currentAnimation = cryAnimation;				
			}						
			isCrying = !currentAnimation.isAnimationFinished(stateTime);
			if (!isCrying) {
				setMoveable(true);
			}
		} else if (idleTimeCount>=10f) {
			if (currentAnimation!=idleAnimation) {
				stateTime = 0;
				currentAnimation = idleAnimation;
			}			
		} else {
			if (onFloor && (currentAnimation==jumpHitRightAnimation || currentAnimation==jumpHitLeftAnimation)) {
				setAttacking(false);
			}
			
			if (isAttacking()) {
				if (onFloor) {
					currentAnimation = direction == DirectionEnum.RIGHT ? hitRightAnimation : hitLeftAnimation;				
				} else {
					currentAnimation = direction == DirectionEnum.RIGHT ? jumpHitRightAnimation : jumpHitLeftAnimation;
				}
				isLoopingAnimation = false;
				if (onFloor && currentAnimation.isAnimationFinished(stateTime)) {
					setAttacking(false);
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
		}		
		currentFrame = currentAnimation.getKeyFrame(stateTime, isLoopingAnimation);		
	}
	
	public void changeState(SpriteMoveEnum pstate) {
		if (state != SpriteMoveEnum.FALLING && state != SpriteMoveEnum.JUMPING ) {
			this.state = pstate;
		}
	}
	
	public void land() {
		this.state = direction==DirectionEnum.LEFT ? SpriteMoveEnum.RUNNING_LEFT : SpriteMoveEnum.RUNNING_RIGHT;
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

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isCrying() {
		return isCrying;
	}

	public void setCrying(boolean isCrying) {
		this.isCrying = isCrying;
	}

	public boolean isStuckToLiana() {
		return isStuckToLiana;
	}

	public void setStuckToLiana(boolean isStuckToLiana) {
		this.isStuckToLiana = isStuckToLiana;
	}		
	
}
