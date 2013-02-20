//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wpvs/j3d/Object3DFactory.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.ogcwebservices.wpvs.j3d;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.media.j3d.Material;
import javax.vecmath.Color3f;
import javax.vecmath.TexCoord2f;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.BootLogger;
import org.deegree.framework.util.ImageUtils;
import org.deegree.framework.util.StringTools;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureProperty;
import org.deegree.model.spatialschema.Surface;

/**
 * 
 * 
 * 
 * @version $Revision: 10461 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * $Revision: 10461 $, $Date: 2008-03-05 17:50:00 +0100 (Wed, 05 Mar 2008) $
 * 
 */
public class Object3DFactory {

    private static InputStream materialURL;

    private static Properties material = new Properties();

    private static final ILogger LOG = LoggerFactory.getLogger( Object3DFactory.class );

    /**
     * all texture images will be stored on a Map to avoid double loading and creating a BufferedImage from an image
     * source (textureMap property)
     */
    private static Map<String, BufferedImage> textImgMap;

    private static QualifiedName objectID;

    private static QualifiedName textMapQn;

    private static QualifiedName textCoordsQn;

    private static QualifiedName shininessQn;

    private static QualifiedName transparencyQn;

    private static QualifiedName ambientintensityQn;

    private static QualifiedName specularcolorQn;

    private static QualifiedName diffusecolorQn;

    private static QualifiedName emissivecolorQn;
    static {
        try {
            textImgMap = new HashMap<String, BufferedImage>( 200 );
            textMapQn = new QualifiedName( "app", "texturemap", new URI( "http://www.deegree.org/app" ) );
            textCoordsQn = new QualifiedName( "app", "texturecoordinates", new URI( "http://www.deegree.org/app" ) );
            shininessQn = new QualifiedName( "app", "shininess", new URI( "http://www.deegree.org/app" ) );
            transparencyQn = new QualifiedName( "app", "transparency", new URI( "http://www.deegree.org/app" ) );
            ambientintensityQn = new QualifiedName( "app", "ambientintensity", new URI( "http://www.deegree.org/app" ) );
            specularcolorQn = new QualifiedName( "app", "specularcolor", new URI( "http://www.deegree.org/app" ) );
            diffusecolorQn = new QualifiedName( "app", "diffusecolor", new URI( "http://www.deegree.org/app" ) );
            emissivecolorQn = new QualifiedName( "app", "emissivecolor", new URI( "http://www.deegree.org/app" ) );
            objectID = new QualifiedName( "app", "fk_feature", new URI( "http://www.deegree.org/app" ) );
            materialURL = Object3DFactory.class.getResourceAsStream( "material.properties" );
            material.load( materialURL );
        } catch ( URISyntaxException e ) {
            BootLogger.logError( e.getMessage(), e );
        } catch ( IOException e ) {
            BootLogger.logError( e.getMessage(), e );
        }
    }

    /**
     * creates a Surface from the passed feature. It is assumed the feature is simple, contains one surfac/polygon
     * geometry and optional has material and/or texture informations. The GML schema for a valid feature is defined as:
     * 
     * <pre>
     *    &lt;xsd:schema targetNamespace=&quot;http://www.deegree.org/app&quot; xmlns:app=&quot;http://www.deegree.org/app&quot; xmlns:ogc=&quot;http://www.opengis.net/ogc&quot; xmlns:deegreewfs=&quot;http://www.deegree.org/wfs&quot; xmlns:xsd=&quot;http://www.w3.org/2001/XMLSchema&quot; xmlns:gml=&quot;http://www.opengis.net/gml&quot; elementFormDefault=&quot;qualified&quot; attributeFormDefault=&quot;unqualified&quot;&gt;
     *        &lt;xsd:import namespace=&quot;http://www.opengis.net/gml&quot; schemaLocation=&quot;http://schemas.opengis.net/gml/3.1.1/base/feature.xsd&quot;/&gt;
     *         &lt;xsd:import namespace=&quot;http://www.opengis.net/gml&quot; schemaLocation=&quot;http://schemas.opengis.net/gml/3.1.1/base/geometryAggregates.xsd&quot;/&gt;
     *         &lt;xsd:element name=&quot;WPVS&quot; type=&quot;app:WPVSType&quot; substitutionGroup=&quot;gml:_Feature&quot;/&gt;
     *         &lt;xsd:complexType name=&quot;WPVSType&quot;&gt;
     *             &lt;xsd:complexContent&gt;
     *                 &lt;xsd:extension base=&quot;gml:AbstractFeatureType&quot;&gt;
     *                     &lt;xsd:sequence&gt;
     *                         &lt;xsd:element name=&quot;fk_feature&quot; type=&quot;xsd:double&quot;/&gt;
     *                         &lt;xsd:element name=&quot;geometry&quot; type=&quot;gml:GeometryPropertyType&quot;/&gt;
     *                         &lt;xsd:element name=&quot;shininess&quot; type=&quot;xsd:double&quot; minOccurs=&quot;0&quot;/&gt;
     *                         &lt;xsd:element name=&quot;transparency&quot; type=&quot;xsd:double&quot; minOccurs=&quot;0&quot;/&gt;
     *                         &lt;xsd:element name=&quot;ambientintensity&quot; type=&quot;xsd:double&quot; minOccurs=&quot;0&quot;/&gt;
     *                         &lt;xsd:element name=&quot;specularcolor&quot; type=&quot;xsd:string&quot; minOccurs=&quot;0&quot;/&gt;
     *                         &lt;xsd:element name=&quot;diffusecolor&quot; type=&quot;xsd:string&quot; minOccurs=&quot;0&quot;/&gt;
     *                         &lt;xsd:element name=&quot;emissivecolor&quot; type=&quot;xsd:string&quot; minOccurs=&quot;0&quot;/&gt;
     *                         &lt;xsd:element name=&quot;texturemap&quot; type=&quot;xsd:string&quot; minOccurs=&quot;0&quot;/&gt;
     *                         &lt;xsd:element name=&quot;texturecoordinates&quot; type=&quot;xsd:string&quot; minOccurs=&quot;0&quot;/&gt;
     *                         &lt;xsd:element name=&quot;texturetype&quot; type=&quot;xsd:string&quot; minOccurs=&quot;0&quot;/&gt;
     *                         &lt;xsd:element name=&quot;repeat&quot; type=&quot;xsd:integer&quot; minOccurs=&quot;0&quot;/&gt;
     *                     &lt;/xsd:sequence&gt;
     *                 &lt;/xsd:extension&gt;
     *             &lt;/xsd:complexContent&gt;
     *         &lt;/xsd:complexType&gt;
     *     &lt;/xsd:schema&gt;
     * </pre>
     * 
     * @param feature
     * @param texturedShapes
     *            which were loaded already
     * @return a DefaultSurface which is a derivative of a Shape3D. NOTE, the surface is not yet 'compiled'.
     */
    public DefaultSurface createSurface( Feature feature, Map<String, TexturedSurface> texturedShapes ) {

        double oId = -1d;
        if ( feature.getDefaultProperty( objectID ).getValue( new Double( -1 ) ) instanceof BigDecimal ) {
            oId = ( (BigDecimal) feature.getDefaultProperty( objectID ).getValue( new Double( -1 ) ) ).doubleValue();
        } else if ( feature.getDefaultProperty( objectID ).getValue( new Double( -1 ) ) instanceof Double ) {
            oId = ( (Double) feature.getDefaultProperty( objectID ).getValue( new Double( -1d ) ) ).doubleValue();
        }

        // read texture informations (if available) from feature
        BufferedImage textImage = null;
        FeatureProperty[] fp = feature.getProperties( textMapQn );
        TexturedSurface cachedSurface = null;
        String textureFile = null;
        if ( fp != null && fp.length > 0 ) {
            textureFile = (String) feature.getProperties( textMapQn )[0].getValue();
            if ( textureFile != null && !"".equals( textureFile.trim() ) ) {
                if ( texturedShapes.containsKey( textureFile ) ) {
                    cachedSurface = texturedShapes.get( textureFile );
                } else {
                    textImage = textImgMap.get( textureFile );
                    if ( textImage == null ) {
                        String lt = textureFile.toLowerCase();
                        try {
                             if ( lt.startsWith( "file:" ) || lt.startsWith( "http:" ) ) {
                             textImage = ImageUtils.loadImage( new URL( textureFile ) );
                             } else {
                             textImage = ImageUtils.loadImage( textureFile );
                             }
                            //textImage = ImageIO.read( new URL( textureFile ) );

                        } catch ( MalformedURLException e ) {
                            e.printStackTrace();
                        } catch ( IOException e ) {
                            e.printStackTrace();
                        }
                        if ( textImage != null ) {
                            textImgMap.put( textureFile, textImage );
                        } else {
                            LOG.logWarning( "Failed to load texture image: " + textureFile );
                        }
                    }
                }
            }
        }
        // float[][] textureCoords = new float[1][];
        List<TexCoord2f> textureCoords = null;
        if ( textImage != null || cachedSurface != null) {
            fp = feature.getProperties( textCoordsQn );
            if ( fp != null && fp.length > 0 ) {
                String tmp = (String) fp[0].getValue();
                LOG.logDebug( "Texture Coordinates: " + tmp );
                if ( tmp != null ) {
                    float[] tc = StringTools.toArrayFloat( tmp, ", " );
                    if ( tc != null && tc.length > 0 ) {
                        textureCoords = new ArrayList<TexCoord2f>( tc.length );
                        for ( int i = 0; i < tc.length; i += 2 ) {
                            textureCoords.add( new TexCoord2f( tc[i], tc[i + 1] ) );
                        }
                    }
                }
            }
        }

        // read color informations from feature. If not available use default values
        // from material.properties
        Double shininess = new Double( material.getProperty( "shininess" ) );
        shininess = (Double) feature.getDefaultProperty( shininessQn ).getValue( shininess );
        Double transparency = new Double( material.getProperty( "transparency" ) );
        transparency = (Double) feature.getDefaultProperty( transparencyQn ).getValue( new Double( 0 ) );
        Double ambientintensity = new Double( material.getProperty( "ambientintensity" ) );
        ambientintensity = (Double) feature.getDefaultProperty( ambientintensityQn ).getValue( new Double( 1 ) );
        Color3f ambientcolor = new Color3f( ambientintensity.floatValue(),
                                            ambientintensity.floatValue(),
                                            ambientintensity.floatValue() );

        String tmp = material.getProperty( "specularcolor" );
        tmp = (String) feature.getDefaultProperty( specularcolorQn ).getValue( tmp );
        float[] tmpFl = StringTools.toArrayFloat( tmp.trim(), " " );
        Color3f specularcolor = new Color3f( tmpFl[0], tmpFl[1], tmpFl[2] );

        tmp = material.getProperty( "diffusecolor" );
        tmp = (String) feature.getDefaultProperty( diffusecolorQn ).getValue( tmp );
        tmpFl = StringTools.toArrayFloat( tmp.trim(), " " );
        Color3f diffusecolor = new Color3f( tmpFl[0], tmpFl[1], tmpFl[2] );

        tmp = material.getProperty( "emissivecolor" );
        tmp = (String) feature.getDefaultProperty( emissivecolorQn ).getValue( tmp );
        tmpFl = StringTools.toArrayFloat( tmp.trim(), " " );
        Color3f emissivecolor = new Color3f( tmpFl[0], tmpFl[1], tmpFl[2] );
        Material material = new Material( ambientcolor,
                                          emissivecolor,
                                          diffusecolor,
                                          specularcolor,
                                          shininess.floatValue() );
        // for diffuse-color to work the ambient lighting must be switched on
        material.setColorTarget( Material.AMBIENT_AND_DIFFUSE );
        /**
         * Please check for the right property here. The defaultPropertyValue just delivers the first geometry defined
         * in the wfs configuration.
         */
        Surface geom = (Surface) feature.getDefaultGeometryPropertyValue();
        DefaultSurface surface = null;
        if ( cachedSurface != null ) {
            LOG.logDebug( "Textured-Surface cached" );
            cachedSurface.addGeometry( geom, textureCoords );
        } else if ( textImage != null ) {
            LOG.logDebug( "Textured-Surface not cached" );
            texturedShapes.put( textureFile, new TexturedSurface( feature.getId(),
                                                                  String.valueOf( oId ),
                                                                  geom,
                                                                  material,
                                                                  transparency.floatValue(),
                                                                  textImage,
                                                                  textureCoords ) );
        } else {
            LOG.logDebug( "3D-Surface without texture: ", geom );
            surface = new ColoredSurface( feature.getId(),
                                          String.valueOf( oId ),
                                          geom,
                                          material,
                                          transparency.floatValue() );
        }
        // surface.compile();
        return surface;
    }
}