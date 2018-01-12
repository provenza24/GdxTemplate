package com.game.core.util.enums.menu;

public enum MainMenuEnum {

	ONE_PLAYER_GAME ("PLAY");
	//SOUND_OPTIONS ("SOUND OPTIONS");
	//CREDITS ("CREDITS");

    private final String name;       

    private MainMenuEnum(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
       return this.name;
    }	
	
}
