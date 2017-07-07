package com.game.core.sprite.piece.impl;

import com.game.core.sprite.piece.AbstractPiece;
import com.game.core.util.enums.PieceType;

public final class JBlock extends AbstractPiece {

	
	public JBlock() {
		super();	
		setPosition(0, 4, 17);
		setPosition(1, 3, 17);
		setPosition(2, 5, 17);
		setPosition(3, 5, 16);			
	}

	@Override
	public PieceType getType() {		
		return PieceType.J_BLOCK;
	}
}
