package com.game.core.util.constants;

public class ScreenConstants {

	public static int PREFERED_WIDHT = 400;
		
	public static final float MAP_UNIT_PIXELS = 16;

	public static final int NB_HORIZONTAL_TILES = 20;

	public static final int NB_VERTICAL_TILES = 14;

	public static final float VERTICAL_HALF_POSITION = (float)(NB_VERTICAL_TILES / 2);

	public static final float HORIZONTAL_HALF_POSITION = (float) (NB_HORIZONTAL_TILES / 2) - 0.5f;

	public static final float ORIGINAL_WIDTH = MAP_UNIT_PIXELS * NB_HORIZONTAL_TILES;

	public static final float ORIGINAL_HEIGHT = MAP_UNIT_PIXELS * NB_VERTICAL_TILES;

	public static final float SCREEN_COEF = (float) (PREFERED_WIDHT / ORIGINAL_WIDTH);

	public static final int WIDTH = (int) (ORIGINAL_WIDTH * SCREEN_COEF);

	public static final int HEIGHT = (int) (ORIGINAL_HEIGHT * SCREEN_COEF);

	public static final float FREE_CAMERA_MAX_SCROLL = HORIZONTAL_HALF_POSITION + 1;

}
