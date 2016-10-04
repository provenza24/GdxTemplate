package com.game.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.game.core.util.constants.ScreenConstants;

public class ResourcesLoader {
	
	public static final Texture PLAYER = new Texture(Gdx.files.internal("player/player.png"));
	
	public static final Texture FLAG = new Texture(Gdx.files.internal("items/flag.png"));
	
	public static Texture BACKGROUND_FOREST;
	
	public static Texture getBackgroundForest() {

		if (BACKGROUND_FOREST==null) {
		
			Pixmap pixmap200 = new Pixmap(Gdx.files.internal("background/forest.png"));		
			Pixmap pixmap100 = new Pixmap(ScreenConstants.WIDTH *2, ScreenConstants.HEIGHT*2, pixmap200.getFormat());
			pixmap100.drawPixmap(pixmap200,
			        0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
			        0, 0, pixmap100.getWidth(), pixmap100.getHeight()
			);
			BACKGROUND_FOREST = new Texture(pixmap100);
		}
		
		return BACKGROUND_FOREST;
	}			
		
}
