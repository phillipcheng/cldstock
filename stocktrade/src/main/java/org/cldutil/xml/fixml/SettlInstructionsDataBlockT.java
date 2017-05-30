//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.22 at 03:44:23 AM PST 
//


package org.cldutil.xml.fixml;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SettlInstructionsData_Block_t complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SettlInstructionsData_Block_t">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}SettlInstructionsDataElements"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}SettlInstructionsDataAttributes"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SettlInstructionsData_Block_t", propOrder = {
    "dlvInst"
})
public class SettlInstructionsDataBlockT {

    @XmlElement(name = "DlvInst")
    protected List<DlvyInstGrpBlockT> dlvInst;
    @XmlAttribute(name = "DlvryTyp")
    protected BigInteger dlvryTyp;
    @XmlAttribute(name = "StandInstDbTyp")
    protected BigInteger standInstDbTyp;
    @XmlAttribute(name = "StandInstDbName")
    protected String standInstDbName;
    @XmlAttribute(name = "StandInstDbID")
    protected String standInstDbID;

    /**
     * Gets the value of the dlvInst property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dlvInst property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDlvInst().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DlvyInstGrpBlockT }
     * 
     * 
     */
    public List<DlvyInstGrpBlockT> getDlvInst() {
        if (dlvInst == null) {
            dlvInst = new ArrayList<DlvyInstGrpBlockT>();
        }
        return this.dlvInst;
    }

    /**
     * Gets the value of the dlvryTyp property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDlvryTyp() {
        return dlvryTyp;
    }

    /**
     * Sets the value of the dlvryTyp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDlvryTyp(BigInteger value) {
        this.dlvryTyp = value;
    }

    /**
     * Gets the value of the standInstDbTyp property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStandInstDbTyp() {
        return standInstDbTyp;
    }

    /**
     * Sets the value of the standInstDbTyp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStandInstDbTyp(BigInteger value) {
        this.standInstDbTyp = value;
    }

    /**
     * Gets the value of the standInstDbName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandInstDbName() {
        return standInstDbName;
    }

    /**
     * Sets the value of the standInstDbName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandInstDbName(String value) {
        this.standInstDbName = value;
    }

    /**
     * Gets the value of the standInstDbID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandInstDbID() {
        return standInstDbID;
    }

    /**
     * Sets the value of the standInstDbID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandInstDbID(String value) {
        this.standInstDbID = value;
    }

}