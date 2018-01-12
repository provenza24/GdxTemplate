package com.game.core.sprite.sfx.wall;

import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.sfx.AbstractSfxSprite;
import com.game.core.util.constants.ScreenConstants;

public abstract class AbstractWallPiece extends AbstractSfxSprite {

	private static final float WIDTH = ScreenConstants.SQUARE_WIDTH/2;
	
	private static final float HEIGHT = ScreenConstants.SQUARE_WIDTH/2;	

	protected static final float X_ACCELERATION_COEFF = 100;
	
	protected static final float Y_ACCELERATION_COEFF = 0.5f;

	public AbstractWallPiece(float x, float y, Vector2 acceleration) {
		super(x, y, new Vector2(WIDTH, HEIGHT));									
		this.acceleration = acceleration;				
		this.moveable = true;		
		this.gravitating = true;		
		this.isAnimationLooping = false;			
	}

	@Override
	public void addAppearAction() {				
	}
	
	@Override
	public void move(float deltaTime) {			
		super.move(deltaTime);
		/*if (acceleration.y>0) {
			acceleration.y -=0.05f;
		} else {
			acceleration.y -=0.005f;
		}*/
	}
	
}
