package com.game.core.sprite.sfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IDrawable {

	public void initializeAnimations();
		
	public void render(SpriteBatch batch);
}
