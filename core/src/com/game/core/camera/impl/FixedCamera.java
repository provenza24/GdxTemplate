package com.game.core.camera.impl;

import com.badlogic.gdx.math.Vector2;
import com.game.core.camera.AbstractGameCamera;
import com.game.core.sprite.AbstractSprite;
import com.game.core.util.enums.CameraEnum;

public class FixedCamera extends AbstractGameCamera {

	public FixedCamera(AbstractSprite followedSprite, Vector2 mapDimensions) {
		super(followedSprite, mapDimensions);		
	}
	
	public void moveCamera() {		
	}

	@Override
	public CameraEnum getCameraType() {		
		return CameraEnum.FREE;
	}

}
