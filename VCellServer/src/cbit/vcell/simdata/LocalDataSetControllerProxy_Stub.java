// Stub class generated by rmic, do not edit.
// Contents subject to change without notice.

package cbit.vcell.simdata;

public final class LocalDataSetControllerProxy_Stub
    extends java.rmi.server.RemoteStub
    implements cbit.vcell.server.DataSetController, java.rmi.Remote
{
    private static final long serialVersionUID = 2;
    
    private static java.lang.reflect.Method $method_addFunction_0;
    private static java.lang.reflect.Method $method_addFunctions_1;
    private static java.lang.reflect.Method $method_getDataIdentifiers_2;
    private static java.lang.reflect.Method $method_getDataSetTimes_3;
    private static java.lang.reflect.Method $method_getFunctions_4;
    private static java.lang.reflect.Method $method_getIsODEData_5;
    private static java.lang.reflect.Method $method_getLineScan_6;
    private static java.lang.reflect.Method $method_getLineScan_7;
    private static java.lang.reflect.Method $method_getMesh_8;
    private static java.lang.reflect.Method $method_getODEData_9;
    private static java.lang.reflect.Method $method_getParticleDataBlock_10;
    private static java.lang.reflect.Method $method_getParticleDataExists_11;
    private static java.lang.reflect.Method $method_getSimDataBlock_12;
    private static java.lang.reflect.Method $method_getTimeSeriesValues_13;
    private static java.lang.reflect.Method $method_makeRemoteFile_14;
    private static java.lang.reflect.Method $method_removeFunction_15;
    
    static {
	try {
	    $method_addFunction_0 = cbit.vcell.server.DataSetController.class.getMethod("addFunction", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class, cbit.vcell.math.AnnotatedFunction.class});
	    $method_addFunctions_1 = cbit.vcell.server.DataSetController.class.getMethod("addFunctions", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class, cbit.vcell.math.AnnotatedFunction[].class});
	    $method_getDataIdentifiers_2 = cbit.vcell.server.DataSetController.class.getMethod("getDataIdentifiers", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class});
	    $method_getDataSetTimes_3 = cbit.vcell.server.DataSetController.class.getMethod("getDataSetTimes", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class});
	    $method_getFunctions_4 = cbit.vcell.server.DataSetController.class.getMethod("getFunctions", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class});
	    $method_getIsODEData_5 = cbit.vcell.server.DataSetController.class.getMethod("getIsODEData", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class});
	    $method_getLineScan_6 = cbit.vcell.server.DataSetController.class.getMethod("getLineScan", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class, java.lang.String.class, double.class, cbit.vcell.math.CoordinateIndex.class, cbit.vcell.math.CoordinateIndex.class});
	    $method_getLineScan_7 = cbit.vcell.server.DataSetController.class.getMethod("getLineScan", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class, java.lang.String.class, double.class, SpatialSelection.class});
	    $method_getMesh_8 = cbit.vcell.server.DataSetController.class.getMethod("getMesh", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class});
	    $method_getODEData_9 = cbit.vcell.server.DataSetController.class.getMethod("getODEData", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class});
	    $method_getParticleDataBlock_10 = cbit.vcell.server.DataSetController.class.getMethod("getParticleDataBlock", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class, double.class});
	    $method_getParticleDataExists_11 = cbit.vcell.server.DataSetController.class.getMethod("getParticleDataExists", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class});
	    $method_getSimDataBlock_12 = cbit.vcell.server.DataSetController.class.getMethod("getSimDataBlock", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class, java.lang.String.class, double.class});
	    $method_getTimeSeriesValues_13 = cbit.vcell.server.DataSetController.class.getMethod("getTimeSeriesValues", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class, cbit.util.TimeSeriesJobSpec.class});
	    $method_makeRemoteFile_14 = cbit.vcell.server.DataSetController.class.getMethod("makeRemoteFile", new java.lang.Class[] {cbit.vcell.export.server.ExportSpecs.class});
	    $method_removeFunction_15 = cbit.vcell.server.DataSetController.class.getMethod("removeFunction", new java.lang.Class[] {cbit.vcell.server.VCDataIdentifier.class, cbit.vcell.math.AnnotatedFunction.class});
	} catch (java.lang.NoSuchMethodException e) {
	    throw new java.lang.NoSuchMethodError(
		"stub class initialization failed");
	}
    }
    
    // constructors
    public LocalDataSetControllerProxy_Stub(java.rmi.server.RemoteRef ref) {
	super(ref);
    }
    
    // methods from remote interfaces
    
    // implementation of addFunction(VCDataIdentifier, AnnotatedFunction)
    public void addFunction(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1, cbit.vcell.math.AnnotatedFunction $param_AnnotatedFunction_2)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    ref.invoke(this, $method_addFunction_0, new java.lang.Object[] {$param_VCDataIdentifier_1, $param_AnnotatedFunction_2}, 1772512561365875085L);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of addFunctions(VCDataIdentifier, AnnotatedFunction[])
    public void addFunctions(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1, cbit.vcell.math.AnnotatedFunction[] $param_arrayOf_AnnotatedFunction_2)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    ref.invoke(this, $method_addFunctions_1, new java.lang.Object[] {$param_VCDataIdentifier_1, $param_arrayOf_AnnotatedFunction_2}, 3763422229753356946L);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getDataIdentifiers(VCDataIdentifier)
    public cbit.vcell.simdata.DataIdentifier[] getDataIdentifiers(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getDataIdentifiers_2, new java.lang.Object[] {$param_VCDataIdentifier_1}, -2921860114544834587L);
	    return ((cbit.vcell.simdata.DataIdentifier[]) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getDataSetTimes(VCDataIdentifier)
    public double[] getDataSetTimes(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getDataSetTimes_3, new java.lang.Object[] {$param_VCDataIdentifier_1}, -3795558972337568161L);
	    return ((double[]) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getFunctions(VCDataIdentifier)
    public cbit.vcell.math.AnnotatedFunction[] getFunctions(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getFunctions_4, new java.lang.Object[] {$param_VCDataIdentifier_1}, -7534649386023357351L);
	    return ((cbit.vcell.math.AnnotatedFunction[]) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getIsODEData(VCDataIdentifier)
    public boolean getIsODEData(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getIsODEData_5, new java.lang.Object[] {$param_VCDataIdentifier_1}, 8702520843889222828L);
	    return ((java.lang.Boolean) $result).booleanValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getLineScan(VCDataIdentifier, String, double, CoordinateIndex, CoordinateIndex)
    public cbit.plot.PlotData getLineScan(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1, java.lang.String $param_String_2, double $param_double_3, cbit.vcell.math.CoordinateIndex $param_CoordinateIndex_4, cbit.vcell.math.CoordinateIndex $param_CoordinateIndex_5)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getLineScan_6, new java.lang.Object[] {$param_VCDataIdentifier_1, $param_String_2, new java.lang.Double($param_double_3), $param_CoordinateIndex_4, $param_CoordinateIndex_5}, 3716346607616230250L);
	    return ((cbit.plot.PlotData) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getLineScan(VCDataIdentifier, String, double, SpatialSelection)
    public cbit.plot.PlotData getLineScan(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1, java.lang.String $param_String_2, double $param_double_3, SpatialSelection $param_SpatialSelection_4)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getLineScan_7, new java.lang.Object[] {$param_VCDataIdentifier_1, $param_String_2, new java.lang.Double($param_double_3), $param_SpatialSelection_4}, 4693657663284950004L);
	    return ((cbit.plot.PlotData) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getMesh(VCDataIdentifier)
    public cbit.vcell.solvers.CartesianMesh getMesh(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getMesh_8, new java.lang.Object[] {$param_VCDataIdentifier_1}, 528701435120739779L);
	    return ((cbit.vcell.solvers.CartesianMesh) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getODEData(VCDataIdentifier)
    public cbit.vcell.solver.ode.ODESimData getODEData(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getODEData_9, new java.lang.Object[] {$param_VCDataIdentifier_1}, 8459143810630880411L);
	    return ((cbit.vcell.solver.ode.ODESimData) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getParticleDataBlock(VCDataIdentifier, double)
    public cbit.vcell.simdata.ParticleDataBlock getParticleDataBlock(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1, double $param_double_2)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getParticleDataBlock_10, new java.lang.Object[] {$param_VCDataIdentifier_1, new java.lang.Double($param_double_2)}, 13989751828209397L);
	    return ((cbit.vcell.simdata.ParticleDataBlock) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getParticleDataExists(VCDataIdentifier)
    public boolean getParticleDataExists(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getParticleDataExists_11, new java.lang.Object[] {$param_VCDataIdentifier_1}, 507491329244883025L);
	    return ((java.lang.Boolean) $result).booleanValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getSimDataBlock(VCDataIdentifier, String, double)
    public cbit.vcell.simdata.SimDataBlock getSimDataBlock(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1, java.lang.String $param_String_2, double $param_double_3)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getSimDataBlock_12, new java.lang.Object[] {$param_VCDataIdentifier_1, $param_String_2, new java.lang.Double($param_double_3)}, 1576570454165661977L);
	    return ((cbit.vcell.simdata.SimDataBlock) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of getTimeSeriesValues(VCDataIdentifier, TimeSeriesJobSpec)
    public cbit.util.TimeSeriesJobResults getTimeSeriesValues(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1, cbit.util.TimeSeriesJobSpec $param_TimeSeriesJobSpec_2)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_getTimeSeriesValues_13, new java.lang.Object[] {$param_VCDataIdentifier_1, $param_TimeSeriesJobSpec_2}, 5894587440704882934L);
	    return ((cbit.util.TimeSeriesJobResults) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of makeRemoteFile(ExportSpecs)
    public cbit.rmi.event.ExportEvent makeRemoteFile(cbit.vcell.export.server.ExportSpecs $param_ExportSpecs_1)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_makeRemoteFile_14, new java.lang.Object[] {$param_ExportSpecs_1}, -2093121804079289022L);
	    return ((cbit.rmi.event.ExportEvent) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of removeFunction(VCDataIdentifier, AnnotatedFunction)
    public void removeFunction(cbit.vcell.server.VCDataIdentifier $param_VCDataIdentifier_1, cbit.vcell.math.AnnotatedFunction $param_AnnotatedFunction_2)
	throws cbit.vcell.server.DataAccessException, java.rmi.RemoteException
    {
	try {
	    ref.invoke(this, $method_removeFunction_15, new java.lang.Object[] {$param_VCDataIdentifier_1, $param_AnnotatedFunction_2}, 4880148552189873128L);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (cbit.vcell.server.DataAccessException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
}
