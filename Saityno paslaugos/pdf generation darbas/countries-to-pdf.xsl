<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version="1.0">
    <xsl:output encoding="UTF-8" indent="yes" method="xml" standalone="no" omit-xml-declaration="no"/>
    <xsl:template match="/">
        <fo:root language="EN">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4-portrait" page-height="297mm"
                                       page-width="210mm" margin-top="5mm"
                                       margin-bottom="5mm" margin-left="5mm"
                                       margin-right="5mm">
                    <fo:region-body  margin-top="25mm" margin-bottom="20mm"/>
                    <fo:region-before region-name="xsl-region-before" extent="25mm" display-align="before"
                                      precedence="true"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4-portrait">
                <fo:flow flow-name="xsl-region-body" border-collapse="collapse" reference-orientation="0">
                    <fo:block vertical-align="top" text-align="center">
                        <fo:external-graphic src="countries.svg">
                        </fo:external-graphic>
                    </fo:block>
                    <fo:block text-align="center">
                        <fo:table table-layout="fixed" width="95%" font-size="10pt"
                                  border-color="grey" border-width="0.1mm"
                                  border-style="solid" text-align="center"
                                  display-align="center" space-after="5mm">
                            <fo:table-column column-width="proportional-column-width(50)"/>
                            <fo:table-column column-width="proportional-column-width(50)"/>
                            <fo:table-column column-width="proportional-column-width(50)"/>
                            <fo:table-column column-width="proportional-column-width(20)"/>
                            <fo:table-column column-width="proportional-column-width(30)"/>
                            <fo:table-body font-size="95%">
                                <fo:table-row height="8mm" border-color="grey"
                                              border-width="0.1mm" border-style="solid"
                                              text-align="start" display-align="center"
                                              space-after="5mm">
                                    <fo:table-cell>
                                        <fo:block>Name</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>Continent</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>Currency</fo:block>
                                    </fo:table-cell>

                                    <fo:table-cell>
                                        <fo:block>Calling-code</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>Area</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <xsl:for-each select="countries/country">
                                    <fo:table-row text-align="start">
                                        <fo:table-cell>
                                            <fo:block>
                                                <xsl:value-of select="continent"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell>
                                            <fo:block>
                                                <xsl:value-of select="currency"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell>
                                            <fo:block>
                                                <xsl:value-of select="calling-code"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell>
                                            <fo:block>
                                                <xsl:value-of select="area"/>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <fo:block id="end-of-document">
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>