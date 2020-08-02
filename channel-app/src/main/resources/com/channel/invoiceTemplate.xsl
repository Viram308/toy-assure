<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <!-- Attribute used for table border -->
    <xsl:attribute-set name="tableBorder">
        <xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
    </xsl:attribute-set>
    <xsl:template match="invoice">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simple"
                                       page-height="8.5in" page-width="11in" margin-top=".5in"
                                       margin-bottom=".5in" margin-left=".5in" margin-right=".5in">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simple"
                              initial-page-number="1">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="10pt" text-align="left"  space-after="2mm">
                        Order ID:
                        <xsl:value-of select="orderId"/>
                    </fo:block>
                    <fo:block font-size="10pt" text-align="left"  space-after="2mm">
                        Client :
                        <xsl:value-of select="clientName"/>
                    </fo:block>
                    <fo:block font-size="10pt" text-align="left"  space-after="2mm">
                        Customer :
                        <xsl:value-of select="customerName"/>
                    </fo:block>
                    <fo:block font-size="10pt" text-align="left"  space-after="2mm">
                        Channel :
                        <xsl:value-of select="channelName"/>
                    </fo:block>
                    <fo:block font-size="10pt" text-align="left"  space-after="2mm">
                        Invoice Type :
                        <xsl:value-of select="invoiceType"/>
                    </fo:block>
                    <fo:block font-size="10pt" text-align="left"  space-after="2mm">
                        Date :
                        <xsl:value-of select="date"/>
                    </fo:block>
                    <fo:block font-size="10pt" text-align="left"  space-after="2mm">
                        Time :
                        <xsl:value-of select="time"/>
                    </fo:block>


                    <fo:block font-size="10pt">
                        <fo:table table-layout="fixed" width="100%" border-collapse="separate">
                            <fo:table-column column-width="2cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="3cm"/>
                            <fo:table-column column-width="3cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-header font-weight="bold">
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">#</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Product</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Brand</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">Quantity
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">MRP</fo:block>
                                </fo:table-cell>
                            </fo:table-header>
                            <fo:table-body>
                                <xsl:apply-templates
                                        select="item"/> <!-- branch tag is taken and pasted in below used template 46 line-->
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <fo:block text-align="right" font-size="15pt"  color="black"
                              font-weight="bold" space-after="5mm">
                        Total Amount :
                        <xsl:value-of select="totalAmount"/> /-
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="item">
        <fo:table-row space-after="5mm">
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="id"/>
                </fo:block>
            </fo:table-cell>

            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="productName"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="brandId"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="orderedQuantity"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt">
                    <xsl:value-of select="sellingPricePerUnit"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
</xsl:stylesheet>