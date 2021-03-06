//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/crs/projections/azimuthal/StereographicTest.java $
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

package org.deegree.crs.projections.azimuthal;

import javax.vecmath.Point2d;

import org.deegree.crs.components.Axis;
import org.deegree.crs.components.GeodeticDatum;
import org.deegree.crs.components.Unit;
import org.deegree.crs.coordinatesystems.GeographicCRS;
import org.deegree.crs.exceptions.ProjectionException;
import org.deegree.crs.projections.ProjectionTest;
import org.deegree.crs.transformations.helmert.Helmert;

/**
 * <code>StereographicAlternativeTest</code> test the stereographic azimuthal projection as it was defined by snyder.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 14991 $, $Date: 2008-11-20 17:09:43 +0100 (Do, 20. Nov 2008) $
 * 
 */
public class StereographicTest extends ProjectionTest {

    // the source and target crs are not correct, but what the hack
    private static final Helmert wgs_56 = new Helmert( 565.04, 49.91, 465.84, -0.40941295127179994, 0.3608190255680464,
                                                       -1.8684910003505757, 4.0772, GeographicCRS.WGS84,
                                                       GeographicCRS.WGS84, new String[] { "TOWGS_56" } );

    private static final GeodeticDatum datum_171 = new GeodeticDatum( ellipsoid_7004, wgs_56,
                                                                      new String[] { "DATUM_171" } );

    private static final GeographicCRS geographic_204 = new GeographicCRS( datum_171,
                                                                           new Axis[] {
                                                                                       new Axis( "longitude",
                                                                                                 Axis.AO_EAST ),
                                                                                       new Axis( "latitude",
                                                                                                 Axis.AO_NORTH ) },
                                                                           new String[] { "GEO_CRS_204" } );

    private static final StereographicAzimuthal projection_28992 = new StereographicAzimuthal(
                                                                                               geographic_204,
                                                                                               463000.0,
                                                                                               155000.0,
                                                                                               new Point2d(
                                                                                                            Math.toRadians( 5.38763888888889 ),
                                                                                                            Math.toRadians( 52.15616055555555 ) ),
                                                                                               Unit.METRE, 0.9999079 );

    /**
     * reference point created with proj4 command : <code>
     * proj -f "%.8f" +proj=stere +ellps=bessel +lon_0=5.38763888888889 +lat_0=52.15616055555555 +k=0.9999079 +x_0=155000 +y_0=463000.0 
     * 6.610765 53.235916
     * 236660.95112449 583829.78324175
     * </code>
     * 
     * @throws IllegalArgumentException
     * @throws ProjectionException
     */
    public void testAccuracy()
                            throws IllegalArgumentException, ProjectionException {

        Point2d sourcePoint = new Point2d( Math.toRadians( 6.610765 ), Math.toRadians( 53.235916 ) );
        Point2d targetPoint = new Point2d( 236660.95112449, 583829.78324175 );

        doForwardAndInverse( projection_28992, sourcePoint, targetPoint );
    }

    /**
     * tests the consistency of the {@link StereographicAlternative} projection.
     */
    public void testConsistency() {
        consistencyTest( projection_28992, 463000, 155000, new Point2d( Math.toRadians( 5.38763888888889 ),
                                                                        Math.toRadians( 52.15616055555555 ) ),
                         Unit.METRE, 0.9999079, true, false, "stereographicAzimuthal" );
    }

}
