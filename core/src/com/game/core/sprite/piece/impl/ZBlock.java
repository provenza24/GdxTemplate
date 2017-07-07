package com.game.core.sprite.piece.impl;

import com.game.core.util.enums.PieceType;

public final class ZBlock extends AbstractSZBlock {

	
	public ZBlock() {
		super();	
		centerCaseAfterRotation = 2;
		setPosition(0, 4, 17);
		setPosition(1, 3, 17);
		setPosition(2, 4, 16);
		setPosition(3, 5, 16);			
	}
	
	@Override
	public PieceType getType() {		
		return PieceType.Z_BLOCK;
	}
		
}
