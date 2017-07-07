package com.game.core.sprite.piece.impl;

import com.game.core.util.enums.PieceType;

public final class SBlock extends AbstractSZBlock {

	public SBlock() {
		super();	
		centerCaseAfterRotation = 3;
		setPosition(0, 4, 17);
		setPosition(1, 5, 17);
		setPosition(2, 3, 16);
		setPosition(3, 4, 16);			
	}
		
	@Override
	public PieceType getType() {		
		return PieceType.S_BLOCK;
	}
}
