package cbit.vcell.math;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.util.*;
import java.io.*;

import org.vcell.expression.ExpressionFactory;
import org.vcell.expression.ExpressionUtilities;
import org.vcell.expression.IExpression;

import cbit.vcell.geometry.*;
/**
 * This type was created in VisualAge.
 */
public class MathDescriptionTest {
/**
 * This method was created by a SmartGuide.
 */
public static MathDescription getExample() throws Exception {
	MathDescription mathDesc = new MathDescription("example");
	int sizeX = 10;
	int sizeY = 10;
	int sizeZ = 10;
//	int HANDLE_CYTOSOL = 5;
//	int HANDLE_ER = 15;

	CompartmentSubDomain cytosolSubDomain = new CompartmentSubDomain("cytosol",0);
//	cytosolSubDomain.setHandle(HANDLE_CYTOSOL);
	mathDesc.addSubDomain(cytosolSubDomain);
	
	CompartmentSubDomain erSubDomain = new CompartmentSubDomain("er",1);
//	erSubDomain.setHandle(HANDLE_ER);
	mathDesc.addSubDomain(erSubDomain);

	MembraneSubDomain erMemSubDomain = new MembraneSubDomain(erSubDomain,cytosolSubDomain);
	mathDesc.addSubDomain(erMemSubDomain);
	
/*
	CartesianDomain domain = new CartesianDomain(mathDesc, 2, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0);
	domain.addCompartment(new Compartment(mathDesc,"er",new IExpression("0;")));
	domain.addCompartment(new Compartment(mathDesc,"cytosol",new IExpression("1;")));
	mathDesc.setDomain(domain);
	
	mathDesc.setMesh(new Mesh(mathDesc,domain,sizeX,sizeY,sizeZ));
*/

	Geometry geo = GeometryTest.getExample_er_cytsol2D();
	geo.getGeometrySpec().setExtent(new org.vcell.util.Extent(1.0, 1.0, 1.0));
	geo.getGeometrySpec().setOrigin(new org.vcell.util.Origin(0.0, 0.0, 0.0));
	mathDesc.setGeometry(geo);
	
	//
	// Constants
	//
	Constant constant = new Constant("Dca",ExpressionFactory.createExpression("40;"));
	mathDesc.addVariable(constant);
	constant = new Constant("Dbuf",ExpressionFactory.createExpression("4;"));
	mathDesc.addVariable(constant);
	constant = new Constant("k2",ExpressionFactory.createExpression("4.498;"));
	mathDesc.addVariable(constant);
	constant = new Constant("k1",ExpressionFactory.createExpression("49.23;"));
	mathDesc.addVariable(constant);
	constant = new Constant("bufferMAX",ExpressionFactory.createExpression("20;"));
	mathDesc.addVariable(constant);
	
	//
	// Variables
	//
	VolVariable calcium = new VolVariable("calcium");
	mathDesc.addVariable(calcium);
	VolVariable buffer = new VolVariable("buffer");
	mathDesc.addVariable(buffer);

	//
	// Calcium PDE for Cytosol
	//
	IExpression rateExpression = ExpressionFactory.createExpression("-k1*calcium+pow(buffer/bufferMAX,3);");
	IExpression initialExpression = ExpressionFactory.createExpression("4;");
	IExpression diffusionExpression = ExpressionFactory.createExpression("Dca;");
	Equation equ = new PdeEquation(calcium,initialExpression, rateExpression,diffusionExpression);
	cytosolSubDomain.addEquation(equ);
	
	//
	// Calcium PDE for ER
	//
	rateExpression = ExpressionFactory.createExpression("-k2*calcium+buffer;");
	initialExpression = ExpressionFactory.createExpression("4;");
	diffusionExpression = ExpressionFactory.createExpression("Dca;");
	equ = new PdeEquation(calcium,initialExpression, rateExpression,diffusionExpression);
	erSubDomain.addEquation(equ);

	//
	// Buffer PDE for Cytosol
	//
	rateExpression = ExpressionFactory.createExpression("k1*calcium-pow(buffer/bufferMAX,3);");
	initialExpression = ExpressionFactory.createExpression("40;");
	diffusionExpression = ExpressionFactory.createExpression("Dbuf;");
	equ = new PdeEquation(buffer,initialExpression, rateExpression,diffusionExpression);
	cytosolSubDomain.addEquation(equ);

	//
	// Buffer PDE for ER
	//
	rateExpression = ExpressionFactory.createExpression("k2*calcium-buffer;");
	initialExpression = ExpressionFactory.createExpression("40;");
	diffusionExpression = ExpressionFactory.createExpression("Dbuf;");
	equ = new PdeEquation(buffer,initialExpression, rateExpression,diffusionExpression);
	erSubDomain.addEquation(equ);

	//
	//	Calcium jump condition for ER membrane
	//
	IExpression inFlux = ExpressionFactory.createExpression("1;");
	IExpression outFlux = ExpressionFactory.createExpression("2;");
	JumpCondition jc = new JumpCondition(calcium);
	jc.setInFlux(inFlux);
	jc.setOutFlux(outFlux);
	erMemSubDomain.addEquation(jc);

	//
	//	Buffer jump condition for ER membrane
	//
	inFlux = ExpressionFactory.createExpression("3;");
	outFlux = ExpressionFactory.createExpression("4;");
	jc = new JumpCondition(buffer);
	jc.setInFlux(inFlux);
	jc.setOutFlux(outFlux);
	erMemSubDomain.addEquation(jc);
	
	return mathDesc;
}
/**
 * This method was created by a SmartGuide.
 */
public static MathDescription getFilamentExample() throws Exception {
	MathDescription mathDesc = getExample();

	FilamentGroup fg = mathDesc.getGeometry().getGeometrySpec().getFilamentGroup();
	cbit.vcell.geometry.Line line = new cbit.vcell.geometry.Line(new org.vcell.util.Coordinate(0,0,0),new org.vcell.util.Coordinate(1,1,1));
	line.setClosed(false);
	fg.addCurve("filament1",line);
	line = new cbit.vcell.geometry.Line(new org.vcell.util.Coordinate(.1,.1,.1),new org.vcell.util.Coordinate(.5,.5,.5));
	fg.addCurve("filament1",line);

	FilamentVariable filamentVar = new FilamentVariable("granule");
	mathDesc.addVariable(filamentVar);
	FilamentSubDomain filamentSubDomain = new FilamentSubDomain("filament1",mathDesc.getCompartmentSubDomain("cytosol"));
	mathDesc.addSubDomain(filamentSubDomain);
	filamentSubDomain.addEquation(new OdeEquation(filamentVar,ExpressionFactory.createExpression(1.0), ExpressionFactory.createExpression("-granule")));
	
	return mathDesc;
}
/**
 * This method was created by a SmartGuide.
 */
public static MathDescription getImageExample() throws Exception {
	MathDescription mathDesc = getExample();
	mathDesc.setGeometry(GeometryTest.getImageExample2D());
	if (mathDesc.isValid()==false){
		throw new RuntimeException("MathDescription.getImageExample(), math is invalid");
	}
	
	return mathDesc;
}
/**
 * This method was created by a SmartGuide.
 */
public static MathDescription getOdeExactExample() throws Exception {
	MathDescription mathDesc = new MathDescription("OdeExactExample");
	int sizeX = 10;
	int sizeY = 10;
	int sizeZ = 10;

	CompartmentSubDomain cytosolSubDomain = new CompartmentSubDomain("cytosol",CompartmentSubDomain.NON_SPATIAL_PRIORITY);
	mathDesc.addSubDomain(cytosolSubDomain);
	
	Geometry geo = new Geometry("getOdeExactExample()",0);
	geo.getGeometrySpec().setExtent(new org.vcell.util.Extent(1.0, 1.0, 1.0));
	geo.getGeometrySpec().setOrigin(new org.vcell.util.Origin(0.0, 0.0, 0.0));
	mathDesc.setGeometry(geo);

	//
	//     k = 2;
	//
	// dA/dt = -k*A;
	//  A(0) = 1.0;
	//  A(t) = exp(-k*t) 
	//
	
	//
	// Constants
	//
	Constant A_init = new Constant("A_init",ExpressionFactory.createExpression("1;"));
	mathDesc.addVariable(A_init);
	Constant k = new Constant("k",ExpressionFactory.createExpression("2;"));
	mathDesc.addVariable(k);
	
	//
	// Variables
	//
	VolVariable A = new VolVariable("A");
	mathDesc.addVariable(A);

	//
	// Functions
	//
	Function A_exact = new Function("A_exact",ExpressionFactory.createExpression("A_init*exp(-k*t)"));
	mathDesc.addVariable(A_exact);
	Function A_error = new Function("A_error",ExpressionFactory.createExpression("A-A_exact"));
	mathDesc.addVariable(A_error);
	
	//
	// A ODE for Cytosol
	//
	IExpression rateExpression = ExpressionFactory.createExpression("-k*A;");
	IExpression initialExpression = ExpressionFactory.createExpression("A_init;");
	Equation equ = new OdeEquation(A,initialExpression, rateExpression);
	equ.setExactSolution(ExpressionFactory.createExpression("A_exact"));
	cytosolSubDomain.addEquation(equ);

	return mathDesc;
}
/**
 * This method was created by a SmartGuide.
 */
public static MathDescription getOdeExample() throws Exception {
	MathDescription mathDesc = new MathDescription("OdeExample1");
	int sizeX = 10;
	int sizeY = 10;
	int sizeZ = 10;
//	int HANDLE_CYTOSOL = 5;
//	int HANDLE_ER = 15;

	CompartmentSubDomain cytosolSubDomain = new CompartmentSubDomain("cytosol",CompartmentSubDomain.NON_SPATIAL_PRIORITY);
//	cytosolSubDomain.setHandle(HANDLE_CYTOSOL);
	mathDesc.addSubDomain(cytosolSubDomain);
	
/*
	CartesianDomain domain = new CartesianDomain(mathDesc, 2, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0);
	mathDesc.setDomain(domain);
*/

	Geometry geo = new Geometry("getOdeExample()",0);
	geo.getGeometrySpec().setExtent(new org.vcell.util.Extent(1.0, 1.0, 1.0));
	geo.getGeometrySpec().setOrigin(new org.vcell.util.Origin(0.0, 0.0, 0.0));
	mathDesc.setGeometry(geo);
	
	//
	// Constants
	//
	Constant constant = new Constant("k2",ExpressionFactory.createExpression("10;"));
	mathDesc.addVariable(constant);
	constant = new Constant("k1",ExpressionFactory.createExpression("30;"));
	mathDesc.addVariable(constant);
	
	//
	// Variables
	//
	VolVariable calcium = new VolVariable("calcium");
	mathDesc.addVariable(calcium);
	VolVariable buffer = new VolVariable("buffer");
	mathDesc.addVariable(buffer);

	//
	// Calcium ODE for Cytosol
	//
	IExpression rateExpression = ExpressionFactory.createExpression("-k1*calcium+k2*buffer;");
	IExpression initialExpression = ExpressionFactory.createExpression("40;");
	Equation equ = new OdeEquation(calcium,initialExpression, rateExpression);
	cytosolSubDomain.addEquation(equ);
	
	//
	// Buffer ODE for Cytosol
	//
	rateExpression = ExpressionFactory.createExpression("k1*calcium-k2*buffer;");
	initialExpression = ExpressionFactory.createExpression("40;");
	equ = new OdeEquation(buffer,initialExpression, rateExpression);
	cytosolSubDomain.addEquation(equ);

	return mathDesc;
}
/**
 * This method was created by a SmartGuide.
 */
public static MathDescription getOdeExample2() throws Exception {
	MathDescription mathDesc = new MathDescription("OdeExample2");
	int sizeX = 10;
	int sizeY = 10;
	int sizeZ = 10;
//	int HANDLE_CYTOSOL = 5;
//	int HANDLE_ER = 15;

	CompartmentSubDomain cytosolSubDomain = new CompartmentSubDomain("cytosol",CompartmentSubDomain.NON_SPATIAL_PRIORITY);
//	cytosolSubDomain.setHandle(HANDLE_CYTOSOL);
	mathDesc.addSubDomain(cytosolSubDomain);
	
/*
	CartesianDomain domain = new CartesianDomain(mathDesc, 2, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0);
	mathDesc.setDomain(domain);
*/

	Geometry geo = new Geometry("getOdeExample2()",0);
	geo.getGeometrySpec().setExtent(new org.vcell.util.Extent(1.0, 1.0, 1.0));
	geo.getGeometrySpec().setOrigin(new org.vcell.util.Origin(0.0, 0.0, 0.0));
	mathDesc.setGeometry(geo);
	
	
	//
	// Constants
	//
	Constant constant = new Constant("k2",ExpressionFactory.createExpression("10;"));
	mathDesc.addVariable(constant);
	constant = new Constant("k1",ExpressionFactory.createExpression("30;"));
	mathDesc.addVariable(constant);
	constant = new Constant("bufferMAX",ExpressionFactory.createExpression("80;"));
	mathDesc.addVariable(constant);
	
	//
	// Variables
	//
	VolVariable buffer = new VolVariable("buffer");
	mathDesc.addVariable(buffer);
	
	//
	// Buffer ODE for Cytosol
	//
	IExpression rateExpression = ExpressionFactory.createExpression("k1*(bufferMAX-buffer)-k2*buffer;");
	IExpression initialExpression = ExpressionFactory.createExpression("40;");
	OdeEquation equ = new OdeEquation(buffer,initialExpression, rateExpression);
	cytosolSubDomain.addEquation(equ);

	return mathDesc;
}
/**
 * This method was created by a SmartGuide.
 */
public static MathDescription getOdeExampleWagner() throws Exception {
	MathDescription mathDesc = new MathDescription("OdeExampleWagner");
	int sizeX = 10;
	int sizeY = 10;
	int sizeZ = 10;
	int HANDLE_CYTOSOL = 5;
	int HANDLE_ER = 15;

	CompartmentSubDomain cytosolSubDomain = new CompartmentSubDomain("cytosol",CompartmentSubDomain.NON_SPATIAL_PRIORITY);
//	cytosolSubDomain.setHandle(HANDLE_CYTOSOL);
	mathDesc.addSubDomain(cytosolSubDomain);
	
/*
	CartesianDomain domain = new CartesianDomain(mathDesc, 2, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0);
	mathDesc.setDomain(domain);
*/

	Geometry geo = new Geometry("getOdeExampleWagner()",0);
	geo.getGeometrySpec().setExtent(new org.vcell.util.Extent(1.0, 1.0, 1.0));
	geo.getGeometrySpec().setOrigin(new org.vcell.util.Origin(0.0, 0.0, 0.0));
	mathDesc.setGeometry(geo);
	
	
	//
	// Constants
	//
	Constant constant = new Constant("LambdaBeta",ExpressionFactory.createExpression("6;"));
	mathDesc.addVariable(constant);
	constant = new Constant("vL",ExpressionFactory.createExpression("5e-4;"));
	mathDesc.addVariable(constant);
	constant = new Constant("I",ExpressionFactory.createExpression("0.12;"));
	mathDesc.addVariable(constant);
	constant = new Constant("dI",ExpressionFactory.createExpression("0.025;"));
	mathDesc.addVariable(constant);
	constant = new Constant("dact",ExpressionFactory.createExpression("1.2;"));
	mathDesc.addVariable(constant);
	constant = new Constant("Cer",ExpressionFactory.createExpression("10;"));
	mathDesc.addVariable(constant);
	constant = new Constant("vP",ExpressionFactory.createExpression("0.1;"));
	mathDesc.addVariable(constant);
	constant = new Constant("kP",ExpressionFactory.createExpression("0.4;"));
	mathDesc.addVariable(constant);
	constant = new Constant("dinh",ExpressionFactory.createExpression("1.5;"));
	mathDesc.addVariable(constant);
	constant = new Constant("tau0",ExpressionFactory.createExpression("4.0;"));
	mathDesc.addVariable(constant);
	
	//
	// Variables
	//
	VolVariable C = new VolVariable("C");
	mathDesc.addVariable(C);
	VolVariable h = new VolVariable("h");
	mathDesc.addVariable(h);
	
	//
	// C ODE for Cytosol
	//
	IExpression rateExpression = ExpressionFactory.createExpression("LambdaBeta*("+
														"(vL+pow(I*C*h/((I+dI)*(C+dact)),3))*(Cer-C)"+
														"-vP*(C*C/(C*C+kP*kP)));");
	IExpression initialExpression = ExpressionFactory.createExpression("3;");
	OdeEquation equ = new OdeEquation(C,initialExpression, rateExpression);
	cytosolSubDomain.addEquation(equ);

	//
	// h ODE for Cytosol
	//
	rateExpression = ExpressionFactory.createExpression("(dinh-(C+dinh)*h)/tau0;");
	initialExpression = ExpressionFactory.createExpression("0.93;");
	equ = new OdeEquation(h,initialExpression, rateExpression);
	cytosolSubDomain.addEquation(equ);

	return mathDesc;
}
/**
 * This method was created in VisualAge.
 * @param args java.lang.String[]
 */
public static void main(String args[]) {
	try {
		MathDescription m1 = getExample();
		MathDescription m2 = getExample();
		MathDescription m3 = getOdeExample();
		MathDescription m4 = getOdeExample();

		if (m1.compareEqual(m1)){
			System.out.println("m1.equals(m1)==true                                       PASSED");
		}else{
			System.out.println("m1.equals(m1)==false                                      FAILED");
		}

		if (m1.compareEqual(m2)){
			System.out.println("m1.equals(m2)==true            (where m1 == m2)           PASSED");
		}else{
			System.out.println("m1.equals(m2)==false           (where m1 == m2)           FAILED");
		}

		if (m1.compareEqual(m3)){
			System.out.println("m1.equals(m3)==true            (where m1 != m3)           FAILED");
		}else{
			System.out.println("m1.equals(m3)==false           (where m1 != m3)           PASSED");
		}

		if (m3.compareEqual(m4)){
			System.out.println("m3.equals(m4)==true            (where m3 == m4)           PASSED");
		}else{
			System.out.println("m3.equals(m4)==false           (where m3 == m4)           FAILED");
		}

	}catch (Throwable e){
		System.out.println("uncaught exception in MathDescriptionTest.main()");
		e.printStackTrace(System.out);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 11:32:46 PM)
 * @param oldMath cbit.vcell.math.MathDescription
 * @param newMath cbit.vcell.math.MathDescription
 */
public static boolean testIfSame(MathDescription oldMathDesc, MathDescription newMathDesc, StringBuffer reasonForDecision) {
	final String NATIVE_MATHS_ARE_SAME =			" MathsEquivalent:Native ";
	final String FLATTENED_MATHS_ARE_SAME =			" MathsEquivalent:Flattened ";
	final String MATHS_ARE_NUMERICALLY_EQUIVALENT =	" MathsEquivalent:Numerically ";
	final String DIFFERENT_NUMBER_OF_VARIABLES =	" MathsDifferent:DifferentNumberOfVariables ";
	final String VARIABLES_DONT_MATCH =				" MathsDifferent:VariablesDontMatch ";
	final String DIFFERENT_NUMBER_OF_EXPRESSIONS =	" MathsDifferent:DifferentNumberOfExpressions ";
	final String EQUATION_ADDED =					" MathsDifferent:EquationAdded ";
	final String EQUATION_REMOVED =					" MathsDifferent:EquationRemoved ";
	final String EXPRESSION_IS_DIFFERENT =			" MathsDifferent:ExpressionIsDifferent ";
	final String UNKNOWN_DIFFERENCE_IN_EQUATION =	" MathsDifferent:UnknownDifferenceInEquation ";
	final String DIFFERENT_NUMBER_OF_SUBDOMAINS =	" MathsDifferent:DifferentNumberOfSubdomains ";
	final String FAILURE_FLATTENING_DIV_BY_ZERO = 	" MathsDifferent:FailedFlatteningDivideByZero ";
	final String FAILURE_FLATTENING_UNKNOWN = 		" MathsDifferent:FailedFlatteningUnknown ";
	final String UNKNOWN_DIFFERENCE_IN_MATH =		" MathsDifferent:Unknown ";
	try {
	    if (oldMathDesc.compareEqual(newMathDesc)){
		    reasonForDecision.append(NATIVE_MATHS_ARE_SAME);
		    return true;
		}else{
		    //System.out.println("------NATIVE MATHS ARE DIFFERENT----------------------");
			//System.out.println("------old native MathDescription:\n"+oldMathDesc.getVCML_database());
			//System.out.println("------new native MathDescription:\n"+newMathDesc.getVCML_database());
			MathDescription strippedOldMath = MathDescription.createCanonicalMathDescription(oldMathDesc);
			MathDescription strippedNewMath = MathDescription.createCanonicalMathDescription(newMathDesc);
			if (strippedOldMath.compareEqual(strippedNewMath)){
				reasonForDecision.append(FLATTENED_MATHS_ARE_SAME);
			    return true;
			}else{
				Variable oldVars[] = (Variable[])org.vcell.util.BeanUtils.getArray(strippedOldMath.getVariables(),Variable.class);
				Variable newVars[] = (Variable[])org.vcell.util.BeanUtils.getArray(strippedNewMath.getVariables(),Variable.class);
				if (oldVars.length != newVars.length){
					//
					// number of state variables are not equal (canonical maths only have state variables)
					//
					reasonForDecision.append(DIFFERENT_NUMBER_OF_VARIABLES);
					return false;
				}
				if (!org.vcell.util.Compare.isEqual(oldVars,newVars)){
					//
					// variable names are not equivalent (nothing much we can do)
					//
					reasonForDecision.append(VARIABLES_DONT_MATCH);
					return false;
				}
				//
				// go through the list of SubDomains, and compare equations one by one and "correct" new one if possible
				//
				SubDomain subDomainsOld[] = (SubDomain[])org.vcell.util.BeanUtils.getArray(strippedOldMath.getSubDomains(),SubDomain.class);
				SubDomain subDomainsNew[] = (SubDomain[])org.vcell.util.BeanUtils.getArray(strippedNewMath.getSubDomains(),SubDomain.class);
				if (subDomainsOld.length != subDomainsNew.length){
					reasonForDecision.append(DIFFERENT_NUMBER_OF_SUBDOMAINS);
					return false;
				}
				for (int i = 0; i < subDomainsOld.length; i++){
					for (int j = 0; j < oldVars.length; j++){
						//
						// test equation for this subdomain and variable
						//
						{
						Equation oldEqu = subDomainsOld[i].getEquation(oldVars[j]);
						Equation newEqu = subDomainsNew[i].getEquation(oldVars[j]);
						if (!org.vcell.util.Compare.isEqualOrNull(oldEqu,newEqu)){
							boolean bFoundDifference = false;
							//
							// equation didn't compare exactly, lets try to evaluate some instead
							//
							if (oldEqu==null){
								//
								// only one MathDescription had Equation for this Variable.
								//
								reasonForDecision.append(EQUATION_ADDED);
								return false;
							}
							if (newEqu==null){
								//
								// only one MathDescription had Equation for this Variable.
								//
								reasonForDecision.append(EQUATION_REMOVED);
								return false;
							}
							IExpression oldExps[] = (IExpression[])org.vcell.util.BeanUtils.getArray(oldEqu.getExpressions(strippedOldMath),IExpression.class);
							IExpression newExps[] = (IExpression[])org.vcell.util.BeanUtils.getArray(newEqu.getExpressions(strippedNewMath),IExpression.class);
							if (oldExps.length != newExps.length){
								reasonForDecision.append(DIFFERENT_NUMBER_OF_EXPRESSIONS);
								return false;
							}
							for (int k = 0; k < oldExps.length; k++){
								if (!oldExps[k].compareEqual(newExps[k])){
									bFoundDifference = true;
									if (!ExpressionUtilities.functionallyEquivalent(oldExps[k], newExps[k])){
										//
										// difference couldn't be reconciled
										//
										System.out.println("expressions are different Old: '"+oldExps[k]+"'\n"+
														   "expressions are different New: '"+newExps[k]+"'");
										reasonForDecision.append(EXPRESSION_IS_DIFFERENT);
										return false;
									}else{
										//System.out.println("expressions are equivalent Old: '"+oldExps[k]+"'\n"+
														   //"expressions are equivalent New: '"+newExps[k]+"'");
									}
								}
							}
							//
							// equation was not strictly "equal" but passed all tests, replace with old equation and move on
							//
							if (bFoundDifference){
								subDomainsNew[i].replaceEquation(oldEqu);
							}else{
								//
								// couldn't find the smoking gun, just plain bad
								//
								System.out.println("couldn't find problem with equation for "+oldVars[j].getName()+" in compartment "+subDomainsOld[i].getName());
								reasonForDecision.append(UNKNOWN_DIFFERENCE_IN_EQUATION);
								return false;
							}
						}
						}
						//
						// if a membrane, test jumpCondition for this subdomain and variable
						//
						if (subDomainsOld[i] instanceof MembraneSubDomain && oldVars[j] instanceof VolVariable){
							JumpCondition oldJumpCondition = ((MembraneSubDomain)subDomainsOld[i]).getJumpCondition((VolVariable)oldVars[j]);
							JumpCondition newJumpCondition = ((MembraneSubDomain)subDomainsNew[i]).getJumpCondition((VolVariable)oldVars[j]);
							if (!org.vcell.util.Compare.isEqualOrNull(oldJumpCondition,newJumpCondition)){
								boolean bFoundDifference = false;
								//
								// equation didn't compare exactly, lets try to evaluate some instead
								//
								if (oldJumpCondition==null){
									//
									// only one MathDescription had Equation for this Variable.
									//
									reasonForDecision.append(EQUATION_ADDED);
									return false;
								}
								if (newJumpCondition==null){
									//
									// only one MathDescription had Equation for this Variable.
									//
									reasonForDecision.append(EQUATION_REMOVED);
									return false;
								}
								IExpression oldExps[] = (IExpression[])org.vcell.util.BeanUtils.getArray(oldJumpCondition.getExpressions(strippedOldMath),IExpression.class);
								IExpression newExps[] = (IExpression[])org.vcell.util.BeanUtils.getArray(newJumpCondition.getExpressions(strippedNewMath),IExpression.class);
								if (oldExps.length != newExps.length){
									reasonForDecision.append(DIFFERENT_NUMBER_OF_EXPRESSIONS);
									return false;
								}
								for (int k = 0; k < oldExps.length; k++){
									if (!oldExps[k].compareEqual(newExps[k])){
										bFoundDifference = true;
										if (!ExpressionUtilities.functionallyEquivalent(oldExps[k], newExps[k])){
											//
											// difference couldn't be reconciled
											//
											System.out.println("expressions are different Old: '"+oldExps[k]+"'\n"+
															   "expressions are different New: '"+newExps[k]+"'");
											reasonForDecision.append(EXPRESSION_IS_DIFFERENT);
											return false;
										}else{
											//System.out.println("expressions are equivalent Old: '"+oldExps[k]+"'\n"+
															   //"expressions are equivalent New: '"+newExps[k]+"'");
										}
									}
								}
								//
								// equation was not strictly "equal" but passed all tests, replace with old equation and move on
								//
								if (bFoundDifference){
									((MembraneSubDomain)subDomainsNew[i]).replaceJumpCondition(oldJumpCondition);
								}else{
									//
									// couldn't find the smoking gun, just plain bad
									//
									System.out.println("couldn't find problem with jumpCondition for "+oldVars[j].getName()+" in compartment "+subDomainsOld[i].getName());
									reasonForDecision.append(UNKNOWN_DIFFERENCE_IN_EQUATION);
									return false;
								}
							}
						}
					}
					//
					// test fast system for subdomain
					//
					FastSystem oldFastSystem = subDomainsOld[i].getFastSystem();
					FastSystem newFastSystem = subDomainsNew[i].getFastSystem();
					if (!org.vcell.util.Compare.isEqualOrNull(oldFastSystem,newFastSystem)){
						boolean bFoundDifference = false;
						//
						// fastSystems didn't compare exactly, lets try to evaluate some expressions instead
						//
						if (oldFastSystem==null){
							//
							// only one MathDescription had Equation for this Variable.
							//
							reasonForDecision.append(EQUATION_ADDED);
							return false;
						}
						if (newFastSystem==null){
							//
							// only one MathDescription had Equation for this Variable.
							//
							reasonForDecision.append(EQUATION_REMOVED);
							return false;
						}
						IExpression oldExps[] = oldFastSystem.getExpressions();
						IExpression newExps[] = newFastSystem.getExpressions();
						if (oldExps.length != newExps.length){
							reasonForDecision.append(DIFFERENT_NUMBER_OF_EXPRESSIONS);
							return false;
						}
						for (int k = 0; k < oldExps.length; k++){
							if (!oldExps[k].compareEqual(newExps[k])){
								bFoundDifference = true;
								if (!ExpressionUtilities.functionallyEquivalent(oldExps[k], newExps[k])){
									//
									// difference couldn't be reconciled
									//
									System.out.println("expressions are different Old: '"+oldExps[k]+"'\n"+
													   "expressions are different New: '"+newExps[k]+"'");
									reasonForDecision.append(EXPRESSION_IS_DIFFERENT);
									return false;
								}else{
									//System.out.println("expressions are equivalent Old: '"+oldExps[k]+"'\n"+
													   //"expressions are equivalent New: '"+newExps[k]+"'");
								}
							}
						}
						//
						// equation was not strictly "equal" but passed all tests, replace with old equation and move on
						//
						if (bFoundDifference){
							subDomainsNew[i].setFastSystem(oldFastSystem);
						}else{
							//
							// couldn't find the smoking gun, just plain bad
							//
							System.out.println("couldn't find problem with FastSystem for compartment "+subDomainsOld[i].getName());
							reasonForDecision.append(UNKNOWN_DIFFERENCE_IN_EQUATION);
							return false;
						}
					}
				}

				//
				// after repairing aspects of MathDescription, now see if same
				//
				if (strippedOldMath.compareEqual(strippedNewMath)){
					reasonForDecision.append(MATHS_ARE_NUMERICALLY_EQUIVALENT);
					return true;
				}else{
				    //System.out.println("------UNKNOWN DIFFERENCE IN MATH----------------------");
					//System.out.println("------old flattened MathDescription:\n"+strippedOldMath.getVCML_database());
					//System.out.println("------new flattened MathDescription:\n"+strippedNewMath.getVCML_database());
					reasonForDecision.append(UNKNOWN_DIFFERENCE_IN_MATH);
					return false;
				}
			}
		}
	}catch (org.vcell.expression.DivideByZeroException e){
		System.out.println("-------DIVIDE BY ZERO EXCEPTION-------------------------");
		reasonForDecision.append(FAILURE_FLATTENING_DIV_BY_ZERO);
		return false;
	}catch (Throwable e){
		e.printStackTrace(System.out);
		reasonForDecision.append(FAILURE_FLATTENING_UNKNOWN);
		return false;
	}
}
}
