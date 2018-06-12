package com.game.core.sprite.sfx.wall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.game.core.board.BoardSquare;
import com.game.core.sprite.sfx.AbstractSfxSprite;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.PieceType;

public abstract class AbstractWallPiece extends AbstractSfxSprite {

	private static final float WIDTH = ScreenConstants.SQUARE_WIDTH/2;
	
	private static final float HEIGHT = ScreenConstants.SQUARE_WIDTH/2;	

	protected static final float X_ACCELERATION_COEFF = 100;
	
	protected static final float Y_ACCELERATION_COEFF = 0.5f;
	
	protected float rotationAngle;
	
	protected PieceType pieceType;

	public AbstractWallPiece(BoardSquare boardSquare, float x, float y, Vector2 acceleration) {
		super(x, y, new Vector2(WIDTH, HEIGHT));									
		this.acceleration = acceleration;				
		this.moveable = true;		
		this.gravitating = true;		
		this.isAnimationLooping = false;
		this.setOrigin(WIDTH/2, HEIGHT/2);		
		this.rotationAngle = (MathUtils.random(1)==1 ? 1 : -1) * (MathUtils.random(40) + 5);	
		this.pieceType = boardSquare.getPieceType();
		
		initializeAnimationsForPiece();
	}
	
	@Override
	public void move(float deltaTime) {			
		super.move(deltaTime);
		this.rotateBy(this.rotationAngle);		
	}

	@Override
	public void initializeAnimations() {			
	}
		
	public void initializeAnimationsForPiece() {
		spriteSheet = new Texture(Gdx.files.internal("sprites/pieces/"+pieceType.getImage()));
		TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 4, spriteSheet.getHeight() / 4);
		TextureRegion[] frame = new TextureRegion[1];
		frame[0] = tmp[0][0];			
		currentAnimation = new Animation(0, frame);		
	}

	@Override
	public void render(SpriteBatch batch) {		
		batch.begin();
		batch.draw(currentFrame, getX(), getY(), this.getOriginX(), this.getOriginY(), renderingSize.x, renderingSize.y, 1,1, this.getRotation());
		batch.end();		
	}
	
	
	
}
