//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.22 at 03:44:23 AM PST 
//


package org.xml.fixml;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderQtyData_Block_t complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderQtyData_Block_t">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}OrderQtyDataElements"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}OrderQtyDataAttributes"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderQtyData_Block_t")
public class OrderQtyDataBlockT {

    @XmlAttribute(name = "Qty")
    protected BigDecimal qty;
    @XmlAttribute(name = "Cash")
    protected BigDecimal cash;
    @XmlAttribute(name = "Pct")
    protected BigDecimal pct;
    @XmlAttribute(name = "RndDir")
    protected String rndDir;
    @XmlAttribute(name = "RndMod")
    protected BigDecimal rndMod;

    /**
     * Gets the value of the qty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQty() {
        return qty;
    }

    /**
     * Sets the value of the qty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQty(BigDecimal value) {
        this.qty = value;
    }

    /**
     * Gets the value of the cash property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCash() {
        return cash;
    }

    /**
     * Sets the value of the cash property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCash(BigDecimal value) {
        this.cash = value;
    }

    /**
     * Gets the value of the pct property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPct() {
        return pct;
    }

    /**
     * Sets the value of the pct property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPct(BigDecimal value) {
        this.pct = value;
    }

    /**
     * Gets the value of the rndDir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRndDir() {
        return rndDir;
    }

    /**
     * Sets the value of the rndDir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRndDir(String value) {
        this.rndDir = value;
    }

    /**
     * Gets the value of the rndMod property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRndMod() {
        return rndMod;
    }

    /**
     * Sets the value of the rndMod property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRndMod(BigDecimal value) {
        this.rndMod = value;
    }

}
