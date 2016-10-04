package com.game.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.game.core.screen.game.AbstractGameScreen;
import com.game.core.screen.game.GameScreen;
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
		currentLevelIndex=0;		
		levels.add(new Level(1,1,"tilemap.tmx"));
		levels.add(new Level(1,2,"tilemap2.tmx"));				
		levels.add(new Level(1,1,"tilemap.tmx"));
		levels.add(new Level(1,2,"tilemap2.tmx"));
		levels.add(new Level(1,1,"tilemap.tmx"));
		levels.add(new Level(1,2,"tilemap2.tmx"));
		levels.add(new Level(1,1,"tilemap.tmx"));
		levels.add(new Level(1,2,"tilemap2.tmx"));
		levels.add(new Level(1,1,"tilemap.tmx"));
		levels.add(new Level(1,2,"tilemap2.tmx"));
		setScreen(new GameScreen(levels.get(currentLevelIndex)));
	}
	
	public void nextLevel() {
		setInit(false);
		currentLevelIndex++;		
		AbstractGameScreen nextGameScreen = new GameScreen(levels.get(currentLevelIndex));				
		setScreen(nextGameScreen, AbstractScreenTransition.getScreenTransition(ScreenTransitionEnum.SLICE));		
	}

	public static GameManager getGameMamanger() {
		return gameManager;
	}

}
