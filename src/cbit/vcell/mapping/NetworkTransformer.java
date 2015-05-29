package cbit.vcell.mapping;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.vcell.model.rbm.RbmNetworkGenerator;
import org.vcell.util.BeanUtils;
import org.vcell.util.Pair;
import org.vcell.util.TokenMangler;
import org.vcell.util.UserCancelException;

import cbit.vcell.bionetgen.BNGOutputFileParser;
import cbit.vcell.bionetgen.BNGOutputSpec;
import cbit.vcell.bionetgen.BNGParameter;
import cbit.vcell.bionetgen.BNGReaction;
import cbit.vcell.bionetgen.BNGSpecies;
import cbit.vcell.bionetgen.ObservableGroup;
import cbit.vcell.client.desktop.biomodel.SimulationConsolePanel;
import cbit.vcell.mapping.SimulationContext.MathMappingCallback;
import cbit.vcell.mapping.SimulationContext.NetworkGenerationRequirements;
import cbit.vcell.mapping.TaskCallbackMessage.TaskCallbackStatus;
import cbit.vcell.model.Kinetics.KineticsParameter;
import cbit.vcell.model.MassActionKinetics;
import cbit.vcell.model.Model;
import cbit.vcell.model.ModelException;
import cbit.vcell.model.Parameter;
import cbit.vcell.model.Product;
import cbit.vcell.model.RbmObservable;
import cbit.vcell.model.Reactant;
import cbit.vcell.model.ReactionStep;
import cbit.vcell.model.SimpleReaction;
import cbit.vcell.model.Species;
import cbit.vcell.model.SpeciesContext;
import cbit.vcell.parser.Expression;
import cbit.vcell.parser.ExpressionBindingException;
import cbit.vcell.parser.ExpressionException;
import cbit.vcell.parser.NameScope;
import cbit.vcell.parser.SymbolTableEntry;
import cbit.vcell.server.bionetgen.BNGInput;
import cbit.vcell.server.bionetgen.BNGOutput;
import cbit.vcell.server.bionetgen.BNGExecutorService;
import cbit.vcell.solver.Simulation;
import cbit.vcell.units.VCUnitDefinition;

/*
 * Flattening a Rule-based Model
 */
public class NetworkTransformer implements SimContextTransformer {

	private Map<String, Pair<SpeciesContext, Expression>> speciesEquivalenceMap = new LinkedHashMap<String, Pair<SpeciesContext, Expression>>();
		
	@Override
	final public SimContextTransformation transform(SimulationContext originalSimContext, MathMappingCallback mathMappingCallback, NetworkGenerationRequirements networkGenerationRequirements) {
		SimulationContext transformedSimContext;
		try {
			transformedSimContext = (SimulationContext)BeanUtils.cloneSerializable(originalSimContext);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("unexpected exception: "+e.getMessage());
		}
		transformedSimContext.getModel().refreshDependencies();
		transformedSimContext.refreshDependencies1(false);

		ArrayList<ModelEntityMapping> entityMappings = new ArrayList<ModelEntityMapping>();
		transform(originalSimContext,transformedSimContext,entityMappings,mathMappingCallback,networkGenerationRequirements);
		
		ModelEntityMapping[] modelEntityMappings = entityMappings.toArray(new ModelEntityMapping[0]);
		return new SimContextTransformation(originalSimContext, transformedSimContext, modelEntityMappings);
	}
	
	public static class GeneratedSpeciesSymbolTableEntry implements SymbolTableEntry {
		private SymbolTableEntry unmappedSymbol = null;
		
		private GeneratedSpeciesSymbolTableEntry(SymbolTableEntry unmappedSymbol){
			this.unmappedSymbol = unmappedSymbol;
		}
		public boolean isConstant(){
			return false;
		}
		public String getName(){
			return unmappedSymbol.getName();
		}
		public NameScope getNameScope(){
			return null; // unmappedSymbol.getNameScope();
		}
		public VCUnitDefinition getUnitDefinition() {
			return unmappedSymbol.getUnitDefinition();
		}
		public Expression getExpression(){
			return null;
		}
		public double getConstantValue() throws ExpressionException {
			throw new ExpressionException("can't evaluate to constant");
		}
		@Override
		public int getIndex() {
			return 0;
		}
	};
	
	public String convertToBngl(SimulationContext simulationContext, boolean ignoreFunctions, MathMappingCallback mathMappingCallback, NetworkGenerationRequirements networkGenerationRequirements) {
		StringWriter bnglStringWriter = new StringWriter();
		PrintWriter pw = new PrintWriter(bnglStringWriter);
		RbmNetworkGenerator.writeBnglSpecial(simulationContext, pw, ignoreFunctions, speciesEquivalenceMap, networkGenerationRequirements);
		String bngl = bnglStringWriter.toString();
		pw.close();
//		System.out.println(bngl);
//		for (Map.Entry<String, Pair<SpeciesContext, Expression>> entry : speciesEquivalenceMap.entrySet()) {
//	    String key = entry.getKey();
//	    Pair<SpeciesContext, Expression> value = entry.getValue();
//	    SpeciesContext sc = value.one;
//	    Expression initial = value.two;
//		System.out.println("key: " + key + ",   species: " + sc.getName() + ", initial: " + initial.infix());
//	}
		return bngl;
	}
	
	private boolean isBngHashValid(String input, String hash, SimulationContext simContext) {
		if(input == null || input.length() == 0) {
			return false;
		}
		if(simContext == null || simContext.getMd5hash() == null || simContext.getMostRecentlyCreatedOutputSpec() == null) {
			return false;
		}
		if(hash.equals(simContext.getMd5hash())) {
			return true;
		} else {
			return false;
		}
	}

	private BNGOutputSpec generateNetwork(SimulationContext simContext, MathMappingCallback mathMappingCallback, NetworkGenerationRequirements networkGenerationRequirements) 
									throws ClassNotFoundException, IOException {
		TaskCallbackMessage tcm;
		BNGOutputSpec outputSpec;
		speciesEquivalenceMap.clear();
		String input = convertToBngl(simContext, true, mathMappingCallback, networkGenerationRequirements);
		for (Map.Entry<String, Pair<SpeciesContext, Expression>> entry : speciesEquivalenceMap.entrySet()) {
		    String key = entry.getKey();
		    Pair<SpeciesContext, Expression> value = entry.getValue();
		    SpeciesContext sc = value.one;
		    Expression initial = value.two;
			System.out.println("key: " + key + ",   species: " + sc.getName() + ", initial: " + initial.infix());
		}

		String md5hash = MD5.md5(input);
		if(isBngHashValid(input, md5hash, simContext)) {
			String s = "Previously saved outputSpec is up-to-date, no need to generate network.";
			System.out.println(s);
			tcm = new TaskCallbackMessage(TaskCallbackStatus.Error, s);	// not an error, we just want to show it in red
			simContext.appendToConsole(tcm);
			outputSpec = simContext.getMostRecentlyCreatedOutputSpec();
			return (BNGOutputSpec)BeanUtils.cloneSerializable(outputSpec);
		}
		
		BNGInput bngInput = new BNGInput(input);
		BNGOutput bngOutput = null;
		try {
			final BNGExecutorService bngService = new BNGExecutorService(bngInput);
			bngOutput = bngService.executeBNG();
		} catch (RuntimeException ex) {
			ex.printStackTrace(System.out);
			throw ex; //rethrow without losing context
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			throw new RuntimeException(ex.getMessage());
		}
		String bngConsoleString = bngOutput.getConsoleOutput();
		tcm = new TaskCallbackMessage(TaskCallbackStatus.DetailBatch, bngConsoleString);
		simContext.appendToConsole(tcm);

		String bngNetString = bngOutput.getNetFileContent();
		outputSpec = BNGOutputFileParser.createBngOutputSpec(bngNetString);
//		BNGOutputFileParser.printBNGNetOutput(outputSpec);			// prints all output to console

		if (mathMappingCallback.isInterrupted()){
			String msg = "Canceled by user.";
			tcm = new TaskCallbackMessage(TaskCallbackStatus.Error, msg);
			simContext.appendToConsole(tcm);
			throw new UserCancelException(msg);
		}
		if(outputSpec.getBNGSpecies().length > SimulationConsolePanel.speciesLimit) {
			String message = SimulationConsolePanel.getSpeciesLimitExceededMessage(outputSpec);
			tcm = new TaskCallbackMessage(TaskCallbackStatus.Error, message);
			simContext.appendToConsole(tcm);
			throw new RuntimeException(message);
		}
		if(outputSpec.getBNGReactions().length > SimulationConsolePanel.reactionsLimit) {
			String message = SimulationConsolePanel.getReactionsLimitExceededMessage(outputSpec);
			tcm = new TaskCallbackMessage(TaskCallbackStatus.Error, message);
			simContext.appendToConsole(tcm);
			throw new RuntimeException(message);
		}
		
//		System.out.println("new hash: " + md5hash);
//		System.out.println("old hash: " + simContext.getMd5hash());
		if(md5hash != null && md5hash.length() != 0 && outputSpec != null) {
			System.out.println("saving hash and output spec");
			synchronized (this) {
				simContext.setMd5hash(md5hash);
				simContext.setMostRecentlyCreatedOutputSpec(outputSpec);
			}
		} else {
			System.out.println("something is wrong with the hash and/or output spec");
		}
		return (BNGOutputSpec)BeanUtils.cloneSerializable(outputSpec);
	}

	static final float progressFractionQuota = 2.0f/5.0f;
	static final float progressFractionQuotaSpecies = progressFractionQuota / 2.0f / 10.0f;
	
	public void transform(SimulationContext simContext, SimulationContext transformedSimulationContext, ArrayList<ModelEntityMapping> entityMappings, MathMappingCallback mathMappingCallback, NetworkGenerationRequirements networkGenerationRequirements){

		String msg = "Generating network: flattening...";
		mathMappingCallback.setMessage(msg);
		TaskCallbackMessage tcm = new TaskCallbackMessage(TaskCallbackStatus.Clean, "");
		simContext.appendToConsole(tcm);
		tcm = new TaskCallbackMessage(TaskCallbackStatus.TaskStart, msg);
		simContext.appendToConsole(tcm);
		long startTime = System.currentTimeMillis();
		System.out.println("Convert to bngl, execute BNG, retrieve the results.");
		try {
		BNGOutputSpec outputSpec = generateNetwork(simContext, mathMappingCallback, networkGenerationRequirements);
		if (mathMappingCallback.isInterrupted()){
			msg = "Canceled by user.";
			tcm = new TaskCallbackMessage(TaskCallbackStatus.Error, msg);
			simContext.appendToConsole(tcm);
			throw new UserCancelException(msg);
		}
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		System.out.println("     " + elapsedTime + " milliseconds");
		
		Model model = transformedSimulationContext.getModel();
		ReactionContext reactionContext = transformedSimulationContext.getReactionContext();
			// ---- Parameters -----------------------------------------------------------------------------------------------
		startTime = System.currentTimeMillis();
		for (int i = 0; i < outputSpec.getBNGParams().length; i++){
			BNGParameter p = outputSpec.getBNGParams()[i];
//			System.out.println(i+1 + ":\t\t"+ p.toString());
			if(model.getRbmModelContainer().getParameter(p.getName()) != null) {
//				System.out.println("   ...already exists.");
				continue;		// if it's already there we don't try to add it again; this should be true for all of them!
			}
			String s = p.getName();
			if(speciesEquivalenceMap.containsKey(s)) {
				continue;	// we get rid of the fake parameters we use as keys
			}
			Expression exp = new Expression(p.getValue());
			exp.bindExpression(model.getRbmModelContainer().getSymbolTable());
			model.getRbmModelContainer().addParameter(p.getName(), exp, model.getUnitSystem().getInstance_TBD());
		}
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		msg = "Adding " + outputSpec.getBNGParams().length + " parameters to model, " + elapsedTime + " ms";
		tcm = new TaskCallbackMessage(TaskCallbackStatus.Notification, msg);
		simContext.appendToConsole(tcm);
		System.out.println(msg);
		
		// ---- Species ------------------------------------------------------------------------------------------------------------
		mathMappingCallback.setMessage("generating network: adding species...");
		mathMappingCallback.setProgressFraction(progressFractionQuota/4.0f);
		startTime = System.currentTimeMillis();
		System.out.println("\nSpecies :");
		HashMap<Integer, String>  speciesMap = new HashMap<Integer, String>(); // the reactions will need this map to recover the names of species knowing only the networkFileIndex
		LinkedHashMap<String, Species> sMap = new LinkedHashMap<String, Species>();
		LinkedHashMap<String, SpeciesContext> scMap = new LinkedHashMap<String, SpeciesContext>();
		LinkedHashMap<String, BNGSpecies> crossMap = new LinkedHashMap<String, BNGSpecies>();
		List<SpeciesContext> noMapForThese = new ArrayList<SpeciesContext>();
		
//		int countGenerated = 0;
//		final int decimalTickCount = Math.max(outputSpec.getBNGSpecies().length/10, 1);
		for (int i = 0; i < outputSpec.getBNGSpecies().length; i++){
			BNGSpecies s = outputSpec.getBNGSpecies()[i];
//			System.out.println(i+1 + ":\t\t"+ s.toString());
			
			String key = s.getConcentration().infix();
			if(key.startsWith(RbmNetworkGenerator.uniqueIdRoot)) {
			    Pair<SpeciesContext, Expression> value = speciesEquivalenceMap.get(key);
			    SpeciesContext originalsc = value.one;		// the species context of the original model
			    Expression initial = value.two;
				s.setConcentration(initial);		// replace the fake initial condition with the real one
				
				// we'll have to find the species context from the cloned model which correspond to the original species
				SpeciesContext sc = model.getSpeciesContext(originalsc.getName());
				
//				System.out.println(sc.getName() + ", " + sc.getSpecies().getCommonName() + "   ...is one of the original seed species.");
				speciesMap.put(s.getNetworkFileIndex(), sc.getName());		// existing name
				sMap.put(sc.getName(), sc.getSpecies());
				scMap.put(sc.getName(), sc);
				crossMap.put(sc.getName(), s);
				noMapForThese.add(sc);
				continue;
			}
			
			// all these species are new!
			int count = 0;				// generate unique name for the species
			String speciesName = null;
			String nameRoot = "s";
			
			if(s.getName() != null) {	// for seed species we generate a name from the species pattern
				nameRoot = s.getName();
				nameRoot = nameRoot.replaceAll("[!?~]+", "");
				nameRoot = TokenMangler.fixTokenStrict(nameRoot);
				while(true) {
					if(nameRoot.endsWith("_")) {		// clean all the '_' at the end, if any
						nameRoot = nameRoot.substring(0, nameRoot.length()-1);
					} else {
						break;
					}
				}
				if(model.getSpecies(nameRoot) == null && model.getSpeciesContext(nameRoot) == null && !sMap.containsKey(nameRoot) && !scMap.containsKey(nameRoot)) {
					speciesName = nameRoot;		// the name is good and unused
				} else {
					nameRoot += "_";
					while (true) {
						speciesName = nameRoot + count;	
						if (model.getSpecies(speciesName) == null && model.getSpeciesContext(speciesName) == null && !sMap.containsKey(speciesName) && !scMap.containsKey(speciesName)) {
							break;
						}	
						count++;
					}
				}
			} else {			// for plain species it works as before
				while (true) {
					speciesName = nameRoot + count;	
					if (model.getSpecies(speciesName) == null && model.getSpeciesContext(speciesName) == null && !sMap.containsKey(speciesName) && !scMap.containsKey(speciesName)) {
						break;
					}	
					count++;
				}
			}
//			System.out.println(speciesName);
			speciesMap.put(s.getNetworkFileIndex(), speciesName);				// newly created name
			SpeciesContext speciesContext = new SpeciesContext(new Species(speciesName, s.getName()), model.getStructure(0), null);
			speciesContext.setName(speciesName);
//			model.addSpecies(speciesContext.getSpecies());
//			model.addSpeciesContext(speciesContext);
			sMap.put(speciesName, speciesContext.getSpecies());
			scMap.put(speciesName, speciesContext);
			crossMap.put(speciesName, s);
//			SpeciesContextSpec scs = reactionContext.getSpeciesContextSpec(speciesContext);
//			Parameter param = scs.getParameter(SpeciesContextSpec.ROLE_InitialConcentration);
//			param.setExpression(s.getConcentration());
//			SpeciesContext origSpeciesContext = simContext.getModel().getSpeciesContext(s.getName());
//			
//			if (origSpeciesContext!=null){
//				// execution never goes through here because we do a "continue" early in the for look
//				// when we find one of the original seed species
//				ModelEntityMapping em = new ModelEntityMapping(origSpeciesContext,speciesContext);
//				entityMappings.add(em);
//			}else{
//				ModelEntityMapping em = new ModelEntityMapping(new GeneratedSpeciesSymbolTableEntry(speciesContext),speciesContext);
//				entityMappings.add(em);
//				countGenerated++;
//			}
			if (mathMappingCallback.isInterrupted()){
				msg = "Canceled by user.";
				tcm = new TaskCallbackMessage(TaskCallbackStatus.Error, msg);
				simContext.appendToConsole(tcm);
				throw new UserCancelException(msg);
			}
//			if(i%50 == 0) {
//				System.out.println(i+"");
//			}
//			if(i%decimalTickCount == 0) {
//				int multiplier = i/decimalTickCount;
//				float progress = progressFractionQuota/4.0f + progressFractionQuotaSpecies*multiplier;
//				mathMappingCallback.setProgressFraction(progress);
//			}
		}
		
//		System.out.println("Total generated species: " + countGenerated);
//		System.out.println("------------------------ " + sMap.size() + " species in the map.");
//		System.out.println("------------------------ " + scMap.size() + " species contexts in the map.");
//		System.out.println("------------------------ " + model.getSpecies().length + " species in the Model.");
//		System.out.println("------------------------ " + model.getSpeciesContexts().length + " species contexts in the Model.");

		for(SpeciesContext sc1 : model.getSpeciesContexts()) {
			boolean found = false;
			for (Map.Entry<String, SpeciesContext> entry : scMap.entrySet()) {
				SpeciesContext sc2 = entry.getValue();
				if(sc1.getName().equals(sc2.getName())) {
					found = true;
//					System.out.println("found species context " + sc1.getName() + " of species " + sc1.getSpecies().getCommonName() + " // " + sc2.getSpecies().getCommonName());
					break;
				}
			}
			if(found == false) {	// we add to the map the species context and the species which exist in the model but which are not in the map yet
									// the only ones in this situation should be plain species which were not given to bngl for flattening (they are flat already)
//				System.out.println("species context " + sc1.getName() + " not found in the map. Adding it.");
				scMap.put(sc1.getName(), sc1);
				sMap.put(sc1.getName(), sc1.getSpecies());
				noMapForThese.add(sc1);
			}
		}
		for(Species s1 : model.getSpecies()) {
			boolean found = false;
			for(Map.Entry<String, Species> entry : sMap.entrySet()) {
				Species s2 = entry.getValue();
				if(s1.getCommonName().equals(s2.getCommonName())) {
					found = true;
//					System.out.println("found species " + s1.getCommonName());
					break;
				}
			}
			if(found == false) {
				System.err.println("species " + s1.getCommonName() + " not found in the map!");
			}
		}
		SpeciesContext[] sca = new SpeciesContext[scMap.size()];
		scMap.values().toArray(sca);
		Species[] sa = new Species[sMap.size()];
		sMap.values().toArray(sa); 

		model.setSpecies(sa);
		model.setSpeciesContexts(sca);
		
		for(SpeciesContext sc : sca) {
			if(noMapForThese.contains(sc)) {
				continue;
			}
			SpeciesContextSpec scs = reactionContext.getSpeciesContextSpec(sc);
			Parameter param = scs.getParameter(SpeciesContextSpec.ROLE_InitialConcentration);
			BNGSpecies s = crossMap.get(sc.getName());
			param.setExpression(s.getConcentration());
			SpeciesContext origSpeciesContext = simContext.getModel().getSpeciesContext(s.getName());
			if (origSpeciesContext!=null){
				ModelEntityMapping em = new ModelEntityMapping(origSpeciesContext,sc);
				entityMappings.add(em);
			}else{
				ModelEntityMapping em = new ModelEntityMapping(new GeneratedSpeciesSymbolTableEntry(sc),sc);
				entityMappings.add(em);
			}
		}
//		for(SpeciesContext sc : sca) {		// clean all the species patterns from the flattened species, we have no sp now
//			sc.setSpeciesPattern(null);
//		}
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		msg = "Adding " + outputSpec.getBNGSpecies().length + " species to model, " + elapsedTime + " ms";
		tcm = new TaskCallbackMessage(TaskCallbackStatus.Notification, msg);
		simContext.appendToConsole(tcm);
		System.out.println(msg);
		
		// ---- Reactions -----------------------------------------------------------------------------------------------------
		mathMappingCallback.setMessage("generating network: adding reactions...");
		mathMappingCallback.setProgressFraction(progressFractionQuota/4.0f*3.0f);
		startTime = System.currentTimeMillis();
		System.out.println("\nReactions :");
		Map<String, ReactionStep> reactionStepMap = new HashMap<String, ReactionStep>();
		for (int i = 0; i < outputSpec.getBNGReactions().length; i++){
			BNGReaction r = outputSpec.getBNGReactions()[i];
//			System.out.println(i+1 + ":\t\t"+ r.writeReaction());
			int count=0;
			String reactionName = null;
			while (true) {
				reactionName = "r" + count;	
//				if (model.getReactionStep(reactionName) == null && model.getRbmModelContainer().getReactionRule(reactionName) == null && !reactionStepMap.containsKey(reactionName)) {
				if (model.getReactionStep(reactionName) == null && !reactionStepMap.containsKey(reactionName)) {	// we can reuse the reaction rule labels
					break;
				}	
				count++;
			}
			SimpleReaction sr = new SimpleReaction(model, model.getStructure(0), reactionName);
			for (int j = 0; j < r.getReactants().length; j++){
				BNGSpecies s = r.getReactants()[j];
				String scName = speciesMap.get(s.getNetworkFileIndex());
				SpeciesContext sc = model.getSpeciesContext(scName);
				Reactant reactant = sr.getReactant(scName);
				if(reactant == null) { 
					int stoichiometry = 1;
					sr.addReactant(sc, stoichiometry);
				} else {
					int stoichiometry = reactant.getStoichiometry();
					stoichiometry += 1;
					reactant.setStoichiometry(stoichiometry);
				}
			}
			for (int j = 0; j < r.getProducts().length; j++){
				BNGSpecies s = r.getProducts()[j];
				String scName = speciesMap.get(s.getNetworkFileIndex());
				SpeciesContext sc = model.getSpeciesContext(scName);
				Product product = sr.getProduct(scName);
				if(product == null) { 
					int stoichiometry = 1;
					sr.addProduct(sc, stoichiometry);
				} else {
					int stoichiometry = product.getStoichiometry();
					stoichiometry += 1;
					product.setStoichiometry(stoichiometry);
				}
			}
			MassActionKinetics k = new MassActionKinetics(sr);
			sr.setKinetics(k);
			KineticsParameter kforward = k.getForwardRateParameter();
			sr.getKinetics().setParameterValue(kforward, r.getParamExpression());
//			model.addReactionStep(sr);
			reactionStepMap.put(reactionName, sr);
		}
		for(ReactionStep rs : model.getReactionSteps()) {
			reactionStepMap.put(rs.getName(), rs);
		}
		ReactionStep[] reactionSteps = new ReactionStep[reactionStepMap.size()];
		reactionStepMap.values().toArray(reactionSteps); 
		model.setReactionSteps(reactionSteps);
		if (mathMappingCallback.isInterrupted()){
			msg = "Canceled by user.";
			tcm = new TaskCallbackMessage(TaskCallbackStatus.Error, msg);
			simContext.appendToConsole(tcm);
			throw new UserCancelException(msg);
		}
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		msg = "Adding " + outputSpec.getBNGReactions().length + " reactions to model, " + elapsedTime + " ms";
		tcm = new TaskCallbackMessage(TaskCallbackStatus.Notification, msg);
		simContext.appendToConsole(tcm);
		System.out.println(msg);
		// clean all the reaction rules
		model.getRbmModelContainer().getReactionRuleList().clear();

		// ---- Observables -------------------------------------------------------------------------------------------------
		mathMappingCallback.setMessage("generating network: adding observables...");
		mathMappingCallback.setProgressFraction(progressFractionQuota/8.0f*7.0f);
		startTime = System.currentTimeMillis();
		System.out.println("\nObservables :");
		for (int i = 0; i < outputSpec.getObservableGroups().length; i++){
			ObservableGroup o = outputSpec.getObservableGroups()[i];
//			System.out.println(i+1 + ":\t\t" + o.toString());
			
			if(model.getRbmModelContainer().getParameter(o.getObservableGroupName()) != null) {
				System.out.println("   ...already exists.");
				continue;		// if it's already there we don't try to add it again; this should be true for all of them!
			}
			Expression exp = null;
			for (int j=0; j<o.getListofSpecies().length; j++){
				Expression term = Expression.mult(new Expression(o.getSpeciesMultiplicity()[j]),new Expression(speciesMap.get(o.getListofSpecies()[j].getNetworkFileIndex())));
				if (exp == null){
					exp = term;
				}else{
					exp = Expression.add(exp,term);
				}
			}
			exp.bindExpression(model.getRbmModelContainer().getSymbolTable());
			RbmObservable originalObservable = model.getRbmModelContainer().getObservable(o.getObservableGroupName());
			model.getRbmModelContainer().removeObservable(originalObservable);
			Parameter newParameter = model.getRbmModelContainer().addParameter(o.getObservableGroupName(), exp, originalObservable.getUnitDefinition());

			RbmObservable origObservable = simContext.getModel().getRbmModelContainer().getObservable(o.getObservableGroupName());
			ModelEntityMapping em = new ModelEntityMapping(origObservable,newParameter);
			entityMappings.add(em);
		}
		if (mathMappingCallback.isInterrupted()){
			msg = "Canceled by user.";
			tcm = new TaskCallbackMessage(TaskCallbackStatus.Error, msg);
			simContext.appendToConsole(tcm);
			throw new UserCancelException(msg);
		}
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		msg = "Adding " + outputSpec.getObservableGroups().length + " observables to model, " + elapsedTime + " ms";
		tcm = new TaskCallbackMessage(TaskCallbackStatus.Notification, msg);
		simContext.appendToConsole(tcm);
		System.out.println(msg);

		} catch (PropertyVetoException ex) {
			ex.printStackTrace(System.out);
			throw new RuntimeException(ex.getMessage());
		} catch (ExpressionBindingException ex) {
			ex.printStackTrace(System.out);
			throw new RuntimeException(ex.getMessage());
		} catch (ModelException ex) {
			ex.printStackTrace(System.out);
			throw new RuntimeException(ex.getMessage());
		} catch (ExpressionException ex) {
			ex.printStackTrace(System.out);
			throw new RuntimeException(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		System.out.println("Done transforming");		
		msg = "Generating math...";
		tcm = new TaskCallbackMessage(TaskCallbackStatus.Notification, msg);
		simContext.appendToConsole(tcm);
		System.out.println(msg);
		mathMappingCallback.setMessage(msg);
		mathMappingCallback.setProgressFraction(progressFractionQuota);
	}

}
