package cbit.vcell.solvers;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/


import cbit.vcell.math.CompartmentSubDomain;
import cbit.vcell.math.Constant;
import cbit.vcell.math.Equation;
import cbit.vcell.math.Function;
import cbit.vcell.math.MemVariable;
import cbit.vcell.math.MembraneRegionEquation;
import cbit.vcell.math.MembraneRegionVariable;
import cbit.vcell.math.MembraneSubDomain;
import cbit.vcell.math.ReservedVariable;
import cbit.vcell.math.Variable;
import cbit.vcell.math.VolVariable;
import cbit.vcell.math.VolumeRegionVariable;
import cbit.vcell.parser.Expression;
import cbit.vcell.parser.ExpressionException;
import cbit.vcell.solver.SimulationJob;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class CppClassCoderMembraneRegionVarContext extends CppClassCoderAbstractVarContext {
/**
 * VarContextCppCoder constructor comment.
 * @param name java.lang.String
 */
protected CppClassCoderMembraneRegionVarContext(CppCoderVCell argCppCoderVCell,
												Equation argEquation,
												MembraneSubDomain argMembraneSubDomain,
												SimulationJob argSimulationJob, 
												String argParentClass) throws Exception
{
	super(argCppCoderVCell,argEquation,argMembraneSubDomain,argSimulationJob,argParentClass);
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.model.Feature
 */
public CompartmentSubDomain getInsideCompartment() {
	if (isFlippedInsideOutside((MembraneSubDomain)getSubDomain())) {
		return ((MembraneSubDomain)getSubDomain()).getOutsideCompartment();
	} else {
		return ((MembraneSubDomain)getSubDomain()).getInsideCompartment();
	}

}


/**
 * This method was created in VisualAge.
 * @return cbit.vcell.math.MembraneSubDomain
 */
public MembraneSubDomain getMembraneSubDomain() {
	return (MembraneSubDomain)getSubDomain();
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.model.Feature
 */
public CompartmentSubDomain getOutsideCompartment() {
	if (isFlippedInsideOutside((MembraneSubDomain)getSubDomain())) {
		return ((MembraneSubDomain)getSubDomain()).getInsideCompartment();
	} else {
		return ((MembraneSubDomain)getSubDomain()).getOutsideCompartment();
	}
}


/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeConstructor(java.io.PrintWriter out) throws Exception {
	out.println(getClassName()+"::"+getClassName()+"(Feature *Afeature, string& AspeciesName)");
	out.println(": "+getParentClassName()+"(Afeature,AspeciesName)");
	out.println("{");
	try {
		Expression ic = getEquation().getInitialExpression();
		ic.bindExpression(simulationJob.getSimulationSymbolTable());
		double value = ic.evaluateConstant();
		out.println("\tinitialValue = new double;");
		out.println("\t*initialValue = " + value + ";");
	}catch (ExpressionException e){
		out.println("\tinitialValue = NULL;");
	}	
	out.println();
	Variable requiredVariables[] = getRequiredVariables();
	for (int i = 0; i < requiredVariables.length; i++){
		Variable var = requiredVariables[i];
		if (var instanceof VolVariable 
				|| var instanceof MemVariable
				|| var instanceof VolumeRegionVariable
				|| var instanceof MembraneRegionVariable) {
			out.println("\t" + CppClassCoder.getEscapedFieldVariableName_C(var.getName()) + " = NULL;");
		}
	}		  	
	out.println("}");
}


/**
 * This method was created by a SmartGuide.
 * @param printWriter java.io.PrintWriter
 */
public void writeDeclaration(java.io.PrintWriter out) throws Exception {
	out.println("//---------------------------------------------");
	out.println("//  class " + getClassName());
	out.println("//---------------------------------------------");

	out.println("class " + getClassName() + " : public " + getParentClassName());
	out.println("{");
	out.println(" public:");
	out.println("\t"+getClassName() + "(Feature *feature, string& speciesName);");
	out.println("\tvoid resolveReferences(Simulation *sim);");

	try {
		Expression ic = getEquation().getInitialExpression();
		ic.bindExpression(simulationJob.getSimulationSymbolTable());
		double value = ic.evaluateConstant();
	}catch (Exception e){
		out.println("\tvirtual double getInitialValue(MembraneElement *memElement);");
	}
	out.println("\tdouble getMembraneReactionRate(MembraneElement *memElement);");
	out.println("\tdouble getUniformRate(MembraneRegion *memElement);");

	out.println("private:");
	Variable requiredVariables[] = getRequiredVariables();
	for (int i = 0; i < requiredVariables.length; i++){
		Variable var = requiredVariables[i];
		String mangledVarName = CppClassCoder.getEscapedFieldVariableName_C(var.getName());
		if (var instanceof VolVariable){
			out.println("\tVolumeVariable *" + mangledVarName + ";");
		}else if (var instanceof MemVariable){
			out.println("\tMembraneVariable *" + mangledVarName + ";");
		}else if (var instanceof MembraneRegionVariable){
			out.println("\tMembraneRegionVariable *" + mangledVarName + ";");
		}else if (var instanceof VolumeRegionVariable){
			out.println("\tVolumeRegionVariable *" + mangledVarName + ";");
		}else if (var instanceof ReservedVariable){
		}else if (var instanceof Constant){
		}else if (var instanceof Function){
		}else{
			throw new Exception("unknown identifier type '" + var.getClass().getName()+"' for identifier: " + var.getName());
		}	
	}		  	
	out.println("};");
}


/**
 * This method was created by a SmartGuide.
 * @param printWriter java.io.PrintWriter
 */
public void writeImplementation(java.io.PrintWriter out) throws Exception {
	out.println("//---------------------------------------------");
	out.println("//  class " + getClassName());
	out.println("//---------------------------------------------");
	writeConstructor(out);
	out.println("");
	writeResolveReferences(out);
	out.println("");
	writeMembraneRegionFunction(out,"getUniformRate", ((MembraneRegionEquation)getEquation()).getUniformRateExpression());
	out.println("");
	boolean bFlippedInsideOutside = isFlippedInsideOutside(getMembraneSubDomain());
	writeMembraneFunction(out,"getMembraneReactionRate", ((MembraneRegionEquation)getEquation()).getMembraneRateExpression(),bFlippedInsideOutside);
	out.println();
	try {
		double value = getEquation().getInitialExpression().evaluateConstant();
	}catch (Exception e){
		writeMembraneFunction(out,"getInitialValue", getEquation().getInitialExpression(),bFlippedInsideOutside);
	}
	out.println();
}
}