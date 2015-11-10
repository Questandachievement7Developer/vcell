package cbit.vcell.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.List;

import net.sourceforge.interval.ia_math.RealInterval;

import org.vcell.util.Compare;
import org.vcell.util.Issue;
import org.vcell.util.Issue.IssueCategory;
import org.vcell.util.Issue.IssueSource;
import org.vcell.util.IssueContext;
import org.vcell.util.IssueContext.ContextType;
import org.vcell.util.Matchable;

import cbit.vcell.mapping.ParameterContext;
import cbit.vcell.mapping.ParameterContext.GlobalParameterContext;
import cbit.vcell.mapping.ParameterContext.LocalParameter;
import cbit.vcell.mapping.ParameterContext.LocalProxyParameter;
import cbit.vcell.mapping.ParameterContext.ParameterPolicy;
import cbit.vcell.mapping.ParameterContext.ParameterRoleEnum;
import cbit.vcell.mapping.ParameterContext.UnresolvedParameter;
import cbit.vcell.parser.Expression;
import cbit.vcell.parser.ExpressionBindingException;
import cbit.vcell.parser.ExpressionException;
import cbit.vcell.parser.ScopedSymbolTable;
import cbit.vcell.units.UnitSystemProvider;
import cbit.vcell.units.VCUnitDefinition;
import cbit.vcell.units.VCUnitSystem;

public class RbmKineticLaw implements Serializable, ModelProcessDynamics, Matchable, PropertyChangeListener, IssueSource {
	
	public static enum RbmKineticLawParameterType implements ParameterRoleEnum {
		RuleRate("ruleRate","rule rate"),
		MassActionForwardRate("Kf","forward rate"),
		MassActionReverseRate("Kr","reverse rate"),
		MichaelisMentenKcat("Kcat","enzymatic rate??"),
		MichaelisMentenKm("Km","saturating concentration"),
		SaturableVmax("Vmax","max rate"),
		SaturableKs("Ks","saturating concentration"),
		UserDefined(null,"user defined");
		
		private final String defaultName;
		private final String description;
		
		private RbmKineticLawParameterType(String defaultName,String description){
			this.defaultName = defaultName;
			this.description = description;
		}
		
		public String getDefaultName() {
			return defaultName;
		}
	
		public String getDescription() {
			return description;
		}
		
	}

	public static enum RateLawType {
		MassAction,
		MichaelisMenten,
		Saturable
	}
	
	private class ParameterContextSettings implements Serializable, ParameterPolicy, UnitSystemProvider, GlobalParameterContext {

		public boolean isUserDefined(LocalParameter localParameter) {
			return (localParameter.getRole() == RbmKineticLaw.RbmKineticLawParameterType.UserDefined);
		}

		public boolean isExpressionEditable(LocalParameter localParameter) {
			return (localParameter.getExpression()!=null);
		}

		public boolean isNameEditable(LocalParameter localParameter) {
			return true;
		}

		public boolean isUnitEditable(LocalParameter localParameter) {
			return isUserDefined(localParameter);
		}	
		
		public VCUnitSystem getUnitSystem() {
			return reactionRule.getModel().getUnitSystem();
		}
		
		@Override
		public ScopedSymbolTable getSymbolTable() {
			return reactionRule.getModel();
		}
		
		@Override
		public Parameter getParameter(String name) {
			return reactionRule.getModel().getModelParameter(name);
		}
		
		@Override
		public Parameter addParameter(String name, Expression exp, VCUnitDefinition unit) throws PropertyVetoException {
			Model model = reactionRule.getModel();
			return model.addModelParameter(model.new ModelParameter(name, exp, Model.ROLE_UserDefined, unit));
		}

		@Override
		public ParameterRoleEnum getUserDefinedRole() {
			return RbmKineticLawParameterType.UserDefined;
		}

		@Override
		public IssueSource getIssueSource() {
			return RbmKineticLaw.this;
		}

		@Override
		public RealInterval getConstraintBounds(ParameterRoleEnum role) {
			// TODO Auto-generated method stub
			return null;
		}

	};
	private boolean bRefreshingUnits = false;
	private ParameterContextSettings parameterContextSettings = new ParameterContextSettings();

	private ReactionRule reactionRule;
	private final RbmKineticLaw.RateLawType rateLawType;
	private transient PropertyChangeSupport propertyChangeSupport;
	
	protected ParameterContext parameterContext = null;

	
	public RbmKineticLaw(final ReactionRule reactionRule, RbmKineticLaw.RateLawType rateLawType) {
		this.reactionRule = reactionRule;
		this.rateLawType = rateLawType;
		this.parameterContext = new ParameterContext(reactionRule.getNameScope(),parameterContextSettings, parameterContextSettings);
		// propagate property change events from parameter context to Kinetic Law listeners.
		parameterContext.addPropertyChangeListener(this);
		ModelUnitSystem modelUnitSystem = reactionRule.getModel().getUnitSystem();
		VCUnitDefinition unit_TBD = modelUnitSystem.getInstance_TBD();

		switch (rateLawType){
		case MassAction: {
			try {
				parameterContext.setLocalParameters(new LocalParameter[] {
					parameterContext.new LocalParameter(RbmKineticLaw.RbmKineticLawParameterType.RuleRate.getDefaultName(), null, RbmKineticLaw.RbmKineticLawParameterType.RuleRate, unit_TBD, RbmKineticLaw.RbmKineticLawParameterType.RuleRate.getDescription()),
					parameterContext.new LocalParameter(RbmKineticLaw.RbmKineticLawParameterType.MassActionForwardRate.getDefaultName(), new Expression(0.0), RbmKineticLaw.RbmKineticLawParameterType.MassActionForwardRate, unit_TBD, RbmKineticLaw.RbmKineticLawParameterType.MassActionForwardRate.getDescription()),
					parameterContext.new LocalParameter(RbmKineticLaw.RbmKineticLawParameterType.MassActionReverseRate.getDefaultName(), new Expression(0.0), RbmKineticLaw.RbmKineticLawParameterType.MassActionReverseRate, unit_TBD, RbmKineticLaw.RbmKineticLawParameterType.MassActionReverseRate.getDescription()),
				});
			} catch (PropertyVetoException | ExpressionBindingException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage(),e);
			}
			break;
		}
		case MichaelisMenten: {
			try {
				parameterContext.setLocalParameters(new LocalParameter[] {
						parameterContext.new LocalParameter(RbmKineticLaw.RbmKineticLawParameterType.RuleRate.getDefaultName(), null, RbmKineticLaw.RbmKineticLawParameterType.RuleRate, unit_TBD, RbmKineticLaw.RbmKineticLawParameterType.RuleRate.getDescription()),
						parameterContext.new LocalParameter(RbmKineticLaw.RbmKineticLawParameterType.MichaelisMentenKcat.getDefaultName(), new Expression(0.0), RbmKineticLaw.RbmKineticLawParameterType.MichaelisMentenKcat, unit_TBD, RbmKineticLaw.RbmKineticLawParameterType.MichaelisMentenKcat.getDescription()), 
						parameterContext.new LocalParameter(RbmKineticLaw.RbmKineticLawParameterType.MichaelisMentenKm.getDefaultName(),   new Expression(0.0), RbmKineticLaw.RbmKineticLawParameterType.MichaelisMentenKm,   unit_TBD, RbmKineticLaw.RbmKineticLawParameterType.MichaelisMentenKm.getDescription()),
				});
			} catch (PropertyVetoException | ExpressionBindingException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage(),e);
			}
			break;
		}
		case Saturable: {
			try {
				parameterContext.setLocalParameters(new LocalParameter[] {
						parameterContext.new LocalParameter(RbmKineticLaw.RbmKineticLawParameterType.RuleRate.getDefaultName(), null, RbmKineticLaw.RbmKineticLawParameterType.RuleRate, unit_TBD, RbmKineticLaw.RbmKineticLawParameterType.RuleRate.getDescription()),
						parameterContext.new LocalParameter(RbmKineticLaw.RbmKineticLawParameterType.SaturableKs.getDefaultName(),   new Expression(0.0), RbmKineticLaw.RbmKineticLawParameterType.SaturableKs,   unit_TBD, RbmKineticLaw.RbmKineticLawParameterType.SaturableKs.getDescription()),
						parameterContext.new LocalParameter(RbmKineticLaw.RbmKineticLawParameterType.SaturableVmax.getDefaultName(), new Expression(0.0), RbmKineticLaw.RbmKineticLawParameterType.SaturableVmax, unit_TBD, RbmKineticLaw.RbmKineticLawParameterType.SaturableVmax.getDescription()), 								
				});
			} catch (PropertyVetoException | ExpressionBindingException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage(),e);
			}
			break;
		}
		default:{
			throw new RuntimeException("unsupported rule-based kinetic law "+rateLawType);
		}
		}
		try {
			bind(reactionRule);
		} catch (ExpressionBindingException e) {
			e.printStackTrace();
			System.out.println("failed to bind kinetics expressions ");
		}
		refreshUnits();
		resolveUndefinedUnits();
	}
	public static void duplicate(RbmKineticLaw to, ReactionRule fromRule) {
		RbmKineticLaw from = fromRule.getKineticLaw();
		try {
		for (LocalParameter fromLocalParameter : from.parameterContext.getLocalParameters()){
			if (fromLocalParameter.getRole() != RbmKineticLawParameterType.UserDefined){
				// find built-in parameter with same role and copy expression.
				// copy name also in case it was changed.
				LocalParameter toLocalParameter = to.parameterContext.getLocalParameterFromRole(fromLocalParameter.getRole());
				toLocalParameter.setName(fromLocalParameter.getName());
				Expression expression = fromLocalParameter.getExpression();
				if(expression != null) {
					to.setParameterValue(toLocalParameter, new Expression(expression), true);
				}
			}
		}
		for (LocalParameter fromLocalParameter : from.parameterContext.getLocalParameters()){
			if (fromLocalParameter.getRole() == RbmKineticLawParameterType.UserDefined){
				LocalParameter toLocalParameter = to.getLocalParameter(fromLocalParameter.getName());
				if (toLocalParameter == null){
					//
					// after lazy parameter creation we didn't find a user-defined rule parameter with this same name.
					// 
					// there must be a global symbol with the same name, that the local reaction parameter has overridden.
					//
					ParameterContext.LocalProxyParameter rule_proxy_parameter = null;
					for (ProxyParameter proxyParameter : from.getProxyParameters()){
						if (proxyParameter.getName().equals(fromLocalParameter.getName())){
							rule_proxy_parameter = (LocalProxyParameter) proxyParameter;
						}
					}
					if (rule_proxy_parameter != null){
						boolean bConvertToGlobal = false;  // we want to convert to local
						to.convertParameterType(rule_proxy_parameter, bConvertToGlobal);
					}else{
						// could find neither local parameter nor proxy parameter
						throw new RuntimeException("user defined parameter "+fromLocalParameter.getName()+" from reactionRule "+fromRule.getName()+" didn't map to a reactionRule parameter");
					}
				}else if (toLocalParameter.getRole() == RbmKineticLawParameterType.UserDefined){
					Expression expression = fromLocalParameter.getExpression();
					if(expression != null) {
						to.setParameterValue(toLocalParameter, expression, true);
					}
					toLocalParameter.setUnitDefinition(fromLocalParameter.getUnitDefinition());
				}else{
					throw new RuntimeException("user defined parameter "+fromLocalParameter.getName()+" from reactionRule "+fromRule.getName()+" mapped to a reactionRule parameter with unexpected role "+toLocalParameter.getRole().getDescription());
				}
			}
		}
		} catch (PropertyVetoException | ExpressionException e) {
			e.printStackTrace();
			throw new RuntimeException("Problem attempting to set RbmKineticLaw expression: "+ e.getMessage());
		}
	}
	
	public RbmKineticLaw.RateLawType getRateLawType() {
		return rateLawType;
	}

	public Expression getLocalParameterValue(RbmKineticLaw.RbmKineticLawParameterType parameterType) {
		if(parameterContext.getLocalParameterFromRole(parameterType) == null) {
			return null;
		}
		return parameterContext.getLocalParameterFromRole(parameterType).getExpression();
	}

	public LocalParameter getLocalParameter(RbmKineticLaw.RbmKineticLawParameterType parameterType) {
		return parameterContext.getLocalParameterFromRole(parameterType);
	}

	public void setLocalParameterValue(RbmKineticLaw.RbmKineticLawParameterType parameterType, Expression expression) throws ExpressionBindingException, PropertyVetoException {
		parameterContext.getLocalParameterFromRole(parameterType).setExpression(expression);
	}
	
	public void gatherIssues(IssueContext issueContext, List<Issue> issueList){
		if (rateLawType==RbmKineticLaw.RateLawType.MassAction && parameterContext.getLocalParameterFromRole(RbmKineticLaw.RbmKineticLawParameterType.MassActionForwardRate).getExpression() == null) {
			issueList.add(new Issue(this, issueContext, IssueCategory.KineticsExpressionError, "Forward Rate is null", Issue.SEVERITY_ERROR));
		}
		issueContext = issueContext.newChildContext(ContextType.ModelProcessDynamics,this);

		parameterContext.gatherIssues(issueContext, issueList, RbmKineticLawParameterType.UserDefined);
//		if((reverseRate == null) && (bReversible == true)) {
//			issueList.add(new Issue(this, IssueCategory.KineticsExpressionMissing, "Reverse Rate is null", Issue.SEVERITY_WARNING));
//		}

	}
	
//	public String toString(){
//		return "bad stuff";
//	}

	@Override
	public boolean compareEqual(Matchable obj) {
		if (obj instanceof RbmKineticLaw){
			RbmKineticLaw other = (RbmKineticLaw)obj;
			if (getRateLawType() != other.getRateLawType()){
				return false;
			}
			if (!Compare.isEqual(parameterContext,  other.parameterContext)){
				return false;
			}
			return true;
		}
		return false;
	}

	public LocalParameter[] getLocalParameters() {
		return parameterContext.getLocalParameters();
	}

	public ProxyParameter[] getProxyParameters() {
		return parameterContext.getProxyParameters();
	}

	public UnresolvedParameter[] getUnresolvedParameters() {
		return parameterContext.getUnresolvedParameters();
	}

	public void renameParameter(String name, String newName) throws ExpressionException, PropertyVetoException {
		parameterContext.renameLocalParameter(name, newName);
	}

	public void convertParameterType(Parameter param, boolean bConvertToGlobal) throws PropertyVetoException, ExpressionBindingException {
		if ((param instanceof LocalParameter) && ((LocalParameter)param).getRole() != RbmKineticLaw.RbmKineticLawParameterType.UserDefined) {
			throw new RuntimeException("Cannot convert pre-defined local parameter : \'" + param.getName() + "\' to global parameter.");
		}

		parameterContext.convertParameterType(param, bConvertToGlobal, parameterContextSettings);
	}

	public LocalParameter getLocalParameter(String name) {
		return parameterContext.getLocalParameterFromName(name);
	}

	public void setParameterValue(LocalParameter parm, Expression exp, boolean autocreateLocalParameter) throws PropertyVetoException, ExpressionException {
		parameterContext.setParameterValue(parm, exp, autocreateLocalParameter);
	}

	public void resolveUndefinedUnits() {
		parameterContext.resolveUndefinedUnits();
	}

	public ScopedSymbolTable getScopedSymbolTable() {
		return parameterContext;
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		getPropertyChange().removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		getPropertyChange().addPropertyChangeListener(listener);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == parameterContext){
			getPropertyChange().firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		}
	}
	
	private PropertyChangeSupport getPropertyChange() {
		if (propertyChangeSupport == null) {
			propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
		};
		return propertyChangeSupport;
	}

	public void bind(ReactionRule reactionRule) throws ExpressionBindingException {
		for(LocalParameter p : parameterContext.getLocalParameters()) {
			if (p.getExpression() == null && p.getRole() == RbmKineticLawParameterType.RuleRate){
				continue;
			}
			p.getExpression().bindExpression(reactionRule.getModel().getRbmModelContainer().getSymbolTable());
		}
		
	}
	
	protected void refreshUnits() {
		if (bRefreshingUnits){
			return;
		}
		try {
			bRefreshingUnits=true;

			ModelUnitSystem modelUnitSystem = reactionRule.getModel().getUnitSystem();
			LocalParameter rateParm = getLocalParameter(RbmKineticLawParameterType.RuleRate);
			if (reactionRule.getStructure() instanceof Feature){
				rateParm.setUnitDefinition(modelUnitSystem.getVolumeReactionRateUnit());
			}else if (reactionRule.getStructure() instanceof Membrane){
				rateParm.setUnitDefinition(modelUnitSystem.getMembraneReactionRateUnit());
			}
			
			switch (this.rateLawType){
			case MassAction:{
				LocalParameter forwardRateParm = getLocalParameter(RbmKineticLawParameterType.MassActionForwardRate);
				LocalParameter reverseRateParm = getLocalParameter(RbmKineticLawParameterType.MassActionReverseRate);
				// since units for kinetic parameters are set from model's unit system, if model is null (possible when model is not yet set on reactionStep when reading from XML)
				// don't worry about setting units on kinetic parameters. Call this method when model is set on reactionStep (rebindToModel()). 
				Model model = reactionRule.getModel();
				if (model != null) {
//					if (reactionRule.getStructure() instanceof Membrane){
//						rateParm.setUnitDefinition(modelUnitSystem.getMembraneReactionRateUnit());
//						if (currentDensityParm!=null){
//							currentDensityParm.setUnitDefinition(modelUnitSystem.getCurrentDensityUnit());
//						}
//						KineticsParameter chargeValenceParm = getChargeValenceParameter();
//						if (chargeValenceParm!=null){
//							chargeValenceParm.setUnitDefinition(modelUnitSystem.getInstance_DIMENSIONLESS());
//						}
//					}else if (getReactionStep().getStructure() instanceof Feature){
//						rateParm.setUnitDefinition(modelUnitSystem.getVolumeReactionRateUnit());
//					}else{
//						throw new RuntimeException("unexpected structure type "+getReactionStep().getStructure()+" in MassActionKinetics.refreshUnits()");
//					}
					
					cbit.vcell.units.VCUnitDefinition kfUnits = rateParm.getUnitDefinition();
					cbit.vcell.units.VCUnitDefinition krUnits = rateParm.getUnitDefinition();
					for (ReactantPattern reactantPattern : reactionRule.getReactantPatterns()){
						VCUnitDefinition reactantUnit = modelUnitSystem.getConcentrationUnit(reactantPattern.getStructure());
						kfUnits = kfUnits.divideBy(reactantUnit);
					}
					for (ProductPattern productPattern : reactionRule.getProductPatterns()){
						VCUnitDefinition productUnit = modelUnitSystem.getConcentrationUnit(productPattern.getStructure());
						krUnits = krUnits.divideBy(productUnit);
					}
					if (forwardRateParm!=null && !kfUnits.compareEqual(forwardRateParm.getUnitDefinition())){
						forwardRateParm.setUnitDefinition(kfUnits);
					}
					if (reverseRateParm!=null && !krUnits.compareEqual(reverseRateParm.getUnitDefinition())){
						reverseRateParm.setUnitDefinition(krUnits);
					}
				}
				break;
			} // end case (MassAction)
			} // end switch (rateLawType)
		}finally{
			bRefreshingUnits=false;
		}
	}


	public void refreshDependencies() {
		removePropertyChangeListener(this);
//		removeVetoableChangeListener(this);
		addPropertyChangeListener(this);
//		addVetoableChangeListener(this);
		
		parameterContext.refreshDependencies();
		
//		reactionRule.removePropertyChangeListener(this);
//		reactionRule.addPropertyChangeListener(this);
		
//		for(ReactantPattern p : reactionRule.getReactantPatterns()) {
//			p.removePropertyChangeListener(this);
//			p.addPropertyChangeListener(this);
//		}
//		for(ProductPattern p : reactionRule.getProductPatterns()) {
//			p.removePropertyChangeListener(this);
//			p.addPropertyChangeListener(this);
//		}
	}


}
