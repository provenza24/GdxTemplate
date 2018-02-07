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
import com.game.core.sprite.sfx.wall.BottomLeftWallPiece;
import com.game.core.sprite.sfx.wall.BottomRightWallPiece;
import com.game.core.sprite.sfx.wall.TopLeftWallPiece;
import com.game.core.sprite.sfx.wall.TopRightWallPiece;
import com.game.core.util.DirectionType;
import com.game.core.util.constants.KeysConstants;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.ScreenEnum;
import com.game.core.util.enums.ScreenStateEnum;

public class GameScreen extends AbstractGameScreen  {
	
	private static final float KEY_DOWN_DELAY = 0.05f;
	
	private static final float PIECE_FALL_DELAY = 0.4f;
		
	private float keyDownDelay = 0;
	
	private SpriteBatch spriteBatch;
			
	private BitmapFont debugFont;
		
	private boolean debugShowText = true;

	private boolean debugShowFps = true;
	
	private boolean showNextPiece = true;
	
	private boolean showPieceProjection = true;
	
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
		
		/*for (int i=0;i<9;i++) {
			board.getBoard()[i][0] = new BoardSquare(0, PieceType.BAR);
			board.getBoard()[i][1] = new BoardSquare(0, PieceType.J_BLOCK);
			board.getBoard()[i][2] = new BoardSquare(0, PieceType.L_BLOCK);
		}*/		
	}
		
	@Override
	public void render(float delta) {		
		
		// Common time counter used to update synchronized sprites animations
		AbstractSprite.updateCommonStateTime(delta);
		
		// Regarding screen state:
		switch(screenState){
		case RUNNING:			
			// Game state running, two cases:
			if (levelFinished) {
				// The level is finished, go back to the Menu
				GameManager.getGameManager().setScreen(ScreenEnum.LEVEL_MENU);
			} else {
				// Otherwise render the game
				renderLogic(delta);
				// Draw the scene on screen
				drawScene(delta);
			}
			break;
		case TRANSITION:		
			break;
		case DELETING_LINES:
			// 1 or several lines are being deleting (animation is playing on screen)
			if (sfxSprites.size()==0) {
				// If all sprites are out of screen (blocs are exploding), end the cinematic
				screenState = ScreenStateEnum.RUNNING;				
				createNewPiece();
			} else {
				// Else render the cinematic (move explosion sprites on screen)
				renderExplosionCinematic(delta);
			}
			break;
		}
							
	}	

	private void renderLogic(float delta) {
		
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
				// Check if lines have to be suppressed
				toSuppress = board.getLinesToSuppress(currentPiece);						
				if (toSuppress[4] > 0) {
					for (int i=0; i<4; i++) {
						// For each line having to be suppressed, add blocs sprites with an exploding animation
						if (toSuppress[i]!=-1) {							
							for (int j=0;j<10;j++) {								
								BoardSquare boardSquare = board.getBoard()[j][toSuppress[i]];								
								sfxSprites.add(new TopLeftWallPiece(boardSquare, (j+1)*ScreenConstants.SQUARE_WIDTH, toSuppress[i]*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2));																	
								sfxSprites.add(new TopRightWallPiece(boardSquare, (j+1)*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2, toSuppress[i]*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2));										
								sfxSprites.add(new BottomRightWallPiece(boardSquare,(j+1)*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2, toSuppress[i]*ScreenConstants.SQUARE_WIDTH));																	
								sfxSprites.add(new BottomLeftWallPiece(boardSquare, (j+1)*ScreenConstants.SQUARE_WIDTH, toSuppress[i]*ScreenConstants.SQUARE_WIDTH));
							}							
						}
					}
					for (int i = 0; i < 4; i++) {						
						if (toSuppress[i] != -1) {
							// Suppress all line having to be suppressed from board
							board.deleteLine(toSuppress[i]);							
						}
					}
					// Screen state is changing to DELETING_LINES to play the corresponding animation
					screenState = ScreenStateEnum.DELETING_LINES;					
				}
				if (screenState!=ScreenStateEnum.DELETING_LINES) {
					// Create and add a new random piece to game board
					createNewPiece();
				}
			}
		}			
	}	
	
	private void drawScene(float delta) {
		// Draw the scene
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
		board.render(spriteBatch);
		currentPiece.render(spriteBatch);
		if (showPieceProjection) {
			board.renderProjection(spriteBatch, currentPiece);
		}
		if (showNextPiece) {
			nextPiece.renderNextPiece(spriteBatch);
		}
					
		// Render debug mode
		renderDebugMode();
	}
	
	private void renderExplosionCinematic(float delta) {
		// Draw the scene
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
		board.render(spriteBatch);		
		if (showNextPiece) {
			nextPiece.renderNextPiece(spriteBatch);
		}
		
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

	private void createNewPiece() {
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
			showNextPiece = !showNextPiece;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F2)) {
			showPieceProjection = !showPieceProjection;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F12)) {
			debugShowFps = !debugShowFps;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F11)) {
			debugShowText = !debugShowText;
		}	
		
		if (Gdx.input.isKeyJustPressed(Keys.F9)) {
			debugShowBounds = !debugShowBounds;
		}	
		
		if (Gdx.input.isKeyJustPressed(Keys.F10)) {		
			currentDebugColor++;
			currentDebugColor = fontColors.length == currentDebugColor ? 0 : currentDebugColor;
			debugFont.setColor(fontColors[currentDebugColor]);
			DEBUG_BOUNDS_COLOR = debugBounds[currentDebugColor];
		}
		
		/*if (Gdx.input.isKeyJustPressed(Keys.F12)) {
			PIECE_FALL_DELAY = PIECE_FALL_DELAY == 0.2f ? 10 : 0.2f;
		}*/
				
	}
		
	private void renderDebugMode() {
						
		if (debugShowText) {	
			int x = 10;
			int y = 17*ScreenConstants.SQUARE_WIDTH;						
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
