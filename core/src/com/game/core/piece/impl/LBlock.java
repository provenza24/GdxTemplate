package com.game.core.piece.impl;

import com.game.core.piece.AbstractPiece;
import com.game.core.util.enums.PieceType;

public final class LBlock extends AbstractPiece {

	
	public LBlock() {
		super();	
		setPosition(0, 4, 16);
		setPosition(1, 3, 16);
		setPosition(2, 5, 16);
		setPosition(3, 3, 15);			
	}

	@Override
	public PieceType getType() {		
		return PieceType.L_BLOCK;
	}
}
