//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.22 at 03:44:23 AM PST 
//


package org.xml.fixml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Abstract_message_t complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Abstract_message_t">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Hdr" type="{http://www.fixprotocol.org/FIXML-5-0-SP2}MessageHeader_t" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Abstract_message_t", propOrder = {
    "hdr"
})
@XmlSeeAlso({
    NewOrderSingleMessageT.class,
    OrderMassCancelReportMessageT.class,
    MultilegOrderCancelReplaceMessageT.class,
    OrderMassStatusRequestMessageT.class,
    OrderCancelReplaceRequestMessageT.class,
    OrderMassActionRequestMessageT.class,
    NewOrderMultilegMessageT.class,
    OrderMassActionReportMessageT.class,
    NewOrderListMessageT.class,
    OrderCancelRejectMessageT.class,
    CrossOrderCancelReplaceRequestMessageT.class,
    OrderStatusRequestMessageT.class,
    ExecutionReportMessageT.class,
    OrderCancelRequestMessageT.class,
    NewOrderCrossMessageT.class,
    DontKnowTradeMessageT.class,
    BidResponseMessageT.class,
    ListStatusMessageT.class,
    OrderMassCancelRequestMessageT.class,
    ListExecuteMessageT.class,
    ListStrikePriceMessageT.class,
    BidRequestMessageT.class,
    CrossOrderCancelRequestMessageT.class,
    ListCancelRequestMessageT.class,
    ExecutionAcknowledgementMessageT.class,
    ListStatusRequestMessageT.class
})
public class AbstractMessageT {

    @XmlElement(name = "Hdr")
    protected MessageHeaderT hdr;

    /**
     * Gets the value of the hdr property.
     * 
     * @return
     *     possible object is
     *     {@link MessageHeaderT }
     *     
     */
    public MessageHeaderT getHdr() {
        return hdr;
    }

    /**
     * Sets the value of the hdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageHeaderT }
     *     
     */
    public void setHdr(MessageHeaderT value) {
        this.hdr = value;
    }

}