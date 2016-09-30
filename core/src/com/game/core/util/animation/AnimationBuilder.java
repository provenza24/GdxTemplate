package com.game.core.util.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationBuilder {
	
	private static AnimationBuilder instance = new AnimationBuilder();

	private AnimationBuilder() {		
	}
	
	public static AnimationBuilder getInstance() {
		return instance;
	}
	
	public Animation build(Texture texture, int startFrameIndex, int nbFrames, float frameDelay) {
			
		TextureRegion[][] textureRegions = TextureRegion.split(texture, texture.getWidth()/nbFrames, texture.getHeight());		
		TextureRegion[] frameTextures = new TextureRegion[nbFrames];
		for (int i=0;i<nbFrames;i++) {			
			frameTextures[i] = textureRegions[0][startFrameIndex+i];
		}		
		return new Animation(frameDelay, frameTextures);		
	}
	
	public Animation build(TextureRegion[][] textureRegions, int startFrameIndex, int nbFrames, float frameDelay) {
		
		TextureRegion[] frameTextures = new TextureRegion[nbFrames];
		for (int i=0;i<nbFrames;i++) {			
			frameTextures[i] = textureRegions[0][startFrameIndex+i];
		}		
		return new Animation(frameDelay, frameTextures);		
	}
	
	public Animation build(TextureRegion[][] textureRegions, int[] frames, int nbHorizontalFrames, float frameDelay) {
		
		TextureRegion[] frameTextures = new TextureRegion[frames.length];		
		for (int i=0;i<frames.length;i++) {		
			int vIndex = frames[i]/nbHorizontalFrames;
			int hIndex = frames[i]%nbHorizontalFrames;
			frameTextures[i] = textureRegions[vIndex][hIndex];
		}		
		return new Animation(frameDelay, frameTextures);		
	}
	
	public Animation buildReversed(Texture texture, int startFrameIndex, int nbFrames, float frameDelay) {
		
		TextureRegion[][] textureRegions = TextureRegion.split(texture, texture.getWidth()/nbFrames, texture.getHeight());		
		TextureRegion[] frameTextures = new TextureRegion[nbFrames];
		int j = 0;
		for (int i=startFrameIndex;i>=startFrameIndex+1-nbFrames;i--) {			
			frameTextures[j] = textureRegions[0][i];
			j++;
		}		
		return new Animation(frameDelay, frameTextures);		
	}
	
	public Animation buildReversed(TextureRegion[][] textureRegions, int startFrameIndex, int nbFrames, float frameDelay) {		
		TextureRegion[] frameTextures = new TextureRegion[nbFrames];
		int j = 0;
		for (int i=startFrameIndex;i>=startFrameIndex+1-nbFrames;i--) {			
			frameTextures[j] = textureRegions[0][i];
			j++;
		}		
		return new Animation(frameDelay, frameTextures);		
	}
	
	

}
