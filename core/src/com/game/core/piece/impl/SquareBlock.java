package com.game.core.piece.impl;

import com.game.core.piece.AbstractPiece;
import com.game.core.util.enums.PieceType;

public class SquareBlock extends AbstractPiece {

	public SquareBlock() {
		super();		
		setPosition(0, 5, 16);
		setPosition(1, 4, 16);
		setPosition(2, 5, 15);
		setPosition(3, 4, 15);		
	}
	
	@Override
	public PieceType getType() {
		return PieceType.SQUARE;
	}

	@Override
	public void rotate(boolean right) {		
	}
	
	
}
