package com.game.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.game.core.util.constants.ScreenConstants;

public class ResourcesLoader {
	
	public static final Texture PLAYER = new Texture(Gdx.files.internal("sprites/player/player.png"));
	
	public static final Texture FLAG = new Texture(Gdx.files.internal("sprites/items/flag.png"));
	
	public static Texture BACKGROUND_FOREST;
	
	public static Texture getBackgroundForest() {

		if (BACKGROUND_FOREST==null) {
		
			Pixmap pixmap200 = new Pixmap(Gdx.files.internal("backgrounds/forest.png"));		
			Pixmap pixmap100 = new Pixmap(2645, 992, pixmap200.getFormat());
			pixmap100.drawPixmap(pixmap200,
			        0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
			        0, 0, pixmap100.getWidth(), pixmap100.getHeight()
			);
			BACKGROUND_FOREST = new Texture(pixmap100);
		}
		
		return BACKGROUND_FOREST;
	}			
	
	public static Texture BACKGROUND_SUBURBS;
	
	public static Texture getBackgroundSuburbs() {

		if (BACKGROUND_SUBURBS==null) {
		
			Pixmap pixmap200 = new Pixmap(Gdx.files.internal("backgrounds/suburbs.png"));		
			Pixmap pixmap100 = new Pixmap((int)(pixmap200.getWidth()*ScreenConstants.SCREEN_COEF), (int)(pixmap200.getHeight()*ScreenConstants.SCREEN_COEF) , pixmap200.getFormat());
			pixmap100.drawPixmap(pixmap200,
			        0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
			        0, 0, pixmap100.getWidth(), pixmap100.getHeight()
			);
			BACKGROUND_SUBURBS = new Texture(pixmap100);
		}
		
		return BACKGROUND_SUBURBS;
	}		
	
	public static final BitmapFont MAIN_MENU_FONT = new BitmapFont(Gdx.files.internal("fonts/menu/pressStart2P.fnt"));
		
}
