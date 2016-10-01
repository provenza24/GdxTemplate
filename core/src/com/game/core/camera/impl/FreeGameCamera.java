package com.game.core.camera.impl;

import com.badlogic.gdx.math.Vector2;
import com.game.core.camera.AbstractGameCamera;
import com.game.core.sprite.AbstractSprite;

public class FreeGameCamera extends AbstractGameCamera {

	public FreeGameCamera(AbstractSprite followedSprite, Vector2 mapDimensions) {
		super(followedSprite, mapDimensions);		
	}
	
	public void moveCamera() {
		
		// Adjust camera horizontally
		if (this.isScrollableHorizontally()) {
			
			float move = followedSprite.getX() - followedSprite.getOldPosition().x;				
			
			if (followedSprite.getX() < 0) {
				followedSprite.setX(followedSprite.getOldPosition().x);
				followedSprite.getAcceleration().x = 0;				
			} else if (followedSprite.getX() < 7.5) {
				cameraOffset = cameraOffset + move;
			}  else if (followedSprite.getX() > mapDimensions.x - 8.5) {
				cameraOffset = cameraOffset + move;
			} else if (cameraOffset < CAMERA_OFFSET_MAX) {
				cameraOffset = cameraOffset + move;
			} else {
				if (cameraOffset!=7.5) {
					float diff = CAMERA_OFFSET_MAX - cameraOffset;
					cameraOffset = 7.5f;
					camera.position.x = camera.position.x - diff;
				}				
				camera.position.x = camera.position.x + move;
			}
						
		}
				
		// Adjust camera vertically
		if (this.isScrollableVertically()) {
			camera.position.y = followedSprite.getY() >= mapDimensions.y - 6 ? mapDimensions.y - 6 : followedSprite.getY() >= this.initialY ? followedSprite.getY() : initialY;
		}
		
		camera.update();
	}

}
