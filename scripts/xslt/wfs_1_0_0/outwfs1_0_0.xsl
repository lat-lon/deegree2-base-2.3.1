<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:gml="http://www.opengis.net/gml"
  xmlns:wfs="http://www.opengis.net/wfs"
  version="1.0">

  <xsl:output
    method="xml"
    indent="yes" />

  <!-- maps gml3.1.1 featurecollection to gml2.1.2 featurecollection -->
  <xsl:template match="wfs:FeatureCollection">
    <xsl:copy>
      <xsl:apply-templates select="@gml:id"/>
      <xsl:apply-templates select="*"/>
    </xsl:copy>
  </xsl:template>

  <!-- none mappable elements-->
  <xsl:template match="gml:metaDataProperty"/>
  <xsl:template match="gml:location"/>
  <xsl:template match="@axisLabels"/>
  <xsl:template match="@uomLabels"/>
  <xsl:template match="@srsDimension"/>



  <xsl:template match="@gml:id">
    <xsl:attribute name="fid">
      <xsl:value-of select="."/>        
    </xsl:attribute>
  </xsl:template>

  <xsl:template name="StandardObjectProperties">
      <!--xsl:apply-templates select="gml:metaDataProperty"/-->
      <xsl:apply-templates select="gml:description"/>
      <xsl:apply-templates select="gml:name[0]"/>    
  </xsl:template>

  <xsl:template match="gml:description">
    <xsl:copy-of select="."/>
  </xsl:template>

  <xsl:template match="gml:name">
    <xsl:copy-of select="."/>    
  </xsl:template>

  <xsl:template match="gml:MultiSurface">
    
  </xsl:template>

  <xsl:template match="gml:boundedBy">
    <xsl:copy>
      <xsl:apply-templates select="gml:Envelope"/>
      <xsl:apply-templates select="gml:Null"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="gml:Null">
    <xsl:param name="null" select="."/>
    <gml:Null>
      <xsl:choose>
        <xsl:when test="$null = 'inapplicable' or $null='missing' or $null='unknown'">
          <xsl:value-of select="$null"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>unavailable</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </gml:Null>
  </xsl:template>


  <xsl:template match="gml:featureMember">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:apply-templates select="*"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="*">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:choose>
        <xsl:when test="count(*) = 0">
          <xsl:value-of select="."/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="*"/>          
        </xsl:otherwise>
      </xsl:choose>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@*">
    <xsl:copy-of select="."/>
  </xsl:template>


  <!--################################
       # * Geometry Section 
       # * Because deegree, uses a predefined output 
       # * (see org.deegree.model.spatialschema.GMLGeometryAdapter),
       # * following geometries are copied-of, 
       # * @id and @gid are never set for geometries so no need to fear them:
       # - gml:LineString (only uses gml:coordinates)
       # - gml:Point (only uses gml:coordinates)
       # - gml:Polygon (which is a surface or multisurface) uses the deprecated 
       #   gml:outerBoundaryIs (outerBoundaryIs in gml:3.1.1 is an extension of gml:exterior) 
       #   and the gml:innerBoundaryIs (also an deprecrecated extension of gml:interior) 
       #   both use the deprecated gml:coordinates.
       # - gml:MultiPoint only uses gml:PointMember (not gml:PointMembers), there can be 
       #   null member though...which in gml2 must at leas be one.
       ###############################-->

  <xsl:template name="copy_attribs">
    <xsl:copy-of select="@xlink:*"/>
    <xsl:copy-of select="@remoteSchema"/>
    <xsl:choose>
      <xsl:when test="@gid != '' ">
        <xsl:copy-of select="@gid"/>          
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="@gml:id != '' ">
          <xsl:attribute name="gid" >
            <xsl:value-of select="@gml:id"/>
          </xsl:attribute>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:copy-of select="@srsName"/>
  </xsl:template>
  
  <xsl:template name="create_required_srsName">
    <xsl:if test="@srsName = ''">
      <xsl:attribute name="srsName">
        <xsl:text>unknown</xsl:text>
      </xsl:attribute>                
    </xsl:if>
  </xsl:template>

  <xsl:template match="gml:Envelope">
    <gml:Box>
      <xsl:call-template name="copy_attribs"/>
      <gml:coordinates decimal="." cs="&#x20;" ts=",">
        <xsl:param name="counter" select="count( gml:pos )"/>
        <xsl:for-each select="gml:pos">
          <xsl:value-of select="."/>
          <xsl:if test="position() &lt; $counter">
            <xsl:text>,</xsl:text>
          </xsl:if>
        </xsl:for-each>
      </gml:coordinates>
    </gml:Box>
  </xsl:template>  

  <xsl:template match="gml:MultiPoint">
      <xsl:call-template name="copy_attribs"/>
      <xsl:call-template name="create_required_srsName"/>
      <xsl:copy-of select="*"/>
  </xsl:template>

  <xsl:template match="gml:MultiCurve">
    <gml:MultiLineString>
      <xsl:call-template name="copy_attribs"/>
      <xsl:call-template name="create_required_srsName"/>
      <!--name, description etc. are not valid for GML2, only select the curveMembers-->
      <xsl:apply-templates select="gml:curveMember"/>
    </gml:MultiLineString>
  </xsl:template>  

  <xsl:template match="gml:curveMember" >
    <gml:lineStringMember>
      <xsl:call-template name="copy_attribs"/>
      <!-- Attention, the GML2 spec defines minOccurs=1, GML3.1.1 minOccurs=0 -->
      <!--name, description etc. are not valid for GML2, only select the curveMembers-->
      <xsl:copy-of select="gml:LineString" />      
    </gml:lineStringMember>
  </xsl:template>


  <xsl:template match="gml:MultiSurface">
    <gml:MultiPolygon>
      <xsl:call-template name="copy_attribs"/>
      <xsl:call-template name="create_required_srsName"/>
      <!--name, description etc. are not valid for GML2, only select the curveMembers-->
      <xsl:apply-templates select="gml:surfaceMember"/>
    </gml:MultiPolygon>
  </xsl:template>  

  <xsl:template match="gml:surfaceMember" >
    <gml:polygonMember>
      <xsl:call-template name="copy_attribs"/>
      <!-- Attention, the GML2 spec defines minOccurs=1, GML3.1.1 minOccurs=0 -->
      <!--name, description etc. are not valid for GML2, only select the curveMembers-->
      <xsl:apply-templates select="gml:Polygon" />      
    </gml:polygonMember>
  </xsl:template>

  <xsl:template match="gml:Polygon">
    <xsl:copy>
      <xsl:call-template name="copy_attribs"/>      
      <xsl:apply-templates select="*[local-name( )!= 'name' or local-name( )!= 'metaDataProperty' or local-name( )!= 'description']"/>      
      
    </xsl:copy>
  </xsl:template>

  <xsl:template match="gml:exterior">
    <gml:outerBoundaryIs>
      <xsl:call-template name="copy_attribs"/>
      <xsl:copy-of select="gml:LinearRing"/>
    </gml:outerBoundaryIs>
  </xsl:template>


  <xsl:template match="gml:interior">
    <gml:innerBoundaryIs>
      <xsl:call-template name="copy_attribs"/>      
      <xsl:copy-of select="gml:LinearRing"/>
    </gml:innerBoundaryIs>
  </xsl:template>






</xsl:stylesheet>
