//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/spatialschema/SurfaceBoundaryImpl.java $
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

import java.io.Serializable;
import java.util.Arrays;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;

/**
 * default implementation of the SurfaceBoundary interface.
 * 
 * ------------------------------------------------------------
 * 
 * @version 11.6.2001
 * @author Andreas Poth href="mailto:poth@lat-lon.de"
 */

public class SurfaceBoundaryImpl extends PrimitiveBoundaryImpl implements SurfaceBoundary, Serializable {
    /** Use serialVersionUID for interoperability. */
    private final static long serialVersionUID = 1399131144729310956L;

    private static final ILogger LOG = LoggerFactory.getLogger( SurfaceBoundaryImpl.class );

    /**
     * The exterior ring of the surface boundary
     */
    public Ring exterior = null;

    /**
     * The interior ring of the surface boundary
     */
    public Ring[] interior = null;

    /**
     * @param exterior
     * @param interior
     */
    protected SurfaceBoundaryImpl( Ring exterior, Ring[] interior ) {
        super( exterior.getCoordinateSystem() );
        this.exterior = exterior;
        this.interior = interior;
        setValid( false );
    }

    public Ring getExteriorRing() {
        return exterior;
    }

    public Ring[] getInteriorRings() {
        return interior;
    }

    /**
     * @return the boundary of the boundary
     */
    @Override
    public Boundary getBoundary() {
        return null;
    }

    @Override
    public boolean equals( Object other ) {
        if ( !super.equals( other ) || !( other instanceof SurfaceBoundaryImpl ) ) {
            return false;
        }

        if ( !exterior.equals( ( (SurfaceBoundary) other ).getExteriorRing() ) ) {
            return false;
        }

        if ( interior != null ) {
            Ring[] r1 = getInteriorRings();
            Ring[] r2 = ( (SurfaceBoundary) other ).getInteriorRings();

            if ( !Arrays.equals( r1, r2 ) ) {
                return false;
            }
        } else {
            if ( ( (SurfaceBoundary) other ).getInteriorRings() != null ) {
                return false;
            }
        }

        return true;
    }

    /**
     * The operation "dimension" shall return the inherent dimension of this Geometry, which shall be less than or equal
     * to the coordinate dimension. The dimension of a collection of geometric objects shall be the largest dimension of
     * any of its pieces. Points are 0-dimensional, curves are 1-dimensional, surfaces are 2-dimensional, and solids are
     * 3-dimensional.
     */
    public int getDimension() {
        return 1;
    }

    /**
     * The operation "coordinateDimension" shall return the dimension of the coordinates that define this Geometry,
     * which must be the same as the coordinate dimension of the coordinate reference system for this Geometry.
     */
    public int getCoordinateDimension() {
        return exterior.getPositions()[0].getCoordinateDimension();
    }

    @Override
    public Object clone() {
        SurfaceBoundary sb = null;

        try {
            Ring ext = (Ring) ( (RingImpl) getExteriorRing() ).clone();
            Ring[] inn = new Ring[interior.length];

            for ( int i = 0; i < inn.length; i++ ) {
                inn[i] = (Ring) ( (RingImpl) interior[i] ).clone();
            }

            sb = new SurfaceBoundaryImpl( ext, inn );
        } catch ( Exception ex ) {
            LOG.logError( "SurfaceBoundary_Impl.clone: ", ex );
        }

        return sb;
    }

    @Override
    public boolean intersects( Geometry gmo ) {
        boolean inter = exterior.intersects( gmo );

        if ( !inter ) {
            if ( interior != null ) {
                for ( int i = 0; i < interior.length; i++ ) {
                    if ( interior[i].intersects( gmo ) ) {
                        inter = true;
                        break;
                    }
                }
            }
        }

        return inter;
    }

    @Override
    public boolean contains( Geometry gmo ) {
        boolean con = false;

        con = exterior.contains( gmo );

        if ( con ) {
            if ( interior != null ) {
                for ( int i = 0; i < interior.length; i++ ) {
                    if ( interior[i].intersects( gmo ) ) {
                        con = false;
                        break;
                    }
                }
            }
        }

        return con;
    }

    /**
     * The Boolean valued operation "contains" shall return TRUE if this Geometry contains a single point given by a
     * coordinate.
     * <p>
     * </p>
     * dummy implementation
     */
    @Override
    public boolean contains( Position position ) {
        return contains( new PointImpl( position, null ) );
    }

    /**
     * calculates the envelope of the surface boundary
     */
    private void calculateEnvelope() {
        envelope = (Envelope) ( (EnvelopeImpl) exterior.getEnvelope() ).clone();
    }

    /**
     * calculates the centroid of the surface boundary
     */
    private void calculateCentroid() {
        try {
            double[] cen = exterior.getCentroid().getAsArray().clone();
            double cnt = exterior.getAsCurveSegment().getNumberOfPoints();

            for ( int i = 0; i < cen.length; i++ ) {
                cen[i] *= cnt;
            }

            if ( interior != null ) {
                for ( int i = 0; i < interior.length; i++ ) {
                    double[] pos = interior[i].getCentroid().getAsArray();
                    cnt += interior[i].getAsCurveSegment().getNumberOfPoints();

                    for ( int j = 0; j < pos.length; j++ ) {
                        cen[j] += ( pos[j] * interior[i].getAsCurveSegment().getNumberOfPoints() );
                    }
                }
            }

            for ( int j = 0; j < cen.length; j++ ) {
                cen[j] /= cnt;
            }

            centroid = new PointImpl( new PositionImpl( cen ), crs );
        } catch ( Exception ex ) {
            LOG.logError( "", ex );
        }
    }

    @Override
    protected void calculateParam() {
        calculateEnvelope();
        calculateCentroid();
        setValid( true );
    }

    @Override
    public String toString() {
        String ret = null;
        ret = "interior = " + interior + "\n";
        ret += ( "exterior = " + exterior + "\n" );
        return ret;
    }
}