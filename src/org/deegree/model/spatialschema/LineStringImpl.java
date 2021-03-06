//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/spatialschema/LineStringImpl.java $
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
 53115 Bonn
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
package org.deegree.model.spatialschema;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.GeometryUtils;
import org.deegree.model.crs.CoordinateSystem;

/**
 * default implementation of the LineString interface of package deegree.model.spatialschema.
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 14248 $, $Date: 2008-10-08 09:34:43 +0200 (Mi, 08. Okt 2008) $
 */
public class LineStringImpl extends CurveSegmentImpl implements LineString {
    /** Use serialVersionUID for interoperability. */
    private final static long serialVersionUID = 8093549521711824076L;

    private static final ILogger LOG = LoggerFactory.getLogger( LineStringImpl.class );

    private double length = -1;

    /**
     * Creates a new LineStringImpl object.
     * 
     * @param gmps
     * @param cs
     * 
     * @throws GeometryException
     */
    protected LineStringImpl( Position[] gmps, CoordinateSystem cs ) throws GeometryException {
        super( gmps, cs );
    }

    @Override
    public Object clone() {
        CurveSegment cs = null;

        Position[] tmp = new Position[points.length];
        for ( int i = 0; i < tmp.length; i++ ) {
            tmp[i] = new PositionImpl( points[i].getX(), points[i].getY(), points[i].getZ() );
        }

        try {
            cs = new LineStringImpl( tmp, getCoordinateSystem() );
        } catch ( Exception ex ) {
            LOG.logError( "LineString_Impl.clone: ", ex );
        }

        return cs;
    }

    private void calcLength() {
        double d = 0;
        for ( int i = 0; i < points.length - 1; i++ ) {
            d += GeometryUtils.distance( points[i], points[i + 1] );
        }
        length = d;
    }

    /**
     * @return length of the curve in units of the related spatial reference system
     */
    public double getLength() {
        if ( length < 0 ) {
            calcLength();
        }
        return length;
    }

    /**
     * returns a reference to itself
     */
    public LineString getAsLineString()
                            throws GeometryException {
        return this;
    }

    /**
     * The Boolean valued operation "intersects" shall return TRUE if this Geometry intersects another Geometry. Within
     * a Complex, the Primitives do not intersect one another. In general, topologically structured data uses shared
     * geometric objects to capture intersection information.
     */
    public boolean intersects( Geometry gmo ) {
        boolean inter = false;

        try {
            if ( gmo instanceof Point ) {
                double tolerance = ( (Point) gmo ).getTolerance();
                inter = LinearIntersects.intersects( ( (Point) gmo ).getPosition(), this, tolerance );
            } else if ( gmo instanceof Curve ) {
                CurveSegment[] cs = new CurveSegment[] { this };
                inter = LinearIntersects.intersects( (Curve) gmo, new CurveImpl( cs ) );
            } else if ( gmo instanceof Surface ) {
                CurveSegment[] cs = new CurveSegment[] { this };
                inter = LinearIntersects.intersects( new CurveImpl( cs ), (Surface) gmo );
            } else if ( gmo instanceof MultiPrimitive ) {
                inter = intersectsMultiPrimitive( (MultiPrimitive) gmo );
            }
        } catch ( Exception e ) {
            LOG.logError( "", e );
        }

        return inter;
    }

    /**
     * the operations returns true if the submitted multi primitive intersects with the curve segment
     */
    private boolean intersectsMultiPrimitive( MultiPrimitive mprim )
                            throws Exception {
        boolean inter = false;

        int cnt = mprim.getSize();

        for ( int i = 0; i < cnt; i++ ) {
            if ( intersects( mprim.getPrimitiveAt( i ) ) ) {
                inter = true;
                break;
            }
        }

        return inter;
    }

    @Override
    public boolean contains( Geometry gmo ) {
        return false;
    }
}