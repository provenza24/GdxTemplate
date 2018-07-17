package com.game.core.collision.math;

@FunctionalInterface
public interface MathFunction {	
	
	/** Ascending slope tiles */
	public static final MathFunction f1 = x -> x / 4f;

	public static final MathFunction f2 = x -> x / 4f + 0.25f;

	public static final MathFunction f3 = x -> x / 4f + 0.5f;

	public static final MathFunction f4 = x -> x / 4f + 0.75f;	
	
	/** Descending slope tiles */
	public static final MathFunction f5 = x -> 1 - x / 4f;
	
	public static final MathFunction f6 = x -> 0.75f - x / 4f;
	
	public static final MathFunction f7 = x -> 0.5f - x / 4f;
	
	public static final MathFunction f8 = x -> 0.25f - x / 4f;		
	
	/** */
	public static final MathFunction f9 = x -> 0.2f;	
	
	Float compute(Float x);
}
