//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.09 at 03:55:47 PM EST 
//


package neuroml.network;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import neuroml.meta.Annotation;
import neuroml.meta.Properties;


/**
 * Subset of sections on cell where synaptic connection of a particular type is allowed
 * 
 * <p>Java class for PotentialSynapticLocation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PotentialSynapticLocation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://morphml.org/metadata/schema}metadata"/>
 *         &lt;sequence>
 *           &lt;element name="synapse_type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="synapse_direction" type="{http://morphml.org/networkml/schema}SynapseDirection" minOccurs="0"/>
 *           &lt;element name="group" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PotentialSynapticLocation", propOrder = {
    "content"
})
public class PotentialSynapticLocation {

    @XmlElementRefs({
        @XmlElementRef(name = "annotation", namespace = "http://morphml.org/metadata/schema", type = JAXBElement.class),
        @XmlElementRef(name = "synapse_type", namespace = "http://morphml.org/networkml/schema", type = JAXBElement.class),
        @XmlElementRef(name = "notes", namespace = "http://morphml.org/metadata/schema", type = JAXBElement.class),
        @XmlElementRef(name = "properties", namespace = "http://morphml.org/metadata/schema", type = JAXBElement.class),
        @XmlElementRef(name = "group", namespace = "http://morphml.org/metadata/schema", type = JAXBElement.class),
        @XmlElementRef(name = "synapse_direction", namespace = "http://morphml.org/networkml/schema", type = JAXBElement.class),
        @XmlElementRef(name = "group", namespace = "http://morphml.org/networkml/schema", type = PotentialSynapticLocation.AllowedGroups.class)
    })
    protected List<JAXBElement<?>> content;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "Group" is used by two different parts of a schema. See: 
     * line 474 of file:/C:/Developer/eclipse/workspace/neuroml-api/conf/Level3/NetworkML_v1.4.xsd
     * line 243 of file:/C:/Developer/eclipse/workspace/neuroml-api/conf/Level1/Metadata_v1.4.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Annotation }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link Properties }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link SynapseDirection }{@code >}
     * {@link PotentialSynapticLocation.AllowedGroups }
     * 
     * 
     */
    public List<JAXBElement<?>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<?>>();
        }
        return this.content;
    }

    public static class AllowedGroups
        extends JAXBElement<String>
    {

        protected final static QName NAME = new QName("http://morphml.org/networkml/schema", "group");

        public AllowedGroups(String value) {
            super(NAME, ((Class) String.class), PotentialSynapticLocation.class, value);
        }

    }

}
