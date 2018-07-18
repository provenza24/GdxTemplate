package com.game.core.sprite;

import com.badlogic.gdx.math.Vector2;
import com.game.core.util.enums.DirectionEnum;
import com.game.core.util.enums.SpriteMoveEnum;

public abstract class AbstractEnemy extends AbstractSprite {
			
	protected boolean collidableWithEnnemies;
	
	protected boolean killableByPlayer;
	
	protected int nbHitBeforeDeath;
		
	protected float deltaTime;
	
	protected float invincibleTimeCount;
	
	public AbstractEnemy(float x, float y, Vector2 size, Vector2 offset) {
		super(x, y, size, offset);		
		state = SpriteMoveEnum.WALKING;
		killableByPlayer = true;
		gravitating = true;
		moveable = true;
		collidableWithTilemap = true;
		collidableWithEnnemies = true;
		nbHitBeforeDeath = 0;
	}
	
	public AbstractEnemy(float x, float y) {
		this(x, y, new Vector2(1,1), new Vector2());
	}
	
	public boolean hit() {
		nbHitBeforeDeath--;
		if (nbHitBeforeDeath<0) {
			setKilled(true);
			setDeletable(true);
		}		
		return true;
	}
	
	public abstract AbstractSprite generateDeadSprite(DirectionEnum directionEnum);

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

	public float getInvincibleTimeCount() {
		return invincibleTimeCount;
	}

	public void setInvincibleTimeCount(float invincibleTimeCount) {
		this.invincibleTimeCount = invincibleTimeCount;
	}

	public int getNbHitBeforeDeath() {
		return nbHitBeforeDeath;
	}

	public void setNbHitBeforeDeath(int nbHitBeforeDeath) {
		this.nbHitBeforeDeath = nbHitBeforeDeath;
	}		
	
	public void decreaseNbHitBeforeDeath() {
		this.nbHitBeforeDeath--;
	}

	public float getDeltaTime() {
		return deltaTime;
	}

	public void setDeltaTime(float deltaTime) {
		this.deltaTime = deltaTime;
	}
	
	public void updateInvincibleTimeCount() {
		this.invincibleTimeCount+=this.deltaTime;
	}

}
