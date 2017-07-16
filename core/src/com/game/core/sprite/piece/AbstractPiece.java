package com.game.core.sprite.piece;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.game.core.util.DirectionType;
import com.game.core.util.constants.ScreenConstants;
import com.game.core.util.enums.PieceType;

public abstract class AbstractPiece implements IPiece {
	
	private static final int BLOCK_WIDTH = ScreenConstants.SQUARE_WIDTH;

	protected final static Map<DirectionType, Vector2> MOVES = new HashMap<DirectionType, Vector2>();
	
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
	
	static {
		MOVES.put(DirectionType.DOWN, new Vector2(0, -1));
		MOVES.put(DirectionType.LEFT, new Vector2(-1, 0));
		MOVES.put(DirectionType.RIGHT, new Vector2(1, 0));		
	}
	
	protected Vector2[] cases;
	
	protected Vector2[] oldCases;
	
	public AbstractPiece() {
		super();		
		this.cases = new Vector2[4];
		this.oldCases = new Vector2[4];
		for(int i=0;i<4;i++) {
			this.cases[i]=new Vector2();
			this.oldCases[i]=new Vector2();
		}		
	}
	
	public abstract PieceType getType();

	public void rotate(boolean right) {
		this.savePositions();
		for (int i = 1; i < 4; i++) {
			Vector2 p = new Vector2(cases[i].x - cases[0].x,
					cases[i].y - cases[0].y);
			Vector2 resul = new Vector2( right ? p.y : -p.y, right ? -p.x : p.x);
			resul.x = resul.x + cases[0].x;
			resul.y = resul.y + cases[0].y;
			cases[i].x = resul.x;
			cases[i].y = resul.y;
		}
	}
	
	protected void setPosition(int caseNumber, int x, int y) {
		this.cases[caseNumber].x = x;
		this.cases[caseNumber].y = y;
		this.oldCases[caseNumber].x = x;
		this.oldCases[caseNumber].y = y;
	}
	
	
	public void render(Batch batch) {		
		batch.begin();						
		batch.draw(PIECE_IMAGES.get(this.getType()), (ScreenConstants.BOARD_LEFT_SPACE+this.cases[0].x) * BLOCK_WIDTH, this.cases[0].y * BLOCK_WIDTH, BLOCK_WIDTH ,BLOCK_WIDTH);
		batch.draw(PIECE_IMAGES.get(this.getType()), (ScreenConstants.BOARD_LEFT_SPACE+this.cases[1].x) * BLOCK_WIDTH, this.cases[1].y * BLOCK_WIDTH, BLOCK_WIDTH ,BLOCK_WIDTH);
		batch.draw(PIECE_IMAGES.get(this.getType()), (ScreenConstants.BOARD_LEFT_SPACE+this.cases[2].x) * BLOCK_WIDTH, this.cases[2].y * BLOCK_WIDTH, BLOCK_WIDTH ,BLOCK_WIDTH);
		batch.draw(PIECE_IMAGES.get(this.getType()), (ScreenConstants.BOARD_LEFT_SPACE+this.cases[3].x) * BLOCK_WIDTH, this.cases[3].y * BLOCK_WIDTH, BLOCK_WIDTH ,BLOCK_WIDTH);			
		batch.end();
	}
	
	public void renderNextPiece(Batch batch) {		
		batch.begin();						
		batch.draw(PIECE_IMAGES.get(this.getType()), (18-this.cases[0].x) * BLOCK_WIDTH, (5 + 17-this.cases[0].y) * BLOCK_WIDTH, BLOCK_WIDTH ,BLOCK_WIDTH);
		batch.draw(PIECE_IMAGES.get(this.getType()), (18-this.cases[1].x) * BLOCK_WIDTH, (5 + 17-this.cases[1].y) * BLOCK_WIDTH, BLOCK_WIDTH ,BLOCK_WIDTH);
		batch.draw(PIECE_IMAGES.get(this.getType()), (18-this.cases[2].x) * BLOCK_WIDTH, (5 + 17-this.cases[2].y) * BLOCK_WIDTH, BLOCK_WIDTH ,BLOCK_WIDTH);
		batch.draw(PIECE_IMAGES.get(this.getType()), (18-this.cases[3].x) * BLOCK_WIDTH, (5 + 17-this.cases[3].y) * BLOCK_WIDTH, BLOCK_WIDTH ,BLOCK_WIDTH);			
		batch.end();
	}
	
	public void translate(DirectionType direction) {
		this.savePositions();
		Vector2 move = MOVES.get(direction);
		for (int i = 0; i < 4; i++) {
			this.cases[i].x = this.cases[i].x + move.x;
			this.cases[i].y = this.cases[i].y + move.y;			
		}
	}
	
	private void savePositions() {
		for (int i = 0; i < 4; i++) {
			this.oldCases[i].x = this.cases[i].x;
			this.oldCases[i].y = this.cases[i].y;
		}		
	}
	
	public void undoTranslation() {
		for (int i = 0; i < 4; i++) {
			this.cases[i].x = this.oldCases[i].x;
			this.cases[i].y = this.oldCases[i].y;
		}				
	}
	
	public Vector2 getCase(int i) {
		return this.cases[i];
	}
	
	public Vector2[] getCases() {
		return this.cases;
	}

	public void swap(int case1, int case2) {
		Vector2 vect1 = new Vector2(this.cases[case1].x, this.cases[case1].y);
		Vector2 vect2 = new Vector2(this.cases[case2].x, this.cases[case2].y);
		this.cases[case1] = vect2;
		this.cases[case2] = vect1;	
	}
	
	public void undoRotation() {
		this.undoTranslation();				
	}

	
}
