package com.game.core.tilemap;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.game.core.sprite.AbstractItem;
import com.game.core.sprite.impl.item.Flag;
import com.game.core.sprite.impl.player.Player;
import com.game.core.util.constants.TilemapConstants;
import com.game.core.util.enums.BackgroundTypeEnum;
import com.game.core.util.enums.CameraEnum;

public class TmxMap {

	private TiledMap map;

	private TiledMapTileLayer tileLayer;

	private MapLayer objectsLayer;
	
	private MapProperties properties;
	
	private Array<BackgroundTypeEnum> backgroundTypesEnum;

	private Player player;
	
	private Vector2 dimensions;
	
	private CameraEnum cameraEnum;

	private Flag flag;
	
	private List<AbstractItem> items;
	
	public TmxMap(String levelName) {
		map = new TmxMapLoader().load(levelName);
		tileLayer = (TiledMapTileLayer) map.getLayers().get(0);
		objectsLayer = map.getLayers().get(1);
		properties = tileLayer.getProperties();
		initBackgrounds();
		initMapObjects();
		dimensions = new Vector2((Integer)map.getProperties().get("width"), (Integer)map.getProperties().get("height"));
		cameraEnum = CameraEnum.valueOf(((String) properties.get(TilemapConstants.CAMERA)).toUpperCase());		
	}

	private void initBackgrounds() {

		backgroundTypesEnum = new Array<BackgroundTypeEnum>();
		try {
			String backgrounds[] = ((String) properties.get(TilemapConstants.BACKGROUNDS)).toUpperCase().split(",");		
			for (String background : backgrounds) {
				if (background!=null && background.compareTo("")!=0)
					backgroundTypesEnum.add(BackgroundTypeEnum.valueOf(background.toUpperCase()));
			}
		} catch(Exception e) {
			Gdx.app.log("TMX MAP LOADING", "Initialising backgrounds failure.");
		}
	}

	private void initMapObjects() {

		items = new ArrayList<AbstractItem>();
		
		MapObjects objects = objectsLayer.getObjects();
		
		for (MapObject mapObject : objects) {

			MapProperties objectProperty = mapObject.getProperties();
		
			if (objectProperty.get("type").toString().equals("player")) {
				player = new Player(mapObject);
			}			
			if (objectProperty.get("type").toString().equals("flag")) {
				flag = new Flag(mapObject, new Vector2());
				items.add(flag);
			}
		}
	}

	public boolean isCollisioningTileAt(int x, int y) {
		Cell cell = tileLayer.getCell(x, y);
		if (cell != null) {
			return cell.getTile().getId() >= 200 && cell.getTile().getId()!=391 && cell.getTile().getId()!=392 && cell.getTile().getId()!=393 && cell.getTile().getId()!=394;
		}
		return false;
	}

	public Cell getTileAt(int x, int y) {
		Cell cell = tileLayer.getCell(x, y);
		return cell;
	}

	public void changeCellValue(int x, int y, int value) {
		Cell cell = tileLayer.getCell(x, y);
		cell.setTile(map.getTileSets().getTile(value));
	}

	public void removeCell(int x, int y) {
		tileLayer.setCell(x, y, null);
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

	public TiledMapTileLayer getTileLayer() {
		return tileLayer;
	}

	public void setTileLayer(TiledMapTileLayer tileLayer) {
		this.tileLayer = tileLayer;
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

}
