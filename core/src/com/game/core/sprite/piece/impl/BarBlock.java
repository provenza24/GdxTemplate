package com.game.core.sprite.piece.impl;

import com.game.core.sprite.piece.AbstractPieceRotatingTwice;
import com.game.core.util.enums.PieceType;

public final class BarBlock extends AbstractPieceRotatingTwice {

	public BarBlock() {
		super();	
		centerCaseAfterRotation = 2;
		setPosition(0, 4, 17);
		setPosition(1, 3, 17);
		setPosition(2, 5, 17);
		setPosition(3, 6, 17);			
	}

	@Override
	public void rotate(boolean right) {
		super.rotate(true);		
		if (rotationNum == 0) {
			for (int i = 0; i < 4; i++) {
				cases[i].x +=1;
				//cases[i].y +=1;
			}
		}
		if (rotationNum == 1) {			
			for (int i = 0; i < 4; i++) {				
				//cases[i].y -=1;
			}
			swap(0, 2);
		}		
		updateToPreviousRotationNum();
	}

	@Override
	public PieceType getType() {		
		return PieceType.BAR;
	}
}
