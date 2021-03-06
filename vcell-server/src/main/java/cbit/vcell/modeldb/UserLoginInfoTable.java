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

import org.vcell.db.KeyFactory;
import org.vcell.util.TokenMangler;
import org.vcell.util.document.KeyValue;
import org.vcell.util.document.UserLoginInfo;

import cbit.sql.Field;
import cbit.sql.Field.SQLDataType;
import cbit.sql.Table;
import cbit.vcell.resource.PropertyLoader;

public class UserLoginInfoTable extends Table {
	private static final String TABLE_NAME = "vc_userlogininfo";
	public static final String REF_TYPE = "REFERENCES " + TABLE_NAME + "(" + Table.id_ColumnName + ")";

    private static final String[] userLoginInfoTableUniqueConstraintOracle =
		new String[] {
		"ulinfo_unique UNIQUE(userRef,osarch,osname,osvers,clientVers,serverVers,javavers)"};

    private static final String[] userLoginInfoTableUniqueConstraintPostgres =
		new String[] {
		"ulinfo_unique UNIQUE(userRef,osarch,osname,osvers,clientVers,serverVers,javavers)"};

    private static final int MAX_FIELD_LENGTH = 32;
    
	public final Field userRef		= new Field("userRef",		SQLDataType.integer,		"NOT NULL "+UserTable.REF_TYPE+" ON DELETE CASCADE");
	public final Field loginCount	= new Field("loginCount",	SQLDataType.number_as_integer,"NOT NULL");
	public final Field lastLogin	= new Field("lastLogin",	SQLDataType.date,			"NOT NULL");
	public final Field osarch		= new Field("osarch",		SQLDataType.varchar2_32,	"NOT NULL");
	public final Field osname		= new Field("osname",		SQLDataType.varchar2_32,	"NOT NULL");
	public final Field osvers		= new Field("osvers",		SQLDataType.varchar2_32,	"NOT NULL");
	public final Field clientVers	= new Field("clientVers",	SQLDataType.varchar2_32,	"NOT NULL");
	public final Field serverVers	= new Field("serverVers",	SQLDataType.varchar2_32,	"NOT NULL");
	public final Field javavers		= new Field("javavers",		SQLDataType.varchar2_32,	"NOT NULL");

	private final Field fields[] = {userRef,loginCount,lastLogin,osarch,osname,osvers,clientVers,serverVers,javavers};
	
	public static final UserLoginInfoTable table = new UserLoginInfoTable();

/**
 * ModelTable constructor comment.
 */
private UserLoginInfoTable() {
	super(TABLE_NAME,userLoginInfoTableUniqueConstraintOracle,userLoginInfoTableUniqueConstraintPostgres);
	addFields(fields);
}

public static String getSQLValueList(KeyValue userRef,int logincount,UserLoginInfo userLoginInfo,KeyFactory keyFactory) {
	StringBuffer buffer = new StringBuffer();
	buffer.append("(");
	buffer.append(keyFactory.nextSEQ()+",");
	buffer.append(userRef.toString()+",");
	buffer.append(logincount+",");
	buffer.append("current_timestamp"+",");
	buffer.append("'"+TokenMangler.getSQLEscapedString(userLoginInfo.getOs_arch(), MAX_FIELD_LENGTH)+"'"+",");
	buffer.append("'"+TokenMangler.getSQLEscapedString(userLoginInfo.getOs_name(), MAX_FIELD_LENGTH)+"'"+",");
	buffer.append("'"+TokenMangler.getSQLEscapedString(userLoginInfo.getOs_version(), MAX_FIELD_LENGTH)+"'"+",");
	buffer.append("'"+TokenMangler.getSQLEscapedString(getVCellSoftwareVersionClient(userLoginInfo), MAX_FIELD_LENGTH)+"'"+",");
	buffer.append("'"+TokenMangler.getSQLEscapedString(getVCellSoftwareVersionServer(), MAX_FIELD_LENGTH)+"'"+",");
	buffer.append("'"+TokenMangler.getSQLEscapedString(userLoginInfo.getJava_version(), MAX_FIELD_LENGTH)+"'");
	buffer.append(")");
	return buffer.toString();
}
public static String getSQLUpdateLoginCount(KeyValue userRef,UserLoginInfo userLoginInfo){
	return
		"UPDATE "+UserLoginInfoTable.table.getTableName()+
		" SET "+
			UserLoginInfoTable.table.loginCount + " = "+ UserLoginInfoTable.table.loginCount+" + 1"+","+
			UserLoginInfoTable.table.lastLogin + " = current_timestamp" +
		" WHERE "+
			getSQLWhereCondition(userRef,userLoginInfo);
}
public static String getSQLWhereCondition(KeyValue userRef,UserLoginInfo userLoginInfo){
	return
	UserLoginInfoTable.table.userRef.getUnqualifiedColName()+ " = "+userRef.toString()+
	" AND "+
	UserLoginInfoTable.table.osarch.getUnqualifiedColName()+ " = "+"'"+TokenMangler.getSQLEscapedString(userLoginInfo.getOs_arch(), MAX_FIELD_LENGTH)+"'"+
	" AND "+
	UserLoginInfoTable.table.osname.getUnqualifiedColName()+ " = "+"'"+TokenMangler.getSQLEscapedString(userLoginInfo.getOs_name(), MAX_FIELD_LENGTH)+"'"+
	" AND "+
	UserLoginInfoTable.table.osvers.getUnqualifiedColName()+ " = "+"'"+TokenMangler.getSQLEscapedString(userLoginInfo.getOs_version(), MAX_FIELD_LENGTH)+"'"+
	" AND "+
	UserLoginInfoTable.table.clientVers.getUnqualifiedColName()+ " = "+"'"+TokenMangler.getSQLEscapedString(getVCellSoftwareVersionClient(userLoginInfo), MAX_FIELD_LENGTH)+"'"+
	" AND "+
	UserLoginInfoTable.table.serverVers.getUnqualifiedColName()+ " = "+"'"+TokenMangler.getSQLEscapedString(getVCellSoftwareVersionServer(), MAX_FIELD_LENGTH)+"'"+
	" AND "+
	UserLoginInfoTable.table.javavers.getUnqualifiedColName()+ " = "+"'"+TokenMangler.getSQLEscapedString(userLoginInfo.getJava_version(), MAX_FIELD_LENGTH)+"'";


}
private static String getVCellSoftwareVersionServer(){
	//Whatever loaded this class is assumed to have loaded required VCell properties
	String vcellSoftwareVersionServer = System.getProperty(PropertyLoader.vcellSoftwareVersion);;
	return (vcellSoftwareVersionServer==null?"unknown":vcellSoftwareVersionServer);
}
private static String getVCellSoftwareVersionClient(UserLoginInfo userLoginInfo){
	//This information is sent over from client during login
	String vcellSoftwareVersionClient = userLoginInfo.getVCellSoftwareVersion();
	return (vcellSoftwareVersionClient==null?"unknown":vcellSoftwareVersionClient);
}
}
