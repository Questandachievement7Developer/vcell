/* Generated By:JJTree: Do not edit this line. ASTRationalNumberExponent.java */

package cbit.vcell.units.parser;

import cbit.vcell.matrix.RationalNumber;

public class ASTRationalNumberExponent extends SimpleNode {
	RationalNumber value = null;

	public ASTRationalNumberExponent(int id) {
		super(id);
	}

	public ASTRationalNumberExponent(UnitSymbolParser p, int id) {
		super(p, id);
	}

	public String toInfix(RationalNumber power) {
		RationalNumber product = value.mult(power);
		if (product.intValue() == product.doubleValue()) {
			return product.infix();
		} else {
			return "(" + product.infix() + ")";
		}
	}

	public String toSymbol(RationalNumber power, UnitTextFormat format) {
		RationalNumber product = value.mult(power);
		double doubleValue = product.doubleValue();
		int intValue = product.intValue();
		switch (format) {
			case plain:{
				if (intValue == doubleValue) {
					return product.infix();
				} else {
					return "(" + product.infix() + ")";
				}
			}
			case unicode:{
				if (intValue == doubleValue) {
					if (Math.abs(intValue)<10){
						String superScriptChar = superScripts_0_to_9[Math.abs(intValue)];
						if (intValue<0){
							return SUPER_MINUS+superScriptChar;
						}else{
							return superScriptChar;
						}
					}else{
						return product.infix();
					}
				} else {
					return "(" + product.infix() + ")";
				}
			}
			default: {
				throw new RuntimeException("format "+format.name()+" not yet supported by UnitSymbol");
			}
		}
	}
	
	public static String getUnicodeExponent(RationalNumber exponent){
		int intValue = exponent.intValue();
		double doubleValue = exponent.doubleValue();
		if (intValue == doubleValue) {
			if (Math.abs(intValue)<10){
				String superScriptChar = superScripts_0_to_9[Math.abs(intValue)];
				if (intValue<0){
					return SUPER_MINUS+superScriptChar;
				}else{
					return superScriptChar;
				}
			}else{
				return exponent.infix();
			}
		} else {
			return "(" + exponent.infix() + ")";
		}
	}

}