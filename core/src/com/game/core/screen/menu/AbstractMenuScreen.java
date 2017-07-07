package com.game.core.screen.menu;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.core.screen.game.AbstractGameScreen;
import com.game.core.sprite.menu.DefaultSelector;

public abstract class AbstractMenuScreen extends AbstractGameScreen implements IMenuScreen {

	protected BitmapFont font;

	protected List<MenuItem> menuItems = new ArrayList<MenuItem>();
	
	protected Group backgroundGroup;
	
	protected Stage stage;
	
	private float horizontalMenuStart = Gdx.graphics.getWidth() / 2;

	private Vector2 offset;

	private float selectorHeight = 0;

	private int verticalMenuSpacing = 30;

	private float verticalMenuStart = Gdx.graphics.getHeight() / 2;	

	private int currentItem = 0;

	private Actor selector;
	
	private Group foregroundGroup;

	public <E extends Enum<?>> AbstractMenuScreen(Class<E> menuEnumClass) {
		this(menuEnumClass, null, null);
	}

	public <E extends Enum<?>> AbstractMenuScreen(Class<E> menuEnumClass, BitmapFont pFont) {
		this(menuEnumClass, pFont, null);
	}

	public <E extends Enum<?>> AbstractMenuScreen(Class<E> menuEnumClass, Class<?> selectorClass) {
		this(menuEnumClass, null, selectorClass);
	}

	public <E extends Enum<?>> AbstractMenuScreen(Class<E> menuEnumClass, BitmapFont pFont, Class<?> selectorClass) {

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		font = pFont != null ? pFont : new BitmapFont();
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);		
		font.setColor(0, 0, 0, 1);
		selectorHeight = font.getCapHeight();
		initItemsPositions(menuEnumClass);
		generateSelector(selectorClass);
		foregroundGroup= new Group();
		backgroundGroup = new Group();
		addBackgroundElements();
		foregroundGroup.addActor(selector);
		stage.addActor(backgroundGroup);
		stage.addActor(foregroundGroup);		
	}
	
	protected void resetCursorPosition() {
		currentItem = 0;
		selector.setPosition(horizontalMenuStart - selector.getWidth() * 2,
				verticalMenuStart - selector.getHeight() + 5);
	}
	
	protected void setFontColor(float r, float g, float b) {
		font.setColor(r, g, b, 1);
	}

	private <E extends Enum<?>> void initItemsPositions(Class<E> menuEnumClass) {

		offset = new Vector2(0, 0);

		float verticalMenuSize = font.getXHeight() * (menuEnumClass.getEnumConstants().length) + verticalMenuSpacing * (menuEnumClass.getEnumConstants().length - 1);
		verticalMenuStart = Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() - verticalMenuSize) / 2;

		float letterMaxWidth = 0;		

		for (E enumValue : menuEnumClass.getEnumConstants()) {
			GlyphLayout layout = new GlyphLayout();
			layout.setText(font, enumValue.toString());
			letterMaxWidth = layout.width > letterMaxWidth ? layout.width : letterMaxWidth;
			menuItems.add(new MenuItem(enumValue, new Vector2((Gdx.graphics.getWidth() - layout.width) / 2, 0)));
		}
		horizontalMenuStart = (Gdx.graphics.getWidth() - letterMaxWidth) / 2;		
	}

	protected void setOffset(float xNewOffset, float yNewOffset) {
		for (MenuItem menuItem : menuItems) {
			menuItem.getPosition().x = menuItem.getPosition().x - offset.x + xNewOffset;
			menuItem.getPosition().y = menuItem.getPosition().y - offset.y + yNewOffset;
		}
		verticalMenuStart = verticalMenuStart - offset.y + yNewOffset;
		horizontalMenuStart = horizontalMenuStart - offset.x + xNewOffset;
		selector.setPosition(horizontalMenuStart - selector.getWidth() * 2,
				verticalMenuStart - selector.getHeight() + 5);
		offset.x = xNewOffset;
		offset.y = yNewOffset;
	}
	
	private void generateSelector(Class<?> selectorClass) {
		try {
			if (selectorClass != null) {
				selector = (Actor) selectorClass.newInstance();
			} else {
				selector = (Actor) DefaultSelector.class.newInstance();
			}
		} catch (Exception e) {
			try {
				selector = (Actor) DefaultSelector.class.newInstance();
			} catch (Exception e1) {
				// Should never happen
			}
		}
		selector.setZIndex(10);
		selector.setSize(selectorHeight * 1.2f, selectorHeight * 1.2f);
		selector.setPosition(horizontalMenuStart - selector.getWidth() * 2,
				verticalMenuStart - selector.getHeight() + 5);
	}

	protected abstract void addBackgroundElements();
	
	@Override
	public void render(float delta) {
		handleInput();
		stage.act();
		draw();
	}

	@Override
	public void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.draw();
		
		Batch batch = stage.getBatch();
		batch.begin();
		float y = 0;
		int i = 0;
		for (MenuItem menuItem : menuItems) {
			font.draw(batch, menuItem.getName(), menuItems.get(i).getPosition().x, verticalMenuStart - y);
			y += verticalMenuSpacing;
			i++;
		}
		batch.end();		
	}

	@Override
	public void handleInput() {
		if (Gdx.input.isKeyJustPressed(Keys.DOWN) && currentItem < menuItems.size() - 1) {
			currentItem++;
			selector.setY(selector.getY() - verticalMenuSpacing);
		}
		if (Gdx.input.isKeyJustPressed(Keys.UP) && currentItem > 0) {
			currentItem--;
			selector.setY(selector.getY() + verticalMenuSpacing);
		}
	}

	protected int getSelectedItemIndex() {
		return currentItem;
	}

	protected String getSelectedItemName() {
		return menuItems.get(currentItem).getName();
	}
	
	protected Enum<?> getSelectedItemEnum() {
		return menuItems.get(currentItem).getMenuEnum();
	}

	protected BitmapFont getFont() {
		return font;
	}

}
