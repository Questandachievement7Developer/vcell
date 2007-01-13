package cbit.vcell.vcml.compare;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Hashtable;

import org.jdom.Element;

import cbit.util.Matchable;
import cbit.util.xml.XmlParseException;
import cbit.util.xml.XmlUtil;
import cbit.vcell.biomodel.BioModel;
import cbit.vcell.mathmodel.MathModel;
import cbit.vcell.vcml.VCellXMLComparePolicy;
import cbit.vcell.xml.XMLTags;
import cbit.vcell.xml.Xmlproducer;
import cbit.vcell.xml.merge.XmlComparator;


/**
This utility class encapsulates the functionality of comparing VCML documents. Contrary to what the class name might indicate,
this class does not extend java.util.Comparator

 * Creation date: (9/21/2004 4:04:50 PM)
 * @author: Rashad Badrawi
 */
public class VCMLComparator {

	private static boolean VERBOSE_MODE = true;	                //for now...
	
	public static PrintStream ps;
	private static Hashtable<String,String> map;

	public static class VCMLElementSorter implements Comparator {
	
		public int compare(Object o1, Object o2) {

			int result;
			Element e1 = (Element)o1;
			Element e2 = (Element)o2;

			//sort by their element name
			String eName1 = e1.getName();
			String eName2 = e2.getName();
			result = eName1.compareTo(eName2);
			if (result != 0) {
				return result;
			}
			if (eName1.equals(XMLTags.CoordinateTag)) {                         //or eName2, no re-ordering for Coordinate elements. 
				return result; 
			}
			//if they belong to the same element, sort by their 'primary key' attribute.
			String pkName = (String)map.get(eName1);                       //or eName2
			if (pkName == null)
				pkName = XMLTags.NameAttrTag;
			int index = pkName.indexOf("&");
			if (index == -1) {
				if (pkName.equals("TEXT")) {
					result = e1.getTextTrim().compareTo(e2.getTextTrim());
				} else {
					result = e1.getAttributeValue(pkName).compareTo(e2.getAttributeValue(pkName));
				}
			} else {         
				java.util.StringTokenizer tokens = new java.util.StringTokenizer(pkName,"&");
    			while (tokens.hasMoreElements()){
     				String token = tokens.nextToken();
     				result = e1.getAttributeValue(token).compareTo(e2.getAttributeValue(token));
     				if (result != 0){
      					break;
     				}
    			}
			}

			return result;
		}	
	}

	//can also be loaded from a property file. Fills a hashtable of all the VCML elements whose 'primary key' is not 'Name'
	//but some other attribute.
	static { 
		map = new Hashtable<String,String>();
		map.put(XMLTags.ReactantTag, XMLTags.SpeciesContextRefAttrTag);
		map.put(XMLTags.ProductTag, XMLTags.SpeciesContextRefAttrTag);
		map.put(XMLTags.CatalystTag, XMLTags.SpeciesContextRefAttrTag);
		map.put(XMLTags.SpeciesContextShapeTag, XMLTags.SpeciesContextRefAttrTag);
		map.put(XMLTags.SimpleReactionShapeTag, XMLTags.SimpleReactionRefAttrTag);
		map.put(XMLTags.FluxReactionShapeTag, XMLTags.FluxReactionRefAttrTag);
		map.put(XMLTags.FeatureMappingTag, XMLTags.FeatureAttrTag);
		map.put(XMLTags.MembraneMappingTag, XMLTags.MembraneAttrTag);
		map.put(XMLTags.SpeciesContextSpecTag, XMLTags.SpeciesContextRefAttrTag);
		map.put(XMLTags.ReactionSpecTag, XMLTags.ReactionStepRefAttrTag);
		map.put(XMLTags.BoundaryTypeTag, XMLTags.BoundaryAttrTag);
//		map.put(ParameterEstimationTaskXMLPersistence.ParameterMappingSpecTag, ParameterEstimationTaskXMLPersistence.ParameterReferenceAttribute);
//		map.put(ParameterEstimationTaskXMLPersistence.ReferenceDataMappingSpecTag, ParameterEstimationTaskXMLPersistence.ReferenceDataColumnNameAttribute);
//		map.put(ParameterEstimationTaskXMLPersistence.DataRowTag, "TEXT");
		
		map.put(XMLTags.FastInvariantTag, "TEXT");
		map.put(XMLTags.FastRateTag, "TEXT");
		map.put(XMLTags.NameTag, "TEXT");
		//a hack, for compound 'primary key'
		map.put(XMLTags.MembraneSubDomainTag, XMLTags.InsideCompartmentTag + "&" + XMLTags.OutsideCompartmentTag);
		map.put(XMLTags.CoordinateTag, XMLTags.XAttrTag + "&" + XMLTags.YAttrTag + "&" + XMLTags.ZAttrTag);
		map.put(XMLTags.VelocityTag, XMLTags.XAttrTag + "&" + XMLTags.YAttrTag + "&" + XMLTags.ZAttrTag);
		map.put(XMLTags.SurfaceDescriptionTag, XMLTags.CutoffFrequencyAttrTag + "&" + XMLTags.NumSamplesXAttrTag +
			   "&" + XMLTags.NumSamplesYAttrTag + "&" + XMLTags.NumSamplesZAttrTag);     //?

		//for stochastic model , added 19th Sept, 2006
		map.put(XMLTags.ActionTag, XMLTags.VarNameAttrTag);
		map.put(XMLTags.ProbabilityRateTag, "TEXT");
		ps = System.out;
	}
	
	
	public static boolean compareEquals(String xmlStr1, String xmlStr2) throws XmlParseException {

		if (xmlStr1 == null || xmlStr1.length() == 0 ||
			xmlStr2 == null || xmlStr2.length() == 0) {
			throw new XmlParseException("Invalid values for the xml strings.");
		}
		XmlComparator xmlComparator = new XmlComparator(new VCellXMLComparePolicy(true));
		return xmlComparator.compareXML(xmlStr1, xmlStr2, false);
	}


	public static boolean compareMatchables(Matchable m1, Matchable m2, String type) {

		Element source = null, target = null; 
		try { 
			Xmlproducer producer = new Xmlproducer(true);
			if (type.equals(XMLTags.BioModelTag)) {
				source = producer.getXML((BioModel)m1);
				target = producer.getXML((BioModel)m2);
			} else if (type.equals(XMLTags.MathModelTag)) {
				source = producer.getXML((MathModel)m1);
				target = producer.getXML((MathModel)m2);
			} else {
				throw new IllegalArgumentException("Accepted matchable types are biomodel and mathmodel");
			}
			String sourceXMLStr = XmlUtil.xmlToString(source);
			String targetXMLStr = XmlUtil.xmlToString(target);
			
			XmlComparator xmlComparator = new XmlComparator(new VCellXMLComparePolicy(true));
			
			boolean result = xmlComparator.compareXML(sourceXMLStr, targetXMLStr, true);
			if (!result && VERBOSE_MODE) {
				ps.println(sourceXMLStr);
				ps.println(targetXMLStr);
			}
			return result;
		} catch (Exception e) {         					//ExpressionException, XmlParseException 
			e.printStackTrace();
			return false;
		}
	}
}