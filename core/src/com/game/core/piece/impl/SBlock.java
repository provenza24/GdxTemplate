package com.game.core.piece.impl;

import com.game.core.piece.AbstractSZBlock;
import com.game.core.util.enums.PieceType;

public final class SBlock extends AbstractSZBlock {

	public SBlock() {
		super();	
		centerCaseAfterRotation = 3;
		setPosition(0, 4, 16);
		setPosition(1, 5, 16);
		setPosition(2, 3, 15);
		setPosition(3, 4, 15);			
	}
		
	@Override
	public PieceType getType() {		
		return PieceType.S_BLOCK;
	}
}
