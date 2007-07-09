package cbit.vcell.math;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.util.*;

import org.vcell.expression.ExpressionException;
import org.vcell.expression.ExpressionFactory;
import org.vcell.expression.IExpression;
import org.vcell.expression.SymbolTable;
import org.vcell.util.CommentStringTokenizer;

import edu.uchc.vcell.expression.internal.*;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class MembraneRegionEquation extends Equation {
	private IExpression membraneRateExpression = ExpressionFactory.createExpression(0.0);
	private IExpression uniformRateExpression = ExpressionFactory.createExpression(0.0);

/**
 * OdeEquation constructor comment.
 * @param subDomain cbit.vcell.math.SubDomain
 * @param var cbit.cell.math.Variable
 * @param initialExp cbit.vcell.parser.Expression
 * @param rateExp cbit.vcell.parser.Expression
 */
public MembraneRegionEquation(MembraneRegionVariable var, IExpression initialExp) {
	super(var, initialExp, null);
}


/**
 * Insert the method's description here.
 * Creation date: (9/4/2003 12:32:19 PM)
 * @return boolean
 * @param object cbit.util.Matchable
 */
public boolean compareEqual(org.vcell.util.Matchable object) {
	MembraneRegionEquation equ = null;
	if (!(object instanceof MembraneRegionEquation)){
		return false;
	}else{
		equ = (MembraneRegionEquation)object;
	}
	if (!compareEqual0(equ)){
		return false;
	}
	if (!org.vcell.util.Compare.isEqualOrNull(membraneRateExpression,equ.membraneRateExpression)){
		return false;
	}
	if (!org.vcell.util.Compare.isEqualOrNull(uniformRateExpression,equ.uniformRateExpression)){
		return false;
	}
	return true;
}


/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 10:41:10 AM)
 * @param sim cbit.vcell.solver.Simulation
 */
void flatten(SymbolTable symbolTable, boolean bRoundCoefficients) throws org.vcell.expression.ExpressionException, MathException {
	super.flatten0(symbolTable,bRoundCoefficients);
	
	membraneRateExpression = getFlattenedExpression(symbolTable,membraneRateExpression,bRoundCoefficients);
	uniformRateExpression = getFlattenedExpression(symbolTable,uniformRateExpression,bRoundCoefficients);
}


/**
 * This method was created by a SmartGuide.
 * @return java.util.Vector
 */
protected Vector getExpressions(MathDescription mathDesc){
	Vector list = new Vector();
	list.addElement(getUniformRateExpression());
	list.addElement(getMembraneRateExpression());
	
	if (getRateExpression()!=null)		list.addElement(getRateExpression());
	if (getInitialExpression()!=null)	list.addElement(getInitialExpression());
	if (getExactSolution()!=null)		list.addElement(getExactSolution());
	return list;
}


/**
 * Insert the method's description here.
 * Creation date: (7/9/01 2:05:09 PM)
 * @return cbit.vcell.parser.Expression
 */
public IExpression getMembraneRateExpression() {
	return membraneRateExpression;
}


/**
 * This method was created by a SmartGuide.
 * @return java.util.Enumeration
 */
public Enumeration getTotalExpressions() throws ExpressionException {
	Vector vector = new Vector();
	IExpression lvalueExp = ExpressionFactory.createExpression("UniformRate_"+getVariable().getName()+";");
	IExpression rvalueExp = ExpressionFactory.createExpression(getUniformRateExpression());
	IExpression totalExp = ExpressionFactory.assign(lvalueExp,rvalueExp);
	totalExp.bindExpression(null);
	totalExp.flatten();
	vector.addElement(totalExp);
	lvalueExp = ExpressionFactory.createExpression("MembraneRate_"+getVariable().getName()+";");
	rvalueExp = ExpressionFactory.createExpression(getMembraneRateExpression());
	totalExp = ExpressionFactory.assign(lvalueExp,rvalueExp);
	totalExp.bindExpression(null);
	totalExp.flatten();
	vector.addElement(totalExp);
	vector.addElement(getTotalInitialExpression());
	IExpression solutionExp = getTotalSolutionExpression();
	if (solutionExp!=null){
		vector.addElement(solutionExp);
	}	
	return vector.elements();
}


/**
 * Insert the method's description here.
 * Creation date: (7/9/01 2:05:09 PM)
 * @return cbit.vcell.parser.Expression
 */
public IExpression getUniformRateExpression() {
	return uniformRateExpression;
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 */
public String getVCML() {
	StringBuffer buffer = new StringBuffer();
	buffer.append("\t"+VCML.MembraneRegionEquation+" "+getVariable().getName()+" {\n");
	if (getUniformRateExpression() != null){
		buffer.append("\t\t"+VCML.UniformRate+" "+getUniformRateExpression().infix()+";\n");
	}else{
		buffer.append("\t\t"+VCML.UniformRate+" "+"0.0;\n");
	}
	if (getMembraneRateExpression() != null){
		buffer.append("\t\t"+VCML.MembraneRate+" "+getMembraneRateExpression().infix()+";\n");
	}else{
		buffer.append("\t\t"+VCML.MembraneRate+" "+"0.0;\n");
	}
	if (initialExp != null){
		buffer.append("\t\t"+VCML.Initial+"\t "+initialExp.infix()+";\n");
	}
	switch (solutionType){
		case UNKNOWN_SOLUTION:{
			if (initialExp == null){
				buffer.append("\t\t"+VCML.Initial+"\t "+"0.0;\n");
			}
			break;
		}
		case EXACT_SOLUTION:{
			buffer.append("\t\t"+VCML.Exact+" "+exactExp.infix()+";\n");
			break;
		}
	}				
		
	buffer.append("\t}\n");
	return buffer.toString();		
}


/**
 * This method was created by a SmartGuide.
 * @param tokens java.util.StringTokenizer
 * @exception java.lang.Exception The exception description.
 */
public void read(CommentStringTokenizer tokens) throws MathFormatException, ExpressionException {
	String token = null;
	token = tokens.nextToken();
	if (!token.equalsIgnoreCase(VCML.BeginBlock)){
		throw new MathFormatException("unexpected token "+token+" expecting "+VCML.BeginBlock);
	}			
	while (tokens.hasMoreTokens()){
		token = tokens.nextToken();
		if (token.equalsIgnoreCase(VCML.EndBlock)){
			break;
		}			
		if (token.equalsIgnoreCase(VCML.Initial)){
			initialExp = ExpressionFactory.createExpression(tokens);
			continue;
		}
		if (token.equalsIgnoreCase(VCML.UniformRate)){
			IExpression exp = ExpressionFactory.createExpression(tokens);
			setUniformRateExpression(exp);
			continue;
		}
		if (token.equalsIgnoreCase(VCML.MembraneRate)){
			IExpression exp = ExpressionFactory.createExpression(tokens);
			setMembraneRateExpression(exp);
			continue;
		}
		if (token.equalsIgnoreCase(VCML.Exact)){
			exactExp = ExpressionFactory.createExpression(tokens);
			solutionType = EXACT_SOLUTION;
			continue;
		}
		throw new MathFormatException("unexpected identifier "+token);
	}	
		
}


/**
 * Insert the method's description here.
 * Creation date: (7/9/01 2:05:09 PM)
 * @param newMembraneRateExpression cbit.vcell.parser.Expression
 */
public void setMembraneRateExpression(IExpression newMembraneRateExpression) {
	membraneRateExpression = newMembraneRateExpression;
}


/**
 * Insert the method's description here.
 * Creation date: (7/9/01 2:05:09 PM)
 * @param newMembraneRateExpression cbit.vcell.parser.Expression
 */
public void setUniformRateExpression(IExpression newUniformRateExpression) {
	uniformRateExpression = newUniformRateExpression;
}
}