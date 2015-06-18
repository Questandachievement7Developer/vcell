package cbit.vcell.client.pyvcellproxy;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.thrift.TException;
import org.vcell.util.FileUtils;
import org.vcell.util.document.KeyValue;
import org.vcell.util.document.User;
import org.vcell.util.document.VCDocument;
import org.vcell.util.gui.DialogUtils;
import org.vcell.vis.io.VtuFileContainer;
import org.vcell.vis.io.VtuVarInfo;
import org.vcell.vis.vtk.VtkGridUtils;

import vtk.vtkDoubleArray;
import vtk.vtkUnstructuredGrid;
import cbit.vcell.biomodel.BioModel;
import cbit.vcell.client.BioModelWindowManager;
import cbit.vcell.client.MathModelWindowManager;
import cbit.vcell.client.TopLevelWindowManager;
import cbit.vcell.client.VCellClient;
import cbit.vcell.client.data.DataProcessingResultsPanel;
import cbit.vcell.client.task.AsynchClientTask;
import cbit.vcell.client.task.ClientTaskDispatcher;
import cbit.vcell.clientdb.ClientDocumentManager;
import cbit.vcell.math.VariableType.VariableDomain;
import cbit.vcell.mathmodel.MathModel;
import cbit.vcell.resource.ResourceUtil;
import cbit.vcell.server.SimulationStatus;
import cbit.vcell.simdata.ClientPDEDataContext;
import cbit.vcell.simdata.OutputContext;
import cbit.vcell.simdata.PDEDataManager;
import cbit.vcell.solver.AnnotatedFunction;
import cbit.vcell.solver.Simulation;
import cbit.vcell.solver.SimulationInfo;
import cbit.vcell.solver.SimulationOwner;
import cbit.vcell.solver.VCSimulationDataIdentifier;
import cbit.vcell.solver.VCSimulationIdentifier;

public class VCellProxyHandler implements VCellProxy.Iface{

private final VCellClient vcellClient;
private final File localVisDataDir;

public VCellProxyHandler(VCellClient vcellClient) {
	this.vcellClient = vcellClient;
	this.localVisDataDir = ResourceUtil.getLocalVisDataDir();
}

@Override 
public List<SimulationDataSetRef> getSimsFromOpenModels() throws cbit.vcell.client.pyvcellproxy.ThriftDataAccessException, org.apache.thrift.TException {
	ArrayList<SimulationDataSetRef> simulationDataSetRefs = new ArrayList<SimulationDataSetRef>();
	TopLevelWindowManager windowManager = null;
	Enumeration<TopLevelWindowManager> windowManagers = vcellClient.getMdiManager().getWindowManagers();

	while (windowManagers.hasMoreElements()) {
		windowManager = windowManagers.nextElement();
		Simulation[] simulations = null;
		VCDocument modelDocument = null;
		int simOwnerCount = 0;
		
		if (windowManager instanceof BioModelWindowManager){
			BioModelWindowManager selectedBioWindowManager = (BioModelWindowManager)windowManager;
			BioModel bioModel = selectedBioWindowManager.getBioModel();
			simulations = bioModel.getSimulations();
			modelDocument = bioModel;
			simOwnerCount = bioModel.getNumSimulationContexts();
			
		}else if (windowManager instanceof MathModelWindowManager){
			MathModelWindowManager selectedMathWindowManager = (MathModelWindowManager)windowManager; 
			MathModel mathModel = selectedMathWindowManager.getMathModel();
			simulations = mathModel.getSimulations();
			modelDocument = mathModel;
			simOwnerCount = 1;
		}
		if (simulations != null){
			for (Simulation simulation : simulations){
				if (!isVtkSupported(simulation)){
					continue;
				}
			    SimulationInfo simInfo=simulation.getSimulationInfo();
				SimulationStatus simStatus = vcellClient.getRequestManager().getServerSimulationStatus(simInfo);
				for (int jobIndex = 0; jobIndex<simulation.getScanCount(); jobIndex++){
					if (simStatus!=null && simStatus.getHasData()){
						SimulationDataSetRef simulationDataSetReference = new SimulationDataSetRef();
						simulationDataSetReference.setSimName(simInfo.getName());
						String simName = simInfo.getName();
						if (jobIndex!=0){
							simName = simName + " job#"+String.valueOf(jobIndex);
						}
						
						simulationDataSetReference.setSimId(simInfo.getAuthoritativeVCSimulationIdentifier().getSimulationKey().toString());
						simulationDataSetReference.setModelId(modelDocument.getVersion().getVersionKey().toString());
						simulationDataSetReference.setUsername(simInfo.getVersion().getOwner().getName());
						simulationDataSetReference.setUserkey(simInfo.getVersion().getOwner().getID().toString());
						simulationDataSetReference.setIsMathModel(modelDocument instanceof MathModel);
						simulationDataSetReference.setJobIndex(jobIndex);
						if (modelDocument instanceof BioModel){
							BioModel bm = (BioModel) modelDocument; 
							simulationDataSetReference.setSimulationContextName(simulation.getSimulationOwner().getName());
							if (bm.getNumSimulationContexts()>0){
								simName = simName + " application: "+simulation.getSimulationOwner().getName();
							}
						}
						simulationDataSetReference.setSimName(simInfo.getName());
						simulationDataSetRefs.add(simulationDataSetReference);
					
					}
				}
			}
		}
	}
	return simulationDataSetRefs;
}

private boolean isVtkSupported(Simulation simulation){
	if (simulation.getMathDescription().getGeometry().getDimension()<2){
		return false;
	}
	return true;
}

@Override
public List<VariableInfo> getVariableList(SimulationDataSetRef simulationDataSetRef) throws cbit.vcell.client.pyvcellproxy.ThriftDataAccessException {
	try {
		VCSimulationDataIdentifier vcSimulationDataIdentifier = getVCSimulationDataIdentifier(simulationDataSetRef);
		PDEDataManager pdeDataManager = (PDEDataManager)vcellClient.getRequestManager().getDataManager(null, vcSimulationDataIdentifier, true);
		OutputContext outputContext = null;
		VtuVarInfo[] vtuVarInfos = pdeDataManager.getVtuVarInfos(outputContext);
		ArrayList<VariableInfo> varInfoList = new ArrayList<VariableInfo>();
		for (VtuVarInfo vtuVarInfo : vtuVarInfos){
			DomainType variableDomainType = null;
			switch (vtuVarInfo.variableDomain){
				case VARIABLEDOMAIN_MEMBRANE:{
					variableDomainType = DomainType.MEMBRANE;
					break;
				}
				case VARIABLEDOMAIN_VOLUME:{
					variableDomainType = DomainType.VOLUME;
					break;
				}
				default:{
					break;
				}
			}
			String unitsLabel = "<unknown unit>";
			if (vtuVarInfo.bMeshVariable){
				continue; // skip "mesh variables" like size, vcRegionArea, etc, globalIndex, etc.
			}
			varInfoList.add(new VariableInfo(vtuVarInfo.name, vtuVarInfo.displayName, vtuVarInfo.domainName, variableDomainType, unitsLabel, vtuVarInfo.bMeshVariable));
		}
		return varInfoList;
	} catch (Exception e) {
		e.printStackTrace();
		throw new ThriftDataAccessException("failed to retrieve variable list for data set.");
	}

}



private File getEmptyMeshFileLocation(SimulationDataSetRef simulationDataSetRef, String domainName){
	
	VCSimulationDataIdentifier vcSimulationDataIdentifier = getVCSimulationDataIdentifier(simulationDataSetRef);
	
	//
	// create the dataset directory if necessary
	//
	File vtuSimDataFolder = new File(localVisDataDir,simulationDataSetRef.getSimId());
	if (!(vtuSimDataFolder.exists() && vtuSimDataFolder.isDirectory())){
		vtuSimDataFolder.mkdirs();
	}
	
	//
	// compose the specific mesh filename
	//
	File vtuDataFile = new File(vtuSimDataFolder, vcSimulationDataIdentifier.getID()+"_"+domainName+".vtu");
	return vtuDataFile;
}

private File getPopulatedMeshFileLocation(SimulationDataSetRef simulationDataSetRef, VariableInfo varInfo, int timeIndex){
	
	VCSimulationDataIdentifier vcSimulationDataIdentifier = getVCSimulationDataIdentifier(simulationDataSetRef);
	
	//
	// create the dataset directory if necessary
	//
	File vtuSimDataFolder = new File(localVisDataDir,simulationDataSetRef.getSimId());
	if (!(vtuSimDataFolder.exists() && vtuSimDataFolder.isDirectory())){
		vtuSimDataFolder.mkdirs();
	}
	
	//
	// compose the specific mesh filename
	//
	String timeIndexStr = String.format("%06d" , timeIndex);
	String domainName = varInfo.domainName;
	String varName = varInfo.variableVtuName.replace(":", "_");
	File vtuDataFile = new File(vtuSimDataFolder, vcSimulationDataIdentifier.getID()+"_"+domainName+"_"+varName+"_"+timeIndexStr+".vtu");
	return vtuDataFile;
}

@Override
public String getDataSetFileOfVariableAtTimeIndex(SimulationDataSetRef simulationDataSetRef, VariableInfo var, int timeIndex) throws ThriftDataAccessException {
	try {
		if (var.isMeshVar){
			return getEmptyMeshFile(simulationDataSetRef, var.domainName).getAbsolutePath();
		}
		File meshFileForVariableAndTime = getPopulatedMeshFileLocation(simulationDataSetRef, var, timeIndex);
		if (meshFileForVariableAndTime.exists()){
			return meshFileForVariableAndTime.getAbsolutePath();
		}
		
		//
		// get data from server for this variable, domain, time
		//
		VCSimulationDataIdentifier vcSimulationDataIdentifier = getVCSimulationDataIdentifier(simulationDataSetRef);
		PDEDataManager pdeDataManager = (PDEDataManager)vcellClient.getRequestManager().getDataManager(null, vcSimulationDataIdentifier, true);
		VariableDomain variableDomainType = VariableDomain.VARIABLEDOMAIN_UNKNOWN;
		if (var.variableDomainType == DomainType.MEMBRANE){
			variableDomainType = VariableDomain.VARIABLEDOMAIN_MEMBRANE;
		}else if (var.variableDomainType == DomainType.VOLUME){
			variableDomainType = VariableDomain.VARIABLEDOMAIN_VOLUME;
		}
		VtuVarInfo vtuVarInfo = new VtuVarInfo(var.getVariableVtuName(),var.getVariableDisplayName(),var.getDomainName(),variableDomainType,var.isMeshVar);
		List<Double> times = getTimePoints(simulationDataSetRef);
		double time = (double)times.get(timeIndex);
		double[] data = pdeDataManager.getVtuMeshData(vtuVarInfo, time);
		
		//
		// get empty mesh file for this domain (getEmptyMeshFile() will ensure that the file exists or create it).
		//
		File emptyMeshFile = getEmptyMeshFile(simulationDataSetRef, var.getDomainName());
		VtkGridUtils vtkGridUtils = new VtkGridUtils();
		
		vtkUnstructuredGrid vtkgrid = null;
		try {
			vtkgrid = vtkGridUtils.read(emptyMeshFile.getCanonicalPath());
			vtkgrid.BuildLinks();
			
			//
			// add cell data array to the empty mesh for this variable
			//
			vtkDoubleArray dataArray = new vtkDoubleArray();
			dataArray.SetName(var.getVariableVtuName());
			dataArray.SetJavaArray(data);
			vtkgrid.GetCellData().AddArray(dataArray);
			
			//
			// write mesh and data to the file for that domain and time
			//
			vtkGridUtils.write(vtkgrid, meshFileForVariableAndTime.getAbsolutePath());
			
			return meshFileForVariableAndTime.getAbsolutePath();
		} finally {
			if (vtkgrid != null){
				vtkgrid.Delete();
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
		throw new ThriftDataAccessException("failed to retrieve data file for variable "+var.getVariableVtuName()+" at time index "+timeIndex);
	}
}


private File getEmptyMeshFile(SimulationDataSetRef simulationDataSetRef, String domainName) throws ThriftDataAccessException {
	
	File vtuEmptyMeshFile = getEmptyMeshFileLocation(simulationDataSetRef, domainName);

	System.out.println("looking for file: "+vtuEmptyMeshFile);
	
	//
	// if requested mesh file is not found, get files for this timepoint
	//
	if (!vtuEmptyMeshFile.exists()){
		try {
			VtuFileContainer vtuFileContainer = downloadEmptyVtuFileContainer(simulationDataSetRef);

			for (VtuFileContainer.VtuMesh mesh : vtuFileContainer.getVtuMeshes()){
				FileUtils.writeByteArrayToFile(mesh.vtuMeshContents, getEmptyMeshFileLocation(simulationDataSetRef, mesh.domainName));
			}
			
			if (!vtuEmptyMeshFile.exists()){
				System.out.println("after export, couldn't find requested empty mesh file "+vtuEmptyMeshFile);
				throw new ThriftDataAccessException("after export, couldn't find empty requested mesh file "+vtuEmptyMeshFile);
			}
			System.out.println("vtuData file exists, " + vtuEmptyMeshFile);

		}catch (IOException e){
			e.printStackTrace();
			System.out.println("failed to export entire dataset: "+e.getMessage());
			throw new ThriftDataAccessException("failed to export entire dataset: "+e.getMessage());
		}
	}
	
	return vtuEmptyMeshFile;
}

private VtuFileContainer downloadEmptyVtuFileContainer(SimulationDataSetRef simulationDataSetRef) throws ThriftDataAccessException {
	try {
		VCSimulationDataIdentifier vcSimulationDataIdentifier = getVCSimulationDataIdentifier(simulationDataSetRef);
		PDEDataManager pdeDataManager = (PDEDataManager)vcellClient.getRequestManager().getDataManager(null, vcSimulationDataIdentifier, true);
		VtuFileContainer vtuFileContainer = pdeDataManager.getEmptyVtuMeshFiles();
		return vtuFileContainer;
	}catch (Exception e){
		e.printStackTrace();
		throw new ThriftDataAccessException("failed to get data for simulation "+simulationDataSetRef+": "+e.getMessage());
	}
}

private VCSimulationDataIdentifier getVCSimulationDataIdentifier(SimulationDataSetRef simulationDataSetRef){
	User user = new User(simulationDataSetRef.getUsername(),new KeyValue(simulationDataSetRef.getUserkey()));
	KeyValue simKeyValue = new KeyValue(simulationDataSetRef.getSimId());
	VCSimulationIdentifier vcSimulationIdentifier = new VCSimulationIdentifier(simKeyValue, user);
	VCSimulationDataIdentifier vcSimulationDataIdentifier = new VCSimulationDataIdentifier(vcSimulationIdentifier, simulationDataSetRef.getJobIndex());
	
	return vcSimulationDataIdentifier;
}

@Override
public List<Double> getTimePoints(SimulationDataSetRef simulationDataSetRef) throws ThriftDataAccessException {
	VCSimulationDataIdentifier vcSimulationDataIdentifier = getVCSimulationDataIdentifier(simulationDataSetRef);

	ArrayList<Double> timesList = new ArrayList<Double>();
	try {
		if (vcSimulationDataIdentifier != null) {				
			double[] timesArray = vcellClient.getRequestManager().getDataManager(null, vcSimulationDataIdentifier, true).getDataSetTimes();
			for (int i=0; i<timesArray.length; i++){
			    timesList.add(new Double(timesArray[i]));
			}
			return timesList;			 
		}
	} catch (org.vcell.util.DataAccessException e) {
		e.printStackTrace();
	}
	return null;
}

@Override
public PostProcessingData getPostProcessingData(
		SimulationDataSetRef simulationDataSetRef)
		throws ThriftDataAccessException, TException {
	return null;
}

//private PostProcessingData getMockPostProcessingData(SimulationDataSetRef simulationDataSetRef) throws ThriftDataAccessException {
//	PostProcessingData mockPostProcessingData = new PostProcessingData();
//	mockPostProcessingData.setVariableList(getVariableList(simulationDataSetRef));
//	System.out.println("Var list size should be "+mockPostProcessingData.getVariableListSize());
//	PlotData mockPlotData = new PlotData();
//	ArrayList<Double> timePoints = (ArrayList<Double>) getTimePoints(simulationDataSetRef);
//	mockPlotData.setTimePoints(timePoints);
//	
//	ArrayList<Double> mockData = new ArrayList<Double>();
//	ListIterator<Double> listIterator = timePoints.listIterator();
//	while (listIterator.hasNext()) {
//		mockData.add(new Double(2+java.lang.Math.sin(listIterator.next().doubleValue())));
//	}
//	mockPlotData.setData(mockData);
//	mockPostProcessingData.setPlotData(new ArrayList<PlotData>());
//	ListIterator<VariableInfo> variableIter = mockPostProcessingData.getVariableList().listIterator();
//	while (variableIter.hasNext()){
//		VariableInfo var = variableIter.next();
//		mockPostProcessingData.getPlotData().add(mockPlotData);
//	}
//	return mockPostProcessingData;		 
//}

private static class FindWindow {
	public static Window getWindow(Component component) {
		if (component == null) {
			return JOptionPane.getRootFrame();
		} else if (component instanceof Window) {
			return (Window) component;
		} else {
			return getWindow(component.getParent());
		}
	}
}
	
@Override
public void displayPostProcessingDataInVCell(SimulationDataSetRef simulationDataSetRef)throws ThriftDataAccessException, TException {
	 try {
		 User vcUser = vcellClient.getRequestManager().getDocumentManager().getUser();
		 VCSimulationIdentifier vcSimId = new VCSimulationIdentifier(new KeyValue(simulationDataSetRef.getSimId()), vcUser );
		 ClientDocumentManager clientDocumentManager = (ClientDocumentManager) vcellClient.getClientServerManager().getDocumentManager();
		 SimulationOwner simulationOwner = null;
		 try {
			 if(simulationDataSetRef.isMathModel){
				 simulationOwner = clientDocumentManager.getMathModel(new KeyValue(simulationDataSetRef.getModelId()));
			 }else{
				 BioModel bioModel = clientDocumentManager.getBioModel(new KeyValue(simulationDataSetRef.getModelId()));
				 simulationOwner = bioModel.getSimulationContext(simulationDataSetRef.getSimulationContextName());
			 }
		 } catch (Exception e1) {
				e1.printStackTrace();
				throw new ThriftDataAccessException(e1.getMessage());
		 }
		 ArrayList<AnnotatedFunction> outputFunctionsList = simulationOwner.getOutputFunctionContext().getOutputFunctionsList();
		 OutputContext outputContext = new OutputContext(outputFunctionsList.toArray(new AnnotatedFunction[outputFunctionsList.size()]));
		 VCSimulationDataIdentifier vcSimDataId = new VCSimulationDataIdentifier(vcSimId, simulationDataSetRef.getJobIndex());
		 PDEDataManager pdeDataManager = null;
		 try {
			 pdeDataManager = (PDEDataManager)vcellClient.getRequestManager().getDataManager(outputContext, vcSimDataId , true);
		} catch (org.vcell.util.DataAccessException e1) {
			e1.printStackTrace();
			throw new ThriftDataAccessException(e1.getMessage());
		}
		 final ClientPDEDataContext newClientPDEDataContext = pdeDataManager.getPDEDataContext();
		
		Enumeration<TopLevelWindowManager> windowManagers = vcellClient.getMdiManager().getWindowManagers();
		final Window window = FindWindow.getWindow(windowManagers.nextElement().getComponent());
	
		AsynchClientTask task = new AsynchClientTask("Display Post Processing Statistics", AsynchClientTask.TASKTYPE_SWING_NONBLOCKING,false,false) {
			
			@Override
			public void run(Hashtable<String, Object> hashTable) throws Exception {
				DataProcessingResultsPanel dataProcessingResultsPanel = new DataProcessingResultsPanel();
				dataProcessingResultsPanel.update(newClientPDEDataContext);
				DialogUtils.showComponentOKCancelDialog(window, dataProcessingResultsPanel, "Post Processing Statistics");			
			}
		};
		Hashtable<String, Object> hash = new Hashtable<String, Object>();
		Vector<AsynchClientTask> tasksV = new Vector<AsynchClientTask>();
		tasksV.add(task);
		AsynchClientTask[] tasks = new AsynchClientTask[tasksV.size()];
		tasksV.copyInto(tasks);
		ClientTaskDispatcher.dispatch(window, hash, tasks, true);
	} catch (Throwable exc2) {
		 exc2.printStackTrace();
		 throw new ThriftDataAccessException(exc2.getMessage());
	}
}

double[] ListOfDoublesToPrimitiveArrayOfdoubles(List<Double> arrayListOfDoubles){
	ListIterator<Double> listIter = arrayListOfDoubles.listIterator();
	double[] doubles = new double[arrayListOfDoubles.size()];
	int i = 0;
	while (listIter.hasNext()){
		doubles[i] = listIter.next().doubleValue();
	}
	return doubles;
}

}