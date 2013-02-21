//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/model/spatialschema/GeometryTest.java $
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

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.model.crs.CRSFactory;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.crs.UnknownCRSException;

/**
 * 
 *
 * @version $Revision: 10877 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 *
 * @version 1.0. $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Di, 01. Apr 2008) $
 *
 * @since 2.0
 */
public class GeometryTest extends TestCase {
    
    private static ILogger LOG = LoggerFactory.getLogger( GeometryTest.class );
    
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
    
    public void testSurface1() throws Exception {
        Surface s1 = createTestSurface1();
        Surface s2 = createTestSurface1();
        assertEquals( s1, s2 );
    }
    
    public void testSurface2() throws Exception {
        Surface s1 = createTestSurface2();
        Surface s2 = createTestSurface2();
        assertEquals( s1, s2 );
    }
    
    public void testSurface3() throws Exception {
        Surface s1 = createTestSurface1();
        Surface s2 = createTestSurface2();
        assertEquals( s1, s2 );
    }

    /**
     * Creates a simple test surface (one patch) with one interior ring
     * @return
     * @throws GeometryException
     */
    private Surface createTestSurface1() throws GeometryException {
        double[] exteriorRingOrdinates = new double[] { 2581000.0, 5618000.0, 2580950.0, 5618050.0,
                                                       2581000.0, 5618100.0, 2581100.0, 5618100.0,
                                                       2581100.0, 5618000.0, 2581000.0, 5618000.0 };
        double[] interiorRingOrdinates = new double[] { 2581025.0, 5618025.0, 2581075.0, 5618025.0,
                                                       2581075.0, 5618075.0, 2581025.0, 5618075.0,
                                                       2581025.0, 5618025.0 };
        double[][] interiorRingsOrdinates = new double[][] { interiorRingOrdinates };
        return GeometryFactory.createSurface( exteriorRingOrdinates, interiorRingsOrdinates, 2,
            this.cs );
    }
    
    private Surface createTestSurface2() throws GeometryException {
        double[][] exteriorSegs = new double[3][];
        exteriorSegs[0] = new double[] { 2581000.0, 5618000.0, 2580950.0, 5618050.0 };
        exteriorSegs[1] = new double[] { 2581000.0, 5618100.0, 2581100.0, 5618100.0 };
        exteriorSegs[2] = new double[] { 2581100.0, 5618000.0, 2581000.0, 5618000.0 };
        
        CurveSegment[] ecs = new CurveSegment[exteriorSegs.length];
        for ( int i = 0; i < ecs.length; i++ ) {
            Position[] pos = new Position[exteriorSegs[i].length/2];
            int k = 0;
            for ( int j = 0; j < exteriorSegs[i].length/2; j++ ) {
                pos[j] = GeometryFactory.createPosition( exteriorSegs[i][k++], exteriorSegs[i][k++] );
            }            
            ecs[i] = GeometryFactory.createCurveSegment( pos, this.cs );
        }
        
        double[][] interiorSegs = new double[2][];
        interiorSegs[0] = new double[] { 2581025.0, 5618025.0, 2581075.0, 5618025.0 };
        interiorSegs[1] = new double[] { 2581075.0, 5618075.0, 2581025.0, 5618075.0, 2581025.0, 5618025.0 };
        CurveSegment[] ics = new CurveSegment[interiorSegs.length];
        for ( int i = 0; i < ics.length; i++ ) {
            Position[] pos = new Position[interiorSegs[i].length/2];
            int k = 0;
            for ( int j = 0; j < interiorSegs[i].length/2; j++ ) {
                pos[j] = GeometryFactory.createPosition( interiorSegs[i][k++], interiorSegs[i][k++] );
            }
            ics[i] = GeometryFactory.createCurveSegment( pos, this.cs );
        }
        CurveSegment[][] inner = new CurveSegment[1][2];
        inner[0][0] = ics[0];
        inner[0][1] = ics[1];

        SurfacePatch sp = GeometryFactory.createSurfacePatch( ecs, inner, this.cs );
        return GeometryFactory.createSurface( sp );
    }
    
    
}