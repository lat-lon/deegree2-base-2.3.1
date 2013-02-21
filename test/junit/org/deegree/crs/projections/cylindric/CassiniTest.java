//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/crs/projections/cylindric/CassiniTest.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2007 by:
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

package org.deegree.crs.projections.cylindric;

import static org.deegree.crs.projections.ProjectionUtils.DecMinSecToRadians;

import javax.vecmath.Point2d;

import org.deegree.crs.Identifiable;
import org.deegree.crs.components.Unit;
import org.deegree.crs.exceptions.ProjectionException;
import org.deegree.crs.projections.ProjectionTest;
import org.deegree.crs.projections.conic.LambertConformalConic;

/**
 * The <code>CassiniTest</code> class uses the transverse mercator projection, which results in mm error margins.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 14689 $, $Date: 2008-11-04 17:39:08 +0100 (Di, 04. Nov 2008) $
 * 
 */
public class CassiniTest extends ProjectionTest {

    private static final TransverseMercator projection_19996 = new TransverseMercator(
                                                                                       true,
                                                                                       geographic_4314,
                                                                                       10000,
                                                                                       40000,
                                                                                       new Point2d(
                                                                                                    Math.toRadians( 13.6272037 ),
                                                                                                    Math.toRadians( 52.4186483 ) ),
                                                                                       Unit.METRE, 1.0,
                                                                                       new Identifiable( "EPSG:19996" ) );

    // 13.1206370000, 52.3800912000
    /**
     * reference point given by vodafone, verified with proj4 (node the cass projection): <code>
     * proj -f "%.8f" +proj=cass +ellps=bessel +lon_0=13.6272037 +lat_0=52.4186483 +x_0=40000 +y_0=10000
     * 13.201769444444446 52.63358666666667
     * 11199.99849768  34000.0006299
     * 
     *  </code>
     * 
     * @throws ProjectionException
     */
    public void testAccuracy()
                            throws ProjectionException {
        // Thanx to VDF for the coordinates
        Point2d sourcePoint = new Point2d( DecMinSecToRadians( 13.120637 ), DecMinSecToRadians( 52.3800912 ) );

        Point2d targetPoint = new Point2d( 11200.0, 34000.0 );
        doForwardAndInverse( projection_19996, sourcePoint, targetPoint );

    }

    /**
     * tests the consistency of the {@link LambertConformalConic} projection.
     */
    public void testConsistency() {
        consistencyTest( projection_19996, 10000, 40000, new Point2d( Math.toRadians( 13.6272037 ),
                                                                      Math.toRadians( 52.4186483 ) ), Unit.METRE, 1,
                         true, false, "transverseMercator" );
    }

}
