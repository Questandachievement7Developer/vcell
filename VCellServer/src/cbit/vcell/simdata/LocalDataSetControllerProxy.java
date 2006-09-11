package cbit.vcell.simdata;

import cbit.rmi.event.*;
/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import cbit.util.DataAccessException;
import cbit.util.SessionLog;
import cbit.util.VCDataIdentifier;
import cbit.vcell.export.server.*;
import cbit.vcell.math.*;
import cbit.gui.PropertyLoader;
import cbit.plot.*;
import java.rmi.*;
import java.rmi.server.*;

import cbit.vcell.solver.CartesianMesh;
import cbit.vcell.server.*;
/**
 * This interface was generated by a SmartGuide.
 * 
 */
public class LocalDataSetControllerProxy extends UnicastRemoteObject implements DataSetController {
	private LocalDataSetController localDataSetController = null;
	private RemoteDataSetControllerFactory remoteDataSetControllerFactory = null;
	private DataSetController remoteDataSetController = null;
	private SessionLog sessionLog = null;
/**
 * This method was created by a SmartGuide.
 */
public LocalDataSetControllerProxy (SessionLog sessionLog, 
									RemoteDataSetControllerFactory argRemoteDataSetControllerFactory, 
									LocalDataSetController argLocalDataSetController)
									throws RemoteException {
	super(PropertyLoader.getIntProperty(PropertyLoader.rmiPortDataSetController,0));
	this.localDataSetController = argLocalDataSetController;
	this.remoteDataSetControllerFactory = argRemoteDataSetControllerFactory;
	this.sessionLog = sessionLog;
}
/**
 * Insert the method's description here.
 * Creation date: (10/11/00 1:11:04 PM)
 * @param function cbit.vcell.math.Function
 * @exception cbit.util.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public void addFunction(VCDataIdentifier vcdID, AnnotatedFunction function) throws cbit.util.DataAccessException, java.rmi.RemoteException {
	sessionLog.print("LocalDataSetControllerProxy.addFunction(simID="+vcdID.getID()+", function="+function.toString()+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				rdsc.addFunction(vcdID, function);
				return;
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		getLocalDataSetController().addFunction(vcdID, function);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * Insert the method's description here.
 * Creation date: (10/11/00 1:11:04 PM)
 * @param function cbit.vcell.math.Function
 * @exception cbit.util.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public void addFunctions(VCDataIdentifier vcdID, AnnotatedFunction[] functions) throws cbit.util.DataAccessException, java.rmi.RemoteException {
	sessionLog.print("LocalDataSetControllerProxy.addFunctions(simID="+vcdID.getID()+", functions="+functions+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				rdsc.addFunctions(vcdID, functions);
				return;
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		getLocalDataSetController().addFunctions(vcdID, functions);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * This method was created by a SmartGuide.
 * @return java.lang.String[]
 */
public DataIdentifier[] getDataIdentifiers(VCDataIdentifier vcdID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerProxy.getDataIdentifiers(simID="+vcdID.getID()+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getDataIdentifiers(vcdID);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getDataIdentifiers(vcdID);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * This method was created by a SmartGuide.
 * @return double[]
 */
public double[] getDataSetTimes(VCDataIdentifier vcdID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerProxy.getDataSetTimes(simID="+vcdID.getID()+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getDataSetTimes(vcdID);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getDataSetTimes(vcdID);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * Insert the method's description here.
 * Creation date: (10/11/00 1:11:04 PM)
 * @param function cbit.vcell.math.Function
 * @exception cbit.util.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public AnnotatedFunction[] getFunctions(VCDataIdentifier vcdID) throws cbit.util.DataAccessException, java.rmi.RemoteException {
	sessionLog.print("LocalDataSetControllerProxy.getFunctions(simID="+vcdID.getID()+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getFunctions(vcdID);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getFunctions(vcdID);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * This method was created by a SmartGuide.
 * @return boolean
 */
public boolean getIsODEData(VCDataIdentifier vcdID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerProxy.getIsODEData(simID="+vcdID.getID()+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getIsODEData(vcdID);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getIsODEData(vcdID);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * This method was created by a SmartGuide.
 * @return cbit.plot.PlotData
 * @param varName java.lang.String
 * @param begin cbit.vcell.math.CoordinateIndex
 * @param end cbit.vcell.math.CoordinateIndex
 */
public PlotData getLineScan(VCDataIdentifier vcdID, String varName, double time, CoordinateIndex begin, CoordinateIndex end) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerProxy.getLineScan(simID="+vcdID.getID()+", "+varName+", "+time+", from="+begin+" to "+end+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getLineScan(vcdID, varName, time, begin, end);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getLineScan(vcdID, varName, time, begin, end);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * This method was created by a SmartGuide.
 * @return cbit.plot.PlotData
 * @param varName java.lang.String
 * @param spatialSelection cbit.vcell.simdata.gui.SpatialSelection
 */
public PlotData getLineScan(VCDataIdentifier vcdID, String varName, double time, SpatialSelection spatialSelection) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerProxy.getLineScan(simID="+vcdID.getID()+", "+varName+", "+time+", at "+spatialSelection+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getLineScan(vcdID, varName, time, spatialSelection);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getLineScan(vcdID, varName, time, spatialSelection);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * Insert the method's description here.
 * Creation date: (10/22/01 11:21:33 AM)
 * @return cbit.vcell.server.DataSetController
 * @param user cbit.vcell.server.User
 */
private LocalDataSetController getLocalDataSetController() {
	return localDataSetController;
}
/**
 * This method was created by a SmartGuide.
 * @return int[]
 */
public CartesianMesh getMesh(VCDataIdentifier vcdID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerProxy.getMesh(simID="+vcdID.getID()+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getMesh(vcdID);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getMesh(vcdID);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * Insert the method's description here.
 * Creation date: (1/14/00 11:20:51 AM)
 * @return cbit.vcell.export.data.ODESimData
 * @exception cbit.util.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public cbit.vcell.solver.ode.ODESimData getODEData(VCDataIdentifier vcdID) throws DataAccessException, RemoteException {
	sessionLog.print("LocalDataSetControllerProxy.getODEData(simID="+vcdID.getID()+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getODEData(vcdID);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getODEData(vcdID);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * This method was created by a SmartGuide.
 * @return double[]
 * @param varName java.lang.String
 * @param time double
 */
public ParticleDataBlock getParticleDataBlock(VCDataIdentifier vcdID, double time) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerProxy.getParticleDataBlock(simID="+vcdID.getID()+",time="+time+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getParticleDataBlock(vcdID,time);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getParticleDataBlock(vcdID,time);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * This method was created by a SmartGuide.
 * @return boolean
 */
public boolean getParticleDataExists(VCDataIdentifier vcdID) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerProxy.getParticleDataExists(simID="+vcdID.getID()+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getParticleDataExists(vcdID);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getParticleDataExists(vcdID);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * Insert the method's description here.
 * Creation date: (10/22/01 11:21:33 AM)
 * @return cbit.vcell.server.DataSetController
 * @param user cbit.vcell.server.User
 */
private DataSetController getRemoteDataSetController() {
	//
	// slave data server should have a null remote factory
	//
	if (remoteDataSetControllerFactory == null){
		return null;
	}
	if (remoteDataSetController==null){
		try {
			remoteDataSetController = remoteDataSetControllerFactory.getRemoteDataSetController();
		}catch (Throwable e){
			sessionLog.exception(e);
		}
	}
	return remoteDataSetController;
}
/**
 * This method was created by a SmartGuide.
 * @return double[]
 * @param varName java.lang.String
 * @param time double
 */
public SimDataBlock getSimDataBlock(VCDataIdentifier vcdID, String varName, double time) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerProxy.getSimDataBlock(simID="+vcdID.getID()+", varName="+varName+", time="+time+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getSimDataBlock(vcdID,varName,time);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getSimDataBlock(vcdID,varName,time);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * This method was created by a SmartGuide.
 * @return double[]
 * @param varName java.lang.String
 * @param index int
 */
public cbit.util.TimeSeriesJobResults getTimeSeriesValues(VCDataIdentifier vcdID,cbit.util.TimeSeriesJobSpec timeSeriesJobSpec) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerProxy.getTimeSeriesValues(simID="+vcdID.getID()+", "+timeSeriesJobSpec+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.getTimeSeriesValues(vcdID,timeSeriesJobSpec);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().getTimeSeriesValues(vcdID,timeSeriesJobSpec);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * Insert the method's description here.
 * Creation date: (10/22/01 11:38:12 AM)
 */
private void invalidateRemoteDataSetController() {
	this.remoteDataSetController = null;
}
/**
 * This method was created in VisualAge.
 * @param simInfo cbit.vcell.solver.SimulationInfo
 * @exception cbit.util.DataAccessException The exception description.
 */
public ExportEvent makeRemoteFile(ExportSpecs exportSpecs) throws DataAccessException {
	sessionLog.print("LocalDataSetControllerProxy.makeRemoteFile(simID="+exportSpecs.getVCDataIdentifier().getID()+","+exportSpecs+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				return rdsc.makeRemoteFile(exportSpecs);
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		return getLocalDataSetController().makeRemoteFile(exportSpecs);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}
}
/**
 * Insert the method's description here.
 * Creation date: (10/11/00 1:11:04 PM)
 * @param function cbit.vcell.math.Function
 * @exception cbit.util.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
public void removeFunction(VCDataIdentifier vcdID, AnnotatedFunction function) throws cbit.util.DataAccessException, java.rmi.RemoteException {
	sessionLog.print("LocalDataSetControllerProxy.removeFunction(simID="+vcdID.getID()+", function="+function+")");
	try {
		//
		// try once with remote reference (if it exists)
		// if it fails with a RemoteException, invalidate the remote reference and try local
		//
		DataSetController rdsc = getRemoteDataSetController();
		if (rdsc!=null){
			try {
				rdsc.removeFunction(vcdID, function);
				return;
			}catch (RemoteException e){
				sessionLog.exception(e);
				invalidateRemoteDataSetController();
			}
		}
		getLocalDataSetController().removeFunction(vcdID, function);
	}catch (Throwable e){
		sessionLog.exception(e);
		throw new DataAccessException(e.getMessage());
	}

}
}
