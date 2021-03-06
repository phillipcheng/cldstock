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
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for Batch_t complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Batch_t">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}BatchElements"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.fixprotocol.org/FIXML-5-0-SP2}BatchAttributes"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Batch_t", propOrder = {
    "hdr",
    "message"
})
public class BatchT {

    @XmlElement(name = "Hdr")
    protected BatchHeaderT hdr;
    @XmlElementRef(name = "Message", namespace = "http://www.fixprotocol.org/FIXML-5-0-SP2", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends AbstractMessageT>> message;
    @XmlAttribute(name = "ID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "TotMsg")
    protected BigInteger totMsg;

    /**
     * Gets the value of the hdr property.
     * 
     * @return
     *     possible object is
     *     {@link BatchHeaderT }
     *     
     */
    public BatchHeaderT getHdr() {
        return hdr;
    }

    /**
     * Sets the value of the hdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BatchHeaderT }
     *     
     */
    public void setHdr(BatchHeaderT value) {
        this.hdr = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the message property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link OrderMassStatusRequestMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link BidRequestMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link NewOrderMultilegMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link CrossOrderCancelReplaceRequestMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link BidResponseMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link ListStatusMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link OrderCancelRejectMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link ListCancelRequestMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link OrderCancelRequestMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link NewOrderSingleMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link NewOrderListMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link CrossOrderCancelRequestMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link ListExecuteMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link OrderMassActionReportMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link ExecutionReportMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link ListStrikePriceMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link MultilegOrderCancelReplaceMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link NewOrderCrossMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link ExecutionAcknowledgementMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link OrderMassActionRequestMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link DontKnowTradeMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link OrderMassCancelReportMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link ListStatusRequestMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link OrderMassCancelRequestMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link OrderCancelReplaceRequestMessageT }{@code >}
     * {@link JAXBElement }{@code <}{@link OrderStatusRequestMessageT }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractMessageT>> getMessage() {
        if (message == null) {
            message = new ArrayList<JAXBElement<? extends AbstractMessageT>>();
        }
        return this.message;
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
     * Gets the value of the totMsg property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotMsg() {
        return totMsg;
    }

    /**
     * Sets the value of the totMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotMsg(BigInteger value) {
        this.totMsg = value;
    }

}
