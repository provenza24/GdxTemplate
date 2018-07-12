package com.game.core.sprite;

import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.impl.player.Player;
import com.game.core.util.enums.ItemEnum;

public abstract class AbstractItem extends AbstractSprite implements IAppearable {

	public AbstractItem(float x, float y, Vector2 size, Vector2 offset) {
		super(x, y, size, offset);		
		moveable = true;
		collidableWithTilemap = true;
		gravitating = true;		
	}
	
	public AbstractItem(float x, float y) {
		this(x, y, new Vector2(1,1), new Vector2());
	}

	public abstract ItemEnum getType();		
	
	public abstract void collideWithPlayer(Player player);

}
