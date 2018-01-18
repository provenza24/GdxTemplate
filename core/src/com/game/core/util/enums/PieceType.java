package com.game.core.util.enums;

public enum PieceType {

	BAR("barre.jpg"),
	T_BLOCK("T.jpg"),
	Z_BLOCK("Z.jpg"),
	S_BLOCK("S.jpg"),
	L_BLOCK("L.jpg"),
	J_BLOCK("Linverse.jpg"),
	SQUARE("carre.jpg"),
	EMPTY("");
	
	private String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	private PieceType(String image) {
		this.image = image;
	}
	
}
