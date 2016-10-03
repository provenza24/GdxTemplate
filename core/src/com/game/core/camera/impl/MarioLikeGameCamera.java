package com.game.core.camera.impl;

import com.badlogic.gdx.math.Vector2;
import com.game.core.camera.AbstractGameCamera;
import com.game.core.sprite.AbstractSprite;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.CameraEnum;

public class MarioLikeGameCamera extends AbstractGameCamera {

	public MarioLikeGameCamera(AbstractSprite followedSprite, Vector2 mapDimensions) {
		super(followedSprite, mapDimensions);
	}
	
	@Override
	public void moveCamera() {
		
		// Adjust camera horizontally
		if (this.isScrollableHorizontally()) {
			float move = followedSprite.getX() - followedSprite.getOldPosition().x;				
			
			if (cameraOffset < CAMERA_OFFSET_MAX) {
				cameraOffset = cameraOffset + move;
			} else {
				cameraOffset = CAMERA_OFFSET_MAX;
				if (move > 0) {
					camera.position.x = camera.position.x + move;
				} else {
					cameraOffset = cameraOffset + move;
				}
			}		
			if (cameraOffset<=0) {
				cameraOffset = 0;
				followedSprite.setX(getCamera().position.x - CAMERA_OFFSET_MIN);
				followedSprite.getAcceleration().x = 0;
			}
		}
				
		// Adjust camera vertically
		if (this.isScrollableVertically()) {
			camera.position.y = followedSprite.getY() >= mapDimensions.y - ScreenConstants.VERTICAL_HALF_POSITION ? 
					mapDimensions.y - ScreenConstants.VERTICAL_HALF_POSITION : followedSprite.getY() >= this.initialY ? followedSprite.getY() : initialY;
		}
		
		camera.update();
	}
	
	@Override
	public CameraEnum getCameraType() {		
		return CameraEnum.MARIO_LIKE;
	}

}
