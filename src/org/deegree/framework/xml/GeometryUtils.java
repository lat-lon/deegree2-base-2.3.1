//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/xml/GeometryUtils.java $
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
package org.deegree.framework.xml;

import java.util.List;

import org.deegree.framework.util.StringTools;
import org.deegree.model.crs.GeoTransformer;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GMLGeometryAdapter;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.model.spatialschema.MultiSurface;
import org.deegree.model.spatialschema.Point;
import org.deegree.model.spatialschema.Position;
import org.deegree.model.spatialschema.Ring;
import org.deegree.model.spatialschema.Surface;
import org.deegree.ogcbase.CommonNamespaces;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utility methods for handling geometries within XSLT transformations
 * 
 * 
 * @version $Revision: 10660 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 * 
 * @since 2.0
 */
public class GeometryUtils {

    private static NamespaceContext nsc = CommonNamespaces.getNamespaceContext();

    /**
     * 
     * @param node
     * @return
     */
    public static String getPolygonCoordinatesFromEnvelope( Node node ) {
        StringBuffer sb = new StringBuffer( 500 );
        try {
            Envelope env = GMLGeometryAdapter.wrapBox( (Element) node, null );
            sb.append( env.getMin().getX() ).append( ',' ).append( env.getMin().getY() ).append( ' ' );
            sb.append( env.getMin().getX() ).append( ',' ).append( env.getMax().getY() ).append( ' ' );
            sb.append( env.getMax().getX() ).append( ',' ).append( env.getMax().getY() ).append( ' ' );
            sb.append( env.getMax().getX() ).append( ',' ).append( env.getMin().getY() ).append( ' ' );
            sb.append( env.getMin().getX() ).append( ',' ).append( env.getMin().getY() );
        } catch ( Exception e ) {
            e.printStackTrace();
            sb.append( StringTools.stackTraceToString( e ) );
        }
        return sb.toString();
    }

    /**
     * 
     * @param node
     * @return
     */
    public static String getEnvelopeFromGeometry( Node node ) {
        StringBuffer sb = new StringBuffer( 500 );
        try {
            Envelope env = GMLGeometryAdapter.wrap( (Element) node, null ).getEnvelope();
            sb.append( env.getMin().getX() ).append( ',' ).append( env.getMin().getY() ).append( ' ' );
            sb.append( env.getMin().getX() ).append( ',' ).append( env.getMax().getY() ).append( ' ' );
            sb.append( env.getMax().getX() ).append( ',' ).append( env.getMax().getY() ).append( ' ' );
            sb.append( env.getMax().getX() ).append( ',' ).append( env.getMin().getY() ).append( ' ' );
            sb.append( env.getMin().getX() ).append( ',' ).append( env.getMin().getY() );
        } catch ( Exception e ) {
            e.printStackTrace();
            sb.append( StringTools.stackTraceToString( e ) );
        }
        return sb.toString();
    }

    /**
     * returns the coordinates of the out ring of a polygon as comma seperated list. The coordinate
     * tuples are seperated by a blank. If required the polygon will first transformed to the target
     * CRS
     * 
     * @param node
     * @param sourceCRS
     * @param targetCRS
     * @return
     */
    public static String getPolygonOuterRing( Node node, String sourceCRS, String targetCRS ) {
        StringBuffer coords = new StringBuffer( 10000 );

        try {
            Surface surface = (Surface) GMLGeometryAdapter.wrap( (Element) node, sourceCRS );
            if ( !targetCRS.equals( sourceCRS ) ) {
                GeoTransformer gt = new GeoTransformer( targetCRS );
                surface = (Surface) gt.transform( surface );
            }
            Position[] pos = surface.getSurfaceBoundary().getExteriorRing().getPositions();
            int dim = pos[0].getCoordinateDimension();
            for ( int i = 0; i < pos.length; i++ ) {
                coords.append( pos[i].getX() ).append( ',' ).append( pos[i].getY() );
                if ( dim == 3 ) {
                    coords.append( ',' ).append( pos[i].getZ() );
                }
                coords.append( ' ' );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return coords.toString();
    }

    /**
     * 
     * @param node
     * @param index
     * @param sourceCRS
     * @param targetCRS
     * @return
     */
    public static String getPolygonInnerRing( Node node, int index, String sourceCRS, String targetCRS ) {
        StringBuffer coords = new StringBuffer( 10000 );

        if ( "Polygon".equals( node.getLocalName() ) || "Surface".equals( node.getLocalName() ) ) {
            try {
                Surface surface = (Surface) GMLGeometryAdapter.wrap( (Element) node, sourceCRS );
                if ( !targetCRS.equals( sourceCRS ) ) {
                    GeoTransformer gt = new GeoTransformer( targetCRS );
                    surface = (Surface) gt.transform( surface );
                }
                Position[] pos = surface.getSurfaceBoundary().getInteriorRings()[index - 1].getPositions();
                int dim = pos[0].getCoordinateDimension();
                for ( int i = 0; i < pos.length; i++ ) {
                    coords.append( pos[i].getX() ).append( ',' ).append( pos[i].getY() );
                    if ( dim == 3 ) {
                        coords.append( ',' ).append( pos[i].getZ() );
                    }
                    coords.append( ' ' );
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        return coords.toString();
    }

    /**
     * 
     * @param node
     * @return
     */
    public static double calcArea( Node node ) {
        double area = -1;
        try {
            Geometry geom = GMLGeometryAdapter.wrap( (Element) node, null );
            if ( geom instanceof Surface ) {
                area = ( (Surface) geom ).getArea();
            } else if ( geom instanceof MultiSurface ) {
                area = ( (MultiSurface) geom ).getArea();
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return area;
    }

    /**
     * 
     * @param node
     * @return
     */
    public static double calcOuterBoundaryLength( Node node ) {
        double length = 0;
        try {
            Geometry geom = GMLGeometryAdapter.wrap( (Element) node, null );
            if ( geom instanceof Surface ) {
                Ring ring = ( (Surface) geom ).getSurfaceBoundary().getExteriorRing();
                length = ring.getAsCurveSegment().getLength();
            } else if ( geom instanceof MultiSurface ) {
                MultiSurface ms = ( (MultiSurface) geom );
                for ( int i = 0; i < ms.getSize(); i++ ) {
                    Ring ring = ms.getSurfaceAt( i ).getSurfaceBoundary().getExteriorRing();
                    length += ring.getAsCurveSegment().getLength();
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return length;
    }

    /**
     * returns the centroid X coordinate of the geometry represented by the passed Node
     * 
     * @param node
     * @param targetCRS
     * @return
     */
    public static double getCentroidX( Node node, String targetCRS ) {
        if ( node != null ) {
            Point point = null;
            try {
                if ( "Envelope".equals( node.getLocalName() ) ) {
                    Envelope env = GMLGeometryAdapter.wrapBox( (Element) node, null );
                    point = env.getCentroid();
                } else {
                    Geometry geom = GMLGeometryAdapter.wrap( (Element) node, null );
                    point = geom.getCentroid();
                }
                GeoTransformer gt = new GeoTransformer( targetCRS );
                point = (Point) gt.transform( point );
            } catch ( Exception e ) {
                e.printStackTrace();
            }

            return point.getX();
        }
        return -1;
    }

    /**
     * returns the centroid Y coordinate of the geometry represented by the passed Node
     * 
     * @param node
     * @param targetCRS
     * @return
     */
    public static double getCentroidY( Node node, String targetCRS ) {
        if ( node != null ) {
            Point point = null;
            try {
                if ( "Envelope".equals( node.getLocalName() ) ) {
                    Envelope env = GMLGeometryAdapter.wrapBox( (Element) node, null );
                    point = env.getCentroid();
                } else {
                    Geometry geom = GMLGeometryAdapter.wrap( (Element) node, null );
                    point = geom.getCentroid();
                }
                GeoTransformer gt = new GeoTransformer( targetCRS );
                point = (Point) gt.transform( point );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return point.getY();
        }
        return -1;
    }

    public static String getCurveCoordinates( Node node ) {
        StringBuffer sb = new StringBuffer( 10000 );
        try {
            List<Node> list = XMLTools.getNodes( node, ".//gml:posList | gml:pos | gml:coordinates", nsc );
            for ( Node node2 : list ) {
                String s = XMLTools.getStringValue( node2 ).trim();
                if ( node2.getLocalName().equals( "posList" ) ) {
                    String[] sl = StringTools.toArray( s, " ", false );
                    int dim = XMLTools.getNodeAsInt( node2, "./@srsDimension", nsc, 2 );
                    for ( int i = 0; i < sl.length; i++ ) {
                        sb.append( sl[i] );
                        if ( ( i + 1 ) % dim == 0 ) {
                            sb.append( ' ' );
                        } else {
                            sb.append( ',' );
                        }
                    }
                } else if ( node2.getLocalName().equals( "pos" ) ) {
                    String[] sl = StringTools.toArray( s, "\t\n\r\f ,", false );
                    for ( int i = 0; i < sl.length; i++ ) {
                        sb.append( sl[i] );
                        if ( i < sl.length - 1 ) {
                            sb.append( ',' );
                        } else {
                            sb.append( ' ' );
                        }
                    }
                } else if ( node2.getLocalName().equals( "coordinates" ) ) {
                    sb.append( s );
                }
            }
        } catch ( XMLParsingException e ) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}