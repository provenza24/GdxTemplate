package com.game.core;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;

public class ActionBuilder {

	public static FloatAction createFloatAction(float x, float y, float duration) {	
	    FloatAction floatAction = new FloatAction(x ,y);
	    floatAction.setInterpolation(Interpolation.exp5);
	    floatAction.setDuration(duration);	    
	    return floatAction;
	}
	
}
