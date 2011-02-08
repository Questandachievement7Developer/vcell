package cbit.vcell.client.desktop.biomodel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cbit.vcell.client.GuiConstants;
import cbit.vcell.mapping.SimulationContext;
import cbit.vcell.mapping.gui.InitialConditionsPanel;
import cbit.vcell.mapping.gui.ReactionSpecsPanel;

@SuppressWarnings("serial")
public class ApplicationSettingsPanel extends ApplicationSubPanel {
	private InternalEventHandler eventHandler = new InternalEventHandler();
	private InitialConditionsPanel initialConditionsPanel;
	private ReactionSpecsPanel reactionSpecsPanel;	
	
	private class InternalEventHandler implements ActionListener {				
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	public ApplicationSettingsPanel() {
		super();
		initialize();
	}

	private void initialize(){			
		initialConditionsPanel = new InitialConditionsPanel();
		reactionSpecsPanel = new ReactionSpecsPanel();
		
		initialConditionsPanel.setBorder(GuiConstants.TAB_PANEL_BORDER);
		reactionSpecsPanel.setBorder(GuiConstants.TAB_PANEL_BORDER);
		tabbedPane.addTab("Species Settings", initialConditionsPanel);
		tabbedPane.addTab("Reaction Settings", reactionSpecsPanel);
	}
	
	@Override
	public void setSimulationContext(SimulationContext newValue) {
		super.setSimulationContext(newValue);
		initialConditionsPanel.setSimulationContext(simulationContext);
		reactionSpecsPanel.setSimulationContext(simulationContext);		
	}

	@Override
	public void setSelectionManager(SelectionManager selectionManager) {
		super.setSelectionManager(selectionManager);
		reactionSpecsPanel.setSelectionManager(selectionManager);
		initialConditionsPanel.setSelectionManager(selectionManager);
	}
}
