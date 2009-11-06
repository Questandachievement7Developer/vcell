package cbit.vcell.solvers;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.util.*;
import cbit.vcell.math.*;
import cbit.vcell.solver.*;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class CppClassCoderVCellModel extends CppClassCoder {
/**
 * VarContextCppCoder constructor comment.
 * @param name java.lang.String
 */
protected CppClassCoderVCellModel(CppCoderVCell cppCoderVCell, SimulationJob argSimulationJob) 
{
	super(argSimulationJob, cppCoderVCell,"UserVCellModel", "VCellModel");
}
/**
 * This method was created by a SmartGuide.
 * @param out java.io.PrintWriter
 */
protected void writeConstructor(java.io.PrintWriter out) throws Exception {
	out.println(getClassName()+"::"+getClassName()+"()");
	out.println(": "+getParentClassName()+"()");
 	out.println("{");
 	out.println("\tstring featurename;");
 	//
 	// add 'Features' to VCellModel
 	//
 	Enumeration enum1 = cppCoder.getCppClassCoders();
 	while (enum1.hasMoreElements()){
 		CppClassCoder coder = (CppClassCoder)enum1.nextElement();
 		if (coder instanceof CppClassCoderFeature){
 			CppClassCoderFeature featureClassCoder = (CppClassCoderFeature)coder;
 			
 			//
 			// calculate a priority based on level of nesting 
 			//
 			CompartmentSubDomain subDomain = featureClassCoder.getCompartmentSubDomain();
 			out.println("\tfeaturename=\""+subDomain.getName()+"\";");
			out.println("\taddFeature(new "+featureClassCoder.getClassName()+"(featurename,"+subDomain.getPriority()+"));");
		}
	}
 	//
 	// add 'Contours' to VCellModel (if any)
 	//
 	cbit.vcell.geometry.FilamentGroup fg = simulationJob.getSimulation().getMathDescription().getGeometry().getGeometrySpec().getFilamentGroup();
  	for (int i=0;i<fg.getFilamentCount();i++){
	  	out.println("\taddContour(new Contour("+i+"));  // for Filament "+fg.getFilamentNames()[i]);
  	}	 	
	out.println("}");
}
/**
 * This method was created by a SmartGuide.
 * @param printWriter java.io.PrintWriter
 */
public void writeDeclaration(java.io.PrintWriter out) {
	out.println("//---------------------------------------------");
	out.println("//  class " + getClassName());
	out.println("//---------------------------------------------");

	out.println("class " + getClassName() + " : public " + getParentClassName());
	out.println("{");
	out.println("public:");
	out.println("\t"+getClassName() + "();");
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
}
}
