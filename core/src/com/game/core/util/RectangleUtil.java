package com.game.core.util;

import com.badlogic.gdx.math.Rectangle;

public class RectangleUtil {

	public static boolean overlaps (Rectangle r1, Rectangle r2) {
		return r2.x <= r1.x + r1.width && r2.x + r2.width >= r1.x && r2.y <= r1.y + r1.height && r2.y + r2.height >= r1.y;
	}

}
