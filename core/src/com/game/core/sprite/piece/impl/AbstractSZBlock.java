package com.game.core.sprite.piece.impl;

import com.game.core.sprite.piece.AbstractPieceRotatingTwice;

public abstract class AbstractSZBlock extends AbstractPieceRotatingTwice {

	@Override
	public void rotate(boolean right) {
		super.rotate(right);
		if (rotationNum == 1) {
			for (int i = 0; i < 4; i++) {
				cases[i].y -=1;
			}	
			swap(0, centerCaseAfterRotation);			
		}
		updateToPreviousRotationNum();
	}
	
}
