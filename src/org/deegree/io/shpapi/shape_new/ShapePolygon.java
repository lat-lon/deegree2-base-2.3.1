//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/shpapi/shape_new/ShapePolygon.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

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
package org.deegree.io.shpapi.shape_new;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.spatialschema.ByteUtils;
import org.deegree.model.spatialschema.Curve;
import org.deegree.model.spatialschema.Geometry;
import org.deegree.model.spatialschema.GeometryException;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.model.spatialschema.Position;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * <code>ShapePolygon</code> corresponds to the Polygon, PolygonM and PolygonZ shapes of the
 * shapefile spec.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: otonnhofer $
 * 
 * @version $Revision: 9734 $, $Date: 2008-01-24 11:43:21 +0100 (Do, 24. Jan 2008) $
 */
public class ShapePolygon extends ShapePolyline {

    private boolean normalized;

    /**
     * Creates a new Polygon/M/Z.
     * 
     * @param z
     * @param m
     */
    public ShapePolygon( boolean z, boolean m ) {
        super( z, m );
    }
    
    /**
     * Creates a new Polygon/M/Z.
     * 
     * @param z
     * @param m
     * @param crs
     *            CoordinateSystem of the shape
     */
    public ShapePolygon( boolean z, boolean m, CoordinateSystem crs ) {
        super( z, m, crs );
    }
    /**
     * Creates a PolygonZ from deegree Curves.
     * 
     * @param curves
     */
    public ShapePolygon( List<Curve> curves ) {
        super( curves );
    }

    private Coordinate[] getAsCoordinates( ShapePoint[] ps ) {
        Coordinate[] cs = new Coordinate[ps.length];

        for ( int i = 0; i < cs.length; ++i ) {
            cs[i] = ps[i].export();
        }

        return cs;
    }

    /**
     * Normalizes the orientation etc. of this polygon as required by the shapefile spec. This means
     * (to my understanding), that the outer ring runs clockwise while the inner rings run
     * counter-clockwise.
     */
    public void normalize() {
        if ( normalized ) {
            return;
        }

        normalized = true;

        Coordinate[] cs = getAsCoordinates( points[0] );

        if ( CGAlgorithms.isCCW( cs ) ) {
            List<ShapePoint> list = Arrays.asList( points[0] );
            Collections.reverse( list );
            points[0] = list.toArray( new ShapePoint[points[0].length] );
        }

        for ( int i = 1; i < points.length; ++i ) {
            cs = getAsCoordinates( points[i] );
            if ( !CGAlgorithms.isCCW( cs ) ) {
                List<ShapePoint> list = Arrays.asList( points[i] );
                Collections.reverse( list );
                points[i] = list.toArray( new ShapePoint[points[i].length] );
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.io.shpapi.Shape#read(byte[], int)
     */
    @Override
    public int read( byte[] bytes, int offset ) {
        int off = offset;

        int type = ByteUtils.readLEInt( bytes, off );
        off += 4;

        if ( type == ShapeFile.NULL ) {
            return off;
        }

        if ( type == ShapeFile.POLYGON ) {
            return readPolyline( bytes, off );
        }

        if ( type == ShapeFile.POLYGONZ ) {
            return readPolylineZ( bytes, off );
        }

        if ( type == ShapeFile.POLYGONM ) {
            return readPolylineM( bytes, off );
        }

        return -1;
    }

    /**
     * Note that the normalize method will be called automatically, if it has not been called
     * before.
     * 
     * @see org.deegree.io.shpapi.shape_new.Shape#write(byte[], int)
     */
    @Override
    public int write( byte[] bytes, int offset ) {
        if ( !normalized ) {
            normalize();
        }
        if ( isZ ) {
            ByteUtils.writeLEInt( bytes, offset, ShapeFile.POLYGONZ );
            return writePolylineZ( bytes, offset + 4 );
        }
        if ( isM ) {
            ByteUtils.writeLEInt( bytes, offset, ShapeFile.POLYGONM );
            return writePolylineM( bytes, offset + 4 );
        }
        ByteUtils.writeLEInt( bytes, offset, ShapeFile.POLYGON );
        return writePolyline( bytes, offset + 4 );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.io.shpapi.shape_new.Shape#getType()
     */
    @Override
    public int getType() {
        if ( isZ ) {
            return ShapeFile.POLYGONZ;
        }
        if ( isM ) {
            return ShapeFile.POLYGONM;
        }
        return ShapeFile.POLYGON;
    }

    /**
     * This creates a Surface object.
     */
    @Override
    public Geometry getGeometry()
                            throws ShapeGeometryException {
        if ( points == null ) {
            return null;
        }
        try {
            Position[] outer = new Position[points[0].length];

            Position[][] inner = new Position[points.length - 1][];

            for ( int i = 0; i < points[0].length; ++i ) {
                if ( isZ ) {
                    outer[i] = GeometryFactory.createPosition( points[0][i].x, points[0][i].y, points[0][i].z );
                } else {
                    outer[i] = GeometryFactory.createPosition( points[0][i].x, points[0][i].y );
                }
            }

            for ( int k = 1; k < points.length; ++k ) {
                inner[k - 1] = new Position[points[k].length];
                for ( int i = 0; i < points[k].length; ++i ) {
                    if ( isZ ) {
                        inner[k - 1][i] = GeometryFactory.createPosition( points[k][i].x, points[k][i].y,
                                                                          points[k][i].z );
                    } else {
                        inner[k - 1][i] = GeometryFactory.createPosition( points[k][i].x, points[k][i].y );
                    }
                }
            }

            // not sure if interpolation can be null
            return GeometryFactory.createSurface( outer, inner, null, crs );
        } catch ( GeometryException e ) {
            throw new ShapeGeometryException( "Surface could not be constructed from Polygon.", e );
        }
    }

}
