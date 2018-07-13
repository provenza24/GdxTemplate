package com.game.core.sprite.impl.ennemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.AbstractSfxSprite;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.animation.AnimationBuilder;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.DirectionEnum;

public class DeadCaveman extends AbstractSfxSprite {
	
	public DeadCaveman(float x, float y, DirectionEnum direction) {
		super(x, y);	
		moveable = true;
		gravitating = true;
		isAnimationLooping = false;
		setDirection(direction);
		setAcceleration(new Vector2(5f, 0.25f));
		setSize((spriteSheet.getWidth()/2)/ScreenConstants.MAP_UNIT_PIXELS, (spriteSheet.getHeight())/ScreenConstants.MAP_UNIT_PIXELS);
		setRenderingSize((spriteSheet.getWidth()/2)/ScreenConstants.MAP_UNIT_PIXELS, (spriteSheet.getHeight())/ScreenConstants.MAP_UNIT_PIXELS);
	}

	@Override
	public void initializeAnimations() {
		spriteSheet = ResourcesLoader.CAVEMAN_DEAD;			
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight());		
		currentAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{0,1}, 2, 0.2f);				
	}

}
