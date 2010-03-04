package cbit.vcell.model.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JList;

import org.vcell.util.BeanUtils;
import org.vcell.util.Compare;
import org.vcell.util.DataAccessException;
import org.vcell.util.TokenMangler;
import org.vcell.util.document.KeyValue;
import org.vcell.util.gui.LineBorderBean;
import org.vcell.util.gui.TitledBorderBean;
import org.vcell.util.gui.ZEnforcer;

import cbit.vcell.client.PopupGenerator;
import cbit.vcell.client.task.AsynchClientTask;
import cbit.vcell.client.task.ClientTaskDispatcher;
import cbit.vcell.clientdb.DocumentManager;
import cbit.vcell.dictionary.DBFormalSpecies;
import cbit.vcell.dictionary.DBNonFormalUnboundSpecies;
import cbit.vcell.dictionary.DBSpecies;
import cbit.vcell.dictionary.DictionaryQueryResults;
import cbit.vcell.dictionary.ReactionDescription;
import cbit.vcell.dictionary.SpeciesDescription;
import cbit.vcell.graph.BioCartoonTool;
import cbit.vcell.model.Catalyst;
import cbit.vcell.model.Feature;
import cbit.vcell.model.Flux;
import cbit.vcell.model.FluxReaction;
import cbit.vcell.model.HMM_IRRKinetics;
import cbit.vcell.model.Kinetics;
import cbit.vcell.model.Membrane;
import cbit.vcell.model.Model;
import cbit.vcell.model.Product;
import cbit.vcell.model.Reactant;
import cbit.vcell.model.ReactionParticipant;
import cbit.vcell.model.ReactionStep;
import cbit.vcell.model.ReactionStepInfo;
import cbit.vcell.model.SimpleReaction;
import cbit.vcell.model.Species;
import cbit.vcell.model.SpeciesContext;
import cbit.vcell.model.Structure;
import cbit.vcell.modeldb.DatabaseConstants;
import cbit.vcell.modeldb.ReactionQuerySpec;
import cbit.vcell.parser.Expression;

/**
 * Insert the type's description here.
 * Creation date: (8/13/2003 11:00:32 AM)
 * @author: Frank Morgan
 */
public class DBReactionWizardPanel extends javax.swing.JPanel implements java.awt.event.ActionListener {

	//
	//
	public class MapStringToObject {
		String mapString;
		Object toObject;
		public MapStringToObject(String argMapString,Object argToObject){
			mapString = argMapString;
			toObject = argToObject;
		}
		public Object getToObject(){
			return toObject;
		}
		public String getMappedString(){
			return mapString;
		}
		public String toString(){
			return mapString;
		}
	}
	//
	private javax.swing.JComboBox[] speciesAssignmentJCB = null;
	private javax.swing.JComboBox[] structureAssignmentJCB = null;
	private Species[] speciesOrder = null;
	private Object[] lastSearchChangeInfo = null;
	private Object lastReactionSelection = null;
	private ReactionStep lastReactStepSelection = null;
	//
	//
	private Hashtable<String, Vector<String>> mapRXStringtoRXIDs = new Hashtable<String, Vector<String>>();
	//
	private ReactionDescription resolvedReaction = null;
	private javax.swing.JButton ivjBackJButton = null;
	private javax.swing.JPanel ivjBFNJPanel = null;
	private javax.swing.JButton ivjFinishJButton = null;
	private javax.swing.JButton ivjNextJButton = null;
	private javax.swing.JLabel ivjJLabel1 = null;
	private javax.swing.JLabel ivjJLabel2 = null;
	private javax.swing.JScrollPane ivjJScrollPane3 = null;
	private javax.swing.JPanel ivjParameterJPanel = null;
	private javax.swing.JList ivjParameterNamesJList = null;
	private javax.swing.JScrollPane ivjParameterNamesJScrollPane = null;
	private javax.swing.JList ivjParameterValuesJList = null;
	private javax.swing.JScrollPane ivjParameterValuesJScrollPane = null;
	private javax.swing.JList ivjReactionsJList = null;
	private javax.swing.JLabel ivjResolveHighlightJLabel = null;
	private javax.swing.JPanel ivjResolverJPanel = null;
	private javax.swing.JPanel ivjSearchCriteriaJPanel = null;
	private javax.swing.JPanel ivjSearchResultsJPanel = null;
	private DocumentManager fieldDocumentManager = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JPanel ivjCardLayoutJPanel = null;
	private boolean ivjConnPtoP3Aligning = false;
	private javax.swing.ListSelectionModel ivjReactionSelectionModel = null;
	private Model fieldModel = null;
	private Structure fieldStructure = null;
	private boolean ivjConnPtoP4Aligning = false;
	private javax.swing.ListSelectionModel ivjParameterNameSelectionModel = null;
	private javax.swing.JPanel ivjJPanel1 = null;
	private javax.swing.JRadioButton ivjSearchDictionaryJRadioButton = null;
	private javax.swing.JRadioButton ivjSearchUserJRadioButton = null;
	private javax.swing.ButtonGroup ivjSearchTypeButtonGroup = null;
	private ReactionStep fieldReactionStep = null;
	private javax.swing.JButton ivjJButton1 = null;
	private ReactionDescription fieldReactionDescription = null;
	private boolean ivjConnPtoP5Aligning = false;
	private javax.swing.ListSelectionModel ivjRXDescriptionLSM = null;
	private javax.swing.JPanel ivjJPanel = null;
	private javax.swing.ButtonGroup ivjFindRXButtonGroup = null;
	private javax.swing.JRadioButton ivjFindRXTextRadioButton = null;
	private javax.swing.JTextField ivjFindTextJTextField = null;
	private DBFormalSpecies ivjCurrentDBFormalSpecies = null;
	private boolean ivjConnPtoP6Aligning = false;
	private javax.swing.text.Document ivjdocument2 = null;
	private javax.swing.JRadioButton ivjKeggMoleculeJRadioButton = null;
	private javax.swing.JButton ivjKeggSpecifyJButton = null;
	private javax.swing.JLabel ivjKeggTypeJLabel = null;
	private javax.swing.JPanel ivjRXParticipantsJPanel = null;
	private ReactionCanvas ivjReactionCanvas1 = null;
	private javax.swing.JScrollPane ivjJScrollPane1 = null;
	private javax.swing.JScrollPane ivjJScrollPane2 = null;

class IvjEventHandler implements java.awt.event.ActionListener, java.beans.PropertyChangeListener, javax.swing.event.DocumentListener, javax.swing.event.ListSelectionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == DBReactionWizardPanel.this.getBackJButton()) 
				connEtoC12(e);
			if (e.getSource() == DBReactionWizardPanel.this.getFinishJButton()) 
				connEtoC13(e);
			if (e.getSource() == DBReactionWizardPanel.this.getNextJButton()) 
				connEtoC14(e);
			if (e.getSource() == DBReactionWizardPanel.this.getJButton1()) 
				connEtoC26(e);
			if (e.getSource() == DBReactionWizardPanel.this.getFindRXTextRadioButton()) 
				connEtoC15(e);
			if (e.getSource() == DBReactionWizardPanel.this.getKeggMoleculeJRadioButton()) 
				connEtoC28(e);
			if (e.getSource() == DBReactionWizardPanel.this.getKeggSpecifyJButton()) 
				connEtoC29(e);
			if (e.getSource() == DBReactionWizardPanel.this.getFindTextJTextField()) 
				connEtoM1(e);
		};
		public void changedUpdate(javax.swing.event.DocumentEvent e) {
			if (e.getDocument() == DBReactionWizardPanel.this.getdocument2()) 
				connEtoC31(e);
		};
		public void insertUpdate(javax.swing.event.DocumentEvent e) {
			if (e.getDocument() == DBReactionWizardPanel.this.getdocument2()) 
				connEtoC31(e);
		};
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			if (evt.getSource() == DBReactionWizardPanel.this.getReactionsJList() && (evt.getPropertyName().equals("selectionModel"))) 
				connPtoP3SetTarget();
			if (evt.getSource() == DBReactionWizardPanel.this.getParameterNamesJList() && (evt.getPropertyName().equals("selectionModel"))) 
				connPtoP4SetTarget();
			if (evt.getSource() == DBReactionWizardPanel.this && (evt.getPropertyName().equals("reactionDescription"))) 
				connEtoC22(evt);
			if (evt.getSource() == DBReactionWizardPanel.this.getParameterValuesJList() && (evt.getPropertyName().equals("selectionModel"))) 
				connPtoP5SetTarget();
			if (evt.getSource() == DBReactionWizardPanel.this.getFindTextJTextField() && (evt.getPropertyName().equals("document"))) 
				connPtoP6SetTarget();
		};
		public void removeUpdate(javax.swing.event.DocumentEvent e) {
			if (e.getDocument() == DBReactionWizardPanel.this.getdocument2()) 
				connEtoC31(e);
		};
		public void valueChanged(javax.swing.event.ListSelectionEvent e) {
			if (e.getSource() == DBReactionWizardPanel.this.getParameterNameSelectionModel()) 
				connEtoC23(e);
			if (e.getSource() == DBReactionWizardPanel.this.getRXDescriptionLSM()) 
				connEtoM6();
			if (e.getSource() == DBReactionWizardPanel.this.getReactionSelectionModel()) 
				connEtoC27(e);
		};
	};

/**
 * DBReactionWizardPanel constructor comment.
 */
public DBReactionWizardPanel() {
	super();
	initialize();
}


/**
 * DBReactionWizardPanel constructor comment.
 * @param layout java.awt.LayoutManager
 */
public DBReactionWizardPanel(java.awt.LayoutManager layout) {
	super(layout);
}


/**
 * DBReactionWizardPanel constructor comment.
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public DBReactionWizardPanel(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
}


/**
 * DBReactionWizardPanel constructor comment.
 * @param isDoubleBuffered boolean
 */
public DBReactionWizardPanel(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
}


	/**
	 * Invoked when an action occurs.
	 */
public void actionPerformed(java.awt.event.ActionEvent e) {


	if(resolvedReaction.isFluxReaction()){
		if(e.getSource() == speciesAssignmentJCB[0]){
			if(speciesAssignmentJCB[1].getSelectedIndex() != speciesAssignmentJCB[0].getSelectedIndex()){
				speciesAssignmentJCB[1].setSelectedIndex(speciesAssignmentJCB[0].getSelectedIndex());
			}
		}else if(e.getSource() == speciesAssignmentJCB[1]){
			if(speciesAssignmentJCB[0].getSelectedIndex() != speciesAssignmentJCB[1].getSelectedIndex()){
				speciesAssignmentJCB[0].setSelectedIndex(speciesAssignmentJCB[1].getSelectedIndex());
			}
		}
	}
	//for(int i=0;i<speciesAssignmentJCB.length;i+= 1){
		//Species species = speciesOrder[speciesAssignmentJCB[i].getSelectedIndex()];
		//Structure structure = null;
		//if(getStructure() instanceof Feature){
			//structure = getStructure();
		//}else if(resolvedReaction.isFluxReaction() && i == 0){
			//structure = ((Membrane)getStructure()).getOutsideFeature();
		//}else if(resolvedReaction.isFluxReaction() && i == 1){
			//structure = ((Membrane)getStructure()).getInsideFeature();
		//}else if(structureAssignmentJCB[i].getSelectedIndex() == 0){
			//structure = getStructure();
		//}else if(structureAssignmentJCB[i].getSelectedIndex() == 1){
			//structure = ((Membrane)getStructure()).getOutsideFeature();
		//}else if(structureAssignmentJCB[i].getSelectedIndex() == 2){
			//structure = ((Membrane)getStructure()).getInsideFeature();
		//}
		//System.out.println(
			//resolvedReaction.getReactionElement(i).getPreferredName()+
			//" assigned to "+(species != null?species.getCommonName():"New Species")+
			//" in structure "+ structure.getName());
	//}
	
}


/**
 * Insert the method's description here.
 * Creation date: (8/18/2003 2:38:49 PM)
 */
private void afterSearchConfigure() {

	if(getReactionsJList().getModel().getSize() == 0){
		lastSearchChangeInfo = null;
		PopupGenerator.showInfoDialog(this, "No Reactions found matching search criteria");
	}else{
		lastSearchSaveInfo();
    	((java.awt.CardLayout) getCardLayoutJPanel().getLayout()).next(getCardLayoutJPanel());
	}
    configureBFN();
}


/**
 * Comment
 */
private void bfnActionPerformed(java.awt.event.ActionEvent actionEvent) {
	//
	javax.swing.DefaultListModel pndlm = (javax.swing.DefaultListModel)getParameterNamesJList().getModel();
	//
	if(actionEvent.getSource().equals(getBackJButton())){
		if(getResolverJPanel().isVisible() && pndlm.size() == 0 && getSearchDictionaryJRadioButton().isSelected() == false){
			//skip Parameters if there are none
			((java.awt.CardLayout)getCardLayoutJPanel().getLayout()).previous(getCardLayoutJPanel());
		}
		((java.awt.CardLayout)getCardLayoutJPanel().getLayout()).previous(getCardLayoutJPanel());
	}else if(actionEvent.getSource().equals(getNextJButton())){
		if(getSearchCriteriaJPanel().isVisible()){
			if(getSearchDictionaryJRadioButton().isSelected()){
				getParameterJPanel().setVisible(false);
			}else{
				getParameterJPanel().setVisible(true);
			}
			if(!lastSearchIsSameAsCurrent()){
				search();
				lastReactionSelection = null;
				return;
			}
		}else if(getParameterJPanel().isVisible()){
			if(lastReactStepSelection == null || !lastReactStepSelection.equals(getReactionStep())){
				lastReactStepSelection = getReactionStep();
				String rxType = null;
				if(getReactionStep() instanceof FluxReaction){
					rxType = DatabaseConstants.REACTTYPE_FLUX;
				}else{
					rxType = DatabaseConstants.REACTTYPE_SIMPLE;
				}
				ReactionDescription dbfr = new ReactionDescription(
						getReactionStep().getName(),rxType,getReactionStep().getKey());
				//
				ReactionParticipant[] rpArr = getReactionStep().getReactionParticipants();
				for(int i=0;i < rpArr.length;i+= 1){
					DBNonFormalUnboundSpecies dbnfu = new DBNonFormalUnboundSpecies(rpArr[i].getSpecies().getCommonName());
					char role;
					if(rpArr[i] instanceof Reactant){
						role = ReactionDescription.RX_ELEMENT_REACTANT;
					}else if(rpArr[i] instanceof Product){
						role = ReactionDescription.RX_ELEMENT_PRODUCT;
					}else if(rpArr[i] instanceof Catalyst){
						role = ReactionDescription.RX_ELEMENT_CATALYST;
					}else if(rpArr[i] instanceof Flux){
						role = ReactionDescription.RX_ELEMENT_FLUX;
					}else{
						throw new RuntimeException("Unsupported ReationParticiapnt="+rpArr[i].getClass().getName());
					}
					dbfr.addReactionElement(dbnfu,rpArr[i].getSpeciesContext().getName(),rpArr[i].getStoichiometry(),role);
				}
				if(dbfr.isFluxReaction()){//make sure flux is in right direction
					Structure outsideStruct = ((Membrane)getReactionStep().getStructure()).getOutsideFeature();
					String defaultOutsideSCName = dbfr.getOrigSpeciesContextName(dbfr.getFluxIndexOutside());
					for(int i=0;i < rpArr.length;i+= 1){
						if(rpArr[i].getSpeciesContext().getName().equals(defaultOutsideSCName)){
							if(!rpArr[i].getStructure().equals(outsideStruct)){
								dbfr.swapFluxSCNames();
							}
							break;
						}
					}
				}
				setupRX(dbfr);
			}
		}
		//
		((java.awt.CardLayout)getCardLayoutJPanel().getLayout()).next(getCardLayoutJPanel());
	}else if(actionEvent.getSource().equals(getFinishJButton())){
		//done();
		resolve2();
	}
	//
	configureBFN();	
}


/**
 * Insert the method's description here.
 * Creation date: (9/18/2003 2:01:32 PM)
 */
private void closeParent() {
    //Try to close whoever contains us
    javax.swing.JInternalFrame jif = (javax.swing.JInternalFrame)BeanUtils.findTypeParentOfComponent(this, javax.swing.JInternalFrame.class);
    if (jif != null) {
        jif.dispose();
    } else {
        java.awt.Window window = (java.awt.Window)BeanUtils.findTypeParentOfComponent(this, java.awt.Window.class);
        if (window != null) {
            window.dispose();
        }
    }
}


/**
 * Insert the method's description here.
 * Creation date: (8/14/2003 2:38:54 PM)
 */
private void configureBFN() {
		
	boolean bBackEnabled = true;
	boolean bFinishEnabled = false;
	boolean bNextEnabled = false;
	
	if(getSearchCriteriaJPanel().isShowing()){
		String sText = getFindTextJTextField().getText();
		sText = (sText != null && sText.length() > 0?sText:null);
		bNextEnabled =
			(getFindRXTextRadioButton().isSelected()?sText != null:false) ||
			(!getFindRXTextRadioButton().isSelected()?getCurrentDBFormalSpecies() != null:false);
		bBackEnabled = false;
	}else if(getSearchResultsJPanel().isShowing() && getSearchDictionaryJRadioButton().isSelected()){
		bNextEnabled = !getReactionsJList().isSelectionEmpty();
	}else if(getParameterJPanel().isShowing()){
		bNextEnabled = getReactionStep() != null;
	}else if(getResolverJPanel().isShowing()){
		bFinishEnabled = true;
	}

	if(getBackJButton().isEnabled() != bBackEnabled){
		getBackJButton().setEnabled(bBackEnabled);
	}
	if(getFinishJButton().isEnabled() != bFinishEnabled){
		getFinishJButton().setEnabled(bFinishEnabled);
	}
	if(getNextJButton().isEnabled() != bNextEnabled){
		getNextJButton().setEnabled(bNextEnabled);
	}
}


/**
 * Comment
 */
private void configureRXParameterList(javax.swing.event.ListSelectionEvent listSelectionEvent) {
	//
	final javax.swing.DefaultListModel pndlm = (javax.swing.DefaultListModel)getParameterNamesJList().getModel();
	//
	if(lastReactionSelection == null || !lastReactionSelection.equals(getReactionsJList().getSelectedValue())){
		lastReactionSelection = getReactionsJList().getSelectedValue();
		setReactionStep(null);
		setReactionDescription(null);
		//
		pndlm.removeAllElements();
		javax.swing.DefaultListModel pvdlm = (javax.swing.DefaultListModel)getParameterValuesJList().getModel();
		pvdlm.removeAllElements();
		//
		if(getReactionsJList().getSelectedValue() instanceof String){//User Reactions
			Vector<String> rxIDV = mapRXStringtoRXIDs.get(getReactionsJList().getSelectedValue());
			//String[] rxIDArr = (String[])rxIDV.toArray(new String[rxIDV.size()]);
			final KeyValue rxKeys[] = new KeyValue[rxIDV.size()];
			for (int i = 0; i < rxKeys.length; i++){
				rxKeys[i] = new KeyValue(rxIDV.elementAt(i));
			}
			AsynchClientTask task1 = new AsynchClientTask("getting user reaction step infos", AsynchClientTask.TASKTYPE_NONSWING_BLOCKING) {
				public void run(Hashtable<String, Object> hashTable) throws Exception {
					ReactionStepInfo reactionStepInfos[] = getDocumentManager().getUserReactionStepInfos(rxKeys);
					hashTable.put("reactionStepInfos", reactionStepInfos);
				}
			};
			AsynchClientTask task2 = new AsynchClientTask("getting user reaction step infos", AsynchClientTask.TASKTYPE_SWING_BLOCKING) {
				public void run(Hashtable<String, Object> hashTable) {
					ReactionStepInfo reactionStepInfos[] = (ReactionStepInfo[])hashTable.get("reactionStepInfos");
					for (int i = 0;reactionStepInfos!=null && i < reactionStepInfos.length; i++){
						String descriptiveText =
							reactionStepInfos[i].getOwner().getName()+" - "+
							"  "+reactionStepInfos[i].getBioModelName()+"  "+
							"("+reactionStepInfos[i].getReactionName()+")"+" "+
							reactionStepInfos[i].getBioModelVersionDate();
							
						pndlm.addElement(new MapStringToObject(descriptiveText/*reactionStepInfos[i].getDescriptiveText()*/,reactionStepInfos[i]));						
					}
				}
			};
			Hashtable<String, Object> hash = new Hashtable<String, Object>();
			AsynchClientTask[] taskArray = new AsynchClientTask[]{task1, task2};
			ClientTaskDispatcher.dispatch(this, hash, taskArray);	
		}else{//Dictionary ReactionDescription
			ReactionDescription dbfr  = (ReactionDescription)getReactionsJList().getSelectedValue();
			setupRX(dbfr);
		}
	}
	//if(pndlm.size() == 0){
		////Skip parameters if there are none
		//((java.awt.CardLayout)getCardLayoutJPanel().getLayout()).next(getCardLayoutJPanel());
	//}
}


/**
 * connEtoC12:  (BackJButton.action.actionPerformed(java.awt.event.ActionEvent) --> DBReactionWizardPanel.bfnActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC12(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.bfnActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoC13:  (FinishJButton.action.actionPerformed(java.awt.event.ActionEvent) --> DBReactionWizardPanel.bfnActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC13(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.bfnActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoC14:  (NextJButton.action.actionPerformed(java.awt.event.ActionEvent) --> DBReactionWizardPanel.bfnActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC14(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.bfnActionPerformed(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoC15:  (FindRXTextRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> DBReactionWizardPanel.findCriteriaChanged(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC15(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.findCriteriaChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoC22:  (DBReactionWizardPanel.ReactionDescription --> DBReactionWizardPanel.configureBFN()V)
 * @param arg1 java.beans.PropertyChangeEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC22(java.beans.PropertyChangeEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.configureBFN();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoC23:  (ParameterNameSelectionModel.listSelection.valueChanged(javax.swing.event.ListSelectionEvent) --> DBReactionWizardPanel.parameterNameSelectionChanged()V)
 * @param arg1 javax.swing.event.ListSelectionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC23(javax.swing.event.ListSelectionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.parameterNameSelectionChanged();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoC26:  (JButton1.action.actionPerformed(java.awt.event.ActionEvent) --> DBReactionWizardPanel.closeParent()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC26(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.closeParent();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoC27:  (ReactionSelectionModel.listSelection.valueChanged(javax.swing.event.ListSelectionEvent) --> DBReactionWizardPanel.configureRXParameterList(Ljavax.swing.event.ListSelectionEvent;)V)
 * @param arg1 javax.swing.event.ListSelectionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC27(javax.swing.event.ListSelectionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.configureRXParameterList(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoC28:  (FindRXTypeJRadioButton.action.actionPerformed(java.awt.event.ActionEvent) --> DBReactionWizardPanel.findCriteriaChanged(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC28(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.findCriteriaChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoC29:  (SpecifyTypeJButton.action.actionPerformed(java.awt.event.ActionEvent) --> DBReactionWizardPanel.showSpeciesBrowser()Lcbit.vcell.dictionary.DBFormalSpecies;)
 * @return cbit.vcell.dictionary.DBFormalSpecies
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private DBFormalSpecies connEtoC29(java.awt.event.ActionEvent arg1) {
	DBFormalSpecies connEtoC29Result = null;
	try {
		// user code begin {1}
		// user code end
		connEtoC29Result = this.showSpeciesBrowser();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
	return connEtoC29Result;
}

/**
 * connEtoC30:  (CurrentDBFormalSpecies.this --> DBReactionWizardPanel.findCriteriaDetailsChanged()V)
 * @param value cbit.vcell.dictionary.DBFormalSpecies
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC30(DBFormalSpecies value) {
	try {
		// user code begin {1}
		// user code end
		this.findCriteriaDetailsChanged();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoC31:  (document2.document. --> DBReactionWizardPanel.findCriteriaDetailsChanged()V)
 * @param evt javax.swing.event.DocumentEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC31(javax.swing.event.DocumentEvent evt) {
	try {
		// user code begin {1}
		// user code end
		this.findCriteriaDetailsChanged();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoC4:  (DBReactionWizardPanel.initialize() --> DBReactionWizardPanel.dBReactionWizardPanel_Initialize()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4() {
	try {
		// user code begin {1}
		// user code end
		this.dBReactionWizardPanel_Initialize();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoM1:  (FindTextJTextField.action.actionPerformed(java.awt.event.ActionEvent) --> NextJButton.doClick(I)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		getNextJButton().doClick(1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connEtoM6:  (RXDescriptionLSM.listSelection. --> ParameterValuesJList.clearSelection()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM6() {
	try {
		// user code begin {1}
		// user code end
		getParameterValuesJList().clearSelection();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connPtoP3SetSource:  (ReactionsJList.selectionModel <--> selectionModel2.this)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP3SetSource() {
	/* Set the source from the target */
	try {
		if (ivjConnPtoP3Aligning == false) {
			// user code begin {1}
			// user code end
			ivjConnPtoP3Aligning = true;
			if ((getReactionSelectionModel() != null)) {
				getReactionsJList().setSelectionModel(getReactionSelectionModel());
			}
			// user code begin {2}
			// user code end
			ivjConnPtoP3Aligning = false;
		}
	} catch (java.lang.Throwable ivjExc) {
		ivjConnPtoP3Aligning = false;
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connPtoP3SetTarget:  (ReactionsJList.selectionModel <--> selectionModel2.this)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP3SetTarget() {
	/* Set the target from the source */
	try {
		if (ivjConnPtoP3Aligning == false) {
			// user code begin {1}
			// user code end
			ivjConnPtoP3Aligning = true;
			setReactionSelectionModel(getReactionsJList().getSelectionModel());
			// user code begin {2}
			// user code end
			ivjConnPtoP3Aligning = false;
		}
	} catch (java.lang.Throwable ivjExc) {
		ivjConnPtoP3Aligning = false;
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connPtoP4SetSource:  (ParameterNamesJList.selectionModel <--> ParameterNameSelectionModel.this)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP4SetSource() {
	/* Set the source from the target */
	try {
		if (ivjConnPtoP4Aligning == false) {
			// user code begin {1}
			// user code end
			ivjConnPtoP4Aligning = true;
			if ((getParameterNameSelectionModel() != null)) {
				getParameterNamesJList().setSelectionModel(getParameterNameSelectionModel());
			}
			// user code begin {2}
			// user code end
			ivjConnPtoP4Aligning = false;
		}
	} catch (java.lang.Throwable ivjExc) {
		ivjConnPtoP4Aligning = false;
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connPtoP4SetTarget:  (ParameterNamesJList.selectionModel <--> ParameterNameSelectionModel.this)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP4SetTarget() {
	/* Set the target from the source */
	try {
		if (ivjConnPtoP4Aligning == false) {
			// user code begin {1}
			// user code end
			ivjConnPtoP4Aligning = true;
			setParameterNameSelectionModel(getParameterNamesJList().getSelectionModel());
			// user code begin {2}
			// user code end
			ivjConnPtoP4Aligning = false;
		}
	} catch (java.lang.Throwable ivjExc) {
		ivjConnPtoP4Aligning = false;
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connPtoP5SetSource:  (ParameterValuesJList.selectionModel <--> RXDescriptionLSM.this)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP5SetSource() {
	/* Set the source from the target */
	try {
		if (ivjConnPtoP5Aligning == false) {
			// user code begin {1}
			// user code end
			ivjConnPtoP5Aligning = true;
			if ((getRXDescriptionLSM() != null)) {
				getParameterValuesJList().setSelectionModel(getRXDescriptionLSM());
			}
			// user code begin {2}
			// user code end
			ivjConnPtoP5Aligning = false;
		}
	} catch (java.lang.Throwable ivjExc) {
		ivjConnPtoP5Aligning = false;
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connPtoP5SetTarget:  (ParameterValuesJList.selectionModel <--> RXDescriptionLSM.this)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP5SetTarget() {
	/* Set the target from the source */
	try {
		if (ivjConnPtoP5Aligning == false) {
			// user code begin {1}
			// user code end
			ivjConnPtoP5Aligning = true;
			setRXDescriptionLSM(getParameterValuesJList().getSelectionModel());
			// user code begin {2}
			// user code end
			ivjConnPtoP5Aligning = false;
		}
	} catch (java.lang.Throwable ivjExc) {
		ivjConnPtoP5Aligning = false;
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connPtoP6SetSource:  (FindTextJTextField.document <--> document2.this)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP6SetSource() {
	/* Set the source from the target */
	try {
		if (ivjConnPtoP6Aligning == false) {
			// user code begin {1}
			// user code end
			ivjConnPtoP6Aligning = true;
			if ((getdocument2() != null)) {
				getFindTextJTextField().setDocument(getdocument2());
			}
			// user code begin {2}
			// user code end
			ivjConnPtoP6Aligning = false;
		}
	} catch (java.lang.Throwable ivjExc) {
		ivjConnPtoP6Aligning = false;
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * connPtoP6SetTarget:  (FindTextJTextField.document <--> document2.this)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connPtoP6SetTarget() {
	/* Set the target from the source */
	try {
		if (ivjConnPtoP6Aligning == false) {
			// user code begin {1}
			// user code end
			ivjConnPtoP6Aligning = true;
			setdocument2(getFindTextJTextField().getDocument());
			// user code begin {2}
			// user code end
			ivjConnPtoP6Aligning = false;
		}
	} catch (java.lang.Throwable ivjExc) {
		ivjConnPtoP6Aligning = false;
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}

/**
 * Comment
 */
private void dBReactionWizardPanel_Initialize() {

	getFindRXButtonGroup().add(getFindRXTextRadioButton());
	getFindRXButtonGroup().add(getKeggMoleculeJRadioButton());
	
	getSearchTypeButtonGroup().add(getSearchUserJRadioButton());
	getSearchTypeButtonGroup().add(getSearchDictionaryJRadioButton());

	getParameterValuesJList().setModel(new javax.swing.DefaultListModel());
	getParameterNamesJList().setModel(new javax.swing.DefaultListModel());

	//((javax.swing.table.DefaultTableModel)getScrollPaneTable().getModel()).addColumn("rcp");
	//((javax.swing.table.DefaultTableModel)getScrollPaneTable().getModel()).addColumn("structure");
	//((javax.swing.table.DefaultTableModel)getScrollPaneTable().getModel()).addColumn("mapped to");
	
}


/**
 * Comment
 */
private void done(Model tempModel,Structure tempStructure) {
	
	Vector<SpeciesContext> newlyAddedSpeciesContexts = new Vector<SpeciesContext>();
	Vector<Species> newlyAddedSpecies = new Vector<Species>();
	Vector<Object[]> speciesWithChangedBindings = new Vector<Object[]>();
	final int SPECIES_INDEX = 0;
	final int ORIG_DBS_INDEX = 1;
	final int CHANGED_DBS_INDEX = 2;
	//
	boolean bClose = false;
	//
	try{
		if(getReactionDescription() != null){
			ReactionDescription dbfr = getReactionDescription();
			String uniqueName = (dbfr.isFluxReaction()?"Flux":"Reaction");
			while(true){
				boolean bUnique = true;
				ReactionStep[] rsArr = tempModel.getReactionSteps();
				for(int i=0;i<rsArr.length;i+= 1){
					if(rsArr[i].getName().equals(uniqueName)){
						bUnique = false;
						break;
					}
				}
				if(bUnique){
					break;
				}
				uniqueName = TokenMangler.getNextEnumeratedToken(uniqueName);
			}
			//Create RX components and determine which must be newly added to model
			Vector<SpeciesContext> speciesContextV = new Vector<SpeciesContext>();
			Vector<Character> scrxTypeV = new Vector<Character>();
			Vector<Integer> scStoichV = new Vector<Integer>();
			Species newFluxSpecies = null;
			for(int i = 0;i < dbfr.elementCount();i+= 1){
				Species currentSpecies = dbfr.getResolved(i).getSpecies();
				Structure currentStructure = dbfr.getResolved(i).getStructure();
				if(dbfr.isFlux(i) && newFluxSpecies != null){
					currentSpecies = newFluxSpecies;
				}else if(!tempModel.contains(currentSpecies)){
					//No mapping from user so we must add new Species
					newlyAddedSpecies.add(currentSpecies);
					//Make sure species name doesn't conflict
					while(tempModel.getSpecies(currentSpecies.getCommonName()) != null){
						currentSpecies.setCommonName(TokenMangler.getNextEnumeratedToken(currentSpecies.getCommonName()));
					}
					if(dbfr.isFlux(i)){
						newFluxSpecies = currentSpecies;
					}
				}else {
					SpeciesDescription speciesDescription = dbfr.getReactionElement(i);
					if(speciesDescription instanceof DBFormalSpecies){
						DBSpecies reDBSpecies =	getDocumentManager().getBoundSpecies((DBFormalSpecies)speciesDescription);
						if(currentSpecies.getDBSpecies() != null){
							if(!currentSpecies.getDBSpecies().compareEqual(reDBSpecies)){
								String message = "Error Mapping RXParticipant:\n\n"+
								"Existing Species '"+currentSpecies.getCommonName()+
								"' has binding \n'"+
								currentSpecies.getDBSpecies().getFormalSpeciesInfo().getFormalID()+" : "+
								currentSpecies.getDBSpecies().getFormalSpeciesInfo().getPreferredName()+
								"'\n but mapped ReactionElement has binding\n'"+
								reDBSpecies.getFormalSpeciesInfo().getFormalID()+" : "+
								reDBSpecies.getFormalSpeciesInfo().getPreferredName()+"'";

								PopupGenerator.showInfoDialog(this, message);
								//bClose = false;
								return;
							}
						}else{
							Object[] sbc = new Object[3];
							sbc[SPECIES_INDEX] = currentSpecies;
							sbc[ORIG_DBS_INDEX] = currentSpecies.getDBSpecies();
							sbc[CHANGED_DBS_INDEX] = reDBSpecies;
							speciesWithChangedBindings.add(sbc);
						}
					}
				}
				SpeciesContext currentSpeciesContext = null;
				if(tempModel.contains(currentStructure) && tempModel.contains(currentSpecies)){
					currentSpeciesContext = tempModel.getSpeciesContext(currentSpecies,currentStructure);
				}
				if(currentSpeciesContext == null){
					currentSpeciesContext = dbfr.getResolved(i);//new SpeciesContext(currentSpecies,currentStructure);
					newlyAddedSpeciesContexts.add(currentSpeciesContext);
				}
				speciesContextV.add(currentSpeciesContext);
				scrxTypeV.add(new Character(dbfr.getType(i)));
				scStoichV.add(new Integer(dbfr.getStoich(i)));
			}
			////Add new species to Model
			//for(int i=0;i<newlyAddedSpecies.size();i+= 1){
				//tempModel.addSpecies((Species)newlyAddedSpecies.get(i));
			//}
			////Add new SpeciesContexts to Model
			//for(int i=0;i<newlyAddedSpeciesContexts.size();i+= 1){
				//tempModel.addSpeciesContext((SpeciesContext)newlyAddedSpeciesContexts.get(i));
			//}
			////Add RX Species Bindings to Model Species if necessary
			//for(int i = 0;i < speciesWithChangedBindings.size();i+= 1){
				//Object[] sbc = (Object[])speciesWithChangedBindings.get(i);
				//Species changedSpecies = (Species)sbc[SPECIES_INDEX];
				//cbit.vcell.dictionary.DBSpecies changedDBSpecies = (cbit.vcell.dictionary.DBSpecies)sbc[CHANGED_DBS_INDEX];
				//changedSpecies.setDBSpecies(changedDBSpecies);
			//}
			//
			//
			ReactionStep reaction = null;
			if(getReactionStep() == null){//Must have been from the EnzymeReaction dictionary, has no Kinetics
				//Create Default Kinetics for Dictionary Reaction
				reaction = new SimpleReaction(tempStructure,uniqueName);
				
				tempModel.addReactionStep(reaction);
				for(int i=0;i<newlyAddedSpecies.size();i+= 1){
					tempModel.addSpecies(newlyAddedSpecies.get(i));
				}
				//Add new SpeciesContexts to Model
				for(int i=0;i<newlyAddedSpeciesContexts.size();i+= 1){
					tempModel.addSpeciesContext(newlyAddedSpeciesContexts.get(i));
				}
				//Add RX Species Bindings to Model Species if necessary
				for(int i = 0;i < speciesWithChangedBindings.size();i+= 1){
					Object[] sbc = (Object[])speciesWithChangedBindings.get(i);
					Species changedSpecies = (Species)sbc[SPECIES_INDEX];
					DBSpecies changedDBSpecies = (DBSpecies)sbc[CHANGED_DBS_INDEX];
					changedSpecies.setDBSpecies(changedDBSpecies);
				}

				//Add Components to Reaction
				for(int i=0;i<speciesContextV.size();i+= 1){
					SpeciesContext sc = speciesContextV.get(i);
					char rxType = scrxTypeV.get(i);
					int rxStoich = scStoichV.get(i);
					//
					if(rxType == ReactionDescription.RX_ELEMENT_CATALYST){
						reaction.addCatalyst(sc);
					}else if(rxType == ReactionDescription.RX_ELEMENT_FLUX){
						((FluxReaction)reaction).setFluxCarrier(sc.getSpecies(),tempModel);
					}else if(rxType == ReactionDescription.RX_ELEMENT_PRODUCT){
						((SimpleReaction)reaction).addProduct(sc,rxStoich);
					}else if(rxType == ReactionDescription.RX_ELEMENT_REACTANT){
						((SimpleReaction)reaction).addReactant(sc,rxStoich);
					}else{
						throw new Exception("Unknown ReactionElementType="+dbfr.getType(i)+", should be (C)atalyst,(F)lux,(R)eactant,(P)roduct");
					}
				}
				//
				Kinetics kinetics = new HMM_IRRKinetics(reaction);
				Expression kmExpression = new Expression("1.0");
				Expression vmaxExpression = new Expression("1.0");
				kinetics.setParameterValue(((HMM_IRRKinetics)kinetics).getKmParameter(),kmExpression);
				kinetics.setParameterValue(((HMM_IRRKinetics)kinetics).getVmaxParameter(),vmaxExpression);
				//reaction = new SimpleReaction(tempStructure,uniqueName);
				reaction.setKinetics(kinetics);
				
				reaction.refreshDependencies();
				BioCartoonTool.pasteReactionSteps(
						new ReactionStep[] {reaction},getModel(), getStructure(), false,false,this);

			}else{//Must be user reaction with kinetics
				reaction = getReactionStep();
				
				for(int i=0;i<newlyAddedSpecies.size();i+= 1){
					tempModel.addSpecies(newlyAddedSpecies.get(i));
				}
				//Add new SpeciesContexts to Model
				for(int i=0;i<newlyAddedSpeciesContexts.size();i+= 1){
					tempModel.addSpeciesContext(newlyAddedSpeciesContexts.get(i));
				}
				//Add RX Species Bindings to Model Species if necessary
				for(int i = 0;i < speciesWithChangedBindings.size();i+= 1){
					Object[] sbc = (Object[])speciesWithChangedBindings.get(i);
					Species changedSpecies = (Species)sbc[SPECIES_INDEX];
					DBSpecies changedDBSpecies = (DBSpecies)sbc[CHANGED_DBS_INDEX];
					changedSpecies.setDBSpecies(changedDBSpecies);
				}

				reaction.refreshDependencies();
				reaction.setName(uniqueName);
				reaction.setStructure(tempStructure);
				reaction.refreshDependencies();
//				//
//				//Make sure Kinetics parameters don't conflict
				Kinetics kinetics = reaction.getKinetics();
//				Kinetics.KineticsParameter[] kpArr = kinetics.getKineticsParameters();
//				for(int i=0;i < kpArr.length;i+= 1){
//					String kpName = kpArr[i].getName();
////					Kinetics.KineticsParameter kp = null;
//					while(tempModel.getKineticsParameter(kpName) != null){
//						String newKPName = org.vcell.util.TokenMangler.getNextEnumeratedToken(kpName);
//						kinetics.renameParameter(kpName,newKPName);
//						kpName = newKPName;
//					}
//				}
				//change old rxParticpant contextNames to new contextNames
				ReactionParticipant[] rpArr = reaction.getReactionParticipants();
				for(int i=0;i< rpArr.length;i+= 1){
					for(int j=0;j< dbfr.elementCount();j+= 1){
						if(dbfr.getOrigSpeciesContextName(j).equals(rpArr[i].getSpeciesContext().getName())){
							rpArr[i].setSpeciesContext(dbfr.getResolved(j));
						}
					}
				}
				////
				////Remove current if not in membrane -or- Add current if in membrane
				////
				//if(kinetics instanceof HMM_IRRKinetics){
					//((HMM_IRRKinetics)kinetics).resolveCurrentWithStructure(tempStructure);
				//}
				kinetics.resolveCurrentWithStructure(tempStructure);
				
				reaction.refreshDependencies();

			}
//			reaction.refreshDependencies();
//			//
//			//Now Add everything to the Model
//			//
//			//Add new species to Model
//			for(int i=0;i<newlyAddedSpecies.size();i+= 1){
//				tempModel.addSpecies(newlyAddedSpecies.get(i));
//			}
//			//Add new SpeciesContexts to Model
//			for(int i=0;i<newlyAddedSpeciesContexts.size();i+= 1){
//				tempModel.addSpeciesContext(newlyAddedSpeciesContexts.get(i));
//			}
//			//Add RX Species Bindings to Model Species if necessary
//			for(int i = 0;i < speciesWithChangedBindings.size();i+= 1){
//				Object[] sbc = (Object[])speciesWithChangedBindings.get(i);
//				Species changedSpecies = (Species)sbc[SPECIES_INDEX];
//				DBSpecies changedDBSpecies = (DBSpecies)sbc[CHANGED_DBS_INDEX];
//				changedSpecies.setDBSpecies(changedDBSpecies);
//			}
//			//Add Reaction
//			tempModel.addReactionStep(reaction);
			BioCartoonTool.pasteReactionSteps(
					new ReactionStep[] {reaction},getModel(), getStructure(), false,false,this);
		}
		

		bClose = true;
		
	}catch(Exception e){
		e.printStackTrace();
		PopupGenerator.showErrorDialog(this,"Error adding reaction: \n"+
				e.getClass().getName()+"\n"+e.getMessage());
	}finally{
		if(bClose){closeParent();}
	}
	
}


/**
 * Comment
 */
private void findCriteriaChanged(java.awt.event.ActionEvent actionEvent) {
	
	if(getFindRXTextRadioButton().isSelected()){
		getFindTextJTextField().setEnabled(true);
		getKeggSpecifyJButton().setEnabled(false);
		getKeggTypeJLabel().setEnabled(false);
	}else if(getKeggMoleculeJRadioButton().isSelected()){
		getFindTextJTextField().setEnabled(false);
		getKeggSpecifyJButton().setEnabled(true);
		getKeggTypeJLabel().setEnabled(true);
	}
	configureBFN();
}


/**
 * Comment
 */
private void findCriteriaDetailsChanged() {
	
	if(getKeggMoleculeJRadioButton().isSelected()){
		getKeggTypeJLabel().setText("Current: "+(getCurrentDBFormalSpecies() == null?"None Specified":getCurrentDBFormalSpecies().getFormalSpeciesInfo().toString()));
	}
	configureBFN();
}




/**
 * Insert the method's description here.
 * Creation date: (8/4/2003 2:40:51 PM)
 * @return java.lang.String
 * @param origS java.lang.String
 */
private String formatLikeString(String origS) {
	
	if(origS == null || origS.length() == 0){
		return null;
	}
	
	StringBuffer sb = new StringBuffer(origS);
	for(int i=0;i<sb.length();i+= 1){
		if(sb.charAt(i) == '*'){
			sb.replace(i,i+1,"%");
		}
	}

	origS = sb.toString();
	if(origS.indexOf("%") == -1 && origS.indexOf("_") == -1){
		origS = "%"+origS+"%";
	}
	//The character "%" matches any string of zero or more characters except null.
	//The character "_" matches any single character.
	//A wildcard character is treated as a literal if preceded by the character designated as the escape character.
	//Default ESCAPE character for VCell = '/' defined in DictionaryDbDriver.getDatabaseSpecies

	return origS;
}


/**
 * Return the BackJButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getBackJButton() {
	if (ivjBackJButton == null) {
		try {
			ivjBackJButton = new javax.swing.JButton();
			ivjBackJButton.setName("BackJButton");
			ivjBackJButton.setText("Back");
			ivjBackJButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBackJButton;
}


/**
 * Return the BFNJPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getBFNJPanel() {
	if (ivjBFNJPanel == null) {
		try {
			ivjBFNJPanel = new javax.swing.JPanel();
			ivjBFNJPanel.setName("BFNJPanel");
			ivjBFNJPanel.setLayout(new java.awt.GridBagLayout());

			java.awt.GridBagConstraints constraintsBackJButton = new java.awt.GridBagConstraints();
			constraintsBackJButton.gridx = 0; constraintsBackJButton.gridy = 0;
			constraintsBackJButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBFNJPanel().add(getBackJButton(), constraintsBackJButton);

			java.awt.GridBagConstraints constraintsFinishJButton = new java.awt.GridBagConstraints();
			constraintsFinishJButton.gridx = 1; constraintsFinishJButton.gridy = 0;
			constraintsFinishJButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBFNJPanel().add(getFinishJButton(), constraintsFinishJButton);

			java.awt.GridBagConstraints constraintsNextJButton = new java.awt.GridBagConstraints();
			constraintsNextJButton.gridx = 2; constraintsNextJButton.gridy = 0;
			constraintsNextJButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getBFNJPanel().add(getNextJButton(), constraintsNextJButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBFNJPanel;
}


/**
 * Return the CardLayouJPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getCardLayoutJPanel() {
	if (ivjCardLayoutJPanel == null) {
		try {
			ivjCardLayoutJPanel = new javax.swing.JPanel();
			ivjCardLayoutJPanel.setName("CardLayoutJPanel");
			ivjCardLayoutJPanel.setLayout(new java.awt.CardLayout());
			getCardLayoutJPanel().add(getSearchCriteriaJPanel(), getSearchCriteriaJPanel().getName());
			getCardLayoutJPanel().add(getSearchResultsJPanel(), getSearchResultsJPanel().getName());
			getCardLayoutJPanel().add(getResolverJPanel(), getResolverJPanel().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCardLayoutJPanel;
}

/**
 * Return the CurrentDBFormalSpecies property value.
 * @return cbit.vcell.dictionary.DBFormalSpecies
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private DBFormalSpecies getCurrentDBFormalSpecies() {
	// user code begin {1}
	// user code end
	return ivjCurrentDBFormalSpecies;
}


/**
 * Return the document2 property value.
 * @return javax.swing.text.Document
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.text.Document getdocument2() {
	// user code begin {1}
	// user code end
	return ivjdocument2;
}


/**
 * Gets the documentManager property (cbit.vcell.clientdb.DocumentManager) value.
 * @return The documentManager property value.
 * @see #setDocumentManager
 */
public DocumentManager getDocumentManager() {
	return fieldDocumentManager;
}


/**
 * Return the FindRXButtonGroup property value.
 * @return javax.swing.ButtonGroup
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.ButtonGroup getFindRXButtonGroup() {
	if (ivjFindRXButtonGroup == null) {
		try {
			ivjFindRXButtonGroup = new javax.swing.ButtonGroup();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFindRXButtonGroup;
}


/**
 * Return the SearchUserJRadioButton1 property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getFindRXTextRadioButton() {
	if (ivjFindRXTextRadioButton == null) {
		try {
			ivjFindRXTextRadioButton = new javax.swing.JRadioButton();
			ivjFindRXTextRadioButton.setName("FindRXTextRadioButton");
			ivjFindRXTextRadioButton.setSelected(true);
			ivjFindRXTextRadioButton.setText("Species name containing text (* = any string):");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFindRXTextRadioButton;
}

/**
 * Return the JTextField1 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getFindTextJTextField() {
	if (ivjFindTextJTextField == null) {
		try {
			ivjFindTextJTextField = new javax.swing.JTextField();
			ivjFindTextJTextField.setName("FindTextJTextField");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFindTextJTextField;
}

/**
 * Return the FinishJButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getFinishJButton() {
	if (ivjFinishJButton == null) {
		try {
			ivjFinishJButton = new javax.swing.JButton();
			ivjFinishJButton.setName("FinishJButton");
			ivjFinishJButton.setText("Finish");
			ivjFinishJButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjFinishJButton;
}


/**
 * Return the JButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getJButton1() {
	if (ivjJButton1 == null) {
		try {
			ivjJButton1 = new javax.swing.JButton();
			ivjJButton1.setName("JButton1");
			ivjJButton1.setText("Cancel");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJButton1;
}


/**
 * Return the JLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new javax.swing.JLabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setText("Reaction Details");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel1;
}

/**
 * Return the JLabel2 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel2() {
	if (ivjJLabel2 == null) {
		try {
			ivjJLabel2 = new javax.swing.JLabel();
			ivjJLabel2.setName("JLabel2");
			ivjJLabel2.setText("Reaction Version(s)");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel2;
}

/**
 * Return the JPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPanel() {
	if (ivjJPanel == null) {
		try {
			LineBorderBean ivjLocalBorder4 = new LineBorderBean();
			TitledBorderBean ivjLocalBorder3 = new TitledBorderBean();
			ivjLocalBorder3.setTitleFont(getFont().deriveFont(Font.BOLD));
			ivjLocalBorder3.setBorder(ivjLocalBorder4);
			ivjLocalBorder3.setTitleColor(java.awt.Color.black);
			ivjLocalBorder3.setTitle("2.  What to Search");
			ivjJPanel = new javax.swing.JPanel();
			ivjJPanel.setName("JPanel");
			ivjJPanel.setBorder(ivjLocalBorder3);
			ivjJPanel.setLayout(new java.awt.GridBagLayout());

			java.awt.GridBagConstraints constraintsFindRXTextRadioButton = new java.awt.GridBagConstraints();
			constraintsFindRXTextRadioButton.gridx = 0; constraintsFindRXTextRadioButton.gridy = 0;
			constraintsFindRXTextRadioButton.gridwidth = 2;
			constraintsFindRXTextRadioButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsFindRXTextRadioButton.anchor = java.awt.GridBagConstraints.WEST;
			constraintsFindRXTextRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPanel().add(getFindRXTextRadioButton(), constraintsFindRXTextRadioButton);

			java.awt.GridBagConstraints constraintsKeggMoleculeJRadioButton = new java.awt.GridBagConstraints();
			constraintsKeggMoleculeJRadioButton.gridx = 0; constraintsKeggMoleculeJRadioButton.gridy = 2;
			constraintsKeggMoleculeJRadioButton.anchor = java.awt.GridBagConstraints.WEST;
			constraintsKeggMoleculeJRadioButton.insets = new java.awt.Insets(4, 4, 4, 0);
			getJPanel().add(getKeggMoleculeJRadioButton(), constraintsKeggMoleculeJRadioButton);

			java.awt.GridBagConstraints constraintsFindTextJTextField = new java.awt.GridBagConstraints();
			constraintsFindTextJTextField.gridx = 0; constraintsFindTextJTextField.gridy = 1;
			constraintsFindTextJTextField.gridwidth = 2;
			constraintsFindTextJTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsFindTextJTextField.weightx = 1.0;
			constraintsFindTextJTextField.insets = new java.awt.Insets(4, 20, 20, 4);
			getJPanel().add(getFindTextJTextField(), constraintsFindTextJTextField);

			java.awt.GridBagConstraints constraintsKeggSpecifyJButton = new java.awt.GridBagConstraints();
			constraintsKeggSpecifyJButton.gridx = 1; constraintsKeggSpecifyJButton.gridy = 2;
//			constraintsKeggSpecifyJButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsKeggSpecifyJButton.anchor = GridBagConstraints.WEST;
			constraintsKeggSpecifyJButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPanel().add(getKeggSpecifyJButton(), constraintsKeggSpecifyJButton);

			java.awt.GridBagConstraints constraintsKeggTypeJLabel = new java.awt.GridBagConstraints();
			constraintsKeggTypeJLabel.gridx = 0; constraintsKeggTypeJLabel.gridy = 3;
			constraintsKeggTypeJLabel.gridwidth = 2;
			constraintsKeggTypeJLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsKeggTypeJLabel.anchor = java.awt.GridBagConstraints.WEST;
			constraintsKeggTypeJLabel.insets = new java.awt.Insets(0, 20, 20, 4);
			getJPanel().add(getKeggTypeJLabel(), constraintsKeggTypeJLabel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPanel;
}

/**
 * Return the JPanel1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPanel1() {
	if (ivjJPanel1 == null) {
		try {
			LineBorderBean ivjLocalBorder2 = new LineBorderBean();
			TitledBorderBean ivjLocalBorder1 = new TitledBorderBean();
			ivjLocalBorder1.setTitleFont(getFont().deriveFont(Font.BOLD));
//			ivjLocalBorder1.setTitleFont(new java.awt.Font("Arial", 1, 14));
			ivjLocalBorder1.setBorder(ivjLocalBorder2);
			ivjLocalBorder1.setTitleColor(java.awt.Color.black);
			ivjLocalBorder1.setTitle("1.  Where to Search");
			ivjJPanel1 = new javax.swing.JPanel();
			ivjJPanel1.setName("JPanel1");
			ivjJPanel1.setBorder(ivjLocalBorder1);
			ivjJPanel1.setLayout(new FlowLayout(FlowLayout.LEADING));

			ivjJPanel1.add(getSearchUserJRadioButton());
			ivjJPanel1.add(Box.createRigidArea(new Dimension(20, 5)));
			ivjJPanel1.add(getSearchDictionaryJRadioButton());
			
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}
	return ivjJPanel1;
}

/**
 * Return the JScrollPane1 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getJScrollPane1() {
	if (ivjJScrollPane1 == null) {
		try {
			ivjJScrollPane1 = new javax.swing.JScrollPane();
			ivjJScrollPane1.setName("JScrollPane1");
			getJScrollPane1().setViewportView(getReactionCanvas1());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollPane1;
}


/**
 * Return the JScrollPane2 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getJScrollPane2() {
	if (ivjJScrollPane2 == null) {
		try {
			ivjJScrollPane2 = new javax.swing.JScrollPane();
			ivjJScrollPane2.setName("JScrollPane2");
			getJScrollPane2().setViewportView(getRXParticipantsJPanel());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollPane2;
}


/**
 * Return the JScrollPane3 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getJScrollPane3() {
	if (ivjJScrollPane3 == null) {
		try {
			ivjJScrollPane3 = new javax.swing.JScrollPane();
			ivjJScrollPane3.setName("JScrollPane3");
			getJScrollPane3().setViewportView(getReactionsJList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollPane3;
}


/**
 * Return the SearchDictionaryJRadioButton1 property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getKeggMoleculeJRadioButton() {
	if (ivjKeggMoleculeJRadioButton == null) {
		try {
			ivjKeggMoleculeJRadioButton = new javax.swing.JRadioButton();
			ivjKeggMoleculeJRadioButton.setName("KeggMoleculeJRadioButton");
			ivjKeggMoleculeJRadioButton.setText("KEGG Molecule / SWISSPROT Protein");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjKeggMoleculeJRadioButton;
}

/**
 * Return the JButton2 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getKeggSpecifyJButton() {
	if (ivjKeggSpecifyJButton == null) {
		try {
			ivjKeggSpecifyJButton = new javax.swing.JButton();
			ivjKeggSpecifyJButton.setName("KeggSpecifyJButton");
			ivjKeggSpecifyJButton.setText("Specify...");
			ivjKeggSpecifyJButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjKeggSpecifyJButton;
}

/**
 * Return the JLabel6 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getKeggTypeJLabel() {
	if (ivjKeggTypeJLabel == null) {
		try {
			ivjKeggTypeJLabel = new javax.swing.JLabel();
			ivjKeggTypeJLabel.setName("KeggTypeJLabel");
			ivjKeggTypeJLabel.setFont(ivjKeggTypeJLabel.getFont().deriveFont(Font.BOLD));
			ivjKeggTypeJLabel.setText("Current: None Specified");
			ivjKeggTypeJLabel.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjKeggTypeJLabel;
}

/**
 * Gets the model property (cbit.vcell.model.Model) value.
 * @return The model property value.
 * @see #setModel
 */
public Model getModel() {
	return fieldModel;
}


/**
 * Return the NextJButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getNextJButton() {
	if (ivjNextJButton == null) {
		try {
			ivjNextJButton = new javax.swing.JButton();
			ivjNextJButton.setName("NextJButton");
			ivjNextJButton.setText("Next");
			ivjNextJButton.setEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjNextJButton;
}


/**
 * Return the ParameterJPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getParameterJPanel() {
	if (ivjParameterJPanel == null) {
		try {
			LineBorderBean ivjLocalBorder8 = new LineBorderBean();
			TitledBorderBean ivjLocalBorder7 = new TitledBorderBean();
			ivjLocalBorder7.setTitleFont(getFont().deriveFont(Font.BOLD));
			ivjLocalBorder7.setBorder(ivjLocalBorder8);
			ivjLocalBorder7.setTitleColor(java.awt.Color.black);
			ivjLocalBorder7.setTitle("2.  Choose a Version of the Selected Reaction");
			ivjParameterJPanel = new javax.swing.JPanel();
			ivjParameterJPanel.setName("ParameterJPanel");
			ivjParameterJPanel.setBorder(ivjLocalBorder7);
			ivjParameterJPanel.setLayout(new java.awt.GridBagLayout());

			java.awt.GridBagConstraints constraintsParameterNamesJScrollPane = new java.awt.GridBagConstraints();
			constraintsParameterNamesJScrollPane.gridx = 0; constraintsParameterNamesJScrollPane.gridy = 1;
			constraintsParameterNamesJScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsParameterNamesJScrollPane.weightx = 1.0;
			constraintsParameterNamesJScrollPane.weighty = 1.0;
			constraintsParameterNamesJScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getParameterJPanel().add(getParameterNamesJScrollPane(), constraintsParameterNamesJScrollPane);

			java.awt.GridBagConstraints constraintsParameterValuesJScrollPane = new java.awt.GridBagConstraints();
			constraintsParameterValuesJScrollPane.gridx = 1; constraintsParameterValuesJScrollPane.gridy = 1;
			constraintsParameterValuesJScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsParameterValuesJScrollPane.weightx = 1.0;
			constraintsParameterValuesJScrollPane.weighty = 1.0;
			constraintsParameterValuesJScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getParameterJPanel().add(getParameterValuesJScrollPane(), constraintsParameterValuesJScrollPane);

			java.awt.GridBagConstraints constraintsJLabel1 = new java.awt.GridBagConstraints();
			constraintsJLabel1.gridx = 1; constraintsJLabel1.gridy = 0;
			constraintsJLabel1.weightx = 1.0;
			constraintsJLabel1.insets = new java.awt.Insets(4, 4, 4, 4);
			getParameterJPanel().add(getJLabel1(), constraintsJLabel1);

			java.awt.GridBagConstraints constraintsJLabel2 = new java.awt.GridBagConstraints();
			constraintsJLabel2.gridx = 0; constraintsJLabel2.gridy = 0;
			constraintsJLabel2.insets = new java.awt.Insets(4, 4, 4, 4);
			getParameterJPanel().add(getJLabel2(), constraintsJLabel2);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjParameterJPanel;
}

/**
 * Return the ParameterNameSelectionModel property value.
 * @return javax.swing.ListSelectionModel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.ListSelectionModel getParameterNameSelectionModel() {
	// user code begin {1}
	// user code end
	return ivjParameterNameSelectionModel;
}


/**
 * Return the ParameterNamesJList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getParameterNamesJList() {
	if (ivjParameterNamesJList == null) {
		try {
			ivjParameterNamesJList = new javax.swing.JList();
			ivjParameterNamesJList.setName("ParameterNamesJList");
			ivjParameterNamesJList.setBounds(0, 0, 160, 120);
			ivjParameterNamesJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjParameterNamesJList;
}

/**
 * Return the ParameterNamesJScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getParameterNamesJScrollPane() {
	if (ivjParameterNamesJScrollPane == null) {
		try {
			ivjParameterNamesJScrollPane = new javax.swing.JScrollPane();
			ivjParameterNamesJScrollPane.setName("ParameterNamesJScrollPane");
			getParameterNamesJScrollPane().setViewportView(getParameterNamesJList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjParameterNamesJScrollPane;
}


/**
 * Return the ParameterValuesJList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getParameterValuesJList() {
	if (ivjParameterValuesJList == null) {
		try {
			ivjParameterValuesJList = new javax.swing.JList();
			ivjParameterValuesJList.setName("ParameterValuesJList");
			ivjParameterValuesJList.setBounds(0, 0, 160, 120);
			ivjParameterValuesJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjParameterValuesJList;
}

/**
 * Return the ParameterValuesJScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getParameterValuesJScrollPane() {
	if (ivjParameterValuesJScrollPane == null) {
		try {
			ivjParameterValuesJScrollPane = new javax.swing.JScrollPane();
			ivjParameterValuesJScrollPane.setName("ParameterValuesJScrollPane");
			getParameterValuesJScrollPane().setViewportView(getParameterValuesJList());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjParameterValuesJScrollPane;
}


/**
 * Return the ReactionCanvas1 property value.
 * @return cbit.vcell.model.gui.ReactionCanvas
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private ReactionCanvas getReactionCanvas1() {
	if (ivjReactionCanvas1 == null) {
		try {
			LineBorderBean ivjLocalBorder12 = new LineBorderBean();
			TitledBorderBean ivjLocalBorder11 = new TitledBorderBean();
			ivjLocalBorder11.setTitleFont(getFont().deriveFont(Font.BOLD));
			ivjLocalBorder11.setBorder(ivjLocalBorder12);
			ivjLocalBorder11.setTitle("Reaction Stoichiometry");
			ivjReactionCanvas1 = new ReactionCanvas();
			ivjReactionCanvas1.setName("ReactionCanvas1");
			ivjReactionCanvas1.setBorder(ivjLocalBorder11);
			ivjReactionCanvas1.setLocation(0, 0);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjReactionCanvas1;
}

/**
 * Gets the reactionDescription property (cbit.vcell.dictionary.ReactionDescription) value.
 * @return The reactionDescription property value.
 * @see #setReactionDescription
 */
public ReactionDescription getReactionDescription() {
	return fieldReactionDescription;
}


/**
 * Return the selectionModel2 property value.
 * @return javax.swing.ListSelectionModel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.ListSelectionModel getReactionSelectionModel() {
	// user code begin {1}
	// user code end
	return ivjReactionSelectionModel;
}


/**
 * Return the ReactionsJList property value.
 * @return javax.swing.JList
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JList getReactionsJList() {
	if (ivjReactionsJList == null) {
		try {
			ivjReactionsJList = new javax.swing.JList();
			ivjReactionsJList.setName("ReactionsJList");
			ivjReactionsJList.setBounds(0, 0, 160, 120);
			ivjReactionsJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjReactionsJList;
}

/**
 * Gets the reactionStep property (cbit.vcell.model.ReactionStep) value.
 * @return The reactionStep property value.
 * @see #setReactionStep
 */
public ReactionStep getReactionStep() {
	return fieldReactionStep;
}


/**
 * Return the ResolveHighlightJLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getResolveHighlightJLabel() {
	if (ivjResolveHighlightJLabel == null) {
		try {
			ivjResolveHighlightJLabel = new javax.swing.JLabel();
			ivjResolveHighlightJLabel.setName("ResolveHighlightJLabel");
			ivjResolveHighlightJLabel.setText(" ");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjResolveHighlightJLabel;
}


/**
 * Return the ResolverJPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getResolverJPanel() {
	if (ivjResolverJPanel == null) {
		try {
			LineBorderBean ivjLocalBorder10 = new LineBorderBean();
			TitledBorderBean ivjLocalBorder9 = new TitledBorderBean();
			ivjLocalBorder9.setTitleFont(getFont().deriveFont(Font.BOLD));
			ivjLocalBorder9.setBorder(ivjLocalBorder10);
			ivjLocalBorder9.setTitle("Resolve Reaction Participants with Model");
			ivjResolverJPanel = new javax.swing.JPanel();
			ivjResolverJPanel.setName("ResolverJPanel");
			ivjResolverJPanel.setBorder(ivjLocalBorder9);
			ivjResolverJPanel.setLayout(new java.awt.GridBagLayout());

			java.awt.GridBagConstraints constraintsResolveHighlightJLabel = new java.awt.GridBagConstraints();
			constraintsResolveHighlightJLabel.gridx = 0; constraintsResolveHighlightJLabel.gridy = 2;
			constraintsResolveHighlightJLabel.gridwidth = 2;
			constraintsResolveHighlightJLabel.insets = new java.awt.Insets(4, 4, 4, 4);
			getResolverJPanel().add(getResolveHighlightJLabel(), constraintsResolveHighlightJLabel);

			java.awt.GridBagConstraints constraintsJScrollPane1 = new java.awt.GridBagConstraints();
			constraintsJScrollPane1.gridx = 0; constraintsJScrollPane1.gridy = 0;
			constraintsJScrollPane1.gridwidth = 2;
			constraintsJScrollPane1.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJScrollPane1.weightx = 1.0;
			constraintsJScrollPane1.weighty = 1.0;
			constraintsJScrollPane1.insets = new java.awt.Insets(4, 4, 4, 4);
			getResolverJPanel().add(getJScrollPane1(), constraintsJScrollPane1);

			java.awt.GridBagConstraints constraintsJScrollPane2 = new java.awt.GridBagConstraints();
			constraintsJScrollPane2.gridx = 0; constraintsJScrollPane2.gridy = 1;
			constraintsJScrollPane2.gridwidth = 2;
			constraintsJScrollPane2.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJScrollPane2.weightx = 1.0;
			constraintsJScrollPane2.weighty = 1.0;
			constraintsJScrollPane2.insets = new java.awt.Insets(4, 4, 4, 4);
			getResolverJPanel().add(getJScrollPane2(), constraintsJScrollPane2);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjResolverJPanel;
}

/**
 * Return the RXDescriptionLSM property value.
 * @return javax.swing.ListSelectionModel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.ListSelectionModel getRXDescriptionLSM() {
	// user code begin {1}
	// user code end
	return ivjRXDescriptionLSM;
}


/**
 * Return the JPanel3 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getRXParticipantsJPanel() {
	if (ivjRXParticipantsJPanel == null) {
		try {
			LineBorderBean ivjLocalBorder14 = new LineBorderBean();
//			ivjLocalBorder14.setThickness(2);
			TitledBorderBean ivjLocalBorder13 = new TitledBorderBean();
			ivjLocalBorder13.setTitleFont(getFont().deriveFont(Font.BOLD));
			ivjLocalBorder13.setBorder(ivjLocalBorder14);
			ivjLocalBorder13.setTitle("Assign Reaction Participants To Model");
			ivjRXParticipantsJPanel = new javax.swing.JPanel();
			ivjRXParticipantsJPanel.setName("RXParticipantsJPanel");
			ivjRXParticipantsJPanel.setBorder(ivjLocalBorder13);
			ivjRXParticipantsJPanel.setLayout(new java.awt.GridBagLayout());
			ivjRXParticipantsJPanel.setBounds(0, 0, 459, 47);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRXParticipantsJPanel;
}

/**
 * Return the SearchJPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getSearchCriteriaJPanel() {
	if (ivjSearchCriteriaJPanel == null) {
		try {
			TitledBorderBean ivjLocalBorder = new TitledBorderBean();
			ivjLocalBorder.setTitleFont(getFont().deriveFont(Font.BOLD));
			ivjLocalBorder.setTitle("Reaction Search");
			ivjSearchCriteriaJPanel = new javax.swing.JPanel();
			ivjSearchCriteriaJPanel.setName("SearchCriteriaJPanel");
			ivjSearchCriteriaJPanel.setBorder(ivjLocalBorder);
			ivjSearchCriteriaJPanel.setLayout(new java.awt.GridBagLayout());

			java.awt.GridBagConstraints constraintsJPanel1 = new java.awt.GridBagConstraints();
			constraintsJPanel1.gridx = 0; constraintsJPanel1.gridy = 0;
			constraintsJPanel1.gridwidth = 2;
			constraintsJPanel1.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJPanel1.weightx = 1.0;
			constraintsJPanel1.anchor = GridBagConstraints.WEST;
			constraintsJPanel1.insets = new java.awt.Insets(4, 4, 4, 4);
			getSearchCriteriaJPanel().add(getJPanel1(), constraintsJPanel1);

			java.awt.GridBagConstraints constraintsJPanel = new java.awt.GridBagConstraints();
			constraintsJPanel.gridx = 0; constraintsJPanel.gridy = 1;
			constraintsJPanel.gridwidth = 2;
			constraintsJPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJPanel.weightx = 1.0;
			constraintsJPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getSearchCriteriaJPanel().add(getJPanel(), constraintsJPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSearchCriteriaJPanel;
}

/**
 * Return the SearchDictionaryJRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getSearchDictionaryJRadioButton() {
	if (ivjSearchDictionaryJRadioButton == null) {
		try {
			ivjSearchDictionaryJRadioButton = new javax.swing.JRadioButton();
			ivjSearchDictionaryJRadioButton.setName("SearchDictionaryJRadioButton");
			ivjSearchDictionaryJRadioButton.setText("KEGG Enzymatic Reactions");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSearchDictionaryJRadioButton;
}

/**
 * Return the SearchResultsJPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getSearchResultsJPanel() {
	if (ivjSearchResultsJPanel == null) {
		try {
			LineBorderBean ivjLocalBorder6 = new LineBorderBean();
			TitledBorderBean ivjLocalBorder5 = new TitledBorderBean();
			ivjLocalBorder5.setTitleFont(getFont().deriveFont(Font.BOLD));
			ivjLocalBorder5.setBorder(ivjLocalBorder6);
			ivjLocalBorder5.setTitleColor(java.awt.Color.black);
			ivjLocalBorder5.setTitle("1.  Select Reaction");
			ivjSearchResultsJPanel = new javax.swing.JPanel();
			ivjSearchResultsJPanel.setName("SearchResultsJPanel");
			ivjSearchResultsJPanel.setBorder(ivjLocalBorder5);
			ivjSearchResultsJPanel.setLayout(new java.awt.GridBagLayout());

			java.awt.GridBagConstraints constraintsJScrollPane3 = new java.awt.GridBagConstraints();
			constraintsJScrollPane3.gridx = 0; constraintsJScrollPane3.gridy = 0;
			constraintsJScrollPane3.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJScrollPane3.weightx = 1.0;
			constraintsJScrollPane3.weighty = 1.0;
			constraintsJScrollPane3.ipadx = 4;
			constraintsJScrollPane3.ipady = 4;
			constraintsJScrollPane3.insets = new java.awt.Insets(4, 4, 4, 4);
			getSearchResultsJPanel().add(getJScrollPane3(), constraintsJScrollPane3);

			java.awt.GridBagConstraints constraintsParameterJPanel = new java.awt.GridBagConstraints();
			constraintsParameterJPanel.gridx = 0; constraintsParameterJPanel.gridy = 1;
			constraintsParameterJPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsParameterJPanel.weightx = 1.0;
			constraintsParameterJPanel.weighty = 1.0;
			constraintsParameterJPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getSearchResultsJPanel().add(getParameterJPanel(), constraintsParameterJPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSearchResultsJPanel;
}

/**
 * Return the NameTypeButtonGroup property value.
 * @return javax.swing.ButtonGroup
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.ButtonGroup getSearchTypeButtonGroup() {
	if (ivjSearchTypeButtonGroup == null) {
		try {
			ivjSearchTypeButtonGroup = new javax.swing.ButtonGroup();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSearchTypeButtonGroup;
}


/**
 * Return the SearchUserJRadioButton property value.
 * @return javax.swing.JRadioButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JRadioButton getSearchUserJRadioButton() {
	if (ivjSearchUserJRadioButton == null) {
		try {
			ivjSearchUserJRadioButton = new javax.swing.JRadioButton();
			ivjSearchUserJRadioButton.setName("SearchUserJRadioButton");
			ivjSearchUserJRadioButton.setSelected(true);
			ivjSearchUserJRadioButton.setText("VCell User Reactions");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSearchUserJRadioButton;
}

/**
 * Gets the structure property (cbit.vcell.model.Structure) value.
 * @return The structure property value.
 * @see #setStructure
 */
public Structure getStructure() {
	return fieldStructure;
}


/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}


/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getBackJButton().addActionListener(ivjEventHandler);
	getFinishJButton().addActionListener(ivjEventHandler);
	getNextJButton().addActionListener(ivjEventHandler);
	getReactionsJList().addPropertyChangeListener(ivjEventHandler);
	getParameterNamesJList().addPropertyChangeListener(ivjEventHandler);
	getJButton1().addActionListener(ivjEventHandler);
	this.addPropertyChangeListener(ivjEventHandler);
	getParameterValuesJList().addPropertyChangeListener(ivjEventHandler);
	getFindRXTextRadioButton().addActionListener(ivjEventHandler);
	getKeggMoleculeJRadioButton().addActionListener(ivjEventHandler);
	getKeggSpecifyJButton().addActionListener(ivjEventHandler);
	getFindTextJTextField().addPropertyChangeListener(ivjEventHandler);
	getFindTextJTextField().addActionListener(ivjEventHandler);
	connPtoP3SetTarget();
	connPtoP4SetTarget();
	connPtoP5SetTarget();
	connPtoP6SetTarget();
}

/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("DBReactionWizardPanel");
		setLayout(new java.awt.GridBagLayout());

		java.awt.GridBagConstraints constraintsBFNJPanel = new java.awt.GridBagConstraints();
		constraintsBFNJPanel.gridx = 0; constraintsBFNJPanel.gridy = 1;
		constraintsBFNJPanel.gridwidth = 2;
		constraintsBFNJPanel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getBFNJPanel(), constraintsBFNJPanel);

		java.awt.GridBagConstraints constraintsCardLayoutJPanel = new java.awt.GridBagConstraints();
		constraintsCardLayoutJPanel.gridx = 0; constraintsCardLayoutJPanel.gridy = 0;
		constraintsCardLayoutJPanel.gridwidth = 2;
		constraintsCardLayoutJPanel.fill = java.awt.GridBagConstraints.BOTH;
		constraintsCardLayoutJPanel.weightx = 1.0;
		constraintsCardLayoutJPanel.weighty = 1.0;
		constraintsCardLayoutJPanel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getCardLayoutJPanel(), constraintsCardLayoutJPanel);

		java.awt.GridBagConstraints constraintsJButton1 = new java.awt.GridBagConstraints();
		constraintsJButton1.gridx = 1; constraintsJButton1.gridy = 1;
		constraintsJButton1.anchor = java.awt.GridBagConstraints.EAST;
		constraintsJButton1.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getJButton1(), constraintsJButton1);
		initConnections();
		connEtoC4();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}

/**
 * Insert the method's description here.
 * Creation date: (8/15/2003 6:07:56 PM)
 * @return boolean
 * @param sc cbit.vcell.model.SpeciesContext
 */
private boolean isSCResolvable(SpeciesContext speciesContext) {
	return
		(getStructure().equals(speciesContext.getStructure())) ||
	(
		getStructure() instanceof Membrane &&
		(
			((Membrane)getStructure()).getOutsideFeature().equals(speciesContext.getStructure()) ||
			((Membrane)getStructure()).getInsideFeature().equals(speciesContext.getStructure())
		)
	);

}


/**
 * Insert the method's description here.
 * Creation date: (4/5/2005 11:01:36 AM)
 */
private boolean lastSearchIsSameAsCurrent() {
	
	if(lastSearchChangeInfo == null){
		return false;
	}

	if(!((javax.swing.JRadioButton)lastSearchChangeInfo[0]).isSelected()){//Where search button
		return false;
	}

	if(!((javax.swing.JRadioButton)lastSearchChangeInfo[1]).isSelected()){//What to search for radio button
		return false;
	}

	String s = getFindTextJTextField().getText();
	if(s != null && s.length() == 0){
		s = null;
	}
	if(lastSearchChangeInfo[2] instanceof String){
		if(!Compare.isEqualOrNull(s,((String)lastSearchChangeInfo[2]))){
			return false;
		}
	}
	if(lastSearchChangeInfo[2] instanceof DBFormalSpecies){
		if(((DBFormalSpecies)lastSearchChangeInfo[2]) != getCurrentDBFormalSpecies()){
			return false;
		}
	}
	return true;
}


/**
 * Insert the method's description here.
 * Creation date: (4/5/2005 11:01:36 AM)
 */
private void lastSearchSaveInfo() {
	
	lastSearchChangeInfo = new Object[3];

	lastSearchChangeInfo[0] =
		(getSearchUserJRadioButton().isSelected()?getSearchUserJRadioButton():getSearchDictionaryJRadioButton());
		
	lastSearchChangeInfo[1] =
		(getFindRXTextRadioButton().isSelected()?getFindRXTextRadioButton():getKeggMoleculeJRadioButton());

	if(getFindRXTextRadioButton().isSelected()){
		String s = getFindTextJTextField().getText();
		if(s != null && s.length() == 0){
			s = null;
		}
		lastSearchChangeInfo[2] = s;
	}else{
		lastSearchChangeInfo[2] = getCurrentDBFormalSpecies();
	}
}


/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		DBReactionWizardPanel aDBReactionWizardPanel;
		aDBReactionWizardPanel = new DBReactionWizardPanel();
		frame.setContentPane(aDBReactionWizardPanel);
		frame.setSize(aDBReactionWizardPanel.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JPanel");
		exception.printStackTrace(System.out);
	}
}


/**
 * Comment
 */
private void parameterNameSelectionChanged() {

	try{
		javax.swing.DefaultListModel dlm = (javax.swing.DefaultListModel)getParameterValuesJList().getModel();
		dlm.removeAllElements();
		//
		if(getParameterNamesJList().getSelectedValue() != null){

//			final cbit.vcell.clientdb.DocumentManager docManager = getDocumentManager();
//			final javax.swing.JList jlist = getReactionsJList();
			final MapStringToObject parameNameMSO = (MapStringToObject)getParameterNamesJList().getSelectedValue();
			final KeyValue reactionStepKey = ((ReactionStepInfo)parameNameMSO.getToObject()).getReactionKey();
			//
			final String RXSTEP_HASH_VALUE_KEY = "rxStep";
			
			AsynchClientTask searchReactions = new AsynchClientTask("Getting Full Reaction", AsynchClientTask.TASKTYPE_NONSWING_BLOCKING) {
				public void run(Hashtable<String, Object> hash) throws DataAccessException {
					ReactionStep rStep = getDocumentManager().getReactionStep(reactionStepKey);
					if(rStep != null){
						hash.put(RXSTEP_HASH_VALUE_KEY,rStep);
					}
				}
			};
			//
			AsynchClientTask updateRXParams = new AsynchClientTask("updateRXParams", AsynchClientTask.TASKTYPE_SWING_BLOCKING) {
				public void run(Hashtable<String, Object> hash) throws DataAccessException {
					ReactionStep rStep = (ReactionStep)hash.get(RXSTEP_HASH_VALUE_KEY);
					if(rStep != null){
						Kinetics.KineticsParameter[] kpArr = rStep.getKinetics().getKineticsParameters();
						ReactionParticipant[] rpArr = rStep.getReactionParticipants();
						//
						javax.swing.DefaultListModel pvdlm = (javax.swing.DefaultListModel)getParameterValuesJList().getModel();
						pvdlm.removeAllElements();
						for(int i=0;i < kpArr.length;i+= 1){
							pvdlm.addElement("Parameter - "+kpArr[i].getName().toString()+" = "+kpArr[i].getExpression().infix());
						}
						pvdlm.addElement("ChargeCarrierValence="+rStep.getChargeCarrierValence().getExpression().infix());
						pvdlm.addElement("PhysicsOption="+rStep.getPhysicsOptions());
						for(int i=0;i < rpArr.length;i+= 1){
							String role = "Unknown";
							if(rpArr[i] instanceof Reactant){
								role = "Reactant";
							}else if(rpArr[i] instanceof Product){
								role = "Product";
							}else if(rpArr[i] instanceof Catalyst){
								role = "Catalyst";
							}else if(rpArr[i] instanceof Flux){
								role = "Flux";
							}
							String fluxFlag = "";
							if(rStep instanceof FluxReaction){
								Membrane rStepStruct = (Membrane)rStep.getStructure();
								if(rpArr[i] instanceof Flux){
									if(rpArr[i].getStructure().equals(rStepStruct.getOutsideFeature())){
										fluxFlag = "Outside";
									}else{
										fluxFlag = "Inside";
									}
								}
							}
							pvdlm.addElement("RXParticipant("+role+") "+fluxFlag+" "+(rpArr[i].getSpecies().getDBSpecies() != null?"*":"-")+" "+rpArr[i].getSpeciesContext().getName());
						}
					}
					setReactionStep(rStep);
					configureBFN();
				}
			};
			//	
			Hashtable<String, Object> hashTemp = new Hashtable<String, Object>();
			ClientTaskDispatcher.dispatch(this,hashTemp,new AsynchClientTask[] {searchReactions,updateRXParams},true);
		}else{
			setReactionStep(null);
		}
	}catch(Exception e){
		PopupGenerator.showErrorDialog(this,e.getMessage());
	}
	//
	configureBFN();
	
}


/**
 * Comment
 */
private void reactionListSelectionChanged() {
	//
	if(!getReactionSelectionModel().getValueIsAdjusting()){
		configureBFN();
	}
}


/**
 * Comment
 */
private void resolve2(){

	try {
		Model tempModel = null;
		Structure tempStructure = null;
		if(getReactionStep() == null){
			tempModel = (Model)BeanUtils.cloneSerializable(getModel());
			tempStructure = tempModel.getStructure(getStructure().getName());

		}else{
			tempModel = new Model("temp");
			ReactionStep reactStep = getReactionStep();
			tempStructure = reactStep.getStructure();
			if((!tempStructure.getClass().equals(getStructure().getClass()))){
				throw new RuntimeException("<html>A reaction from a <font color=red>" + tempStructure.getClass().getSimpleName() + "</font> cannot be added to a <font color=red>" 
						+ getStructure().getClass().getSimpleName() + "</font>. Structure types must match.</html>");
			}
			Structure structure = reactStep.getStructure();
			ArrayList<Structure> structHash = new ArrayList<Structure>();
			if(structure instanceof Membrane){
				structHash.add(((Membrane)structure).getInsideFeature());
			}
			while(structure != null){
				structHash.add(structure);
				structure = structure.getParentStructure();
			}
			Structure[] structArr = structHash.toArray(new Structure[0]);
			tempModel.setStructures(structArr);
			ReactionParticipant[] reactionParticipantArr = reactStep.getReactionParticipants();
			for (int i = 0; i < reactionParticipantArr.length; i++) {
				if(!tempModel.contains(reactionParticipantArr[i].getSpeciesContext().getSpecies())){
					tempModel.addSpecies(reactionParticipantArr[i].getSpeciesContext().getSpecies());
				}
				if(!tempModel.contains(reactionParticipantArr[i].getSpeciesContext())){
					tempModel.addSpeciesContext(reactionParticipantArr[i].getSpeciesContext());
				}
			}
			tempModel.addReactionStep(reactStep);
			reactStep.rebindAllToModel(tempModel);
			tempModel.refreshDependencies();
			tempStructure.setName(getStructure().getName());
			Model tempTempModel = (Model)BeanUtils.cloneSerializable(getModel());
			SpeciesContext[] temptempSCArr = tempTempModel.getSpeciesContexts(tempTempModel.getStructure(getStructure().getName()));
			for (int i = 0; i < temptempSCArr.length; i++) {
				if(tempModel.getSpecies(temptempSCArr[i].getSpecies().getCommonName()) == null){
					tempModel.addSpecies(temptempSCArr[i].getSpecies());
				}
				if(tempModel.getSpeciesContext(temptempSCArr[i].getName()) == null){
					SpeciesContext newSpeciesContext =
						new SpeciesContext(temptempSCArr[i].getSpecies(),tempStructure);
					tempModel.addSpeciesContext(newSpeciesContext);
				}
			}
		}
		
		tempModel.refreshDependencies();

		setReactionDescription(null);
		
		for(int i=0;i<resolvedReaction.elementCount();i+= 1){
			Species species = speciesOrder[speciesAssignmentJCB[i].getSelectedIndex()];
			SpeciesContext speciesContext = null;
			
			if(tempStructure instanceof Feature){
				//structure = getStructure();
			}else if(resolvedReaction.isFluxReaction() && i == 0){
				tempStructure = ((Membrane)tempStructure/*getStructure()*/).getOutsideFeature();
			}else if(resolvedReaction.isFluxReaction() && i == 1){
				tempStructure = ((Membrane)tempStructure/*getStructure()*/).getInsideFeature();
			}else if(structureAssignmentJCB[i].getSelectedIndex() == 0){
				//structure = getStructure();
			}else if(structureAssignmentJCB[i].getSelectedIndex() == 1){
				tempStructure = ((Membrane)tempStructure/*getStructure()*/).getOutsideFeature();
			}else if(structureAssignmentJCB[i].getSelectedIndex() == 2){
				tempStructure = ((Membrane)tempStructure/*getStructure()*/).getInsideFeature();
			}

			if(species != null){
				speciesContext = tempModel.getSpeciesContext(tempModel.getSpecies(species.getCommonName()),tempStructure);
			}else if(resolvedReaction.isFluxReaction() && i == resolvedReaction.getFluxIndexInside()){
				//get the same species(flux carrier) for "Inside" as we generated for "Outside"
				//Note: ReactionDescription always has Outside flux at index 0 and Inside flux at index 1
				species = resolvedReaction.getResolved(resolvedReaction.getFluxIndexOutside()).getSpecies();
			}else{
				DBSpecies dbSpecies = null;
				SpeciesDescription dbsd = resolvedReaction.getReactionElement(i);
				if(dbsd instanceof DBFormalSpecies){//get DBSpecies (Dictionary Reactions)
					try{
						dbSpecies = getDocumentManager().getBoundSpecies((DBFormalSpecies)dbsd);
					}catch(DataAccessException e){
						//Do Nothing, this SC won't be bound in database, user can do it later
					}
				}else{//get DBSpecies (user Reactions)
					String origSCName = resolvedReaction.getOrigSpeciesContextName(resolvedReaction.getDBSDIndex(dbsd));
					ReactionParticipant[] rPart = getReactionStep().getReactionParticipants();
					for(int j=0;j<rPart.length;j+= 1){
						if(rPart[j].getSpeciesContext().getName().equals(origSCName)){
							dbSpecies = rPart[j].getSpecies().getDBSpecies();
						}
					}
				}
				species =
					new Species(TokenMangler.fixTokenStrict(dbsd.getPreferredName()),
						null,
						dbSpecies);
			}

			if(speciesContext == null){
				speciesContext = new SpeciesContext(species,tempStructure);
			}

			try{
				resolvedReaction.resolve(i,speciesContext);
			}catch(IllegalArgumentException e){
				PopupGenerator.showErrorDialog(this, "Error Resolving RX Elements --\n"+e.getMessage());
				return;
			}
			
			//System.out.println("\n"+
				//resolvedReaction.getReactionElement(i).getPreferredName()+
				//" resolved to "+speciesContext.toString()+"\n");
		}

		setReactionDescription(resolvedReaction);

		done(tempModel,tempStructure);
	} catch (Exception e) {
		e.printStackTrace();
		PopupGenerator.showErrorDialog(this,e.getMessage());
	}
}


/**
 * Comment
 */
private void search(){

	if(getDocumentManager() != null){
		String textSearchS = getFindTextJTextField().getText();
		if(textSearchS != null && textSearchS.length() == 0){
			textSearchS = null;
		}
		final ReactionQuerySpec reactionQuerySpec =
			new ReactionQuerySpec(
				(getFindRXTextRadioButton().isSelected()?formatLikeString(textSearchS):null),
				(getKeggMoleculeJRadioButton().isSelected()?getCurrentDBFormalSpecies():null));
			
		if(getSearchUserJRadioButton().isSelected()){
			searchUserReactions(reactionQuerySpec);
			return;
		}
		final DocumentManager docManager = getDocumentManager();
		final javax.swing.JList jlist = getReactionsJList();
		//
		final String RXDESC_VALUE_KEY = "rxDesc";
		AsynchClientTask searchReactions = new AsynchClientTask("searching reactions", AsynchClientTask.TASKTYPE_NONSWING_BLOCKING) {
			public void run(Hashtable<String, Object> hash) throws DataAccessException {
				ReactionDescription[] dbfr = docManager.getDictionaryReactions(reactionQuerySpec);
				if(dbfr != null && dbfr.length >0){
					hash.put(RXDESC_VALUE_KEY,dbfr);
				}
			}
		};
		//
		AsynchClientTask updateRXList = new AsynchClientTask("updateRXList", AsynchClientTask.TASKTYPE_SWING_BLOCKING) {
			public void run(Hashtable<String, Object> hash){
				ReactionDescription[] dbfr = (ReactionDescription[])hash.get(RXDESC_VALUE_KEY);
				if(dbfr != null){
					jlist.setListData(dbfr);
				}else{
					jlist.setListData(new ReactionDescription[0]);
				}
				afterSearchConfigure();
			}
		};
		//	
		Hashtable<String, Object> hashTemp = new Hashtable<String, Object>();
		ClientTaskDispatcher.dispatch(this,hashTemp,new AsynchClientTask[] {searchReactions,updateRXList},false);
	}
}


/**
 * Comment
 */
public void searchDictionaryJRadioButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	return;
}


/**
 * Insert the method's description here.
 * Creation date: (7/12/2003 2:45:44 PM)
 */
private void searchUserReactions(final ReactionQuerySpec reactionQuerySpec){

	if(getDocumentManager() != null){
		final DocumentManager docManager = getDocumentManager();
		final JList jlist = getReactionsJList();
		//
		final String RXSTRING_VALUE_KEY = "rxString";
		//
		AsynchClientTask searchReactions = new AsynchClientTask("searching reactions", AsynchClientTask.TASKTYPE_NONSWING_BLOCKING) {
			public void run(Hashtable<String, Object> hash){
				try{
					mapRXStringtoRXIDs.clear();
					//
					ReactionDescription[] dbrd = docManager.getUserReactionDescriptions(reactionQuerySpec);
					//
					if(dbrd != null && !(getStructure() instanceof Membrane)){
						Vector<ReactionDescription> noflux = new Vector<ReactionDescription>();
						for(int i=0;i<dbrd.length;i+= 1){
							if(!dbrd[i].isFluxReaction()){
								noflux.add(dbrd[i]);
							}
						}
						if(noflux.size() > 0){
							dbrd = new ReactionDescription[noflux.size()];
							noflux.copyInto(dbrd);
						}
					}
					//
					String[] dbrdS = null;
					if(dbrd != null){
						//if(mapRXStringtoRXIDs == null){mapRXStringtoRXIDs = new java.util.Hashtable();}
						for(int i=0;i<dbrd.length;i+= 1){
							String rxString = dbrd[i].toString();
							if(!mapRXStringtoRXIDs.containsKey(rxString)){
								mapRXStringtoRXIDs.put(rxString,new Vector<String>());
							}
							(mapRXStringtoRXIDs.get(rxString)).add(dbrd[i].getVCellRXID().toString());
						}
						dbrdS = (String[])mapRXStringtoRXIDs.keySet().toArray(new String[0]);
					}
					//
					if(dbrd != null && dbrd.length >0){
						hash.put(RXSTRING_VALUE_KEY,dbrdS);
					}
				}catch(DataAccessException e){
					PopupGenerator.showErrorDialog(DBReactionWizardPanel.this,e.getMessage());
				}
			}
		};
		//
		AsynchClientTask updateRXList = new AsynchClientTask("updateRXList", AsynchClientTask.TASKTYPE_SWING_BLOCKING) {
			public void run(Hashtable<String, Object> hash){
				String[] dbrdS = (String[])hash.get(RXSTRING_VALUE_KEY);
				if(dbrdS != null){
					jlist.setListData(dbrdS);
				}else{
					jlist.setListData(new String[0]);
				}
				afterSearchConfigure();
			}
		};
		//	
		Hashtable<String, Object> hashTemp = new Hashtable<String, Object>();
		ClientTaskDispatcher.dispatch(this,hashTemp,new AsynchClientTask[] {searchReactions,updateRXList},false);
	}	
}


/**
 * Set the CurrentDBFormalSpecies to a new value.
 * @param newValue cbit.vcell.dictionary.DBFormalSpecies
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void setCurrentDBFormalSpecies(DBFormalSpecies newValue) {
	if (ivjCurrentDBFormalSpecies != newValue) {
		try {
			ivjCurrentDBFormalSpecies = newValue;
			connEtoC30(ivjCurrentDBFormalSpecies);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	// user code begin {3}
	// user code end
}

/**
 * Set the document2 to a new value.
 * @param newValue javax.swing.text.Document
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void setdocument2(javax.swing.text.Document newValue) {
	if (ivjdocument2 != newValue) {
		try {
			/* Stop listening for events from the current object */
			if (ivjdocument2 != null) {
				ivjdocument2.removeDocumentListener(ivjEventHandler);
			}
			ivjdocument2 = newValue;

			/* Listen for events from the new object */
			if (ivjdocument2 != null) {
				ivjdocument2.addDocumentListener(ivjEventHandler);
			}
			connPtoP6SetSource();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	// user code begin {3}
	// user code end
}


/**
 * Sets the documentManager property (cbit.vcell.clientdb.DocumentManager) value.
 * @param documentManager The new value for the property.
 * @see #getDocumentManager
 */
public void setDocumentManager(DocumentManager documentManager) {
	DocumentManager oldValue = fieldDocumentManager;
	fieldDocumentManager = documentManager;
	firePropertyChange("documentManager", oldValue, documentManager);
}


/**
 * Sets the model property (cbit.vcell.model.Model) value.
 * @param model The new value for the property.
 * @see #getModel
 */
public void setModel(Model model) {
	Model oldValue = fieldModel;
	fieldModel = model;
	firePropertyChange("model", oldValue, model);
}


/**
 * Set the ParameterNameSelectionModel to a new value.
 * @param newValue javax.swing.ListSelectionModel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void setParameterNameSelectionModel(javax.swing.ListSelectionModel newValue) {
	if (ivjParameterNameSelectionModel != newValue) {
		try {
			/* Stop listening for events from the current object */
			if (ivjParameterNameSelectionModel != null) {
				ivjParameterNameSelectionModel.removeListSelectionListener(ivjEventHandler);
			}
			ivjParameterNameSelectionModel = newValue;

			/* Listen for events from the new object */
			if (ivjParameterNameSelectionModel != null) {
				ivjParameterNameSelectionModel.addListSelectionListener(ivjEventHandler);
			}
			connPtoP4SetSource();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	// user code begin {3}
	// user code end
}


/**
 * Sets the reactionDescription property (cbit.vcell.dictionary.ReactionDescription) value.
 * @param reactionDescription The new value for the property.
 * @see #getReactionDescription
 */
private void setReactionDescription(ReactionDescription reactionDescription) {
	ReactionDescription oldValue = fieldReactionDescription;
	fieldReactionDescription = reactionDescription;
	firePropertyChange("reactionDescription", oldValue, reactionDescription);
}


/**
 * Set the selectionModel2 to a new value.
 * @param newValue javax.swing.ListSelectionModel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void setReactionSelectionModel(javax.swing.ListSelectionModel newValue) {
	if (ivjReactionSelectionModel != newValue) {
		try {
			/* Stop listening for events from the current object */
			if (ivjReactionSelectionModel != null) {
				ivjReactionSelectionModel.removeListSelectionListener(ivjEventHandler);
			}
			ivjReactionSelectionModel = newValue;

			/* Listen for events from the new object */
			if (ivjReactionSelectionModel != null) {
				ivjReactionSelectionModel.addListSelectionListener(ivjEventHandler);
			}
			connPtoP3SetSource();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	// user code begin {3}
	// user code end
}


/**
 * Sets the reactionStep property (cbit.vcell.model.ReactionStep) value.
 * @param reactionStep The new value for the property.
 * @see #getReactionStep
 */
private void setReactionStep(ReactionStep reactionStep) {
	ReactionStep oldValue = fieldReactionStep;
	fieldReactionStep = reactionStep;
	firePropertyChange("reactionStep", oldValue, reactionStep);
}


/**
 * Set the RXDescriptionLSM to a new value.
 * @param newValue javax.swing.ListSelectionModel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void setRXDescriptionLSM(javax.swing.ListSelectionModel newValue) {
	if (ivjRXDescriptionLSM != newValue) {
		try {
			/* Stop listening for events from the current object */
			if (ivjRXDescriptionLSM != null) {
				ivjRXDescriptionLSM.removeListSelectionListener(ivjEventHandler);
			}
			ivjRXDescriptionLSM = newValue;

			/* Listen for events from the new object */
			if (ivjRXDescriptionLSM != null) {
				ivjRXDescriptionLSM.addListSelectionListener(ivjEventHandler);
			}
			connPtoP5SetSource();
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	// user code begin {3}
	// user code end
}


/**
 * Sets the structure property (cbit.vcell.model.Structure) value.
 * @param structure The new value for the property.
 * @see #getStructure
 */
public void setStructure(Structure structure) {
	Structure oldValue = fieldStructure;
	fieldStructure = structure;
	firePropertyChange("structure", oldValue, structure);
}


/**
 * Insert the method's description here.
 * Creation date: (8/5/2003 2:50:56 PM)
 * @param dbfr cbit.vcell.dictionary.ReactionDescription
 */
private void setupRX(ReactionDescription dbfr) {

	resolvedReaction = dbfr;

	if(resolvedReaction != null){

		if(speciesAssignmentJCB != null){
			for(int i=0;i<speciesAssignmentJCB.length;i+= 1){
				speciesAssignmentJCB[i].removeActionListener(this);
			}
		}
		if(structureAssignmentJCB != null){
			for(int i=0;i<structureAssignmentJCB.length;i+= 1){
				structureAssignmentJCB[i].removeActionListener(this);
			}
		}

		getReactionCanvas1().setReactionCanvasDisplaySpec(resolvedReaction.toReactionCanvasDisplaySpec());

		getRXParticipantsJPanel().removeAll();
//		java.awt.Insets zeroInsets = new java.awt.Insets(0,0,0,0);
		java.awt.Insets fourInsets = new java.awt.Insets(4,4,4,4);
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbc.insets = fourInsets;
		gbc.gridx = 0;
		gbc.gridy = 0;

		javax.swing.JLabel rxjlabel = new javax.swing.JLabel("RX Elements");
		//rxjlabel.setForeground(java.awt.Color.white);
		//rxjlabel.setOpaque(true);
		//rxjlabel.setBackground(java.awt.Color.white);
		getRXParticipantsJPanel().add(rxjlabel,gbc);
		
		//gbc.insets = zeroInsets;
		for(int i=0;i<resolvedReaction.elementCount();i+= 1){
			gbc.gridy = i+1;
			javax.swing.JLabel jlabel =
				new javax.swing.JLabel(
					resolvedReaction.getReactionElement(i).getPreferredName()+
					(resolvedReaction.isFluxReaction() && resolvedReaction.getFluxIndexOutside()==i?" (Outside)":"") +
					(resolvedReaction.isFluxReaction() && resolvedReaction.getFluxIndexInside()==i?" (Inside)":""));
			//jlabel.setOpaque(true);
			//jlabel.setBackground(java.awt.Color.white);
			//jlabel.setForeground(java.awt.Color.black);
			getRXParticipantsJPanel().add(jlabel,gbc);
		}

		//gbc.insets = fourInsets;
		gbc.gridx = 1;
		gbc.gridy = 0;

		speciesAssignmentJCB = new javax.swing.JComboBox[resolvedReaction.elementCount()];
		
		javax.swing.JLabel rspjlabel = new javax.swing.JLabel("Assign to Model Species");
		//rspjlabel.setForeground(java.awt.Color.white);
		//rspjlabel.setOpaque(true);
		//rspjlabel.setBackground(java.awt.Color.white);
		getRXParticipantsJPanel().add(rspjlabel,gbc);
		//getRXParticipantsJPanel().add(new javax.swing.JLabel("Resolve to Model Species"),gbc);
		speciesOrder = new Species[getModel().getSpecies().length+1];
		speciesOrder[0] = null;
		for(int j=0;j<getModel().getSpecies().length;j+= 1){
			speciesOrder[j+1] = getModel().getSpecies(j);
		}
		for(int i=0;i<resolvedReaction.elementCount();i+= 1){
			javax.swing.JComboBox jcb = new javax.swing.JComboBox();
			speciesAssignmentJCB[i] = jcb;
			jcb.addItem("New Species");
			for(int j=1;j<speciesOrder.length;j+= 1){
				jcb.addItem("Existing "+speciesOrder[j].getCommonName());
			}
			gbc.gridy = i+1;
			getRXParticipantsJPanel().add(jcb,gbc);
		}
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		structureAssignmentJCB = new javax.swing.JComboBox[resolvedReaction.elementCount()];

		javax.swing.JLabel rstjlabel = new javax.swing.JLabel("Assign to Model Compartment");
		//rstjlabel.setForeground(java.awt.Color.white);
		//rstjlabel.setOpaque(true);
		//rstjlabel.setBackground(java.awt.Color.white);
		getRXParticipantsJPanel().add(rstjlabel,gbc);
		//getRXParticipantsJPanel().add(new javax.swing.JLabel("Resolve to Model Compartment"),gbc);
		for(int i=0;i<resolvedReaction.elementCount();i+= 1){
			javax.swing.JComboBox jcb = new javax.swing.JComboBox();
			structureAssignmentJCB[i] = jcb;
			if(resolvedReaction.isFluxReaction() && resolvedReaction.isFlux(i) && resolvedReaction.getFluxIndexOutside() == i){
				jcb.addItem(((Membrane)getStructure()).getOutsideFeature().getName());
				jcb.setEnabled(false);
			}else if(resolvedReaction.isFluxReaction() && resolvedReaction.isFlux(i) && resolvedReaction.getFluxIndexInside() == i){
				jcb.addItem(((Membrane)getStructure()).getInsideFeature().getName());
				jcb.setEnabled(false);
			}else{
				jcb.addItem(getStructure().getName());
				if(getStructure() instanceof Membrane){
					jcb.addItem(((Membrane)getStructure()).getOutsideFeature().getName());
					jcb.addItem(((Membrane)getStructure()).getInsideFeature().getName());
				}else{
					jcb.setEnabled(false);
				}
			}
			gbc.gridy = i+1;
			getRXParticipantsJPanel().add(jcb,gbc);
		}
		//
		for(int i=0;i<resolvedReaction.elementCount();i+= 1){
			speciesAssignmentJCB[i].addActionListener(this);
			structureAssignmentJCB[i].addActionListener(this);
		}
	}
}


/**
 * Comment
 */
private DBFormalSpecies showSpeciesBrowser() {

	SpeciesQueryDialog sqd = new SpeciesQueryDialog((java.awt.Frame)null,true);
	sqd.setSearchableTypes(
		SpeciesQueryPanel.SEARCHABLE_ENZYME | 
		SpeciesQueryPanel.SEARCHABLE_COMPOUND | 
		(getSearchUserJRadioButton().isSelected()?SpeciesQueryPanel.SEARCHABLE_PROTEIN:0x00L));
	//boolean isDictSearch = getJTabbedPane1().getTitleAt(getJTabbedPane1().getSelectedIndex()).equals("Dictionary");
	//if(getSearchDictionaryJRadioButton().isSelected()){
		//if(getAnyJRadioButton().isSelected()){
			//sqd.setSearchableTypes(SpeciesQueryPanel.SEARCHABLE_ENZYME | SpeciesQueryPanel.SEARCHABLE_COMPOUND);
		//}else{
			//sqd.setSearchableTypes((getCatalystJRadioButton().isSelected()?SpeciesQueryPanel.SEARCHABLE_ENZYME:SpeciesQueryPanel.SEARCHABLE_COMPOUND));
		//}
	//}
	sqd.setDocumentManager(getDocumentManager());
	sqd.setSize(550,500);
	BeanUtils.centerOnScreen(sqd);
	ZEnforcer.showModalDialogOnTop(sqd,this);
	//sqd.setVisible(true);

	DBFormalSpecies dbfs = null;
	DictionaryQueryResults dqr = sqd.getDictionaryQueryResults();
	if(dqr != null && dqr.getSelection() != null){
		dbfs = dqr.getDBFormalSpecies()[dqr.getSelection()[0]];
	}

	if(dbfs != null){
		setCurrentDBFormalSpecies(dbfs);
	}
	//if(dbfs != null){
		//if(getReactantsJRadioButton().isSelected()){
			//getSearchDBReactionElementsPanel().reactionAddReactant(dbfs,1);
		//}else if(getEnzymeJRadioButton().isSelected()){
			//getSearchDBReactionElementsPanel().reactionAddCatalyst(dbfs);
		//}else if(getProductsJRadioButton().isSelected()){
			//getSearchDBReactionElementsPanel().reactionAddProduct(dbfs,1);
		//}
	//}
	
	return dbfs;
}

}