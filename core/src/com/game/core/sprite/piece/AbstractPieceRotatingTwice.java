package com.game.core.sprite.piece;

public abstract class AbstractPieceRotatingTwice extends AbstractPiece {
	
	protected int rotationNum;		
	
	protected int centerCaseAfterRotation;
	
	public AbstractPieceRotatingTwice() {
		super();
		this.rotationNum = 0;				
	}
	
	@Override
	public void undoRotation() {
		super.undoRotation();
		this.updateToPreviousRotationNum();
		
	}
		
	protected void updateToPreviousRotationNum() {
		rotationNum = rotationNum == 0 ? 1 : 0;
	}
	
}
