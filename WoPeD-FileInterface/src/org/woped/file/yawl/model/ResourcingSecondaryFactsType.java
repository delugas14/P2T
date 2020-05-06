//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.18 at 08:57:29 AM CEST 
//


package org.woped.file.yawl.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for ResourcingSecondaryFactsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourcingSecondaryFactsType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.yawlfoundation.org/yawlschema}ResourcingSetFactsType">
 *       &lt;sequence>
 *         &lt;element name="nonHumanResource" type="{http://www.yawlfoundation.org/yawlschema}NameType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nonHumanCategory" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.yawlfoundation.org/yawlschema>NameType">
 *                 &lt;attribute name="subcategory" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourcingSecondaryFactsType", propOrder = {
    "nonHumanResource",
    "nonHumanCategory"
})
public class ResourcingSecondaryFactsType
    extends ResourcingSetFactsType
{

    protected List<String> nonHumanResource;
    protected List<ResourcingSecondaryFactsType.NonHumanCategory> nonHumanCategory;

    /**
     * Gets the value of the nonHumanResource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nonHumanResource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNonHumanResource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNonHumanResource() {
        if (nonHumanResource == null) {
            nonHumanResource = new ArrayList<String>();
        }
        return this.nonHumanResource;
    }

    /**
     * Gets the value of the nonHumanCategory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nonHumanCategory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNonHumanCategory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourcingSecondaryFactsType.NonHumanCategory }
     * 
     * 
     */
    public List<ResourcingSecondaryFactsType.NonHumanCategory> getNonHumanCategory() {
        if (nonHumanCategory == null) {
            nonHumanCategory = new ArrayList<ResourcingSecondaryFactsType.NonHumanCategory>();
        }
        return this.nonHumanCategory;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.yawlfoundation.org/yawlschema>NameType">
     *       &lt;attribute name="subcategory" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class NonHumanCategory {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "subcategory")
        protected String subcategory;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the subcategory property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSubcategory() {
            return subcategory;
        }

        /**
         * Sets the value of the subcategory property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSubcategory(String value) {
            this.subcategory = value;
        }

    }

}
