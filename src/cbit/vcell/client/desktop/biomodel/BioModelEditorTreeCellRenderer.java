package cbit.vcell.client.desktop.biomodel;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import cbit.vcell.biomodel.BioModel;
import cbit.vcell.client.desktop.biomodel.BioModelEditorTreeModel.BioModelEditorTreeFolderClass;
import cbit.vcell.client.desktop.biomodel.BioModelEditorTreeModel.BioModelEditorTreeFolderNode;
import cbit.vcell.data.DataSymbol;
import cbit.vcell.data.FieldDataSymbol;
import cbit.vcell.desktop.Annotation;
import cbit.vcell.desktop.BioModelNode;
import cbit.vcell.mapping.BioEvent;
import cbit.vcell.mapping.SimulationContext;
import cbit.vcell.math.AnnotatedFunction;
import cbit.vcell.model.Feature;
import cbit.vcell.model.FluxReaction;
import cbit.vcell.model.Model.ModelParameter;
import cbit.vcell.model.ReactionStep;
import cbit.vcell.model.SimpleReaction;
import cbit.vcell.model.SpeciesContext;
import cbit.vcell.model.Structure;
import cbit.vcell.solver.Simulation;
 
public class BioModelEditorTreeCellRenderer extends DefaultTreeCellRenderer  {
	private Font regularFont = null;
	private Font boldFont = null;
	
	private JTree ownerTree;
	private BioModel bioModel = null;
	Icon bioModelIcon = null;
	Icon outputFunctionIcon = null;
	
	public BioModelEditorTreeCellRenderer(JTree tree) {
		super();
		ownerTree = tree;
		setPreferredSize(new Dimension(170,30));
		try {
			bioModelIcon = new ImageIcon(getClass().getResource("/images/bioModel_16x16.gif"));
		    outputFunctionIcon = new ImageIcon(getClass().getResource("/icons/function_icon.png"));
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}


	public void setBioModel(BioModel bm) {
		bioModel = bm;
	}
	
	public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (regularFont == null) {
			regularFont = getFont();
			boldFont = regularFont.deriveFont(Font.BOLD);
		}
		if (value instanceof BioModelNode) {
	        BioModelNode node = (BioModelNode)value;
	        Object userObj = node.getUserObject();
	    	String labelText = null;
	    	String toolTipPrefix = "";
	    	String toolTipSuffix = "";
	    	Font font = regularFont;
	    	Icon icon = null;
	    	if (userObj instanceof BioModel) {
	    		font = boldFont;
	    		icon = bioModelIcon;
	    		labelText = ((BioModel)userObj).getName();
	    	} else if (userObj instanceof Annotation) {		// --- root: application name
	    		font = boldFont;
	    		labelText = userObj.toString();
	    	} else if (userObj instanceof SimulationContext) {		// --- root: application name
	    		font = boldFont;
	    		labelText = ((SimulationContext)userObj).getName();
	    		toolTipPrefix = "Application : ";
	    		toolTipSuffix = labelText;
	    	} else if (userObj instanceof BioModelEditorTreeFolderNode) {		// --- 1st level folders
	    		BioModelEditorTreeFolderNode folder = (BioModelEditorTreeFolderNode)userObj;
	    		BioModelEditorTreeFolderClass folderClass = folder.getFolderClass();
	    		if (folder.isFirstLevel()) {
	    			font = boldFont;
	    			toolTipPrefix = folder.getName();
	    		}
	    		labelText = folder.getName();
	    		switch (folderClass) {
	    		case ANNOTATION_NODE:
	    			font = regularFont;
	    			if (bioModel != null && bioModel.getVCMetaData().getFreeTextAnnotation(bioModel).length() > 0) {
	    				labelText = bioModel.getVCMetaData().getFreeTextAnnotation(bioModel);
	    			} else {
	    				labelText = "(click to edit notes)";
	    			}
	    			break;	    		
        		case DATA_SYMBOLS_NODE:
    	        	toolTipPrefix = "Data Symbols : ";
					toolTipSuffix = labelText;
        			break;
	        	}
	    	} else if (userObj instanceof SpeciesContext) { 	// --- species context
	        	labelText = ((SpeciesContext)userObj).getName();
	        	toolTipPrefix = "Species : ";
				toolTipSuffix = labelText;
	        } else if (userObj instanceof ModelParameter) {		// --- global parameter
	        	labelText = ((ModelParameter)userObj).getName();
	        	toolTipPrefix = "Global Parameter : ";
				toolTipSuffix = labelText;
	        } else if (userObj instanceof SimpleReaction) {		// --- simple reaction
	        	labelText = ((ReactionStep)userObj).getName();
	        	toolTipPrefix = "Simple Reaction : ";
				toolTipSuffix = labelText;
	        } else if (userObj instanceof FluxReaction) {		// --- flux reaction
	        	labelText = ((ReactionStep)userObj).getName();
	        	toolTipPrefix = "Flux Reaction : ";
				toolTipSuffix = labelText;
	        } else if (userObj instanceof DataSymbol) {			// --- field data
	        	labelText = ((DataSymbol)userObj).getName();
	        	toolTipPrefix = "Dataset: " + ((FieldDataSymbol)userObj).getExternalDataIdentifier().getName();
				toolTipSuffix = "";
	        } else if (userObj instanceof BioEvent) {			// --- event
	        	BioEvent bioEvent = (BioEvent)userObj;
	        	SimulationContext simulationContext = bioEvent.getSimulationContext();
				if (simulationContext.getGeometry() != null && simulationContext.getGeometry().getDimension() > 0 
						|| simulationContext.isStoch()) {
					setEnabled(false);
					setDisabledIcon(this.getClosedIcon());
				} else {
					labelText = bioEvent.getName();
					toolTipPrefix = "Event : ";
					toolTipSuffix = labelText;
				}
	    	} else if (userObj instanceof Simulation) {
	        	labelText = ((Simulation)userObj).getName();
	        	toolTipPrefix = "Simulation : ";
	        	toolTipSuffix = labelText;
	        } else if (userObj instanceof AnnotatedFunction) {
	        	labelText = ((AnnotatedFunction)userObj).getName();
	        	toolTipPrefix = "Output Function : ";
	        	toolTipSuffix = labelText;
	        	icon = outputFunctionIcon;
	        } else if (userObj instanceof Structure) {
	        	labelText = ((Structure)userObj).getName();
	        	toolTipPrefix = ((Structure)userObj) instanceof Feature ? "Feauture : " : "Membrane : ";
	        	toolTipSuffix = labelText;
	        }
	    	setIcon(icon);
	        setFont(font);
	    	setText(" " + labelText);
	    	setToolTipText(toolTipPrefix + toolTipSuffix);
		}
        return this;
    }

	@Override
	protected void paintComponent(Graphics g) {
		if (getForeground() == getTextSelectionColor()) {
			FontMetrics metrics = getFontMetrics(getFont());
			String text = getText();
			int startX = getIcon() == null ? 0 : getIcon().getIconWidth() + getIconTextGap();
			int startY = 0; //You probably have some vertical offset to add here.
			int length = metrics.stringWidth(text);
			int height = ownerTree.getRowHeight();
			g.fillRect(startX + length + 1, startY, getWidth() - length - startX, height);
		}
		super.paintComponent(g);
	}
}
