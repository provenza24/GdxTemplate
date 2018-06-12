package com.game.core.piece.impl;

import com.game.core.piece.AbstractPiece;
import com.game.core.util.enums.PieceType;

public final class TBlock extends AbstractPiece {

	
	public TBlock() {
		super();	
		setPosition(0, 4, 16);
		setPosition(1, 3, 16);
		setPosition(2, 5, 16);
		setPosition(3, 4, 15);			
	}

	@Override
	public PieceType getType() {		
		return PieceType.T_BLOCK;
	}
}
