package com.game.core.sprite;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.game.core.tilemap.TmxMap;

public interface IMoveable {

	public void update(TmxMap tileMap, OrthographicCamera camera, float deltaTime);	
	
}
