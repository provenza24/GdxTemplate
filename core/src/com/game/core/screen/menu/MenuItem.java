package com.game.core.screen.menu;

import com.badlogic.gdx.math.Vector2;

public class MenuItem {

	private Enum<?> menuEnum;
	
	private String name;

	private Vector2 position;
	
	public MenuItem(Enum<?> pmenuEnum, Vector2 position) {
		super();
		this.menuEnum = pmenuEnum;
		this.name = pmenuEnum.toString();
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}
		
	public Enum<?> getMenuEnum() {
		return menuEnum;
	}

	public void setMenuEnum(Enum<?> menuEnum) {
		this.menuEnum = menuEnum;
	}

}
