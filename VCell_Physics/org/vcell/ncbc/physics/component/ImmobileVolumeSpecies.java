package org.vcell.ncbc.physics.component;

/**
 * Insert the type's description here.
 * Creation date: (1/6/2004 12:52:00 PM)
 * @author: Jim Schaff
 */
public class ImmobileVolumeSpecies extends VolumeSpecies {
/**
 * ResolvedVolumeSpecies constructor comment.
 */
public ImmobileVolumeSpecies(String argSpeciesContextName, VolumeLocation argVolumeLocation) {
	super(argSpeciesContextName, argVolumeLocation);
}
/**
 * Insert the method's description here.
 * Creation date: (1/14/2004 4:53:49 PM)
 * @return ncbc.physics.component.ConcentrationConnector
 */
public ConcentrationConnector getFluxConnector() {
	return (ConcentrationConnector)getConnectorByName(CONNECTOR_NAME_FLUX);
}
}
