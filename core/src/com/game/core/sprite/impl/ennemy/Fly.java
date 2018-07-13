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

public class Fly extends AbstractTileObjectEnemy {

	private Animation flyLeftAnimation;
	
	public Fly(MapObject mapObject, Vector2 offset) {
		super(mapObject, offset);
		moveable = true;
		collidableWithTilemap = false;
		gravitating = false;	
		this.acceleration.x = 2f;
		this.direction = DirectionEnum.LEFT;			
	}

	@Override
	public void initializeAnimations() {		
		spriteSheet = ResourcesLoader.FLY;			
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth(), spriteSheet.getHeight()/4);		
		flyLeftAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{0,1}, 1, 0.07f);					
		currentAnimation = flyLeftAnimation;
	}

	@Override
	public AbstractSprite generateDeadSprite(DirectionEnum playerDirection) {		
		return new DeadFly(getX(), getY(), getDirection(), playerDirection);
	}
	
}
