package com.game.core.screen.game;

import com.badlogic.gdx.Screen;
import com.game.core.util.enums.ScreenStateEnum;

public abstract class AbstractGameScreen implements Screen {

	protected ScreenStateEnum screenState = ScreenStateEnum.RUNNING;

	public ScreenStateEnum getScreenState() {
		return screenState;
	}

	public void setScreenState(ScreenStateEnum screenState) {
		this.screenState = screenState;
	}
	
}
