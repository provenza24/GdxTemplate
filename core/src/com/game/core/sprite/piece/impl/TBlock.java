package com.game.core.sprite.piece.impl;

import com.game.core.sprite.piece.AbstractPiece;
import com.game.core.util.enums.PieceType;

public final class TBlock extends AbstractPiece {

	
	public TBlock() {
		super();	
		setPosition(0, 4, 17);
		setPosition(1, 3, 17);
		setPosition(2, 5, 17);
		setPosition(3, 4, 16);			
	}

	@Override
	public PieceType getType() {		
		return PieceType.T_BLOCK;
	}
}
