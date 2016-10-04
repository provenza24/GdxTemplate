package com.game.core.screen.transition;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Interpolation;
import com.game.core.screen.transition.impl.ScreenTransitionFade;
import com.game.core.screen.transition.impl.ScreenTransitionSlice;
import com.game.core.screen.transition.impl.ScreenTransitionSlide;

public abstract class AbstractScreenTransition implements IScreenTransition {

	private static Map<ScreenTransitionEnum, IScreenTransition> TRANSITIONS = new HashMap<AbstractScreenTransition.ScreenTransitionEnum, IScreenTransition>();
	
	static {		
		TRANSITIONS.put(ScreenTransitionEnum.SLIDE, ScreenTransitionSlide.init(1, 2, false,Interpolation.linear));
		TRANSITIONS.put(ScreenTransitionEnum.SLICE, ScreenTransitionSlice.init(2, ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out));
		TRANSITIONS.put(ScreenTransitionEnum.FADE, ScreenTransitionFade.init(2));
	}
	
	public static enum ScreenTransitionEnum {
		SLICE, SLIDE, FADE;
	}
	
	public static IScreenTransition getScreenTransition(ScreenTransitionEnum screenTransitionEnum) {
		return TRANSITIONS.get(screenTransitionEnum);
	}	
	
}
