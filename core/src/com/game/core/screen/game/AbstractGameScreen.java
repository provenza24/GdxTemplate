package com.game.core.screen.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.game.core.util.constants.KeysConstants;
import com.game.core.util.enums.ScreenStateEnum;

public abstract class AbstractGameScreen extends ScreenAdapter {

	/** KEYS CONSTANTS */
	protected static final int KEY_LEFT =  KeysConstants.KEY_LEFT;
	
	protected static final int KEY_RIGHT =  KeysConstants.KEY_RIGHT;
			
	protected static final int KEY_DOWN = KeysConstants.KEY_DOWN;
	
	protected static final int KEY_UP = KeysConstants.KEY_UP;
	
	protected static Color DEBUG_BOUNDS_COLOR;
	
	protected static final Color[] fontColors = new Color[]{Color.WHITE, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE};
	
	protected static final Color[] debugBounds = new Color[]{new Color(1, 1, 1, 0.5f), new Color(0, 0, 0, 0.5f), new Color(1, 0, 0, 0.5f), new Color(0, 1, 0, 0.5f), new Color(0, 0, 1, 0.5f)};
	
	protected static int currentDebugColor = 4;	
	
	protected ScreenStateEnum screenState = ScreenStateEnum.RUNNING;

	public ScreenStateEnum getScreenState() {
		return screenState;
	}

	public void setScreenState(ScreenStateEnum screenState) {
		this.screenState = screenState;
	}
	
}
