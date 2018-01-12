package com.game.core.sprite.sfx;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface IDrawable {

	public void initializeAnimations();
		
	public void render(Batch batch);
}
