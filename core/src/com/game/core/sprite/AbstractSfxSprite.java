package com.game.core.sprite;

import com.badlogic.gdx.math.Vector2;

public abstract class AbstractSfxSprite extends AbstractSprite implements IAppearable {
			
	public AbstractSfxSprite(float x, float y, Vector2 size, Vector2 offset) {
		super(x, y, size, offset);		
		alive = true;				
		moveable = false;
		collidableWithTilemap = false;
		gravitating = false; 
	}
	
	public AbstractSfxSprite(float x, float y) {
		this(x, y, new Vector2(1,1), new Vector2());
	}
	
	public abstract void addAppearAction();
}
