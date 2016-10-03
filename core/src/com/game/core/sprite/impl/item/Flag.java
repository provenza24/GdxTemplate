package com.game.core.sprite.impl.item;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.tileobject.AbstractTileObjectItem;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.animation.AnimationBuilder;
import com.game.core.util.enums.ItemEnum;

public class Flag extends AbstractTileObjectItem {

	public Flag(MapObject mapObject, Vector2 offset) {
		super(mapObject, offset);
		collidableWithTilemap = false;
		gravitating = false;
		isAnimationLooping = false;
	}

	@Override
	public ItemEnum getType() {
		return null;
	}

	@Override
	public void initializeAnimations() {		
		currentAnimation = AnimationBuilder.getInstance().build(ResourcesLoader.FLAG,0, 1, 0);			
	}

}
