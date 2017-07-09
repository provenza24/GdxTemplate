package com.game.core.util.constants;

public class ScreenConstants {
			
	public static final int SQUARE_WIDTH = 32;

	public static final int BOARD_WIDTH = 10;
	
	public static final int BOARD_HEIGHT = 18;
	
	/*public static final int NB_HORIZONTAL_TILES = 17;

	public static final int NB_VERTICAL_TILES = 18;

	public static final float VERTICAL_HALF_POSITION = (float)(NB_VERTICAL_TILES / 2);

	public static final float HORIZONTAL_HALF_POSITION = (float) (NB_HORIZONTAL_TILES / 2) - 0.5f;

	public static final float ORIGINAL_WIDTH = MAP_UNIT_PIXELS * NB_HORIZONTAL_TILES;

	public static final float ORIGINAL_HEIGHT = MAP_UNIT_PIXELS * NB_VERTICAL_TILES;

	public static int PREFERED_WIDHT = NB_HORIZONTAL_TILES * 32;
	
	public static final float SCREEN_COEF = (float) (PREFERED_WIDHT / ORIGINAL_WIDTH);

	public static final int WIDTH = (int) (ORIGINAL_WIDTH * SCREEN_COEF);

	public static final int HEIGHT = (int) (ORIGINAL_HEIGHT * SCREEN_COEF);*/	

	public static final int WIDTH = SQUARE_WIDTH * BOARD_WIDTH;

	public static final int HEIGHT = SQUARE_WIDTH * BOARD_HEIGHT;
	
}
