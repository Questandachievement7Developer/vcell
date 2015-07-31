/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.client.desktop.biomodel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.vcell.model.rbm.ComponentStateDefinition;
import org.vcell.model.rbm.ComponentStatePattern;
import org.vcell.model.rbm.MolecularComponent;
import org.vcell.model.rbm.MolecularComponentPattern;
import org.vcell.model.rbm.MolecularType;
import org.vcell.model.rbm.MolecularTypePattern;
import org.vcell.model.rbm.SpeciesPattern;
import org.vcell.model.rbm.MolecularComponentPattern.BondType;
import org.vcell.model.rbm.SpeciesPattern.Bond;
import org.vcell.util.Compare;
import org.vcell.util.document.PropertyConstants;
import org.vcell.util.gui.GuiUtils;

import cbit.vcell.biomodel.BioModel;
import cbit.vcell.biomodel.meta.VCMetaData;
import cbit.vcell.client.PopupGenerator;
import cbit.vcell.client.desktop.biomodel.RbmDefaultTreeModel.SpeciesPatternLocal;
import cbit.vcell.desktop.BioModelNode;
import cbit.vcell.graph.LargeShape;
import cbit.vcell.graph.SpeciesPatternLargeShape;
import cbit.vcell.graph.MolecularTypeLargeShape;
import cbit.vcell.model.RbmObservable;
import cbit.vcell.model.common.VCellErrorMessages;

@SuppressWarnings("serial")
public class ObservablePropertiesPanel extends DocumentEditorSubPanel {
	
	private class InternalEventHandler implements PropertyChangeListener, ActionListener, MouseListener, TreeSelectionListener,
		TreeWillExpandListener, FocusListener
	{
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() == observable) {
				if (evt.getPropertyName().equals(PropertyConstants.PROPERTY_NAME_NAME)) {
					updateInterface();
				}
			}
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (e.getSource() == getAddSpeciesPatternMenuItem()) {
				addSpeciesPattern();
			} else if (source == getDeleteMenuItem()) {
				delete();
			} else if (source == getRenameMenuItem()) {
				observableTree.startEditingAtPath(observableTree.getSelectionPath());
			} else if (source == getAddMenu()) {
				addNew();
			} else if (source == getEditMenuItem()) {
				observableTree.startEditingAtPath(observableTree.getSelectionPath());
//			} else if (source == showDetailsCheckBox) {
//				observableTreeModel.setShowDetails(showDetailsCheckBox.isSelected());
			}
		}
		@Override
		public void mouseClicked(MouseEvent e) {
//			System.out.println("click! " + e.getSource());
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if (!e.isConsumed() && e.getSource() == observableTree) {
				showPopupMenu(e);
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (!e.isConsumed() && e.getSource() == observableTree) {
				showPopupMenu(e);
			}			
		}
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
//			super.mouseExited(e);
			if(e.getSource() == annotationTextArea){
				changeFreeTextAnnotation();
			}
		}
		@Override
		public void valueChanged(TreeSelectionEvent e) {
		}
		@Override
		public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
			boolean veto = false;
			if (veto) {
				throw new ExpandVetoException(e);
			}
		}
		@Override
		public void treeWillCollapse(TreeExpansionEvent e) throws ExpandVetoException {
			JTree tree = (JTree) e.getSource();
			TreePath path = e.getPath();
			boolean veto = false;
			if(path.getParentPath() == null) {
				veto = true;
			}
			if (veto) {
				throw new ExpandVetoException(e);
			}
		}
		@Override
		public void focusGained(FocusEvent e) {
		}
		@Override
		public void focusLost(FocusEvent e) {
			if (e.getSource() == annotationTextArea) {
				changeFreeTextAnnotation();
			}
		}
	}
	
	private JTree observableTree = null;
	private ObservableTreeModel observableTreeModel = null;
	private RbmObservable observable;
	private JLabel titleLabel = null;
	private JTextArea annotationTextArea;

	private InternalEventHandler eventHandler = new InternalEventHandler();
	
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);	// between tree and right side
	private JSplitPane splitPaneHorizontal = new JSplitPane(JSplitPane.VERTICAL_SPLIT);	// between shape and annotation

	List<SpeciesPatternLargeShape> spsList = new ArrayList<SpeciesPatternLargeShape>();

	private JPopupMenu popupMenu;
	private JMenu addMenu;
	private JMenuItem deleteMenuItem;	
	private JMenuItem renameMenuItem;
	private JMenuItem editMenuItem;
	private JMenuItem addSpeciesPatternMenuItem;
//	private JCheckBox showDetailsCheckBox;
	
	private BioModel bioModel;
	public ObservablePropertiesPanel() {
		super();
		initialize();
	}

	public void addNew() {
		Object obj = observableTree.getLastSelectedPathComponent();
		if (obj == null || !(obj instanceof BioModelNode)) {
			return;
		}
		BioModelNode selectedNode = (BioModelNode) obj;
		Object selectedUserObject = selectedNode.getUserObject();
		if (selectedUserObject == observable){
			for (MolecularType mt : bioModel.getModel().getRbmModelContainer().getMolecularTypeList()) {
				JMenuItem menuItem = new JMenuItem(mt.getName());
				menuItem.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
			}
//			MolecularComponent molecularComponent = observable.createMolecularComponent();
//			observable.addMolecularComponent(molecularComponent);
//			jtree.startEditingAtPath(jtreeModel.findObjectPath(null, molecularComponent));
		} else if (selectedUserObject instanceof MolecularComponent){
//			MolecularComponent molecularComponent = (MolecularComponent) selectedUserObject;
//			ComponentState componentState = molecularComponent.createComponentState();
//			molecularComponent.addComponentState(componentState);
//			jtree.startEditingAtPath(jtreeModel.findObjectPath(null, componentState));
		}	
	}
	
	public void addSpeciesPattern() {
		SpeciesPattern sp = new SpeciesPattern();
		observable.addSpeciesPattern(sp);
		final TreePath path = observableTreeModel.findObjectPath(null, observable);
		observableTree.setSelectionPath(path);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {				
				observableTree.scrollPathToVisible(path);
			}
		});
	}

	public void delete() {
		Object obj = observableTree.getLastSelectedPathComponent();
		if (obj == null || !(obj instanceof BioModelNode)) {
			return;
		}
		BioModelNode selectedNode = (BioModelNode) obj;
		TreeNode parent = selectedNode.getParent();
		if (!(parent instanceof BioModelNode)) {
			return;
		}
		BioModelNode parentNode = (BioModelNode) parent;
		Object selectedUserObject = selectedNode.getUserObject();
		
		if(selectedUserObject instanceof SpeciesPatternLocal) {
			System.out.println("deleting species pattern local");
			Object parentUserObject = parentNode.getUserObject();
			SpeciesPatternLocal spl = (SpeciesPatternLocal)selectedUserObject;
			RbmObservable o = (RbmObservable)parentUserObject;
			List<SpeciesPattern> speciesPatternList = o.getSpeciesPatternList();
			speciesPatternList.remove(spl.speciesPattern);
			final TreePath path = observableTreeModel.findObjectPath(null, observable);
			observableTree.setSelectionPath(path);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					observableTreeModel.populateTree();			// repaint tree
					observableTree.scrollPathToVisible(path);	// scroll back up to show the observable
				}
			});
		} else if (selectedUserObject instanceof MolecularTypePattern){
			System.out.println("deleting molecular type pattern");
			MolecularTypePattern mtp = (MolecularTypePattern) selectedUserObject;
			Object parentUserObject = parentNode.getUserObject();
			SpeciesPatternLocal spl = (SpeciesPatternLocal)parentUserObject;
			SpeciesPattern sp = spl.speciesPattern;
			sp.removeMolecularTypePattern(mtp);
			if(!sp.getMolecularTypePatterns().isEmpty()) {
				sp.resolveBonds();
			}
			final TreePath path = observableTreeModel.findObjectPath(null, spl);
			observableTree.setSelectionPath(path);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					observableTreeModel.populateTree();
					observableTree.scrollPathToVisible(path);	// this doesn't seem to work ?
				}
			});
		} else if(selectedUserObject instanceof MolecularComponentPattern) {
			MolecularComponentPattern mcp = (MolecularComponentPattern) selectedUserObject;
			Object parentUserObject = parentNode.getUserObject();
			MolecularTypePattern mtp = (MolecularTypePattern)parentUserObject;
			mtp.removeMolecularComponentPattern(mcp);
			System.out.println("deleting MolecularComponentPattern " + mcp.getMolecularComponent().getName());
			parent = parentNode.getParent();
			parentNode = (BioModelNode) parent;
			parentUserObject = parentNode.getUserObject();
			SpeciesPatternLocal spl = (SpeciesPatternLocal)parentUserObject;
			SpeciesPattern sp = spl.speciesPattern;
			if(!sp.getMolecularTypePatterns().isEmpty()) {
				sp.resolveBonds();
			}
			final TreePath path = observableTreeModel.findObjectPath(null, spl);
			observableTree.setSelectionPath(path);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					observableTreeModel.populateTree();
					observableTree.scrollPathToVisible(path);	// this doesn't seem to work ?
				}
			});
		} else {
			System.out.println("deleting " + selectedUserObject.toString());
		}
	}

	private void initialize() {
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		leftPanel.setBackground(Color.white);	
		
		observableTree = new BioModelNodeEditableTree();
		observableTreeModel = new ObservableTreeModel(observableTree);
		observableTree.setModel(observableTreeModel);
		observableTreeModel.setShowDetails(true);
		
		RbmObservableTreeCellRenderer cro = new RbmObservableTreeCellRenderer();
		observableTree.setCellRenderer(cro);
		DisabledTreeCellEditor dtce =  new DisabledTreeCellEditor(observableTree, (cro));
		observableTree.setCellEditor(dtce);
		observableTree.setEditable(false);
		
		int rowHeight = observableTree.getRowHeight();
		if (rowHeight < 10) { 
			rowHeight = 20; 
		}
		observableTree.setRowHeight(rowHeight + 5);
		observableTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		ToolTipManager.sharedInstance().registerComponent(observableTree);
		observableTree.addTreeSelectionListener(eventHandler);
		observableTree.addTreeWillExpandListener(eventHandler);
		observableTree.addMouseListener(eventHandler);
		observableTree.setLargeModel(true);
		observableTree.setRootVisible(true);
		
		setLayout(new GridBagLayout());
		
		int gridy = 0;
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = gridy;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(2,2,0,2);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		titleLabel = new JLabel("Observable");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		leftPanel.add(titleLabel, gbc);
		
		gridy ++;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = gridy;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(2,2,2,2);
		gbc.fill = GridBagConstraints.BOTH;
		leftPanel.add(new JScrollPane(observableTree), gbc);
		
// --------------------------------------------------------------------------------------------------------	
		
		splitPaneHorizontal.setOneTouchExpandable(true);
		splitPaneHorizontal.setDividerLocation(120);
		splitPaneHorizontal.setResizeWeight(0.1);
		
		Border border = BorderFactory.createLineBorder(Color.gray);
		Border loweredEtchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border loweredBevelBorder = BorderFactory.createLoweredBevelBorder();

		TitledBorder annotationBorder = BorderFactory.createTitledBorder(loweredEtchedBorder, " Annotation ");
		annotationBorder.setTitleJustification(TitledBorder.LEFT);
		annotationBorder.setTitlePosition(TitledBorder.TOP);
		annotationBorder.setTitleFont(getFont().deriveFont(Font.BOLD));
		
//		JScrollPane p = new JScrollPane(shapePanel);
//		p.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		p.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel shapePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				for(SpeciesPatternLargeShape sps : spsList) {
					if(sps == null) {
						continue;
					}
					sps.paintSelf(g);
				}
			}
		};
		shapePanel.setBorder(border);
		shapePanel.setBackground(Color.white);
		
		shapePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				super.mouseClicked(me);
				Point whereClicked = me.getPoint();
				for (SpeciesPatternLargeShape sps : spsList) {
					if (sps.contains(whereClicked)) {		//check if mouse is clicked within shape
//						System.out.println("Inside " + sps.getClass().getName() + " at " + whereClicked.x + ", " + whereClicked.y);
					} else {
//						System.out.println("Outside " + sps.getClass().getName() + " at " + whereClicked.x + ", " + whereClicked.y);
					}
				}
			}
		});
//		shapePanel.addMouseListener(eventHandler);		// alternately use this
		
		JPanel generalPanel = new JPanel();		// right bottom panel, contains just the annotation
		generalPanel.setBorder(annotationBorder);
		generalPanel.setLayout(new GridBagLayout());

		gridy = 0;
		annotationTextArea = new javax.swing.JTextArea("", 1, 30);
		annotationTextArea.setLineWrap(true);
		annotationTextArea.setWrapStyleWord(true);
		annotationTextArea.setFont(new Font("monospaced", Font.PLAIN, 11));
		annotationTextArea.setEditable(false);
		javax.swing.JScrollPane jsp = new javax.swing.JScrollPane(annotationTextArea);
		
		gbc = new java.awt.GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.weighty = 0.1;
		gbc.gridx = 0; 
		gbc.gridy = gridy;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 4, 4, 4);
		generalPanel.add(jsp, gbc);

		splitPaneHorizontal.setTopComponent(shapePanel);
		splitPaneHorizontal.setBottomComponent(generalPanel);

// -------------------------------------------------------------------------------------------------		
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(240);
		splitPane.setResizeWeight(0.1);
		splitPane.setLeftComponent(leftPanel);
		splitPane.setRightComponent(splitPaneHorizontal);
		
		Dimension minimumSize = new Dimension(100, 150);	//provide minimum sizes for the two components in the split pane
		splitPane.setMinimumSize(minimumSize);
		leftPanel.setMinimumSize(minimumSize);
		splitPaneHorizontal.setMinimumSize(minimumSize);
		
		setName("ObservablePropertiesPanel");
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
		setBackground(Color.white);
		
		annotationTextArea.addFocusListener(eventHandler);
		annotationTextArea.addMouseListener(eventHandler);
	}
	
	private JMenu getAddMenu() {
		if (addMenu == null) {
			addMenu = new JMenu("Add");
			addMenu.addActionListener(eventHandler);
		}
		return addMenu;
	}
	
	private JMenuItem getRenameMenuItem() {
		if (renameMenuItem == null) {
			renameMenuItem = new JMenuItem("Rename");
			renameMenuItem.addActionListener(eventHandler);
		}
		return renameMenuItem;
	}
	
	private JMenuItem getDeleteMenuItem() {
		if (deleteMenuItem == null) {
			deleteMenuItem = new JMenuItem("Delete");
			deleteMenuItem.addActionListener(eventHandler);
		}
		return deleteMenuItem;
	}
	
	private JMenuItem getEditMenuItem() {
		if (editMenuItem == null) {
			editMenuItem = new JMenuItem("Edit");
			editMenuItem.addActionListener(eventHandler);
		}
		return editMenuItem;
	}
	
	private JMenuItem getAddSpeciesPatternMenuItem() {
		if (addSpeciesPatternMenuItem == null) {
			addSpeciesPatternMenuItem = new JMenuItem("Add Species Pattern");
			addSpeciesPatternMenuItem.addActionListener(eventHandler);
		}
		return addSpeciesPatternMenuItem;
	}
	
	@Override
	protected void onSelectedObjectsChange(Object[] selectedObjects) {
		RbmObservable observable = null;
		if (selectedObjects.length == 1 && selectedObjects[0] instanceof RbmObservable) {
			observable = (RbmObservable) selectedObjects[0];
		}
		setObservable(observable);	
	}
	
	private void setObservable(RbmObservable newValue) {
		if (observable == newValue) {
			return;
		}
		RbmObservable oldValue = observable;
		if (oldValue != null) {
			oldValue.removePropertyChangeListener(eventHandler);
		}
		if (newValue != null) {
			newValue.addPropertyChangeListener(eventHandler);
		}
		observable = newValue;
		observableTreeModel.setObservable(observable);
		updateInterface();
	}
	
	private void updateInterface() {
		boolean bNonNullObservable = observable != null && bioModel != null;
		annotationTextArea.setEditable(bNonNullObservable);
		if (bNonNullObservable) {
			VCMetaData vcMetaData = bioModel.getModel().getVcMetaData();
			annotationTextArea.setText(vcMetaData.getFreeTextAnnotation(observable));
		} else {
			annotationTextArea.setText(null);
		}
		updateTitleLabel();
		updateShape();
	}
	private void updateTitleLabel() {
		if (observable != null) {
			titleLabel.setText("Properties for Observable : " + observable.getName());
		}
	}
	private void updateShape() {
		spsList.clear();
		if(observable != null && observable.getSpeciesPatternList() != null && observable.getSpeciesPatternList().size() > 0) {
			Graphics gc = splitPane.getRightComponent().getGraphics();
			for(int i = 0; i<observable.getSpeciesPatternList().size(); i++) {
				SpeciesPattern sp = observable.getSpeciesPatternList().get(i);
				// TODO: count the number of bonds for this sp and allow enough vertical space for them
				SpeciesPatternLargeShape sps = new SpeciesPatternLargeShape(20, 20+75*i, sp, gc, observable);
				spsList.add(sps);
			}
		}
		splitPane.getRightComponent().repaint();
	}
	private void changeFreeTextAnnotation() {
		try{
			if (observable == null) {
				return;
			}
			// set text from annotationTextField in free text annotation for species in vcMetaData (from model)
			if(bioModel.getModel() != null && bioModel.getModel().getVcMetaData() != null){
				VCMetaData vcMetaData = bioModel.getModel().getVcMetaData();
				String textAreaStr = (annotationTextArea.getText() == null || annotationTextArea.getText().length()==0?null:annotationTextArea.getText());
				if(!Compare.isEqualOrNull(vcMetaData.getFreeTextAnnotation(observable),textAreaStr)){
					vcMetaData.setFreeTextAnnotation(observable, textAreaStr);	
				}
			}
		} catch(Exception e){
			e.printStackTrace(System.out);
			PopupGenerator.showErrorDialog(this,"Edit Observable Error\n"+e.getMessage(), e);
		}
	}
	
	private void showPopupMenu(MouseEvent e){ 
		if (!e.isPopupTrigger()) {
			return;
		}
		if (popupMenu == null) {
			popupMenu = new JPopupMenu();			
		}		
		if (popupMenu.isShowing()) {
			return;
		}
		boolean bDelete = false;
		boolean bAdd = false;
		boolean bEdit = false;
		boolean bRename = false;
		popupMenu.removeAll();
		Point mousePoint = e.getPoint();
		GuiUtils.selectClickTreePath(observableTree, e);		
		TreePath clickPath = observableTree.getPathForLocation(mousePoint.x, mousePoint.y);
	    if (clickPath == null) {
	    	popupMenu.add(getAddSpeciesPatternMenuItem());
	    	return;
	    }
		TreePath[] selectedPaths = observableTree.getSelectionPaths();
		if (selectedPaths == null) {
			return;
		}
		for (TreePath tp : selectedPaths) {
			Object obj = tp.getLastPathComponent();
			if (obj == null || !(obj instanceof BioModelNode)) {
				continue;
			}
			
			BioModelNode selectedNode = (BioModelNode) obj;
			final Object selectedObject = selectedNode.getUserObject();
			
			if (selectedObject instanceof RbmObservable) {
				popupMenu.add(getAddSpeciesPatternMenuItem());
			} else if(selectedObject instanceof SpeciesPatternLocal) {
				getAddMenu().setText(VCellErrorMessages.AddMolecularTypes);
				getAddMenu().removeAll();
				for (final MolecularType mt : bioModel.getModel().getRbmModelContainer().getMolecularTypeList()) {
					JMenuItem menuItem = new JMenuItem(mt.getName());
					getAddMenu().add(menuItem);
					menuItem.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							MolecularTypePattern molecularTypePattern = new MolecularTypePattern(mt);
							((SpeciesPatternLocal)selectedObject).speciesPattern.addMolecularTypePattern(molecularTypePattern);
							final TreePath path = observableTreeModel.findObjectPath(null, molecularTypePattern);
							observableTree.setSelectionPath(path);
							SwingUtilities.invokeLater(new Runnable() {
								
								public void run() {				
									observableTree.scrollPathToVisible(path);
								}
							});
						}
					});
				}
				bAdd = true;
				bDelete = true;
			} else if (selectedObject instanceof MolecularTypePattern) {
				bDelete = true;
			} else if (selectedObject instanceof MolecularComponentPattern) {
				manageComponentPattern(observableTreeModel, observableTree, selectedNode, selectedObject);
				bDelete = false;
			}
		}
//		popupMenu.removeAll();
		// everything can be renamed
		if (bRename) {
			popupMenu.add(getRenameMenuItem());
		}
		if (bDelete) {
			popupMenu.add(getDeleteMenuItem());
		}
		if (bEdit) {
			popupMenu.add(getEditMenuItem());
		}
		if (bAdd) {
			popupMenu.add(new JSeparator());
			popupMenu.add(getAddMenu());
		}
		popupMenu.show(observableTree, mousePoint.x, mousePoint.y);
	}
	
	public void setBioModel(BioModel newValue) {
		bioModel = newValue;
		observableTreeModel.setBioModel(bioModel);
	}
	public void manageComponentPattern(final ObservableTreeModel treeModel, final JTree tree,
			BioModelNode selectedNode, final Object selectedObject) {
		popupMenu.removeAll();
		final MolecularComponentPattern mcp = (MolecularComponentPattern)selectedObject;
		final MolecularComponent mc = mcp.getMolecularComponent();
		//
		// --- State
		//
		if(mc.getComponentStateDefinitions().size() != 0) {
			JMenu editStateMenu = new JMenu();
			editStateMenu.setText("Edit State");
			editStateMenu.removeAll();
			List<String> itemList = new ArrayList<String>();
			itemList.add(ComponentStatePattern.strAny);
			for (final ComponentStateDefinition csd : mc.getComponentStateDefinitions()) {
				String name = csd.getName();
				itemList.add(name);
			}
			for(String name : itemList) {
				JMenuItem menuItem = new JMenuItem(name);
				editStateMenu.add(menuItem);
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String name = e.getActionCommand();
						if(name.equals(ComponentStatePattern.strAny)) {
							ComponentStatePattern csp = new ComponentStatePattern();
							mcp.setComponentStatePattern(csp);
						} else {
							String csdName = e.getActionCommand();
							ComponentStateDefinition csd = mcp.getMolecularComponent().getComponentStateDefinition(csdName);
							if(csd == null) {
								throw new RuntimeException("Missing ComponentStateDefinition " + csdName + " for Component " + mcp.getMolecularComponent().getName());
							}
							ComponentStatePattern csp = new ComponentStatePattern(csd);
							mcp.setComponentStatePattern(csp);
						}
					}
				});
			}
			popupMenu.add(editStateMenu);
		}
		//
		// --- Bonds
		//						
		final MolecularTypePattern mtp;
		final SpeciesPattern sp;
		BioModelNode parentNode = (BioModelNode) selectedNode.getParent();
		Object parentObject = parentNode == null ? null : parentNode.getUserObject();
		if(parentObject != null && parentObject instanceof MolecularTypePattern) {
			mtp = (MolecularTypePattern)parentObject;
			parentNode = (BioModelNode) parentNode.getParent();
			parentObject = parentNode == null ? null : parentNode.getUserObject();
			if(parentObject != null && parentObject instanceof SpeciesPatternLocal) {
				sp = ((SpeciesPatternLocal)parentObject).speciesPattern;
			} else {
				sp = null;
			}
		} else {
			mtp = null;
			sp = null;
		}
		
		JMenu editBondMenu = new JMenu();
		editBondMenu.setText("Edit Bond");
		editBondMenu.removeAll();
		final Map<String, Bond> itemMap = new LinkedHashMap<String, Bond>();
		
		final String noneString = "<html><b>" + BondType.None.symbol + "</b> " + BondType.None.name() + "</html>";
		final String existsString = "<html><b>" + BondType.Exists.symbol + "</b> " + BondType.Exists.name() + "</html>";
		final String possibleString = "<html><b>" + BondType.Possible.symbol + "</b> " + BondType.Possible.name() + "</html>";
		itemMap.put(noneString, null);
		itemMap.put(existsString, null);
		itemMap.put(possibleString, null);
		if(mtp != null && sp != null) {
			List<Bond> bondPartnerChoices = sp.getAllBondPartnerChoices(mtp, mc);
			for(Bond b : bondPartnerChoices) {
				if(b.equals(mcp.getBond())) {
					continue;	// if the mcp has a bond already we don't offer it
				}
				int index = 0;
				if(mcp.getBondType() == BondType.Specified) {
					index = mcp.getBondId();
				} else {
					index = sp.nextBondId();
				}
//				itemMap.put(b.toHtmlStringLong(sp, mtp, mc, index), b);
				itemMap.put(b.toHtmlStringLong(sp, index), b);
			}
		}
		for(String name : itemMap.keySet()) {
			JMenuItem menuItem = new JMenuItem(name);
			editBondMenu.add(menuItem);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String name = e.getActionCommand();
					BondType btBefore = mcp.getBondType();
					if(name.equals(noneString)) {
						if(btBefore == BondType.Specified) {	// specified -> not specified
							// change the partner to possible
							mcp.getBond().molecularComponentPattern.setBondType(BondType.Possible);
							mcp.getBond().molecularComponentPattern.setBond(null);
						}
						mcp.setBondType(BondType.None);
						mcp.setBond(null);
						treeModel.populateTree();
					} else if(name.equals(existsString)) {
						if(btBefore == BondType.Specified) {	// specified -> not specified
							// change the partner to possible
							mcp.getBond().molecularComponentPattern.setBondType(BondType.Possible);
							mcp.getBond().molecularComponentPattern.setBond(null);
						}
						mcp.setBondType(BondType.Exists);
						mcp.setBond(null);
						treeModel.populateTree();
					} else if(name.equals(possibleString)) {
						if(btBefore == BondType.Specified) {	// specified -> not specified
							// change the partner to possible
							mcp.getBond().molecularComponentPattern.setBondType(BondType.Possible);
							mcp.getBond().molecularComponentPattern.setBond(null);
						}
						mcp.setBondType(BondType.Possible);
						mcp.setBond(null);
						treeModel.populateTree();
					} else {
						if (btBefore != BondType.Specified) {
							// if we go from a non-specified to a specified we need to find the next available
							// bond id, so that we can choose the color for displaying the bond
							// a bad bond id, like -1, will crash badly when trying to choose the color
							int bondId = sp.nextBondId();
							mcp.setBondId(bondId);
						} else {
							// specified -> specified
							// change the old partner to possible, continue using the bond id
							mcp.getBond().molecularComponentPattern.setBondType(BondType.Possible);
							mcp.getBond().molecularComponentPattern.setBond(null);
						}
						mcp.setBondType(BondType.Specified);
						Bond b = itemMap.get(name);
						mcp.setBond(b);
						mcp.getBond().molecularComponentPattern.setBondId(mcp.getBondId());
						sp.resolveBonds();

						final TreePath path = treeModel.findObjectPath(null, mcp);
						treeModel.populateTree();
						tree.setSelectionPath(path);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {				
								tree.scrollPathToVisible(path);
							}
						});
					}

				}
			});
		}
		popupMenu.add(editBondMenu);
	}

}