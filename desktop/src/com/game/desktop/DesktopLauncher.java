package com.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.core.GameManager;
import com.game.core.util.constants.ScreenConstants;

public class DesktopLauncher {

	public static void main(String[] arg) {
					
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = ScreenConstants.WIDTH;
		config.height = ScreenConstants.HEIGHT;
		
		System.out.println("Starting game with size "+config.width+"x"+config.height);
					
		new LwjglApplication(GameManager.getGameManager(), config);
	}
}
