package com.game.core.piece;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.game.core.util.DirectionType;
import com.game.core.util.enums.PieceType;

public interface IPiece {

	public void update(float delta);
		
	public void render(Batch batch);
	
	public void renderNextPiece(Batch batch);
	
	public PieceType getType();				
	
	public void translate(DirectionType direction);	
	
	public void undoTranslation();
	
	public Vector2 getSquare(int i);
	
	public void rotate(boolean right);
	
	public void undoRotation();

	public boolean contains(Vector2 vector);
	
	public boolean isGhostPiece();
}
