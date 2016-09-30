package com.game.core.background.impl;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.game.core.camera.GameCamera;
import com.game.core.sprite.AbstractSprite;
import com.game.core.util.enums.BackgroundTypeEnum;

/**
 * Left to right scrolling background support
 *
 */
public class RightScrollingBackground extends AbstractScrollingBackground {

	public RightScrollingBackground(GameCamera camera, AbstractSprite followedSprite, Batch batch, BackgroundTypeEnum backgroundType, float pvelocity) {
		super(camera, followedSprite, batch, backgroundType);		
		setX(0);
		this.velocity = pvelocity;
	}

}
