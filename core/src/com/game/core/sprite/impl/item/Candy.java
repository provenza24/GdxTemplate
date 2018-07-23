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
	
	private Integer tileNumber;
	
	public Candy(MapObject mapObject, Vector2 offset) {
		super(mapObject, offset);
		collidableWithTilemap = false;
		gravitating = false;
		isAnimated = false;		
		origin = new Vector2(getHalfWidth(),getHalfHeight());		
		tileNumber = Integer.parseInt((String)mapObject.getProperties().get("tileNumber"));
		initializeTexture();
	}

	@Override
	public void initializeAnimations() {					
	}	
	
	public void initializeTexture() {
		spriteSheet = ResourcesLoader.CANDY;
		int x = tileNumber/9;
		int y = tileNumber%9;
		System.out.println(x+" "+y);
		currentFrame = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/9, spriteSheet.getHeight()/3)[x][y];	
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
