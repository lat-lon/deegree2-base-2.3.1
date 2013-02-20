// $HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/xml/MinMaxExtractor.java $
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
 Aennchenstraße 19
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
package org.deegree.framework.xml;

import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GMLGeometryAdapter;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.model.spatialschema.GeometryException;
import org.deegree.model.spatialschema.Point;
import org.deegree.model.spatialschema.Position;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Provides methods to XSLT-Sheets that determine the min/max coordinates of a gml geometry.
 * <p>
 * The submitted node parameter must be set to the root node of the geometry.
 * 
 * @version $Revision: 9339 $
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 9339 $, $Date: 2007-12-27 13:31:52 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */
public class MinMaxExtractor {

    /**
     * 
     * @param node
     * @return maximum x coordinate
     * @throws GeometryException
     */
    public static String getXMax( Node node )
                            throws GeometryException {
        if ( node == null ) {
            return "";
        }
        Geometry geometry = GMLGeometryAdapter.wrap( (Element) node, null );
        Envelope envelope = geometry.getEnvelope();
        return Double.toString( envelope.getMax().getX() );
    }

    /**
     * 
     * @param node
     * @return minimum x coordinate
     * @throws GeometryException
     */
    public static String getXMin( Node node )
                            throws GeometryException {
        if ( node == null ) {
            return "";
        }
        Geometry geometry = GMLGeometryAdapter.wrap( (Element) node, null );
        Envelope envelope = geometry.getEnvelope();
        return Double.toString( envelope.getMin().getX() );
    }

    /**
     * 
     * @param node
     * @return maximum y coordinate
     * @throws GeometryException
     */
    public static String getYMax( Node node )
                            throws GeometryException {
        if ( node == null ) {
            return "";
        }
        Geometry geometry = GMLGeometryAdapter.wrap( (Element) node, null );
        Envelope envelope = geometry.getEnvelope();
        return Double.toString( envelope.getMax().getY() );
    }

    /**
     * 
     * @param node
     * @return minimum y coordinate
     * @throws GeometryException
     */
    public static String getYMin( Node node )
                            throws GeometryException {
        if ( node == null ) {
            return "";
        }
        Geometry geometry = GMLGeometryAdapter.wrap( (Element) node, null );
        Envelope envelope = geometry.getEnvelope();
        return Double.toString( envelope.getMin().getY() );
    }

    /**
     * 
     * @param node
     * @return maximum z coordinate
     * @throws GeometryException
     */
    public static String getZMin( Node node )
                            throws GeometryException {
        if ( node == null ) {
            return "";
        }
        Geometry geometry = GMLGeometryAdapter.wrap( (Element) node, null );
        Envelope envelope = geometry.getEnvelope();
        if ( geometry.getCoordinateDimension() > 2 ) {
            return "";
        }
        return Double.toString( envelope.getMin().getZ() );
    }

    /**
     * 
     * @param node
     * @return minimum z coordinate
     * @throws GeometryException
     */
    public static String getZMax( Node node )
                            throws GeometryException {
        if ( node == null ) {
            return "";
        }
        Geometry geometry = GMLGeometryAdapter.wrap( (Element) node, null );
        Envelope envelope = geometry.getEnvelope();
        if ( geometry.getCoordinateDimension() > 2 ) {
            return "";
        }
        return Double.toString( envelope.getMax().getZ() );

    }

    /**
     * Extracts the x value of a gml:Point element described by <code>pointNode</code>
     * 
     * @param pointNode
     *            the point node from which the x value will be extracted. For example, node is
     *            &lt;gml:Point srsName="EPSG:31466"&gt; &lt;gml:coordinates cs="," decimal="." ts="
     *            "&gt;0.0,0.0&lt;/gml:coordinates&gt; &lt;/gml:Point&gt;
     * @return the String representation of the x value
     * @throws GeometryException
     */
    public static String getPointX( Node pointNode )
                            throws GeometryException {
        return getPointXorY( pointNode, 0 );
    }

    /**
     * Extracts the y value of a gml:Point element described by <code>pointNode</code>
     * 
     * @param pointNode
     *            the point node from which the y value will be extracted. For example, node is
     *            &lt;gml:Point srsName="EPSG:31466"&gt; &lt;gml:coordinates cs="," decimal="." ts="
     *            "&gt;0.0,0.0&lt;/gml:coordinates&gt; &lt;/gml:Point&gt;
     * @return the String representation of the y value
     * @throws GeometryException
     */
    public static String getPointY( Node pointNode )
                            throws GeometryException {
        return getPointXorY( pointNode, 1 );
    }

    /**
     * 
     * @param pointNode
     *            the point node from which the x or y value will be extracted. For example, node is
     *            &lt;gml:Point srsName="EPSG:31466"&gt; &lt;gml:coordinates cs="," decimal="." ts="
     *            "&gt;0.0,0.0&lt;/gml:coordinates&gt; &lt;/gml:Point&gt;
     * @param coordIndex
     *            the coordenate index indicated whether to extract from x (index = 0) otherwise
     *            from y
     * @return the String representation of the x or y value
     * @throws GeometryException
     */
    private static String getPointXorY( Node pointNode, int coordIndex )
                            throws GeometryException {
        String value = "";

        if ( pointNode != null ) {

            Geometry geometry = GMLGeometryAdapter.wrap( (Element) pointNode, null );
            if ( geometry instanceof Point ) {
                Point p = (Point) geometry;
                double d = coordIndex == 0 ? p.getX() : p.getY();
                value = Double.toString( d );
            }
        }

        return value;

    }

    /**
     * returns the minimum coordinate of the envelope of the geometry encoded by the passed node as
     * a double array
     * 
     * @param node
     * @return the minimum coordinate of the envelope of the geometry encoded by the passed node as
     *         a double array
     * @throws GeometryException
     */
    public static String getMinAsArray( Node node )
                            throws GeometryException {
        if ( node == null ) {
            return "";
        }
        Geometry geometry = GMLGeometryAdapter.wrap( (Element) node, null );
        if ( geometry instanceof Point ) {
            return "";
        }
        Envelope env = geometry.getEnvelope();
        StringBuffer sb = new StringBuffer( 100 );

        Position pos = env.getMin();
        int dim = pos.getCoordinateDimension();
        double[] d = pos.getAsArray();
        for ( int i = 0; i < dim - 1; i++ ) {
            sb.append( Double.toString( d[i] ) ).append( ' ' );
        }
        sb.append( Double.toString( d[dim - 1] ) );

        return sb.toString();
    }

    /**
     * returns the minimum coordinate of the envelope of the geometry encoded by the passed node as
     * a double array
     * 
     * @param node
     * @return the minimum coordinate of the envelope of the geometry encoded by the passed node as
     *         a double array
     * @throws GeometryException
     */
    public static String getMaxAsArray( Node node )
                            throws GeometryException {
        if ( node == null ) {
            return "";
        }
        Geometry geometry = GMLGeometryAdapter.wrap( (Element) node, null );
        if ( geometry instanceof Point ) {
            return "";
        }
        Envelope env = geometry.getEnvelope();
        StringBuffer sb = new StringBuffer( 100 );

        Position pos = env.getMax();
        int dim = pos.getCoordinateDimension();
        double[] d = pos.getAsArray();
        for ( int i = 0; i < dim - 1; i++ ) {
            sb.append( Double.toString( d[i] ) ).append( ' ' );
        }
        sb.append( Double.toString( d[dim - 1] ) );

        return sb.toString();
    }

    /**
     * returns the the name of the SRS of the geometry encoded by the passed node as a double array
     * 
     * @param node
     * @return the the name of the SRS of the geometry encoded by the passed node as a double array
     * @throws GeometryException
     */
    public static String getSRSName( Node node ) {
        if ( node == null ) {
            return "";
        }
        return ( (Element) node ).getAttribute( "srsName" );
    }

}
