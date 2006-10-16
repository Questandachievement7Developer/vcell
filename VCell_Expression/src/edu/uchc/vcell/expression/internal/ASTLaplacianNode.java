package edu.uchc.vcell.expression.internal;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
/* JJT: 0.2.2 */
import net.sourceforge.interval.ia_math.RealInterval;

import org.vcell.expression.ExpressionException;
import org.vcell.expression.NameScope;

public class ASTLaplacianNode extends SimpleNode {
ASTLaplacianNode() {
	super(-1);
}  
public String code() throws ExpressionException {

	StringBuffer buffer = new StringBuffer();
	 
	buffer.append(" laplacian(");

	buffer.append(jjtGetChild(0).code());

	buffer.append(") ");

	return buffer.toString();
}    
/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.parser.Node
 * @exception java.lang.Exception The exception description.
 */
public Node copyTree() {
	ASTLaplacianNode node = new ASTLaplacianNode();
	for (int i=0;i<jjtGetNumChildren();i++){
		node.jjtAddChild(jjtGetChild(i).copyTree());
	}
	return node;	
}
/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.parser.Node
 * @exception java.lang.Exception The exception description.
 */
public Node copyTreeBinary() {
	ASTLaplacianNode node = new ASTLaplacianNode();
	for (int i=0;i<jjtGetNumChildren();i++){
		node.jjtAddChild(jjtGetChild(i).copyTreeBinary());
	}
	return node;	
}
/**
 * This method was created by a SmartGuide.
 * @return double
 * @exception java.lang.Exception The exception description.
 */
public Node differentiate(String variable) throws ExpressionException {
	throw new ExpressionException("ASTLaplacianNode.differentiate(), not implemented");
}
public double evaluateConstant() throws ExpressionException {
	throw new ExpressionException("LaplaicianNode cannot be evaluated as a constant");
}    
public RealInterval evaluateInterval(RealInterval intervals[]) throws ExpressionException {
	throw new ExpressionException("ASTLaplacianNode.evaluateInterval(), not implemented");
}    
public double evaluateVector(double values[]) throws ExpressionException {
	throw new ExpressionException("ASTLaplacianNode.evaluateVector(), not implemented");
}    
/**
 * This method was created by a SmartGuide.
 * @exception java.lang.Exception The exception description.
 */
public Node flatten() throws ExpressionException {
	
	try {
		double value = evaluateConstant();
		return new ASTFloatNode(value);
	}catch (Exception e){}		

	if (jjtGetNumChildren()!=1){ 
		throw new Error("ASTLaplacianNode should have 1 child"); 
	}
		
	ASTLaplacianNode laplacianNode = new ASTLaplacianNode();
	laplacianNode.jjtAddChild(jjtGetChild(0).flatten());	
	return laplacianNode;
}
public String infixString(int lang, NameScope nameScope){

	StringBuffer buffer = new StringBuffer();
	 
	buffer.append(" laplacian(");

	buffer.append(jjtGetChild(0).infixString(lang,nameScope));
	
	buffer.append(") ");

	return buffer.toString();

}    
/**
 * Insert the method's description here.
 * Creation date: (6/20/01 11:04:41 AM)
 * @return boolean
 */
public boolean narrow(RealInterval intervals[]) {
	throw new RuntimeException("ASTLaplacianNode.narrow(), not yet supported");
}
}
