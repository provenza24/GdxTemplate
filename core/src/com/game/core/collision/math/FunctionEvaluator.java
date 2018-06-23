package com.game.core.collision.math;

import java.util.HashMap;
import java.util.Map;

public class FunctionEvaluator {

	/** Fonctions 1 a 4: pente ascendante */
	public static final MathFunction f1 = x -> x / 4f;

	public static final MathFunction f2 = x -> x / 4f + 0.25f;

	public static final MathFunction f3 = x -> x / 4f + 0.5f;

	public static final MathFunction f4 = x -> x / 4f + 0.75f;	
	
	/** Fonctions 5 a 8: pente descendante */
	public static final MathFunction f5 = x -> 1 - x / 4f;
	
	public static final MathFunction f6 = x -> 0.75f - x / 4f;
	
	public static final MathFunction f7 = x -> 0.5f - x / 4f;
	
	public static final MathFunction f8 = x -> 0.25f - x / 4f;	
	
	private static final Map<Integer, MathFunction> FUNCTIONS = new HashMap<Integer, MathFunction>();
	
	static {
		FUNCTIONS.put(191, f1);
		FUNCTIONS.put(192, f2);
		FUNCTIONS.put(193, f3);
		FUNCTIONS.put(194, f4);		
		
		FUNCTIONS.put(195, f1);
		FUNCTIONS.put(196, f2);
		FUNCTIONS.put(197, f3);
		FUNCTIONS.put(198, f4);
		
		FUNCTIONS.put(199, f5);
		FUNCTIONS.put(200, f6);
		FUNCTIONS.put(201, f7);
		FUNCTIONS.put(202, f8);
		
		FUNCTIONS.put(203, f5);
		FUNCTIONS.put(204, f6);
		FUNCTIONS.put(205, f7);
		FUNCTIONS.put(206, f8);
	}	
	
	public static Float compute(int tileId, Float x) {
		return FUNCTIONS.get(tileId).compute(x);
	}
}
