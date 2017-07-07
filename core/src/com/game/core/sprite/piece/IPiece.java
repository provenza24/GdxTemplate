package com.game.core.sprite.piece;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.game.core.util.DirectionType;
import com.game.core.util.enums.PieceType;

public interface IPiece {

	public void render(Batch batch);
	
	public PieceType getType();				
	
	public void translate(DirectionType direction);	
	
	public void undoTranslation();
	
	public Vector2 getCase(int i);
	
	public void rotate();
	
	public void undoRotation();
}
