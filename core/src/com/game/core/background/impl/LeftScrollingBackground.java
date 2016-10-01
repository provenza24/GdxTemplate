package com.game.core.background.impl;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.game.core.camera.AbstractGameCamera;
import com.game.core.sprite.AbstractSprite;
import com.game.core.util.enums.BackgroundTypeEnum;

/**
 * Right to left scrolling image support
 */
public class LeftScrollingBackground extends AbstractScrollingBackground {

	public LeftScrollingBackground(AbstractGameCamera camera, AbstractSprite followedSprite, Batch batch, BackgroundTypeEnum backgroundType, float pvelocity) {
		super(camera, followedSprite, batch, backgroundType);		
		setX(width);
		this.velocity = -pvelocity;
	}

}
