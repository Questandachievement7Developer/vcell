package cbit.vcell.simdata;
import cbit.rmi.event.*;
import cbit.vcell.solver.SimulationInfo;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import cbit.vcell.export.server.*;
import cbit.vcell.math.*;
import cbit.plot.*;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import cbit.vcell.solvers.CartesianMesh;
import cbit.vcell.server.*;
/**
 * This interface was generated by a SmartGuide.
 * 
 */
public class LocalDataSetController extends UnicastRemoteObject implements DataSetController {
	private LocalVCellConnection vcConn = null;
	private SessionLog log = null;
	private User user = null;
	private DataServerImpl dataServerImpl = null;

/**
 * This method was created by a SmartGuide.
 */
public LocalDataSetController (LocalVCellConnection argvcConn, SessionLog log, DataSetControllerImpl dsControllerImpl, ExportServiceImpl exportServiceImpl, User user) throws RemoteException {
	super(PropertyLoader.getIntProperty(PropertyLoader.rmiPortDataSetController,0));
	this.vcConn = argvcConn;
	this.user = user;
	this.log = log;
	dataServerImpl = new DataServerImpl(log, dsControllerImpl, exportServiceImpl);	
}


/**
 * Insert the method's description here.
 * Creation date: (10/11/00 1:11:04 PM)
 * @param function cbit.vcell.math.Function
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public void addFunction(VCDataIdentifier vcdID, AnnotatedFunction function) throws cbit.vcell.server.DataAccessException {
	log.print("LocalDataSetController.addFunction("+function+"="+function.getExpression()+")");
	dataServerImpl.addFunction(user, vcdID, function);
}


/**
 * Insert the method's description here.
 * Creation date: (10/11/00 1:11:04 PM)
 * @param function cbit.vcell.math.Function[]
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public void addFunctions(VCDataIdentifier vcdID, AnnotatedFunction[] functions) throws cbit.vcell.server.DataAccessException {
	dataServerImpl.addFunctions(user, vcdID, functions);
}


/**
 * This method was created by a SmartGuide.
 * @return java.lang.String[]
 */
public DataIdentifier[] getDataIdentifiers(VCDataIdentifier vcdID) throws DataAccessException {
	return dataServerImpl.getDataIdentifiers(user, vcdID);
}


/**
 * This method was created by a SmartGuide.
 * @return double[]
 */
public double[] getDataSetTimes(VCDataIdentifier vcdID) throws DataAccessException {
	return dataServerImpl.getDataSetTimes(user, vcdID);
}


/**
 * Insert the method's description here.
 * Creation date: (10/11/00 1:11:04 PM)
 * @param function cbit.vcell.math.Function
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public AnnotatedFunction[] getFunctions(VCDataIdentifier vcdID) throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException {
	return dataServerImpl.getFunctions(user, vcdID);
}


/**
 * This method was created in VisualAge.
 * @return boolean
 */
public boolean getIsODEData(VCDataIdentifier vcdID) throws DataAccessException {
	return dataServerImpl.getIsODEData(user, vcdID);
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.plot.PlotData
 * @param varName java.lang.String
 * @param begin cbit.vcell.math.CoordinateIndex
 * @param end cbit.vcell.math.CoordinateIndex
 */
public PlotData getLineScan(VCDataIdentifier vcdID, String varName, double time, CoordinateIndex begin, CoordinateIndex end) throws DataAccessException {
	return dataServerImpl.getLineScan(user, vcdID,varName,time,begin,end);
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.plot.PlotData
 * @param variable java.lang.String
 * @param time double
 * @param spatialSelection cbit.vcell.simdata.gui.SpatialSelection
 * @exception java.rmi.RemoteException The exception description.
 */
public cbit.plot.PlotData getLineScan(VCDataIdentifier vcdID, java.lang.String varName, double time, SpatialSelection spatialSelection) throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException {
	return dataServerImpl.getLineScan(user, vcdID,varName,time,spatialSelection);
}


/**
 * This method was created in VisualAge.
 * @return CartesianMesh
 */
public CartesianMesh getMesh(VCDataIdentifier vcdID) throws DataAccessException {
	return dataServerImpl.getMesh(user, vcdID);
}


/**
 * Insert the method's description here.
 * Creation date: (1/14/00 11:20:51 AM)
 * @return cbit.vcell.export.data.ODESimData
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public cbit.vcell.solver.ode.ODESimData getODEData(VCDataIdentifier vcdID) throws DataAccessException, RemoteException {
	return dataServerImpl.getODEData(user, vcdID);
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.server.DataSet
 * @param time double
 */
public ParticleDataBlock getParticleDataBlock(VCDataIdentifier vcdID, double time) throws DataAccessException {
	return dataServerImpl.getParticleDataBlock(user, vcdID, time);
}


/**
 * This method was created in VisualAge.
 * @return boolean
 */
public boolean getParticleDataExists(VCDataIdentifier vcdID) throws DataAccessException {
	return dataServerImpl.getParticleDataExists(user, vcdID);
}


/**
 * This method was created by a SmartGuide.
 * @return cbit.vcell.server.DataSet
 * @param time double
 */
public SimDataBlock getSimDataBlock(VCDataIdentifier vcdID, String var, double time) throws DataAccessException {
	return dataServerImpl.getSimDataBlock(user, vcdID,var,time);
}


/**
 * This method was created by a SmartGuide.
 * @return double[]
 * @param varName java.lang.String
 * @param x int
 * @param y int
 * @param z int
 */
public cbit.util.TimeSeriesJobResults getTimeSeriesValues(VCDataIdentifier vcdID,cbit.util.TimeSeriesJobSpec timeSeriesJobSpec) throws DataAccessException {
	return dataServerImpl.getTimeSeriesValues(user,vcdID,timeSeriesJobSpec);
}


/**
 * Insert the method's description here.
 * Creation date: (3/30/2001 11:11:52 AM)
 * @param exportSpecs cbit.vcell.export.server.ExportSpecs
 * @exception cbit.vcell.server.DataAccessException The exception description.
 */
public ExportEvent makeRemoteFile(cbit.vcell.export.server.ExportSpecs exportSpecs) throws cbit.vcell.server.DataAccessException {
	return dataServerImpl.makeRemoteFile(user, exportSpecs);

	/*
	log.print("LocalDataSetController.makeRemoteFile(" + exportSpecs.getVCDataIdentifier() + ")");
	try {
		ExportEvent exportEvent = exportServiceImpl.makeRemoteFile(getUser(), this, exportSpecs);
		if (exportEvent != null && exportEvent.getEventTypeID() == MessageEvent.EXPORT_COMPLETE) {
			// updates database with export metadata
			if (exportSpecs.getVCDataIdentifier() instanceof SimulationInfo){
				vcConn.getResultSetCrawler().updateExportData(user, (SimulationInfo)(exportSpecs.getVCDataIdentifier()), exportEvent);
			}else{
				log.alert("LocalDataSetController.makeRemoteFile(), invoked for data '"+exportSpecs.getVCDataIdentifier()+"' not indexed using ResultSetCrawler");
			}
		}
		return exportEvent;
	} catch (Throwable e) {
		log.exception(e);
		throw new DataAccessException(e.getMessage());
	}*/
}


/**
 * Insert the method's description here.
 * Creation date: (10/11/00 1:11:04 PM)
 * @param function cbit.vcell.math.Function
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public void removeFunction(VCDataIdentifier vcdID, AnnotatedFunction function) throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException {
	dataServerImpl.removeFunction(user, vcdID,function);
}
}