package com.game.core.camera.impl;

import com.badlogic.gdx.math.Vector2;
import com.game.core.camera.AbstractGameCamera;
import com.game.core.sprite.AbstractSprite;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.CameraEnum;

public class FreeGameCamera extends AbstractGameCamera {

	public FreeGameCamera(AbstractSprite followedSprite, Vector2 mapDimensions) {
		super(followedSprite, mapDimensions);		
	}
	
	public void moveCamera() {
		
		// Adjust camera horizontally
		if (this.isScrollableHorizontally()) {
			
			float move = followedSprite.getX() - followedSprite.getOldPosition().x;				
			
			if (followedSprite.getX() + followedSprite.getOffset().x < 0) {				
				followedSprite.setX(followedSprite.getOldPosition().x);
				followedSprite.getAcceleration().x = 0;				
			} else if (followedSprite.getX() < ScreenConstants.HORIZONTAL_HALF_POSITION) {
				cameraOffset = cameraOffset + move;
			}  else if (followedSprite.getX() > mapDimensions.x - ScreenConstants.FREE_CAMERA_MAX_SCROLL) {
				cameraOffset = cameraOffset + move;
			} else if (cameraOffset < CAMERA_OFFSET_MAX) {
				cameraOffset = cameraOffset + move;
			} else {
				if (cameraOffset!=ScreenConstants.HORIZONTAL_HALF_POSITION) {
					float diff = CAMERA_OFFSET_MAX - cameraOffset;
					cameraOffset = ScreenConstants.HORIZONTAL_HALF_POSITION;
					camera.position.x = camera.position.x - diff;
				}				
				camera.position.x = camera.position.x + move;
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
		return CameraEnum.FREE;
	}

}
