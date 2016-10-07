package com.game.core.util.setting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.game.core.util.constants.SettingsConstants;

public class GameSettings {

	public static final String TAG = GameSettings.class.getName();

	public static final GameSettings instance = new GameSettings();

	public boolean showFpsCounter;

	private Preferences prefs;

	private GameSettings() {
		prefs = Gdx.app.getPreferences(SettingsConstants.PREFERENCES);
	}

	public void load() {
		showFpsCounter = prefs.getBoolean("showFpsCounter", true);
	}

	public void save() {
	}

}