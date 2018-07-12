package com.game.core.sprite;

import com.badlogic.gdx.math.Vector2;
import com.game.core.util.enums.SpriteMoveEnum;

public abstract class AbstractEnemy extends AbstractSprite {
			
	protected boolean collidableWithEnnemies;
	
	protected boolean killableByPlayer;
	
	public AbstractEnemy(float x, float y, Vector2 size, Vector2 offset) {
		super(x, y, size, offset);		
		state = SpriteMoveEnum.WALKING;
		killableByPlayer = true;
		gravitating = true;
		moveable = true;
		collidableWithTilemap = true;
		collidableWithEnnemies = true;
	}
	
	public AbstractEnemy(float x, float y) {
		this(x, y, new Vector2(1,1), new Vector2());
	}

	public void killByFireball(AbstractSprite fireball) {
		this.bump();
		acceleration.x = fireball.getAcceleration().x > 0 ? 3 : -3;		
	}		
	
	public void killByStar() {
		this.bump();		
	}

	public boolean isKillable() {
		return killableByPlayer;
	}

	public void setKillable(boolean killable) {
		this.killableByPlayer = killable;
	}

	public boolean isCollidableWithEnnemies() {
		return collidableWithEnnemies;
	}

	public void setCollidableWithEnnemies(boolean collidableWithEnnemies) {
		this.collidableWithEnnemies = collidableWithEnnemies;
	}
		

}
