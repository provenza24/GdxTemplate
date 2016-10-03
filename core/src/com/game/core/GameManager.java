package com.game.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.game.core.screen.GameScreen;
import com.game.core.util.Level;
import com.game.core.util.enums.ScreenEnum;

public class GameManager extends Game {

	private static final Map<ScreenEnum, Screen> SCREENS = new HashMap<ScreenEnum, Screen>();

	private static final GameManager gameManager = new GameManager();

	private static GameScreen gameScreen;
	
	private List<Level> levels = new ArrayList<Level>();
	
	private static int currentLevelIndex;
		
	@Override
	public void create() {
		
		levels.add(new Level(1,1,"tilemap.tmx"));
		levels.add(new Level(1,2,"tilemap2.tmx"));
		
		gameScreen = new GameScreen(levels.get(currentLevelIndex));		
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
	
	public void nextLevel() {
		currentLevelIndex++;
		gameScreen.dispose();		
		gameScreen = new GameScreen(levels.get(currentLevelIndex));
		SCREENS.put(ScreenEnum.GAME, gameScreen);			
		changeScreen(ScreenEnum.GAME);
	}
	
}
