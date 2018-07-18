package com.game.core.sprite.impl.ennemy;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.game.core.collision.tilemap.impl.BasicTilemapCollisionHandler;
import com.game.core.ia.DinosaurManState;
import com.game.core.sprite.AbstractSprite;
import com.game.core.sprite.tileobject.AbstractTileObjectEnemy;
import com.game.core.tilemap.TmxMap;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.animation.AnimationBuilder;
import com.game.core.util.enums.DirectionEnum;

public class DinosaurMan extends AbstractTileObjectEnemy {

	private Animation walkLeftAnimation;		
	
	private Animation walkRightAnimation;
	
	private Animation walkLeftNoCostumeAnimation;
	
	private Animation walkRightNoCostumeAnimation;	
	
	private StateMachine<DinosaurMan> stateMachine;

	public DinosaurMan(MapObject mapObject, Vector2 offset) {
		super(mapObject, offset);
		this.acceleration.x = 2.5f;
		this.direction = DirectionEnum.LEFT;
		tilemapCollisionHandler = new BasicTilemapCollisionHandler();	
		nbHitBeforeDeath = 1;		
		stateMachine = new DefaultStateMachine<DinosaurMan>(this, DinosaurManState.WALK);		
	}

	@Override
	public void initializeAnimations() {		
		spriteSheet = ResourcesLoader.DINOSAURMAN;			
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/8, spriteSheet.getHeight()/2);		
		walkLeftAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{0,1,2,3}, 8, 0.07f);			
		walkRightAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{4,5,6,7}, 8, 0.07f);
		walkLeftNoCostumeAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{8,9,10,11}, 8, 0.07f);			
		walkRightNoCostumeAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{4,5,6,7}, 8, 0.07f);
		currentAnimation = walkLeftAnimation;
	}

	@Override
	public void update(TmxMap tileMap, OrthographicCamera camera, float deltaTime) {
		setDeltaTime(deltaTime);
		stateMachine.update();
		super.update(tileMap, camera, deltaTime);		
	}			

	@Override
	public boolean hit() {		
		stateMachine.changeState(DinosaurManState.HIT);		
		return killed;
	}
	
	@Override
	public AbstractSprite generateDeadSprite(DirectionEnum directionEnum) {
		// TODO Auto-generated method stub
		return null;
	}

	public StateMachine<DinosaurMan> getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(StateMachine<DinosaurMan> stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	public Animation getWalkLeftAnimation() {
		return walkLeftAnimation;
	}

	public Animation getWalkRightAnimation() {
		return walkRightAnimation;
	}

	public Animation getWalkLeftNoCostumeAnimation() {
		return walkLeftNoCostumeAnimation;
	}

	public Animation getWalkRightNoCostumeAnimation() {
		return walkRightNoCostumeAnimation;
	}

}
