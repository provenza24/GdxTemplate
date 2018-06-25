package com.game.core.util.enums.math;

import com.game.core.collision.math.MathFunction;

public enum MathFunctionEnum {

	F1(MathFunction.f1),
	F2(MathFunction.f2),
	F3(MathFunction.f3),
	F4(MathFunction.f4),
	F5(MathFunction.f5),
	F6(MathFunction.f6),
	F7(MathFunction.f7),
	F8(MathFunction.f8);
	
	private MathFunction mathFunction;
	
	private MathFunctionEnum(MathFunction mathFunction) {
		this.mathFunction = mathFunction;
	}

	public MathFunction getMathFunction() {
		return mathFunction;
	}

	public void setMathFunction(MathFunction mathFunction) {
		this.mathFunction = mathFunction;
	}
}
