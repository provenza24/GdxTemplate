package com.game.core.sprite.sfx.wall;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.board.BoardSquare;

public class TopRightWallPiece extends AbstractWallPiece {

	public TopRightWallPiece(BoardSquare boardSquare, float x, float y) {
		super(boardSquare, x, y, new Vector2(X_ACCELERATION_COEFF+MathUtils.random(28), Y_ACCELERATION_COEFF*(1+MathUtils.random(3f))));		
	}

}
