package com.game.core.sprite.sfx.laser;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.game.core.board.Board;
import com.game.core.board.BoardSquare;
import com.game.core.sprite.sfx.AbstractSfxSprite;
import com.game.core.sprite.sfx.AbstractSprite;
import com.game.core.sprite.sfx.wall.BottomLeftWallPiece;
import com.game.core.sprite.sfx.wall.BottomRightWallPiece;
import com.game.core.sprite.sfx.wall.TopLeftWallPiece;
import com.game.core.sprite.sfx.wall.TopRightWallPiece;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.PieceType;

public class ProtonLaser extends AbstractSfxSprite {

	private Vector2 startPos;
	
	private Vector2 endPos1;
	
	private Vector2 endPos2;
	
	private Vector2 boardVector;	
	
	private Board board;
	
	private List<AbstractSprite> sfxSprites;
	
	private static final int THIKNESS = ScreenConstants.SQUARE_WIDTH == 32 ? 3 : ScreenConstants.SQUARE_WIDTH >= 16 ? 2 : 1;
	
	public ProtonLaser(Vector2 startPos, Vector2 endPos1, Vector2 endPos2, Vector2 boardVector, Board board, List<AbstractSprite> sfxSprites) {
		super(startPos.x, startPos.y);	
		this.startPos = startPos;
		this.endPos1 = endPos1;
		this.endPos2 = endPos2;
		this.boardVector = boardVector;
		this.board=board;
		this.sfxSprites = sfxSprites;
		this.blendingSprite = true;
	}

	@Override
	public void initializeAnimations() {
	}
	
	@Override
	public void update(float deltaTime) {		
		stateTime = stateTime + deltaTime;
		if (stateTime>=1) {
			deletable = true;
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.begin();
		GFX.drawChainLightningRandomBetweenPoints(batch, startPos, endPos1, endPos2, THIKNESS, 3);
		batch.end();
	}
	
	public void onDelete() {
		int x = (int) boardVector.x;
		int y = (int) boardVector.y;
		BoardSquare boardSquare = board.getBoard()[x][y];
		if (boardSquare.getPieceType()!=PieceType.EMPTY) {
			sfxSprites.add(new TopLeftWallPiece(boardSquare, (x+1)*ScreenConstants.SQUARE_WIDTH, y*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2));																	
			sfxSprites.add(new TopRightWallPiece(boardSquare, (x+1)*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2, y*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2));										
			sfxSprites.add(new BottomRightWallPiece(boardSquare,(x+1)*ScreenConstants.SQUARE_WIDTH+ScreenConstants.SQUARE_WIDTH/2, y*ScreenConstants.SQUARE_WIDTH));																	
			sfxSprites.add(new BottomLeftWallPiece(boardSquare, (x+1)*ScreenConstants.SQUARE_WIDTH, y*ScreenConstants.SQUARE_WIDTH));
			board.getBoard()[x][y].setPieceType(PieceType.EMPTY);
		}		
	}

	public Vector2 getBoardVector() {
		return boardVector;
	}

	public void setBoardVector(Vector2 boardVector) {
		this.boardVector = boardVector;
	}

	
	
}
