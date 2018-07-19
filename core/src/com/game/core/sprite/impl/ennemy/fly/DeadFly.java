package com.game.core.sprite.impl.ennemy.fly;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.AbstractSfxSprite;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.enums.DirectionEnum;

public class DeadFly extends AbstractSfxSprite {

	public DeadFly(float x, float y, DirectionEnum playerDirection, DirectionEnum flyDirection) {
		super(x, y);	
		moveable = true;
		gravitating = true;
		isAnimated = false;
		setDirection(flyDirection);
		setAcceleration(new Vector2(5f, 0.25f));		
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight());
		currentFrame =  playerDirection==DirectionEnum.LEFT ? textureRegions[0][0] : textureRegions[0][1];
	}

	@Override
	public void initializeAnimations() {
		spriteSheet = ResourcesLoader.FLY_DEAD;										
	}
	
}
