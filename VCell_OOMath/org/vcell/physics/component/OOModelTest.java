package org.vcell.physics.component;
import org.jdom.Element;
import org.vcell.units.VCUnitDefinition;

import jscl.plugin.Expression;
import jscl.plugin.ParseException;


import cbit.util.xml.XmlUtil;

/**
 * Insert the type's description here.
 * Creation date: (1/16/2006 11:33:57 PM)
 * @author: Jim Schaff
 */
public class OOModelTest {
	/**
	 * Insert the method's description here.
	 * Creation date: (1/16/2006 11:34:20 PM)
	 * @return ncbc.physics2.component.Model
	 * @throws ParseException 
	 */
	public static OOModel getPlanarPendulumExample() throws ParseException {

		OOModel oOModel = new OOModel();
		
		ModelComponent pendulum = new ModelComponent("pendulum");
		pendulum.addSymbol(new Parameter("m",VCUnitDefinition.getInstance("kg")));
		pendulum.addSymbol(new Variable("F(t)",VCUnitDefinition.getInstance("N")));
		pendulum.addSymbol(new Parameter("g",VCUnitDefinition.getInstance("m.s-2")));
		pendulum.addSymbol(new Parameter("L",VCUnitDefinition.getInstance("m")));
		pendulum.addSymbol(new Variable("Px(t)",VCUnitDefinition.getInstance("m")));
		pendulum.addSymbol(new Variable("Px(0)",VCUnitDefinition.getInstance("m")));
		pendulum.addSymbol(new Variable("Py(t)",VCUnitDefinition.getInstance("m")));
		pendulum.addSymbol(new Variable("Py(0)",VCUnitDefinition.getInstance("m")));
		pendulum.addSymbol(new Variable("Vx(t)",VCUnitDefinition.getInstance("m.s-1")));
		pendulum.addSymbol(new Variable("Vx(0)",VCUnitDefinition.getInstance("m.s-1")));
		pendulum.addSymbol(new Variable("Vy(t)",VCUnitDefinition.getInstance("m.s-1")));
		pendulum.addSymbol(new Variable("Vy(0)",VCUnitDefinition.getInstance("m.s-1")));
		pendulum.addEquation(Expression.valueOf("m*d(Vx(t),t) + Px(t)/L*F(t)"));
		pendulum.addEquation(Expression.valueOf("m*d(Vy(t),t) + Py(t)/L*F(t) + m*g"));
		pendulum.addEquation(Expression.valueOf("Px(t)^2 + Py(t)^2 - L^2"));
		pendulum.addEquation(Expression.valueOf("d(Px(t),t) - Vx(t)"));
		pendulum.addEquation(Expression.valueOf("d(Py(t),t) - Vy(t)"));
		pendulum.addEquation(Expression.valueOf("L - 1"));
		pendulum.addEquation(Expression.valueOf("m - 1"));
		pendulum.addEquation(Expression.valueOf("Px(0)-1"));
		pendulum.addEquation(Expression.valueOf("Vx(0)-0.0"));
		pendulum.addEquation(Expression.valueOf("Py(0)-0"));
		pendulum.addEquation(Expression.valueOf("Vy(0)-0.0"));
		//pendulum.addEquation(Expression.valueOf("F - 1"));
		pendulum.addEquation(Expression.valueOf("g - 9.8"));

		oOModel.addModelComponent(pendulum);
		
		return oOModel;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (1/16/2006 11:34:20 PM)
	 * @return ncbc.physics2.component.Model
	 * @throws ParseException 
	 */
	public static OOModel getPlanarPendulumExample_Simple() throws ParseException {

		OOModel oOModel = new OOModel();
		
		ModelComponent pendulum = new ModelComponent("pendulum");
//		pendulum.addSymbol(new Parameter("m",VCUnitDefinition.getInstance("kg")));
		pendulum.addSymbol(new Variable("F(t)",VCUnitDefinition.getInstance("N")));
//		pendulum.addSymbol(new Parameter("g",VCUnitDefinition.getInstance("m.s-2")));
//		pendulum.addSymbol(new Parameter("L",VCUnitDefinition.getInstance("m")));
		pendulum.addSymbol(new Variable("Px(t)",VCUnitDefinition.getInstance("m")));
//		pendulum.addSymbol(new Variable("Px(0)",VCUnitDefinition.getInstance("m")));
		pendulum.addSymbol(new Variable("Py(t)",VCUnitDefinition.getInstance("m")));
//		pendulum.addEquation(Expression.valueOf("m*Px''(t) + Px(t)/L*F(t)"));
//		pendulum.addEquation(Expression.valueOf("m*Py''(t) + Py(t)/L*F(t) + m*g"));
//		pendulum.addEquation(Expression.valueOf("Px(t)^2 + Py(t)^2 - L^2"));
		pendulum.addEquation(Expression.valueOf("Px''(t) + Px(t)*F(t)"));
		pendulum.addEquation(Expression.valueOf("Py''(t) + Py(t)*F(t) + 9.8"));
		pendulum.addEquation(Expression.valueOf("Px(t)^2 + Py(t)^2 - 1"));
//		pendulum.addEquation(Expression.valueOf("L - 1"));
//		pendulum.addEquation(Expression.valueOf("m - 1"));
//		pendulum.addEquation(Expression.valueOf("Px(0)-1"));
//		pendulum.addEquation(Expression.valueOf("g - 9.8"));

		oOModel.addModelComponent(pendulum);
		
		return oOModel;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (1/16/2006 11:34:20 PM)
	 * @return ncbc.physics2.component.Model
	 * @throws ParseException 
	 */
	public static OOModel getCircuit_INDEX_REDUCTION() throws ParseException {

		OOModel oOModel = new OOModel();
		
		ModelComponent pendulum = new ModelComponent("circuit");
		pendulum.addSymbol(new Variable("i(t)",VCUnitDefinition.getInstance("A")));
		pendulum.addSymbol(new Variable("i1(t)",VCUnitDefinition.getInstance("A")));
		pendulum.addSymbol(new Variable("i2(t)",VCUnitDefinition.getInstance("A")));
		pendulum.addSymbol(new Variable("v1(t)",VCUnitDefinition.getInstance("V")));
		pendulum.addSymbol(new Variable("v2(t)",VCUnitDefinition.getInstance("V")));
		pendulum.addEquation(Expression.valueOf("v1'(t) + v1(t) - i1(t)"));
		pendulum.addEquation(Expression.valueOf("v2'(t) + v2(t) - i2(t)"));
		pendulum.addEquation(Expression.valueOf("v1(t) - v2(t)"));
		pendulum.addEquation(Expression.valueOf("i1(t) + i2(t) - i(t)"));
		pendulum.addEquation(Expression.valueOf("i(t) - sin(t)"));

		oOModel.addModelComponent(pendulum);
		
		return oOModel;
	}


	/**
	 * Insert the method's description here.
	 * Creation date: (1/16/2006 11:34:20 PM)
	 * @return ncbc.physics2.component.Model
	 */
	public static OOModel getExample() {

		//
		//
		//              +   R1
		//         c1____/\/\/\____c2
		//         |                |
		//         |                |
		//         |                |+
		//        +|              _____
		//       [ Vs ]           _____ C1
		//         |                |
		//         |                |
		//         |                |
		//         |________________|
		//                  |
		//                  O gnd
		//                  |
		//               ______
		//                ____   
		//                 __
		//
		//
		//
		OOModel oOModel = new OOModel();
		VoltageSource vs = new VoltageSource("VS",10);
		Resistor r = new Resistor("R1",1);
		Capacitor c = new Capacitor("C1",1,1);
		Ground gnd = new Ground("gnd");
		Connection c1 = new Connection(new Connector[] { vs.getConnectors(0), r.getConnectors(0) });
		Connection c2 = new Connection(new Connector[] { r.getConnectors(1), c.getConnectors(0) });
		Connection c3 = new Connection(new Connector[] { vs.getConnectors(1), c.getConnectors(1), gnd.getConnectors(0) });

		oOModel.addModelComponent(vs);
		oOModel.addModelComponent(r);
		oOModel.addModelComponent(c);
		oOModel.addModelComponent(gnd);
		oOModel.addConnection(c1);
		oOModel.addConnection(c2);
		oOModel.addConnection(c3);
		
		return oOModel;
	}


	/**
	 * Insert the method's description here.
	 * Creation date: (1/16/2006 11:34:20 PM)
	 * @return ncbc.physics2.component.Model
	 * @throws ParseException 
	 */
	public static OOModel getExampleSpineDendrite() throws ParseException {

		//
		//
		//                                      R_spine_proxD                 R_proxD_distD
		//      ________________________spine______/\/\/\_+_____________proxD___/\/\/\_+_____________distD
		//      |        |       |        |                    |          |                   |        |
		//      |        |       |        |                    |          |                   |        |
		//      |+       |+      |+       |+                   |+         |+                 +|        |+
		//      |        |       |      _____                  |       _____                  |      _____
		//  [NaChan]  [KChan]  [Leak]   _____ C_spine       [I_proxD]  _____ C_proxD      [I_distD]  _____ C_distD
		//      |        |       |        |                    |          |                   |        |
		//      |        |       |        |                    |          |                   |        |
		//      |        |       |        |                    |          |                   |        |
		//      |________|_______|________|____________________|__________|___________________|________|
		//                   |
		//                   O ec
		//                   |
		//                ______
		//                 ____   
		//                  __
		//
		//
		//
		double KMOLE = 1.0/602.0;
		double spineVolume = 1.0;
		double spineArea = 1.0;
		double ecVolume = 100;
		double proxDendriteVolume = 20.0;
		double proxDendriteArea = 10.0;
		double distDendriteVolume = 20.0;
		double distDendriteArea = 10.0;
		double NaSpineConcInit = 50000;
		double NaSpineNumInit = NaSpineConcInit*spineVolume/KMOLE;
		double KSpineConcInit = 397000;
		double KSpineNumInit = KSpineConcInit*spineVolume/KMOLE;
		double NaEcConcInit = 437000;
		double NaEcNumInit = NaEcConcInit*ecVolume/KMOLE;
		double KEcConcInit = 20000;
		double KEcNumInit = KEcConcInit*ecVolume/KMOLE;
		double NaProxDendriteConcInit = 50000;
		double NaProxDendriteNumInit = NaProxDendriteConcInit*proxDendriteVolume/KMOLE;
		double KProxDendriteConcInit = 397000;
		double KProxDendriteNumInit = KProxDendriteConcInit*proxDendriteVolume/KMOLE;
		double NaDistDendriteConcInit = 50000;
		double NaDistDendriteNumInit = NaDistDendriteConcInit*distDendriteVolume/KMOLE;
		double KDistDendriteConcInit = 397000;
		double KDistDendriteNumInit = KDistDendriteConcInit*distDendriteVolume/KMOLE;
		
		OOModel oOModel = new OOModel();
		NaChannel NaChannelSpine = new NaChannel("NaChannelSpine");
		KChannel KChannelSpine = new KChannel("KChannelSpine");
		LeakChannel LeakSpine = new LeakChannel("LeakSpine");
		Capacitor C_spine = new Capacitor("C_spine",0.01*spineArea,-62.897633102);
		Species Na_spine = new Species("Na_spine",Expression.valueOf(""+NaSpineNumInit));
		Species K_spine = new Species("K_spine",Expression.valueOf(""+KSpineNumInit));

		Resistor R_spine_proxD = new Resistor("R_spine_proxD",1*1e6);

		NaChannel NaChannelProxD = new NaChannel("NaChannelProxD");
		KChannel KChannelProxD = new KChannel("KChannelProxD");
		LeakChannel LeakProxD = new LeakChannel("LeakProxD");
		Capacitor C_ProxD = new Capacitor("C_ProxD",0.01*proxDendriteArea,-62.897633102);
		Species Na_ProxD = new Species("Na_ProxD",Expression.valueOf(""+NaProxDendriteNumInit));
		Species K_ProxD = new Species("K_ProxD",Expression.valueOf(""+KProxDendriteNumInit));
		
		Resistor R_proxD_distD = new Resistor("R_proxD_distD",1*1e6);

		NaChannel NaChannelDistD = new NaChannel("NaChannelDistD");
		KChannel KChannelDistD = new KChannel("KChannelDistD");
		LeakChannel LeakDistD = new LeakChannel("LeakDistD");
		Capacitor C_DistD = new Capacitor("C_DistD",0.01*distDendriteArea,-62.897633102);
		Species Na_DistD = new Species("Na_DistD",Expression.valueOf(""+NaDistDendriteNumInit));
		Species K_DistD = new Species("K_DistD",Expression.valueOf(""+KDistDendriteNumInit));

		Ground GND_ec = new Ground("GND_ec");
		Species Na_ec = new Species("Na_ec",Expression.valueOf(""+NaEcNumInit));
		Species K_ec = new Species("K_ec",Expression.valueOf(""+KEcNumInit));
		
		LumpedLocation spine = new LumpedLocation("spine",3,spineVolume);
//		LumpedLocation spineMem = new LumpedLocation("spineMem",2,spineArea);
		LumpedLocation ec = new LumpedLocation("ec",3,ecVolume);
		LumpedLocation proxDendrite = new LumpedLocation("proxDendrite",3,proxDendriteVolume);
//		LumpedLocation proxDendriteMem = new LumpedLocation("proxDendriteMem",2,proxDendriteArea);
		LumpedLocation distDendrite = new LumpedLocation("distDendrite",3,distDendriteVolume);
//		LumpedLocation distDendriteMem = new LumpedLocation("distDendrite",2,distDendriteArea);

//		spineMem.addAdjacentLocation(spine);
//		spineMem.addAdjacentLocation(ec);
		
		
		oOModel.addModelComponent(NaChannelSpine);
		oOModel.addModelComponent(KChannelSpine);
		oOModel.addModelComponent(LeakSpine);
		oOModel.addModelComponent(C_spine);
		oOModel.addModelComponent(Na_spine);
		oOModel.addModelComponent(K_spine);

		oOModel.addModelComponent(R_spine_proxD);

		oOModel.addModelComponent(NaChannelProxD);
		oOModel.addModelComponent(KChannelProxD);
		oOModel.addModelComponent(LeakProxD);
		oOModel.addModelComponent(C_ProxD);
		oOModel.addModelComponent(Na_ProxD);
		oOModel.addModelComponent(K_ProxD);

		oOModel.addModelComponent(R_proxD_distD);

		oOModel.addModelComponent(NaChannelDistD);
		oOModel.addModelComponent(KChannelDistD);
		oOModel.addModelComponent(LeakDistD);
		oOModel.addModelComponent(C_DistD);
		oOModel.addModelComponent(Na_DistD);
		oOModel.addModelComponent(K_DistD);

		oOModel.addModelComponent(GND_ec);
		oOModel.addModelComponent(Na_ec);
		oOModel.addModelComponent(K_ec);
		oOModel.addModelComponent(spine);
		//oOModel.addModelComponent(spineMem);
		oOModel.addModelComponent(ec);
		oOModel.addModelComponent(proxDendrite);
		//oOModel.addModelComponent(proxDendriteMem);
		oOModel.addModelComponent(distDendrite);
		//oOModel.addModelComponent(distDendriteMem);
		

		Connection conn_elect_spine = new Connection(new Connector[] { 
				KChannelSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
				 NaChannelSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
				LeakSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS),
				C_spine.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
				spine.getConnectors(LumpedLocation.CONNECTOR_ELECTRICAL),
				R_spine_proxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),
				});
		Connection conn_elect_proxD = new Connection(new Connector[] { 
				KChannelProxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
				NaChannelProxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
				LeakProxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS),
				C_ProxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
				proxDendrite.getConnectors(LumpedLocation.CONNECTOR_ELECTRICAL),
				R_spine_proxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS),
				R_proxD_distD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),
				});
		Connection conn_elect_distD = new Connection(new Connector[] { 
				KChannelDistD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
				NaChannelDistD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
				LeakDistD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS),
				C_DistD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
				distDendrite.getConnectors(LumpedLocation.CONNECTOR_ELECTRICAL),
				R_proxD_distD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS),
				});
		Connection conn_elect_ec = new Connection(new Connector[] { 
				KChannelSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG), 
				NaChannelSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG), 
				LeakSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),
				C_spine.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),  
				ec.getConnectors(LumpedLocation.CONNECTOR_ELECTRICAL),
				KChannelProxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG), 
				NaChannelProxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG), 
				LeakProxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),
				C_ProxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),  
				KChannelDistD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG), 
				NaChannelDistD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG), 
				LeakDistD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),
				C_DistD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),  
				GND_ec.getConnectors(0) });
		
		Connection conn_Na_spine_conc = new Connection(new Connector[] {
				Na_spine.getConnectors(Species.CONNECTOR_CONC),
				NaChannelSpine.getConnectors(IonChannelHH.CONNECTOR_ION_INSIDE_CONC),
				});
		Connection conn_K_spine_conc = new Connection(new Connector[] {
				K_spine.getConnectors(Species.CONNECTOR_CONC),
				KChannelSpine.getConnectors(IonChannelHH.CONNECTOR_ION_INSIDE_CONC),
				});
		
		Connection conn_Na_proxD_conc = new Connection(new Connector[] {
				Na_ProxD.getConnectors(Species.CONNECTOR_CONC),
				NaChannelProxD.getConnectors(IonChannelHH.CONNECTOR_ION_INSIDE_CONC),
				});
		Connection conn_K_proxD_conc = new Connection(new Connector[] {
				K_ProxD.getConnectors(Species.CONNECTOR_CONC),
				KChannelProxD.getConnectors(IonChannelHH.CONNECTOR_ION_INSIDE_CONC),
				});
		
		Connection conn_Na_distD_conc = new Connection(new Connector[] {
				Na_DistD.getConnectors(Species.CONNECTOR_CONC),
				NaChannelDistD.getConnectors(IonChannelHH.CONNECTOR_ION_INSIDE_CONC),
				});
		Connection conn_K_distD_conc = new Connection(new Connector[] {
				K_DistD.getConnectors(Species.CONNECTOR_CONC),
				KChannelDistD.getConnectors(IonChannelHH.CONNECTOR_ION_INSIDE_CONC),
				});
		
		Connection conn_Na_ec_conc = new Connection(new Connector[] {
				Na_ec.getConnectors(Species.CONNECTOR_CONC),
				NaChannelSpine.getConnectors(IonChannelHH.CONNECTOR_ION_OUTSIDE_CONC), 
				NaChannelProxD.getConnectors(IonChannelHH.CONNECTOR_ION_OUTSIDE_CONC), 
				NaChannelDistD.getConnectors(IonChannelHH.CONNECTOR_ION_OUTSIDE_CONC), 
				});
		Connection conn_K_ec_conc = new Connection(new Connector[] {
				K_ec.getConnectors(Species.CONNECTOR_CONC),
				KChannelSpine.getConnectors(IonChannelHH.CONNECTOR_ION_OUTSIDE_CONC), 
				KChannelProxD.getConnectors(IonChannelHH.CONNECTOR_ION_OUTSIDE_CONC), 
				KChannelDistD.getConnectors(IonChannelHH.CONNECTOR_ION_OUTSIDE_CONC), 
				});
		
		Connection conn_spine_size = new Connection(new Connector[]{
				K_spine.getConnector(Species.CONNECTOR_SIZE),
				Na_spine.getConnector(Species.CONNECTOR_SIZE),
				spine.getConnectors(LumpedLocation.CONNECTOR_SIZE),
				});
		Connection conn_ec_size = new Connection(new Connector[]{
				K_ec.getConnector(Species.CONNECTOR_SIZE),
				Na_ec.getConnector(Species.CONNECTOR_SIZE),
				ec.getConnectors(LumpedLocation.CONNECTOR_SIZE),
				});
		Connection conn_proxD_size = new Connection(new Connector[]{
				K_ProxD.getConnector(Species.CONNECTOR_SIZE),
				Na_ProxD.getConnector(Species.CONNECTOR_SIZE),
				proxDendrite.getConnectors(LumpedLocation.CONNECTOR_SIZE),
				});
		Connection conn_distD_size = new Connection(new Connector[]{
				K_DistD.getConnector(Species.CONNECTOR_SIZE),
				Na_DistD.getConnector(Species.CONNECTOR_SIZE),
				distDendrite.getConnectors(LumpedLocation.CONNECTOR_SIZE),
				});
		
		oOModel.addConnection(conn_elect_spine);
		oOModel.addConnection(conn_elect_proxD);
		oOModel.addConnection(conn_elect_distD);
		oOModel.addConnection(conn_elect_ec);
		
		oOModel.addConnection(conn_Na_spine_conc);
		oOModel.addConnection(conn_K_spine_conc);
		oOModel.addConnection(conn_Na_ec_conc);
		oOModel.addConnection(conn_K_ec_conc);
		oOModel.addConnection(conn_Na_proxD_conc);
		oOModel.addConnection(conn_K_proxD_conc);
		oOModel.addConnection(conn_Na_distD_conc);
		oOModel.addConnection(conn_K_distD_conc);
		
		oOModel.addConnection(conn_spine_size);
		oOModel.addConnection(conn_ec_size);
		oOModel.addConnection(conn_proxD_size);
		oOModel.addConnection(conn_distD_size);
		
		return oOModel;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (1/16/2006 11:34:20 PM)
	 * @return ncbc.physics2.component.Model
	 * @throws ParseException 
	 */
	public static OOModel getExampleSpine() throws ParseException {

		//
		//
		//                                      R_spine_proxD                 R_proxD_distD
		//      ________________________spine______/\/\/\_+_____________proxD___/\/\/\_+_____________distD
		//      |        |       |        |                    |          |                   |        |
		//      |        |       |        |                    |          |                   |        |
		//      |+       |+      |+       |+                   |+         |+                 +|        |+
		//      |        |       |      _____                  |       _____                  |      _____
		//  [NaChan]  [KChan]  [Leak]   _____ C_spine       [I_proxD]  _____ C_proxD      [I_distD]  _____ C_distD
		//      |        |       |        |                    |          |                   |        |
		//      |        |       |        |                    |          |                   |        |
		//      |        |       |        |                    |          |                   |        |
		//      |________|_______|________|____________________|__________|___________________|________|
		//                   |
		//                   O ec
		//                   |
		//                ______
		//                 ____   
		//                  __
		//
		//
		//
		double KMOLE = 1.0/602.0;
		double spineVolume = 1.0;
		double spineArea = 1.0;
		double ecVolume = 100;
		double NaSpineConcInit = 50000;
		double NaSpineNumInit = NaSpineConcInit*spineVolume/KMOLE;
		double KSpineConcInit = 397000;
		double KSpineNumInit = KSpineConcInit*spineVolume/KMOLE;
		double NaEcConcInit = 437000;
		double NaEcNumInit = NaEcConcInit*ecVolume/KMOLE;
		double KEcConcInit = 20000;
		double KEcNumInit = KEcConcInit*ecVolume/KMOLE;
		
		OOModel oOModel = new OOModel();
		NaChannel NaChannelSpine = new NaChannel("NaChannelSpine");
		KChannel KChannelSpine = new KChannel("KChannelSpine");
		LeakChannel LeakSpine = new LeakChannel("LeakSpine");
		Capacitor C_spine = new Capacitor("C_spine",0.01*spineArea,-62.897633102);
		Species Na_spine = new Species("Na_spine",Expression.valueOf(""+NaSpineNumInit));
		Species K_spine = new Species("K_spine",Expression.valueOf(""+KSpineNumInit));
		
//		Resistor R_spine_proxD = new Resistor("R_spine_proxD",1*1e6);
//
//		CurrentSource IS_proxD = new CurrentSource("IS_proxD",0);
//		Capacitor C_proxD = new Capacitor("C_proxD",0.01,-62.897633102);
//		Species Na_proxD = new Species("Na_proxD",Expression.valueOf("50000"));
//		Species K_proxD = new Species("K_proxD",Expression.valueOf("397000"));
//		
//		Resistor R_proxD_distD = new Resistor("R_proxD_distD",1*1e6);
//
//		CurrentSource IS_distD = new CurrentSource("IS_distD",0);
//		Capacitor C_distD = new Capacitor("C_distD",0.01,-62.897633102);
//		Species Na_distD = new Species("Na_distD",Expression.valueOf("50000"));
//		Species K_distD = new Species("K_distD",Expression.valueOf("397000"));
		
		Ground GND_ec = new Ground("GND_ec");
		Species Na_ec = new Species("Na_ec",Expression.valueOf(""+NaEcNumInit));
		Species K_ec = new Species("K_ec",Expression.valueOf(""+KEcNumInit));
		
		LumpedLocation spine = new LumpedLocation("spine",3,spineVolume);
		LumpedLocation spineMem = new LumpedLocation("spineMem",2,spineArea);
		LumpedLocation ec = new LumpedLocation("ec",3,ecVolume);
//		LumpedLocation proxDendrite = new LumpedLocation("proxDendrite",3,1.0);
//		LumpedLocation proxDendriteMem = new LumpedLocation("proxDendriteMem",2,1.0);
//		LumpedLocation distDendrite = new LumpedLocation("distDendrite",3,1.0);
//		LumpedLocation distDendriteMem = new LumpedLocation("distDendrite",2,1.0);

		spineMem.addAdjacentLocation(spine);
		spineMem.addAdjacentLocation(ec);
		
		
		oOModel.addModelComponent(NaChannelSpine);
		oOModel.addModelComponent(KChannelSpine);
		oOModel.addModelComponent(LeakSpine);
		oOModel.addModelComponent(C_spine);
		oOModel.addModelComponent(Na_spine);
		oOModel.addModelComponent(K_spine);
//		oOModel.addModelComponent(R_spine_proxD);
//		oOModel.addModelComponent(IS_proxD);
//		oOModel.addModelComponent(C_proxD);
//		oOModel.addModelComponent(Na_proxD);
//		oOModel.addModelComponent(K_proxD);
//		oOModel.addModelComponent(R_proxD_distD);
//		oOModel.addModelComponent(IS_distD);
//		oOModel.addModelComponent(C_distD);
//		oOModel.addModelComponent(Na_distD);
//		oOModel.addModelComponent(K_distD);
		oOModel.addModelComponent(GND_ec);
		oOModel.addModelComponent(Na_ec);
		oOModel.addModelComponent(K_ec);
		oOModel.addModelComponent(spine);
		oOModel.addModelComponent(spineMem);
		oOModel.addModelComponent(ec);
		

		Connection conn_elect_spine = new Connection(new Connector[] { KChannelSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
																 NaChannelSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
																LeakSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS),
																C_spine.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
																spine.getConnectors(LumpedLocation.CONNECTOR_ELECTRICAL)
																//R_spine_proxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG)
																});
//		Connection conn_elect_proxD = new Connection(new Connector[] { IS_proxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
//																C_proxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
//																R_spine_proxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
//																R_proxD_distD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG) });
//		Connection conn_elect_distD = new Connection(new Connector[] { IS_distD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
//																C_distD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS), 
//																R_proxD_distD.getConnectors(TwoPortElectricalComponent.CONNECTOR_POS) });
		Connection conn_elect_ec = new Connection(new Connector[] { KChannelSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG), 
																NaChannelSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG), 
																LeakSpine.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),
																C_spine.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),  
																ec.getConnectors(LumpedLocation.CONNECTOR_ELECTRICAL),
																//IS_proxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),
																//C_proxD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG),
																//IS_distD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG), 
																//C_distD.getConnectors(TwoPortElectricalComponent.CONNECTOR_NEG), 
																GND_ec.getConnectors(0) });
		
		Connection conn_Na_spine_conc = new Connection(new Connector[] {
				Na_spine.getConnectors(Species.CONNECTOR_CONC),
				NaChannelSpine.getConnectors(IonChannelHH.CONNECTOR_ION_INSIDE_CONC) } );
		Connection conn_K_spine_conc = new Connection(new Connector[] {
				K_spine.getConnectors(Species.CONNECTOR_CONC),
				KChannelSpine.getConnectors(IonChannelHH.CONNECTOR_ION_INSIDE_CONC) } );
		
		Connection conn_Na_ec_conc = new Connection(new Connector[] {
				Na_ec.getConnectors(Species.CONNECTOR_CONC),
				NaChannelSpine.getConnectors(IonChannelHH.CONNECTOR_ION_OUTSIDE_CONC) } );
		Connection conn_K_ec_conc = new Connection(new Connector[] {
				K_ec.getConnectors(Species.CONNECTOR_CONC),
				KChannelSpine.getConnectors(IonChannelHH.CONNECTOR_ION_OUTSIDE_CONC) } );
		
		Connection conn_spine_size = new Connection(new Connector[]{
				K_spine.getConnector(Species.CONNECTOR_SIZE),
				Na_spine.getConnector(Species.CONNECTOR_SIZE),
				spine.getConnectors(LumpedLocation.CONNECTOR_SIZE) });
		Connection conn_ec_size = new Connection(new Connector[]{
				K_ec.getConnector(Species.CONNECTOR_SIZE),
				Na_ec.getConnector(Species.CONNECTOR_SIZE),
				ec.getConnectors(LumpedLocation.CONNECTOR_SIZE) });
		
		oOModel.addConnection(conn_elect_spine);
//		oOModel.addConnection(conn_elect_proxD);
//		oOModel.addConnection(conn_elect_distD);
		oOModel.addConnection(conn_elect_ec);
		
		oOModel.addConnection(conn_Na_spine_conc);
		oOModel.addConnection(conn_K_spine_conc);
		oOModel.addConnection(conn_Na_ec_conc);
		oOModel.addConnection(conn_K_ec_conc);
		
		oOModel.addConnection(conn_spine_size);
		oOModel.addConnection(conn_ec_size);
		
		return oOModel;
	}


	/**
	 * Insert the method's description here.
	 * Creation date: (1/16/2006 11:34:20 PM)
	 * @return ncbc.physics2.component.Model
	 */
	public static OOModel getExampleSpineSimple() {

		//
		//
		//                         R_spine_proxD
		//         _________spine______/\/\/\_+_____________proxD___/\/\/\_+_____________distD
		//         |          |                    |          |                   |        |
		//         |          |                    |          |                   |        |
		//         |+         |+                   |+         |+                 +|        |+
		//         |        _____                  |       _____                  |      _____
		//       [I_spine]  _____ C_spine       [I_proxD]  _____ C_proxD      [I_distD]  _____ C_distD
		//         |          |                    |          |                   |        |
		//         |          |                    |          |                   |        |
		//         |          |                    |          |                   |        |
		//         |__________|____________________|__________|___________________|________|
		//              |
		//              O ec
		//              |
		//           ______
		//            ____   
		//             __
		//
		//
		//
		OOModel oOModel = new OOModel();
		CurrentSource IS_spine = new CurrentSource("IS_spine",10);
		Capacitor C_spine = new Capacitor("C_spine",1,1);
		
		Resistor R_spine_proxD = new Resistor("R_spine_proxD",1);

		CurrentSource IS_proxD = new CurrentSource("IS_proxD",10);
		Capacitor C_proxD = new Capacitor("C_proxD",1,1);
		
		Resistor R_proxD_distD = new Resistor("R_proxD_distD",1);

		CurrentSource IS_distD = new CurrentSource("IS_distD",10);
		Capacitor C_distD = new Capacitor("C_distD",1,1);
		
		Ground GND_ec = new Ground("GND_ec");
		
		Connection conn_spine = new Connection(new Connector[] { IS_spine.getConnectors(0), C_spine.getConnectors(0), R_spine_proxD.getConnectors(1) });
		Connection conn_proxD = new Connection(new Connector[] { IS_proxD.getConnectors(0), C_proxD.getConnectors(0), R_spine_proxD.getConnectors(0), R_proxD_distD.getConnectors(1) });
		Connection conn_distD = new Connection(new Connector[] { IS_distD.getConnectors(0), C_distD.getConnectors(0), R_proxD_distD.getConnectors(0) });
		Connection conn_ec = new Connection(new Connector[] { IS_spine.getConnectors(1), C_spine.getConnectors(1), 
															  IS_proxD.getConnectors(1), C_proxD.getConnectors(1), 
															  IS_distD.getConnectors(1), C_distD.getConnectors(1), 
															  GND_ec.getConnectors(0) });

		oOModel.addModelComponent(IS_spine);
		oOModel.addModelComponent(C_spine);
		oOModel.addModelComponent(R_spine_proxD);
		oOModel.addModelComponent(IS_proxD);
		oOModel.addModelComponent(C_proxD);
		oOModel.addModelComponent(R_proxD_distD);
		oOModel.addModelComponent(IS_distD);
		oOModel.addModelComponent(C_distD);
		oOModel.addModelComponent(GND_ec);
		oOModel.addConnection(conn_spine);
		oOModel.addConnection(conn_proxD);
		oOModel.addConnection(conn_distD);
		oOModel.addConnection(conn_ec);
		
		return oOModel;
	}


/**
 * Insert the method's description here.
 * Creation date: (1/16/2006 11:34:20 PM)
 * @return ncbc.physics2.component.Model
 */
public static OOModel getExample2() {

	//
	//
	//                 R1           L1
	//         c1____/\/\/\__c2___UUUUU___c3
	//         |                          |
	//         |                          |
	//         |                          |
	//         |                        _____
	//       [ Vs ]                     _____ C1
	//         |                          |
	//         |                          |
	//         |                          |
	//         |________c4________________|
	//                  |
	//                  O gnd
	//                  |
	//               ______
	//                ____   
	//                 __
	//
	//
	//
	OOModel oOModel = new OOModel();
	VoltageSource vs = new VoltageSource("VS",10);
	Resistor R = new Resistor("R1",1);
	Capacitor C = new Capacitor("C1",1,1);
	Inductor L = new Inductor("L1",1);
	Ground gnd = new Ground("gnd");
	Connection c1 = new Connection(new Connector[] { vs.getConnectors(0), R.getConnectors(1) });
	Connection c2 = new Connection(new Connector[] { R.getConnectors(0), L.getConnectors(1) });
	Connection c3 = new Connection(new Connector[] { L.getConnectors(0), C.getConnectors(1) });
	Connection c4 = new Connection(new Connector[] { vs.getConnectors(1), C.getConnectors(0), gnd.getConnectors(0) });

	oOModel.addModelComponent(vs);
	oOModel.addModelComponent(R);
	oOModel.addModelComponent(L);
	oOModel.addModelComponent(C);
	oOModel.addModelComponent(gnd);
	oOModel.addConnection(c1);
	oOModel.addConnection(c2);
	oOModel.addConnection(c3);
	oOModel.addConnection(c4);
	
	return oOModel;
}


/**
 * Insert the method's description here.
 * Creation date: (1/16/2006 11:34:20 PM)
 * @return ncbc.physics2.component.Model
 */
public static OOModel getExample3() {

	//
	//
	//                     
	//          ___________________c1_____
	//         |                   |      |
	//         |                   |      |
	//         |                   |      |
	//         |                   \    _____
	//       [ Vs ]             R1 /    _____ C1
	//         |                   \      |
	//         |                   /      |
	//         |                   |      |
	//         |________c2_________|______|
	//                  |
	//                  O gnd
	//                  |
	//               ______
	//                ____   
	//                 __
	//
	//
	//
	OOModel oOModel = new OOModel();
	VoltageSource vs = new VoltageSource("VS",10);
	Resistor R = new Resistor("R1",1);
	Capacitor C = new Capacitor("C1",1,1);
	Ground gnd = new Ground("gnd");
	Connection c1 = new Connection(new Connector[] { vs.getConnectors(1), R.getConnectors(1), C.getConnectors(1) });
	Connection c2 = new Connection(new Connector[] { vs.getConnectors(0), R.getConnectors(0), C.getConnectors(0), gnd.getConnectors(0) });

	oOModel.addModelComponent(vs);
	oOModel.addModelComponent(R);
	oOModel.addModelComponent(C);
	oOModel.addModelComponent(gnd);
	oOModel.addConnection(c1);
	oOModel.addConnection(c2);
	
	return oOModel;
}


/**
 * Insert the method's description here.
 * Creation date: (1/16/2006 11:34:20 PM)
 * @return ncbc.physics2.component.Model
 */
public static OOModel getExample4() throws ParseException {

	//
	//
	//                     
	//        c1   ____________               
	//     A ===> |            |      
	//            |    R1      |      c3       __________
	//        c2  |            | ===> C  ===> |          |  c5
	//     B ===> |____________|              |    R2    | ===> E
	//                                    c4  |          |
	//                                D  ===> |          |
	//                                        |__________|
	//
	//
	//
	OOModel oOModel = new OOModel();
	MassActionReaction R1 = new MassActionReaction("R1",new String[] { "r1","r2","p1" }, new int[] { -1, -1, 1 }, false);
	MassActionReaction R2 = new MassActionReaction("R2",new String[] { "r1","r2","p1" }, new int[] { -1, -1, 1 }, true);
	Species A = new Species("A",Expression.valueOf("1"));
	Species B = new Species("B",Expression.valueOf("2"));
	Species C = new Species("C",Expression.valueOf("3"));
	Species D = new Species("D",Expression.valueOf("4"));
	Species E = new Species("E",Expression.valueOf("5"));
	Connection c1 = new Connection(new Connector[] { R1.getConnectors(0), A.getConnectors(0) });
	Connection c2 = new Connection(new Connector[] { R1.getConnectors(1), B.getConnectors(0) });
	Connection c3 = new Connection(new Connector[] { R1.getConnectors(2), C.getConnectors(0), R2.getConnectors(0) });
	Connection c4 = new Connection(new Connector[] { R2.getConnectors(1), D.getConnectors(0) });
	Connection c5 = new Connection(new Connector[] { R2.getConnectors(2), E.getConnectors(0) });

	oOModel.addModelComponent(R1);
	oOModel.addModelComponent(R2);
	oOModel.addModelComponent(A);
	oOModel.addModelComponent(B);
	oOModel.addModelComponent(C);
	oOModel.addModelComponent(D);
	oOModel.addModelComponent(E);
	oOModel.addConnection(c1);
	oOModel.addConnection(c2);
	oOModel.addConnection(c3);
	oOModel.addConnection(c4);
	oOModel.addConnection(c5);
	
	return oOModel;
}


/**
 * Insert the method's description here.
 * Creation date: (1/16/2006 11:34:20 PM)
 * @return ncbc.physics2.component.Model
 */
public static OOModel getExample5() {

	//
	//
	//                     
	//          ___________________c1_____________
	//         |                   |              |
	//         |                   |              |+
	//         |                   |+            C_
	//         |                   \             C_
	//         |                R1 /             C_  L1
	//         |                   \             C_
	//         |                   /              |
	//         |+                  |              |
	//       [ Vs ]                |      R2      |
	//         |                 c3|____/\/\/\____|
	//         |                   |          +   |
	//         |                  +|              |
	//         |                   \              |
	//         |                R3 /              |
	//         |                   \              |
	//         |                   /              |
	//         |                   |              |
	//         |________c2_________|______________|
	//                  |
	//                  | gnd
	//                  |
	//               ______
	//                ____   
	//                 __
	//
	//
	//
	OOModel oOModel = new OOModel();
	VoltageSource vs = new VoltageSource("VS",10);
	Resistor R1 = new Resistor("R1",1);
	Resistor R2 = new Resistor("R2",1);
	Resistor R3 = new Resistor("R3",1);
	Inductor L1 = new Inductor("L1",1);
	Ground gnd = new Ground("gnd");
	Connection c1 = new Connection(new Connector[] { vs.getConnectors(1), R1.getConnectors(1), L1.getConnectors(1) });
	Connection c2 = new Connection(new Connector[] { vs.getConnectors(0), R3.getConnectors(0), R2.getConnectors(1), L1.getConnectors(0), gnd.getConnectors(0) });
	Connection c3 = new Connection(new Connector[] { R3.getConnectors(1), R2.getConnectors(0), R1.getConnectors(0) });

	oOModel.addModelComponent(vs);
	oOModel.addModelComponent(R1);
	oOModel.addModelComponent(R2);
	oOModel.addModelComponent(R3);
	oOModel.addModelComponent(L1);
	oOModel.addModelComponent(gnd);
	oOModel.addConnection(c1);
	oOModel.addConnection(c2);
	oOModel.addConnection(c3);
	
	return oOModel;
}


/**
 * Insert the method's description here.
 * Creation date: (1/16/2006 11:34:20 PM)
 * @return ncbc.physics2.component.Model
 */
public static OOModel getExampleTriangle_a_b() throws ParseException {

	//
	//
	//                     
	//                               |
	//                              b|
	//                               |
	//                      a   ----------   area
	//                  -------| triangle |-------
	//                         |__________|
	//                               |
	//                              h|
	//                               |
	//
	//
	OOModel oOModel = new OOModel();

	ModelComponent triangle = new ModelComponent("triangle");
	Variable tri_a = new Variable("a",VCUnitDefinition.getInstance("m"));
	Variable tri_b = new Variable("b",VCUnitDefinition.getInstance("m"));
	Variable tri_h = new Variable("h",VCUnitDefinition.getInstance("m"));
	Variable tri_area = new Variable("area",VCUnitDefinition.getInstance("m2"));
	triangle.addSymbol(tri_a);
	triangle.addSymbol(tri_b);
	triangle.addSymbol(tri_h);
	triangle.addSymbol(tri_area);
	triangle.addEquation(Expression.valueOf("a*a+b*b-h*h"));
	triangle.addEquation(Expression.valueOf("area - a*b/2"));
	triangle.setConnectors(new Connector[] { 
		new Connector(triangle,"pin_a",tri_a,null),
		new Connector(triangle,"pin_b",tri_b,null),
		new Connector(triangle,"pin_h",tri_h,null),
		new Connector(triangle,"pin_area",tri_area,null) } );

	PointSource source_a = new PointSource("source_a",3);
	PointSource source_b = new PointSource("source_b",4);
	
	Connection c1 = new Connection(new Connector[] { source_a.getConnectors(0), triangle.getConnectors(0) });
	Connection c2 = new Connection(new Connector[] { source_b.getConnectors(0), triangle.getConnectors(1) });

	oOModel.addModelComponent(triangle);
	oOModel.addModelComponent(source_a);
	oOModel.addModelComponent(source_b);
	oOModel.addConnection(c1);
	oOModel.addConnection(c2);
	
	return oOModel;
}


/**
 * Insert the method's description here.
 * Creation date: (1/16/2006 11:34:20 PM)
 * @return ncbc.physics2.component.Model
 */
public static OOModel getExampleTriangle_h_a() throws ParseException {

	//
	//
	//                     
	//                               |
	//                              b|
	//                               |
	//                      a   ----------   area
	//                  -------| triangle |-------
	//                         |__________|
	//                               |
	//                              h|
	//                               |
	//
	//
	OOModel oOModel = new OOModel();

	ModelComponent triangle = new ModelComponent("triangle");
	Variable tri_a = new Variable("a",VCUnitDefinition.getInstance("m"));
	Variable tri_b = new Variable("b",VCUnitDefinition.getInstance("m"));
	Variable tri_h = new Variable("h",VCUnitDefinition.getInstance("m"));
	Variable tri_area = new Variable("area",VCUnitDefinition.getInstance("m2"));
	triangle.addSymbol(tri_a);
	triangle.addSymbol(tri_b);
	triangle.addSymbol(tri_h);
	triangle.addSymbol(tri_area);
	triangle.addEquation(Expression.valueOf("a*a+b*b-h*h"));
	triangle.addEquation(Expression.valueOf("area - a*b/2"));
	triangle.setConnectors(new Connector[] { 
		new Connector(triangle,"pin_a",tri_a,null),
		new Connector(triangle,"pin_b",tri_b,null),
		new Connector(triangle,"pin_h",tri_h,null),
		new Connector(triangle,"pin_area",tri_area,null) } );

	PointSource source_h = new PointSource("source_h",5);
	PointSource source_area = new PointSource("source_area",6);
	
	Connection c1 = new Connection(new Connector[] { source_h.getConnectors(0), triangle.getConnectors(2) });
	Connection c2 = new Connection(new Connector[] { source_area.getConnectors(0), triangle.getConnectors(3) });

	oOModel.addModelComponent(triangle);
	oOModel.addModelComponent(source_h);
	oOModel.addModelComponent(source_area);
	oOModel.addConnection(c1);
	oOModel.addConnection(c2);
	
	return oOModel;
}

/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {
	try {
		OOModel ooModel = null;
		if (args.length>0){
			String xmlString = XmlUtil.getXMLString(args[0]);
			ModelReader modelReader = new ModelReader();
			Element root = XmlUtil.stringToXML(xmlString,null);
			ooModel = modelReader.getOOModel(root);
		}else{
			//ooModel = ModelReader.parse(null);
			//ooModel = getExampleTriangle_h_a();
			ooModel = getExample3();
		}
		ModelWriter modelWriter = new ModelWriter();
		System.out.println(XmlUtil.xmlToString(modelWriter.getXML(ooModel)));

	} catch (Throwable e) {
		e.printStackTrace(System.out);
	}
}
}