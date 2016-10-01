package com.game.core.camera.impl;

import com.badlogic.gdx.math.Vector2;
import com.game.core.camera.AbstractGameCamera;
import com.game.core.sprite.AbstractSprite;

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
				if (move > 0) {
					camera.position.x = camera.position.x + move;
				} else {
					cameraOffset = cameraOffset + move;
				}
			}
			if (followedSprite.getX() < camera.position.x - CAMERA_OFFSET_MIN) {					
				followedSprite.setX(followedSprite.getOldPosition().x);
				followedSprite.getAcceleration().x = 0;
				cameraOffset = cameraOffset - move;
			}
		}
				
		// Adjust camera vertically
		if (this.isScrollableVertically()) {
			camera.position.y = followedSprite.getY() >= mapDimensions.y - 6 ? mapDimensions.y - 6 : followedSprite.getY() >= this.initialY ? followedSprite.getY() : initialY;
		}
		
		camera.update();
	}

}
