//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.22 at 03:44:23 AM PST 
//


package org.xml.fixml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CompIDReqGrp_Block_t complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CompIDReqGrp_Block_t">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}CompIDReqGrpElements"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}CompIDReqGrpAttributes"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompIDReqGrp_Block_t")
public class CompIDReqGrpBlockT {

    @XmlAttribute(name = "RefCompID")
    protected String refCompID;
    @XmlAttribute(name = "RefSubID")
    protected String refSubID;
    @XmlAttribute(name = "LctnID")
    protected String lctnID;
    @XmlAttribute(name = "DeskID")
    protected String deskID;

    /**
     * Gets the value of the refCompID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefCompID() {
        return refCompID;
    }

    /**
     * Sets the value of the refCompID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefCompID(String value) {
        this.refCompID = value;
    }

    /**
     * Gets the value of the refSubID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefSubID() {
        return refSubID;
    }

    /**
     * Sets the value of the refSubID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefSubID(String value) {
        this.refSubID = value;
    }

    /**
     * Gets the value of the lctnID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLctnID() {
        return lctnID;
    }

    /**
     * Sets the value of the lctnID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLctnID(String value) {
        this.lctnID = value;
    }

    /**
     * Gets the value of the deskID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeskID() {
        return deskID;
    }

    /**
     * Sets the value of the deskID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeskID(String value) {
        this.deskID = value;
    }

}