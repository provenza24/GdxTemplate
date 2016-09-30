package com.game.core.collision.tilemap;

import com.game.core.sprite.AbstractSprite;
import com.game.core.tilemap.TmxMap;

public interface ITilemapCollisionHandler<T extends AbstractSprite> {

	public void collideWithTilemap(TmxMap tileMap, T sprite);

}
