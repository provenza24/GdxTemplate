package com.game.core.background.impl;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.game.core.background.IScrollingBackground;
import com.game.core.camera.GameCamera;
import com.game.core.sprite.AbstractSprite;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.BackgroundTypeEnum;

/**
 * Abstract class which provides scrolling support
 * Provided images width must be equals to two times the screen width
 * A velocity is given to backgrounds to temporize the automatic scrolling
 */
public abstract class AbstractScrollingBackground extends Sprite implements IScrollingBackground {

	/** Map containing backgrounds images */
	protected static final Map<BackgroundTypeEnum, Texture> BACKGROUND_IMAGES = new HashMap<BackgroundTypeEnum, Texture>();
	
	/** Horizontal velocity */
	protected float velocity;
	
	/** Batch used to draw this background */
	protected Batch batch;
	
	/** Camera */
	protected GameCamera camera;
	
	/** Followed sprite */
	protected AbstractSprite followedSprite;
	
	/** Background width */
	protected int width;
	
	/** Indicator to determine if background is enabled / disabled */
	protected boolean enabled;
	
	/** Background can scroll horizontally */
	protected boolean scrollableHorizontally;
	
	/** Background can scroll vertically */
	protected boolean scrollableVertically;
		
	static {
		BACKGROUND_IMAGES.put(BackgroundTypeEnum.FOREST, ResourcesLoader.getBackgroundForest());
	}
		
	public AbstractScrollingBackground(GameCamera camera, AbstractSprite followedSprite, Batch batch, BackgroundTypeEnum backgroundType) {
		super(BACKGROUND_IMAGES.get(backgroundType));
		this.batch = batch;
		this.followedSprite = followedSprite;
		this.enabled = true;
		this.width = getTexture().getWidth();	
		this.scrollableHorizontally = camera.isScrollableHorizontally();
		this.scrollableVertically = camera.isScrollableVertically();
		this.camera = camera;
	}
			
	@Override
	public void update() {
					
		if (scrollableHorizontally && Math.floor(camera.getCameraOffset()) == 8) {
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
	
	@Override
	public void render() {		
		if (enabled) {
			batch.begin();
			// draw the first background
			batch.draw(getTexture(), getX() - width, getY());
			// draw the second background
			batch.draw(getTexture(), getX(), getY());
			batch.end();
		}		
	}

	@Override
	public void toggleEnabled() {
		enabled = !enabled;
	}
	
}
