package com.game.core.sprite.impl.player;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
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
	
	private final static Map<Integer, Vector2> RIGHT_ATTACK_POSITIONS = new HashMap<>();
	
	private final static Map<Integer, Float> RIGHT_ATTACK_ANGLES = new HashMap<>();
	
	private final static Map<Integer, Vector2> LEFT_ATTACK_POSITIONS = new HashMap<>();
	
	private final static Map<Integer, Float> LEFT_ATTACK_ANGLES = new HashMap<>();
	
	static {
		Y_JUMPING_POSITIONS.put(0, -0.2f);
		Y_JUMPING_POSITIONS.put(1, 0.2f);
		Y_JUMPING_POSITIONS.put(2, 0.4f);
		Y_JUMPING_POSITIONS.put(3, 0.4f);
		Y_JUMPING_POSITIONS.put(4, 0.4f);
		
		RIGHT_ATTACK_POSITIONS.put(0, new Vector2(-1.8f, 0.2f));
		RIGHT_ATTACK_POSITIONS.put(1, new Vector2(-1.8f, 0f));
		RIGHT_ATTACK_POSITIONS.put(2, new Vector2(-0.1f, -0.1f));
		RIGHT_ATTACK_POSITIONS.put(3, new Vector2(0.3f, 0.6f));
		RIGHT_ATTACK_POSITIONS.put(4, new Vector2(-0.4f, 0.7f));
		
		LEFT_ATTACK_POSITIONS.put(0, new Vector2(1.8f, 0.2f));
		LEFT_ATTACK_POSITIONS.put(1, new Vector2(1.8f, 0f));
		LEFT_ATTACK_POSITIONS.put(2, new Vector2(+0.1f, -0.1f));
		LEFT_ATTACK_POSITIONS.put(3, new Vector2(0.1f, 0.6f));
		LEFT_ATTACK_POSITIONS.put(4, new Vector2(1f, 0.7f));
		
		RIGHT_ATTACK_ANGLES.put(0, 0f);
		RIGHT_ATTACK_ANGLES.put(1, 20f);
		RIGHT_ATTACK_ANGLES.put(2, 160f);
		RIGHT_ATTACK_ANGLES.put(3, 240f);
		RIGHT_ATTACK_ANGLES.put(4, -45f);
		
		LEFT_ATTACK_ANGLES.put(0, 0f);
		LEFT_ATTACK_ANGLES.put(1, -20f);
		LEFT_ATTACK_ANGLES.put(2, -160f);
		LEFT_ATTACK_ANGLES.put(3, -240f);
		LEFT_ATTACK_ANGLES.put(4, 45f);		
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
	
		idleTextureRight = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight()/1)[0][0];
		idleTextureLeft = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight()/1)[0][0];
		idleTextureLeft.flip(true, false);
		
		walkTextureRight = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight()/1)[0][1];
		walkTextureLeft = TextureRegion.split(spriteSheet, spriteSheet.getWidth()/2, spriteSheet.getHeight()/1)[0][1];
		walkTextureLeft.flip(true, false);						
	}

	public void render(Batch batch, Player player) {
		
		TextureRegion currentTexture = player.getDirection()==DirectionEnum.RIGHT ? walkTextureRight : walkTextureLeft;				
		setX(player.getDirection()==DirectionEnum.RIGHT ? player.getX()-1.8f : player.getX()+1.8f);	
		setY(player.getY());
		
		if (player.getState()==SpriteMoveEnum.IDLE) {	
			currentTexture = player.getDirection()==DirectionEnum.RIGHT ? idleTextureRight : idleTextureLeft;
			setX(player.getDirection()==DirectionEnum.RIGHT ? player.getX()-1.5f : player.getX()+1.5f);
		} else if (player.getState()==SpriteMoveEnum.JUMPING) {			
			setY(player.getY()+Y_JUMPING_POSITIONS.get(player.getCurrentAnimation().getKeyFrameIndex(player.getStateTime())));						
		} else if (player.getState()==SpriteMoveEnum.FALLING) {
			setY(player.getY()+0.4f);
		}
		
		float originX = 2;
		float originY = 0.5f;
		
		if (player.isAttacking()) {
			int animIdx = player.getCurrentAnimation().getKeyFrameIndex(player.getStateTime());
			if (player.getDirection()==DirectionEnum.RIGHT) {
				setX(player.getX()+RIGHT_ATTACK_POSITIONS.get(animIdx).x);
				setY(player.getY()+RIGHT_ATTACK_POSITIONS.get(animIdx).y);
				setRotation(RIGHT_ATTACK_ANGLES.get(animIdx));				
			} else {
				originX = 0;
				originY = 0.5f;
				setX(player.getX()+LEFT_ATTACK_POSITIONS.get(animIdx).x);
				setY(player.getY()+LEFT_ATTACK_POSITIONS.get(animIdx).y);
				setRotation(LEFT_ATTACK_ANGLES.get(animIdx));		
			}
			
			polygonBounds = new Polygon(new float[]{
					getX(), getY(),
					getX(), getY() + getHeight(), 
					getX() + getWidth(), getY()+getHeight(),
					getX() + getWidth(), getY()
					});		
			polygonBounds.setOrigin(getX(), getY() + 0.5f);
			polygonBounds.setRotation(getRotation());
			
			/*setX(player.getX()-1.5f);
			setY(player.getY());
			originX = 2.5f;
			originY = 0.5f;
			setRotation(getRotation()>=360 ? 360 : getRotation()+16f);*/
		} else {
			setRotation(0);
			if (player.isOnCurvedTile()) {
				if (player.getDirection()==DirectionEnum.RIGHT) {
					this.setRotation(player.isPositiveCurvedTile()?20:-20); 
				} else {
					originX = 0;
					originY = 0.5f;
					this.setRotation(player.isPositiveCurvedTile()?-20:20);
				}
				
			} else {
				this.setRotation(0);
			}
		}		
					
		batch.draw(currentTexture, getX() , getY(), originX, originY , renderingSize.x, renderingSize.y, 1,1, getRotation());
		
		
	}

}
