package com.game.core.sprite.impl.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.game.core.ActionBuilder;
import com.game.core.sprite.AbstractSprite;
import com.game.core.sprite.impl.player.Player;
import com.game.core.sprite.tileobject.AbstractTileObjectItem;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.SpriteMoveEnum;

public class Liana extends AbstractTileObjectItem {
	
	private Vector2 origin;
	
	private List<Vector2> points;		
	
	private float radius = 7 / ScreenConstants.MAP_UNIT_PIXELS;
		
	private static int NB_POINTS = 10;
	
	private static List<Integer> LEFT_ANGLES = Arrays.asList(-90,-128,-130,-136,-138,-144,-146,-152,-154,-160);
	
	private static List<Integer> RIGHT_ANGLES = Arrays.asList(-90,-52,-50,-44,-42,-36,-34,-28,-26,-20);
	
	private FloatAction floatAction;
	
	private boolean isPlayerStuck;
	
	private boolean stuckable;
	
	private float stuckTimeCount;
	
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
		floatAction = ActionBuilder.createFloatAction(-90, -160, 0.5f); 
		addAction(floatAction);
		setSize(radius, radius);
		bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
		stuckable = true;
	}

	@Override
	public void initializeAnimations() {	
		spriteSheet = ResourcesLoader.LIANA;		
		currentFrame = TextureRegion.split(spriteSheet, spriteSheet.getWidth(), spriteSheet.getHeight())[0][0];	
	}	
	
	@Override
	public void move(float deltaTime) {
		
		if (!stuckable) {
			stuckTimeCount += deltaTime;
			if (stuckTimeCount>=1) {
				stuckable = true;
				stuckTimeCount = 0;
			}
		}
		
		this.act(deltaTime);
		setRotation(floatAction.getValue());
				
		if (getRotation()==-160) {
			floatAction = ActionBuilder.createFloatAction(-160, -20, 1); 
			addAction(floatAction);			
		} else if (getRotation()==-20) {
			floatAction = ActionBuilder.createFloatAction(-20, -160, 1);
			addAction(floatAction);			
		}
				
		for (int i=0;i<NB_POINTS;i++) {
			if (getRotation()>LEFT_ANGLES.get(i) && getRotation()<RIGHT_ANGLES.get(i)) {
				points.get(i).x = origin.x + (radius*i) * (float)Math.cos(Math.toRadians(getRotation()));
				points.get(i).y = origin.y + (radius*i) * (float)Math.sin(Math.toRadians(getRotation()));
			}
		}					
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
		if (!player.isStuckToLiana()) {
			player.setMoveable(false);
			player.setCollidableWithTilemap(false);
			player.setStuckToLiana(true);
			player.setAcceleration(new Vector2());
			player.setState(SpriteMoveEnum.STUCK_TO_LIANA);
			setPlayerStuck(true);
			player.storeOldPosition();
			player.setX(points.get(NB_POINTS-1).x + 0.2f - player.getHalfWidth()-player.getOffset().x );
			player.setY(points.get(NB_POINTS-1).y + 0.2f - player.getHalfHeight());
			player.updateBounds();			
		}				
	}
	
	public void stuckPlayer(Player player) {
		player.storeOldPosition();
		player.setX(points.get(NB_POINTS-1).x + 0.2f - player.getHalfWidth()-player.getOffset().x );
		player.setY(points.get(NB_POINTS-1).y + 0.2f - player.getHalfHeight());
		player.updateBounds();
	}
	
	public boolean overlaps(AbstractSprite sprite) {		
		return stuckable ? this.getBounds().contains(sprite.getX()+sprite.getHalfWidth()+sprite.getOffset().x, sprite.getY()+sprite.getHalfHeight()) : false;		
	}
	
	public void updateBounds() {
		bounds.setX(points.get(NB_POINTS-1).x);
		bounds.setY(points.get(NB_POINTS-1).y);
	}

	public boolean isPlayerStuck() {
		return isPlayerStuck;
	}

	public void setPlayerStuck(boolean isPlayerStuck) {
		this.isPlayerStuck = isPlayerStuck;
	}
	
	public void unstuck() {
		this.isPlayerStuck = false;
		stuckTimeCount = 0;
		stuckable = false;
	}
	
}
