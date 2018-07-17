package com.game.core.sprite.impl.ennemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.game.core.collision.tilemap.impl.BasicTilemapCollisionHandler;
import com.game.core.sprite.AbstractSprite;
import com.game.core.sprite.tileobject.AbstractTileObjectEnemy;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.animation.AnimationBuilder;
import com.game.core.util.enums.DirectionEnum;

public class DinosaurMan extends AbstractTileObjectEnemy {

	private Animation walkLeftAnimation;		
	
	private Animation walkRightAnimation;
		
	public DinosaurMan(MapObject mapObject, Vector2 offset) {
		super(mapObject, offset);
		moveable = true;
		collidableWithTilemap = true;
		gravitating = true;	
		this.acceleration.x = 2.5f;
		this.direction = DirectionEnum.LEFT;
		tilemapCollisionHandler = new BasicTilemapCollisionHandler();		
	}

	@Override
	public void initializeAnimations() {		
		spriteSheet = ResourcesLoader.DINOSAURMAN;			
		TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/8, spriteSheet.getHeight());		
		walkLeftAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{0,1,2,3}, 8, 0.07f);			
		walkRightAnimation = AnimationBuilder.getInstance().build(textureRegions, new int[]{4,5,6,7}, 8, 0.07f);
		currentAnimation = walkLeftAnimation;
	}

	
	
	@Override
	protected void updateAnimation(float delta) {
		currentAnimation = direction==DirectionEnum.LEFT ? walkLeftAnimation : walkRightAnimation;
		super.updateAnimation(delta);
	}

	@Override
	public AbstractSprite generateDeadSprite(DirectionEnum directionEnum) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
