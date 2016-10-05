package com.game.core.screen.menu.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.game.core.GameManager;
import com.game.core.screen.menu.AbstractMenuScreen;
import com.game.core.sprite.menu.DefaultSelector;
import com.game.core.sprite.menu.MushroomSelector;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.enums.menu.LevelMenuEnum;

public class LevelMenuScreen extends AbstractMenuScreen {
		
	public LevelMenuScreen() {				
		super(LevelMenuEnum.class, ResourcesLoader.MAIN_MENU_FONT ,DefaultSelector.class);		
		setOffset(20, -160);
		setFontColor(1,1,1);			
	}

	@Override
	public void handleInput() {
		
		super.handleInput();
		
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {		
			GameManager.getGameManager().nextLevel();							
		}
	}

	@Override
	public void addBackgroundElements() {				
	}
}
