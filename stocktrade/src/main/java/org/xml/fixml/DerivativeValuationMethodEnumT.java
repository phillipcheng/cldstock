//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.22 at 03:44:23 AM PST 
//


package org.xml.fixml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DerivativeValuationMethod_enum_t.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DerivativeValuationMethod_enum_t">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EQTY"/>
 *     &lt;enumeration value="FUT"/>
 *     &lt;enumeration value="FUTDA"/>
 *     &lt;enumeration value="CDS"/>
 *     &lt;enumeration value="CDSD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DerivativeValuationMethod_enum_t")
@XmlEnum
public enum DerivativeValuationMethodEnumT {

    EQTY,
    FUT,
    FUTDA,
    CDS,
    CDSD;

    public String value() {
        return name();
    }

    public static DerivativeValuationMethodEnumT fromValue(String v) {
        return valueOf(v);
    }

}
