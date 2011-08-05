/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.biomodel.meta;

import java.util.Set;

public interface IdentifiableProvider {
	VCID getVCID(Identifiable identifiable);
	Identifiable getIdentifiableObject(VCID vcid);
	Set<Identifiable> getAllIdentifiables();
}
