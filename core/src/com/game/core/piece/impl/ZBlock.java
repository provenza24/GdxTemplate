package com.game.core.piece.impl;

import com.game.core.piece.AbstractSZBlock;
import com.game.core.util.enums.PieceType;

public final class ZBlock extends AbstractSZBlock {

	
	public ZBlock() {
		super();	
		centerCaseAfterRotation = 2;
		setPosition(0, 4, 16);
		setPosition(1, 3, 16);
		setPosition(2, 4, 15);
		setPosition(3, 5, 15);			
	}
	
	@Override
	public PieceType getType() {		
		return PieceType.Z_BLOCK;
	}
		
}
