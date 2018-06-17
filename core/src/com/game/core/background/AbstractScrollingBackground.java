package com.game.core.background;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.game.core.background.impl.FreeScrollingBackground;
import com.game.core.background.impl.MarioLikeScrollingBackground;
import com.game.core.camera.AbstractGameCamera;
import com.game.core.sprite.AbstractSprite;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.BackgroundTypeEnum;
import com.game.core.util.enums.CameraEnum;

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
	protected AbstractGameCamera camera;
	
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
		
	protected static final float CAMERA_OFFSET_START_SCROLL = ScreenConstants.NB_HORIZONTAL_TILES / 2 - 0.5f; 
		
	static {
		BACKGROUND_IMAGES.put(BackgroundTypeEnum.FOREST, ResourcesLoader.getBackgroundForest());
		BACKGROUND_IMAGES.put(BackgroundTypeEnum.SUBURBS, ResourcesLoader.getBackgroundSuburbs());
	}
		
	public AbstractScrollingBackground(AbstractGameCamera camera, AbstractSprite followedSprite, Batch batch, BackgroundTypeEnum backgroundType) {
		super(BACKGROUND_IMAGES.get(backgroundType));
		this.batch = batch;
		this.followedSprite = followedSprite;
		this.enabled = true;
		this.width = getTexture().getWidth();	
		this.scrollableHorizontally = camera.isScrollableHorizontally();
		this.scrollableVertically = camera.isScrollableVertically();
		this.camera = camera;
		this.setY(ScreenConstants.HEIGHT - BACKGROUND_IMAGES.get(backgroundType).getHeight());
	}
	
	public static AbstractScrollingBackground createScrollingBackground(AbstractGameCamera camera, AbstractSprite followedSprite, Batch batch, BackgroundTypeEnum backgroundType, float velocity) {
		return camera.getCameraType()==CameraEnum.FREE ? 
				new FreeScrollingBackground(camera, followedSprite, batch, backgroundType, velocity) 
				: new MarioLikeScrollingBackground(camera, followedSprite, batch, backgroundType, velocity);			
	}
			
	@Override
	public abstract void update();					
	
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
