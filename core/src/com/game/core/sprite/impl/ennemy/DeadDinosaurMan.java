package com.game.core.sprite.impl.ennemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.AbstractSfxSprite;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.DirectionEnum;

public class DeadDinosaurMan extends AbstractSfxSprite {

	public DeadDinosaurMan(float x, float y, DirectionEnum direction) {
		super(x, y);	
		moveable = true;
		gravitating = true;
		isAnimated = false;
		setDirection(direction);
		setAcceleration(new Vector2(5f, 0.25f));
		setRenderingSize((spriteSheet.getWidth()/9)/ScreenConstants.MAP_UNIT_PIXELS, (spriteSheet.getHeight()/2)/ScreenConstants.MAP_UNIT_PIXELS);
	}

	@Override
	public void initializeAnimations() {
		spriteSheet = ResourcesLoader.DINOSAURMAN;
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/9, spriteSheet.getHeight()/2);
		currentFrame =  textureRegions[1][8];
	}
	
}
