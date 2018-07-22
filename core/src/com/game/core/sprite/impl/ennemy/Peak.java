package com.game.core.sprite.impl.ennemy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.AbstractEnemy;
import com.game.core.sprite.AbstractSprite;
import com.game.core.sprite.impl.player.Player;
import com.game.core.util.enums.DirectionEnum;
import com.game.core.util.enums.SpriteMoveEnum;

public class Peak extends AbstractEnemy {
	
	public Peak(Vector2 position) {
		super(position.x, position.y);		
		moveable = false;
		collidableWithTilemap = false;
		gravitating = false;
		killableByPlayer = false;		
	}

	@Override
	public void initializeAnimations() {				
	}

	@Override
	public AbstractSprite generateDeadSprite(DirectionEnum directionEnum) {		
		return null;
	}

	@Override
	public void render(Batch batch) {
		// No rendering
	}
	
	@Override
	protected void updateAnimation(float delta) {	
		// No animation to update
	}
	
	@Override
	public void hitPlayer(Player player) {
		if (player.getState()==SpriteMoveEnum.FALLING) {
			super.hitPlayer(player);
			player.setState(SpriteMoveEnum.JUMPING);
			player.setAcceleration(new Vector2(0,0.4f));
			player.setMoveable(true);
		}
	}
	
}
