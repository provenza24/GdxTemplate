package com.game.core.sprite.sfx.venkman;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.sfx.AbstractSfxSprite;
import com.game.core.util.ResourcesLoader;

public class Venkman extends AbstractSfxSprite {

	//private static final float DISP_COEF = (float)(2 / (float)(ScreenConstants.PREFERRED_SQUARE_WIDTH/ScreenConstants.SQUARE_WIDTH));	
	
	private static final float DISP_COEF = 1f;
	
	public Venkman(float x, float y) {
		super(x, y, new Vector2((float)(ResourcesLoader.VENKMAN.getWidth() /2 * DISP_COEF),(float)(ResourcesLoader.VENKMAN.getHeight() * DISP_COEF)));		
	}

	@Override
	public void initializeAnimations() {
		spriteSheet = ResourcesLoader.VENKMAN;
		TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 2, spriteSheet.getHeight() / 1);
		TextureRegion[] frame = new TextureRegion[2];
		frame[0] = tmp[0][0];			
		frame[1] = tmp[0][1];
		currentAnimation = new Animation(0, frame);
	}

}
