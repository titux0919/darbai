<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="text"/>

<xsl:template match="/">

Purchase Order Nr: <xsl:value-of select="/PurchaseOrder/@PurchaseOrderNumber"/>.
Order date: <xsl:value-of select="/PurchaseOrder/@OrderDate"/>

Shipping Address:
<xsl:value-of select="/PurchaseOrder/Address[@Type='Shipping']/Name"/>,
<xsl:value-of select="/PurchaseOrder/Address[@Type='Shipping']/Zip"/>,
<xsl:value-of select="/PurchaseOrder/Address[@Type='Shipping']/Street"/>,
<xsl:value-of select="/PurchaseOrder/Address[@Type='Shipping']/City"/>,
<xsl:value-of select="/PurchaseOrder/Address[@Type='Shipping']/State"/>,
<xsl:value-of select="/PurchaseOrder/Address[@Type='Shipping']/Country"/>

Billing Address:
<xsl:value-of select="/PurchaseOrder/Address[@Type='Billing']/Name"/>,
<xsl:value-of select="/PurchaseOrder/Address[@Type='Billing']/Zip"/>,
<xsl:value-of select="/PurchaseOrder/Address[@Type='Billing']/Street"/>,
<xsl:value-of select="/PurchaseOrder/Address[@Type='Billing']/City"/>,
<xsl:value-of select="/PurchaseOrder/Address[@Type='Billing']/State"/>,
<xsl:value-of select="/PurchaseOrder/Address[@Type='Billing']/Country"/>

</xsl:template>

</xsl:stylesheet>