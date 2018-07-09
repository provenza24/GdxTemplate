package com.game.core.sprite.impl.player;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.game.core.sprite.AbstractSprite;
import com.game.core.util.ResourcesLoader;
import com.game.core.util.enums.DirectionEnum;
import com.game.core.util.enums.SpriteMoveEnum;

public class Club extends AbstractSprite {

	private TextureRegion idleTextureRight;
	
	private TextureRegion idleTextureLeft;
	
	private TextureRegion walkTextureRight;
	
	private TextureRegion walkTextureLeft;
	
	private final static Map<Integer, Float> Y_JUMPING_POSITIONS = new HashMap<>();
	
	static {
		Y_JUMPING_POSITIONS.put(0, -0.2f);
		Y_JUMPING_POSITIONS.put(1, 0.2f);
		Y_JUMPING_POSITIONS.put(2, 0.4f);
		//Y_JUMPING_POSITIONS.put(3, 0.4f);
	}
	
	public Club(float x, float y) {
		super(x, y);
		gravitating = false;
		collidableWithTilemap = false;		
		moveable = false;
		
		setSize(2, 1);
		setRenderingSize(2, 1);
	}

	@Override
	public void initializeAnimations() {
		spriteSheet = ResourcesLoader.CLUB;		
		//TextureRegion[][] textureRegions = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight()/1);		
		idleTextureRight = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight()/1)[0][0];
		idleTextureLeft = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight()/1)[0][0];
		idleTextureLeft.flip(true, false);
		
		walkTextureRight = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight()/1)[0][1];
		walkTextureLeft = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight()/1)[0][1];
		walkTextureLeft.flip(true, false);						
	}

	public void render(Batch batch, Player player) {
		
		TextureRegion currentTexture = player.getDirection()==DirectionEnum.RIGHT ? idleTextureRight : idleTextureLeft;
		if (player.getState()!=SpriteMoveEnum.IDLE) {
			currentTexture = player.getDirection()==DirectionEnum.RIGHT ? walkTextureRight : walkTextureLeft;	
		}
		
		setX(player.getDirection()==DirectionEnum.RIGHT ? player.getX()-1.8f : player.getX()+1.8f);	
		setY(player.getY());
		
		if (player.getState()==SpriteMoveEnum.IDLE) {			
			setX(player.getDirection()==DirectionEnum.RIGHT ? player.getX()-1.5f : player.getX()+1.5f);
		} else if (player.getState()==SpriteMoveEnum.JUMPING) {			
			setY(player.getY()+Y_JUMPING_POSITIONS.get(player.getCurrentAnimation().getKeyFrameIndex(player.getStateTime())));						
		} else if (player.getState()==SpriteMoveEnum.FALLING) {
			setY(player.getY()+0.4f);
		}
		
		
		if (player.isOnCurvedTile()) {			
			this.setRotation(player.isPositiveCurvedTile() ? -20 : 20);
		} else {
			this.setRotation(0);
		}
		
		/*TextureRegion currentTexture = player.getDirection()==DirectionEnum.RIGHT ? idleTextureRight : idleTextureLeft;		
		if (player.getState()==SpriteMoveEnum.IDLE) {
			setX(player.getDirection()==DirectionEnum.RIGHT ? player.getX()-1.5f : player.getX()+1.5f);
			//setY(player.getY()-0.2f);
		} else if (player.getState()==SpriteMoveEnum.JUMPING) {
			int animationIdx = player.getCurrentAnimation().getKeyFrameIndex(player.getStateTime());
			if (animationIdx==0) {
				setX(player.getDirection()==DirectionEnum.RIGHT ? player.getX()-1.8f : player.getX()+1.8f);
				setY(player.getY()-0.1f);
			} else if (animationIdx==1) {
				setX(player.getDirection()==DirectionEnum.RIGHT ? player.getX()-1.8f : player.getX()+1.8f);
				setY(player.getY()+0.3f);
			} else {
				setX(player.getDirection()==DirectionEnum.RIGHT ? player.getX()-1.8f : player.getX()+1.8f);
				setY(player.getY()+0.4f);
			}			
		} else if (player.getState()==SpriteMoveEnum.FALLING) {
			setX(player.getDirection()==DirectionEnum.RIGHT ? player.getX()-1.8f : player.getX()+1.8f);
			setY(player.getY()+0.4f);
		} else {
			setX(player.getDirection()==DirectionEnum.RIGHT ? player.getX()-1.8f : player.getX()+1.8f);
			setY(player.getY());
		}*/
				
		batch.draw(currentTexture, getX() , getY(), 2, 1 , renderingSize.x, renderingSize.y, 1,1, this.getRotation());		
	}
	
	private void update() {
		
	}
}
