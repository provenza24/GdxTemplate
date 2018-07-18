package com.game.core.ia;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.game.core.sprite.impl.ennemy.DinosaurMan;
import com.game.core.util.enums.DirectionEnum;

public enum DinosaurManState implements State<DinosaurMan> {

	WALK() {		
		@Override
		public void enter(DinosaurMan entity) {						
			entity.getAcceleration().x = 2.5f;			
		}
		
		@Override
		public void update(DinosaurMan entity) {
			// Update animation
			if (entity.getNbHitBeforeDeath()>0) {
				// With costume
				entity.setCurrentAnimation(entity.getDirection()==DirectionEnum.LEFT ? 
						entity.getWalkLeftAnimation() : entity.getWalkRightAnimation());
			} else {
				// Without costume
				entity.setCurrentAnimation(entity.getDirection()==DirectionEnum.LEFT ? 
						entity.getWalkLeftNoCostumeAnimation() : entity.getWalkRightNoCostumeAnimation());
			}
			
		}
	},
	
	HIT() {		
		@Override
		public void enter(DinosaurMan entity) {						
			entity.decreaseNbHitBeforeDeath();
			switch (entity.getNbHitBeforeDeath()) {
			case 0:
				entity.setCurrentAnimation(entity.getDirection()==DirectionEnum.LEFT ? 
						entity.getWalkLeftNoCostumeAnimation() : entity.getWalkRightNoCostumeAnimation());
				entity.setCurrentFrame(entity.getCurrentAnimation().getKeyFrame(0));
				entity.setAnimated(false);
				entity.setKillable(false);
				entity.setInvincibleTimeCount(0);
				entity.getAcceleration().x = 0;
				break;
			default:
				entity.setKilled(true);
				entity.setDeletable(true);
				entity.getStateMachine().changeState(DEAD);
				break;
			}			
		}
		
		@Override
		public void update(DinosaurMan entity) {			
			entity.updateInvincibleTimeCount();
			if (entity.getInvincibleTimeCount()>0.5f) {
				entity.setAnimated(true);
				entity.setKillable(true);
				entity.getStateMachine().changeState(WALK);
			}
		}
		
		@Override
		public void exit(DinosaurMan entity) {							
		}
	}, DEAD;
	
	@Override
	public void enter(DinosaurMan entity) {		
	}

	@Override
	public void update(DinosaurMan entity) {	
	}

	@Override
	public void exit(DinosaurMan entity) {
	}

	@Override
	public boolean onMessage(DinosaurMan entity, Telegram telegram) {
		return false;
	}
	
}
