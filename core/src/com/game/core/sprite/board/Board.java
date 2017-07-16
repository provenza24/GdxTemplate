package com.game.core.sprite.board;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.game.core.sprite.piece.IPiece;
import com.game.core.sprite.piece.impl.BarBlock;
import com.game.core.sprite.piece.impl.JBlock;
import com.game.core.sprite.piece.impl.LBlock;
import com.game.core.sprite.piece.impl.SBlock;
import com.game.core.sprite.piece.impl.SquareBlock;
import com.game.core.sprite.piece.impl.TBlock;
import com.game.core.sprite.piece.impl.ZBlock;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.PieceType;

public class Board {

	private static final int BOARD_HEIGHT = ScreenConstants.BOARD_HEIGHT;

	private static final int BOARD_WIDTH = ScreenConstants.BOARD_WIDTH;

	private static final int BLOCK_WIDTH = ScreenConstants.SQUARE_WIDTH;

	private static final int RANDOM_HIGH_VALUE = 7;
	
	private static final int RANDOM_LOW_VALUE = 1;
	
	private final static Map<PieceType, Texture> PIECE_IMAGES = new HashMap<PieceType, Texture>();

	static {
		PIECE_IMAGES.put(PieceType.BAR, new Texture(Gdx.files.internal("sprites/pieces/barre.jpg")));
		PIECE_IMAGES.put(PieceType.J_BLOCK, new Texture(Gdx.files.internal("sprites/pieces/Linverse.jpg")));
		PIECE_IMAGES.put(PieceType.L_BLOCK, new Texture(Gdx.files.internal("sprites/pieces/L.jpg")));
		PIECE_IMAGES.put(PieceType.S_BLOCK, new Texture(Gdx.files.internal("sprites/pieces/S.jpg")));
		PIECE_IMAGES.put(PieceType.SQUARE, new Texture(Gdx.files.internal("sprites/pieces/carre.jpg")));
		PIECE_IMAGES.put(PieceType.T_BLOCK, new Texture(Gdx.files.internal("sprites/pieces/T.jpg")));
		PIECE_IMAGES.put(PieceType.Z_BLOCK, new Texture(Gdx.files.internal("sprites/pieces/Z.jpg")));
	}

	private final BoardSquare board[][];

	public Board() {
		board = new BoardSquare[BOARD_WIDTH][BOARD_HEIGHT];
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++) {
				board[i][j] = new BoardSquare(0, PieceType.EMPTY);
			}
		}		
	}

	public void render(Batch batch) {
		batch.begin();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				BoardSquare boardSquare = board[i][j];
				if (boardSquare.getPieceType() != PieceType.EMPTY) {
					batch.draw(PIECE_IMAGES.get(boardSquare.getPieceType()), (i + ScreenConstants.BOARD_LEFT_SPACE) * BLOCK_WIDTH, j * BLOCK_WIDTH, BLOCK_WIDTH, BLOCK_WIDTH);
				}
			}
		}
		batch.end();
	}
	
	public boolean isAcceptable(IPiece piece) {
		try {			
			for (int i = 0; i < 4; i++) {				 
				if (board[(int)piece.getCase(i).x][(int)piece.getCase(i).y].getPieceType() != PieceType.EMPTY)
					return false;
			}
			return true;
		} catch (Exception e) {			
			return false;
		}
	}
	
	public void posePiece(IPiece piece) {		
		for (int i = 0; i < 4; i++) {
			this.board[(int)piece.getCase(i).x][(int)piece.getCase(i).y].setPieceType(piece.getType());			
		}		
	}
	
	public int[] getLinesToSuppress(IPiece piece) {

		int[] res = initTabSuppress();
		int j = 0;
		int y = (int) piece.getCase(0).y;
		for (int i = y + 3; i > y - 3; i--) {
			if (i >= 0 && i <= 17 && isDeletableLine(i)) {
				res[j] = i;
				j++;				
				res[4]= res[4]+1;						
			}
		}			
		return res;
	}
	
	private int[] initTabSuppress() {
		int res[] = new int[5];
		for (int i = 0; i < 4; i++) {
			res[i] = -1;
		}
		res[4]=0;
		return res;
	}
	
	private boolean isDeletableLine(int line) {		
		for (int i = 0; i < BOARD_WIDTH; i++) {
			if (board[i][line].getPieceType()==PieceType.EMPTY) {				
				return false;
			}
		}
		return true;
	}
	
	public void deleteLine(int line) {
		int currentLine = line + 1;
		for (int i = currentLine; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; j++) {
				board[j][i - 1].setPieceType(board[j][i].getPieceType());				
				board[j][i].setPieceType(PieceType.EMPTY);
			}
		}
	}

	public BoardSquare[][] getBoard() {
		return board;
	}
	
	public IPiece createPiece() {
		
		IPiece piece = null;		
		int random = (int)(Math.random() * (RANDOM_HIGH_VALUE-RANDOM_LOW_VALUE)) + RANDOM_LOW_VALUE;
		//random = 2;
		switch (random) {
		case 1:
			piece = new BarBlock();
			break;
		case 2:
			piece = new JBlock();
			break;
		case 3:
			piece = new LBlock();
			break;
		case 4:
			piece = new SBlock();
			break;
		case 5:
			piece = new SquareBlock();
			break;
		case 6:
			piece = new TBlock();
			break;
		case 7:
			piece = new ZBlock();
			break;		
		}
		return piece;
	}

}
