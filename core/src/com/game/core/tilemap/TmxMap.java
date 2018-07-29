package com.game.core.tilemap;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.game.core.collision.math.MathFunction;
import com.game.core.sprite.AbstractEnemy;
import com.game.core.sprite.AbstractItem;
import com.game.core.sprite.impl.ennemy.Peak;
import com.game.core.sprite.impl.item.Flag;
import com.game.core.sprite.impl.item.Liana;
import com.game.core.sprite.impl.player.Player;
import com.game.core.util.constants.TilemapConstants;
import com.game.core.util.enums.BackgroundTypeEnum;
import com.game.core.util.enums.CameraEnum;
import com.game.core.util.enums.SpriteTypeEnum;
import com.game.core.util.enums.TileObjectEnum;
import com.game.core.util.enums.math.MathFunctionEnum;

public class TmxMap {

	private TiledMap map;

	private TiledMapTileLayer backgroundLayer;

	private TiledMapTileLayer foregroundLayer;

	private MapLayer objectsLayer;
	
	private MapProperties properties;
	
	private Array<BackgroundTypeEnum> backgroundTypesEnum;

	private Player player;
	
	private Vector2 dimensions;
	
	private CameraEnum cameraEnum;

	private Flag flag;
	
	private List<AbstractItem> items;		
	
	private List<AbstractEnemy> enemies;
	
	private List<Liana> lianaList;
	
	private List<Integer> cloudTiles;
	
	private Map<Integer, MathFunction> slopeTilesFunctions;
	
	private List<Integer> slopeContantTiles;
	
	private List<Integer> peakTiles;
	
	private boolean backgroundScrollingVertically;
	
	public TmxMap(String levelName) {
		map = new TmxMapLoader().load(levelName);
		backgroundLayer = (TiledMapTileLayer) map.getLayers().get(TilemapConstants.LAYER_NAME_BACKGROUND);
		foregroundLayer = (TiledMapTileLayer) map.getLayers().get(TilemapConstants.LAYER_NAME_FOREGROUND);		
		objectsLayer = map.getLayers().get(TilemapConstants.LAYER_NAME_OBJECTS);
		properties = backgroundLayer.getProperties();
		initBackgrounds();
		initMapObjects();
		initTilesProperties();
		createObjectFromTiles();
		dimensions = new Vector2((Integer)map.getProperties().get(TilemapConstants.MAP_PROPERTY_WIDTH), (Integer)map.getProperties().get(TilemapConstants.MAP_PROPERTY_HEIGHT));
		cameraEnum = CameraEnum.valueOf(((String) properties.get(TilemapConstants.MAP_PROPERTY_CAMERA)).toUpperCase());						
	}	

	private void initTilesProperties() {
		
		slopeTilesFunctions = new HashMap<Integer, MathFunction>();
		cloudTiles= new ArrayList<>();
		slopeContantTiles = new ArrayList<>();
		peakTiles = new ArrayList<>();		
		
		TiledMapTileSet tileset = map.getTileSets().getTileSet("stoneage");	
		for (TiledMapTile tiledMapTile : tileset) {			
			MapProperties props = tiledMapTile.getProperties();
			Iterator<String> keysIterator = props.getKeys();
			Iterator<Object> valuesIterator = props.getValues();
			while(keysIterator.hasNext()) {	
				String key = keysIterator.next();
				if (key.equalsIgnoreCase(TilemapConstants.TILE_PROPERTY_SLOPE)) {					
					String value = valuesIterator.next().toString().toUpperCase();
					MathFunctionEnum mathFunctionEnum = MathFunctionEnum.valueOf(value);
					slopeTilesFunctions.put(tiledMapTile.getId(), mathFunctionEnum.getMathFunction());
					Gdx.app.debug("SLOPE TILE", Integer.toString(tiledMapTile.getId()));
					if (mathFunctionEnum.isConstant()) {
						slopeContantTiles.add(tiledMapTile.getId());
					}									
				} else if (key.equalsIgnoreCase(TilemapConstants.TILE_PROPERTY_CLOUD)) {
					cloudTiles.add(tiledMapTile.getId());					
				} else if (key.equalsIgnoreCase(TilemapConstants.TILE_PROPERTY_PEAK)) {
					peakTiles.add(tiledMapTile.getId());
				} 
			}
		}
	}
	
	private void createObjectFromTiles() {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("foreground");
        for(int x = 0; x < layer.getWidth();x++){
            for(int y = 0; y < layer.getHeight();y++){
                TiledMapTileLayer.Cell cell = layer.getCell(x,y);
                if (cell!=null && peakTiles.contains(cell.getTile().getId())) {                
                	enemies.add(new Peak(new Vector2(x, y)));                	
                }                
            }
        }		
	}
	
	private void initBackgrounds() {			
		
		backgroundTypesEnum = new Array<BackgroundTypeEnum>();
		try {
			String backgrounds[] = ((String) properties.get(TilemapConstants.MAP_PROPERTY_BACKGROUNDS)).toUpperCase().split(",");		
			for (String background : backgrounds) {
				if (background!=null && background.compareTo("")!=0)
					backgroundTypesEnum.add(BackgroundTypeEnum.valueOf(background.toUpperCase()));
			}			
		} catch(Exception e) {
			Gdx.app.log("TMX MAP LOADING", "Initialising backgrounds failure.");
		}
		String verticalScroll = (String) properties.get("background.vertical.scrolling");
		backgroundScrollingVertically = Boolean.valueOf(verticalScroll);		
	}

	private void initMapObjects() {

		items = new ArrayList<AbstractItem>();
		enemies = new ArrayList<AbstractEnemy>();
		lianaList = new ArrayList<Liana>();
		
		MapObjects objects = objectsLayer.getObjects();
		
		for (MapObject mapObject : objects) {

			MapProperties objectProperty = mapObject.getProperties();
		
			if (objectProperty.get("type").toString().equals(TilemapConstants.TILE_TYPE_PLAYER)) {
				player = new Player(mapObject);
			} else {														
				try {
					TileObjectEnum tileObjectEnum = TileObjectEnum.valueOf(objectProperty.get("type").toString().toUpperCase());					
					Constructor<?> constructor = tileObjectEnum.getZclass().getConstructor(MapObject.class, Vector2.class);
					Object object = constructor.newInstance(mapObject, tileObjectEnum.getOffset());
					if (tileObjectEnum.getSpriteTypeEnum()==SpriteTypeEnum.ENEMY) {
						enemies.add((AbstractEnemy) object);
					} else if (tileObjectEnum.getSpriteTypeEnum()==SpriteTypeEnum.ITEM) {
						if (object instanceof Flag) {
							flag = (Flag)object;
						}
						items.add((AbstractItem) object);
					} else if (tileObjectEnum.getSpriteTypeEnum()==SpriteTypeEnum.LIANA) {
						lianaList.add((Liana)object);
					}
				} catch (Exception e) {					
					e.printStackTrace();	
				}
			}					
		}
	}

	public boolean isCollisioningTileAt(int x, int y) {
		Cell cell = backgroundLayer.getCell(x, y);
		if (cell != null) {
			return cell.getTile().getId() >= 220;								
		}
		return false;
	}

	public Cell getTileAt(int x, int y) {
		Cell cell = backgroundLayer.getCell(x, y);
		return cell;
	}

	public void changeCellValue(int x, int y, int value) {
		Cell cell = backgroundLayer.getCell(x, y);
		cell.setTile(map.getTileSets().getTile(value));
	}

	public void removeCell(int x, int y) {
		backgroundLayer.setCell(x, y, null);
	}

	public void dispose() {
		map.dispose();
	}

	/** Getters / Setters */

	public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}

	public TiledMapTileLayer getBackgroundLayer() {
		return backgroundLayer;
	}

	public void setBackgroundLayer(TiledMapTileLayer tileLayer) {
		this.backgroundLayer = tileLayer;
	}

	public Array<BackgroundTypeEnum> getBackgroundTypesEnum() {
		return backgroundTypesEnum;
	}

	public void setBackgroundTypesEnum(Array<BackgroundTypeEnum> backgroundTypesEnum) {
		this.backgroundTypesEnum = backgroundTypesEnum;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Vector2 getDimensions() {
		return dimensions;
	}

	public void setDimensions(Vector2 dimensions) {
		this.dimensions = dimensions;
	}

	public CameraEnum getCameraEnum() {
		return cameraEnum;
	}

	public void setCameraEnum(CameraEnum cameraEnum) {
		this.cameraEnum = cameraEnum;
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	public List<AbstractItem> getItems() {
		return items;
	}

	public void setItems(List<AbstractItem> items) {
		this.items = items;
	}

	public TiledMapTileLayer getForegroundLayer() {
		return foregroundLayer;
	}

	public void setForegroundLayer(TiledMapTileLayer foregroundLayer) {
		this.foregroundLayer = foregroundLayer;
	}

	public List<Integer> getCloudTiles() {
		return cloudTiles;
	}

	public void setCloudTiles(List<Integer> cloudTiles) {
		this.cloudTiles = cloudTiles;
	}
	
	public boolean isCloudTile(Integer idx) {
		return this.cloudTiles.contains(idx);
	}
	
	public boolean isSlopeConstantTile(Integer idx) {
		return this.slopeContantTiles.contains(idx);
	}

	public Map<Integer, MathFunction> getSlopeTilesFunctions() {
		return slopeTilesFunctions;
	}

	public void setSlopeTilesFunctions(Map<Integer, MathFunction> slopeTilesFunctions) {
		this.slopeTilesFunctions = slopeTilesFunctions;
	}

	public boolean isBackgroundScrollingVertically() {
		return backgroundScrollingVertically;
	}

	public void setBackgroundScrollingVertically(boolean backgroundScrollingVertically) {
		this.backgroundScrollingVertically = backgroundScrollingVertically;
	}

	public List<AbstractEnemy> getEnemies() {
		return enemies;
	}

	public void setEnemies(List<AbstractEnemy> enemies) {
		this.enemies = enemies;
	}

	public List<Liana> getLianaList() {
		return lianaList;
	}

	public void setLianaList(List<Liana> lianaList) {
		this.lianaList = lianaList;
	}


}
