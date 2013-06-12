/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.modeldb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.vcell.util.DataAccessException;
import org.vcell.util.SessionLog;
import org.vcell.util.document.KeyValue;
import org.vcell.util.document.User;
import org.vcell.util.document.VCellSoftwareVersion;
import org.vcell.util.document.Version;
import org.vcell.util.document.VersionInfo;

import cbit.sql.Field;
import cbit.sql.Table;
import cbit.vcell.biomodel.BioModelMetaData;

/**
 * This type was created in VisualAge.
 */
public class BioModelTable extends cbit.vcell.modeldb.VersionTable {
	private static final String TABLE_NAME = "vc_biomodel";
	public static final String REF_TYPE = "REFERENCES " + TABLE_NAME + "(" + Table.id_ColumnName + ")";

	public final Field modelRef				= new Field("modelRef",			"integer",	"NOT NULL "+ModelTable.REF_TYPE);
	public final Field childSummaryLarge	= new Field("childSummaryLRG",	"CLOB",				"");
	public final Field childSummarySmall	= new Field("childSummarySML",	"VARCHAR2(4000)",	"");
	
	private final Field fields[] = {modelRef,childSummaryLarge,childSummarySmall};
	
	public static final BioModelTable table = new BioModelTable();
/**
 * ModelTable constructor comment.
 */
private BioModelTable() {
	super(TABLE_NAME,BioModelTable.REF_TYPE);
	addFields(fields);
}
/**
 * This method was created in VisualAge.
 * @return cbit.vcell.math.MathDescription
 * @param user cbit.vcell.server.User
 * @param rset java.sql.ResultSet
 */
public BioModelMetaData getBioModelMetaData(ResultSet rset, SessionLog log, BioModelDbDriver bioModelDbDriver, Connection con) 
										throws SQLException,DataAccessException {

	//
	// Get Version
	//
	BigDecimal groupid = rset.getBigDecimal(VersionTable.privacy_ColumnName);
	Version version = getVersion(rset,DbDriver.getGroupAccessFromGroupID(con,groupid),log);
	KeyValue bioModelKey = version.getVersionKey();

	KeyValue modelRef = new KeyValue(rset.getBigDecimal(table.modelRef.toString()));

	//
	// get Simulation Keys for bioModelKey
	//
	KeyValue simKeys[] = bioModelDbDriver.getSimulationEntriesFromBioModel(con, bioModelKey);

	//
	// get SimulationContext Keys for bioModelKey
	//
	KeyValue simContextKeys[] = bioModelDbDriver.getSimContextEntriesFromBioModel(con, bioModelKey);
	
	//
	//Get VCMetaData XML
	//
	String vcMetaDataXML = VCMetaDataTable.getVCMetaDataXML(rset);
	
	BioModelMetaData bioModelMetaData = new BioModelMetaData(version,modelRef,simContextKeys,simKeys,vcMetaDataXML);
	//
	// setMathDescription is done in calling parent
	//
	//simulation.setMathDescription(mathDesc);

	return bioModelMetaData;
}
/**
 * This method was created in VisualAge.
 * @return cbit.vcell.math.MathDescription
 * @param user cbit.vcell.server.User
 * @param rset java.sql.ResultSet
 */
public BioModelMetaData getBioModelMetaData(ResultSet rset, Connection con,SessionLog log, KeyValue simContextKeys[], KeyValue simulationKeys[]) 
										throws SQLException,DataAccessException {

	//
	// Get Version
	//
	BigDecimal groupid = rset.getBigDecimal(VersionTable.privacy_ColumnName);
	Version version = getVersion(rset,DbDriver.getGroupAccessFromGroupID(con,groupid),log);

	KeyValue modelRef = new KeyValue(rset.getBigDecimal(table.modelRef.toString()));

	String vcMetaDataXML = VCMetaDataTable.getVCMetaDataXML(rset);
	BioModelMetaData bioModelMetaData = new BioModelMetaData(version,modelRef,simContextKeys,simulationKeys,vcMetaDataXML);
	//
	// setMathDescription is done in calling parent
	//
	//simulation.setMathDescription(mathDesc);

	return bioModelMetaData;
}
/**
 * This method was created in VisualAge.
 * @return cbit.vcell.geometry.GeometryInfo
 * @param rset java.sql.ResultSet
 * @param log cbit.vcell.server.SessionLog
 */
public VersionInfo getInfo(ResultSet rset,Connection con,SessionLog log) throws SQLException,org.vcell.util.DataAccessException {

	KeyValue modelRef = new KeyValue(rset.getBigDecimal(table.modelRef.toString()));
	BigDecimal groupid = rset.getBigDecimal(VersionTable.privacy_ColumnName);
	Version version = getVersion(rset,DbDriver.getGroupAccessFromGroupID(con,groupid),log);
	String softwareVersion = rset.getString(SoftwareVersionTable.table.softwareVersion.toString());
	VCellSoftwareVersion vcSoftwareVersion = VCellSoftwareVersion.fromString(softwareVersion);
	String serialDbChildSummary = DbDriver.varchar2_CLOB_get(rset,BioModelTable.table.childSummarySmall,BioModelTable.table.childSummaryLarge);
	
	return new org.vcell.util.document.BioModelInfo(version, modelRef, serialDbChildSummary, vcSoftwareVersion);
}
/**
 * This method was created in VisualAge.
 * @return java.lang.String
 */
public String getInfoSQL(User user,String extraConditions,String special) {
	
	UserTable userTable = UserTable.table;
	BioModelTable vTable = BioModelTable.table;
	SoftwareVersionTable swvTable = SoftwareVersionTable.table;
	String sql;
	Field[] f = {userTable.userid,new cbit.sql.StarField(vTable),swvTable.softwareVersion};
	Table[] t = {vTable,userTable,swvTable};
	String condition = userTable.id.getQualifiedColName() + " = " + vTable.ownerRef.getQualifiedColName() +  // links in the userTable
			           " AND " + vTable.id.getQualifiedColName() + " = " + swvTable.versionableRef.getQualifiedColName()+"(+) ";
	if (extraConditions != null && extraConditions.trim().length()>0){
		condition += " AND "+extraConditions;
	}
	sql = DatabasePolicySQL.enforceOwnershipSelect(user,f,t,condition,special,true);
	return sql;
}
/**
 * This method was created in VisualAge.
 * @return java.lang.String
 * @param key KeyValue
 * @param modelName java.lang.String
 */
public String getSQLValueList(BioModelMetaData bioModelMetaData, String serialBMChildSummary,Version version) {
	StringBuffer buffer = new StringBuffer();
	buffer.append("(");
	buffer.append(getVersionGroupSQLValue(version) + ",");
	buffer.append(bioModelMetaData.getModelKey() + ",");

	if (serialBMChildSummary==null){
		buffer.append("null,null");
	}else if (DbDriver.varchar2_CLOB_is_Varchar2_OK(serialBMChildSummary)){
		buffer.append("null"+","+DbDriver.INSERT_VARCHAR2_HERE);
	}else{
		buffer.append(DbDriver.INSERT_CLOB_HERE+","+"null");
	}
	
	buffer.append(")");
	return buffer.toString();
}

public String getPreparedStatement_BioModelReps(String conditions, int numRows){

	BioModelTable bmTable = BioModelTable.table;
	BioModelSimulationLinkTable bmsimTable = BioModelSimulationLinkTable.table;
	BioModelSimContextLinkTable bmscTable = BioModelSimContextLinkTable.table;
	GroupTable groupTable = GroupTable.table;
	UserTable userTable = UserTable.table;
	
	String subquery = 			
		"select " +
		    bmTable.id.getQualifiedColName()+", "+
		    bmTable.name.getQualifiedColName()+", "+
		    bmTable.privacy.getQualifiedColName()+", "+
		    bmTable.versionDate.getQualifiedColName()+", "+
		    bmTable.versionAnnot.getQualifiedColName()+", "+
		    bmTable.versionBranchID.getQualifiedColName()+", "+
		    bmTable.modelRef.getQualifiedColName()+", "+
		    bmTable.ownerRef.getQualifiedColName()+", "+
		    UserTable.table.userid.getQualifiedColName()+", "+
		
		   "(select '['||wm_concat("+"SQ1_"+bmsimTable.simRef.getQualifiedColName()+")||']' "+
		   "   from "+bmsimTable.getTableName()+" SQ1_"+bmsimTable.getTableName()+" "+
		   "   where SQ1_"+bmsimTable.bioModelRef.getQualifiedColName()+" = "+bmTable.id.getQualifiedColName()+") simKeys,  "+
		
		   "(select '['||wm_concat("+"SQ2_"+bmscTable.simContextRef.getQualifiedColName()+")||']' "+
		   "   from "+bmscTable.getTableName()+"  SQ2_"+bmscTable.getTableName()+" "+
		   "   where SQ2_"+bmscTable.bioModelRef.getQualifiedColName()+ " = " + bmTable.id.getQualifiedColName()+") simContextKeys,  "+
		
		   "(select '['||wm_concat(SQ3_"+groupTable.userRef.getQualifiedColName()+"||':'||SQ3_"+userTable.userid.getQualifiedColName()+")||']'  "+
	   	   "   from "+groupTable.getTableName()+" SQ3_"+groupTable.getTableName()+", "+
		   "        "+userTable.getTableName()+"  SQ3_"+userTable.getTableName()+" "+
		   "   where SQ3_"+groupTable.groupid.getQualifiedColName()+" = "+bmTable.privacy.getQualifiedColName()+" "+
		   "   and   SQ3_"+userTable.id.getQualifiedColName()+" = "+groupTable.userRef.getQualifiedColName()+" "+
		   "   and   "+bmTable.privacy.getQualifiedColName()+" > 1) groupMembers "+
		
		"from "+bmTable.getTableName()+", "+userTable.getTableName()+", "+groupTable.getTableName()+" "+
		"where "+bmTable.ownerRef.getQualifiedColName()+" = "+userTable.id.getQualifiedColName()+" "+
		"and   "+bmTable.privacy.getQualifiedColName()+" = "+groupTable.groupid.getQualifiedColName()+" "+
		"and   (("+bmTable.ownerRef.getQualifiedColName()+" =?) or ("+bmTable.privacy.getQualifiedColName()+" = 0) or ("+groupTable.userRef+" =? ))";
	
	String additionalConditionsClause = "";
	if (conditions!=null && conditions.length()>0){
		additionalConditionsClause = " and ("+conditions+")";
	}
	
	String orderByClause = "order by "+bmTable.versionDate.getQualifiedColName()+" DESC";

	// query guarantees authorized access to biomodels based on the supplied User authentication.
	String sql =  
		"select * from "+
		"(" + subquery + " " + additionalConditionsClause + " " + orderByClause + ") "+
		"where rownum <= ?";
	
	System.out.println(sql);
	return sql;
}

public void setPreparedStatement_BioModelReps(PreparedStatement stmt, User user, int numRows) throws SQLException{
	if (user == null) {
		throw new IllegalArgumentException("Improper parameters for getBioModelRepsSQL");
	}
	BigDecimal userKey = new BigDecimal(user.getID().toString());
	stmt.setBigDecimal(1, userKey);
	stmt.setBigDecimal(2, userKey);
	stmt.setInt(3, numRows);
}

public BioModelRep getBioModelRep(User user, ResultSet rset) throws IllegalArgumentException, SQLException {
	KeyValue bmKey = new KeyValue(rset.getBigDecimal(table.id.toString()));
	String name = rset.getString(table.name.toString());
	int privacy = rset.getInt(table.privacy.toString());
	Date date = getDate(rset, table.versionDate.toString());
	String annot = rset.getString(table.versionAnnot.toString());
	BigDecimal branchID = rset.getBigDecimal(table.versionBranchID.toString());
	KeyValue modelRef = new KeyValue(rset.getBigDecimal(table.modelRef.toString()));
	KeyValue ownerRef = new KeyValue(rset.getBigDecimal(table.ownerRef.toString()));
	String ownerName = rset.getString(UserTable.table.userid.toString());
	User owner = new User(ownerName,ownerRef);
	
	String simKeysString = rset.getString("simKeys");
	ArrayList<KeyValue> simKeyList = new ArrayList<KeyValue>();
	String[] simKeys = simKeysString.replace("[", "").replace("]", "").split(",");
	for (String simKey : simKeys) {
		if (simKey!=null && simKey.length()>0){
			simKeyList.add(new KeyValue(simKey));
		}
	}
	KeyValue[] simKeyArray = simKeyList.toArray(new KeyValue[0]);

	String simContextsString = rset.getString("simContextKeys");
	ArrayList<KeyValue> simContextKeyList = new ArrayList<KeyValue>();
	String[] simContextKeys = simContextsString.replace("[", "").replace("]", "").split(",");
	for (String simContextKey : simContextKeys) {
		if (simContextKey!=null && simContextKey.length()>0){
			simContextKeyList.add(new KeyValue(simContextKey));
		}
	}
	KeyValue[] simContextKeyArray = simContextKeyList.toArray(new KeyValue[0]);

	String groupMembers = rset.getString("groupMembers");
	ArrayList<User> groupUsers = new ArrayList<User>();
	String[] groupUserStrings = groupMembers.replace("[", "").replace("]", "").split(",");
	for (String groupUserString : groupUserStrings) {
		if (groupUserString!=null && groupUserString.length()>0){
			String[] groupUserTokens = groupUserString.split(":");
			KeyValue groupUserKey = new KeyValue(groupUserTokens[0]);
			String  groupUserid = groupUserTokens[1];
			groupUsers.add(new User(groupUserid,groupUserKey));
		}
	}
	User[] groupUserArray = groupUsers.toArray(new User[0]);
		
	
	return new BioModelRep(bmKey,name,privacy,groupUserArray,date,annot,branchID,modelRef,owner,simKeyArray,simContextKeyArray);
}
}
