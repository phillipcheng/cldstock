//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.22 at 03:44:23 AM PST 
//


package org.cldutil.xml.fixml;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApplicationSequenceControl_Block_t complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicationSequenceControl_Block_t">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}ApplicationSequenceControlElements"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}ApplicationSequenceControlAttributes"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationSequenceControl_Block_t")
public class ApplicationSequenceControlBlockT {

    @XmlAttribute(name = "ApplID")
    protected String applID;
    @XmlAttribute(name = "ApplSeqNum")
    protected BigInteger applSeqNum;
    @XmlAttribute(name = "ApplLastSeqNum")
    protected BigInteger applLastSeqNum;
    @XmlAttribute(name = "ApplResendFlag")
    protected String applResendFlag;

    /**
     * Gets the value of the applID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplID() {
        return applID;
    }

    /**
     * Sets the value of the applID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplID(String value) {
        this.applID = value;
    }

    /**
     * Gets the value of the applSeqNum property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getApplSeqNum() {
        return applSeqNum;
    }

    /**
     * Sets the value of the applSeqNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setApplSeqNum(BigInteger value) {
        this.applSeqNum = value;
    }

    /**
     * Gets the value of the applLastSeqNum property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getApplLastSeqNum() {
        return applLastSeqNum;
    }

    /**
     * Sets the value of the applLastSeqNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setApplLastSeqNum(BigInteger value) {
        this.applLastSeqNum = value;
    }

    /**
     * Gets the value of the applResendFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplResendFlag() {
        return applResendFlag;
    }

    /**
     * Sets the value of the applResendFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplResendFlag(String value) {
        this.applResendFlag = value;
    }

}
