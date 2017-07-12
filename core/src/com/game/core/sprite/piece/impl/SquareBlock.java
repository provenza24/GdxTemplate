package com.game.core.sprite.piece.impl;

import com.game.core.sprite.piece.AbstractPiece;
import com.game.core.util.enums.PieceType;

public class SquareBlock extends AbstractPiece {

	public SquareBlock() {
		super();		
		setPosition(0, 5, 17);
		setPosition(1, 4, 17);
		setPosition(2, 5, 16);
		setPosition(3, 4, 16);		
	}
	
	@Override
	public PieceType getType() {
		return PieceType.SQUARE;
	}

	@Override
	public void rotate(boolean right) {		
	}
	
	
}
