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

public class DinausorMan extends AbstractTileObjectEnemy {

	private Animation walkLeftAnimation;		
	
	private Animation walkRightAnimation;
	
	private int X_MOVE = 8;
	
	private float X_MIN_LIMIT;
	
	private float X_MAX_LIMIT;
	
	public DinausorMan(MapObject mapObject, Vector2 offset) {
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
		spriteSheet = ResourcesLoader.DINAUSORMAN;			
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/8, spriteSheet.getHeight());		
		walkLeftAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{0,1,2,3}, 8, 0.07f);			
		walkRightAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{4,5,6,7}, 8, 0.07f);
		currentAnimation = walkLeftAnimation;
	}

	@Override
	public void move(float deltaTime) {		
		super.move(deltaTime);		
		if ((direction==DirectionEnum.LEFT && getX()<X_MIN_LIMIT) || (direction==DirectionEnum.RIGHT && getX()>X_MAX_LIMIT)) {
			setX(direction==DirectionEnum.LEFT ? X_MIN_LIMIT : X_MAX_LIMIT);
			setDirection(direction==DirectionEnum.LEFT ? DirectionEnum.RIGHT : DirectionEnum.LEFT);
			currentAnimation = direction==DirectionEnum.RIGHT ? walkRightAnimation : walkLeftAnimation;			
			acceleration.x = 2.5f;
		} 
	}

	@Override
	public AbstractSprite generateDeadSprite(DirectionEnum directionEnum) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
