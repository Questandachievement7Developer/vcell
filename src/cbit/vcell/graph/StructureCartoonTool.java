package cbit.vcell.graph;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import cbit.gui.graph.*;
import cbit.gui.graph.Shape;
import cbit.vcell.model.gui.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import cbit.vcell.model.*;
import cbit.gui.*;
import javax.swing.event.InternalFrameEvent;
import cbit.vcell.desktop.VCellTransferable;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class StructureCartoonTool extends BioCartoonTool implements java.beans.PropertyChangeListener{
	//
	private StructureCartoon structureCartoon = null;
	//
	//public boolean bMoving = false;
	//public Shape movingShape = null;
	//private Point movingPointWorld = null;
	//private Point movingOffsetWorld = null;
	private int mode = 	-1;
	private Hashtable reactionEditorHash = new Hashtable();
	private ModelParametersDialog modelParametersDialog = null;


/**
 * @param canvas cbit.vcell.graph.CartoonCanvas
 * @param cartoon cbit.vcell.graph.StructureCartoon
 * @param buttonGroup cbit.gui.ButtonGroupCivilized
 */
public StructureCartoonTool () {
	super();
}


/**
 * Insert the method's description here.
 * Creation date: (6/21/2005 5:41:56 PM)
 * @param bDispose boolean
 */
private void disposeReactionCartoonEditorDialog(ReactionCartoonEditorDialog rced) {

	cbit.vcell.client.DocumentWindowManager.close(rced,getJDesktopPane());
	//rced.dispose();
	
	//
	//The following added to force ReactionCartoon to cleanup its listeners
	//otherwise
	//1.  ReactionCartoon could not be garbage collected.  Many "phantom" copies would accumulate from showing ReactionCartoonEditorPanel
	//2.  Erroneous (deleted,renamed) object references in "phantom" ReactionCartoons would interact with
	//    listener callbacks and throw exceptions.
	//
	rced.cleanupOnClose();
}


/**
 * Insert the method's description here.
 * Creation date: (1/31/2003 4:57:53 PM)
 * @param e java.lang.Exception
 */
private void generateErrorDialog(Exception e,int x,int y) {
	
	System.out.println("CartoonTool.mouseClicked: uncaught exception");
	e.printStackTrace(System.out);
	Point canvasLoc = getGraphPane().getLocationOnScreen();
	canvasLoc.x += x;
	canvasLoc.y += y;
	cbit.vcell.client.PopupGenerator.showErrorDialog(e.getMessage());
}


/**
 * Insert the method's description here.
 * Creation date: (9/9/2002 10:26:00 AM)
 * @return cbit.vcell.graph.GraphModel
 */
public GraphModel getGraphModel() {
	return structureCartoon;
}


/**
 * Insert the method's description here.
 * Creation date: (9/9/2002 10:26:00 AM)
 * @return cbit.vcell.graph.GraphModel
 */
public StructureCartoon getStructureCartoon() {
	return (StructureCartoon)getGraphModel();
}


/**
 * Insert the method's description here.
 * Creation date: (9/17/2002 3:56:54 PM)
 * @param shape cbit.vcell.graph.Shape
 * @param menuAction java.lang.String
 */
protected void menuAction(Shape shape, String menuAction) {
	//
	if(shape == null){return;}
	//
	if(menuAction.equals(COPY_MENU_ACTION)){
		if (shape instanceof SpeciesContextShape){
			Species species = ((SpeciesContextShape)shape).getSpeciesContext().getSpecies();
			VCellTransferable.sendToClipboard(species);
		}		
	}else if (menuAction.equals(PASTE_MENU_ACTION) || menuAction.equals(PASTE_NEW_MENU_ACTION)){
		if (shape instanceof StructureShape){
			Species species = (Species)VCellTransferable.getFromClipboard(VCellTransferable.SPECIES_FLAVOR);
			IdentityHashMap<Species, Species> speciesHash = new IdentityHashMap<Species, Species>();
			if(species != null){
				pasteSpecies(species,getStructureCartoon().getModel(),((StructureShape)shape).getStructure(),menuAction.equals(PASTE_NEW_MENU_ACTION), speciesHash);
			}
		}
	}else if (menuAction.equals(PROPERTIES_MENU_ACTION)){
		if (shape instanceof FeatureShape){
			//
			// showFeaturePropertyDialog is invoked in two modes:
			//
			// 1) parent!=null and child==null
			//      upon ok, it adds a new feature to the supplied parent.
			//
			// 2) parent==null and child!=null
			//      upon ok, edits the feature name
			//
			showFeaturePropertiesDialog(getGraphPane(),(getStructureCartoon().getModel() == null?null:getStructureCartoon().getModel()),null,((FeatureShape)shape).getFeature(),shape.getLocationOnScreen(getGraphPane().getLocationOnScreen()));
		}else if (shape instanceof MembraneShape){
			showMembranePropertiesDialog(getGraphPane(),((MembraneShape)shape).getMembrane(),shape.getLocationOnScreen(getGraphPane().getLocationOnScreen()));
		}else if (shape instanceof SpeciesContextShape){
			showEditSpeciesDialog(getGraphPane(),((SpeciesContextShape)shape).getSpeciesContext(),shape.getLocationOnScreen(getGraphPane().getLocationOnScreen()));
		}
			
	}else if (menuAction.equals(SHOW_PARAMETERS_MENU_ACTION)){
		if (shape instanceof FeatureShape || shape instanceof MembraneShape){
			showParametersDialog();
		}
			
	}else if (menuAction.equals(ADD_GLOBAL_PARAM_MENU_ACTION)){
		if (shape instanceof FeatureShape || shape instanceof MembraneShape){
			showCreateGlobalParamDialog(getGraphPane(), getStructureCartoon().getModel(), shape.getLocationOnScreen(getGraphPane().getLocationOnScreen()));
		}
			
	}else if(menuAction.equals(ADD_SPECIES_MENU_ACTION)){
		if(shape instanceof StructureShape){
			showCreateSpeciesContextDialog(getGraphPane(),getStructureCartoon().getModel(),((StructureShape)shape).getStructure(),shape.getLocationOnScreen(getGraphPane().getLocationOnScreen()),null);
		}
		
	}else if(menuAction.equals(ADD_FEATURE_MENU_ACTION)){
		try{
			if (shape instanceof FeatureShape){
				showFeaturePropertiesDialog(getGraphPane(),(getStructureCartoon().getModel() == null?null:getStructureCartoon().getModel()),((FeatureShape)shape).getFeature(),null,shape.getLocationOnScreen(getGraphPane().getLocationOnScreen()));
			}
		}catch(Exception e){
			generateErrorDialog(e,0,0);
		}
		
	}
	else if (menuAction.equals(DELETE_MENU_ACTION) || menuAction.equals(CUT_MENU_ACTION)){
		try {
			if (shape instanceof FeatureShape && menuAction.equals(DELETE_MENU_ACTION)){
				getStructureCartoon().getModel().removeFeature(((FeatureShape)shape).getFeature());
			}
			else if (shape instanceof SpeciesContextShape){
				getStructureCartoon().getModel().removeSpeciesContext(((SpeciesContextShape)shape).getSpeciesContext());
				if (menuAction.equals(CUT_MENU_ACTION)){
					VCellTransferable.sendToClipboard(((SpeciesContextShape)shape).getSpeciesContext().getSpecies());
				}
			}
		}catch (Throwable e){
			cbit.vcell.client.PopupGenerator.showErrorDialog(e.getMessage());
		}
		
	}else if (menuAction.equals(REACTIONS_MENU_ACTION)){
		if (shape instanceof StructureShape){
			showReactionCartoonEditorPanel((StructureShape)shape);
		}
		
	}else if (menuAction.equals(HIGH_RES_MENU_ACTION) || menuAction.equals(MED_RES_MENU_ACTION) ||
			   menuAction.equals(LOW_RES_MENU_ACTION)) { 
		try {
			String resType = null;
			if (menuAction.equals(HIGH_RES_MENU_ACTION)) {
				resType = cbit.vcell.publish.ITextWriter.HIGH_RESOLUTION;
			} else if (menuAction.equals(MED_RES_MENU_ACTION)) {
				resType = cbit.vcell.publish.ITextWriter.MEDIUM_RESOLUTION;
			} else if (menuAction.equals(LOW_RES_MENU_ACTION)) {
				resType = cbit.vcell.publish.ITextWriter.LOW_RESOLUTION;
			}
			if(shape instanceof StructureShape){
				showSaveStructureImageDialog(((StructureShape)shape).getModel(), resType);
			}
		} catch(Exception e) {
			cbit.vcell.client.PopupGenerator.showErrorDialog(e.getMessage());
		}
	}else if (menuAction.equals(MOVE_MENU_ACTION)){
		if (shape instanceof FeatureShape){
			showMoveDialog((FeatureShape)shape);
		}
	}else{
		//
		// default action is to ignore
		//
		System.out.println("unsupported menu action '"+menuAction+"' on shape '"+shape+"'");
	}

}


/**
 * This method was created by a SmartGuide.
 * @param event java.awt.event.MouseEvent
 */
public void mouseClicked(java.awt.event.MouseEvent event) {
	//
	if(getStructureCartoon() == null){return;}
	//
	try {
		Point worldPoint = screenToWorld(event.getPoint());
		//Shape pickedShape = getStructureCartoon().pickWorld(worldPoint);
		//
		// if right mouse button, then do popup menu
		//
		if ((event.getModifiers() & (MouseEvent.BUTTON2_MASK | MouseEvent.BUTTON3_MASK)) != 0){
			return;
		}
		switch (mode) {
			case SELECT_MODE: {
				if (event.getClickCount()==2){
					Shape selectedShape = getStructureCartoon().getSelectedShape();
					if (selectedShape != null){
						menuAction(selectedShape,PROPERTIES_MENU_ACTION);
						//if(selectedShape instanceof SpeciesContextShape){
							//showEditSpeciesDialog(((SpeciesContextShape)selectedShape).getSpeciesContext(),worldPoint);
						//}else if(selectedShape instanceof SimpleReactionShape){
							//showSimpleReactionPropertiesDialog((SimpleReactionShape)selectedShape,worldPoint);
						//}else if(selectedShape instanceof FluxReactionShape){
							//showFluxReactionPropertiesDialog((FluxReactionShape)selectedShape,worldPoint);
						//}else if(selectedShape instanceof ProductShape){
							//showProductPropertiesDialog((ProductShape)selectedShape,worldPoint);
						//}else if(selectedShape instanceof ReactantShape){
							//showReactantPropertiesDialog((ReactantShape)selectedShape,worldPoint);
						//}
						//selectedShape.showPropertiesDialog(desktop, selectedShape.getLocationOnScreen());
					}
				}
				//Shape selectedShape = getStructureCartoon().getSelectedShape();
				
				//if (event.getClickCount()==2){
					//selectEvent(event.getX(),event.getY());
					//if (selectedShape != null){
						//selectedShape.showPropertiesDialog(desktop, selectedShape.getLocationOnScreen());
					//}
				//}else{					
					//selectEventFromWorld(worldPoint);
				//}	
				break;		
			}	
			case FEATURE_MODE: {
				menuAction(getStructureCartoon().getSelectedShape(),ADD_FEATURE_MENU_ACTION);
				//createFeature(pickedShape);
				//String newFeatureName = getStructureCartoon().getModel().getFreeFeatureName();
				//String newMembraneName = getStructureCartoon().getModel().getFreeMembraneName();
				//if (pickedShape instanceof FeatureShape){
				//	getStructureCartoon().getModel().addFeature(newFeatureName,((FeatureShape)pickedShape).getFeature(),newMembraneName);
				//}else if (pickedShape instanceof MembraneShape){
				//	throw new Exception("cannot add new structure (compartment) to a membrane");
				//}else if (pickedShape==null){
				//	getStructureCartoon().getModel().addFeature(newFeatureName,null,null);
				//}
				setMode(SELECT_MODE);
				//Feature feature = (Feature)getStructureCartoon().getModel().getStructure(newFeatureName);
				//Shape shape = getStructureCartoon().getShapeFromModelObject(feature);
				//showFeaturePropertiesDialog((FeatureShape)shape,shape.getLocationOnScreen());
				break;
			}	
			case SPECIES_MODE: {
				menuAction(getStructureCartoon().getSelectedShape(),ADD_SPECIES_MENU_ACTION);
				//if (pickedShape instanceof StructureShape){
					//showCreateSpeciesContextDialog(getStructureCartoon().getModel(),((StructureShape)pickedShape).getStructure(),pickedShape.getLocationOnScreen(getGraphPane().getLocationOnScreen()));
				//}
			}
			default:
				break;
		}	
	}catch (Exception e){
		generateErrorDialog(e,event.getX(),event.getY());
		//System.out.println("CartoonTool.mouseClicked: uncaught exception");
		//e.printStackTrace(System.out);
		//Point canvasLoc = graphPane.getLocationOnScreen();
		//canvasLoc.x += event.getX();
		//canvasLoc.y += event.getY();
		//javax.swing.JOptionPane.showMessageDialog(cbit.vcell.desktop.controls.ClientDisplayManager.getClientDisplayManager().getMainClientWindow(),
		//	e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
	}				
}


/**
 * This method was created by a SmartGuide.
 * @param event java.awt.event.MouseEvent
 */
public void mouseDragged(java.awt.event.MouseEvent event) {
//	if(event.getID() != DragFix.QUEUED){
//	 	dragFix.queueEvent(event);
//	 	return;
//   }	
	if ((event.getModifiers() & (MouseEvent.BUTTON2_MASK | MouseEvent.BUTTON3_MASK)) != 0){
		return;
	}
	//try {
		//switch (mode){
			//case SELECT_MODE: {
				//java.awt.Point worldPoint = screenToWorld(event.getX(),event.getY());
				//if (bMoving){
					//getGraphPane().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					////graphPane.repaint();
					//Graphics2D g = (Graphics2D)getGraphPane().getGraphics();
					//java.awt.geom.AffineTransform oldTransform = g.getTransform();
					//g.scale(0.01*getStructureCartoon().getZoomPercent(),0.01*getStructureCartoon().getZoomPercent());
					//g.setXORMode(Color.white);
					//movingShape.setLocation(movingPointWorld);
					//movingShape.paint(g,0,0);
					//movingPointWorld = new Point(worldPoint.x-movingOffsetWorld.x,worldPoint.y-movingOffsetWorld.y);
					//movingShape.setLocation(movingPointWorld);
					//movingShape.paint(g,0,0);
					//g.setTransform(oldTransform);
				//}else{
					//if (movingShape != null){
						//return;
					//}	
					//movingShape = getStructureCartoon().pickWorld(worldPoint);
					//if (movingShape!=null){
						//if (movingShape.getParent()!=null){
							//if (movingShape instanceof FeatureShape){
								//movingShape = (MembraneShape)movingShape.getParent();
								//if (movingShape.getParent()!=null && movingShape.getParent() instanceof FeatureShape){
									//movingShape.getParent().removeChild(movingShape);
								//}
								//bMoving=true;
								//movingPointWorld = movingShape.getAbsLocation();
								//movingOffsetWorld = new Point(worldPoint.x-movingPointWorld.x,worldPoint.y-movingPointWorld.y);
							//}		
						//}	
					//}		
				//}		
				//break;
			//}
			//default: {
				//break;
			//}
		//}		
	//}catch (Exception e){
		//System.out.println("CartoonTool.mouseDragged: uncaught exception");
		//e.printStackTrace(System.out);
	//}			
}


/**
 * This method was created by a SmartGuide.
 * @param event java.awt.event.MouseEvent
 */
public void mousePressed(java.awt.event.MouseEvent event) {
	//
	if(getStructureCartoon() == null){return;}
	//
	try {
		int x = event.getX();
		int y = event.getY();
		java.awt.Point worldPoint = screenToWorld(x,y);
		//
		//Always select with MousePress
		//
		if(mode == SELECT_MODE || (event.getModifiers() & MouseEvent.BUTTON1_MASK) != 0){
			selectEventFromWorld(worldPoint);
		}
		//
		//If mouse popupMenu event, popup menu
		if (event.isPopupTrigger() && mode == SELECT_MODE){
			popupMenu(getStructureCartoon().getSelectedShape(),event.getX(),event.getY());
			return;
		}
	}catch (Exception e){
		System.out.println("StructureCartoonTool.mousePressed: uncaught exception");
		e.printStackTrace(System.out);
	}				
}


/**
 * This method was created by a SmartGuide.
 * @param event java.awt.event.MouseEvent
 */
public void mouseReleased(java.awt.event.MouseEvent event) {
	try {
		//Picked shape
		int x = event.getX();
		int y = event.getY();
		java.awt.Point worldPoint = screenToWorld(x,y);
		//Shape pickedShape = getStructureCartoon().pickWorld(worldPoint);
		//
		// if mouse popupMenu event, popup menu
		//
		if (event.isPopupTrigger() && 
			//!bMoving && 
			mode == SELECT_MODE){
			//selectEventFromWorld(worldPoint);
			Shape pickedShape = getStructureCartoon().pickWorld(worldPoint);
			if(pickedShape == getStructureCartoon().getSelectedShape()){
				popupMenu(getStructureCartoon().getSelectedShape(),event.getX(),event.getY());
			}
			return;
		}
		//
		//if ((event.getModifiers() & (MouseEvent.BUTTON2_MASK | MouseEvent.BUTTON3_MASK)) != 0){
			//return;
		//}
		////
		//// else select or move
		////
		//getGraphPane().setCursor(Cursor.getDefaultCursor());
		//if (mode==SELECT_MODE){
			//if (bMoving){
				//if (!(movingShape instanceof MembraneShape)){
					//throw new Exception("expected movingShape to be of type MembraneShape");
				//}
				//Shape newParent = getStructureCartoon().pickWorld(worldPoint);
				//if (newParent!=null){
					//// make sure that new parent is a featureShape other than the one that is being moved
					//if (newParent!=movingShape				&& 
						 //newParent instanceof FeatureShape	&& 
						 //newParent.getParent()!=movingShape	&&	
						 //!movingShape.isDescendant(newParent)) {
						//FeatureShape fs = (FeatureShape)newParent;
						//((MembraneShape)movingShape).getMembrane().setOutsideFeature(fs.getFeature());
						//getStructureCartoon().refreshAll();
					//}
				//}
				//getStructureCartoon().notifyChangeEvent();
////				dragFix.queueEvent(null);
				//getGraphPane().repaint();
			//}	
		//}
		////
		//bMoving=false;
		//movingShape=null;
		////
	}catch (Exception e){
		System.out.println("CartoonTool.mouseReleased: uncaught exception");
		e.printStackTrace(System.out);
	}			
		
}


	/**
	 * This method gets called when a bound property is changed.
	 * @param evt A PropertyChangeEvent object describing the event source 
	 *   	and the property that has changed.
	 */
public void propertyChange(java.beans.PropertyChangeEvent evt) {

	if((evt.getSource() == (StructureCartoon)getGraphModel() && evt.getPropertyName().equals("model")) ||
		(evt.getSource() == ((StructureCartoon)getGraphModel()).getModel() && evt.getPropertyName().equals("structures"))
	){
		//The Model (or the Structures therein) associated with our StructureCartoon have changed
		//dispose of all ReactionCartoonEditors because they have references to
		//the wrong Model or the structures don't match the ContainerShape heirarchy
		Iterator iter = reactionEditorHash.values().iterator();
		while(iter.hasNext()){
			disposeReactionCartoonEditorDialog((ReactionCartoonEditorDialog)iter.next());
		}
		reactionEditorHash.clear();
		// clear the model in ModelParametersPanel and ProblemsPanel thro' ModelParametersDialog
		if (modelParametersDialog != null) {
			modelParametersDialog.cleanupOnClose();
		}

		if(evt.getSource() == (StructureCartoon)getGraphModel() && evt.getPropertyName().equals("model")){
			//configure Model listeners for structure changes
			if(evt.getOldValue() != null){
				((Model)evt.getOldValue()).removePropertyChangeListener(this);
			}
			if(evt.getNewValue() != null){
				((Model)evt.getNewValue()).removePropertyChangeListener(this);
				((Model)evt.getNewValue()).addPropertyChangeListener(this);
			}
			if (modelParametersDialog != null && modelParametersDialog.getModelParametersPanel() != null &&
					(modelParametersDialog.getModelParametersPanel().getModel() != ((StructureCartoon)getGraphModel()).getModel())) {
				modelParametersDialog.init(((StructureCartoon)getGraphModel()).getModel());
			}
		}
		
		
		
	}

	//if(evt.getSource() == ((StructureCartoon)getGraphModel()).getModel()){
		//if(evt.getPropertyName().equals("structures")){
			////The Structures in the Model associated with our
			////StructureCartoon have changed (removed  or heirarchy changed)
			////If any existing ReactionCartoonEditor is associated with a changed Structure
			////then dispose of that Editor
			//Iterator iter = reactionEditorHash.keySet().iterator();
			//while(iter.hasNext()){
				//Structure recpStruct = (Structure)iter.next();
				//Structure modelStruct = ((Model)evt.getSource()).getStructure(recpStruct.getName());
				//if(modelStruct == null ||
					//modelStruct != recpStruct ||
					//modelStruct.getParentStructure() != recpStruct.getParentStructure() ||
					//(modelStruct instanceof Membrane && ((Membrane)modelStruct).getInsideFeature() != ((Membrane)recpStruct).getInsideFeature()) ||
					//(modelStruct instanceof Membrane && ((Membrane)modelStruct).getOutsideFeature() != ((Membrane)recpStruct).getOutsideFeature())
					//){
					//disposeReactionCartoonEditorDialog((ReactionCartoonEditorDialog)reactionEditorHash.get(recpStruct));
					//reactionEditorHash.remove(recpStruct);
				//}
			//}
		//}
	//}
	
}


/**
 * This method was created by a SmartGuide.
 * @param x int
 * @param y int
 */
private void selectEventFromWorld(Point worldPoint) {
	//
	if(getStructureCartoon() == null){return;}
	//Only 1 thing at a time can be selected in StructureCartoon
	getStructureCartoon().clearSelection();
	Shape pickedShape = getStructureCartoon().pickWorld(worldPoint);
	//	
	if (pickedShape == null){return;}
	//
	getStructureCartoon().select(pickedShape);
}


/**
 * Insert the method's description here.
 * Creation date: (7/14/00 11:39:38 AM)
 * @param newCartoon cbit.vcell.graph.StructureCartoon
 */
public void setGraphModel(StructureCartoon newCartoon) {
	
	if(structureCartoon != null){
		structureCartoon.removePropertyChangeListener(this);
		if(structureCartoon.getModel() != null){
			structureCartoon.getModel().removePropertyChangeListener(this);
		}
	}
	
	structureCartoon = newCartoon;
	
	if(structureCartoon != null){
		structureCartoon.addPropertyChangeListener(this);//Listen for BioModel change
		if(structureCartoon.getModel() != null){
			structureCartoon.getModel().addPropertyChangeListener(this);//Listen for structure change
		}
	}
}


/**
 * Insert the method's description here.
 * Creation date: (9/17/2002 4:04:32 PM)
 * @return boolean
 * @param actionString java.lang.String
 */
protected boolean shapeHasMenuAction(Shape shape, java.lang.String menuAction) {
	//
	// all structures (features and membranes) can edit properties and reactions
	//
	if (shape instanceof StructureShape){
		if (menuAction.equals(PROPERTIES_MENU_ACTION) || 
			menuAction.equals(REACTIONS_MENU_ACTION) ||
			menuAction.equals(PASTE_MENU_ACTION) ||
			menuAction.equals(PASTE_NEW_MENU_ACTION) ||
			menuAction.equals(ADD_SPECIES_MENU_ACTION) ||
			menuAction.equals(ADD_GLOBAL_PARAM_MENU_ACTION) ||
			menuAction.equals(SHOW_PARAMETERS_MENU_ACTION) ||
			menuAction.equals(HIGH_RES_MENU_ACTION) || 
			menuAction.equals(MED_RES_MENU_ACTION) ||
			menuAction.equals(LOW_RES_MENU_ACTION)){
			return true;
		}
	}
	//
	// only features should be deleted (not membranes).
	//
	if (shape instanceof FeatureShape){
		if (menuAction.equals(DELETE_MENU_ACTION) || 
			menuAction.equals(ADD_FEATURE_MENU_ACTION) ||
			menuAction.equals(MOVE_MENU_ACTION))
		{
			return true;
		}
	}
	//
	// speciesContext's may be deleted or edited or species edited or copied.
	//
	if (shape instanceof SpeciesContextShape){
		if (menuAction.equals(CUT_MENU_ACTION) || 
			menuAction.equals(PROPERTIES_MENU_ACTION) ||
			menuAction.equals(COPY_MENU_ACTION)){
			return true;
		}
	}
	return false;
}


/**
 * Insert the method's description here.
 * Creation date: (5/9/2003 9:11:21 AM)
 * @return boolean
 * @param actionString java.lang.String
 */
protected boolean shapeHasMenuActionEnabled(Shape shape, java.lang.String menuAction) {
	
	//Paste if there is a species on the system clipboard and (it doesn't exist in structure || you are PASTE_NEW)
	if (shape instanceof StructureShape){
		boolean bPasteNew = menuAction.equals(PASTE_NEW_MENU_ACTION);
		boolean bPaste = menuAction.equals(PASTE_MENU_ACTION);
		if(bPaste || bPasteNew){
			Species species = (Species)VCellTransferable.getFromClipboard(VCellTransferable.SPECIES_FLAVOR);
			if(species == null){
				return false;
			}
			if(getStructureCartoon().getModel().contains(species)) {
				if (getStructureCartoon().getModel().getSpeciesContext(species,((StructureShape)shape).getStructure()) != null) {
					return bPasteNew ? true : false;
				} else {
					return bPasteNew ? false : true;
				}
			} else {
				return bPasteNew ? false : true;
			}
		}
	}
	//Is Move valid
	if(shape instanceof FeatureShape){
		if(menuAction.equals(MOVE_MENU_ACTION)){
			Feature[] featureArr = getStructureCartoon().getModel().getValidDestinationsForMovingFeature(((FeatureShape)shape).getFeature());
			if(featureArr == null || featureArr.length == 0){
				return false;
			}
		}
	}
	//
	return true;
}


/**
 * Insert the method's description here.
 * Creation date: (6/23/2005 12:59:55 PM)
 * @param featureShape cbit.vcell.graph.FeatureShape
 */
private void showMoveDialog(FeatureShape featureShape) {

	try{
		Feature[] validDestinations = featureShape.getModel().getValidDestinationsForMovingFeature(featureShape.getFeature());
		if(validDestinations == null || validDestinations.length == 0){
			cbit.vcell.client.PopupGenerator.showErrorDialog("No valid Destinations for Feature '"+featureShape.getFeature().getName()+"'");
		}else{
			String[] destinations = new String[validDestinations.length];
			for(int i=0;i<validDestinations.length;i+= 1){
				destinations[i] = validDestinations[i].getName();
			}
			String featureSelection = (String)cbit.vcell.client.PopupGenerator.showListDialog(
				getJDesktopPane(),
				destinations,
				"Select destination for Moving Feature '"+featureShape.getFeature().getName()+"'");

			if(featureSelection == null){
				return;
			}

			Feature finalDestination = (Feature)featureShape.getModel().getStructure(featureSelection);
			if(finalDestination == null){
				cbit.vcell.client.PopupGenerator.showErrorDialog("Feature with name "+featureSelection+" not found in model");
				return;
			}
			SpeciesContext[] neededSCArr = featureShape.getModel().getSpeciesContextsNeededByMovingMembrane(featureShape.getFeature().getMembrane());
			if(neededSCArr != null){
				String message = "";
				for(int i=0;i<neededSCArr.length;i+= 1){
					if(featureShape.getModel().getSpeciesContext(neededSCArr[i].getSpecies(),finalDestination) == null){
						if(message.length() > 0){message+= "\n";}
						message+= "     "+neededSCArr[i].getSpecies().getCommonName();
					}
				}
				if(message.length() > 0){
					String result =
						cbit.vcell.client.PopupGenerator.showWarningDialog(getJDesktopPane(),
							"The following species must be copied to destination '"+finalDestination.getName()+"'\n"+
							"because reactions of moving Membrane'"+featureShape.getFeature().getMembrane().getName()+"' need them\n"+message,new String[] {"OK","Cancel"},"OK");
					if(result.equals("Cancel")){
						return;
					}
				}
			}

			Feature parentOfMoving = (Feature)featureShape.getFeature().getMembrane().getParentStructure();
			featureShape.getModel().moveFeature(featureShape.getFeature(),finalDestination);

			//Ask if cleanup wanted on SpeciesContexts of parent of moving membrane that have no other references
			Vector cleanupWantedV = new Vector();
			if(neededSCArr != null && neededSCArr.length > 0){
				for(int i=0;i<neededSCArr.length;i+= 1){
					boolean bFound = false;
					ReactionStep[] reactionArr = featureShape.getModel().getReactionSteps();
					if(reactionArr != null){
						for(int j=0;j<reactionArr.length;j+= 1){
							if((reactionArr[j].getStructure() == parentOfMoving ||
								reactionArr[j].getStructure() == parentOfMoving.getMembrane()) &&
								reactionArr[j].getReactionParticipants(neededSCArr[i]).length > 0)
							{
								bFound = true;
								break;
							}
						}
					}
					if(!bFound){
						cleanupWantedV.add(neededSCArr[i]);
					}
				}
			}
			if(cleanupWantedV.size() > 0){
				String message = "";
				for(int i=0;i<cleanupWantedV.size();i+= 1){
					if(message.length() > 0){message+= "\n";}
					message+= "     "+((SpeciesContext)cleanupWantedV.elementAt(i)).getName();
				}
				if(message.length() > 0){
					String result =
						cbit.vcell.client.PopupGenerator.showWarningDialog(getJDesktopPane(),
							"The following Species from Feature '"+parentOfMoving.getName()+"' that were used by\n"+
							"moved membrane '"+featureShape.getFeature().getMembrane().getName()+"' no longer have references\n"+
							"Should the Species be deleted?\n"+
							message,
							new String[] {cbit.vcell.client.UserMessage.OPTION_YES,cbit.vcell.client.UserMessage.OPTION_NO},
							cbit.vcell.client.UserMessage.OPTION_NO);
					if(result.equals(cbit.vcell.client.UserMessage.OPTION_YES)){
						for(int i=0;i<cleanupWantedV.size();i+= 1){
							try{
								featureShape.getModel().removeSpeciesContext((SpeciesContext)cleanupWantedV.elementAt(i));
							}catch(Throwable e){
								System.out.println(e.getClass()+" "+e.getMessage());
							}
						}
					}
				}
			}
		}
	}catch(Throwable e){
		cbit.vcell.client.PopupGenerator.showErrorDialog(e.getClass().getName()+"\n"+e.getMessage());
	}
}

/**
 * showParametersDialog : displays all the parameters used in the model (mainly reaction parameters)
 * @param frame java.awt.Frame
 */
public void showParametersDialog() {
	if(getGraphModel() == null || getDocumentManager() == null || getJDesktopPane() == null){
		return;
	}
	
	if(modelParametersDialog == null){
		modelParametersDialog = new ModelParametersDialog();
		modelParametersDialog.setIconifiable(true);
		modelParametersDialog.init(((StructureCartoon)getGraphModel()).getModel());
		
		cbit.util.BeanUtils.centerOnComponent(modelParametersDialog, getJDesktopPane());
	}

	cbit.vcell.client.DocumentWindowManager.showFrame(modelParametersDialog,getJDesktopPane());
}

/**
 * @param frame java.awt.Frame
 */
public void showReactionCartoonEditorPanel(final StructureShape structureShape) {
	if(getGraphModel() == null || getDocumentManager() == null || getJDesktopPane() == null){
		return;
	}
	//
	//See propertyChange method for related code that closes ReactionEditorCartoonDialog when appropriate
	//
	ReactionCartoonEditorDialog rced = (ReactionCartoonEditorDialog)reactionEditorHash.get(structureShape.getStructure());
	if(rced == null){
		final ReactionCartoonEditorDialog reactionCartoonEditorDialog = new ReactionCartoonEditorDialog();
		reactionCartoonEditorDialog.setIconifiable(true);
		reactionCartoonEditorDialog.init(structureShape.getModel(),structureShape.getStructure(),getDocumentManager());
		
		reactionEditorHash.put(structureShape.getStructure(),reactionCartoonEditorDialog);
		rced = reactionCartoonEditorDialog;
		cbit.util.BeanUtils.centerOnComponent(rced, getJDesktopPane());
		rced.setLocation(rced.getLocation().x + reactionEditorHash.size() * 15, rced.getLocation().y + reactionEditorHash.size() * 15);
	}

	cbit.vcell.client.DocumentWindowManager.showFrame(rced,getJDesktopPane());
}


	//TO DO: allow user preferences for directory selection. 
	public void showSaveStructureImageDialog(Model model, String resLevel) throws Exception {

		if (model == null) {             //or throw exception?
			System.err.println("Insufficient params for generating structures image.");
			return;
		}
		if (resLevel == null) {                                //default resolution.
			resLevel = cbit.vcell.publish.ITextWriter.HIGH_RESOLUTION;
		}
		System.out.println("Processing save as Image request for: " + model.getName() + "(" + resLevel + ")");
		//set file filter
		cbit.util.SimpleFilenameFilter gifFilter = new cbit.util.SimpleFilenameFilter("gif");
		cbit.vcell.client.server.ClientServerManager csm = (cbit.vcell.client.server.ClientServerManager)getDocumentManager().getSessionManager();
		cbit.vcell.client.server.UserPreferences userPref = csm.getUserPreferences();
		String defaultPath = userPref.getGenPref(cbit.vcell.client.server.UserPreferences.GENERAL_LAST_PATH_USED);
		cbit.gui.VCFileChooser fileChooser = new cbit.gui.VCFileChooser(defaultPath);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.addChoosableFileFilter(gifFilter);
		final java.io.File defaultFile = new java.io.File(model.getName() + ".gif");
		fileChooser.setSelectedFile(defaultFile);
		fileChooser.setDialogTitle("Save Image As...");
		//a hack to fix the jdk 1.2 problem (?) of losing the selected file name once the user changes the directory.
		class FileChooserFix implements java.beans.PropertyChangeListener {
			public void propertyChange(java.beans.PropertyChangeEvent ev) {    
				JFileChooser chooser = (JFileChooser)ev.getSource();       
				if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(ev.getPropertyName())) {      
					java.io.File directory = (java.io.File)ev.getNewValue(); 
					chooser.setSelectedFile(defaultFile);     
				}  
			} 
		}
		fileChooser.addPropertyChangeListener(new FileChooserFix());
		//process user input
		if (fileChooser.showSaveDialog(getDialogOwner(getGraphPane())) == JFileChooser.APPROVE_OPTION) {
			java.io.File selectedFile = fileChooser.getSelectedFile();
			if (selectedFile != null) {
				if (selectedFile.exists()) {
					int question = javax.swing.JOptionPane.showConfirmDialog(getDialogOwner(getGraphPane()), 
																			 "Overwrite file: " + selectedFile.getPath() + "?");
					if (question == javax.swing.JOptionPane.NO_OPTION || question == javax.swing.JOptionPane.CANCEL_OPTION) {
						return;
					}
				}
				//System.out.println("Saving reactions image to file: " + selectedFile.toString());
				getDocumentManager().generateStructureImage(model, resLevel, new java.io.FileOutputStream(selectedFile));
				//reset the user preference for the default path, if needed.
		        String newPath = selectedFile.getParent();
		        if (!newPath.equals(defaultPath)) {
					userPref.setGenPref(cbit.vcell.client.server.UserPreferences.GENERAL_LAST_PATH_USED, newPath);
		        }
			} else {
				//throw cbit.vcell.client.task.UserCancelException.CANCEL_FILE_SELECTION;
			}
		} else {
			//throw cbit.vcell.client.task.UserCancelException.CANCEL_FILE_SELECTION;            //best available
		}
	}


/**
 * This method was created in VisualAge.
 * @param mode int
 */
public void updateMode(int newMode) {
	if (newMode==mode){
		return;
	}
	//bMoving = false;
	//movingShape = null;
	if(getStructureCartoon() != null){
		getStructureCartoon().clearSelection();
	}
	this.mode = newMode;
	if(getGraphPane() != null){
		switch (mode){
			case FEATURE_MODE:{
				getGraphPane().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				break;
			}
			case SPECIES_MODE:{
				getGraphPane().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				break;
			}
			case SELECT_MODE:{
				getGraphPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				break;
			}
			default:{
				System.out.println("ERROR: mode " + newMode + "not defined");
				break;
			}
		}
	}
	return;
}
}