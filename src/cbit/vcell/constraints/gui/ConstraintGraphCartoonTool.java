/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.constraints.gui;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JViewport;

import org.vcell.util.BeanUtils;
import org.vcell.util.gui.DialogUtils;

import com.genlogic.GraphLayout.GlgCube;
import com.genlogic.GraphLayout.GlgGraphEdge;
import com.genlogic.GraphLayout.GlgGraphLayout;
import com.genlogic.GraphLayout.GlgGraphNode;
import com.genlogic.GraphLayout.GlgPoint;

import cbit.gui.graph.CartoonTool;
import cbit.gui.graph.ContainerShape;
import cbit.gui.graph.ElipseShape;
import cbit.gui.graph.GraphLayoutManager;
import cbit.gui.graph.GraphModel;
import cbit.gui.graph.RubberBandRectShape;
import cbit.gui.graph.Shape;
import cbit.gui.graph.ShapeUtil;
import cbit.gui.graph.SimpleContainerShape;
import cbit.gui.graph.actions.CartoonToolEditActions;
import cbit.gui.graph.actions.CartoonToolMiscActions;
import cbit.vcell.constraints.AbstractConstraint;
import cbit.vcell.constraints.ConstraintSolver;
import cbit.vcell.constraints.GeneralConstraint;
import cbit.vcell.constraints.SimpleBounds;
import edu.rpi.graphdrawing.Annealer;
import edu.rpi.graphdrawing.Circularizer;
import edu.rpi.graphdrawing.Cycleizer;
import edu.rpi.graphdrawing.Embedder;
import edu.rpi.graphdrawing.ForceDirect;
import edu.rpi.graphdrawing.Leveller;
import edu.rpi.graphdrawing.Node;
import edu.rpi.graphdrawing.Randomizer;
import edu.rpi.graphdrawing.Relaxer;
import edu.rpi.graphdrawing.Stabilizer;

/**
 * This class was generated by a SmartGuide.
 * 
 */
public class ConstraintGraphCartoonTool extends CartoonTool {
	private ConstraintsGraphModel graphModel = null;

	//
	// for dragging speciesContext's around
	//
	private boolean bMoving = false;
	private Shape movingShape = null;
	private Point movingPointWorld = null;
	private Point movingOffsetWorld = null;

	//
	// for dragging rectangle around
	//
	private boolean bRectStretch = false;
	private RubberBandRectShape rectShape = null;

	//
	// for dragging line around
	//
	private Point endPointWorld = null;
	private Mode mode = null;

	protected transient java.beans.PropertyChangeSupport propertyChange;
	private ConstraintSolver fieldConstraintSolver = null;

/**
 * This method was created by a SmartGuide.
 * @param canvas cbit.vcell.graph.CartoonCanvas
 */
public ConstraintGraphCartoonTool () {
	super();
}


/**
 * The addPropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
	getPropertyChange().addPropertyChangeListener(listener);
}


/**
 * The addPropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void addPropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
	getPropertyChange().addPropertyChangeListener(propertyName, listener);
}


/**
 * The firePropertyChange method was generated to support the propertyChange field.
 */
public void firePropertyChange(java.beans.PropertyChangeEvent evt) {
	getPropertyChange().firePropertyChange(evt);
}


/**
 * The firePropertyChange method was generated to support the propertyChange field.
 */
public void firePropertyChange(java.lang.String propertyName, int oldValue, int newValue) {
	getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
}


/**
 * The firePropertyChange method was generated to support the propertyChange field.
 */
public void firePropertyChange(java.lang.String propertyName, java.lang.Object oldValue, java.lang.Object newValue) {
	getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
}


/**
 * The firePropertyChange method was generated to support the propertyChange field.
 */
public void firePropertyChange(java.lang.String propertyName, boolean oldValue, boolean newValue) {
	getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
}


/**
 * Insert the method's description here.
 * Creation date: (9/9/2002 10:25:37 AM)
 * @return cbit.vcell.graph.GraphModel
 */
public ConstraintsGraphModel getConstraintsGraphModel() {
	return graphModel;
}


/**
 * Gets the constraintSolver property (cbit.vcell.constraints.ConstraintSolver) value.
 * @return The constraintSolver property value.
 * @see #setConstraintSolver
 */
public ConstraintSolver getConstraintSolver() {
	return fieldConstraintSolver;
}


/**
 * Insert the method's description here.
 * Creation date: (9/9/2002 10:25:37 AM)
 * @return cbit.vcell.graph.GraphModel
 */
public GraphModel getGraphModel() {
	return graphModel;
}


/**
 * Accessor for the propertyChange field.
 */
protected java.beans.PropertyChangeSupport getPropertyChange() {
	if (propertyChange == null) {
		propertyChange = new java.beans.PropertyChangeSupport(this);
	};
	return propertyChange;
}


/**
 * The hasListeners method was generated to support the propertyChange field.
 */
public synchronized boolean hasListeners(java.lang.String propertyName) {
	return getPropertyChange().hasListeners(propertyName);
}


/**
 * This method was created in VisualAge.
 */
public void layout(String layoutName) throws Exception {

	edu.rpi.graphdrawing.Blackboard bb = new edu.rpi.graphdrawing.Blackboard();
	java.util.HashMap<String, Shape> nodeShapeMap = new java.util.HashMap<String, Shape>();
	for(Shape shape : getGraphModel().getShapes()) {
		edu.rpi.graphdrawing.Node newNode = null;
		if (ShapeUtil.isMovable(shape)){
			newNode = bb.addNode(shape.getLabel());
		}
		//
		// initialize node location to current absolute position
		//
		if (newNode!=null){
			newNode.XY(shape.getSpaceManager().getAbsLoc().x, shape.getSpaceManager().getAbsLoc().y);
			nodeShapeMap.put(newNode.label(),shape);
		}
	}

	for(Shape shape : getConstraintsGraphModel().getShapes()) {
		if (shape instanceof ConstraintDependencyEdgeShape){
			ConstraintDependencyEdgeShape eShape = (ConstraintDependencyEdgeShape)shape;
			ElipseShape node1Shape = eShape.getConstraintShape();
			ElipseShape node2Shape = eShape.getVarShape();
			bb.addEdge(node1Shape.getLabel(),node2Shape.getLabel());
		}
	}

	bb.setArea(0,0,getGraphPane().getWidth(),getGraphPane().getHeight());
	bb.globals.D(20);
	
	bb.addEmbedder(GraphLayoutManager.OldLayouts.ANNEALER, new Annealer(bb));
	bb.addEmbedder(GraphLayoutManager.OldLayouts.CIRCULARIZER, new Circularizer(bb));
	bb.addEmbedder(GraphLayoutManager.OldLayouts.CYCLEIZER, new Cycleizer(bb));
	bb.addEmbedder(GraphLayoutManager.OldLayouts.FORCEDIRECT, new ForceDirect(bb));
	bb.addEmbedder(GraphLayoutManager.OldLayouts.LEVELLER, new Leveller(bb));
	bb.addEmbedder(GraphLayoutManager.OldLayouts.RANDOMIZER, new Randomizer(bb));
	bb.addEmbedder(GraphLayoutManager.OldLayouts.RELAXER, new Relaxer(bb));
	bb.addEmbedder(GraphLayoutManager.OldLayouts.STABILIZER, new Stabilizer(bb));

	bb.setEmbedding(layoutName);

	@SuppressWarnings("unchecked")
	List<Node> nodeList = bb.nodes();
	for (int i = 0; i < nodeList.size(); i++){
		Node node = nodeList.get(i);
		System.out.println("Node "+node.label()+" @ ("+node.x()+","+node.y()+")");
	}
	bb.PreprocessNodes();

	Embedder embedder = bb.embedder();
	embedder.Init();
	for (int i = 0; i < 1000; i++){
		embedder.Embed();
	}

	bb.removeDummies();
	@SuppressWarnings("unchecked")
	List<Node> nodesTemp = bb.nodes();
	nodeList = nodesTemp;
	//
	// calculate offset and scaling so that resulting graph fits on canvas
	//
	double lowX = 100000;
	double highX = -100000;
	double lowY = 100000;
	double highY = -100000;
	for (int i = 0; i < nodeList.size(); i++){
		Node node = nodeList.get(i);
		lowX = Math.min(lowX,node.x());
		highX = Math.max(highX,node.x());
		lowY = Math.min(lowY,node.y());
		highY = Math.max(highY,node.y());
	}
	double scaleX = getGraphPane().getWidth()/(1.5*(highX-lowX));
	double scaleY = getGraphPane().getHeight()/(1.5*(highY-lowY));
	int offsetX = getGraphPane().getWidth()/6;
	int offsetY = getGraphPane().getHeight()/6;
	for (int i = 0; i < nodeList.size(); i++){
		Node node = nodeList.get(i);
		Shape shape = nodeShapeMap.get(node.label());
		Point parentLoc = shape.getParent().getSpaceManager().getAbsLoc();
		shape.getSpaceManager().setRelPos((int)(scaleX*(node.x()-lowX)) + offsetX + parentLoc.x, 
				(int)((scaleY*(node.y()-lowY))+offsetY+parentLoc.y));
		System.out.println("Shape " + shape.getLabel() + " @ " + shape.getSpaceManager().getAbsLoc());
	}

	getGraphPane().repaint();
}


/**
 * This method calls the glg layout library.
 * Creation date: (8/28/2002 3:44:20 PM)
 */
public void layoutGlg() {
	//
	//Create graph object
	//
	GlgGraphLayout graph = new GlgGraphLayout();
	graph.SetUntangle(true); //true
	//specify dimensions for the graph! 400x400
	//System.out.println("H:"+getGraphPane().getHeight()+" W"+getGraphPane().getWidth());
	GlgCube graphDim = new GlgCube();
	GlgPoint newPoint = new GlgPoint(0,0,0);
	graphDim.p1 = newPoint;
	//newPoint = new com.genlogic.GlgPoint(getGraphPane().getWidth()-20, getGraphPane().getHeight()-10, 0);//400,400,0
	newPoint = new GlgPoint(1600,1600, 0);
	graphDim.p2 = newPoint;
	graph.dimensions = graphDim;

	GlgGraphNode graphNode;
	Map<Shape, GlgGraphNode> nodeMap = new HashMap<Shape, GlgGraphNode>(); 
	
	for(Shape shape : getConstraintsGraphModel().getShapes()) {
		//add to the graph			
		if (ShapeUtil.isMovable(shape)) {
			graphNode = graph.AddNode(null, 0, null);
		} else {
			continue;
		}
		
		//add to the hashmap
		nodeMap.put(shape,graphNode);
	}
	for(Shape shape : getConstraintsGraphModel().getShapes()) {
		if (shape instanceof ConstraintDependencyEdgeShape) {
			ConstraintDependencyEdgeShape eShape = (ConstraintDependencyEdgeShape)shape;
			graph.AddEdge(nodeMap.get(eShape.getConstraintShape()),nodeMap.get(eShape.getVarShape()),null, 0 ,null);
		}
	}

	//
	//call layout algorithm
	//
	while (!graph.SpringIterate()) {
		;
	}
	graph.Update();

	//
	//resize and scale the graph
	//
	//com.genlogic.GlgObject edgeArray = graph.edge_array;
	@SuppressWarnings("unchecked")
	List<GlgGraphEdge> edgeVector = graph.edge_array;
	double distance, minDistance = Double.MAX_VALUE;
	
	for (int i = 0; i < edgeVector.size(); i++){
		GlgGraphEdge edge = edgeVector.get(i);
		distance = java.awt.geom.Point2D.distance(edge.start_node.display_position.x, edge.start_node.display_position.y, edge.end_node.display_position.x, edge.end_node.display_position.y);
		minDistance = distance<minDistance?distance:minDistance;
	}
	double ratio = 1.0;
	if (minDistance > 40) {
		ratio = 40.0/minDistance;
	}
	
	Point place;
	GlgPoint glgPoint;
	for(Shape shape : getConstraintsGraphModel().getShapes()) {
		//test if it is contained in the nodeMap
		graphNode = nodeMap.get(shape);
		
		if (graphNode!= null) {
			glgPoint = graph.GetNodePosition(graphNode);
			//glgPoint = graphNode.display_position;
			place = new Point();
			place.setLocation(glgPoint.x*ratio, glgPoint.y*ratio+30);
			shape.getSpaceManager().setRelPos(place);		
		}
	}	
	
	
	Dimension graphSize = new Dimension((int)(1600*ratio)+50,(int)(1600*ratio)+50);
	getGraphPane().setSize(graphSize);
	getGraphPane().setPreferredSize(graphSize);

	//update the window
	getGraphPane().invalidate();
	((JViewport)getGraphPane().getParent()).revalidate();	
}


/**
 * Insert the method's description here.
 * Creation date: (9/17/2002 3:56:54 PM)
 * @param shape cbit.vcell.graph.Shape
 * @param menuAction java.lang.String
 */
protected void menuAction(Shape shape, String menuAction) {
	try {
	
		if(shape == null){return;}

		// if multiselect, then get them all
		ConstraintsGraphModel constraintsGraphModel = (ConstraintsGraphModel)getGraphModel();
		List<Shape> shapes = constraintsGraphModel.getSelectedShapes();
		//	
		if (menuAction.equals(CartoonToolMiscActions.Reset.MENU_ACTION)){
			getConstraintSolver().resetIntervals();
		}
		if (menuAction.equals(CartoonToolMiscActions.Solve.MENU_ACTION)){
			getConstraintSolver().narrow();
		}
		if (menuAction.equals(CartoonToolMiscActions.Enable.MENU_ACTION)){
			for (Shape selectedShape : shapes){
				if (selectedShape instanceof BoundsNode || 
						selectedShape instanceof GeneralConstraintNode){
					AbstractConstraint constraint = (AbstractConstraint)shape.getModelObject();
					getConstraintsGraphModel().getConstraintContainerImpl().setActive(constraint,true);
				}
			}
		}
		if (menuAction.equals(CartoonToolMiscActions.Disable.MENU_ACTION)){
			for (Shape selectedShape : shapes){
				if (selectedShape instanceof BoundsNode || 
						selectedShape instanceof GeneralConstraintNode){
					AbstractConstraint constraint = 
						(AbstractConstraint) selectedShape.getModelObject();
					getConstraintsGraphModel().getConstraintContainerImpl().setActive(constraint,false);
				}
			}
		}
		if (menuAction.equals(CartoonToolEditActions.Delete.MENU_ACTION)){
			List<BoundsNode> boundsToDelete = new ArrayList<BoundsNode>();
			List<GeneralConstraintNode> genConstraintsToDelete = new ArrayList<GeneralConstraintNode>();
			for (Shape selectedShape : shapes){
				if (selectedShape instanceof BoundsNode){
					boundsToDelete.add((BoundsNode) selectedShape.getModelObject());
				} else if (selectedShape instanceof GeneralConstraintNode){
					genConstraintsToDelete.add((GeneralConstraintNode) selectedShape.getModelObject());
				}
			}
			if (boundsToDelete.size()>0){
				SimpleBounds bounds[] = constraintsGraphModel.getConstraintContainerImpl().getSimpleBounds();
				for (int i = 0; i < boundsToDelete.size(); i++){
					bounds = (SimpleBounds[])BeanUtils.removeElement(bounds,boundsToDelete.get(i));
				}
				constraintsGraphModel.getConstraintContainerImpl().setSimpleBounds(bounds);
			}
			if (genConstraintsToDelete.size()>0){
				GeneralConstraint genConstraints[] = constraintsGraphModel.getConstraintContainerImpl().getGeneralConstraints();
				for (int i = 0; i < genConstraintsToDelete.size(); i++){
					genConstraints = (GeneralConstraint[])BeanUtils.removeElement(genConstraints,genConstraintsToDelete.get(i));
				}
				constraintsGraphModel.getConstraintContainerImpl().setGeneralConstraints(genConstraints);
			}
		}
	}catch (Throwable e){
		DialogUtils.showErrorDialog(getGraphPane(), e.getMessage(), e);
	}
}


/**
 * This method was created by a SmartGuide.
 * @param event java.awt.event.MouseEvent
 */
public void mouseClicked(java.awt.event.MouseEvent event) {
	try {
		//
		// if right mouse button, then do popup menu
		//
		if ((event.getModifiers() & (MouseEvent.BUTTON2_MASK | MouseEvent.BUTTON3_MASK)) != 0){
			return;
		}
		switch (mode) {
			case SELECT: {
				if (event.getClickCount()==2){
					Shape selectedShape = getConstraintsGraphModel().getSelectedShape();
					if (selectedShape != null){
						menuAction(selectedShape,CartoonToolMiscActions.Properties.MENU_ACTION);
					}
				}
				break;		
			}	
			default:
				break;
		}	
	}catch (Exception e){
		System.out.println("CartoonTool.mouseClicked: uncaught exception");
		e.printStackTrace(System.out);
		Point canvasLoc = getGraphPane().getLocationOnScreen();
		java.awt.Point screenPoint = new java.awt.Point(event.getX(),event.getY());
		canvasLoc.x += screenPoint.x;
		canvasLoc.y += screenPoint.y;
		DialogUtils.showErrorDialog(getGraphPane(), e.getMessage(), e);
	}				
}


/**
 * This method was created by a SmartGuide.
 * @param event java.awt.event.MouseEvent
 */
public void mouseDragged(MouseEvent event) {
	
	if ((event.getModifiers() & (MouseEvent.BUTTON2_MASK | MouseEvent.BUTTON3_MASK)) != 0){
		return;
	}
	boolean bShift = (event.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK;
	boolean bCntrl = (event.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK;
	//
	try {
		switch (mode){
			case SELECT: {
				Point worldPoint = screenToWorld(event.getX(),event.getY());
				if (bMoving){
					List<Shape> selectedShapes = getConstraintsGraphModel().getSelectedShapes();
					//
					// constrain to stay within the corresponding parent for the "movingShape" as well as all other selected (hence moving) shapes.
					//
					Point movingParentLoc = movingShape.getParent().getSpaceManager().getAbsLoc();
					Dimension movingParentSize = movingShape.getParent().getSpaceManager().getSize();
					worldPoint.x = Math.max(movingOffsetWorld.x + movingParentLoc.x,
							Math.min(movingOffsetWorld.x + movingParentLoc.x + movingParentSize.width - 
									movingShape.getSpaceManager().getSize().width, worldPoint.x));
					worldPoint.y = Math.max(movingOffsetWorld.y + movingParentLoc.y,
							Math.min(movingOffsetWorld.x + movingParentLoc.y + movingParentSize.height - 
									movingShape.getSpaceManager().getSize().height, worldPoint.y));
					for (Shape shape : selectedShapes) {
						if (shape != movingShape){
							Point selectedParentLoc = shape.getParent().getSpaceManager().getAbsLoc();
							Dimension selectedParentSize = shape.getParent().getSpaceManager().getSize();
							int selectedMovingOffsetX = 
								movingOffsetWorld.x + (movingShape.getSpaceManager().getAbsLoc().x - 
										shape.getSpaceManager().getAbsLoc().x);
							int selectedMovingOffsetY = movingOffsetWorld.y + 
							(movingShape.getSpaceManager().getAbsLoc().y - 
									shape.getSpaceManager().getAbsLoc().y);
							worldPoint.x = Math.max(selectedMovingOffsetX + selectedParentLoc.x, 
									Math.min(selectedMovingOffsetX + selectedParentLoc.x + 
											selectedParentSize.width - shape.getSpaceManager().getSize().width, worldPoint.x));
							worldPoint.y = Math.max(selectedMovingOffsetY + selectedParentLoc.y,
									Math.min(selectedMovingOffsetY + selectedParentLoc.y + 
											selectedParentSize.height - shape.getSpaceManager().getSize().height, 
											worldPoint.y));
						}
					}

					getGraphPane().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					Point newMovingPoint = new Point(worldPoint.x - movingOffsetWorld.x,
							worldPoint.y - movingOffsetWorld.y);
					int deltaX = newMovingPoint.x - movingPointWorld.x;
					int deltaY = newMovingPoint.y - movingPointWorld.y;
					movingPointWorld = newMovingPoint;
					movingShape.getSpaceManager().setRelPos(movingPointWorld.x - movingParentLoc.x,
							movingPointWorld.y-movingParentLoc.y);
					// for any other "movable" shapes that are selected, move them also
					for (Shape shape : selectedShapes) {
						if (shape != movingShape){
							shape.getSpaceManager().move(deltaX, deltaY);
						}
					}
					getGraphPane().invalidate();
					((JViewport)getGraphPane().getParent()).revalidate();
					getGraphPane().repaint();
				} else if (bRectStretch){
					// constain to stay within parent
					Point parentLoc = rectShape.getParent().getSpaceManager().getAbsLoc();
					Dimension parentSize = rectShape.getParent().getSpaceManager().getSize();
					worldPoint.x = Math.max(1,Math.min(parentSize.width-1,worldPoint.x-parentLoc.x)) + parentLoc.x;
					worldPoint.y = Math.max(1,Math.min(parentSize.height-1,worldPoint.y-parentLoc.y)) + parentLoc.y;
					getGraphPane().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					//getGraphPane().repaint();
					Graphics2D g = (Graphics2D)getGraphPane().getGraphics();
					java.awt.geom.AffineTransform oldTransform = g.getTransform();
					g.scale(0.01*getConstraintsGraphModel().getZoomPercent(),0.01*getConstraintsGraphModel().getZoomPercent());
					g.setXORMode(Color.white);
					rectShape.setEnd(endPointWorld);
					rectShape.paint(g,0,0);
					endPointWorld = worldPoint;
					rectShape.setEnd(endPointWorld);
					rectShape.paint(g,0,0);
					g.setTransform(oldTransform);
				}else{
					Shape shape = (getGraphModel().getSelectedShape() != null?getGraphModel().getSelectedShape():getConstraintsGraphModel().pickWorld(worldPoint));
					if (!bCntrl && !bShift && (ShapeUtil.isMovable(shape))){
						bMoving=true;
						movingShape = shape;
						movingPointWorld = shape.getSpaceManager().getAbsLoc();
						movingOffsetWorld = new Point(worldPoint.x-movingPointWorld.x,worldPoint.y-movingPointWorld.y);
					}else if (shape instanceof ContainerShape || bShift || bCntrl){
						bRectStretch = true;
						endPointWorld = new Point(worldPoint.x+1,worldPoint.y+1);
				 		rectShape = new RubberBandRectShape(worldPoint,endPointWorld,getConstraintsGraphModel());
						rectShape.setEnd(endPointWorld);
						if(!(shape instanceof ContainerShape)){
							shape.getParent().addChildShape(rectShape);
						}else{
							shape.addChildShape(rectShape);
						}
						Graphics2D g = (Graphics2D)getGraphPane().getGraphics();
						java.awt.geom.AffineTransform oldTransform = g.getTransform();
						g.scale(0.01*getConstraintsGraphModel().getZoomPercent(),0.01*getConstraintsGraphModel().getZoomPercent());
						g.setXORMode(Color.white);
						rectShape.paint(g,0,0);
						g.setTransform(oldTransform);
					}		
				}		
				break;
			}
			default: {
				break;
			}
		}		
	}catch (Exception e){
		System.out.println("CartoonTool.mouseDragged: uncaught exception");
		e.printStackTrace(System.out);
	}			
}


/**
 * This method was created by a SmartGuide.
 * @param event java.awt.event.MouseEvent
 */
public void mousePressed(java.awt.event.MouseEvent event) {
	//
	if(getConstraintsGraphModel() == null){return;}
	try {
		//
		int eventX = event.getX();
		int eventY = event.getY();
		java.awt.Point worldPoint = new java.awt.Point((int)(eventX*100.0/getConstraintsGraphModel().getZoomPercent()),(int)(eventY*100.0/getConstraintsGraphModel().getZoomPercent()));
		//
		//Always select with MousePress
		//
		boolean bShift = (event.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK;
		boolean bCntrl = (event.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK;
		if(mode == Mode.SELECT || (event.getModifiers() & MouseEvent.BUTTON1_MASK) != 0){
			selectEventFromWorld(worldPoint,bShift,bCntrl);
		}
		//
		// if mouse popupMenu event, popup menu
		if (event.isPopupTrigger() && mode == Mode.SELECT){
			popupMenu(getConstraintsGraphModel().getSelectedShape(),eventX,eventY);
			return;
		}
	}catch (Exception e){
		System.out.println("CartoonTool.mousePressed: uncaught exception");
		e.printStackTrace(System.out);
	}				
}


/**
 * This method was created by a SmartGuide.
 * @param event java.awt.event.MouseEvent
 */
public void mouseReleased(java.awt.event.MouseEvent event) {
	//
	if(getConstraintsGraphModel() == null){return;}
	//
	try {
		//Pick shape
		int eventX = event.getX();
		int eventY = event.getY();
		java.awt.Point worldPoint = new java.awt.Point((int)(eventX*100.0/getConstraintsGraphModel().getZoomPercent()),(int)(eventY*100.0/getConstraintsGraphModel().getZoomPercent()));
		Shape pickedShape = getConstraintsGraphModel().pickWorld(worldPoint);
		//
		// if mouse popupMenu event, popup menu
		//
		if (event.isPopupTrigger() && mode == Mode.SELECT){
			//boolean bShift = (event.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK;
			//boolean bCntrl = (event.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK;
			//selectEventFromWorld(worldPoint,bShift,bCntrl);
			if(pickedShape.isSelected()){ // == getConstraintsGraphModel().getSelectedShape()){
				popupMenu(pickedShape,event.getX(),event.getY());
			}
			//popupMenu(pickedShape,event.getX(),event.getY());
			return;
		}
		//
		if ((event.getModifiers() & (MouseEvent.BUTTON2_MASK | MouseEvent.BUTTON3_MASK)) != 0){
			return;
		}
		//
		// else do select and move
		//
		switch (mode){
			case SELECT:{
				getGraphPane().setCursor(Cursor.getDefaultCursor());
				if (bMoving){
					getGraphPane().invalidate();
					((JViewport)getGraphPane().getParent()).revalidate();
					getGraphPane().repaint();
				} else if (bRectStretch){
					Point absLoc = rectShape.getSpaceManager().getRelPos();
					Dimension size = rectShape.getSpaceManager().getSize();
					// remove temporary rectangle
					getConstraintsGraphModel().removeShape(rectShape);
					rectShape = null;
					Rectangle rect = new Rectangle(absLoc.x,absLoc.y,size.width,size.height);
					boolean bShift = (event.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK;
					boolean bCntrl = (event.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK;
					selectEventFromWorld(rect,bShift,bCntrl);
					getGraphPane().repaint();
				}
				bMoving=false;
				movingShape=null;
				bRectStretch=false;
				rectShape=null;
				break;
			}
			default:{
				break;
			}
		}
	}catch (Exception e){
		System.out.println("CartoonTool.mouseReleased: uncaught exception");
		e.printStackTrace(System.out);
	}			
		
}


/**
 * The removePropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
	getPropertyChange().removePropertyChangeListener(listener);
}


/**
 * The removePropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void removePropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener) {
	getPropertyChange().removePropertyChangeListener(propertyName, listener);
}


/**
 * This method was created by a SmartGuide.
 * @param x int
 * @param y int
 */
private void selectEventFromWorld(Point worldPoint, boolean bShift, boolean bCntrl) {
	//
	if(getConstraintsGraphModel() == null){return;}
	//
	if (!bShift && !bCntrl){
		//
		Shape pickedShape = getConstraintsGraphModel().pickWorld(worldPoint);
		//
		if (pickedShape == null || !pickedShape.isSelected()){
			getConstraintsGraphModel().clearSelection();
		}
		if (pickedShape != null && pickedShape.isSelected()){
			return;
		}
		if (pickedShape instanceof ContainerShape){
			if (pickedShape.isSelected()){
				getConstraintsGraphModel().clearSelection();
				return;
			}
		}
		if(pickedShape != null){
			getConstraintsGraphModel().selectShape(pickedShape);
		}

	}else if (bShift){
		Shape pickedShape = getConstraintsGraphModel().pickWorld(worldPoint);
		if (pickedShape==null){
			return;
		}
		if (pickedShape instanceof ContainerShape){
			return;
		}
		if(getConstraintsGraphModel().getSelectedShape() instanceof ContainerShape){
			getConstraintsGraphModel().clearSelection();
		}
		getConstraintsGraphModel().selectShape(pickedShape);
	}else if (bCntrl){
		Shape pickedShape = getConstraintsGraphModel().pickWorld(worldPoint);
		if (pickedShape==null){
			return;
		}
		if (pickedShape instanceof ContainerShape){
			return;
		}
		if (pickedShape.isSelected()){
			getConstraintsGraphModel().deselectShape(pickedShape);
		}else{
			getConstraintsGraphModel().selectShape(pickedShape);
		}
	}
}


/**
 * This method was created in VisualAge.
 * @param rect java.awt.Rectangle
 * @param bShift boolean
 * @param bCntrl boolean
 */
private void selectEventFromWorld(Rectangle rect, boolean bShift, boolean bCntrl) {
	if (!bShift && !bCntrl) {
		getConstraintsGraphModel().clearSelection();
		List<Shape> shapes = getConstraintsGraphModel().pickWorld(rect);
		for (Shape shape : shapes){
			if (ShapeUtil.isMovable(shape)){
				getConstraintsGraphModel().selectShape(shape);
			}
		}
	} else if (bShift) {
		if(getConstraintsGraphModel().getSelectedShape() instanceof ContainerShape){
			getConstraintsGraphModel().clearSelection();
		}
		List<Shape> shapes = getConstraintsGraphModel().pickWorld(rect);
		for (Shape shape : shapes){
			if (ShapeUtil.isMovable(shape)){
				getConstraintsGraphModel().selectShape(shape);
			}
		}
	}else if (bCntrl){
		if(getConstraintsGraphModel().getSelectedShape() instanceof ContainerShape){
			getConstraintsGraphModel().clearSelection();
		}
		List<Shape> shapes = getConstraintsGraphModel().pickWorld(rect);
		for (Shape shape : shapes){
			if (ShapeUtil.isMovable(shape)){
				if (shape.isSelected()){
					getConstraintsGraphModel().deselectShape(shape);
				} else {
					getConstraintsGraphModel().selectShape(shape);
				}
			}
		}
	}
}


/**
 * Insert the method's description here.
 * Creation date: (5/14/2003 10:51:54 AM)
 * @param newReactionCartoon cbit.vcell.graph.ReactionCartoon
 */
public void setConstraintsGraphModel(ConstraintsGraphModel constraintsGraphModel) {
	this.graphModel = constraintsGraphModel;
}


/**
 * Sets the constraintSolver property (cbit.vcell.constraints.ConstraintSolver) value.
 * @param constraintSolver The new value for the property.
 * @see #getConstraintSolver
 */
public void setConstraintSolver(ConstraintSolver constraintSolver) {
	ConstraintSolver oldValue = fieldConstraintSolver;
	fieldConstraintSolver = constraintSolver;
	firePropertyChange("constraintSolver", oldValue, constraintSolver);
}


/**
 * Insert the method's description here.
 * Creation date: (9/17/2002 3:47:34 PM)
 * @return boolean
 * @param shape cbit.vcell.graph.Shape
 * @param actionString java.lang.String
 */
public boolean shapeHasMenuAction(Shape shape, String menuAction) {
	if (shape == null){
		return false;
	}
	
	if (menuAction.equals(CartoonToolMiscActions.Reset.MENU_ACTION)){
		if (shape instanceof SimpleContainerShape && shape.getModelObject() == ((ConstraintsGraphModel)getGraphModel()).getConstraintContainerImpl()){
			return true;
		}
	}
	if (menuAction.equals(CartoonToolMiscActions.Solve.MENU_ACTION)){
		if (shape instanceof SimpleContainerShape && shape.getModelObject() == ((ConstraintsGraphModel)getGraphModel()).getConstraintContainerImpl()){
			return true;
		}
	}
	if (menuAction.equals(CartoonToolMiscActions.Enable.MENU_ACTION)){
		if (shape instanceof GeneralConstraintNode ||
			shape instanceof BoundsNode){
			return true;
		}
	}
	if (menuAction.equals(CartoonToolMiscActions.Disable.MENU_ACTION)){
		if (shape instanceof GeneralConstraintNode ||
			shape instanceof BoundsNode){
			return true;
		}
	}
	if (menuAction.equals(CartoonToolEditActions.Delete.MENU_ACTION)){
		if (shape instanceof GeneralConstraintNode ||
			shape instanceof BoundsNode){
			return true;
		}
	}
	return false;
}


/**
 * Insert the method's description here.
 * Creation date: (5/9/2003 9:11:06 AM)
 * @return boolean
 * @param actionString java.lang.String
 */
public boolean shapeHasMenuActionEnabled(Shape shape, String menuAction) {
	if (shape == null){
		return false;
	}
	
	if (menuAction.equals(CartoonToolMiscActions.Reset.MENU_ACTION)){
		if (shape instanceof SimpleContainerShape && shape.getModelObject() == ((ConstraintsGraphModel)getGraphModel()).getConstraintContainerImpl()){
			return true;
		}
	}
	if (menuAction.equals(CartoonToolMiscActions.Solve.MENU_ACTION)){
		if (shape instanceof SimpleContainerShape && shape.getModelObject() == ((ConstraintsGraphModel)getGraphModel()).getConstraintContainerImpl()){
			return true;
		}
	}
	if (menuAction.equals(CartoonToolMiscActions.Enable.MENU_ACTION)){
		//
		// enable if any selected shapes can be enabled
		//
		List<Shape> shapes = getGraphModel().getSelectedShapes();
		for (Shape shape2 : shapes) {
			if ((shape2 instanceof GeneralConstraintNode || shape2 instanceof BoundsNode)){
				AbstractConstraint constraint = (AbstractConstraint) shape2.getModelObject();
				if (!getConstraintsGraphModel().getConstraintContainerImpl().getActive(constraint)){
					return true;
				}
			}
		}
	}
	if (menuAction.equals(CartoonToolMiscActions.Disable.MENU_ACTION)){
		// disable if any selected shapes can be disabled
		List<Shape> shapes = getGraphModel().getSelectedShapes();
		for (Shape shape2 : shapes) {
			if ((shape2 instanceof GeneralConstraintNode || shape2 instanceof BoundsNode)){
				AbstractConstraint constraint = (AbstractConstraint) shape2.getModelObject();
				if (getConstraintsGraphModel().getConstraintContainerImpl().getActive(constraint)==true){
					return true;
				}
			}
		}
	}
	if (menuAction.equals(CartoonToolEditActions.Delete.MENU_ACTION)){
		//
		// delete if any selected shapes can be deleted
		//
		List<Shape> shapes = getGraphModel().getSelectedShapes();
		for (Shape shape2 : shapes) {
			if ((shape2 instanceof GeneralConstraintNode || shape2 instanceof BoundsNode)){
				return true;
			}
		}
	}
	return false;
}


/**
 * This method was created in VisualAge.
 * @param mode int
 */
public void updateMode(Mode newMode) {
	if (newMode==mode){
		return;
	}

	bMoving = false;
	movingShape = null;

	bRectStretch = false;
	rectShape = null;

	endPointWorld = null;
	if(getConstraintsGraphModel() != null){
		getConstraintsGraphModel().clearSelection();
	}

	this.mode = newMode;
	if(getGraphPane() != null){
		switch (mode){
			case SELECT:{
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


public void saveNodePositions() {}

}
