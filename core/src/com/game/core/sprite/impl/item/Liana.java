package com.game.core.sprite.impl.item;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.impl.player.Player;
import com.game.core.sprite.tileobject.AbstractTileObjectItem;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.constants.ScreenConstants;

public class Liana extends AbstractTileObjectItem {
	
	private Vector2 origin;
	
	private List<Vector2> points;		
	
	private float radius = 7 / ScreenConstants.MAP_UNIT_PIXELS;
		
	private static int NB_POINTS = 10;
	
	private int balance = -1;		
	
	private static List<Integer> LEFT_ANGLES;
	
	private static List<Integer> RIGHT_ANGLES;		
	
	static {
		LEFT_ANGLES = new ArrayList<Integer>();
		LEFT_ANGLES.add(-90);
		LEFT_ANGLES.add(-120);
		LEFT_ANGLES.add(-124);
		LEFT_ANGLES.add(-128);
		LEFT_ANGLES.add(-132);
		LEFT_ANGLES.add(-136);
		LEFT_ANGLES.add(-140);
		LEFT_ANGLES.add(-148);
		LEFT_ANGLES.add(-154);
		LEFT_ANGLES.add(-160);
		
		RIGHT_ANGLES = new ArrayList<Integer>();
		RIGHT_ANGLES.add(-90);
		RIGHT_ANGLES.add(-83);
		RIGHT_ANGLES.add(-77);
		RIGHT_ANGLES.add(-71);
		RIGHT_ANGLES.add(-65);
		RIGHT_ANGLES.add(-59);
		RIGHT_ANGLES.add(-53);
		RIGHT_ANGLES.add(-47);
		RIGHT_ANGLES.add(-41);
		RIGHT_ANGLES.add(-35);
	}
	
	public Liana(MapObject mapObject, Vector2 offset) {
		super(mapObject, offset);
		collidableWithTilemap = false;
		gravitating = false;
		isAnimated = false;		
		origin = new Vector2(getX(),getY());
		points = new ArrayList<Vector2>();
		for (int i=0; i<NB_POINTS;i++) {
			points.add(new Vector2(origin.x, origin.y + radius*i));
		}		
		setRotation(-90);
		
	}

	@Override
	public void initializeAnimations() {	
		spriteSheet = ResourcesLoader.LIANA;		
		currentFrame = TextureRegion.split(spriteSheet, spriteSheet.getWidth(), spriteSheet.getHeight())[0][0];	
	}	
	
	@Override
	public void move(float deltaTime) {
		
		if (getRotation()<-160 && balance==-1) {
			setRotation(-160);
			balance = 1;
		} else if (getRotation()>-35) {
			setRotation(-35);
			balance = -1;
		} 
		
		for (int i=0;i<NB_POINTS;i++) {
			if (getRotation()>LEFT_ANGLES.get(i) && getRotation()<RIGHT_ANGLES.get(i)) {
				points.get(i).x = origin.x + (radius*i) * (float)Math.cos(Math.toRadians(getRotation()));
				points.get(i).y = origin.y + (radius*i) * (float)Math.sin(Math.toRadians(getRotation()));
			}
		}		
		setRotation(getRotation()+2*balance);				
	}
	
	@Override
	public void render(Batch batch) {
		batch.begin();	
		for (int i=0;i<NB_POINTS;i++) {
			batch.draw(currentFrame, points.get(i).x, points.get(i).y, renderingSize.x, renderingSize.y);
		}			
		batch.end();
	}			

	@Override
	public void collideWithPlayer(Player player) {				
	}
	
}
