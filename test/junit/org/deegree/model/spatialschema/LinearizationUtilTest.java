//$HeadURL$
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

import junit.framework.TestCase;

/**
 * Unit tests for class {@link LinearizationUtil}.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class LinearizationUtilTest extends TestCase {

    private double getDistance( Position p0, Position p1 ) {
        double dx = p1.getX() - p0.getX();
        double dy = p1.getY() - p0.getY();
        return Math.sqrt( dx * dx + dy * dy );
    }

    private void arePointsOnCircle( Position center, Position p0, Position p1, Position p2 ) {
        double dp0 = getDistance( center, p0 );
        double dp1 = getDistance( center, p1 );
        double dp2 = getDistance( center, p2 );
        assertEquals( dp0, dp1, 1E-9 );
        assertEquals( dp1, dp2, 1E-9 );
    }

    private void testLinearization( Position p0, Position p1, Position p2, int numPositions ) {
        Position center = LinearizationUtil.findCircleCenter( p0, p1, p2 );
        double radius = getDistance( center, p0 );

        Position[] positions = LinearizationUtil.linearizeCircle( p0, p1, p2, numPositions );
        for ( int i = 0; i < positions.length; i++ ) {
            double dist = getDistance( center, positions[0] );
            assertEquals( radius, dist, 1E-9 );
        }
    }

    /**
     * Tests if {@link LinearizationUtil#isClockwise(Position, Position, Position)} determines the correct point order.
     */
    public void testIsClockwise() {
        Position p0 = GeometryFactory.createPosition( -2, 0 );
        Position p1 = GeometryFactory.createPosition( 0, 2 );
        Position p2 = GeometryFactory.createPosition( 2, 0 );
        assertTrue( LinearizationUtil.isClockwise( p0, p1, p2 ) );
        assertFalse( LinearizationUtil.isClockwise( p0, p2, p1 ) );

        p0 = GeometryFactory.createPosition( -2, 0 );
        p1 = GeometryFactory.createPosition( 0, -2 );
        p2 = GeometryFactory.createPosition( 2, 0 );
        assertFalse( LinearizationUtil.isClockwise( p0, p1, p2 ) );
    }

    /**
     * Tests if {@link LinearizationUtil#findCircleCenter(Position, Position, Position)} finds the correct midpoint.
     */
    public void testFindCircleCenter() {
        Position p0 = GeometryFactory.createPosition( 8, -1 );
        Position p1 = GeometryFactory.createPosition( 3, 1.6 );
        Position p2 = GeometryFactory.createPosition( -110, 16.77777 );
        Position center = LinearizationUtil.findCircleCenter( p0, p1, p2 );
        arePointsOnCircle( center, p0, p1, p2 );
    }

    /**
     * Tests if {@link LinearizationUtil#linearizeCircle(Position, Position, Position, int)} produces sequences of
     * positions that coincide with the circle arc.
     */
    public void testLinearizeCircle() {
        testLinearization( GeometryFactory.createPosition( 0, 2 ), GeometryFactory.createPosition( 2, 0 ),
                           GeometryFactory.createPosition( -2, 0 ), 10 );
        testLinearization( GeometryFactory.createPosition( 0, 2 ), GeometryFactory.createPosition( 2, 0 ),
                           GeometryFactory.createPosition( -2, 0 ), 1000 );
        testLinearization( GeometryFactory.createPosition( 8, -1 ), GeometryFactory.createPosition( 3, 1.6 ),
                           GeometryFactory.createPosition( -110, 16.77777 ), 1000 );
        testLinearization( GeometryFactory.createPosition( 8, -1 ), GeometryFactory.createPosition( 3, 1.6 ),
                           GeometryFactory.createPosition( -110, 16.77777 ), 10 );
    }

    /**
     * Tests if start- and end-points of the linearized line strings produced by
     * {@link LinearizationUtil#linearizeArc(Position, Position, Position)} are identical with the input positions.
     */
    public void testLinearizeArc() {
        Position p0 = GeometryFactory.createPosition( 118075.83, 407620.65 );
        Position p1 = GeometryFactory.createPosition( 118082.289, 407621.62 );
        Position p2 = GeometryFactory.createPosition( 118087.099, 407626.038 );
        Position[] positions = LinearizationUtil.linearizeArc( p0, p1, p2, 150 );
        assertEquals( p0.getX(), positions[0].getX() );
        assertEquals( p0.getY(), positions[0].getY() );
        assertEquals( p2.getX(), positions[positions.length - 1].getX() );
        assertEquals( p2.getY(), positions[positions.length - 1].getY() );
    }
}
