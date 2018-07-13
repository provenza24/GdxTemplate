package com.game.core.sprite.impl.ennemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.AbstractSprite;
import com.game.core.sprite.tileobject.AbstractTileObjectEnemy;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.animation.AnimationBuilder;
import com.game.core.util.enums.DirectionEnum;

public class Caveman extends AbstractTileObjectEnemy {

	private Animation walkLeftAnimation;
	
	private Animation walkRightAnimation;
	
	private Animation turnAnimation;		
	
	private int X_MOVE = 8;
	
	private float X_MIN_LIMIT;
	
	private float X_MAX_LIMIT;
	
	public Caveman(MapObject mapObject, Vector2 offset) {
		super(mapObject, offset);
		moveable = true;
		collidableWithTilemap = false;
		gravitating = false;	
		this.acceleration.x = 2.5f;
		this.direction = DirectionEnum.LEFT;
		String xMoveString = (String) mapObject.getProperties().get("xMove");
		X_MIN_LIMIT = xMoveString!=null ? getX() - Float.parseFloat(xMoveString) : getX() - X_MOVE;
		X_MAX_LIMIT = getX();		
	}

	@Override
	public void initializeAnimations() {		
		spriteSheet = ResourcesLoader.CAVEMAN;			
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/9, spriteSheet.getHeight());		
		walkLeftAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{0,3,1,2}, 9, 0.07f);			
		walkRightAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{4,7,5,6}, 9, 0.07f);
		turnAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{8}, 9, 0.1f);
		currentAnimation = walkLeftAnimation;
	}

	@Override
	public void move(float deltaTime) {		
		super.move(deltaTime);		
		if ((direction==DirectionEnum.LEFT && getX()<X_MIN_LIMIT) || (direction==DirectionEnum.RIGHT && getX()>X_MAX_LIMIT)) {
			setX(direction==DirectionEnum.LEFT ? X_MIN_LIMIT : X_MAX_LIMIT);
			setDirection(direction==DirectionEnum.LEFT ? DirectionEnum.RIGHT : DirectionEnum.LEFT);
			currentAnimation = turnAnimation;				
			acceleration.x = 0;
			stateTime = 0;
		} 
		if (currentAnimation==turnAnimation && turnAnimation.isAnimationFinished(stateTime)) {						
			currentAnimation = direction==DirectionEnum.RIGHT ? walkRightAnimation : walkLeftAnimation;
			this.acceleration.x = 2f;
		}
	}

	@Override
	public AbstractSprite generateDeadSprite(DirectionEnum directionEnum) {		
		return new DeadCaveman(getX(), getY() , directionEnum);
	}
	
}
