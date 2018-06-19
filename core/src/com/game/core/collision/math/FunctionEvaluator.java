package com.game.core.collision.math;

import java.util.HashMap;
import java.util.Map;

public class FunctionEvaluator {

	public static final MathFunction X_DIV_4 = x -> x / 4f;

	public static final MathFunction X_DIV_4_ADD_0_25 = x -> x / 4f + 0.25f;

	public static final MathFunction X_DIV_4_ADD_0_5 = x -> x / 4f + 0.5f;

	public static final MathFunction X_DIV_4_ADD_0_75 = x -> x / 4f + 0.75f;	
		
	public static final MathFunction f5 = x -> 1 - x / 4f;
	
	public static final MathFunction f6 = x -> 0.75f - x / 4f;
	
	public static final MathFunction f7 = x -> 0.5f - x / 4f;
	
	public static final MathFunction f8 = x -> 0.25f - x / 4f;
	
	
	private static final Map<Integer, MathFunction> FUNCTIONS = new HashMap<Integer, MathFunction>();
	
	static {
		FUNCTIONS.put(391, X_DIV_4);
		FUNCTIONS.put(392, X_DIV_4_ADD_0_25);
		FUNCTIONS.put(393, X_DIV_4_ADD_0_5);
		FUNCTIONS.put(394, X_DIV_4_ADD_0_75);		
		
		FUNCTIONS.put(395, f5);
		FUNCTIONS.put(396, f6);
		FUNCTIONS.put(397, f7);
		FUNCTIONS.put(398, f8);
	}	
	
	public static Float compute(int tileId, Float x) {
		return FUNCTIONS.get(tileId).compute(x);
	}
}
