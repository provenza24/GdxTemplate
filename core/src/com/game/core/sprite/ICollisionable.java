package com.game.core.sprite;

import com.game.core.tilemap.TmxMap;

public interface ICollisionable {

	public void collideWithTilemap(TmxMap tileMap);
}
