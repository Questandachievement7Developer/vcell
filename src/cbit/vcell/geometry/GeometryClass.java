package cbit.vcell.geometry;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

import org.vcell.util.Matchable;
import org.vcell.util.document.KeyValue;

public interface GeometryClass extends Matchable, Serializable {
	
	public String getName();
	
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
	public KeyValue getKey();

}
