package com.game.core.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.game.core.camera.impl.FixedCamera;
import com.game.core.camera.impl.FreeGameCamera;
import com.game.core.camera.impl.MarioLikeGameCamera;
import com.game.core.sprite.AbstractSprite;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.CameraEnum;

/**
 * Class used to define an orthographic camera which will follow a chosen sprite
 */
public abstract class AbstractGameCamera implements IGameCamera {

	protected float CAMERA_OFFSET_MAX = (float)(ScreenConstants.NB_HORIZONTAL_TILES/2) - 0.5f;
	
	protected float CAMERA_OFFSET_MIN;

	protected OrthographicCamera camera;
	
	protected float cameraOffset = 0;
	
	protected boolean scrollableHorizontally;
	
	protected boolean scrollableVertically;
	
	protected AbstractSprite followedSprite;
	
	protected float initialY;
	
	protected Vector2 mapDimensions;

	public AbstractGameCamera(AbstractSprite followedSprite, Vector2 mapDimensions) {
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, ScreenConstants.NB_HORIZONTAL_TILES, ScreenConstants.NB_VERTICAL_TILES);
		this.camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		this.camera.update();		
		this.scrollableHorizontally =  mapDimensions.x > ScreenConstants.NB_HORIZONTAL_TILES;				
		this.scrollableVertically = mapDimensions.y > ScreenConstants.NB_VERTICAL_TILES;
		this.followedSprite = followedSprite;						
		this.initialY = camera.viewportHeight / 2f;
		this.mapDimensions = mapDimensions;	
		this.CAMERA_OFFSET_MIN = this.CAMERA_OFFSET_MAX + (float)(this.followedSprite.getWidth()) + (float)(this.followedSprite.getOffset().x/2);
		this.cameraOffset = followedSprite.getX();
	}
	
	public static AbstractGameCamera createCamera(CameraEnum cameraEnum, AbstractSprite followedSprite, Vector2 mapDimensions) {
		return cameraEnum==CameraEnum.FREE ? new FreeGameCamera(followedSprite, mapDimensions) : 
			cameraEnum==CameraEnum.MARIO_LIKE ?  new MarioLikeGameCamera(followedSprite, mapDimensions) : new FixedCamera(followedSprite, mapDimensions);			
	}
	
	public abstract void moveCamera();
	
	public abstract CameraEnum getCameraType();
	
	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public float getCameraOffset() {
		return cameraOffset;
	}

	public void setCameraOffset(float cameraOffset) {
		this.cameraOffset = cameraOffset;
	}	
	
	public boolean isScrollableVertically() {
		return scrollableVertically;
	}

	public void setScrollableVertically(boolean scrollableVertically) {
		this.scrollableVertically = scrollableVertically;
	}

	public boolean isScrollableHorizontally() {
		return scrollableHorizontally;
	}

	public void setScrollableHorizontally(boolean scrollableHorizontally) {
		this.scrollableHorizontally = scrollableHorizontally;
	}

	public Vector2 getMapDimensions() {
		return mapDimensions;
	}

	public void setMapDimensions(Vector2 mapDimensions) {
		this.mapDimensions = mapDimensions;
	}
}
