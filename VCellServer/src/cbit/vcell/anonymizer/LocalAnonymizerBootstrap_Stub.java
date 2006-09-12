// Stub class generated by rmic, do not edit.
// Contents subject to change without notice.

package cbit.vcell.anonymizer;

import cbit.util.User;

public final class LocalAnonymizerBootstrap_Stub
    extends java.rmi.server.RemoteStub
    implements cbit.vcell.server.VCellBootstrap, java.rmi.Remote
{
    private static final long serialVersionUID = 2;
    
    private static java.lang.reflect.Method $method_getVCellConnection_0;
    private static java.lang.reflect.Method $method_getVCellServer_1;
    private static java.lang.reflect.Method $method_getVCellSoftwareVersion_2;
    
    static {
	try {
	    $method_getVCellConnection_0 = cbit.vcell.server.VCellBootstrap.class.getMethod("getVCellConnection", new java.lang.Class[] {java.lang.String.class, java.lang.String.class});
	    $method_getVCellServer_1 = cbit.vcell.server.VCellBootstrap.class.getMethod("getVCellServer", new java.lang.Class[] {User.class, java.lang.String.class});
	    $method_getVCellSoftwareVersion_2 = cbit.vcell.server.VCellBootstrap.class.getMethod("getVCellSoftwareVersion", new java.lang.Class[] {});
	} catch (java.lang.NoSuchMethodException e) {
	    throw new java.lang.NoSuchMethodError(
		"stub class initialization failed");
	}
    }
    
    // constructors
    public LocalAnonymizerBootstrap_Stub(java.rmi.server.RemoteRef ref) {
	super(ref);
    }
    
    // methods from remote interfaces
    
    // implementation of getVCellConnection(String, String)
    public cbit.vcell.server.VCellConnection getVCellConnection(java.lang.String $param_String_1, java.lang.String $param_String_2)
	throws cbit.vcell.server.AuthenticationException, cbit.util.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getVCellConnection_0, new java.lang.Object[] {$param_String_1, $param_String_2}, -2481929734604910463L);
	    return ((cbit.vcell.server.VCellConnection) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.AuthenticationException e) {
	    throw e;
	} catch (cbit.util.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getVCellServer(User, String)
    public cbit.vcell.server.VCellServer getVCellServer(User $param_User_1, java.lang.String $param_String_2)
	throws cbit.vcell.server.AuthenticationException, cbit.util.DataAccessException, cbit.vcell.server.PermissionException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getVCellServer_1, new java.lang.Object[] {$param_User_1, $param_String_2}, 829192960962989424L);
	    return ((cbit.vcell.server.VCellServer) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.AuthenticationException e) {
	    throw e;
	} catch (cbit.util.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getVCellSoftwareVersion()
    public java.lang.String getVCellSoftwareVersion()
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getVCellSoftwareVersion_2, null, 7409065355811918435L);
	    return ((java.lang.String) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
}
