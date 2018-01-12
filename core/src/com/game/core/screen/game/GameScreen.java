package com.game.core.screen.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.game.core.GameManager;
import com.game.core.sprite.board.Board;
import com.game.core.sprite.board.BoardSquare;
import com.game.core.sprite.piece.IPiece;
import com.game.core.sprite.sfx.AbstractSfxSprite;
import com.game.core.sprite.sfx.AbstractSprite;
import com.game.core.sprite.sfx.wall.AbstractWallPiece;
import com.game.core.sprite.sfx.wall.BottomLeftWallPiece;
import com.game.core.sprite.sfx.wall.BottomRightWallPiece;
import com.game.core.sprite.sfx.wall.TopLeftWallPiece;
import com.game.core.sprite.sfx.wall.TopRightWallPiece;
import com.game.core.util.DirectionType;
import com.game.core.util.constants.KeysConstants;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.PieceType;
import com.game.core.util.enums.ScreenEnum;
import com.game.core.util.enums.ScreenStateEnum;

public class GameScreen extends AbstractGameScreen  {
	
	private static final float LINE_DELETED_ANIMATION_DELAY = 3f;
	
	private static final float LINE_DELETED_DRAW_ANIMATION_DELAY = 0.25f;
	
	private static final float KEY_DOWN_DELAY = 0.05f;
	
	private static final float PIECE_FALL_DELAY = 0.4f;
	
	private float lineDeletedDelay = 0;
	
	private float lineDeletedDrawDelay = 0;
	
	private boolean displayDeletedLines = true;				
	
	private float keyDownDelay = 0;
	
	private SpriteBatch spriteBatch;
			
	private BitmapFont debugFont;
		
	private boolean debugShowText = true;

	private boolean debugShowFps = true;
	
	private boolean levelFinished = false;		

	private Board board = new Board();
	
	private float currentKeyPressDelay;
	
	private float KEY_PRESS_DELAY;
	
	private IPiece currentPiece;
	
	private IPiece nextPiece;
	
	private float currentPieceFallDelay;
	
	private boolean fallFaster = false;
	
	private boolean debugShowBounds = true;
	
	private List<AbstractSfxSprite> sfxSprites = new ArrayList<AbstractSfxSprite>();
	
	/** Used in debug mode to draw bounding boxes of sprites */
	private ShapeRenderer shapeRenderer;
	
	private int toSuppress[];
	
	public GameScreen() {
										
		// Initialize fonts
		debugFont = new BitmapFont();		
		debugFont.setColor(fontColors[currentDebugColor]);	
		DEBUG_BOUNDS_COLOR = debugBounds[currentDebugColor];
		
		// Sprite batch, used to draw background and debug text 
		spriteBatch = new SpriteBatch();
							
		//stage = new Stage();			
		
		currentPiece = board.createPiece();
		nextPiece = board.createPiece();		
		
		shapeRenderer = new ShapeRenderer();
		
		for (int i=0;i<9;i++) {
			board.getBoard()[i][0] = new BoardSquare(0, PieceType.BAR);
			board.getBoard()[i][1] = new BoardSquare(0, PieceType.J_BLOCK);
			board.getBoard()[i][2] = new BoardSquare(0, PieceType.L_BLOCK);
		}		
	}
		
	@Override
	public void render(float delta) {		
		
		// Common time counter used to update synchronized sprites animations
		AbstractSprite.updateCommonStateTime(delta);
		
		switch(screenState){
		case RUNNING:			
			if (levelFinished) {
				GameManager.getGameManager().setScreen(ScreenEnum.LEVEL_MENU);
			} else {
				renderGame(delta);
			}
			break;
		case TRANSITION:		
			break;
		case DELETING_LINES:
			lineDeletedDelay += delta;
			//if (lineDeletedDelay>=LINE_DELETED_ANIMATION_DELAY) {
			if (sfxSprites.size()==0) {
				endDeletingLinesCinematic();
			} else {
				renderDeletingLinesCinematic(toSuppress, displayDeletedLines, delta);
			}
			break;
		}
							
	}	

	private void renderGame(float delta) {
		
		keyDownDelay +=delta;
		currentKeyPressDelay += delta;
		currentPieceFallDelay += delta;
		
		// Handle input keys
		handleInput();
		handleDebugKeys();
			
		// The piece is falling
		if (currentPieceFallDelay>=PIECE_FALL_DELAY || fallFaster) {
			// If fall delay passed, make it fall
			currentPieceFallDelay = 0;
			currentPiece.translate(DirectionType.DOWN);
			if (!board.isAcceptable(currentPiece)) {
				// If piece can't fall, pose piece on board and create new one
				currentPiece.undoTranslation();
				board.posePiece(currentPiece);								
				toSuppress = board.getLinesToSuppress(currentPiece);						
				if (toSuppress[4] > 0) {
					for (int i=0; i<4; i++) {
						if (toSuppress[i]!=-1) {							
							for (int j=0;j<10;j++) {								
								sfxSprites.add(new TopLeftWallPiece(j*ScreenConstants.SQUARE_WIDTH, toSuppress[i]*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2));																	
								sfxSprites.add(new TopRightWallPiece(j*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2, toSuppress[i]*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2));										
								sfxSprites.add(new BottomRightWallPiece(j*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2, toSuppress[i]*ScreenConstants.SQUARE_WIDTH));																	
								sfxSprites.add(new BottomLeftWallPiece(j*ScreenConstants.SQUARE_WIDTH, toSuppress[i]*ScreenConstants.SQUARE_WIDTH));
							}							
						}
					}
					lineDeletedDelay = 0;
					for (int i = 0; i < 4; i++) {						
						if (toSuppress[i] != -1) {
							board.deleteLine(toSuppress[i]);							
						}
					}
					screenState = ScreenStateEnum.DELETING_LINES;					
				}
				if (screenState!=ScreenStateEnum.DELETING_LINES) {										
					currentPiece = nextPiece;
					nextPiece = board.createPiece();							
					if (!board.isAcceptable(currentPiece)) {
						GameManager.getGameManager().setScreen(ScreenEnum.MAIN_MENU);
					}
				}
			}
		}		
		drawScene(delta);
	}	
	
	private void drawScene(float delta) {
		// Draw the scene
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
		board.render(spriteBatch);
		currentPiece.render(spriteBatch);
		nextPiece.renderNextPiece(spriteBatch);				
					
		// Render debug mode
		renderDebugMode();
	}
	
	private void renderDeletingLinesCinematic(int[] toSuppress, boolean drawDeletedLines, float delta) {
		// Draw the scene
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
		board.render(spriteBatch);
		//board.render(spriteBatch,  toSuppress, drawDeletedLines);
		//currentPiece.render(spriteBatch);
		nextPiece.renderNextPiece(spriteBatch);		
		
		handleSfxSprites(delta);
									
		// Render debug mode
		renderDebugMode();
	}
			
	private void handleSfxSprites(float deltaTime) {	
	for (int i = 0; i < sfxSprites.size(); i++) {
			AbstractSprite sfxSprite = sfxSprites.get(i);			
			sfxSprite.update(deltaTime);			
			if (sfxSprite.isDeletable()) {				
				sfxSprites.remove(i--);
			} else if (sfxSprite.isVisible()) {
				sfxSprite.render(spriteBatch);
			}
		}
	}
	
	private void endDeletingLinesCinematic() {
		screenState = ScreenStateEnum.RUNNING;				
		currentPiece = nextPiece;
		nextPiece = board.createPiece();							
		if (!board.isAcceptable(currentPiece)) {
			GameManager.getGameManager().setScreen(ScreenEnum.MAIN_MENU);
		}
	}
	
	private void handleInput() {
			
		boolean right = false;
		boolean left = false;
		boolean keyRotationRight = false;
		boolean keyRotationLeft = false;
		fallFaster = false;
		
		if (Gdx.input.isKeyJustPressed(KEY_RIGHT)) {		
			right = true;
		} else 
		if (Gdx.input.isKeyJustPressed(KEY_LEFT)) {
			left = true;
		} 
		if (Gdx.input.isKeyJustPressed(KEY_A)) {
			keyRotationRight = true;
		} 
		if (Gdx.input.isKeyJustPressed(KEY_B)) {
			keyRotationLeft = true;
		}				
		
		if (Gdx.input.isKeyPressed(KeysConstants.KEY_DOWN)) {
			if (keyDownDelay>KEY_DOWN_DELAY) {
				keyDownDelay = 0;
				fallFaster = true;
			} 
		}
		
		if (right || left) {
			if (currentKeyPressDelay>=KEY_PRESS_DELAY) {
				currentKeyPressDelay = 0;
				currentPiece.translate(right ? DirectionType.RIGHT : DirectionType.LEFT);
				if (!board.isAcceptable(currentPiece)) {
					currentPiece.undoTranslation();
				}
			}
		} 
		if (keyRotationRight || keyRotationLeft) {
			if (currentKeyPressDelay>=KEY_PRESS_DELAY) {
				currentKeyPressDelay = 0;
				currentPiece.rotate(keyRotationRight);
				if (!board.isAcceptable(currentPiece)) {
					currentPiece.undoRotation();
				}
			}
		}
	}
	
	private void handleDebugKeys() {
		
		if (Gdx.input.isKeyJustPressed(Keys.F1)) {
			debugShowFps = !debugShowFps;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F2)) {
			debugShowText = !debugShowText;
		}	
		
		if (Gdx.input.isKeyJustPressed(Keys.F4)) {
			debugShowBounds = !debugShowBounds;
		}	
		
		if (Gdx.input.isKeyJustPressed(Keys.F3)) {		
			currentDebugColor++;
			currentDebugColor = fontColors.length == currentDebugColor ? 0 : currentDebugColor;
			debugFont.setColor(fontColors[currentDebugColor]);
			DEBUG_BOUNDS_COLOR = debugBounds[currentDebugColor];
		}
		

		if (Gdx.input.isKeyJustPressed(Keys.F5)) {
			
			AbstractWallPiece topLeftPiece = new TopLeftWallPiece(5*16, 9*16+0.5f); 
			sfxSprites.add(topLeftPiece);			
			
			AbstractWallPiece topRightPiece = new TopRightWallPiece(5*16+0.5f, 9*16+0.5f); 
			sfxSprites.add(topRightPiece);			
			
			AbstractWallPiece bottomRightPiece = new BottomRightWallPiece(5*16+0.5f, 9*16); 
			sfxSprites.add(bottomRightPiece);			
			
			AbstractWallPiece bottomLeftPiece = new BottomLeftWallPiece(5*16, 9*16); 
			sfxSprites.add(bottomLeftPiece);			
			
			
			/*sfxSprites.add(new TopLeftWallPiece(5*16, 9*16));
			sfxSprites.add(new BottomLeftWallPiece(5*16, 9*16));			
			sfxSprites.add(new TopRightWallPiece(5*16, 9*16));
			sfxSprites.add(new BottomRightWallPiece(5*16, 9*16));*/
		}	
		
		/*if (Gdx.input.isKeyJustPressed(Keys.F12)) {
			PIECE_FALL_DELAY = PIECE_FALL_DELAY == 0.2f ? 10 : 0.2f;
		}*/
				
	}
		
	private void renderDebugMode() {
						
		if (debugShowText) {	
			int x = 10;
			int y = 9*ScreenConstants.SQUARE_WIDTH;						
			spriteBatch.begin();
			int alive = 0;
			for (AbstractSfxSprite sfxSprite : sfxSprites) {
				alive += sfxSprite.isAlive() ? 1 : 0;
			}
			debugFont.draw(spriteBatch, "Sfx: " + sfxSprites.size() + " - " + alive + " alive", x, y);
			spriteBatch.end();
			y = y -20;
		}
		
		if (debugShowFps) {
			spriteBatch.begin();
			debugFont.draw(spriteBatch, Integer.toString(Gdx.graphics.getFramesPerSecond()), ScreenConstants.WIDTH - 20, ScreenConstants.HEIGHT-10);
			spriteBatch.end();
		}
		
		if (debugShowBounds) {
			// Green rectangle around Player
			spriteBatch.begin();
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			for (int i = 0; i < ScreenConstants.BOARD_WIDTH +1; i++) {
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(DEBUG_BOUNDS_COLOR);
				shapeRenderer.rectLine(new Vector2((i+ScreenConstants.BOARD_LEFT_SPACE)*ScreenConstants.SQUARE_WIDTH,0), new Vector2((i+ScreenConstants.BOARD_LEFT_SPACE)*ScreenConstants.SQUARE_WIDTH,ScreenConstants.BOARD_HEIGHT*ScreenConstants.SQUARE_WIDTH),1);			
				shapeRenderer.end();				
			}
			for (int i = 0; i < ScreenConstants.BOARD_HEIGHT; i++) {
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(DEBUG_BOUNDS_COLOR);
				shapeRenderer.rectLine(new Vector2(ScreenConstants.BOARD_LEFT_SPACE * ScreenConstants.SQUARE_WIDTH, i*ScreenConstants.SQUARE_WIDTH), new Vector2(ScreenConstants.BOARD_LEFT_SPACE * ScreenConstants.SQUARE_WIDTH + ScreenConstants.BOARD_WIDTH * ScreenConstants.SQUARE_WIDTH ,i*ScreenConstants.SQUARE_WIDTH),1);			
				shapeRenderer.end();				
			}
			
			
			Gdx.gl.glDisable(GL20.GL_BLEND);
			spriteBatch.end();
		}
		
	}
	
	@Override
	public void pause() {		
		screenState = ScreenStateEnum.TRANSITION;
	}

	@Override
	public void resume() {
		screenState = ScreenStateEnum.RUNNING;
	}

	@Override
	public void dispose() {							
		debugFont.dispose();
		spriteBatch.dispose();		
	}

}
