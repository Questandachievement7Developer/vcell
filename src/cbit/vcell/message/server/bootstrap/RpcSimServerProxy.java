/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.message.server.bootstrap;
import org.vcell.util.DataAccessException;
import org.vcell.util.SessionLog;

import cbit.vcell.message.VCMessageSession;
import cbit.vcell.message.VCellQueue;
import cbit.vcell.message.server.ServiceSpec.ServiceType;
import cbit.vcell.server.UserLoginInfo;
import cbit.vcell.solver.VCSimulationIdentifier;

/**
 * Insert the type's description here.
 * Creation date: (12/5/2001 12:00:10 PM)
 * @author: Jim Schaff
 *
 * stateless database service for any user (should be thread safe ... reentrant)
 *
 */
public class RpcSimServerProxy extends AbstractRpcServerProxy implements cbit.vcell.server.SimulationController {
/**
 * DataServerProxy constructor comment.
 */
public RpcSimServerProxy(UserLoginInfo userLoginInfo, VCMessageSession vcMessageSession, SessionLog log) {
	super(userLoginInfo, vcMessageSession, VCellQueue.SimReqQueue, log);
}


/**
 * Insert the method's description here.
 * Creation date: (12/5/2001 9:39:03 PM)
 * @return java.lang.Object
 * @param methodName java.lang.String
 * @param args java.lang.Object[]
 * @exception java.lang.Exception The exception description.
 */
private Object rpc(String methodName, Object[] args) throws DataAccessException {
	try {
		return rpc(ServiceType.DISPATCH, methodName, args, true);
	} catch (DataAccessException ex) {
		log.exception(ex);
		throw ex;
	} catch (Exception e){
		log.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * Insert the method's description here.
 * Creation date: (12/5/2001 9:39:03 PM)
 * @return java.lang.Object
 * @param methodName java.lang.String
 * @param args java.lang.Object[]
 * @exception java.lang.Exception The exception description.
 */
private void rpcNoWait(String methodName, Object[] args) throws DataAccessException {
	try {
		rpc(ServiceType.DISPATCH, methodName, args, false);
	} catch (DataAccessException ex) {
		log.exception(ex);
		throw ex;
	} catch (Exception e){
		log.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @exception java.rmi.RemoteException The exception description.
 */
public void startSimulation(VCSimulationIdentifier vcSimID, int numSimulationScanJobs) {
	try {
		rpcNoWait("startSimulation",new Object[]{vcSimID, new Integer(numSimulationScanJobs)});
	}catch (DataAccessException e){
		log.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}


/**
 * This method was created by a SmartGuide.
 * @exception java.rmi.RemoteException The exception description.
 */
public void stopSimulation(VCSimulationIdentifier vcSimID) {
	try {
		rpcNoWait("stopSimulation",new Object[]{vcSimID});
	} catch (DataAccessException e) {
		log.exception(e);
		throw new RuntimeException(e.getMessage());
	}
}
}
