package org.vcell.expression;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.util.Vector;

/**
 * This type was created in VisualAge.
 */
public class VariableSymbolTable implements SymbolTable {
	Vector varList = new Vector();
/**
 * VariableSymbolTable constructor comment.
 */
public VariableSymbolTable() {
	super();
}
/**
 * This method was created in VisualAge.
 * @param var cbit.vcell.math.Variable
 */
public void addVar(SymbolTableEntry var) {
	if (!varList.contains(var)){
		varList.addElement(var);
	}
}
/**
 * getEntry method comment.
 */
public SymbolTableEntry getEntry(String identifierString) throws ExpressionBindingException {
	for (int i=0;i<varList.size();i++){
		SymbolTableEntry var = (SymbolTableEntry)varList.elementAt(i);
		if (var.getName().equals(identifierString)){
			return var;
		}
	}
	return null;
}
}
