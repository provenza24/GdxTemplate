package com.game.core.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.core.GameManager;
import com.game.core.sprite.board.Board;
import com.game.core.sprite.piece.IPiece;
import com.game.core.util.DirectionType;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.ScreenEnum;
import com.game.core.util.enums.ScreenStateEnum;

public class GameScreen extends AbstractGameScreen  {
		
	private Stage stage;
	
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
	
	private float PIECE_FALL_DELAY = 1f;
	
	private boolean debugShowBounds = true;
	
	/** Used in debug mode to draw bounding boxes of sprites */
	private ShapeRenderer shapeRenderer;
	
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
	}
		
	@Override
	public void render(float delta) {		
		
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
		}
					
	}

	private void renderGame(float delta) {
		
		currentKeyPressDelay += delta;
		currentPieceFallDelay += delta;
		
		// Handle input keys
		handleInput();
		handleDebugKeys();
			
		// The piece is falling
		if (currentPieceFallDelay>=PIECE_FALL_DELAY) {
			// If fall delay passed, make it fall
			currentPieceFallDelay = 0;
			currentPiece.translate(DirectionType.DOWN);
			if (!board.isAcceptable(currentPiece)) {
				// If piece can't fall, pose piece on board and create new one
				currentPiece.undoTranslation();
				board.posePiece(currentPiece);				
				
				int toSuppress[] = board.getLinesToSuppress(currentPiece);				
				if (toSuppress[4] > 0) {					
					for (int i = 0; i < 4; i++) {						
						if (toSuppress[i] != -1) {
							board.deleteLine(toSuppress[i]);							
						}
					}					
				}
				currentPiece = nextPiece;
				nextPiece = board.createPiece();				
			}
		}
		
		
		// Draw the scene
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
		board.render(spriteBatch);
		currentPiece.render(spriteBatch);
		nextPiece.renderNextPiece(spriteBatch);
					
		// Render debug mode
		renderDebugMode();
	}
					
	private void handleInput() {
			
		boolean right = false;
		boolean left = false;
		boolean keyRotationRight = false;
		boolean keyRotationLeft = false;
		
		if (Gdx.input.isKeyJustPressed(KEY_RIGHT)) {		
			right = true;
		} else 
		if (Gdx.input.isKeyJustPressed(KEY_LEFT)) {
			left = true;
		} else
		if (Gdx.input.isKeyJustPressed(KEY_A)) {
			keyRotationRight = true;
		} else
		if (Gdx.input.isKeyJustPressed(KEY_B)) {
			keyRotationLeft = true;
		}		
		
		if (right || left) {
			if (currentKeyPressDelay>=KEY_PRESS_DELAY) {
				currentKeyPressDelay = 0;
				currentPiece.translate(right ? DirectionType.RIGHT : DirectionType.LEFT);
				if (!board.isAcceptable(currentPiece)) {
					currentPiece.undoTranslation();
				}
			}
		} else if (keyRotationRight || keyRotationLeft) {
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
		
		if (Gdx.input.isKeyJustPressed(Keys.F12)) {		
			currentDebugColor++;
			currentDebugColor = fontColors.length == currentDebugColor ? 0 : currentDebugColor;
			debugFont.setColor(fontColors[currentDebugColor]);
			DEBUG_BOUNDS_COLOR = debugBounds[currentDebugColor];
		}
				
	}
		
	private void renderDebugMode() {
						
		if (debugShowText) {					
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
			
			for (int i = 0; i < ScreenConstants.BOARD_WIDTH; i++) {
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(DEBUG_BOUNDS_COLOR);
				shapeRenderer.rectLine(new Vector2(i*ScreenConstants.SQUARE_WIDTH,0), new Vector2(i*ScreenConstants.SQUARE_WIDTH,ScreenConstants.BOARD_HEIGHT*ScreenConstants.SQUARE_WIDTH),1);			
				shapeRenderer.end();				
			}
			for (int i = 0; i < ScreenConstants.BOARD_HEIGHT; i++) {
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(DEBUG_BOUNDS_COLOR);
				shapeRenderer.rectLine(new Vector2(0, i*ScreenConstants.SQUARE_WIDTH), new Vector2(ScreenConstants.BOARD_HEIGHT * ScreenConstants.SQUARE_WIDTH ,i*ScreenConstants.SQUARE_WIDTH),1);			
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
		stage.dispose();				
		debugFont.dispose();
		spriteBatch.dispose();		
	}

}
