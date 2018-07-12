package com.game.core.sprite.impl.item;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.impl.player.Player;
import com.game.core.sprite.tileobject.AbstractTileObjectItem;
import com.game.core.util.ResourcesLoader;

public class Candy extends AbstractTileObjectItem {

	private static float commonRotation;
	
	private Vector2 origin;
	
	public Candy(MapObject mapObject, Vector2 offset) {
		super(mapObject, offset);
		collidableWithTilemap = false;
		gravitating = false;
		isAnimated = false;		
		origin = new Vector2(getHalfWidth(),getHalfHeight());
		setName("CANDY");
	}

	@Override
	public void initializeAnimations() {		
		spriteSheet = ResourcesLoader.CANDY;
		currentFrame = TextureRegion.split(spriteSheet, spriteSheet.getWidth(), spriteSheet.getHeight())[0][0];			
	}	
	
	@Override
	public void move(float deltaTime) {			
	}
	
	@Override
	public void render(Batch batch) {
		batch.begin();		
		batch.draw(currentFrame, getX() , getY(), origin.x, origin.y , renderingSize.x, renderingSize.y, 1, 1, commonRotation);			
		batch.end();
	}	
	
	public static void updateRotation() {
		commonRotation+=15;
	}

	@Override
	public void collideWithPlayer(Player player) {
		this.setDeletable(true);
		
	}
	
}
