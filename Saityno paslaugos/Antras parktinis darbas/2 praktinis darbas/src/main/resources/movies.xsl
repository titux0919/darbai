<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Movie List</title>
            </head>

            <body style="font-family: Arial;">

                <!-- FILMAI -->
                <h2>Movie List</h2>

                <xsl:for-each select="movies/movie">
                    <div style="border:1px solid black; padding:10px; margin-bottom:15px;">

                        <h3><xsl:value-of select="title"/></h3>

                        <p><b>Year:</b> <xsl:value-of select="year"/></p>
                        <p><b>Rating:</b> <xsl:value-of select="rating"/></p>
                        <p><b>Popular:</b> <xsl:value-of select="popular"/></p>
                        <p><b>Category:</b> <xsl:value-of select="category"/></p>

                        <b>Actors:</b>
                        <ul>
                            <xsl:for-each select="actors/actor">
                                <li>
                                    <xsl:value-of select="name"/>
                                    (Age: <xsl:value-of select="age"/>)
                                </li>
                            </xsl:for-each>
                        </ul>

                    </div>
                </xsl:for-each>

            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>