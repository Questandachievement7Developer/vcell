package cbit.vcell.client.desktop;

import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.vcell.util.gui.AutoCompleteTableModel;
import org.vcell.util.gui.EditorScrollTable;
import org.vcell.util.gui.GuiUtils;

import cbit.vcell.client.desktop.biomodel.VCellSortTableModel;
import cbit.vcell.parser.AutoCompleteSymbolFilter;
import cbit.vcell.parser.SymbolTable;
import cbit.vcell.server.BioModelLink;
import cbit.vcell.server.MathModelLink;
import cbit.vcell.server.SimpleJobStatus;
import cbit.vcell.server.SimulationJobStatus.SchedulerStatus;

@SuppressWarnings("serial")
public class SimulationJobsTableModel  extends VCellSortTableModel<SimpleJobStatus> 
	implements  PropertyChangeListener, AutoCompleteTableModel {

	public static final int colCount = 9;
	public static final int iColModelApp = 0;
	public static final int iColSimulation = 1;
	public static final int iColSimId = 2;
	public static final int iColSolver = 3;
	public static final int iColStartDate = 4;
	public static final int iColHasData = 5;
	public static final int iColStatus = 6;
	public static final int iColMessage = 7;
	public static final int iColSite = 8;
//	public static final int iColHost = 8;
	
	private static final String[] header = new String[] {"Model / Application", 
			"Simulation", "Sim Id / Job", "Solver", "Start Date", "Data",
			"Status", "Message", "Site"};

	// filtering variables 
	protected static final String PROPERTY_NAME_SEARCH_TEXT = "searchText";
	protected String searchText = null;

	private SimpleJobStatus[] jobStatusArray;
	
	private final ViewJobsPanel owner;

	protected transient java.beans.PropertyChangeSupport propertyChange;

	
	
	public SimulationJobsTableModel(EditorScrollTable table, ViewJobsPanel owner) {
		super(table, header);
		this.owner = owner;
		setMaxRowsPerPage(1000);
	}
	
	@Override
	public Class<?> getColumnClass(int iCol) {
		switch (iCol) {
		case iColModelApp:
		case iColSimulation:
		case iColSimId:
		case iColSolver:
		case iColStartDate:
		case iColHasData:
			return String.class;
		case iColStatus:
			return Object.class;
		case iColMessage:
		case iColSite:
			return String.class;
		default:
			return Object.class;
		}
	}
	
	@Override
	public boolean isCellEditable(int iRow, int iCol) {
		return false;
	}

	@Override
	public Object getValueAt(int iRow, int iCol) {
		SimpleJobStatus sjs = getValueAt(iRow);
		if(sjs == null) {
			return null;
		}
		switch(iCol) {
		case iColModelApp:
			if(sjs.simulationDocumentLink instanceof BioModelLink) {
				BioModelLink bml = (BioModelLink)sjs.simulationDocumentLink;
				return "(BM) " + bml.bioModelName + ",(App) " + bml.simContextName;
			} else if(sjs.simulationDocumentLink instanceof MathModelLink) {
				MathModelLink mml = (MathModelLink)sjs.simulationDocumentLink;
				return "(MM) " + mml.mathModelName;
			} else {
				return null;
			}
		case iColSimulation:
			return sjs.simulationMetadata.simname;
		case iColSimId:
			return sjs.simulationMetadata.vcSimID.getSimulationKey().toString() +
					", [" + sjs.jobStatus.getJobIndex() + "]";
		case iColSolver:
			String str = "";
			if(sjs.simulationMetadata.solverTaskDesc != null) {
				str = sjs.simulationMetadata.solverTaskDesc.getSolverDescription() == null ? "" : sjs.simulationMetadata.solverTaskDesc.getSolverDescription().getDisplayLabel();		
			}
			return str;
		case iColStartDate:
			DateFormat df = new SimpleDateFormat("MM.dd.yyyy");
			Date date = sjs.jobStatus.getStartDate();
			return df.format(date);
		case iColHasData:
			return sjs.jobStatus.hasData() == true ? "Yes" : "No";
		case iColStatus:
			return sjs.jobStatus.getSchedulerStatus().getDescription(); 
		case iColMessage:
			return sjs.jobStatus.getSimulationMessage().getDisplayMessage();
		case iColSite:
			return sjs.jobStatus.getServerID().toString();
//		case iColHost:
//			return sjs.jobStatus.getComputeHost();
		default:
			return null;
		}
	}
	@Override
	public void setValueAt(Object valueNew, int iRow, int iCol) {
		return;
	}
	@Override
	public AutoCompleteSymbolFilter getAutoCompleteSymbolFilter(final int row, final int column) {
		return null;
	}
	@Override
	public Set<String> getAutoCompletionWords(int row, int iCol) {
		return null;
	}
	@Override
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
	}
	
	
	public Comparator<SimpleJobStatus> getComparator(final int col, final boolean ascending) {
		final int scale = ascending ? 1 : -1;
		return new Comparator<SimpleJobStatus>() {
		    public int compare(SimpleJobStatus o1, SimpleJobStatus o2) {
				String s1 = "";
				String s2 = "";
				switch (col) {
				case iColModelApp:
					if(o1.simulationDocumentLink instanceof BioModelLink) {
						BioModelLink bml = (BioModelLink)o1.simulationDocumentLink;
						s1 = "(BM) " + bml.bioModelName + ",(App) " + bml.simContextName;
					} else if(o1.simulationDocumentLink instanceof MathModelLink) {
						MathModelLink mml = (MathModelLink)o1.simulationDocumentLink;
						s1 = "(MM) " + mml.mathModelName;
					}
					if(o2.simulationDocumentLink instanceof BioModelLink) {
						BioModelLink bml = (BioModelLink)o2.simulationDocumentLink;
						s2 = "(BM) " + bml.bioModelName + ",(App) " + bml.simContextName;
					} else if(o2.simulationDocumentLink instanceof MathModelLink) {
						MathModelLink mml = (MathModelLink)o2.simulationDocumentLink;
						s2 = "(MM) " + mml.mathModelName;
					}
					return scale * s1.compareToIgnoreCase(s2);
				case iColSimulation:
					s1 = o1.simulationMetadata.simname;
					s2 = o2.simulationMetadata.simname;
					if(!s1.equalsIgnoreCase(s2)) {
						return scale * s1.compareToIgnoreCase(s2);
					} else {	// if same simulation, sort by date
						Date d1 = o1.jobStatus.getStartDate();
						Date d2 = o2.jobStatus.getStartDate();
						return scale * d1.compareTo(d2);
					}
				case iColSimId:
					s1 = o1.simulationMetadata.vcSimID.getSimulationKey().toString() + "," + o1.jobStatus.getJobIndex();
					s2 = o2.simulationMetadata.vcSimID.getSimulationKey().toString() + "," + o2.jobStatus.getJobIndex();
					return scale * s1.compareToIgnoreCase(s2);
				case iColSolver:
					if(o1.simulationMetadata.solverTaskDesc != null && o1.simulationMetadata.solverTaskDesc.getSolverDescription() != null) {
						s1 = o1.simulationMetadata.solverTaskDesc.getSolverDescription().getDisplayLabel();		
					}
					if(o2.simulationMetadata.solverTaskDesc != null && o2.simulationMetadata.solverTaskDesc.getSolverDescription() != null) {
						s2 = o2.simulationMetadata.solverTaskDesc.getSolverDescription().getDisplayLabel();		
					}
					return scale * s1.compareToIgnoreCase(s2);
				case iColStartDate:
					Date d1 = o1.jobStatus.getStartDate();
					Date d2 = o2.jobStatus.getStartDate();
					return scale * d1.compareTo(d2);
				case iColHasData:
					Boolean b1 = o1.jobStatus.hasData();
					Boolean b2 = o2.jobStatus.hasData();
					return scale * b1.compareTo(b2);
				case iColStatus:
					SchedulerStatus ss1 = o1.jobStatus.getSchedulerStatus();
					SchedulerStatus ss2 = o2.jobStatus.getSchedulerStatus();
					return scale * ss1.compareTo(ss2);
				case iColMessage:
					if(o1.jobStatus != null && o1.jobStatus.getSimulationMessage() != null) {
						s1 = o1.jobStatus.getSimulationMessage().getDisplayMessage();
					}
					if(o2.jobStatus != null && o2.jobStatus.getSimulationMessage() != null) {
						s2 = o2.jobStatus.getSimulationMessage().getDisplayMessage();
					}
					return scale * s1.compareToIgnoreCase(s2);
				case iColSite:
					s1 = o1.jobStatus.getServerID().toString();
					s2 = o2.jobStatus.getServerID().toString();
					return scale * s1.compareToIgnoreCase(s2);
				default:
					return 0;
				}
		    }
		};
	}

	public void setData(SimpleJobStatus[] jobStatusArray) {
		if (this.jobStatusArray == jobStatusArray) {
			return;
		}
		this.jobStatusArray = jobStatusArray;
		refreshData();
	}
	
	public void refreshData() {
		List<SimpleJobStatus> allJobStatusList = new ArrayList<>();
		for(SimpleJobStatus sjj : jobStatusArray) {
			allJobStatusList.add(sjj);
		}
		// ---------------------------------------------------------------------------
		List<SimpleJobStatus> jobStatusList = new ArrayList<>();
		if (searchText == null || searchText.length() == 0) {
			jobStatusList.addAll(allJobStatusList);
		} else {
			jobStatusList.addAll(allJobStatusList);		// TODO: implement search here
		}
		setData(jobStatusList);
		GuiUtils.flexResizeTableColumns(ownerTable);
	}
	
	public void setSearchText(String newValue) {
		if (searchText == newValue) {
			return;
		}
		searchText = newValue;
		refreshData();
	}
	



	
	@Override
	public String checkInputValue(String inputValue, int row, int column) {
		return null;
	}
	@Override
	public SymbolTable getSymbolTable(int row, int column) {
		return null;
	}

}