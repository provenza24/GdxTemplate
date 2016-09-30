package com.game.core.sprite.tileobject;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.AbstractEnemy;
import com.game.core.util.constants.ScreenConstants;

public abstract class AbstractTileObjectEnemy extends AbstractEnemy {
			
	public AbstractTileObjectEnemy(MapObject mapObject, Vector2 offset) {
		
		super((Float) mapObject.getProperties().get("x")/ScreenConstants.MAP_UNIT_PIXELS, (float)(((Float) mapObject.getProperties().get("y")) /ScreenConstants.MAP_UNIT_PIXELS));		
		super.initTileObjectSprite(mapObject, offset);
	}
}
