//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.09 at 03:55:47 PM EST 
//


package neuroml.channel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A synaptic mechanism whose conductance can be blocked by the presence of a specific species (ion/molecule). Based on the
 *             mechanism for blocking of an NMDA receptor by Mg as outlined in Gabbiani et al, 1994, Maex DeSchutter 1998
 * 
 * <p>Java class for BlockingSynapse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BlockingSynapse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://morphml.org/channelml/schema}DoubleExponentialSynapse">
 *       &lt;sequence>
 *         &lt;element name="block" type="{http://morphml.org/channelml/schema}Block"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BlockingSynapse", propOrder = {
    "block"
})
public class BlockingSynapse
    extends DoubleExponentialSynapse
{

    @XmlElement(required = true)
    protected Block block;

    /**
     * Gets the value of the block property.
     * 
     * @return
     *     possible object is
     *     {@link Block }
     *     
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Sets the value of the block property.
     * 
     * @param value
     *     allowed object is
     *     {@link Block }
     *     
     */
    public void setBlock(Block value) {
        this.block = value;
    }

}
