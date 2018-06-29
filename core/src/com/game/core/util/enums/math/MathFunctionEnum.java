package com.game.core.util.enums.math;

import com.game.core.collision.math.MathFunction;

public enum MathFunctionEnum {

	F1(MathFunction.f1, false),
	F2(MathFunction.f2, false),
	F3(MathFunction.f3, false),
	F4(MathFunction.f4, false),
	F5(MathFunction.f5, false),
	F6(MathFunction.f6, false),
	F7(MathFunction.f7, false),
	F8(MathFunction.f8, false),
	F9(MathFunction.f9, true);
	
	private MathFunction mathFunction;
	
	private boolean isConstant;
	
	private MathFunctionEnum(MathFunction mathFunction, boolean isConstant) {
		this.mathFunction = mathFunction;
		this.isConstant = isConstant;
	}

	public MathFunction getMathFunction() {
		return mathFunction;
	}

	public void setMathFunction(MathFunction mathFunction) {
		this.mathFunction = mathFunction;
	}

	public boolean isConstant() {
		return isConstant;
	}

	public void setConstant(boolean isConstant) {
		this.isConstant = isConstant;
	}
}
