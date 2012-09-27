package cbit.vcell.mongodb;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

import org.vcell.util.document.VCellServerID;

import cbit.htc.PbsJobID;
import cbit.rmi.event.MessageEvent;
import cbit.rmi.event.SimulationJobStatusEvent;
import cbit.rmi.event.WorkerEvent;
import cbit.vcell.messaging.WorkerEventMessage;
import cbit.vcell.messaging.db.SimulationExecutionStatus;
import cbit.vcell.messaging.db.SimulationJobStatus;
import cbit.vcell.messaging.db.SimulationQueueEntryStatus;
import cbit.vcell.messaging.server.RpcRequest;
import cbit.vcell.messaging.server.SimulationTask;
import cbit.vcell.server.UserLoginInfo;
import cbit.vcell.solver.SimulationJob;
import cbit.vcell.solver.SimulationMessage;
import cbit.vcell.solver.SimulationMessage.DetailedState;
import cbit.vcell.solver.SolverEvent;
import cbit.vcell.solver.VCSimulationDataIdentifier;
import cbit.vcell.solver.VCSimulationDataIdentifierOldStyle;
import cbit.vcell.solver.VCSimulationIdentifier;
import cbit.vcell.solvers.AbstractSolver;

import com.mongodb.BasicDBObject;

public final class VCMongoMessage {
	public static boolean enabled = true;
	private static boolean processingException = false;
	
	public static enum ServiceName {
		unknown,
		client,
		pbsWorker,
		localWorker,
		dispatch,
		bootstrap,
		simData,
		export,
		database,
		serverManager
	};
	
	private static ServiceName serviceName = ServiceName.unknown;
	private static Integer serviceOrdinal = null;
	private static String[] serviceArgs = new String[0];
	private static long serviceStartupTime = System.currentTimeMillis();
	
	public final static String MongoMessage_msgtype				= "msgtype";
	public static final String MongoMessage_type_testing 								= "testing";	
	public final static String MongoMessage_msgtype_simJobStatusUpdate					= "simJobStatusUpdate";
	public final static String MongoMessage_msgtype_simJobStatusUpdate_DBCacheMiss		= "simJobStatusUpdate_DBCacheMiss";
	public final static String MongoMessage_msgtype_simJobStatusInsert					= "simJobStatusInsert";
	public final static String MongoMessage_msgtype_simJobStatusInsert_AlreadyInserted	= "simJobStatusInsert_AlreadyInserted";
	public final static String MongoMessage_msgtype_solverStatus						= "solverStatus";
	public final static String MongoMessage_msgtype_workerEventMessage					= "workerEventMessage";
	public final static String MongoMessage_msgtype_rpcRequestSent						= "rpcRequestSent";
	public final static String MongoMessage_msgtype_rpcRequestReceived					= "rpcRequestReceived";
	public final static String MongoMessage_msgtype_clientSimStatusQueued				= "clientSimStatusQueued";
	public final static String MongoMessage_msgtype_clientSimStatusDelivered			= "clientSimStatusDelivered";
	public final static String MongoMessage_msgtype_serviceStartup						= "serviceStartup";
	public final static String MongoMessage_msgtype_exception							= "exception";
	public final static String MongoMessage_msgtype_clientConnect						= "clientConnect";
	public final static String MongoMessage_msgtype_clientTimeout						= "clientTimeout";
	public final static String MongoMessage_msgTime				= "msgTime";
	public final static String MongoMessage_msgTimeNice			= "msgTimeNice";
	
	//
	// SimulationJobStatus
	//
	public final static String MongoMessage_oldSimJobStatus		= "oldSimJobStatus";
	public final static String MongoMessage_cachedSimJobStatus	= "cachedSimJobStatus";
	public final static String MongoMessage_newSimJobStatus		= "newSimJobStatus";
	public final static String MongoMessage_updatedSimJobStatus	= "updatedSimJobStatus";
	
	public final static String MongoMessage_simId				= "simId";
	public final static String MongoMessage_taskId				= "taskId";
	public final static String MongoMessage_endTime				= "endDate";
	public final static String MongoMessage_endTimeNice			= "endDateNice";
	public final static String MongoMessage_hasData				= "hasData";
	public final static String MongoMessage_jobIndex			= "jobIndex";
	public final static String MongoMessage_schedulerStatus		= "schedulerStatus";
	public final static String MongoMessage_serverId			= "serverId";
	public final static String MongoMessage_computeHost			= "computeHost";
	public final static String MongoMessage_startTime			= "startDate";
	public final static String MongoMessage_startTimeNice		= "startDateNice";
	public final static String MongoMessage_simTime				= "simTime";
	public final static String MongoMessage_simTimeNice			= "simTimeNice";
	public final static String MongoMessage_simProgress			= "simProgress";
	public final static String MongoMessage_latestUpdateTime	= "updateDate";
	public final static String MongoMessage_latestUpdateTimeNice	= "updateDateNice";
	public final static String MongoMessage_simMessageState		= "simMessageState";
	public final static String MongoMessage_simMessageMsg		= "simMessageMsg";
	public final static String MongoMessage_simQueueEntryDate	= "simQueueEntryDate";
	public final static String MongoMessage_simQueueEntryDateNice	= "simQueueEntryDateNice";
	public final static String MongoMessage_simQueueEntryId		= "simQueueEntryId";
	public final static String MongoMessage_simQueueEntryPriority	= "simQueueEntryPriority";
	public final static String MongoMessage_simJobStatusTimeStamp	= "simJobStatusTimeStamp";
	public final static String MongoMessage_simJobStatusTimeStampNice	= "simJobStatusTimeStampNice";
	public final static String MongoMessage_solverEventType		= "solverEventType";
	public final static String MongoMessage_simComputeResource	= "simComputeResource";
	public final static String MongoMessage_simEstMemory		= "simEstMemory";
	public final static String MongoMessage_pbsJobID			= "pbsJobID";
	public final static String MongoMessage_pbsWorkerMsg		= "pbsWorkerMsg";
	public final static String MongoMessage_rpcRequestMethod	= "rpcMethod";
	public final static String MongoMessage_rpcRequestArgs		= "rpcArgs";
	public final static String MongoMessage_rpcRequestService	= "rpcService";
	public final static String MongoMessage_userName			= "user";
	public final static String MongoMessage_host				= "host";
	public final static String MongoMessage_serviceName			= "serviceName";
	public final static String MongoMessage_serviceOrdinal		= "serviceOrdinal";
	public final static String MongoMessage_serviceStartTime	= "serviceStartTime";
	public final static String MongoMessage_serviceStartTimeNice	= "serviceStartTimeNice";
	public final static String MongoMessage_serviceArgs			= "serviceArgs";
	public final static String MongoMessage_exceptionMessage	= "exceptionMessage";
	public final static String MongoMessage_exceptionStack		= "exceptionStack";
	public final static String MongoMessage_clientInfo			= "clientInfo";
	public final static String MongoMessage_clientId			= "clientId";
	public final static String MongoMessage_javaVersion			= "javaVersion";
	public final static String MongoMessage_osArch				= "osArch";
	public final static String MongoMessage_osName				= "osName";
	public final static String MongoMessage_osVersion			= "osVersion";
	public final static String MongoMessage_vcSoftwareVersion	= "vcSoftwareVersion";

	private BasicDBObject doc = null;
	
	VCMongoMessage(BasicDBObject doc){
		this.doc = doc;
	}
	
	public BasicDBObject getDbObject(){
		return doc;
	}
	
	public String toString() {
		return doc.toString();
	}
	
	public static void serviceStartup(ServiceName serviceName, Integer serviceOrdinal, String[] args){
		VCMongoMessage.serviceName = serviceName;
		VCMongoMessage.serviceOrdinal = serviceOrdinal;
		VCMongoMessage.serviceArgs = args;
		VCMongoMessage.serviceStartupTime = System.currentTimeMillis();
		
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();
	
			addHeader(dbObject,MongoMessage_msgtype_serviceStartup);
			
			if (serviceOrdinal!=null){
				dbObject.put(MongoMessage_serviceOrdinal, serviceOrdinal.intValue());
			}

			if (args!=null){
				dbObject.put(MongoMessage_serviceArgs, Arrays.asList(args).toString());
			}
	
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e);
		}
	}
	
	public static ServiceName getServiceName() {
		return VCMongoMessage.serviceName;
	}

	public static long getServiceStartupTime() {
		return VCMongoMessage.serviceStartupTime;
	}
	
	private static void addHeader(BasicDBObject dbObject, String messageType) throws UnknownHostException{
		dbObject.put(MongoMessage_serverId, org.vcell.util.PropertyLoader.getProperty(org.vcell.util.PropertyLoader.vcellServerIDProperty,"unknown"));
		dbObject.put(MongoMessage_serviceName,serviceName.name());
		dbObject.put(MongoMessage_serviceOrdinal,serviceOrdinal);
		dbObject.put(MongoMessage_serviceStartTime,serviceStartupTime);
		dbObject.put(MongoMessage_serviceStartTimeNice,new Date(serviceStartupTime).toString());
		dbObject.put(MongoMessage_msgtype,messageType);
		
		long msgTime = System.currentTimeMillis();
		dbObject.put(MongoMessage_msgTime, msgTime);
		dbObject.put(MongoMessage_msgTimeNice, new Date(msgTime).toString());
		
		dbObject.put(MongoMessage_host,java.net.InetAddress.getLocalHost().getHostName());
	}

	public static void sendClientConnectionNew(UserLoginInfo userLoginInfo) {
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();
	
			addHeader(dbObject,MongoMessage_msgtype_clientConnect);
			
			addObject(dbObject, userLoginInfo);
							
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e); 
		}
	}

	public static void sendClientConnectionClosing(UserLoginInfo userLoginInfo) {
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();
	
			addHeader(dbObject,MongoMessage_msgtype_clientTimeout);
			
			addObject(dbObject, userLoginInfo);
							
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e); 
		}
	}

	public static void sendClientException(Throwable exception,	UserLoginInfo userLoginInfo) {
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();
	
			addHeader(dbObject,MongoMessage_msgtype_exception);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PrintWriter pw = new PrintWriter(bos);
			exception.printStackTrace(pw);
			pw.close();
			String stack = bos.toString();

			dbObject.put(MongoMessage_exceptionMessage,exception.getMessage());
			dbObject.put(MongoMessage_exceptionStack,stack);
			
			addObject(dbObject, userLoginInfo);
							
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e); 
		}
	}

	public static void sendException(String stack, String message) {
		if (!enabled){
			return;
		}
		if (processingException){
			System.out.println("recursively invoking sendException() for " +message +", " + stack + ", exception not logged to MongoDB.");
			return;
		}
		try {
			processingException = true;
			BasicDBObject dbObject = new BasicDBObject();
	
			addHeader(dbObject,MongoMessage_msgtype_exception);
			
			dbObject.put(MongoMessage_exceptionMessage,message);
			dbObject.put(MongoMessage_exceptionStack,stack);
	
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			e.printStackTrace(System.out);
			// cannot put exception to SessionLog ... infinite recursion.
			// VCMongoDbDriver.getInstance().getSessionLog().exception(e); 
			//
		} finally {
			processingException = false;
		}
	}

	public static void sendSimJobStatusInsertedAlready(SimulationJobStatus newSimulationJobStatus,SimulationJobStatus existingSimulationJobStatus) {
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();
	
			addHeader(dbObject,MongoMessage_msgtype_simJobStatusInsert_AlreadyInserted);
			
			addObject(dbObject,newSimulationJobStatus);
			
			BasicDBObject oldSimJobStatusObject = new BasicDBObject();
			addObject(oldSimJobStatusObject, existingSimulationJobStatus);
			dbObject.put(MongoMessage_oldSimJobStatus, oldSimJobStatusObject);

			BasicDBObject newSimJobStatusObject = new BasicDBObject();
			addObject(newSimJobStatusObject, newSimulationJobStatus);
			dbObject.put(MongoMessage_newSimJobStatus, newSimJobStatusObject);
	
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e);
		}
	}

	public static void sendSimJobStatusInsert(SimulationJobStatus newSimulationJobStatus,SimulationJobStatus updatedSimulationJobStatus) {
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();

			addHeader(dbObject,MongoMessage_msgtype_simJobStatusInsert);

			addObject(dbObject,updatedSimulationJobStatus);
			
			BasicDBObject newSimJobStatusObject = new BasicDBObject();
			addObject(newSimJobStatusObject, newSimulationJobStatus);
			dbObject.put(MongoMessage_newSimJobStatus, newSimJobStatusObject);
	
			BasicDBObject updatedSimJobStatusObject = new BasicDBObject();
			addObject(updatedSimJobStatusObject, updatedSimulationJobStatus);
			dbObject.put(MongoMessage_updatedSimJobStatus, updatedSimJobStatusObject);
			
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e);
		}
	}

	public static void sendSimJobStatusUpdateCacheMiss(SimulationJobStatus cachedSimulationJobStatus, SimulationJobStatus oldSimulationJobStatus, SimulationJobStatus newSimulationJobStatus) {
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();

			addHeader(dbObject,MongoMessage_msgtype_simJobStatusUpdate_DBCacheMiss);

			addObject(dbObject,newSimulationJobStatus);
			
			BasicDBObject cachedSimJobStatusObject = new BasicDBObject();
			addObject(cachedSimJobStatusObject, cachedSimulationJobStatus);
			dbObject.put(MongoMessage_cachedSimJobStatus, cachedSimJobStatusObject);
			
			BasicDBObject oldSimJobStatusObject = new BasicDBObject();
			addObject(oldSimJobStatusObject, oldSimulationJobStatus);
			dbObject.put(MongoMessage_oldSimJobStatus, oldSimJobStatusObject);
	
			BasicDBObject newSimJobStatusObject = new BasicDBObject();
			addObject(newSimJobStatusObject, newSimulationJobStatus);
			dbObject.put(MongoMessage_newSimJobStatus, newSimJobStatusObject);
	
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e);
		}
	}
	
	public static void sendSimJobStatusUpdate(SimulationJobStatus oldSimulationJobStatus, SimulationJobStatus newSimulationJobStatus, SimulationJobStatus updatedSimulationJobStatus) {
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();

			addHeader(dbObject,MongoMessage_msgtype_simJobStatusUpdate);

			addObject(dbObject,updatedSimulationJobStatus);
	
			BasicDBObject oldSimJobStatusObject = new BasicDBObject();
			addObject(oldSimJobStatusObject, oldSimulationJobStatus);
			dbObject.put(MongoMessage_oldSimJobStatus, oldSimJobStatusObject);
	
			BasicDBObject newSimJobStatusObject = new BasicDBObject();
			addObject(newSimJobStatusObject, newSimulationJobStatus);
			dbObject.put(MongoMessage_newSimJobStatus, newSimJobStatusObject);
	
			BasicDBObject updatedSimJobStatusObject = new BasicDBObject();
			addObject(updatedSimJobStatusObject, updatedSimulationJobStatus);
			dbObject.put(MongoMessage_updatedSimJobStatus, updatedSimJobStatusObject);
			
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e);
		}
	}
		
	
	public static void sendSolverEvent(SolverEvent solverEvent) {
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();

			addHeader(dbObject,MongoMessage_msgtype_solverStatus);

			addObject(dbObject,solverEvent);
				
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e);
		}
	}

	public static void sendWorkerEvent(WorkerEventMessage workerEventMessage) {
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();

			addHeader(dbObject,MongoMessage_msgtype_workerEventMessage);

			addObject(dbObject,workerEventMessage);
				
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e);
		}
	}

	public static void sendPBSWorkerMessage(SimulationTask simulationTask, PbsJobID pbsJobID, String pbsWorkerMsg) {
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();

			addHeader(dbObject,MongoMessage_msgtype_workerEventMessage);

			dbObject.put(MongoMessage_pbsWorkerMsg, pbsWorkerMsg);
			if (pbsJobID!=null){
				dbObject.put(MongoMessage_pbsJobID, pbsJobID.getID());
			}
	
			addObject(dbObject,simulationTask);
			
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e);
		}
	}


	public static void sendRpcRequestReceived(RpcRequest rpcRequest) {
		if (!enabled){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();

			addHeader(dbObject,MongoMessage_msgtype_rpcRequestReceived);

			addObject(dbObject,rpcRequest);
			
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e);
		}
	}

	public static void sendRpcRequestSent(RpcRequest rpcRequest, UserLoginInfo userLoginInfo) {
		if (!enabled){
			return;
		}
		try {
			
			BasicDBObject dbObject = new BasicDBObject();

			addHeader(dbObject,MongoMessage_msgtype_rpcRequestSent);

			addObject(dbObject,rpcRequest);
			
			addObject(dbObject,userLoginInfo);
			
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e);
		}
	}

	public static void sendClientMessageEventQueued(MessageEvent messageEvent) {
		if (!enabled || !(messageEvent instanceof SimulationJobStatusEvent)){
			return;
		}
		try {
			BasicDBObject dbObject = new BasicDBObject();

			addHeader(dbObject,MongoMessage_msgtype_clientSimStatusQueued);

			addObject(dbObject,(SimulationJobStatusEvent)messageEvent);
			
			VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
		} catch (Exception e){
			VCMongoDbDriver.getInstance().getSessionLog().exception(e);
		}
	}

	public static void sendClientMessageEventsDelivered(MessageEvent[] messageEvents, UserLoginInfo userLoginInfo) {
		if (!enabled || messageEvents==null){
			return;
		}
		for (MessageEvent messageEvent : messageEvents){
			if (messageEvent instanceof SimulationJobStatusEvent){
				try {
					BasicDBObject dbObject = new BasicDBObject();

					addHeader(dbObject,MongoMessage_msgtype_clientSimStatusDelivered);

					addObject(dbObject,(SimulationJobStatusEvent)messageEvent);
					
					addObject(dbObject, userLoginInfo);
					
					VCMongoDbDriver.getInstance().addMessage(new VCMongoMessage(dbObject));
				} catch (Exception e){
					VCMongoDbDriver.getInstance().getSessionLog().exception(e);
				}
			}
		}
	}

	private static void addObject(BasicDBObject dbObject, UserLoginInfo userLoginInfo){
		if (userLoginInfo == null){
			return;
		}
		if (userLoginInfo.getUser()!=null){
			dbObject.put(MongoMessage_userName, userLoginInfo.getUser().getName());
		}
		dbObject.put(MongoMessage_clientId, userLoginInfo.getClientId());
		
		BasicDBObject dbObjectClientInfo = new BasicDBObject();

		if (userLoginInfo.getUser()!=null){
			dbObjectClientInfo.put(MongoMessage_userName,userLoginInfo.getUser().getName());
		}
		dbObjectClientInfo.put(MongoMessage_clientId, userLoginInfo.getClientId());
		if (userLoginInfo.getJava_version()!=null){
			dbObjectClientInfo.put(MongoMessage_javaVersion, userLoginInfo.getJava_version());
		}			
		if (userLoginInfo.getOs_arch()!=null){
			dbObjectClientInfo.put(MongoMessage_osArch, userLoginInfo.getOs_arch());
		}			
		if (userLoginInfo.getOs_name()!=null){
			dbObjectClientInfo.put(MongoMessage_osName, userLoginInfo.getOs_name());
		}			
		if (userLoginInfo.getOs_version()!=null){
			dbObjectClientInfo.put(MongoMessage_osVersion, userLoginInfo.getOs_version());
		}			
		if (userLoginInfo.getVCellSoftwareVersion()!=null){
			dbObjectClientInfo.put(MongoMessage_vcSoftwareVersion, userLoginInfo.getVCellSoftwareVersion());
		}
		
		dbObject.put(MongoMessage_clientInfo, dbObjectClientInfo);
	}
	
	private static void addObject(BasicDBObject dbObject, SimulationJobStatusEvent simJobStatusEvent){
		addObject(dbObject, simJobStatusEvent.getSimulationMessage());
		addObject(dbObject, simJobStatusEvent.getJobStatus());
		if (simJobStatusEvent.getUser()!=null){
			dbObject.put(MongoMessage_userName,simJobStatusEvent.getUser().getName());
		}
		if (simJobStatusEvent.getProgress()!=null){
			dbObject.put(MongoMessage_simProgress, simJobStatusEvent.getProgress());
		}
		if (simJobStatusEvent.getTimepoint()!=null){
			dbObject.put(MongoMessage_simTime, simJobStatusEvent.getTimepoint());
		}			
	}
	
	private static void addObject(BasicDBObject dbObject, RpcRequest rpcRequest){
		dbObject.put(MongoMessage_rpcRequestArgs,Arrays.asList(rpcRequest.getArguments()).toString());
		for (Object arg : rpcRequest.getArguments()){
			//
			// look for simulation IDs in rpcRequest arguments ... add to field.
			//
			if (arg instanceof VCSimulationIdentifier){
				dbObject.put(MongoMessage_simId, ((VCSimulationIdentifier)arg).getSimulationKey().toString());
			}else if (arg instanceof VCSimulationDataIdentifier){
				dbObject.put(MongoMessage_simId, ((VCSimulationDataIdentifier)arg).getSimulationKey().toString());
			}else if (arg instanceof VCSimulationDataIdentifierOldStyle){
				dbObject.put(MongoMessage_simId, ((VCSimulationDataIdentifierOldStyle)arg).getSimulationKey().toString());
			}
		}
		dbObject.put(MongoMessage_rpcRequestMethod,rpcRequest.getMethodName());
		dbObject.put(MongoMessage_rpcRequestService,rpcRequest.getRequestedServiceType().getName());
		dbObject.put(MongoMessage_userName,rpcRequest.getUserName());
	}
	
	private static void addObject(BasicDBObject dbObject, SimulationTask simulationTask){
		dbObject.put(MongoMessage_simId,simulationTask.getSimulationJob().getVCDataIdentifier().getSimulationKey().toString());
		dbObject.put(MongoMessage_jobIndex, simulationTask.getSimulationJob().getJobIndex());
		dbObject.put(MongoMessage_taskId, simulationTask.getTaskID());
		dbObject.put(MongoMessage_simComputeResource,simulationTask.getComputeResource());
		dbObject.put(MongoMessage_simEstMemory,simulationTask.getEstimatedMemorySizeMB());
	}
	

	private static void addObject(BasicDBObject dbObject, WorkerEventMessage workerEventMessage){
		WorkerEvent workerEvent = workerEventMessage.getWorkerEvent();
		dbObject.put(MongoMessage_computeHost, workerEvent.getHostName());
		dbObject.put(MongoMessage_simId,workerEvent.getVCSimulationDataIdentifier().getSimulationKey().toString());
		dbObject.put(MongoMessage_jobIndex, workerEvent.getJobIndex());
		dbObject.put(MongoMessage_taskId, workerEvent.getTaskID());
		//workerEvent.getEventTypeID();
		//workerEvent.getMessageData();
		//workerEvent.getMessageSource();
		addObject(dbObject, workerEvent.getSimulationMessage());
		dbObject.put(MongoMessage_simProgress,workerEvent.getProgress());
		dbObject.put(MongoMessage_simTime,workerEvent.getTimePoint());
	}
	
	private static void addObject(BasicDBObject dbObject, SolverEvent solverEvent){
		AbstractSolver solver = (AbstractSolver)solverEvent.getSource();
		dbObject.put(MongoMessage_simProgress,solverEvent.getProgress());
		dbObject.put(MongoMessage_simTime,solverEvent.getTimePoint());
		addObject(dbObject, solverEvent.getSimulationMessage());
		dbObject.put(MongoMessage_solverEventType, solverEvent.getType());
		SimulationJob simJob = solver.getSimulationJob();
		dbObject.put(MongoMessage_simId,simJob.getVCDataIdentifier().getSimulationKey().toString());
		dbObject.put(MongoMessage_jobIndex, solver.getJobIndex());
		dbObject.put(MongoMessage_serverId, VCellServerID.getSystemServerID().toString());
	}
	
	private static void addObject(BasicDBObject dbObject, SimulationJobStatus newSimulationJobStatus){
		dbObject.put(MongoMessage_simId,newSimulationJobStatus.getVCSimulationIdentifier().getSimulationKey().toString());
		dbObject.put(MongoMessage_taskId, newSimulationJobStatus.getTaskID());
		dbObject.put(MongoMessage_jobIndex, newSimulationJobStatus.getJobIndex());
		dbObject.put(MongoMessage_schedulerStatus, newSimulationJobStatus.getSchedulerStatus().getDescription());
		dbObject.put(MongoMessage_serverId, newSimulationJobStatus.getServerID().toString());
		if (newSimulationJobStatus.getTimeDateStamp()!=null){
			dbObject.put(MongoMessage_simJobStatusTimeStamp,newSimulationJobStatus.getTimeDateStamp().getTime());
			dbObject.put(MongoMessage_simJobStatusTimeStampNice,newSimulationJobStatus.getTimeDateStamp().toString());
		}

		addObject(dbObject,newSimulationJobStatus.getSimulationExecutionStatus());

		addObject(dbObject,newSimulationJobStatus.getSimulationMessage());

		addObject(dbObject,newSimulationJobStatus.getSimulationQueueEntryStatus());
	}
	
	private static void addObject(BasicDBObject dbObject, SimulationExecutionStatus simExeStatus){
		if (simExeStatus==null){
			return;
		}
		dbObject.put(MongoMessage_computeHost, simExeStatus.getComputeHost());
		dbObject.put(MongoMessage_hasData, simExeStatus.hasData());
		if (simExeStatus.getEndDate()!=null){
			dbObject.put(MongoMessage_endTime, simExeStatus.getEndDate().getTime());
			dbObject.put(MongoMessage_endTimeNice, simExeStatus.getEndDate().toString());
		}
		if (simExeStatus.getStartDate()!=null){
			dbObject.put(MongoMessage_startTime, simExeStatus.getStartDate().getTime());
			dbObject.put(MongoMessage_startTimeNice, simExeStatus.getStartDate().toString());
		}
		if (simExeStatus.getLatestUpdateDate()!=null){
			dbObject.put(MongoMessage_latestUpdateTime, simExeStatus.getLatestUpdateDate().getTime());
			dbObject.put(MongoMessage_latestUpdateTimeNice, simExeStatus.getLatestUpdateDate().toString());
		}
		if (simExeStatus.getPbsJobID()!=null){
			dbObject.put(MongoMessage_pbsJobID, simExeStatus.getPbsJobID().getID());
		}
	}
	
	private static void addObject(BasicDBObject dbObject, SimulationMessage simMessage){
		if (simMessage==null){
			return;
		}
		DetailedState detailedState = simMessage.getDetailedState();
		dbObject.put(MongoMessage_simMessageState,detailedState.name());
		dbObject.put(MongoMessage_simMessageMsg,simMessage.getDisplayMessage());
		if (simMessage.getPbsJobId()!=null){
			dbObject.put(MongoMessage_pbsJobID,simMessage.getPbsJobId().getID());
		}
	}

	private static void addObject(BasicDBObject dbObject, SimulationQueueEntryStatus simQueueEntryStatus){
		if (simQueueEntryStatus==null){
			return;
		}
		if (simQueueEntryStatus.getQueueDate()!=null){
			dbObject.put(MongoMessage_simQueueEntryDate,simQueueEntryStatus.getQueueDate().getTime());
			dbObject.put(MongoMessage_simQueueEntryDateNice,simQueueEntryStatus.getQueueDate().toString());
		}
		dbObject.put(MongoMessage_simQueueEntryId,simQueueEntryStatus.getQueueID());
		dbObject.put(MongoMessage_simQueueEntryPriority,simQueueEntryStatus.getQueuePriority());
	}


}