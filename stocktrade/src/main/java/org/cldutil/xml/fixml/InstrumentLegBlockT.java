//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.22 at 03:44:23 AM PST 
//


package org.cldutil.xml.fixml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for InstrumentLeg_Block_t complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InstrumentLeg_Block_t">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}InstrumentLegElements"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}InstrumentLegAttributes"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstrumentLeg_Block_t", propOrder = {
    "legAID"
})
@XmlSeeAlso({
    InstrmtLegGrpBlockT.class
})
public class InstrumentLegBlockT {

    @XmlElement(name = "LegAID")
    protected List<LegSecAltIDGrpBlockT> legAID;
    @XmlAttribute(name = "Sym")
    protected String sym;
    @XmlAttribute(name = "Sfx")
    protected SymbolSfxEnumT sfx;
    @XmlAttribute(name = "ID")
    protected String id;
    @XmlAttribute(name = "Src")
    protected String src;
    @XmlAttribute(name = "Prod")
    protected BigInteger prod;
    @XmlAttribute(name = "CFI")
    protected String cfi;
    @XmlAttribute(name = "SecTyp")
    protected String secTyp;
    @XmlAttribute(name = "SecSubTyp")
    protected String secSubTyp;
    @XmlAttribute(name = "MMY")
    protected String mmy;
    @XmlAttribute(name = "Mat")
    protected XMLGregorianCalendar mat;
    @XmlAttribute(name = "MatTm")
    protected XMLGregorianCalendar matTm;
    @XmlAttribute(name = "CpnPmt")
    protected XMLGregorianCalendar cpnPmt;
    @XmlAttribute(name = "Issued")
    protected XMLGregorianCalendar issued;
    @XmlAttribute(name = "RepoCollSecTyp")
    protected String repoCollSecTyp;
    @XmlAttribute(name = "RepoTrm")
    protected BigInteger repoTrm;
    @XmlAttribute(name = "RepoRt")
    protected BigDecimal repoRt;
    @XmlAttribute(name = "Fctr")
    protected BigDecimal fctr;
    @XmlAttribute(name = "CrdRtg")
    protected String crdRtg;
    @XmlAttribute(name = "Rgstry")
    protected String rgstry;
    @XmlAttribute(name = "Ctry")
    protected String ctry;
    @XmlAttribute(name = "StOrProvnc")
    protected String stOrProvnc;
    @XmlAttribute(name = "Lcl")
    protected String lcl;
    @XmlAttribute(name = "Redeem")
    protected XMLGregorianCalendar redeem;
    @XmlAttribute(name = "Strk")
    protected BigDecimal strk;
    @XmlAttribute(name = "StrkCcy")
    protected String strkCcy;
    @XmlAttribute(name = "OptA")
    protected String optA;
    @XmlAttribute(name = "Cmult")
    protected BigDecimal cmult;
    @XmlAttribute(name = "MultTyp")
    protected BigInteger multTyp;
    @XmlAttribute(name = "FlowSchedTyp")
    protected String flowSchedTyp;
    @XmlAttribute(name = "UOM")
    protected UnitOfMeasureEnumT uom;
    @XmlAttribute(name = "UOMQty")
    protected BigDecimal uomQty;
    @XmlAttribute(name = "PxUOM")
    protected UnitOfMeasureEnumT pxUOM;
    @XmlAttribute(name = "PxUOMQty")
    protected BigDecimal pxUOMQty;
    @XmlAttribute(name = "TmUnit")
    protected TimeUnitEnumT tmUnit;
    @XmlAttribute(name = "ExerStyle")
    protected BigInteger exerStyle;
    @XmlAttribute(name = "CpnRt")
    protected BigDecimal cpnRt;
    @XmlAttribute(name = "Exch")
    protected String exch;
    @XmlAttribute(name = "Issr")
    protected String issr;
    @XmlAttribute(name = "EncLegIssrLen")
    protected BigInteger encLegIssrLen;
    @XmlAttribute(name = "EncLegIssr")
    protected String encLegIssr;
    @XmlAttribute(name = "Desc")
    protected String desc;
    @XmlAttribute(name = "EncLegSecDescLen")
    protected BigInteger encLegSecDescLen;
    @XmlAttribute(name = "EncLegSecDesc")
    protected String encLegSecDesc;
    @XmlAttribute(name = "RatioQty")
    protected BigDecimal ratioQty;
    @XmlAttribute(name = "Side")
    protected String side;
    @XmlAttribute(name = "Ccy")
    protected String ccy;
    @XmlAttribute(name = "Pool")
    protected String pool;
    @XmlAttribute(name = "Dated")
    protected XMLGregorianCalendar dated;
    @XmlAttribute(name = "CSetMo")
    protected String cSetMo;
    @XmlAttribute(name = "IntAcrl")
    protected XMLGregorianCalendar intAcrl;
    @XmlAttribute(name = "PutCall")
    protected BigInteger putCall;
    @XmlAttribute(name = "LegOptionRatio")
    protected BigDecimal legOptionRatio;
    @XmlAttribute(name = "Px")
    protected BigDecimal px;

    /**
     * Gets the value of the legAID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the legAID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLegAID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LegSecAltIDGrpBlockT }
     * 
     * 
     */
    public List<LegSecAltIDGrpBlockT> getLegAID() {
        if (legAID == null) {
            legAID = new ArrayList<LegSecAltIDGrpBlockT>();
        }
        return this.legAID;
    }

    /**
     * Gets the value of the sym property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSym() {
        return sym;
    }

    /**
     * Sets the value of the sym property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSym(String value) {
        this.sym = value;
    }

    /**
     * Gets the value of the sfx property.
     * 
     * @return
     *     possible object is
     *     {@link SymbolSfxEnumT }
     *     
     */
    public SymbolSfxEnumT getSfx() {
        return sfx;
    }

    /**
     * Sets the value of the sfx property.
     * 
     * @param value
     *     allowed object is
     *     {@link SymbolSfxEnumT }
     *     
     */
    public void setSfx(SymbolSfxEnumT value) {
        this.sfx = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the src property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrc() {
        return src;
    }

    /**
     * Sets the value of the src property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrc(String value) {
        this.src = value;
    }

    /**
     * Gets the value of the prod property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getProd() {
        return prod;
    }

    /**
     * Sets the value of the prod property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setProd(BigInteger value) {
        this.prod = value;
    }

    /**
     * Gets the value of the cfi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCFI() {
        return cfi;
    }

    /**
     * Sets the value of the cfi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCFI(String value) {
        this.cfi = value;
    }

    /**
     * Gets the value of the secTyp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecTyp() {
        return secTyp;
    }

    /**
     * Sets the value of the secTyp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecTyp(String value) {
        this.secTyp = value;
    }

    /**
     * Gets the value of the secSubTyp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecSubTyp() {
        return secSubTyp;
    }

    /**
     * Sets the value of the secSubTyp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecSubTyp(String value) {
        this.secSubTyp = value;
    }

    /**
     * Gets the value of the mmy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMMY() {
        return mmy;
    }

    /**
     * Sets the value of the mmy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMMY(String value) {
        this.mmy = value;
    }

    /**
     * Gets the value of the mat property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMat() {
        return mat;
    }

    /**
     * Sets the value of the mat property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMat(XMLGregorianCalendar value) {
        this.mat = value;
    }

    /**
     * Gets the value of the matTm property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMatTm() {
        return matTm;
    }

    /**
     * Sets the value of the matTm property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMatTm(XMLGregorianCalendar value) {
        this.matTm = value;
    }

    /**
     * Gets the value of the cpnPmt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCpnPmt() {
        return cpnPmt;
    }

    /**
     * Sets the value of the cpnPmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCpnPmt(XMLGregorianCalendar value) {
        this.cpnPmt = value;
    }

    /**
     * Gets the value of the issued property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIssued() {
        return issued;
    }

    /**
     * Sets the value of the issued property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIssued(XMLGregorianCalendar value) {
        this.issued = value;
    }

    /**
     * Gets the value of the repoCollSecTyp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepoCollSecTyp() {
        return repoCollSecTyp;
    }

    /**
     * Sets the value of the repoCollSecTyp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepoCollSecTyp(String value) {
        this.repoCollSecTyp = value;
    }

    /**
     * Gets the value of the repoTrm property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRepoTrm() {
        return repoTrm;
    }

    /**
     * Sets the value of the repoTrm property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRepoTrm(BigInteger value) {
        this.repoTrm = value;
    }

    /**
     * Gets the value of the repoRt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRepoRt() {
        return repoRt;
    }

    /**
     * Sets the value of the repoRt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRepoRt(BigDecimal value) {
        this.repoRt = value;
    }

    /**
     * Gets the value of the fctr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFctr() {
        return fctr;
    }

    /**
     * Sets the value of the fctr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFctr(BigDecimal value) {
        this.fctr = value;
    }

    /**
     * Gets the value of the crdRtg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrdRtg() {
        return crdRtg;
    }

    /**
     * Sets the value of the crdRtg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrdRtg(String value) {
        this.crdRtg = value;
    }

    /**
     * Gets the value of the rgstry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRgstry() {
        return rgstry;
    }

    /**
     * Sets the value of the rgstry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRgstry(String value) {
        this.rgstry = value;
    }

    /**
     * Gets the value of the ctry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCtry() {
        return ctry;
    }

    /**
     * Sets the value of the ctry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCtry(String value) {
        this.ctry = value;
    }

    /**
     * Gets the value of the stOrProvnc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStOrProvnc() {
        return stOrProvnc;
    }

    /**
     * Sets the value of the stOrProvnc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStOrProvnc(String value) {
        this.stOrProvnc = value;
    }

    /**
     * Gets the value of the lcl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLcl() {
        return lcl;
    }

    /**
     * Sets the value of the lcl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLcl(String value) {
        this.lcl = value;
    }

    /**
     * Gets the value of the redeem property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRedeem() {
        return redeem;
    }

    /**
     * Sets the value of the redeem property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRedeem(XMLGregorianCalendar value) {
        this.redeem = value;
    }

    /**
     * Gets the value of the strk property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStrk() {
        return strk;
    }

    /**
     * Sets the value of the strk property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStrk(BigDecimal value) {
        this.strk = value;
    }

    /**
     * Gets the value of the strkCcy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrkCcy() {
        return strkCcy;
    }

    /**
     * Sets the value of the strkCcy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrkCcy(String value) {
        this.strkCcy = value;
    }

    /**
     * Gets the value of the optA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOptA() {
        return optA;
    }

    /**
     * Sets the value of the optA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOptA(String value) {
        this.optA = value;
    }

    /**
     * Gets the value of the cmult property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCmult() {
        return cmult;
    }

    /**
     * Sets the value of the cmult property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCmult(BigDecimal value) {
        this.cmult = value;
    }

    /**
     * Gets the value of the multTyp property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMultTyp() {
        return multTyp;
    }

    /**
     * Sets the value of the multTyp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMultTyp(BigInteger value) {
        this.multTyp = value;
    }

    /**
     * Gets the value of the flowSchedTyp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlowSchedTyp() {
        return flowSchedTyp;
    }

    /**
     * Sets the value of the flowSchedTyp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlowSchedTyp(String value) {
        this.flowSchedTyp = value;
    }

    /**
     * Gets the value of the uom property.
     * 
     * @return
     *     possible object is
     *     {@link UnitOfMeasureEnumT }
     *     
     */
    public UnitOfMeasureEnumT getUOM() {
        return uom;
    }

    /**
     * Sets the value of the uom property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnitOfMeasureEnumT }
     *     
     */
    public void setUOM(UnitOfMeasureEnumT value) {
        this.uom = value;
    }

    /**
     * Gets the value of the uomQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUOMQty() {
        return uomQty;
    }

    /**
     * Sets the value of the uomQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUOMQty(BigDecimal value) {
        this.uomQty = value;
    }

    /**
     * Gets the value of the pxUOM property.
     * 
     * @return
     *     possible object is
     *     {@link UnitOfMeasureEnumT }
     *     
     */
    public UnitOfMeasureEnumT getPxUOM() {
        return pxUOM;
    }

    /**
     * Sets the value of the pxUOM property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnitOfMeasureEnumT }
     *     
     */
    public void setPxUOM(UnitOfMeasureEnumT value) {
        this.pxUOM = value;
    }

    /**
     * Gets the value of the pxUOMQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPxUOMQty() {
        return pxUOMQty;
    }

    /**
     * Sets the value of the pxUOMQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPxUOMQty(BigDecimal value) {
        this.pxUOMQty = value;
    }

    /**
     * Gets the value of the tmUnit property.
     * 
     * @return
     *     possible object is
     *     {@link TimeUnitEnumT }
     *     
     */
    public TimeUnitEnumT getTmUnit() {
        return tmUnit;
    }

    /**
     * Sets the value of the tmUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeUnitEnumT }
     *     
     */
    public void setTmUnit(TimeUnitEnumT value) {
        this.tmUnit = value;
    }

    /**
     * Gets the value of the exerStyle property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getExerStyle() {
        return exerStyle;
    }

    /**
     * Sets the value of the exerStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setExerStyle(BigInteger value) {
        this.exerStyle = value;
    }

    /**
     * Gets the value of the cpnRt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCpnRt() {
        return cpnRt;
    }

    /**
     * Sets the value of the cpnRt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCpnRt(BigDecimal value) {
        this.cpnRt = value;
    }

    /**
     * Gets the value of the exch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExch() {
        return exch;
    }

    /**
     * Sets the value of the exch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExch(String value) {
        this.exch = value;
    }

    /**
     * Gets the value of the issr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssr() {
        return issr;
    }

    /**
     * Sets the value of the issr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssr(String value) {
        this.issr = value;
    }

    /**
     * Gets the value of the encLegIssrLen property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEncLegIssrLen() {
        return encLegIssrLen;
    }

    /**
     * Sets the value of the encLegIssrLen property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEncLegIssrLen(BigInteger value) {
        this.encLegIssrLen = value;
    }

    /**
     * Gets the value of the encLegIssr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncLegIssr() {
        return encLegIssr;
    }

    /**
     * Sets the value of the encLegIssr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncLegIssr(String value) {
        this.encLegIssr = value;
    }

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

    /**
     * Gets the value of the encLegSecDescLen property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEncLegSecDescLen() {
        return encLegSecDescLen;
    }

    /**
     * Sets the value of the encLegSecDescLen property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEncLegSecDescLen(BigInteger value) {
        this.encLegSecDescLen = value;
    }

    /**
     * Gets the value of the encLegSecDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncLegSecDesc() {
        return encLegSecDesc;
    }

    /**
     * Sets the value of the encLegSecDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncLegSecDesc(String value) {
        this.encLegSecDesc = value;
    }

    /**
     * Gets the value of the ratioQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRatioQty() {
        return ratioQty;
    }

    /**
     * Sets the value of the ratioQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRatioQty(BigDecimal value) {
        this.ratioQty = value;
    }

    /**
     * Gets the value of the side property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSide() {
        return side;
    }

    /**
     * Sets the value of the side property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSide(String value) {
        this.side = value;
    }

    /**
     * Gets the value of the ccy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcy() {
        return ccy;
    }

    /**
     * Sets the value of the ccy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcy(String value) {
        this.ccy = value;
    }

    /**
     * Gets the value of the pool property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPool() {
        return pool;
    }

    /**
     * Sets the value of the pool property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPool(String value) {
        this.pool = value;
    }

    /**
     * Gets the value of the dated property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDated() {
        return dated;
    }

    /**
     * Sets the value of the dated property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDated(XMLGregorianCalendar value) {
        this.dated = value;
    }

    /**
     * Gets the value of the cSetMo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSetMo() {
        return cSetMo;
    }

    /**
     * Sets the value of the cSetMo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSetMo(String value) {
        this.cSetMo = value;
    }

    /**
     * Gets the value of the intAcrl property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIntAcrl() {
        return intAcrl;
    }

    /**
     * Sets the value of the intAcrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIntAcrl(XMLGregorianCalendar value) {
        this.intAcrl = value;
    }

    /**
     * Gets the value of the putCall property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPutCall() {
        return putCall;
    }

    /**
     * Sets the value of the putCall property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPutCall(BigInteger value) {
        this.putCall = value;
    }

    /**
     * Gets the value of the legOptionRatio property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLegOptionRatio() {
        return legOptionRatio;
    }

    /**
     * Sets the value of the legOptionRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLegOptionRatio(BigDecimal value) {
        this.legOptionRatio = value;
    }

    /**
     * Gets the value of the px property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPx() {
        return px;
    }

    /**
     * Sets the value of the px property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPx(BigDecimal value) {
        this.px = value;
    }

}
