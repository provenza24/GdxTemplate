package com.game.core.screen.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.game.core.GameManager;
import com.game.core.background.AbstractScrollingBackground;
import com.game.core.background.IScrollingBackground;
import com.game.core.camera.AbstractGameCamera;
import com.game.core.sprite.AbstractEnemy;
import com.game.core.sprite.AbstractItem;
import com.game.core.sprite.AbstractSprite;
import com.game.core.sprite.impl.item.Candy;
import com.game.core.sprite.impl.player.Player;
import com.game.core.tilemap.TmxMap;
import com.game.core.util.Level;
import com.game.core.util.constants.KeysConstants;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.BackgroundTypeEnum;
import com.game.core.util.enums.DirectionEnum;
import com.game.core.util.enums.ScreenEnum;
import com.game.core.util.enums.ScreenStateEnum;
import com.game.core.util.enums.SpriteMoveEnum;

public class GameScreen extends AbstractGameScreen  {
			
	/** KEYS CONSTANTS */
	private static final int KEY_LEFT =  KeysConstants.KEY_LEFT;
	
	private static final int KEY_RIGHT =  KeysConstants.KEY_RIGHT;
			
	//private static final int KEY_DOWN = KeysConstants.KEY_DOWN;
	
	private static final int KEY_UP = KeysConstants.KEY_UP;
	
	private static final int KEY_HIT = KeysConstants.KEY_A;
	
	private static Color DEBUG_BOUNDS_COLOR;
	
	private static final Color[] fontColors = new Color[]{Color.WHITE, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE};
	
	private static final Color[] debugBounds = new Color[]{new Color(1, 1, 1, 0.5f), new Color(0, 0, 0, 0.5f), new Color(1, 0, 0, 0.5f), new Color(0, 1, 0, 0.5f), new Color(0, 0, 1, 0.5f)};
	
	private static int currentDebugColor = 3;	
	
	/** The stage with actors */
	private Stage stage;
	
	/** Camera following Player */
	private AbstractGameCamera camera;
	
	/** Tilemap loaded from a TMX file */
	private TmxMap tilemap;

	/** Tilemap renderer: render tilemap and all sprites owned by the tilemap */
	private OrthogonalTiledMapRenderer tilemapRenderer;

	/** Sprite batch used to render fixed sprites (Status bar sprites) and text (debug, end scene text) */
	private SpriteBatch spriteBatch;
	
	/** Used in debug mode to draw bounding boxes of sprites */
	private ShapeRenderer shapeRenderer;
	
	/** Main batch used to draw elements */
	private Batch batch;

	/** Backgrounds displayed un game */
	private Array<IScrollingBackground> backgrounds;
	
	/** Show foreground */
	private boolean showForeground = true;
	
	/** Debug font */
	private BitmapFont debugFont;
	
	/** Debug parameters */
	private boolean debugShowText = false;

	private boolean debugShowBounds = false;

	private boolean debugShowFps = false;		
	
	private boolean showGrid = false;
	
	private Player player;
	
	private boolean canJump;
	
	private boolean levelFinished = false;		
	
	private float debugFontSize = ScreenConstants.PREFERED_WIDHT * 2f / 800;
					
	private List<AbstractSprite> sfxSprites;			
	
	public GameScreen(Level level) {
				
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		// Initialize fonts
		debugFont = new BitmapFont();		
		debugFont.getData().setScale(debugFontSize, debugFontSize);
		debugFont.setColor(fontColors[currentDebugColor]);	
		DEBUG_BOUNDS_COLOR = debugBounds[currentDebugColor];
		
		// Sprite batch, used to draw background and debug text 
		spriteBatch = new SpriteBatch();
	
		// Shape renderer, used to draw rectangles around sprites in debug mode
		shapeRenderer = new ShapeRenderer();
				
		// Load the tilemap, set the unit scale to 1/32 (1 unit == 32 pixels)
		tilemap = new TmxMap("tilemaps/"+level.getTmxName());
		// Renderer used to draw tilemap
		tilemapRenderer = new OrthogonalTiledMapRenderer(tilemap.getMap(), 1 / ScreenConstants.MAP_UNIT_PIXELS);				

		// Player
		player = tilemap.getPlayer();
		
		// create an orthographic camera, shows us 16x12 units of the world		
		camera = AbstractGameCamera.createCamera(tilemap.getCameraEnum(),player, tilemap.getDimensions());		
		
		//cameraSpriteBatch.setProjectionMatrix(camera.getCamera().combined);
		
		// Initialize backgrounds, which are defined in each TMX map with Tiled
		backgrounds = new Array<IScrollingBackground>();
		int i=0;
		for (BackgroundTypeEnum backgroundTypeEnum : tilemap.getBackgroundTypesEnum()) {
			//IScrollingBackground scrollingBackground = AbstractScrollingBackground.createScrollingBackground(camera, player, spriteBatch, backgroundTypeEnum, i==0 ? 16 : 24);
			IScrollingBackground scrollingBackground = AbstractScrollingBackground.createScrollingBackground(camera, player, spriteBatch, backgroundTypeEnum, i==0 ? 8 : 24, tilemap.isBackgroundScrollingVertically());
			backgrounds.add(scrollingBackground);			
			i++;
		}
		
		// Initialize stage, the stage is used for sprites actions
		stage = new Stage();	
		
		sfxSprites = new ArrayList<>();
		
		for (AbstractEnemy enemy : tilemap.getEnemies()) {
			enemy.setSfxSprites(sfxSprites);
		}
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
			renderTransitionScreen();
			break;
		}
					
	}

	private void renderGame(float delta) {
		AbstractSprite.updateCommonStateTime(delta);
		handleInput();		
		player.update(tilemap, camera.getCamera(), delta);
		player.getClub().update();
		// Draw the scene
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
		// Move camera
		camera.moveCamera();
		
		// Move backgrounds
		if (backgrounds!=null && backgrounds.size>0) {
			backgrounds.get(0).update();
			backgrounds.get(0).render();
			if (backgrounds.size>1) {
				backgrounds.get(1).update();
				backgrounds.get(1).render();
			}
		}							
		
		// Move dead enemies, render
		handleSfxSprites(delta);
		
		// Render tilemap
		tilemapRenderer.setView(camera.getCamera());
		tilemapRenderer.getBatch().begin();		
		tilemapRenderer.renderTileLayer(tilemap.getBackgroundLayer());
		tilemapRenderer.getBatch().end();
		
		// Move items, check collisions, render
		handleItems(delta);
		// Move enemies, check collisions, render
		handleEnemies(delta);		
		
		// Render Player		
		player.render(tilemapRenderer.getBatch());		
		
		// Draw stage for moving actors		
		stage.draw();
		
		// Draw foreground
		if (showForeground) {
			tilemapRenderer.getBatch().begin();
			tilemapRenderer.renderTileLayer(tilemap.getForegroundLayer());
			tilemapRenderer.getBatch().end();
		}
		
		// Render debug mode
		renderDebugMode();
	}

	private void handleSfxSprites(float delta) {
		for (int i = 0; i < sfxSprites.size(); i++) {
			AbstractSprite sprite = sfxSprites.get(i);
			sprite.update(tilemap, camera.getCamera(), delta);
			if (sprite.isDeletable()) {				
				sfxSprites.remove(i--);
			} else if (sprite.isVisible()) {
				sprite.render(tilemapRenderer.getBatch());
			}
		}
	}

	private void renderTransitionScreen() {
		
		player.setAcceleration(new Vector2());		
		player.setState(SpriteMoveEnum.IDLE);
		// Draw the scene
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);						
		// Render backgrounds
		if (backgrounds!=null && backgrounds.size>0) {
			backgrounds.get(0).render();
			if (backgrounds.size>1) {
				backgrounds.get(1).render();
			}
		}							
		// Render tilemap
		tilemapRenderer.setView(camera.getCamera());
		// Render background
		tilemapRenderer.getBatch().begin();
		tilemapRenderer.renderTileLayer(tilemap.getBackgroundLayer());		
		tilemapRenderer.getBatch().end();
		// Render items
		List<AbstractItem> items = tilemap.getItems();
		for (int i = 0; i < items.size(); i++) {
			AbstractItem item = items.get(i);						
			if (Math.abs(item.getX() - camera.getCamera().position.x)<=11 
					&& Math.abs(item.getY() - camera.getCamera().position.y)<=9) {
				item.render(tilemapRenderer.getBatch());
			}
		}
		// Render enemies
		List<AbstractEnemy> enemies = tilemap.getEnemies();
		for (int i = 0; i < enemies.size(); i++) {
			AbstractEnemy enemy = enemies.get(i);						
			if (Math.abs(enemy.getX() - camera.getCamera().position.x)<=11 
					&& Math.abs(enemy.getY() - camera.getCamera().position.y)<=9) {
				enemy.render(tilemapRenderer.getBatch());
			}
		}
			
		// Render Player		
		player.render(tilemapRenderer.getBatch());
		// Render foreground
		if (showForeground) {
			tilemapRenderer.getBatch().begin();
			tilemapRenderer.renderTileLayer(tilemap.getForegroundLayer());
			tilemapRenderer.getBatch().end();
		}		
		// Render debug mode
		//renderDebugMode();
	}
					
	private void handleInput() {
									
		if (!player.isAttacking()) {
			if (Gdx.input.isKeyPressed(KEY_RIGHT)) {
				if (player.getDirection() == DirectionEnum.LEFT) {
					// Sliding on the right				
					player.changeState(SpriteMoveEnum.SLIDING_LEFT);
					player.decelerate(1.5f);
					if (player.getAcceleration().x <= 0) {
						// Not sliding anymore
						player.getAcceleration().x = 0;
						player.setDirection(DirectionEnum.RIGHT);						
					}							
				} else {
					// Running right
					player.accelerate();
					player.setDirection(DirectionEnum.RIGHT);
					player.changeState(SpriteMoveEnum.RUNNING_RIGHT);
				}
			} else if (Gdx.input.isKeyPressed(KEY_LEFT)) {
				if (player.getDirection() == DirectionEnum.RIGHT) {
					// Sliding on the left	
					player.changeState(SpriteMoveEnum.SLIDING_RIGHT);
					player.decelerate(1.5f);
					if (player.getAcceleration().x <= 0) {
						// Not sliding anymore
						player.getAcceleration().x = 0;
						player.setDirection(DirectionEnum.LEFT);
					}							
				} else {
					// Running left, not crouched
					player.accelerate();
					player.setDirection(DirectionEnum.LEFT);
					player.changeState(SpriteMoveEnum.RUNNING_LEFT);
				} 
			} else {
				player.decelerate(1);			
			}
		}	
								
		if (Gdx.input.isKeyPressed(KEY_UP)) {
			if (player.getState()!=SpriteMoveEnum.JUMPING && player.getState()!=SpriteMoveEnum.FALLING 
					&& canJump && !player.isAttacking()) {
				player.setOnFloor(false);
				player.setState(SpriteMoveEnum.JUMPING);
				player.getAcceleration().y = 0.3f;		
				player.setOnSlopeTile(false);
				player.setOnCloudTile(false);
				player.setStateTime(0);
			}			
			canJump = false;
		} else {
			canJump = true;
		}
		
		if (Gdx.input.isKeyJustPressed(KEY_HIT) && !player.isAttacking()) {
			player.setStateTime(0);
			player.setAttacking(true);
			if (player.getState()!=SpriteMoveEnum.JUMPING && player.getState()!=SpriteMoveEnum.FALLING) {				
				player.getAcceleration().x = 0;
				player.setState(player.getDirection()== DirectionEnum.RIGHT ? SpriteMoveEnum.ATTACK_RIGHT: SpriteMoveEnum.ATTACK_LEFT);
			}
		}
		
		handleDebugKeys();
	}
	
	private void handleItems(float deltaTime) {
		List<AbstractItem> items = tilemap.getItems();		
		Candy.updateRotation();
		for (int i = 0; i < items.size(); i++) {
			AbstractItem item = items.get(i);						
			item.update(tilemap, camera.getCamera(), deltaTime);
			boolean collidePlayer = item.overlaps(player);						
			if (collidePlayer) {
				//levelFinished = true;
				item.collideWithPlayer(player);
			}
			if (item.isDeletable()) {				
				items.remove(i--);
			} else if (item.isVisible()) {
				item.render(tilemapRenderer.getBatch());
			}
		}
	}
	
	private void handleEnemies(float deltaTime) {

		List<AbstractEnemy> enemies = tilemap.getEnemies();		
		for (int i = 0; i < enemies.size(); i++) {
			AbstractEnemy enemy = enemies.get(i);
			enemy.update(tilemap, camera.getCamera(), deltaTime);
			// Draw it
			if (enemy.isAlive() && !enemy.isKilled()) {				
				if (player.isAttacking() && enemy.isKillable()) {						
					if (player.getClub().overlaps(enemy) && enemy.hit()) {
						sfxSprites.add(enemy.generateDeadSprite(player.getDirection()));							
					}
				}
				if (!player.isInvincible() && enemy.overlaps(player)) {					
					enemy.hitPlayer(player);	
					Gdx.app.log("GameScreen::handleEnemies", "1 point de vie en moins !!!");				
				}								
			}
			if (enemy.isDeletable()) {				
				enemies.remove(i--);
			} else if (enemy.isVisible()) {
				enemy.render(tilemapRenderer.getBatch());
			}

		}
	}

	private void handleDebugKeys() {
		
		if (Gdx.input.isKeyJustPressed(Keys.F1)) {
			showForeground = !showForeground;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F2)) {
			debugShowFps = !debugShowFps;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F3)) {
			debugShowText = !debugShowText;
		}						

		if (Gdx.input.isKeyJustPressed(Keys.F4)) {
			debugShowBounds = !debugShowBounds;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F5)) {
			showGrid = !showGrid;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F6)) {		
			currentDebugColor++;
			currentDebugColor = fontColors.length == currentDebugColor ? 0 : currentDebugColor;
			debugFont.setColor(fontColors[currentDebugColor]);
			DEBUG_BOUNDS_COLOR = debugBounds[currentDebugColor];
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_6)) {
			player.setX(player.getX()+6);
			camera.getCamera().position.x+=6;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_8)) {
			player.setY(player.getY()+6);
			camera.getCamera().position.y+=6;
		}
		
		
	}
		
	private void renderDebugMode() {
						
		if (debugShowText) {
			
			int x = 10;
			int y = ScreenConstants.HEIGHT-10;
						
			spriteBatch.begin();
			debugFont.draw(spriteBatch, "position=" + String.format("%.3f", player.getX()) + " | " + String.format("%.3f", player.getY()), x, y);
			y = y -20;
			debugFont.draw(spriteBatch, "acceleration=" + String.format("%.1f", player.getAcceleration().x) + " | " + String.format("%.1f", player.getAcceleration().y), x, y);
			y = y -20;
			debugFont.draw(spriteBatch, "state=" + player.getState().toString() + " / direction=" + player.getDirection().toString() + " / onFloor=" + player.isOnFloor(), x, y);
			y = y -20;			
			debugFont.draw(spriteBatch, "hiting= " + player.isAttacking() + " / Invincibe="+player.isInvincible(), x, y);
			y = y -20;							
			debugFont.draw(spriteBatch, "slopeTile= " + player.isOnSlopeTile() + " / slopePositiveTile= " + player.isPositiveSlopeTile(), x, y);
			y = y -20;			
			debugFont.draw(spriteBatch, "cloudTile= " + player.isOnCloudTile(), x, y);			
			y = y -20;
			debugFont.draw(spriteBatch, "camera.type=" + tilemap.getCameraEnum(), x, y);			
			y = y -20;
			debugFont.draw(spriteBatch, "camera.position=" + String.format("%.3f", camera.getCamera().position.x) + " | " + String.format("%.3f", camera.getCamera().position.y), x, y);			
			y = y -20;			
			debugFont.draw(spriteBatch, "camera.offset=" + String.format("%.3f", camera.getCameraOffset()), x, y);			
			y = y -20;
			
			int alive = 0;
			int visible = 0;
			for (AbstractSprite enemy : tilemap.getEnemies()) {
				alive += enemy.isAlive() ? 1 : 0;
				visible += enemy.isVisible() ? 1 : 0;
			}
			debugFont.draw(spriteBatch, "Enemies: " + tilemap.getEnemies().size() + " - " + alive + "/"+visible + " alive/visible", x, y);
			
			y = y -20;
			
			alive = 0;
			visible = 0;
			for (AbstractSprite item : tilemap.getItems()) {
				alive += item.isAlive() ? 1 : 0;
				visible += item.isVisible() ? 1 : 0;
			}
			debugFont.draw(spriteBatch, "Items: " + tilemap.getItems().size() + " - " + alive + "/"+visible + " alive/visible", x, y);
					
			y = y -20;
			alive = 0;
			visible = 0;
			for (AbstractSprite enemy : sfxSprites) {
				alive += enemy.isAlive() ? 1 : 0;
				visible += enemy.isVisible() ? 1 : 0;
			}
			debugFont.draw(spriteBatch, "Dead: " + sfxSprites.size() + " - " + alive + "/"+visible + " alive/visible", x, y);
			
			y = y -20;
			
			spriteBatch.end();
		}
		
		if (debugShowFps) {
			spriteBatch.begin();
			debugFont.draw(spriteBatch, Integer.toString(Gdx.graphics.getFramesPerSecond()), ScreenConstants.WIDTH - 20, ScreenConstants.HEIGHT-10);
			spriteBatch.end();
		}

		if (debugShowBounds) {
			// Green rectangle around Player
			batch = tilemapRenderer.getBatch();
			batch.begin();
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			shapeRenderer.setProjectionMatrix(camera.getCamera().combined);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(DEBUG_BOUNDS_COLOR);
			shapeRenderer.rect(player.getX() + player.getOffset().x, player.getY(), player.getWidth(), player.getHeight());
			
			// Green rectangle around enemies
			for (AbstractSprite sprite : tilemap.getEnemies()) {
				shapeRenderer.rect(sprite.getX() + sprite.getOffset().x, sprite.getY(), sprite.getWidth(),
						sprite.getHeight());
			}
			// Green rectangle around items
			for (AbstractSprite sprite : tilemap.getItems()) {
				shapeRenderer.rect(sprite.getX() + sprite.getOffset().x, sprite.getY(), sprite.getWidth(),
						sprite.getHeight());
			}
			
			
			int widht = tilemap.getBackgroundLayer().getWidth();
			int height = tilemap.getBackgroundLayer().getHeight();
			for (int i=0;i<widht;i++) {
				if (Math.abs(i-camera.getCamera().position.x)<12) {
					for (int j=0;j<height;j++) {
						Cell cell = tilemap.getBackgroundLayer().getCell(i, j);
						if (cell!=null && tilemap.getSlopeTilesFunctions().get(cell.getTile().getId())!=null) {
							shapeRenderer.rect(i , j, 1, 1);
						}
					}
				}			
			}
			
			
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
									
			batch.end();
			
			// Green rectangle around club
			if (player.isAttacking()) {
				Polygon polygon = player.getClub().getPolygonBounds();
				if (polygon!=null) {
					shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
					shapeRenderer.polygon(polygon.getTransformedVertices());
					shapeRenderer.end();					
				}
			}			
			
		}
		
		if (showGrid) {			
			spriteBatch.begin();
			for (int i=1; i<100;i++) {
				drawDebugLine(new Vector2(i, 0), new Vector2(i, ScreenConstants.NB_HORIZONTAL_TILES), 1, Color.GREEN, camera.getCamera().combined);
			}			
			for (int i=1; i<ScreenConstants.NB_HORIZONTAL_TILES;i++) {
				drawDebugLine(new Vector2(0, i), new Vector2(100, i), 1, Color.GREEN, camera.getCamera().combined);
			}
			spriteBatch.end();
		}
	}
	
	private ShapeRenderer debugRenderer = new ShapeRenderer();
	
	public void drawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
		tilemap.dispose();		
		tilemapRenderer.dispose();		
		shapeRenderer.dispose();		
		debugFont.dispose();
		spriteBatch.dispose();		
	}

}
