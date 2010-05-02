package cbit.vcell.modeldb;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JFrame;

import org.vcell.util.BigString;
import org.vcell.util.DataAccessException;
import org.vcell.util.SessionLog;
import org.vcell.util.StdoutSessionLog;
import org.vcell.util.TokenMangler;
import org.vcell.util.document.BioModelInfo;
import org.vcell.util.document.KeyValue;
import org.vcell.util.document.MathModelInfo;
import org.vcell.util.document.User;
import org.vcell.util.document.UserInfo;
import org.vcell.util.document.VCDocument;
import org.vcell.util.document.VCDocumentInfo;

import cbit.sql.ConnectionFactory;
import cbit.sql.DBCacheTable;
import cbit.sql.Field;
import cbit.sql.KeyFactory;
import cbit.sql.OracleKeyFactory;
import cbit.sql.OraclePoolingConnectionFactory;
import cbit.sql.Table;
import cbit.sql.VersionTable;
import cbit.vcell.biomodel.BioModel;
import cbit.vcell.mapping.MappingException;
import cbit.vcell.mapping.SimulationContext;
import cbit.vcell.math.MathDescription;
import cbit.vcell.math.MathException;
import cbit.vcell.model.ModelException;
import cbit.vcell.parser.ExpressionException;
import cbit.vcell.server.AdminDatabaseServer;
import cbit.vcell.solver.Simulation;
import cbit.vcell.solver.SolverResultSetInfo;
import cbit.vcell.xml.XmlHelper;
/**
 * Insert the type's description here.
 * Creation date: (2/2/01 2:57:33 PM)
 * @author: Jim Schaff
 */
public class MathVerifier {
	//
	// list of files to discard ???
	//
//	private java.util.Vector garbageFileList = new java.util.Vector();
	//
	// key   = KeyValue(simulation)
	// value = File() object of .log file
	//
//	private java.util.Hashtable resolvedFileHash = new java.util.Hashtable();
	private AdminDatabaseServer adminDbServer = null;
	private cbit.sql.ConnectionFactory conFactory = null;
	private DatabaseServerImpl dbServerImpl = null;
	private cbit.sql.KeyFactory keyFactory = null;
	private org.vcell.util.SessionLog log = null;
	private cbit.vcell.modeldb.MathDescriptionDbDriver mathDescDbDriver = null;
	private java.util.HashSet skipHash = new java.util.HashSet(); // holds KeyValues of BioModels to skip
	private String testFlag;
	private Timestamp timeStamp;

/**
 * ResultSetCrawler constructor comment.
 */
public MathVerifier(ConnectionFactory argConFactory, KeyFactory argKeyFactory,
		AdminDatabaseServer argAdminDbServer, SessionLog argSessionLog) throws DataAccessException, SQLException {
	cbit.sql.DBCacheTable dbCacheTable = new DBCacheTable(1000*60*30);	
	this.conFactory = argConFactory;
	this.keyFactory = argKeyFactory;
	this.log = argSessionLog;
	this.adminDbServer = argAdminDbServer;
	GeomDbDriver geomDB = new GeomDbDriver(dbCacheTable,argSessionLog);
	this.mathDescDbDriver = new MathDescriptionDbDriver(dbCacheTable,geomDB,argSessionLog);
	this.dbServerImpl = new DatabaseServerImpl(conFactory,keyFactory,dbCacheTable,argSessionLog);
}

public static class LoadModelsStatTable extends Table{
	private static final String TABLE_NAME = "loadmodelstat";
	public static final String REF_TYPE = "REFERENCES " + TABLE_NAME + "(" + Table.id_ColumnName + ")";

    private static final String[] loadModelsStatTableConstraint =
		new String[] {
			"ldmdlstat_only_1 CHECK("+
			"DECODE(bioModelRef,NULL,0,bioModelRef,1)+"+
			"DECODE(mathModelRef,NULL,0,mathModelRef,1) = 1)"
		};

	public static final int RESULTFLAG_SUCCESS = 0;
	public static final int RESULTFLAG_FAILURE = 1;
	public static final int MAX_ERROR_MSG_SIZE = 255;
	public static final int SOFTWARE_VERS_SIZE = 32;
	
	public final Field bioModelRef		= new Field("bioModelRef",	"integer",			BioModelTable.REF_TYPE+ " ON DELETE CASCADE");
	public final Field mathModelRef		= new Field("mathModelRef",	"integer",			MathModelTable.REF_TYPE+ " ON DELETE CASCADE");
	public final Field resultFlag		= new Field("resultFlag",	"integer",	"");
	public final Field errorMessage		= new Field("errorMessage",	"varchar2("+MAX_ERROR_MSG_SIZE+")",	"");
	public final Field timeStamp		= new Field("timeStamp",	"varchar2(32)",	"NOT NULL");
	public final Field loadTime			= new Field("loadTime",		"integer",	"");
	public final Field softwareVers		= new Field("softwareVers",	"varchar2("+SOFTWARE_VERS_SIZE+")",	"NOT NULL");
	
	private final Field fields[] = {bioModelRef,mathModelRef,resultFlag,errorMessage,timeStamp,loadTime,softwareVers};
	
	public static final LoadModelsStatTable table = new LoadModelsStatTable();
	/**
	 * ModelTable constructor comment.
	 */
	private LoadModelsStatTable() {
		super(TABLE_NAME,loadModelsStatTableConstraint);
		addFields(fields);
	}
	
}

public static final String MV_DEFAULT = "MV_DEFAULT";
public static final String MV_LOAD_XML = "MV_LOAD_XML";

public static MathVerifier createMathVerifier(
		String dbHostName,String dbName,String dbSchemaUser,String dbPassword) throws Exception{
	
    SessionLog sessionLog = new StdoutSessionLog("MathVerifier");
    ConnectionFactory conFactory = null;
    KeyFactory keyFactory = null;
    new org.vcell.util.PropertyLoader();

    String driverName = "oracle.jdbc.driver.OracleDriver";
    String connectURL = "jdbc:oracle:thin:@" + dbHostName + ":1521:" + dbName;

    //
    // get appropriate database factory objects
    //
    conFactory =
        new OraclePoolingConnectionFactory(
        	sessionLog,
            driverName,
            connectURL,
            dbSchemaUser,
            dbPassword);
    keyFactory = new OracleKeyFactory();
    
    AdminDatabaseServer adminDbServer =
    	new LocalAdminDbServer(conFactory, keyFactory, sessionLog);
    
    return new MathVerifier(conFactory, keyFactory, adminDbServer, sessionLog);
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(String[] args) {
    //
        if (args.length != 7) {
            System.out.println(
                "Usage: host databaseSID schemaUser schemaUserPassword {MV_DEFAULT,MV_LOAD_XML} {user,-} softwareVersion");
            System.exit(0);
        }
        String host = args[0];
        String db = args[1];
        String connectURL = "jdbc:oracle:thin:@" + host + ":1521:" + db;
        String dbSchemaUser = args[2];
        String dbPassword = args[3];
        String testFlag = args[4];
        String user = args[5];
        String softwareVersion = args[6];
        //

        int ok =
            javax.swing.JOptionPane.showConfirmDialog(
                new JFrame(),
                "Will run MathVerifier with settings: "
                    + "\nconnectURL="
                    + connectURL
                    + "\nUser="
                    + dbSchemaUser
                    + "\npassword="
                    + dbPassword
                    +"\ntestFlag="
                    + testFlag
                    +"\nUser="
                    + (user.equals("-")?"all users":user)
                    +"\nsoftwareVersion="
                    + softwareVersion,
                "Confirm",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);
        if (ok != javax.swing.JOptionPane.OK_OPTION) {
            throw new RuntimeException("Aborted by user");
        }
        
	try {
    	MathVerifier mathVerifier = MathVerifier.createMathVerifier(host, db, dbSchemaUser, dbPassword);
        if(testFlag.equals(MV_LOAD_XML)){
        	mathVerifier.runLoadTest((user == null?null:new String[] {user}), null,softwareVersion);
        }else if(testFlag.equals(MV_DEFAULT)){
        	mathVerifier.runMathTest((user == null?null:new String[] {user}),null);
        }
	} catch (Throwable e) {
	    e.printStackTrace(System.out);
	}
    System.exit(0);
}

private User[] createUsersFromUserids(String[] scanUserids) throws Exception{
    User[] scanUsers = null;
    if(scanUserids == null){
        UserInfo[] allUserInfos = adminDbServer.getUserInfos();
    	scanUsers = new User[allUserInfos.length];
    	for (int i = 0; i < allUserInfos.length; i++) {
			scanUsers[i] = new User(allUserInfos[i].userid,allUserInfos[i].id);
		}
    }else{
    	scanUsers = new User[scanUserids.length];
    	for (int i = 0; i < scanUserids.length; i++) {
			scanUsers[i] = adminDbServer.getUser(scanUserids[i]);
		}
    }
    return scanUsers;
}
private void closeAllConnections(){
	if(this.conFactory != null){
		try{this.conFactory.closeAll();}catch(Exception e){e.printStackTrace();}
	}

}

public void runLoadTest(String[] scanUserids,KeyValue[] bioAndMathModelKeys,String softwareVersion) throws Exception{
	this.testFlag = MathVerifier.MV_LOAD_XML;
	this.timeStamp = new Timestamp(System.currentTimeMillis());
	User[] scanUsers = createUsersFromUserids(scanUserids);
    try{
    	MathVerifier.initLoadModelsStatTable(softwareVersion,scanUsers,bioAndMathModelKeys,this.timeStamp,this.conFactory,this.log);
    	this.scan(scanUsers, true, bioAndMathModelKeys);
    }finally{
    	closeAllConnections();
    }

}
public void runMathTest(String[] scanUserids,KeyValue[] bioAndMathModelKeys) throws Exception{
	this.testFlag = MathVerifier.MV_DEFAULT;
	User[] scanUsers = createUsersFromUserids(scanUserids);
    try{
    	this.scan(scanUsers, true, bioAndMathModelKeys);
    }finally{
    	closeAllConnections();
    }
}
private static void initLoadModelsStatTable(String softwareVersion,User[] users,KeyValue[] bioAndMathModelKeys,Timestamp timestamp,
		ConnectionFactory conFactory,SessionLog sessionLog) throws Exception{
	java.sql.Connection con = null;
	java.sql.Statement stmt = null;
	try {
		con = conFactory.getConnection(new Object());
		StringBuffer sql = new StringBuffer();
		
		//Make sure timestamp does not exist in table
		stmt = con.createStatement();
		sql.setLength(0);
		sql.append(
			"SELECT COUNT(*)"+
			" FROM "+LoadModelsStatTable.table.getTableName() +
			" WHERE "+LoadModelsStatTable.table.timeStamp + " = " + "'" + timestamp.toString() + "'");
		ResultSet resultSet = stmt.executeQuery(sql.toString());
		if(resultSet.next()){
			if(resultSet.getInt(1) != 0){
				throw new Exception("Timestamp "+timestamp.toString()+" already exists in table '"+LoadModelsStatTable.table.getTableName()+"'");
			}
		}else{
			throw new Exception("Timestamp query returned nothing");
		}
		resultSet.close();
		stmt.close();
		
		//Scan only users condition
		String onlyUsersClause = null;
		if(users != null){
			StringBuffer usersSB = new StringBuffer();
			for (int i = 0; i < users.length; i++) {
				usersSB.append((i>0?",":"")+users[i].getID());
			}
			onlyUsersClause = 
				BioModelTable.table.ownerRef.getUnqualifiedColName()+" IN ("+usersSB.toString()+")";
		}
		//Scan only model user conditions
		String onlyModelsClause = null;
		if(bioAndMathModelKeys != null){
			StringBuffer modelsSB = new StringBuffer();
			for (int i = 0; i < bioAndMathModelKeys.length; i++) {
				modelsSB.append((i>0?",":"")+bioAndMathModelKeys[i].toString());
			}
			onlyModelsClause = 
				VersionTable.id_ColumnName+" IN ("+modelsSB.toString()+")";
		}

		String allConditions = "";
		if(onlyModelsClause != null && onlyUsersClause != null){
			allConditions = " WHERE "+onlyUsersClause +" AND "+onlyModelsClause;
		}else if(onlyModelsClause != null){
			allConditions = " WHERE "+onlyModelsClause;
		}else if(onlyUsersClause != null){
			allConditions = " WHERE "+onlyUsersClause;
		}
		//Get ALL BioModel keys
		stmt = con.createStatement();
		sql.setLength(0);
		sql.append(
			"SELECT "+BioModelTable.table.id.getUnqualifiedColName() +
			" FROM "+BioModelTable.table.getTableName()+allConditions);
		Vector<BigDecimal> bioModelKeyV = new Vector<BigDecimal>();
		resultSet = stmt.executeQuery(sql.toString());
		while(resultSet.next()){
			bioModelKeyV.add(resultSet.getBigDecimal(BioModelTable.table.id.getUnqualifiedColName()));
		}
		resultSet.close();
		stmt.close();
		
		//Get ALL MathModel keys
		stmt = con.createStatement();
		sql.setLength(0);
		sql.append(
			"SELECT "+MathModelTable.table.id.getUnqualifiedColName() +
			" FROM "+MathModelTable.table.getTableName()+allConditions);
		Vector<BigDecimal> mathModelKeyV = new Vector<BigDecimal>();
		resultSet = stmt.executeQuery(sql.toString());
		while(resultSet.next()){
			mathModelKeyV.add(resultSet.getBigDecimal(MathModelTable.table.id.getUnqualifiedColName()));
		}
		resultSet.close();
		stmt.close();
		
		//Init row for each model
		int totalKeys = bioModelKeyV.size() + mathModelKeyV.size();
		for (int i = 0; i < totalKeys; i++) {
			String bioModelKeyS = "NULL";
			String mathModelKeyS = "NULL";
			if(i < bioModelKeyV.size()){
				bioModelKeyS = bioModelKeyV.elementAt(i).toString();
			}else{
				mathModelKeyS = mathModelKeyV.elementAt(i-bioModelKeyV.size()).toString();
			}
			sql.setLength(0);
			sql.append(
				"INSERT INTO " + LoadModelsStatTable.table.getTableName() + " " +
				" VALUES (" +
					Table.NewSEQ+","+
					bioModelKeyS+","+
					mathModelKeyS+",NULL,NULL,"+
					"'"+timestamp.toString()+"'"+
					",NULL,"+
					"'"+TokenMangler.getSQLEscapedString(softwareVersion, LoadModelsStatTable.SOFTWARE_VERS_SIZE)+"'"+")");
			DbDriver.updateCleanSQL(con, sql.toString());

		}
		con.commit();
	}catch (Exception e2){
		if(con != null){
			try{con.rollback();}catch(Exception e){sessionLog.exception(e);}
		}
		throw e2;
	}finally{
		if (stmt != null){
			try{stmt.close();}catch(Exception e){sessionLog.exception(e);}
		}
		if(con != null){
			try{con.close();}catch(Exception e){sessionLog.exception(e);}
		}
	}
}

private void updateLoadModelsStatTable(long loadTimeMilliSec,SessionLog userLog,KeyValue BioOrMathModelKey,Exception exception){
	String sql =
		"UPDATE "+LoadModelsStatTable.table.getTableName()+
		" SET "+
		LoadModelsStatTable.table.resultFlag + " = " +
			(exception==null?LoadModelsStatTable.RESULTFLAG_SUCCESS:LoadModelsStatTable.RESULTFLAG_FAILURE)+","+
		LoadModelsStatTable.table.errorMessage + " = " +
			(exception==null?"NULL":"'"+TokenMangler.getSQLEscapedString(exception.getClass().getName()+"::"+exception.getMessage(), LoadModelsStatTable.MAX_ERROR_MSG_SIZE)+"'")+","+
		LoadModelsStatTable.table.loadTime + " = " +
			(exception==null?loadTimeMilliSec:"NULL") +
		" WHERE " +
		"("+
			"("+LoadModelsStatTable.table.bioModelRef + " IS NOT NULL"+
				" AND " + LoadModelsStatTable.table.bioModelRef + " = " + BioOrMathModelKey.toString()+")"+
			" OR "+
			"("+LoadModelsStatTable.table.mathModelRef + " IS NOT NULL"+
				" AND " + LoadModelsStatTable.table.mathModelRef + " = " + BioOrMathModelKey.toString()+")"+
		") AND "+LoadModelsStatTable.table.timeStamp + " = '" + timeStamp.toString()+"'";
	try{
    	Connection con = null;
    	Object lock = new Object();
    	try{
    		con = conFactory.getConnection(lock);
    		DbDriver.updateCleanSQL(con, sql);
    		con.commit();
    	}finally{
    		if(con != null){conFactory.release(con, lock);}
    	}
	}catch(Exception e){
		userLog.exception(e);
	}
}

private void checkMathForBioModel(BigString bioModelXMLFromDB,BioModel bioModelFromDB,User user,SessionLog userLog,boolean bUpdateDatabase) throws Exception{
	BioModel bioModelNewMath = XmlHelper.XMLToBioModel(bioModelXMLFromDB.toString());
	bioModelFromDB.refreshDependencies();
	bioModelNewMath.refreshDependencies();

	//
	// get all Simulations for this model
	//
	Simulation modelSimsFromDB[] = bioModelFromDB.getSimulations();
	SolverResultSetInfo rsetInfos[] = new SolverResultSetInfo[modelSimsFromDB.length];
	for (int k = 0; k < modelSimsFromDB.length; k++){
		try {
			rsetInfos[k] = dbServerImpl.getResultSetInfo(user, modelSimsFromDB[k].getVersion().getVersionKey(),0);
		}catch (Throwable e){
			userLog.exception(e);
			userLog.alert("failure reading ResultSetInfo for Simulation("+modelSimsFromDB[k]+") of BioModel("+bioModelFromDB.getVersion().getVersionKey()+") '"+bioModelFromDB.getName()+"'  "+bioModelFromDB.getVersion().getDate());
		}
	}

	
	//
	// for each application, recompute mathDescription, and verify it is equivalent
	// then check each associated simulation to ensure math overrides are applied in an equivalent manner also.
	//
	SimulationContext simContextsFromDB[] = bioModelFromDB.getSimulationContexts();
	SimulationContext simContextsNewMath[] = bioModelNewMath.getSimulationContexts();
	for (int k = 0; k < simContextsFromDB.length; k++){
		SimulationContext simContextFromDB = simContextsFromDB[k];
		Simulation appSimsFromDB[] = simContextFromDB.getSimulations();
		SimulationContext simContextNewMath = simContextsNewMath[k];
		Simulation appSimsNewMath[] = simContextNewMath.getSimulations();
		String mathEquivalency = null;
		try {
			MathDescription origMathDesc = simContextFromDB.getMathDescription();
			//
			// find out if any simulation belonging to this Application has data
			//
			boolean bApplicationHasData = false;
			for (int l = 0; l < rsetInfos.length; l++){
				if (rsetInfos[l]!=null){
					bApplicationHasData = true;
				}
			}
			//
			// bug compatability mode off
			//
			cbit.vcell.mapping.MembraneStructureAnalyzer.bResolvedFluxCorrectionBug = false;
			cbit.vcell.mapping.MembraneMapping.bFluxCorrectionBugMode = false;
			cbit.vcell.mapping.FeatureMapping.bTotalVolumeCorrectionBug = false;
			cbit.vcell.mapping.MembraneStructureAnalyzer.bNoFluxIfFixed = false;

			//
			// make sure geometry is up to date on "simContextNewMath"
			//
			try {
				if (simContextNewMath.getGeometry().getDimension()>0 && simContextNewMath.getGeometry().getGeometrySurfaceDescription().getGeometricRegions()==null){
					simContextNewMath.getGeometry().getGeometrySurfaceDescription().updateAll();
				}
			}catch (Exception e){
				e.printStackTrace(System.out);
			}
			
			//
			// updated mathdescription loaded into copy of biomodel, then test for equivalence.
			//
			cbit.vcell.mapping.MathMapping mathMapping = new cbit.vcell.mapping.MathMapping(simContextNewMath);
			MathDescription newMathDesc = mathMapping.getMathDescription();
			String issueString = null;
			org.vcell.util.Issue issues[] = mathMapping.getIssues();
			if (issues!=null && issues.length>0){
				StringBuffer buffer = new StringBuffer("Issues("+issues.length+"):");
				for (int l = 0; l < issues.length; l++){
					buffer.append(" <<"+issues[l].toString()+">>");
				}
				issueString = buffer.toString();
			}
			simContextNewMath.setMathDescription(newMathDesc);

			StringBuffer reasonForDecision = new StringBuffer();
			boolean bEquivalent = cbit.vcell.math.MathDescriptionTest.testIfSame(origMathDesc,newMathDesc,new StringBuffer());
			mathEquivalency = MathDescription.testEquivalency(origMathDesc,newMathDesc,reasonForDecision);
			StringBuffer buffer = new StringBuffer();
			buffer.append(">>>BioModel("+bioModelFromDB.getVersion().getVersionKey()+") '"+bioModelFromDB.getName()+"':"+bioModelFromDB.getVersion().getDate()+", Application("+simContextFromDB.getKey()+") '"+simContextFromDB.getName()+"' <<EQUIV="+mathEquivalency+">>: "+reasonForDecision);
			//
			// update Database Status for SimContext
			//
			if (bUpdateDatabase){
				java.sql.Connection con = null;
				java.sql.Statement stmt = null;
				try {
					con = conFactory.getConnection(new Object());
					stmt = con.createStatement();
					//KeyValue mathKey = origMathDesc.getKey();
					String UPDATESTATUS = "UPDATE "+SimContextStat2Table.table.getTableName()+
										  " SET "  +SimContextStat2Table.table.hasData.getUnqualifiedColName()+" = "+((bApplicationHasData)?(1):(0))+", "+
										            SimContextStat2Table.table.equiv.getUnqualifiedColName()+" = "+(mathEquivalency.equals(MathDescription.MATH_DIFFERENT)?(0):(1))+", "+
										            SimContextStat2Table.table.status.getUnqualifiedColName()+" = '"+org.vcell.util.TokenMangler.getSQLEscapedString(mathEquivalency+": "+reasonForDecision.toString())+"'"+
										            ((issueString!=null)?(", "+SimContextStat2Table.table.comments.getUnqualifiedColName()+" = '"+org.vcell.util.TokenMangler.getSQLEscapedString(issueString,255)+"'"):(""))+
										  " WHERE "+SimContextStat2Table.table.simContextRef.getUnqualifiedColName()+" = "+simContextFromDB.getKey();
					int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
					if (numRowsChanged!=1){
						System.out.println("failed to update status");
					}
					con.commit();
					userLog.print("-------------- Update=true, saved 'newMath' for Application '"+simContextFromDB.getName()+"'");
				}catch (SQLException e){
					userLog.exception(e);
					userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO UPDATE MATH for Application '"+simContextFromDB.getName()+"'");
				}finally{
					if (stmt != null){
						stmt.close();
					}
					con.close();
				}
			}
		}catch (Throwable e){
			log.exception(e); // exception in SimContext
			if (bUpdateDatabase){
				java.sql.Connection con = null;
				java.sql.Statement stmt = null;
				try {
					con = conFactory.getConnection(new Object());
					stmt = con.createStatement();
					//KeyValue mathKey = origMathDesc.getKey();
					String UPDATESTATUS = "UPDATE "+SimContextStat2Table.table.getTableName()+
										  " SET "  +SimContextStat2Table.table.status.getUnqualifiedColName()+" = 'EXCEPTION: "+org.vcell.util.TokenMangler.getSQLEscapedString(e.toString())+"'"+
										  " WHERE "+SimContextStat2Table.table.simContextRef.getUnqualifiedColName()+" = "+simContextFromDB.getKey();
					int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
					if (numRowsChanged!=1){
						System.out.println("failed to update status with exception");
					}
					con.commit();
					userLog.print("-------------- Update=true, saved exception for Application '"+simContextFromDB.getName()+"'");
				}catch (SQLException e2){
					userLog.exception(e2);
					userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO save exception status for Application '"+simContextFromDB.getName()+"'");
				}finally{
					if (stmt != null){
						stmt.close();
					}
					con.close();
				}
			}
		}
		//
		// now, verify each associated simulation will apply overrides in an equivalent way
		//
		for (int l = 0; l < appSimsFromDB.length; l++){
			try {
				boolean bSimEquivalent = Simulation.testEquivalency(appSimsNewMath[l], appSimsFromDB[l], mathEquivalency);
				userLog.print("Application("+simContextFromDB.getKey()+") '"+simContextFromDB.getName()+"', "+
							  "Simulation("+modelSimsFromDB[l].getKey()+") '"+modelSimsFromDB[l].getName()+"':"+modelSimsFromDB[l].getVersion().getDate()+
							  "mathEquivalency="+mathEquivalency+", simEquivalency="+bSimEquivalent);
				//
				// update Database Status for Simulation
				//
				if (bUpdateDatabase){
					java.sql.Connection con = null;
					java.sql.Statement stmt = null;
					try {
						con = conFactory.getConnection(new Object());
						stmt = con.createStatement();
						String UPDATESTATUS = "UPDATE "+SimStatTable.table.getTableName()+
											  " SET "  +SimStatTable.table.equiv.getUnqualifiedColName()+" = "+((bSimEquivalent)?(1):(0))+ ", "+
											            SimStatTable.table.status.getUnqualifiedColName()+" = '"+org.vcell.util.TokenMangler.getSQLEscapedString(mathEquivalency)+"'" +
											  " WHERE "+SimStatTable.table.simRef.getUnqualifiedColName()+" = "+appSimsFromDB[l].getKey();
						int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
						if (numRowsChanged!=1){
							System.out.println("failed to update status");
						}
						con.commit();
						userLog.print("-------------- Update=true, saved 'simulation status for simulation '"+appSimsFromDB[l].getName()+"'");
					}catch (SQLException e){
						userLog.exception(e);
						userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO UPDATE status for simulation '"+appSimsFromDB[l].getName()+"'");
					}finally{
						if (stmt != null){
							stmt.close();
						}
						con.close();
					}
				}
			}catch (Throwable e){
				log.exception(e); // exception in SimContext
				if (bUpdateDatabase){
					java.sql.Connection con = null;
					java.sql.Statement stmt = null;
					try {
						con = conFactory.getConnection(new Object());
						stmt = con.createStatement();
						//KeyValue mathKey = origMathDesc.getKey();
						String UPDATESTATUS = "UPDATE "+SimStatTable.table.getTableName()+
											  " SET "  +SimStatTable.table.status.getUnqualifiedColName()+" = 'EXCEPTION: "+org.vcell.util.TokenMangler.getSQLEscapedString(e.toString())+"'"+
											  " WHERE "+SimStatTable.table.simRef.getUnqualifiedColName()+" = "+appSimsFromDB[l].getKey();
						int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
						if (numRowsChanged!=1){
							System.out.println("failed to update status with exception");
						}
						con.commit();
						userLog.print("-------------- Update=true, saved exception for Simulation '"+appSimsFromDB[l].getName()+"'");
					}catch (SQLException e2){
						userLog.exception(e2);
						userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO save exception status for simulation '"+appSimsFromDB[l].getName()+"'");
					}finally{
						if (stmt != null){
							stmt.close();
						}
						con.close();
					}
				}
			}
		}
	}
}
private Comparator<KeyValue> keyValueCpmparator =
	new Comparator<KeyValue>() {
	public int compare(KeyValue o1, KeyValue o2) {
		return Integer.parseInt(o1.toString()) - Integer.parseInt(o2.toString());
	}
};
/**
 * Insert the method's description here.
 * Creation date: (2/2/01 3:40:29 PM)
 */
public void scan(User users[], boolean bUpdateDatabase, KeyValue[] bioAndMathModelKeys) throws MathException, MappingException, SQLException, DataAccessException, ModelException, ExpressionException {
//	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
////	calendar.set(2002,java.util.Calendar.MAY,7+1);
//	calendar.set(2002,java.util.Calendar.JULY,1);
//	final java.util.Date fluxCorrectionOrDisablingBugFixDate = calendar.getTime();
////	calendar.set(2001,java.util.Calendar.JUNE,13+1);
//	calendar.set(2002,java.util.Calendar.JANUARY,1);
//	final java.util.Date totalVolumeCorrectionFixDate = calendar.getTime();
	
	KeyValue[] sortedBioAndMathModelKeys = null;
	if(bioAndMathModelKeys != null){
		sortedBioAndMathModelKeys = bioAndMathModelKeys.clone();
		Arrays.sort(sortedBioAndMathModelKeys,keyValueCpmparator);
	}
	for (int i=0;i<users.length;i++){
		User user = users[i];
		BioModelInfo[] bioModelInfos0 = dbServerImpl.getBioModelInfos(user,false);
		MathModelInfo[] mathModelInfos0 = dbServerImpl.getMathModelInfos(user,false);
		SessionLog userLog = new org.vcell.util.StdoutSessionLog(user.toString());
		userLog.print("Testing user '"+user+"'");

		Vector<VCDocumentInfo> userBioAndMathModelInfoV =
			new Vector<VCDocumentInfo>();
		userBioAndMathModelInfoV.addAll(Arrays.asList(bioModelInfos0));
		userBioAndMathModelInfoV.addAll(Arrays.asList(mathModelInfos0));
			
		//
		// for each bioModel, load BioModelMetaData (to get list of keys for simulations and simContexts
		//
		for (int j = 0; j < userBioAndMathModelInfoV.size(); j++){
			//
			// if certain Bio or Math models are requested, then filter all else out
			//
			if (sortedBioAndMathModelKeys != null){
				int srch =
					Arrays.binarySearch(sortedBioAndMathModelKeys, userBioAndMathModelInfoV.elementAt(j).getVersion().getVersionKey(),keyValueCpmparator);
				if(srch < 0){
					continue;
				}
			}

			//
			// filter out any bioModelKeys present in the "SkipList"
			//
			if (skipHash.contains(userBioAndMathModelInfoV.elementAt(j).getVersion().getVersionKey())){
				System.out.println("skipping "+
					(userBioAndMathModelInfoV.elementAt(j) instanceof BioModelInfo?"BioModel":"MathModel")+
					" with key '"+userBioAndMathModelInfoV.elementAt(j).getVersion().getVersionKey()+"'");
				continue;
			}

			try {
				//
				// read in the BioModel and MathModel from the database
				//
				VCDocument vcDocumentFromDB = null;
				BigString vcDocumentXMLFromDB = null;
				try{
					long startTime = System.currentTimeMillis();
					if(userBioAndMathModelInfoV.elementAt(j) instanceof BioModelInfo){
						vcDocumentXMLFromDB = dbServerImpl.getBioModelXML(user, userBioAndMathModelInfoV.elementAt(j).getVersion().getVersionKey());
						vcDocumentFromDB = XmlHelper.XMLToBioModel(vcDocumentXMLFromDB.toString());
					}else{
						vcDocumentXMLFromDB = dbServerImpl.getMathModelXML(user, userBioAndMathModelInfoV.elementAt(j).getVersion().getVersionKey());						
						vcDocumentFromDB = XmlHelper.XMLToMathModel(vcDocumentXMLFromDB.toString());
					}
					if(bUpdateDatabase && testFlag.equals(MathVerifier.MV_LOAD_XML)){
						updateLoadModelsStatTable(System.currentTimeMillis()-startTime,
							userLog, userBioAndMathModelInfoV.elementAt(j).getVersion().getVersionKey(), null);
					}
				}catch(Exception e){
					log.exception(e); // exception in SimContext
					if (bUpdateDatabase && testFlag.equals(MathVerifier.MV_LOAD_XML)){
						updateLoadModelsStatTable(0,userLog, userBioAndMathModelInfoV.elementAt(j).getVersion().getVersionKey(), e);
					}

				}
				if(vcDocumentFromDB instanceof BioModel && testFlag.equals(MathVerifier.MV_DEFAULT)){
					checkMathForBioModel(vcDocumentXMLFromDB, (BioModel)vcDocumentFromDB, user, userLog, bUpdateDatabase);
				}
				
			}catch (Throwable e){
	            log.exception(e); // exception in whole BioModel
	            // can't update anything in database, since we don't know what simcontexts are involved
			}
		}	
	}
}


/**
 * Insert the method's description here.
 * Creation date: (2/2/01 3:40:29 PM)
 */
public void scanBioModels(boolean bUpdateDatabase, KeyValue[] bioModelKeys) throws MathException, MappingException, SQLException, DataAccessException, ModelException, ExpressionException {
	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
//	calendar.set(2002,java.util.Calendar.MAY,7+1);
	calendar.set(2002,java.util.Calendar.JULY,1);
	final java.util.Date fluxCorrectionOrDisablingBugFixDate = calendar.getTime();
//	calendar.set(2001,java.util.Calendar.JUNE,13+1);
	calendar.set(2002,java.util.Calendar.JANUARY,1);
	final java.util.Date totalVolumeCorrectionFixDate = calendar.getTime();
	

	User user = new User("Administrator", new org.vcell.util.document.KeyValue("2"));
	SessionLog userLog = new org.vcell.util.StdoutSessionLog(user.toString());
	for (int i=0;i<bioModelKeys.length;i++){
		BioModelInfo bioModelInfo = dbServerImpl.getBioModelInfo(user,bioModelKeys[i]);
		userLog.print("Testing bioModel with key '"+bioModelKeys[i]+"'");
        java.sql.Connection con = null;
        java.sql.Statement stmt = null;

		try {
			//
			// read in the BioModel from the database
			//
			BigString bioModelXML = dbServerImpl.getBioModelXML(user, bioModelInfo.getVersion().getVersionKey());
			BioModel bioModelFromDB = XmlHelper.XMLToBioModel(bioModelXML.toString());
			BioModel bioModelNewMath = XmlHelper.XMLToBioModel(bioModelXML.toString());
			bioModelFromDB.refreshDependencies();
			bioModelNewMath.refreshDependencies();

			//
			// get all Simulations for this model
			//
			Simulation modelSimsFromDB[] = bioModelFromDB.getSimulations();
			SolverResultSetInfo rsetInfos[] = new SolverResultSetInfo[modelSimsFromDB.length];
			for (int k = 0; k < modelSimsFromDB.length; k++){
				try {
					rsetInfos[k] = dbServerImpl.getResultSetInfo(user, modelSimsFromDB[k].getVersion().getVersionKey(),0);
				}catch (Throwable e){
					userLog.exception(e);
					userLog.alert("failure reading ResultSetInfo for Simulation("+modelSimsFromDB[k]+") of BioModel("+bioModelFromDB.getVersion().getVersionKey()+") '"+bioModelFromDB.getName()+"'  "+bioModelFromDB.getVersion().getDate());
				}
			}

			
			//
			// for each application, recompute mathDescription, and verify it is equivalent
			// then check each associated simulation to ensure math overrides are applied in an equivalent manner also.
			//
			SimulationContext simContextsFromDB[] = bioModelFromDB.getSimulationContexts();
			SimulationContext simContextsNewMath[] = bioModelNewMath.getSimulationContexts();
			for (int k = 0; k < simContextsFromDB.length; k++){
				SimulationContext simContextFromDB = simContextsFromDB[k];
				Simulation appSimsFromDB[] = simContextFromDB.getSimulations();
				SimulationContext simContextNewMath = simContextsNewMath[k];
				Simulation appSimsNewMath[] = simContextNewMath.getSimulations();
				String mathEquivalency = null;
				try {
					MathDescription origMathDesc = simContextFromDB.getMathDescription();
					//
					// find out if any simulation belonging to this Application has data
					//
					boolean bApplicationHasData = false;
					for (int l = 0; l < rsetInfos.length; l++){
						if (rsetInfos[l]!=null){
							bApplicationHasData = true;
						}
					}
					//
					// bug compatability mode off
					//
					cbit.vcell.mapping.MembraneStructureAnalyzer.bResolvedFluxCorrectionBug = false;
					cbit.vcell.mapping.MembraneMapping.bFluxCorrectionBugMode = false;
					cbit.vcell.mapping.FeatureMapping.bTotalVolumeCorrectionBug = false;
					cbit.vcell.mapping.MembraneStructureAnalyzer.bNoFluxIfFixed = false;

					//
					// make sure geometry is up to date on "simContextNewMath"
					//
					try {
						if (simContextNewMath.getGeometry().getDimension()>0 && simContextNewMath.getGeometry().getGeometrySurfaceDescription().getGeometricRegions()==null){
							simContextNewMath.getGeometry().getGeometrySurfaceDescription().updateAll();
						}
					}catch (Exception e){
						e.printStackTrace(System.out);
					}
					
					//
					// updated mathdescription loaded into copy of biomodel, then test for equivalence.
					//
					cbit.vcell.mapping.MathMapping mathMapping = new cbit.vcell.mapping.MathMapping(simContextNewMath);
					MathDescription newMathDesc = mathMapping.getMathDescription();
					String issueString = null;
					org.vcell.util.Issue issues[] = mathMapping.getIssues();
					if (issues!=null && issues.length>0){
						StringBuffer buffer = new StringBuffer("Issues("+issues.length+"):");
						for (int l = 0; l < issues.length; l++){
							buffer.append(" <<"+issues[l].toString()+">>");
						}
						issueString = buffer.toString();
					}
					simContextNewMath.setMathDescription(newMathDesc);

					StringBuffer reasonForDecision = new StringBuffer();
					boolean bEquivalent = cbit.vcell.math.MathDescriptionTest.testIfSame(origMathDesc,newMathDesc,new StringBuffer());
					mathEquivalency = MathDescription.testEquivalency(origMathDesc,newMathDesc,reasonForDecision);
					StringBuffer buffer = new StringBuffer();
					buffer.append(">>>BioModel("+bioModelFromDB.getVersion().getVersionKey()+") '"+bioModelFromDB.getName()+"':"+bioModelFromDB.getVersion().getDate()+", Application("+simContextFromDB.getKey()+") '"+simContextFromDB.getName()+"' <<EQUIV="+mathEquivalency+">>: "+reasonForDecision);
					//
					// update Database Status for SimContext
					//
					if (bUpdateDatabase){
						con = null;
						stmt = null;
						try {
							con = conFactory.getConnection(new Object());
							stmt = con.createStatement();
							//KeyValue mathKey = origMathDesc.getKey();
							String UPDATESTATUS = "UPDATE "+SimContextStat2Table.table.getTableName()+
												  " SET "  +SimContextStat2Table.table.hasData.getUnqualifiedColName()+" = "+((bApplicationHasData)?(1):(0))+", "+
												            SimContextStat2Table.table.equiv.getUnqualifiedColName()+" = "+(mathEquivalency.equals(MathDescription.MATH_DIFFERENT)?(0):(1))+", "+
												            SimContextStat2Table.table.status.getUnqualifiedColName()+" = '"+org.vcell.util.TokenMangler.getSQLEscapedString(mathEquivalency+": "+reasonForDecision.toString())+"'"+
												            ((issueString!=null)?(", "+SimContextStat2Table.table.comments.getUnqualifiedColName()+" = '"+org.vcell.util.TokenMangler.getSQLEscapedString(issueString,255)+"'"):(""))+
												  " WHERE "+SimContextStat2Table.table.simContextRef.getUnqualifiedColName()+" = "+simContextFromDB.getKey();
							int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
							if (numRowsChanged!=1){
								System.out.println("failed to update status");
							}
							con.commit();
							userLog.print("-------------- Update=true, saved 'newMath' for Application '"+simContextFromDB.getName()+"'");
						}catch (SQLException e){
							userLog.exception(e);
							userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO UPDATE MATH for Application '"+simContextFromDB.getName()+"'");
						}finally{
							if (stmt != null){
								stmt.close();
							}
							con.close();
						}
					}
				}catch (Throwable e){
					log.exception(e); // exception in SimContext
					if (bUpdateDatabase){
						con = null;
						stmt = null;
						try {
							con = conFactory.getConnection(new Object());
							stmt = con.createStatement();
                            String status = "'EXCEPTION: "+org.vcell.util.TokenMangler.getSQLEscapedString(e.toString())+"'";
                            if (status.length() > 255) status = status.substring(0,254)+"'";
                            String UPDATESTATUS = "UPDATE "+SimContextStat2Table.table.getTableName()+
					                              " SET "+SimContextStat2Table.table.status.getUnqualifiedColName()+" = "+status+
												  " WHERE "+SimContextStat2Table.table.simContextRef.getUnqualifiedColName()+" = "+simContextFromDB.getKey();
							int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
							if (numRowsChanged!=1){
								System.out.println("failed to update status with exception");
							}
							con.commit();
							userLog.print("-------------- Update=true, saved exception for Application '"+simContextFromDB.getName()+"'");
						}catch (SQLException e2){
							userLog.exception(e2);
							userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO save exception status for Application '"+simContextFromDB.getName()+"'");
						}finally{
							if (stmt != null){
								stmt.close();
							}
							con.close();
						}
					}
				}
				//
				// now, verify each associated simulation will apply overrides in an equivalent way
				//
				for (int l = 0; l < appSimsFromDB.length; l++){
					try {
						boolean bSimEquivalent = Simulation.testEquivalency(appSimsNewMath[l], appSimsFromDB[l], mathEquivalency);
						userLog.print("Application("+simContextFromDB.getKey()+") '"+simContextFromDB.getName()+"', "+
									  "Simulation("+modelSimsFromDB[l].getKey()+") '"+modelSimsFromDB[l].getName()+"':"+modelSimsFromDB[l].getVersion().getDate()+
									  "mathEquivalency="+mathEquivalency+", simEquivalency="+bSimEquivalent);
						//
						// update Database Status for Simulation
						//
						if (bUpdateDatabase){
							con = null;
							stmt = null;
							try {
								con = conFactory.getConnection(new Object());
								stmt = con.createStatement();
								String UPDATESTATUS = "UPDATE "+SimStatTable.table.getTableName()+
													  " SET "  +SimStatTable.table.equiv.getUnqualifiedColName()+" = "+((bSimEquivalent)?(1):(0))+ ", "+
													            SimStatTable.table.status.getUnqualifiedColName()+" = '"+org.vcell.util.TokenMangler.getSQLEscapedString(mathEquivalency)+"'" +
													  " WHERE "+SimStatTable.table.simRef.getUnqualifiedColName()+" = "+appSimsFromDB[l].getKey();
								int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
								if (numRowsChanged!=1){
									System.out.println("failed to update status");
								}
								con.commit();
								userLog.print("-------------- Update=true, saved 'simulation status for simulation '"+appSimsFromDB[l].getName()+"'");
							}catch (SQLException e){
								userLog.exception(e);
								userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO UPDATE status for simulation '"+appSimsFromDB[l].getName()+"'");
							}finally{
								if (stmt != null){
									stmt.close();
								}
								con.close();
							}
						}
					}catch (Throwable e){
						log.exception(e); // exception in SimContext
						if (bUpdateDatabase){
							con = null;
							stmt = null;
							try {
								con = conFactory.getConnection(new Object());
								stmt = con.createStatement();
	                            String status = "'EXCEPTION: "+org.vcell.util.TokenMangler.getSQLEscapedString(e.toString())+"'";
	                            if (status.length() > 255) status = status.substring(0,254)+"'";
	                            String UPDATESTATUS = "UPDATE "+SimStatTable.table.getTableName()+
                                					  " SET "+SimStatTable.table.status.getUnqualifiedColName()+" = "+status+
													  " WHERE "+SimStatTable.table.simRef.getUnqualifiedColName()+" = "+appSimsFromDB[l].getKey();
								int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
								if (numRowsChanged!=1){
									System.out.println("failed to update status with exception");
								}
								con.commit();
								userLog.print("-------------- Update=true, saved exception for Simulation '"+appSimsFromDB[l].getName()+"'");
							}catch (SQLException e2){
								userLog.exception(e2);
								userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO save exception status for simulation '"+appSimsFromDB[l].getName()+"'");
							}finally{
								if (stmt != null){
									stmt.close();
								}
								con.close();
							}
						}
					}
				}
			}
		}catch (Throwable e){
            log.exception(e); // exception in whole BioModel
            // can't update anything in database, since we don't know what simcontexts are involved
		}
	}
}



/**
 * Insert the method's description here.
 * Creation date: (2/2/01 3:40:29 PM)
 */
public void scanSimContexts(boolean bUpdateDatabase, KeyValue[] simContextKeys) throws MathException, MappingException, SQLException, DataAccessException, ModelException, ExpressionException {
    java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
    //	calendar.set(2002,java.util.Calendar.MAY,7+1);
    calendar.set(2002, java.util.Calendar.JULY, 1);
    final java.util.Date fluxCorrectionOrDisablingBugFixDate = calendar.getTime();
    //	calendar.set(2001,java.util.Calendar.JUNE,13+1);
    calendar.set(2002, java.util.Calendar.JANUARY, 1);
    final java.util.Date totalVolumeCorrectionFixDate = calendar.getTime();

    User user = new User("Administrator", new org.vcell.util.document.KeyValue("2"));
    SessionLog userLog = new org.vcell.util.StdoutSessionLog(user.toString());
    for (int i = 0; i < simContextKeys.length; i++) {
        userLog.print("Testing SimContext with key '" + simContextKeys[i] + "'");
        // get biomodel refs
        java.sql.Connection con = null;
        java.sql.Statement stmt = null;
        con = conFactory.getConnection(new Object());
        cbit.vcell.modeldb.BioModelSimContextLinkTable bmscTable = cbit.vcell.modeldb.BioModelSimContextLinkTable.table;
        String sql = "SELECT "+bmscTable.bioModelRef.getQualifiedColName()+
        			 " FROM "+bmscTable.getTableName()+
        			 " WHERE "+bmscTable.simContextRef.getQualifiedColName()+" = "+simContextKeys[i];
        java.util.Vector keys = new java.util.Vector();
        stmt = con.createStatement();
        try {
            ResultSet rset = stmt.executeQuery(sql);
            while (rset.next()) {
                KeyValue key = new KeyValue(rset.getBigDecimal(bmscTable.bioModelRef.getUnqualifiedColName()));
                keys.addElement(key);
            }
        } finally {
			if (stmt != null) {
				stmt.close();
			}
			con.close();
        }

        KeyValue[] bmKeys = (org.vcell.util.document.KeyValue[]) org.vcell.util.BeanUtils.getArray(keys, org.vcell.util.document.KeyValue.class);
        try {
			// use the first biomodel...
	        BioModelInfo bioModelInfo = dbServerImpl.getBioModelInfo(user, bmKeys[0]);
	        //
            // read in the BioModel from the database
            //
            BigString bioModelXML = dbServerImpl.getBioModelXML(user, bioModelInfo.getVersion().getVersionKey());
            BioModel bioModelFromDB = XmlHelper.XMLToBioModel(bioModelXML.toString());
            BioModel bioModelNewMath = XmlHelper.XMLToBioModel(bioModelXML.toString());
            bioModelFromDB.refreshDependencies();
            bioModelNewMath.refreshDependencies();

            //
            // get all Simulations for this model
            //
            Simulation modelSimsFromDB[] = bioModelFromDB.getSimulations();
            SolverResultSetInfo rsetInfos[] = new SolverResultSetInfo[modelSimsFromDB.length];
            for (int k = 0; k < modelSimsFromDB.length; k++) {
                try {
                    rsetInfos[k] = dbServerImpl.getResultSetInfo(user, modelSimsFromDB[k].getVersion().getVersionKey(),0);
                } catch (Throwable e) {
                    userLog.exception(e);
                    userLog.alert("failure reading ResultSetInfo for Simulation("+modelSimsFromDB[k]+") of BioModel("+bioModelFromDB.getVersion().getVersionKey()+") '"+bioModelFromDB.getName()+"'  "+bioModelFromDB.getVersion().getDate());
                }
            }

            //
            // ---> only for the SimContext we started with...
            // recompute mathDescription, and verify it is equivalent
            // then check each associated simulation to ensure math overrides are applied in an equivalent manner also.
            //
            SimulationContext simContextsFromDB[] = bioModelFromDB.getSimulationContexts();
            SimulationContext simContextsNewMath[] = bioModelNewMath.getSimulationContexts();
            SimulationContext simContextFromDB = null;
            SimulationContext simContextNewMath = null;
            for (int k = 0; k < simContextsFromDB.length; k++) {
	            // find it...
	            if (simContextsFromDB[k].getKey().equals(simContextKeys[i])) {
		            simContextFromDB = simContextsFromDB[k];
					simContextNewMath = simContextsNewMath[k];
		            break;
	            }
            }
            if (simContextFromDB == null) {
	            throw new RuntimeException("BioModel referred to by this SimContext does not contain this SimContext");
            } else {
                Simulation appSimsFromDB[] = simContextFromDB.getSimulations();
                Simulation appSimsNewMath[] = simContextNewMath.getSimulations();
                String mathEquivalency = null;
                try {
                    MathDescription origMathDesc = simContextFromDB.getMathDescription();
                    //
                    // find out if any simulation belonging to this Application has data
                    //
                    boolean bApplicationHasData = false;
                    for (int l = 0; l < rsetInfos.length; l++) {
                        if (rsetInfos[l] != null) {
                            bApplicationHasData = true;
                        }
                    }
                    //
                    // bug compatability mode off
                    //
                    cbit.vcell.mapping.MembraneStructureAnalyzer.bResolvedFluxCorrectionBug = false;
                    cbit.vcell.mapping.MembraneMapping.bFluxCorrectionBugMode = false;
                    cbit.vcell.mapping.FeatureMapping.bTotalVolumeCorrectionBug = false;
                    cbit.vcell.mapping.MembraneStructureAnalyzer.bNoFluxIfFixed = false;

                    //
                    // make sure geometry is up to date on "simContextNewMath"
                    //
                    try {
                        if (simContextNewMath.getGeometry().getDimension() > 0 && simContextNewMath.getGeometry().getGeometrySurfaceDescription().getGeometricRegions() == null) {
                            simContextNewMath.getGeometry().getGeometrySurfaceDescription().updateAll();
                        }
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }

                    //
                    // updated mathdescription loaded into copy of biomodel, then test for equivalence.
                    //
                    cbit.vcell.mapping.MathMapping mathMapping = new cbit.vcell.mapping.MathMapping(simContextNewMath);
                    MathDescription newMathDesc = mathMapping.getMathDescription();
                    String issueString = null;
                    org.vcell.util.Issue issues[] = mathMapping.getIssues();
                    if (issues != null && issues.length > 0) {
                        StringBuffer buffer = new StringBuffer("Issues(" + issues.length + "):");
                        for (int l = 0; l < issues.length; l++) {
                            buffer.append(" <<" + issues[l].toString() + ">>");
                        }
                        issueString = buffer.toString();
                    }
                    simContextNewMath.setMathDescription(newMathDesc);

                    StringBuffer reasonForDecision = new StringBuffer();
                    boolean bEquivalent = cbit.vcell.math.MathDescriptionTest.testIfSame(origMathDesc, newMathDesc, new StringBuffer());
                    mathEquivalency = MathDescription.testEquivalency(origMathDesc, newMathDesc, reasonForDecision);
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(">>>BioModel("+bioModelFromDB.getVersion().getVersionKey()+") '"+bioModelFromDB.getName()+"':"+bioModelFromDB.getVersion().getDate()+", Application("+simContextFromDB.getKey()+") '"+simContextFromDB.getName()+"' <<EQUIV="+mathEquivalency+">>: "+reasonForDecision);
                    //
                    // update Database Status for SimContext
                    //
                    if (bUpdateDatabase) {
                        con = null;
                        stmt = null;
                        try {
                            con = conFactory.getConnection(new Object());
                            stmt = con.createStatement();
                            //KeyValue mathKey = origMathDesc.getKey();
                            String UPDATESTATUS = "UPDATE "+SimContextStat2Table.table.getTableName()+
				                             	  " SET "+SimContextStat2Table.table.hasData.getUnqualifiedColName()+" = "+((bApplicationHasData)?(1):(0))+", "
    			                       				     +SimContextStat2Table.table.equiv.getUnqualifiedColName()+" = "+(mathEquivalency.equals(MathDescription.MATH_DIFFERENT) ? (0) : (1))+", "
       				                       			     +SimContextStat2Table.table.status.getUnqualifiedColName()+" = '"+org.vcell.util.TokenMangler.getSQLEscapedString(mathEquivalency+": "+reasonForDecision.toString())+"'"
           				                     		     +((issueString != null) ? (", "+SimContextStat2Table.table.comments.getUnqualifiedColName()+" = '"+org.vcell.util.TokenMangler.getSQLEscapedString(issueString, 255)+"'") : (""))+
           				                     	  " WHERE "+SimContextStat2Table.table.simContextRef.getUnqualifiedColName()+" = "+simContextFromDB.getKey();
                            int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
                            if (numRowsChanged != 1) {
                                System.out.println("failed to update status");
                            }
                            con.commit();
                            userLog.print("-------------- Update=true, saved 'newMath' for Application '"+simContextFromDB.getName()+"'");
                        } catch (SQLException e) {
                            userLog.exception(e);
                            userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO UPDATE MATH for Application '"+simContextFromDB.getName()+"'");
                        } finally {
                            if (stmt != null) {
                                stmt.close();
                            }
                            con.close();
                        }
                    }
                } catch (Throwable e) {
                    log.exception(e); // exception in SimContext
                    if (bUpdateDatabase) {
                        con = null;
                        stmt = null;
                        try {
                            con = conFactory.getConnection(new Object());
                            stmt = con.createStatement();
                            String status = "'EXCEPTION: "+org.vcell.util.TokenMangler.getSQLEscapedString(e.toString())+"'";
                            if (status.length() > 255) status = status.substring(0,254)+"'";
                            String UPDATESTATUS = "UPDATE "+SimContextStat2Table.table.getTableName()+
					                              " SET "+SimContextStat2Table.table.status.getUnqualifiedColName()+" = "+status+
       						                      " WHERE "+ SimContextStat2Table.table.simContextRef.getUnqualifiedColName()+" = "+simContextFromDB.getKey();
                            int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
                            if (numRowsChanged != 1) {
                                System.out.println("failed to update status with exception");
                            }
                            con.commit();
                            userLog.print("-------------- Update=true, saved exception for Application '"+simContextFromDB.getName()+"'");
                        } catch (SQLException e2) {
                            userLog.exception(e2);
                            userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO save exception status for Application '"+simContextFromDB.getName()+"'");
                        } finally {
                            if (stmt != null) {
                                stmt.close();
                            }
                            con.close();
                        }
                    }
                }
                //
                // now, verify each associated simulation will apply overrides in an equivalent way
                //
                for (int l = 0; l < appSimsFromDB.length; l++) {
                    try {
                        boolean bSimEquivalent = Simulation.testEquivalency(appSimsNewMath[l],appSimsFromDB[l],mathEquivalency);
                        userLog.print("Application("+simContextFromDB.getKey()+") '"+simContextFromDB.getName()+"', "+"Simulation("+modelSimsFromDB[l].getKey()+") '"+modelSimsFromDB[l].getName()+"':"+modelSimsFromDB[l].getVersion().getDate()+"mathEquivalency="+mathEquivalency+", simEquivalency="+bSimEquivalent);
                        //
                        // update Database Status for Simulation
                        //
                        if (bUpdateDatabase) {
                            con = null;
                            stmt = null;
                            try {
                                con = conFactory.getConnection(new Object());
                                stmt = con.createStatement();
                                String UPDATESTATUS = "UPDATE "+SimStatTable.table.getTableName()+
                                					  " SET "+SimStatTable.table.equiv.getUnqualifiedColName()+" = "+((bSimEquivalent) ? (1) : (0))+", "+SimStatTable.table.status.getUnqualifiedColName()+" = '"+org.vcell.util.TokenMangler.getSQLEscapedString(mathEquivalency)+"'"+
                                					  " WHERE "+SimStatTable.table.simRef.getUnqualifiedColName()+" = "+appSimsFromDB[l].getKey();
                                int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
                                if (numRowsChanged != 1) {
                                    System.out.println("failed to update status");
                                }
                                con.commit();
                                userLog.print("-------------- Update=true, saved 'simulation status for simulation '"+appSimsFromDB[l].getName()+"'");
                            } catch (SQLException e) {
                                userLog.exception(e);
                                userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO UPDATE status for simulation '"+appSimsFromDB[l].getName()+"'");
                            } finally {
                                if (stmt != null) {
                                    stmt.close();
                                }
                                con.close();
                            }
                        }
                    } catch (Throwable e) {
                        log.exception(e); // exception in SimContext
                        if (bUpdateDatabase) {
                            con = null;
                            stmt = null;
                            try {
                                con = conFactory.getConnection(new Object());
                                stmt = con.createStatement();
	                            String status = "'EXCEPTION: "+org.vcell.util.TokenMangler.getSQLEscapedString(e.toString())+"'";
	                            if (status.length() > 255) status = status.substring(0,254)+"'";
	                            String UPDATESTATUS = "UPDATE "+SimStatTable.table.getTableName()+
                                					  " SET "+SimStatTable.table.status.getUnqualifiedColName()+" = "+status+
                                					  " WHERE "+SimStatTable.table.simRef.getUnqualifiedColName()+" = "+appSimsFromDB[l].getKey();
                                int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
                                if (numRowsChanged != 1) {
                                    System.out.println("failed to update status with exception");
                                }
                                con.commit();
                                userLog.print("-------------- Update=true, saved exception for Simulation '"+appSimsFromDB[l].getName()+"'");
                            } catch (SQLException e2) {
                                userLog.exception(e2);
                                userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO save exception status for simulation '"+appSimsFromDB[l].getName()+"'");
                            } finally {
                                if (stmt != null) {
                                    stmt.close();
                                }
                                con.close();
                            }
                        }
                    }
                }
        	}
        } catch (Throwable e) {
            log.exception(e); // exception in whole BioModel
	        // update database, since we know the simcontext...
			if (bUpdateDatabase) {
				con = null;
				stmt = null;
				try {
					con = conFactory.getConnection(new Object());
					stmt = con.createStatement();
					//KeyValue mathKey = origMathDesc.getKey();
					String UPDATESTATUS = "UPDATE "+SimContextStat2Table.table.getTableName()+
										  " SET "+SimContextStat2Table.table.status.getUnqualifiedColName()+" = 'BIOMODEL EXCEPTION: "+org.vcell.util.TokenMangler.getSQLEscapedString(e.toString())+"'"+
										  " WHERE "+ SimContextStat2Table.table.simContextRef.getUnqualifiedColName()+" = "+simContextKeys[i];
					int numRowsChanged = stmt.executeUpdate(UPDATESTATUS);
					if (numRowsChanged != 1) {
						System.out.println("failed to update status with exception");
					}
					con.commit();
					userLog.print("-------------- Update=true, saved exception for Application with key '"+simContextKeys[i]+"'");
				} catch (SQLException e2) {
					userLog.exception(e2);
					userLog.alert("*&*&*&*&*&*&*& Update=true, FAILED TO save exception status for Application with key '"+simContextKeys[i]+"'");
				} finally {
					if (stmt != null) {
						stmt.close();
					}
					con.close();
				}
			}
        }
    }
}


/**
 * Insert the method's description here.
 * Creation date: (6/7/2004 12:01:12 PM)
 * @param simContexts cbit.sql.KeyValue[]
 */
public void setSkipList(KeyValue[] simContextKeys) {
	for (int i = 0; i < simContextKeys.length; i++){
		skipHash.add(simContextKeys[i]);
	}
}


}