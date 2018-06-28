package com.game.core.background.impl;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.game.core.background.AbstractScrollingBackground;
import com.game.core.camera.AbstractGameCamera;
import com.game.core.sprite.AbstractSprite;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.BackgroundTypeEnum;

/**
 * Right to left scrolling image support
 */
public class MarioLikeScrollingBackground extends AbstractScrollingBackground {

	public MarioLikeScrollingBackground(AbstractGameCamera camera, AbstractSprite followedSprite, Batch batch, BackgroundTypeEnum backgroundType, float pvelocity, boolean isScrollingVertically) {
		super(camera, followedSprite, batch, backgroundType, isScrollingVertically);		
		setX(width);
		this.velocity = -pvelocity;
	}

	@Override
	public void update() {
					
		
		if (scrollableHorizontally && camera.getCameraOffset() == CAMERA_OFFSET_START_SCROLL) {
			float xPlayerMove = (followedSprite.getX() - followedSprite.getOldPosition().x);
			if (xPlayerMove>0) {
				// Scroll only if player is going to the right of the screen
				setX(getX() + xPlayerMove * velocity);			
			}				
			// Reset image position when needed
			if (getX() <= 0){			
				setX(width);
			} else if (getX()>=width) {
				setX(0);
			}
		}
				
		if (scrollableVertically) {
			setY(followedSprite.getY()> camera.getMapDimensions().y - 6 ? - (camera.getMapDimensions().y-12) * ScreenConstants.MAP_UNIT_PIXELS 
				:  followedSprite.getY() > 6 ? (6 - followedSprite.getY()) * ScreenConstants.MAP_UNIT_PIXELS : 0);
		}
	}
}
