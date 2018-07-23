package com.game.core.util.enums;

import com.badlogic.gdx.math.Vector2;
import com.game.core.sprite.impl.ennemy.caveman.Caveman;
import com.game.core.sprite.impl.ennemy.dinosaurman.DinosaurMan;
import com.game.core.sprite.impl.ennemy.fly.Fly;
import com.game.core.sprite.impl.item.Candy;
import com.game.core.sprite.impl.item.Flag;
import com.game.core.sprite.impl.item.Liana;

public enum TileObjectEnum {

	CAVEMAN(Caveman.class, new Vector2(0.1f,0.3f), SpriteTypeEnum.ENEMY),
	FLY(Fly.class, new Vector2(), SpriteTypeEnum.ENEMY),
	DINOSAURMAN(DinosaurMan.class, new Vector2(0.1f,0.3f), SpriteTypeEnum.ENEMY),
	
	CANDY(Candy.class, new Vector2(), SpriteTypeEnum.ITEM),
	LIANA(Liana.class, new Vector2(), SpriteTypeEnum.ITEM),
	FLAG(Flag.class, new Vector2(), SpriteTypeEnum.ITEM);
	
	private TileObjectEnum(Class<?> zclass, Vector2 offset, SpriteTypeEnum spriteTypeEnum) {
		this.zclass = zclass;
		this.offset = offset;
		this.spriteTypeEnum = spriteTypeEnum;
	}

	private Class<?> zclass;

	private Vector2 offset;
	
	private SpriteTypeEnum spriteTypeEnum;
	
	public Class<?> getZclass() {
		return zclass;
	}

	public Vector2 getOffset() {
		return offset;
	}

	public SpriteTypeEnum getSpriteTypeEnum() {
		return spriteTypeEnum;
	}

	public void setSpriteTypeEnum(SpriteTypeEnum spriteTypeEnum) {
		this.spriteTypeEnum = spriteTypeEnum;
	}
	
}
