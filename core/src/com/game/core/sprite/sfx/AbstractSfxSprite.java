package com.game.core.sprite.sfx;

import com.badlogic.gdx.math.Vector2;

public abstract class AbstractSfxSprite extends AbstractSprite implements IAppearable {
			
	public AbstractSfxSprite(float x, float y, Vector2 size) {
		super(x, y, size);		
		alive = true;				
		moveable = false;		
		gravitating = false; 
	}
	
	public AbstractSfxSprite(float x, float y) {
		this(x, y, new Vector2(1,1));
	}
	
	public abstract void addAppearAction();
}
