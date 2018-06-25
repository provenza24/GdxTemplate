package com.game.core.collision.math;

@FunctionalInterface
public interface MathFunction {	
	
	/** Ascending curved tiles */
	public static final MathFunction f1 = x -> x / 4f;

	public static final MathFunction f2 = x -> x / 4f + 0.25f;

	public static final MathFunction f3 = x -> x / 4f + 0.5f;

	public static final MathFunction f4 = x -> x / 4f + 0.75f;	
	
	/** Descending curved tiles */
	public static final MathFunction f5 = x -> 1 - x / 4f;
	
	public static final MathFunction f6 = x -> 0.75f - x / 4f;
	
	public static final MathFunction f7 = x -> 0.5f - x / 4f;
	
	public static final MathFunction f8 = x -> 0.25f - x / 4f;		
	
	Float compute(Float x);
}
