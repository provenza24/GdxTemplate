package com.game.core.sprite.impl.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.tileobject.AbstractTileObjectSprite;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.animation.AnimationBuilder;
import com.game.core.util.enums.DirectionEnum;
import com.game.core.util.enums.SpriteMoveEnum;

public class Player extends AbstractTileObjectSprite {

	private SpriteMoveEnum previousState;
	
	private Vector2 move = new Vector2();
	
	private Animation runningRightAnimation;
	
	private Animation runningLeftAnimation;
	
	private Animation runningUpAnimation;
	
	private Animation runningDownAnimation;
	
	private boolean isMoving = false;

	public Player(MapObject mapObject) {
		super(mapObject, new Vector2());				
		//direction = DirectionEnum.RIGHT;
		state = SpriteMoveEnum.IDLE;
		gravitating = false;
		collidableWithTilemap = false;
		alive = true;
		moveable = false;			
	}

	@Override
	public void initializeAnimations() {
		spriteSheet = ResourcesLoader.PLAYER;
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight()/4);
		runningRightAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{0,1}, 2, 0.075f);
		runningDownAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{2,3}, 2, 0.075f);
		runningLeftAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{4,5}, 2, 0.075f);
		runningUpAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{6,7}, 2, 0.075f);
		currentAnimation = runningRightAnimation;
	}

	public void render(Batch batch) {
		batch.begin();
		batch.draw(currentFrame, getX(), getY(), renderingSize.x, renderingSize.y);		
		batch.end();
	}
	
	public void updateAnimation(float delta) {
		stateTime = stateTime > 10 ? 0 : stateTime + delta;
		
		if (direction==DirectionEnum.RIGHT) {
			currentAnimation = runningRightAnimation;
		} else if (direction==DirectionEnum.LEFT) {
			currentAnimation = runningLeftAnimation;
		} else if (direction==DirectionEnum.UP) {
			currentAnimation = runningUpAnimation;
		} else if (direction==DirectionEnum.DOWN) {
			currentAnimation = runningDownAnimation;
		}
		
		currentFrame = currentAnimation.getKeyFrame(stateTime, true);		
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
		
}
