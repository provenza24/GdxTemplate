package com.game.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class ResourcesLoader {
	
	public static final BitmapFont MAIN_MENU_FONT = new BitmapFont(Gdx.files.internal("fonts/menu/pressStart2P.fnt"));
	
	public static final Texture BOARD = new Texture(Gdx.files.internal("board.jpg"));		
}
