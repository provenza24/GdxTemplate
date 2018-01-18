package com.game.core.sprite.sfx.wall;

import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.board.BoardSquare;

public class BottomRightWallPiece extends AbstractWallPiece {

	public BottomRightWallPiece(BoardSquare boardSquare, float x, float y) {
		super(boardSquare, x, y, new Vector2(X_ACCELERATION_COEFF/2,Y_ACCELERATION_COEFF));		
	}

}
