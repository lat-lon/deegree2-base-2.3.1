//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/io/datastore/sql/postgis/PGgeometryAdapterTest.java $
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
package org.deegree.io.datastore.sql.postgis;

import junit.framework.TestCase;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.model.crs.CRSFactory;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.crs.UnknownCRSException;
import org.deegree.model.spatialschema.Curve;
import org.deegree.model.spatialschema.GeometryException;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.model.spatialschema.MultiCurve;
import org.deegree.model.spatialschema.MultiPoint;
import org.deegree.model.spatialschema.MultiSurface;
import org.deegree.model.spatialschema.Point;
import org.deegree.model.spatialschema.Position;
import org.deegree.model.spatialschema.Surface;
import org.postgis.PGgeometry;

/**
 * Bla bla bla...
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Tue, 01 Apr 2008) $
 * 
 */
public class PGgeometryAdapterTest extends TestCase {
    
    private static ILogger LOG = LoggerFactory.getLogger( PGgeometryAdapterTest.class );

    private final static int EPSG_CS_NUMBER = 4326;

    private final static String EPSG_CS_NAME = "EPSG:" + EPSG_CS_NUMBER;

    private CoordinateSystem cs;

    public void setUp() {
        try {
            cs = CRSFactory.create( EPSG_CS_NAME );
        } catch ( UnknownCRSException e ) {
            LOG.logError( e.getLocalizedMessage(), e );
            throw new RuntimeException( e );
        }
    }

    public void testPoint()
                            throws GeometryException {
        Point point = GeometryFactory.createPoint( 2581400, 5618780, cs );
        PGgeometry pgGeometry = PGgeometryAdapter.export( point, EPSG_CS_NUMBER );
        assertEquals( org.postgis.Geometry.POINT, pgGeometry.getGeoType() );
        assertEquals( EPSG_CS_NUMBER, pgGeometry.getGeometry().getSrid() );
        Point point2 = (Point) PGgeometryAdapter.wrap( pgGeometry, cs );
        assertEquals( point, point2 );
    }

    public void testMultiPoint()
                            throws GeometryException {
        Point point1 = GeometryFactory.createPoint( 2581400, 5618780, cs );
        Point point2 = GeometryFactory.createPoint( 2581300, 5618680, cs );
        MultiPoint multiPoint = GeometryFactory.createMultiPoint( new Point[] { point1, point2 } );
        PGgeometry pgGeometry = PGgeometryAdapter.export( multiPoint, EPSG_CS_NUMBER );
        assertEquals( org.postgis.Geometry.MULTIPOINT, pgGeometry.getGeoType() );
        assertEquals( EPSG_CS_NUMBER, pgGeometry.getGeometry().getSrid() );
        MultiPoint multiPoint2 = (MultiPoint) PGgeometryAdapter.wrap( pgGeometry, cs );
        assertEquals( multiPoint, multiPoint2 );
    }

    public void testCurve()
                            throws GeometryException {
        Position[] positions = new Position[4];
        positions[0] = GeometryFactory.createPosition( 2581400, 5618780 );
        positions[1] = GeometryFactory.createPosition( 2581300, 5618680 );
        positions[2] = GeometryFactory.createPosition( 2581200, 5618580 );
        positions[3] = GeometryFactory.createPosition( 2581100, 5618480 );
        Curve curve = GeometryFactory.createCurve( positions, this.cs );
        PGgeometry pgGeometry = PGgeometryAdapter.export( curve, EPSG_CS_NUMBER );
        assertEquals( org.postgis.Geometry.LINESTRING, pgGeometry.getGeoType() );
        assertEquals( EPSG_CS_NUMBER, pgGeometry.getGeometry().getSrid() );
        Curve curve2 = (Curve) PGgeometryAdapter.wrap( pgGeometry, cs );
        assertEquals( curve, curve2 );
    }

    public void testMultiCurve()
                            throws GeometryException {
        Position[] positions1 = new Position[4];
        positions1[0] = GeometryFactory.createPosition( 2581400, 5618780 );
        positions1[1] = GeometryFactory.createPosition( 2581300, 5618680 );
        positions1[2] = GeometryFactory.createPosition( 2581200, 5618580 );
        positions1[3] = GeometryFactory.createPosition( 2581100, 5618480 );
        Curve curve1 = GeometryFactory.createCurve( positions1, this.cs );
        Position[] positions2 = new Position[2];
        positions2[0] = GeometryFactory.createPosition( 2581200, 5618580 );
        positions2[1] = GeometryFactory.createPosition( 2581100, 5618480 );
        Curve curve2 = GeometryFactory.createCurve( positions2, this.cs );
        Position[] positions3 = new Position[5];
        positions3[0] = GeometryFactory.createPosition( 2581400, 5618780 );
        positions3[1] = GeometryFactory.createPosition( 2581300, 5618680 );
        positions3[2] = GeometryFactory.createPosition( 2581200, 5618580 );
        positions3[3] = GeometryFactory.createPosition( 2581100, 5618480 );
        positions3[4] = GeometryFactory.createPosition( 2581300, 5618680 );
        Curve curve3 = GeometryFactory.createCurve( positions3, this.cs );
        MultiCurve multiCurve = GeometryFactory.createMultiCurve( new Curve[] { curve1, curve2, curve3 } );
        PGgeometry pgGeometry = PGgeometryAdapter.export( multiCurve, EPSG_CS_NUMBER );
        assertEquals( org.postgis.Geometry.MULTILINESTRING, pgGeometry.getGeoType() );
        assertEquals( EPSG_CS_NUMBER, pgGeometry.getGeometry().getSrid() );
        MultiCurve multiCurve2 = (MultiCurve) PGgeometryAdapter.wrap( pgGeometry, cs );
        // TODO: implement and use MultiCurve.equals() here
        assertEquals( multiCurve.toString(), multiCurve2.toString() );
    }

    public void testSurface()
                            throws GeometryException {
        Surface surface = createTestSurface1();
        PGgeometry pgGeometry = PGgeometryAdapter.export( surface, EPSG_CS_NUMBER );
        assertEquals( org.postgis.Geometry.POLYGON, pgGeometry.getGeoType() );
        assertEquals( EPSG_CS_NUMBER, pgGeometry.getGeometry().getSrid() );
        Surface surface2 = (Surface) PGgeometryAdapter.wrap( pgGeometry, cs );
        assertEquals( surface, surface2 );
    }

    public void testMultiSurface()
                            throws GeometryException {
        MultiSurface multiSurface = GeometryFactory.createMultiSurface( new Surface[] { createTestSurface1(),
                                                                                       createTestSurface2() } );
        PGgeometry pgGeometry = PGgeometryAdapter.export( multiSurface, EPSG_CS_NUMBER );
        assertEquals( org.postgis.Geometry.MULTIPOLYGON, pgGeometry.getGeoType() );
        assertEquals( EPSG_CS_NUMBER, pgGeometry.getGeometry().getSrid() );
        PGgeometryAdapter.wrap( pgGeometry, cs );
        // TODO: implement and use MultiSurface.equals() here
        // assertEquals(multiSurface, multiSurface2);
    }

    // Creates a simple test surface (one patch) with one interior ring
    private Surface createTestSurface1()
                            throws GeometryException {
        double[] exteriorRingOrdinates = new double[] { 2581000.0, 5618000.0, 2580950.0, 5618050.0, 2581000.0,
                                                       5618100.0, 2581100.0, 5618100.0, 2581100.0, 5618000.0,
                                                       2581000.0, 5618000.0 };
        double[] interiorRingOrdinates = new double[] { 2581025.0, 5618025.0, 2581075.0, 5618025.0, 2581075.0,
                                                       5618075.0, 2581025.0, 5618075.0, 2581025.0, 5618025.0 };
        double[][] interiorRingsOrdinates = new double[][] { interiorRingOrdinates };
        return GeometryFactory.createSurface( exteriorRingOrdinates, interiorRingsOrdinates, 2, this.cs );
    }

    // Creates a simple test surface (one patch) with two interior rings
    private Surface createTestSurface2()
                            throws GeometryException {
        double[] exteriorRingOrdinates = new double[] { 2586000.0, 5618000.0, 2585950.0, 5618050.0, 2586000.0,
                                                       5618100.0, 2586100.0, 5618100.0, 2586100.0, 5618000.0,
                                                       2586000.0, 5618000.0 };
        double[] interiorRing1Ordinates = new double[] { 2586025.0, 5618025.0, 2586075.0, 5618025.0, 2586075.0,
                                                        5618075.0, 2586025.0, 5618075.0, 2586025.0, 5618025.0 };
        double[] interiorRing2Ordinates = new double[] { 2586010.0, 5618010.0, 2586020.0, 5618010.0, 2586015.0,
                                                        5618015.0, 2586010.0, 5618010.0 };
        double[][] interiorRingsOrdinates = new double[][] { interiorRing1Ordinates, interiorRing2Ordinates };
        return GeometryFactory.createSurface( exteriorRingOrdinates, interiorRingsOrdinates, 2, this.cs );
    }
}
