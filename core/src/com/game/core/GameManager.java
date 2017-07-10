package com.game.core;

import java.util.HashMap;
import java.util.Map;

import com.game.core.screen.game.AbstractGameScreen;
import com.game.core.screen.game.GameScreen;
import com.game.core.screen.menu.impl.LevelMenuScreen;
import com.game.core.screen.menu.impl.MainMenuScreen;
import com.game.core.screen.transition.AbstractScreenTransition;
import com.game.core.screen.transition.AbstractScreenTransition.ScreenTransitionEnum;
import com.game.core.util.enums.ScreenEnum;

public class GameManager extends AbstractGameManager {

	private static final Map<ScreenEnum, AbstractGameScreen> SCREENS = new HashMap<ScreenEnum, AbstractGameScreen>();

	private static final GameManager gameManager = new GameManager();
	
	@Override
	public void create() {
		
		SCREENS.put(ScreenEnum.MAIN_MENU, new MainMenuScreen());
		SCREENS.put(ScreenEnum.LEVEL_MENU, new LevelMenuScreen());				
		setScreen(SCREENS.get(ScreenEnum.MAIN_MENU));
	}
	
	public void startGame() {
		AbstractGameScreen nextGameScreen = new GameScreen();				
		setScreen(nextGameScreen, AbstractScreenTransition.getScreenTransition(ScreenTransitionEnum.FADE));		
	}
	
	public void nextLevel() {		
		AbstractGameScreen nextGameScreen = new GameScreen();				
		setScreen(nextGameScreen, AbstractScreenTransition.getScreenTransition(ScreenTransitionEnum.SLICE));		
	}

	public static GameManager getGameManager() {
		return gameManager;
	}		
	
	public void setScreen(ScreenEnum screenEnum) {
		setScreen(SCREENS.get(screenEnum));
	}

}
