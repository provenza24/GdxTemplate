package com.game.core.sprite.impl.ennemy.dinosaurman;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.AbstractSfxSprite;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.enums.DirectionEnum;

public class DinosaurManMask extends AbstractSfxSprite {

	public DinosaurManMask(float x, float y, DirectionEnum direction) {
		super(x, y);	
		moveable = true;
		gravitating = true;
		isAnimated = false;
		setDirection(direction);
		setAcceleration(new Vector2(0, 0.25f));				
		currentFrame =  TextureRegion.split(spriteSheet, spriteSheet.getWidth(), spriteSheet.getHeight())[0][0];
	}

	@Override
	public void initializeAnimations() {
		spriteSheet = ResourcesLoader.DINOSAURMAN_MASK;										
	}
	
}
