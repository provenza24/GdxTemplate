package com.game.core.sprite.board;

import com.game.core.util.enums.PieceType;

public class BoardSquare {

	private PieceType pieceType;

	public BoardSquare(int value, PieceType pieceType) {
		super();
		this.pieceType = pieceType;
	}

	public PieceType getPieceType() {
		return pieceType;
	}

	public void setPieceType(PieceType pieceType) {
		this.pieceType = pieceType;
	}


}
