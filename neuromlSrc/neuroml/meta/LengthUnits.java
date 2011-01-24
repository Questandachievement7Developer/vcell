//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.09 at 03:55:47 PM EST 
//


package neuroml.meta;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;


/**
 * <p>Java class for LengthUnits.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LengthUnits">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="micron"/>
 *     &lt;enumeration value="millimetre"/>
 *     &lt;enumeration value="metre"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlEnum
public enum LengthUnits {

    @XmlEnumValue("micron")
    MICRON("micron"),
    @XmlEnumValue("millimetre")
    MILLIMETRE("millimetre"),
    @XmlEnumValue("metre")
    METRE("metre");
    private final String value;

    LengthUnits(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LengthUnits fromValue(String v) {
        for (LengthUnits c: LengthUnits.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
