package com.game.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.core.screen.game.AbstractGameScreen;
import com.game.core.screen.game.GameScreen;
import com.game.core.screen.menu.impl.LevelMenuScreen;
import com.game.core.screen.menu.impl.MainMenuScreen;
import com.game.core.screen.transition.AbstractScreenTransition;
import com.game.core.screen.transition.AbstractScreenTransition.ScreenTransitionEnum;
import com.game.core.util.Level;
import com.game.core.util.enums.ScreenEnum;

public class GameManager extends AbstractGameManager {

	private static final Map<ScreenEnum, AbstractGameScreen> SCREENS = new HashMap<ScreenEnum, AbstractGameScreen>();

	private static final GameManager gameManager = new GameManager();
	
	private List<Level> levels = new ArrayList<Level>();
	
	private static int currentLevelIndex;		
	
	@Override
	public void create() {
		
		SCREENS.put(ScreenEnum.MAIN_MENU, new MainMenuScreen());
		SCREENS.put(ScreenEnum.LEVEL_MENU, new LevelMenuScreen());
		
		currentLevelIndex=0;				
		levels.add(new Level.Builder("tilemap.tmx").levelNumber(1).levelName("FOREST").build());
		levels.add(new Level.Builder("tilemap2.tmx").levelNumber(1).levelName("FOREST").build());
		setScreen(SCREENS.get(ScreenEnum.MAIN_MENU));
	}
	
	public void startGame() {
		currentLevelIndex=0;		
		AbstractGameScreen nextGameScreen = new GameScreen(levels.get(currentLevelIndex));				
		setScreen(nextGameScreen, AbstractScreenTransition.getScreenTransition(ScreenTransitionEnum.SLICE));		
	}
	
	public void nextLevel() {		
		currentLevelIndex++;		
		AbstractGameScreen nextGameScreen = new GameScreen(levels.get(currentLevelIndex));				
		setScreen(nextGameScreen, AbstractScreenTransition.getScreenTransition(ScreenTransitionEnum.SLICE));		
	}

	public static GameManager getGameManager() {
		return gameManager;
	}		
	
	public void setScreen(ScreenEnum screenEnum) {
		setScreen(SCREENS.get(screenEnum));
	}

}
