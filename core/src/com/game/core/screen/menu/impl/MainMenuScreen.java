package com.game.core.screen.menu.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.game.core.GameManager;
import com.game.core.screen.menu.AbstractMenuScreen;
import com.game.core.sprite.menu.DefaultSelector;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.enums.menu.MainMenuEnum;

public class MainMenuScreen extends AbstractMenuScreen {

	public MainMenuScreen() {				
		super(MainMenuEnum.class, ResourcesLoader.MAIN_MENU_FONT,DefaultSelector.class);
		setOffset(0, -55);		
		setFontColor(1,1,1);
	}
	
	public void addBackgroundElements() {				
	}

	@Override
	public void handleInput() {
		
		super.handleInput();
		
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			if (getSelectedItemEnum()==MainMenuEnum.ONE_PLAYER_GAME) {								
				GameManager.getGameManager().startGame();				
			}			
		}
	}

}
