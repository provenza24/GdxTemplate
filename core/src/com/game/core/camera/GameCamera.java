package com.game.core.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.AbstractSprite;
import com.game.core.util.constants.ScreenConstants;

/**
 * Class used to define an orthographic camera which will follow a chosen sprite
 */
public class GameCamera {

	private float CAMERA_OFFSET_MAX = (float)(ScreenConstants.NB_HORIZONTAL_TILES/2);
	
	private float CAMERA_OFFSET_MIN;

	private OrthographicCamera camera;
	
	private float cameraOffset = 0;
	
	private boolean scrollableHorizontally;
	
	private boolean scrollableVertically;
	
	private AbstractSprite followedSprite;
	
	private float initialY;
	
	private Vector2 mapDimensions;

	public GameCamera(AbstractSprite followedSprite, Vector2 mapDimensions) {
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, ScreenConstants.NB_HORIZONTAL_TILES, ScreenConstants.NB_VERTICAL_TILES);
		this.camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		this.camera.update();		
		this.scrollableHorizontally =  mapDimensions.x > ScreenConstants.NB_HORIZONTAL_TILES;				
		this.scrollableVertically = mapDimensions.y > ScreenConstants.NB_VERTICAL_TILES;
		this.followedSprite = followedSprite;						
		this.initialY = camera.viewportHeight / 2f;
		this.mapDimensions = mapDimensions;	
		this.CAMERA_OFFSET_MIN = this.CAMERA_OFFSET_MAX + (float)(this.followedSprite.getWidth()/2) - (float)(this.followedSprite.getOffset().x/2);
	}
	
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
