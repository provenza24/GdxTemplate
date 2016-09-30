package com.game.core;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.game.core.screen.GameScreen;
import com.game.core.util.enums.ScreenEnum;

public class GameManager extends Game {

	private static final Map<ScreenEnum, Screen> SCREENS = new HashMap<ScreenEnum, Screen>();

	private static final GameManager gameManager = new GameManager();

	private static GameScreen gameScreen;
		
	@Override
	public void create() {
		
		gameScreen = new GameScreen();		
		SCREENS.put(ScreenEnum.GAME, gameScreen);
				
		setScreen(SCREENS.get(ScreenEnum.GAME));
	}
	
	public static GameManager getGameManager() {
		return gameManager;
	}

	public void changeScreen(ScreenEnum screenEnum) {
		setScreen(SCREENS.get(screenEnum));
	}

	public Screen getScreen(ScreenEnum screenEnum) {
		return SCREENS.get(screenEnum);
	}		
	
}
