package com.game.core.background;

public interface IScrollingBackground {

	/** Update the background position */
	public void update();
	
	/** Render the background image on screen */
	public void render();	
	
	/** Enable / disable background display */
	public void toggleEnabled();
		
}
