package com.game.core.util.enums.menu;

public enum LevelMenuEnum {

	START ("START NEXT LEVEL");	

    private final String name;       

    private LevelMenuEnum(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
       return this.name;
    }	
	
}
