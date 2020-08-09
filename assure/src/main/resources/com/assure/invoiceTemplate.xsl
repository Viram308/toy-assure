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
                    <fo:region-body margin-top="2cm" margin-bottom="2cm"/>
                    <fo:region-before extent="2cm" overflow="hidden"/>
                    <fo:region-after extent="1cm" overflow="hidden"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simple"
                              initial-page-number="1">
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block font-size="20.0pt" font-family="serif"
                              padding-after="3.0pt" space-before="5.0pt" text-align="center"
                              border-bottom-style="solid" border-bottom-width="1.0pt">
                        <xsl:text>Increff-OMS</xsl:text>
                    </fo:block>
                    <fo:block>

                    </fo:block>
                </fo:static-content>
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-size="12.0pt" font-family="sans-serif"
                              padding-after="2.0pt" space-before="2.0pt" text-align="center"
                              border-top-style="solid" border-bottom-width="1.0pt">
                        <xsl:text>Page </xsl:text>
                        <fo:page-number/>
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
                    <fo:block text-align="center">
                    <fo:table table-layout="fixed" width="100%">
                        <fo:table-column column-width="50%"/>
                        <fo:table-column column-width="50%"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell>
                                    <fo:block font-size="12pt" font-family="sans-serif"
                                              color="black" text-align="left" padding-top="3pt">
                                        Order ID :
                                        <xsl:value-of select="orderId"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="12pt" font-family="sans-serif"
                                              color="black" text-align="right" padding-top="3pt">
                                        Channel :
                                        <xsl:value-of select="channelName"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row margin-top="3pt">
                                <fo:table-cell>
                                    <fo:block font-size="12pt" font-family="sans-serif"
                                              color="black" text-align="left" padding-top="3pt">
                                        Client :
                                        <xsl:value-of select="clientName"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="12pt" font-family="sans-serif"
                                              color="black" text-align="right" padding-top="3pt">
                                        Invoice Type :
                                        <xsl:value-of select="invoiceType"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row margin-top="3pt">
                                <fo:table-cell>
                                    <fo:block font-size="12pt" font-family="sans-serif"
                                              color="black" text-align="left" padding-top="3pt">
                                        Customer :
                                        <xsl:value-of select="customerName"/>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="12pt" font-family="sans-serif"
                                              color="black" text-align="right" padding-top="3pt">
                                        Date :
                                        <xsl:value-of select="date"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row margin-top="3pt">
                                <fo:table-cell>
                                    <fo:block font-size="12pt" font-family="sans-serif"
                                              color="black" text-align="left" padding-top="3pt">
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell>
                                    <fo:block font-size="12pt" font-family="sans-serif"
                                              color="black" text-align="right" padding-top="3pt">
                                        Time :
                                        <xsl:value-of select="time"/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                        <fo:table table-layout="fixed" width="100%" border-collapse="separate" margin-top="20pt">
                            <fo:table-column column-width="10%"/>
                            <fo:table-column column-width="25%"/>
                            <fo:table-column column-width="25%"/>
                            <fo:table-column column-width="10%"/>
                            <fo:table-column column-width="15%"/>
                            <fo:table-column column-width="15%"/>
                            <fo:table-header font-weight="bold">
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" background-color="blue" color="white" text-align="center"
                                              font-weight="bold">#
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" background-color="blue" color="white" text-align="center"
                                              font-weight="bold">Client Sku
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" background-color="blue" color="white" text-align="center"
                                              font-weight="bold">Product Name
                                    </fo:block>
                                </fo:table-cell>

                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" background-color="blue" color="white" text-align="center"
                                              font-weight="bold">Quantity
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" background-color="blue" color="white" text-align="center"
                                              font-weight="bold">Price
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" background-color="blue" color="white" text-align="center"
                                              font-weight="bold">Total Price
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-header>
                            <fo:table-body>
                                <xsl:apply-templates
                                        select="item"/>
                            </fo:table-body>
                        </fo:table>
                    <fo:table table-layout="fixed" width="100%">
                        <fo:table-column column-width="50%" />
                        <fo:table-column column-width="50%" />
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell padding-top="50pt">
                                    <fo:block font-size="18pt" font-family="sans-serif"
                                              color="black" text-align="left" >
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding-top="50pt">
                                    <fo:block font-size="18pt" font-family="sans-serif"
                                              color="black" text-align="right">
                                        Total Amount :
                                        <xsl:value-of select="totalAmount"/> /-
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="item">
        <fo:table-row space-after="5mm">
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt" padding="4pt">
                    <xsl:value-of select="id"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt" padding="4pt">
                    <xsl:value-of select="clientSkuId"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt" padding="4pt">
                    <xsl:value-of select="productName"/>
                </fo:block>
            </fo:table-cell>

            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt" padding="4pt">
                    <xsl:value-of select="orderedQuantity"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt" padding="4pt">
                    <xsl:value-of select="sellingPricePerUnit"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block text-align="center" font-size="15pt" padding="4pt">
                    <xsl:value-of select="totalPrice"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
</xsl:stylesheet>