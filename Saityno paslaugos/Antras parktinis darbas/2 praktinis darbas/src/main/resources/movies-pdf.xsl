<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version="1.0">

    <xsl:template match="/">
        <fo:root>

            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="A4">
                <fo:flow flow-name="xsl-region-body">

                    <fo:block font-size="18pt" font-weight="bold" margin-bottom="10pt">
                        Movie List
                    </fo:block>

                    <xsl:for-each select="movies/movie">
                        <fo:block margin-bottom="10pt">

                            <fo:block font-weight="bold">
                                <xsl:value-of select="title"/>
                            </fo:block>

                            <fo:block>Year: <xsl:value-of select="year"/></fo:block>
                            <fo:block>Rating: <xsl:value-of select="rating"/></fo:block>
                            <fo:block>Popular: <xsl:value-of select="popular"/></fo:block>
                            <fo:block>Category: <xsl:value-of select="category"/></fo:block>

                            <fo:block font-weight="bold" margin-top="5pt">
                                Actors:
                            </fo:block>

                            <xsl:for-each select="actors/actor">
                                <fo:block>
                                    - <xsl:value-of select="name"/> (Age: <xsl:value-of select="age"/>)
                                </fo:block>
                            </xsl:for-each>

                        </fo:block>
                    </xsl:for-each>

                </fo:flow>
            </fo:page-sequence>

        </fo:root>
    </xsl:template>

</xsl:stylesheet>