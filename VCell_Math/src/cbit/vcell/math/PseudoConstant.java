package cbit.vcell.math;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import org.vcell.expression.ExpressionBindingException;
import org.vcell.expression.IExpression;
import org.vcell.expression.SymbolTable;

import edu.uchc.vcell.expression.internal.*;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class PseudoConstant extends Variable {
	IExpression exp = null;
/**
 * Constant constructor comment.
 * @param name java.lang.String
 */
public PseudoConstant(String name, IExpression exp) {
	super(name);
	this.exp = exp;
}
/**
 * This method was created by a SmartGuide.
 * @param symbolTable cbit.vcell.parser.SymbolTable
 * @exception java.lang.Exception The exception description.
 */
public void bind(SymbolTable symbolTable) throws ExpressionBindingException {
	if (exp!=null){
		exp.bindExpression(symbolTable);
	}	
}
/**
 * This method was created in VisualAge.
 * @return boolean
 * @param obj Matchable
 */
public boolean compareEqual(org.vcell.util.Matchable obj) {
	if (!(obj instanceof PseudoConstant)){
		return false;
	}
	if (!compareEqual0(obj)){
		return false;
	}
	PseudoConstant v = (PseudoConstant)obj;
	if (!org.vcell.util.Compare.isEqualOrNull(exp,v.exp)){
		return false;
	}
	
	return true;
}
/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.parser.Expression
 * @exception java.lang.Exception The exception description.
 */
public IExpression getPseudoExpression() {
	return exp;
}
}
