package cbit.vcell.mapping;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.util.*;
import cbit.vcell.model.*;
import cbit.vcell.geometry.*;
import cbit.vcell.parser.*;
/**
 * This type was created in VisualAge.
 */
public class GeometryContextTest {
/**
 * This method was created in VisualAge.
 * @return cbit.vcell.mapping.GeometryContext
 */
public static GeometryContext getExample(int dim) throws Exception {

	//
	// use example model
	//
	Model model = ModelTest.getExample_Wagner();

	//
	// use point geometry (note: makes a single subVolume by default)
	//
	Geometry geo = GeometryTest.getExample(dim);
	SimulationContext simContext = new SimulationContext(model,geo);

	GeometryContext geoContext = simContext.getGeometryContext();

	//
	// manually map all features to subVolumes
	//
	Structure structures[] = geoContext.getModel().getStructures();
	for (int i=0;i<structures.length;i++){
		Structure structure = structures[i];
		if (structure instanceof Feature){
			Feature feature = (Feature)structure;
			if (feature.getName().equals("extracellular") && geo.getDimension()>0){
				geoContext.assignFeature(feature,geo.getGeometrySpec().getSubVolume("extracellular"));
			}else{
				geoContext.assignFeature(feature,geo.getGeometrySpec().getSubVolume("cytosol"));
			}
		}
	}
	
	
	return geoContext;
}
}
