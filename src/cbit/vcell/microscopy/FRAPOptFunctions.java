package cbit.vcell.microscopy;

import java.util.Arrays;

import org.vcell.optimization.ProfileData;
import org.vcell.optimization.ProfileDataElement;
import org.vcell.util.UserCancelException;

import cbit.vcell.client.task.ClientTaskStatusSupport;
import cbit.vcell.model.ReservedSymbol;
import cbit.vcell.modelopt.gui.DataSource;
import cbit.vcell.opt.Parameter;
import cbit.vcell.opt.ReferenceData;
import cbit.vcell.opt.SimpleReferenceData;
import cbit.vcell.parser.Expression;
import cbit.vcell.solver.ode.ODESolverResultSet;
import cbit.vcell.solver.ode.ODESolverResultSetColumnDescription;

public class FRAPOptFunctions 
{
	public static String SYMBOL_A = "A";
	public static String SYMBOL_KOFF = FRAPModel.MODEL_PARAMETER_NAMES[FRAPModel.INDEX_OFF_RATE];
	public static String SYMBOL_BWM_RATE = FRAPModel.MODEL_PARAMETER_NAMES[FRAPModel.INDEX_BLEACH_MONITOR_RATE];
	public static String SYMBOL_I_inibleached = "I_inibleached";
	public static String FUNC_RECOVERY_BLEACH_REACTION_DOMINANT = SYMBOL_I_inibleached + "+" +SYMBOL_A + "*(1-exp(-1*"+SYMBOL_KOFF+"*"+ReservedSymbol.TIME.getName()+"))" + "*exp(-1*"+ SYMBOL_BWM_RATE+"*"+ ReservedSymbol.TIME.getName() +")";
	public static String SYMBOL_I_inicell = "I_inicell";
	public static String FUNC_CELL_INTENSITY = SYMBOL_I_inicell + "*(exp(-1*"+SYMBOL_BWM_RATE+"*"+ReservedSymbol.TIME.getName()+"))";
	
	private static int NUM_PARAM_ESTIMATED = 2;
	//used for optimization when taking measurement error into account.
	//first dimension length 11, according to the order in FRAPData.VFRAP_ROI_ENUM
	//second dimension time, total time length - starting index for recovery 
//	private double[][] measurementErrors = null;
	private FRAPStudy expFrapStudy = null;
	private FrapDataAnalysisResults.ReactionOnlyAnalysisRestults offRateResults = null;
	
	public FRAPOptFunctions(FRAPStudy argExpFrapStudy)
	{
		expFrapStudy = argExpFrapStudy;
	}
	
	public FRAPStudy getExpFrapStudy() {
		return expFrapStudy;
	}
	
	public FrapDataAnalysisResults.ReactionOnlyAnalysisRestults getOffRateResults() {
		return offRateResults;
	}

	public void setOffRateResults(FrapDataAnalysisResults.ReactionOnlyAnalysisRestults offRateResults) {
		this.offRateResults = offRateResults;
	}

	//The best parameters will return a whole set of reaction off rate parameters (totally 8 parameters)
	public Parameter[] getBestParamters(FRAPData frapData, Parameter fixedParam) throws Exception
	{
//		if(measurementErrors == null)
//		{
//			FRAPOptimizationUtils.refreshNormalizedMeasurementError(getExpFrapStudy());
//		}
		Parameter[] outputParams = new Parameter[FRAPModel.NUM_MODEL_PARAMETERS_REACTION_OFF_RATE];
		FrapDataAnalysisResults.ReactionOnlyAnalysisRestults offRateResults = FRAPDataAnalysis.fitRecovery_reacOffRateOnly(frapData, fixedParam);
		setOffRateResults(offRateResults);
		
		outputParams[FRAPModel.INDEX_BLEACH_MONITOR_RATE] = new Parameter(FRAPModel.MODEL_PARAMETER_NAMES[FRAPModel.INDEX_BLEACH_MONITOR_RATE],
															    FRAPModel.REF_BLEACH_WHILE_MONITOR_PARAM.getLowerBound(),
															    FRAPModel.REF_BLEACH_WHILE_MONITOR_PARAM.getUpperBound(),
															    FRAPModel.REF_BLEACH_WHILE_MONITOR_PARAM.getScale(),
															    offRateResults.getBleachWhileMonitoringTau());
		outputParams[FRAPModel.INDEX_BINDING_SITE_CONCENTRATION] = new Parameter(FRAPModel.MODEL_PARAMETER_NAMES[FRAPModel.INDEX_BINDING_SITE_CONCENTRATION],
																FRAPModel.REF_BS_CONCENTRATION_OR_A.getLowerBound(),
																FRAPModel.REF_BS_CONCENTRATION_OR_A.getUpperBound(),
																FRAPModel.REF_BS_CONCENTRATION_OR_A.getScale(),
																offRateResults.getFittingParamA());
		outputParams[FRAPModel.INDEX_OFF_RATE] = new Parameter (FRAPModel.MODEL_PARAMETER_NAMES[FRAPModel.INDEX_OFF_RATE],
															    FRAPModel.REF_REACTION_OFF_RATE.getLowerBound(),
															    FRAPModel.REF_REACTION_OFF_RATE.getUpperBound(),
															    FRAPModel.REF_REACTION_OFF_RATE.getScale(),
															    offRateResults.getOffRate());
		
		return outputParams;
	}
	
	public ProfileData[] evaluateParameters(Parameter[] currentParams, ClientTaskStatusSupport clientTaskStatusSupport) throws Exception
	{
		int totalParamLen = currentParams.length;
		int resultDataCounter = 0;
		ProfileData[] resultData = new ProfileData[NUM_PARAM_ESTIMATED];
		FRAPStudy frapStudy = getExpFrapStudy();
		for(int j=0; j<totalParamLen; j++)
		{	//only bleach while monitoring rate and reaction off rate need to evaluated
			if(currentParams[j] != null && (currentParams[j].getName().equals(FRAPModel.MODEL_PARAMETER_NAMES[FRAPModel.INDEX_BLEACH_MONITOR_RATE]) ||
			   currentParams[j].getName().equals(FRAPModel.MODEL_PARAMETER_NAMES[FRAPModel.INDEX_OFF_RATE])))
			{
				ProfileData profileData = new ProfileData();
				//add the fixed parameter to profileData, output exp data and opt results
				Parameter[] newBestParameters = getBestParamters(frapStudy.getFrapData(), null);
				double iniError = getOffRateResults().getLeastOffRateFuncError();
				//fixed parameter(make sure parameter shall not be smaller than epsilon)
				Parameter fixedParam = newBestParameters[j];
				if(fixedParam.getInitialGuess() == 0)//log function cannot take 0 as parameter
				{
					fixedParam = new Parameter (fixedParam.getName(),
		                        fixedParam.getLowerBound(),
		                        fixedParam.getUpperBound(),
		                        fixedParam.getScale(),
		                        FRAPOptimizationUtils.epsilon);
				}
				if(clientTaskStatusSupport != null)
				{
					clientTaskStatusSupport.setMessage("<html>Evaluating confidence intervals of \'" + fixedParam.getName() + "\' <br> of finding reaction off rate model.</html>");
					clientTaskStatusSupport.setProgress(0);//start evaluation of a parameter.
				}
				ProfileDataElement pde = new ProfileDataElement(fixedParam.getName(), Math.log10(fixedParam.getInitialGuess()), iniError, newBestParameters);
				profileData.addElement(pde);
				//increase
				int iterationCount = 1;
				double paramLogVal = Math.log10(fixedParam.getInitialGuess());
				double lastError = iniError;
				boolean isBoundReached = false;
				double incrementStep = FRAPOptData.DEFAULT_CI_STEPS_OFF_RATE[resultDataCounter];
				int stepIncreaseCount = 0;
				while(true)
				{
					if(iterationCount > FRAPOptData.MAX_ITERATION)//if exceeds the maximum iterations, break;
					{
						break;
					}
					if(isBoundReached)
					{
						break;
					}
					paramLogVal = paramLogVal + incrementStep ;
					double paramVal = Math.pow(10,paramLogVal);
					if(paramVal > (fixedParam.getUpperBound() - FRAPOptimizationUtils.epsilon))
					{
						paramVal = fixedParam.getUpperBound();
						paramLogVal = Math.log10(fixedParam.getUpperBound());
						isBoundReached = true;
					}
					Parameter increasedParam = new Parameter (fixedParam.getName(),
		                                                      fixedParam.getLowerBound(),
		                                                      fixedParam.getUpperBound(),
		                                                      fixedParam.getScale(),
		                                                      paramVal);
					//getBestParameters returns the whole set of parameters including the fixed parameters
					Parameter[] newParameters = getBestParamters(frapStudy.getFrapData(), increasedParam);
					double error = getOffRateResults().getLeastOffRateFuncError();
					pde = new ProfileDataElement(increasedParam.getName(), paramLogVal, error, newParameters);
					profileData.addElement(pde);
					//check if the we run enough to get confidence intervals(99% @6.635, we plus 10 over the min error)
					if(error > (iniError+10))
					{
						break;
					}
					if(Math.abs((error-lastError)/lastError) < FRAPOptData.MIN_LIKELIHOOD_CHANGE)
					{
						stepIncreaseCount ++;
						incrementStep = FRAPOptData.DEFAULT_CI_STEPS_OFF_RATE[resultDataCounter] * Math.pow(2, stepIncreaseCount);
					}
					else
					{
						if(stepIncreaseCount > 1)
						{
							incrementStep = FRAPOptData.DEFAULT_CI_STEPS_OFF_RATE[resultDataCounter] / Math.pow(2, stepIncreaseCount);
							stepIncreaseCount --;
						}
					}
					
					if (clientTaskStatusSupport.isInterrupted())
					{
						throw UserCancelException.CANCEL_GENERIC;
					}
	
					lastError = error;
					iterationCount++;
					clientTaskStatusSupport.setProgress((int)((iterationCount*1.0/FRAPOptData.MAX_ITERATION) * 0.5 * 100));
				}
				clientTaskStatusSupport.setProgress(50);//half way through evaluation of a parameter.
				//decrease
				iterationCount = 1;
				paramLogVal = Math.log10(fixedParam.getInitialGuess());
				lastError = iniError;
				isBoundReached = false;
				double decrementStep = FRAPOptData.DEFAULT_CI_STEPS_OFF_RATE[resultDataCounter];
				stepIncreaseCount = 0;
				while(true)
				{
					if(iterationCount > FRAPOptData.MAX_ITERATION)//if exceeds the maximum iterations, break;
					{
						break;
					}
					if(isBoundReached)
					{
						break;
					}
					paramLogVal = paramLogVal - decrementStep;
					double paramVal = Math.pow(10,paramLogVal);
					System.out.println("paramVal:" + paramVal);
					if(paramVal < (fixedParam.getLowerBound() + FRAPOptimizationUtils.epsilon))
					{
						paramVal = fixedParam.getLowerBound();
						paramLogVal = Math.log10(FRAPOptimizationUtils.epsilon);
						isBoundReached = true;
					}
					Parameter decreasedParam = new Parameter (fixedParam.getName(),
		                                            fixedParam.getLowerBound(),
		                                            fixedParam.getUpperBound(),
		                                            fixedParam.getScale(),
		                                            paramVal);
					//getBestParameters returns the whole set of parameters including the fixed parameters
					Parameter[] newParameters = getBestParamters(frapStudy.getFrapData(), decreasedParam);
					double error = getOffRateResults().getLeastOffRateFuncError();
					pde = new ProfileDataElement(decreasedParam.getName(), paramLogVal, error, newParameters);
					profileData.addElement(0,pde);
					if(error > (iniError+10))
					{
						break;
					}
					if(Math.abs((error-lastError)/lastError) < FRAPOptData.MIN_LIKELIHOOD_CHANGE)
					{
						stepIncreaseCount ++;
						decrementStep = FRAPOptData.DEFAULT_CI_STEPS_OFF_RATE[resultDataCounter] * Math.pow(2, stepIncreaseCount);
					}
					else
					{
						if(stepIncreaseCount > 1)
						{
							incrementStep = FRAPOptData.DEFAULT_CI_STEPS_OFF_RATE[resultDataCounter] / Math.pow(2, stepIncreaseCount);
							stepIncreaseCount --;
						}
					}
					if (clientTaskStatusSupport.isInterrupted())
					{
						throw UserCancelException.CANCEL_GENERIC;
					}
					lastError = error;
					iterationCount++;
					clientTaskStatusSupport.setProgress((int)(((iterationCount+FRAPOptData.MAX_ITERATION)*1.0/FRAPOptData.MAX_ITERATION) * 0.5 * 100));
				}
				resultData[resultDataCounter++] = profileData;
				clientTaskStatusSupport.setProgress(100);//finish evaluation of a parameter
			}
			else
			{
				continue;
			}
		}
		//this message is specifically set for batchrun, the message will stay in the status panel. It doesn't affect single run,which disappears quickly that user won't notice.
		clientTaskStatusSupport.setMessage("Evaluating confidence intervals ...");
		return resultData;
	}
	
	public static void main(String argv[])
	{
		System.out.println(FUNC_RECOVERY_BLEACH_REACTION_DOMINANT);
		System.out.println(FUNC_CELL_INTENSITY);
	}
}
