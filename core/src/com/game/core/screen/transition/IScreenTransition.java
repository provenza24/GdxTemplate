package com.game.core.screen.transition;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IScreenTransition {
	
	public float getDuration();

	public void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha);
}