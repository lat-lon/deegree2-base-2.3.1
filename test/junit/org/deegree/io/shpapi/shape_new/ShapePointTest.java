//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/io/shpapi/shape_new/ShapePointTest.java $
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

import org.deegree.model.spatialschema.ByteUtils;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.model.spatialschema.Point;

import junit.framework.TestCase;

/**
 * Test cases for <code>ShapePoint</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9335 $, $Date: 2007-12-27 11:51:08 +0100 (Thu, 27 Dec 2007) $
 */
public class ShapePointTest extends TestCase {

    /**
     * Test for Point.
     */
    public void testPoint() {
        int size = 20;
        byte[] bytes = new byte[size];
        ByteUtils.writeLEInt( bytes, 0, ShapeFile.POINT );
        ByteUtils.writeLEDouble( bytes, 4, 123.123 );
        ByteUtils.writeLEDouble( bytes, 12, 321.321 );
        ShapePoint p = new ShapePoint( false, true );
        p.read( bytes, 0 );
        assertTrue( p.x == 123.123 );
        assertTrue( p.y == 321.321 );
        assertTrue( p.getByteLength() == size );
    }

    /**
     * Test for PointZ.
     */
    public void testPointZ() {
        int size = 36;
        byte[] bytes = new byte[size];
        ByteUtils.writeLEInt( bytes, 0, ShapeFile.POINTZ );
        ByteUtils.writeLEDouble( bytes, 4, 123.123 );
        ByteUtils.writeLEDouble( bytes, 12, 321.321 );
        ByteUtils.writeLEDouble( bytes, 20, 12.12 );
        ByteUtils.writeLEDouble( bytes, 28, 21.21 );
        ShapePoint p = new ShapePoint( true, false );
        p.read( bytes, 0 );
        assertTrue( p.x == 123.123 );
        assertTrue( p.y == 321.321 );
        assertTrue( p.z == 12.12 );
        assertTrue( p.m == 21.21 );
        assertTrue( p.getByteLength() == size );
    }

    /**
     * Test for PointZ and deegree Point.
     */
    public void testPointDeegree() {
        int size = 36;
        Point pt = GeometryFactory.createPoint(123.123, 321.321, 12.12, null );

        ShapePoint p = new ShapePoint( pt );
        
        assertTrue( p.x == 123.123 );
        assertTrue( p.y == 321.321 );
        assertTrue( p.z == 12.12 );
        assertTrue( p.getByteLength() == size );
    }
    
    /**
     * Test for PointM.
     */
    public void testPointM() {
        int size = 28;
        byte[] bytes = new byte[size];
        ByteUtils.writeLEInt( bytes, 0, ShapeFile.POINTM );
        ByteUtils.writeLEDouble( bytes, 4, 123.123 );
        ByteUtils.writeLEDouble( bytes, 12, 321.321 );
        ByteUtils.writeLEDouble( bytes, 20, 333.333 );
        ShapePoint p = new ShapePoint( false, false );
        p.read( bytes, 0 );
        assertTrue( p.x == 123.123 );
        assertTrue( p.y == 321.321 );
        assertTrue( p.m == 333.333 );
        assertTrue( p.getByteLength() == size );
    }

}

/***************************************************************************************************
 * <code>
 Changes to this class. What the people have been up to:

 $Log$
 Revision 1.1  2007/02/26 14:26:49  schmitz
 Added a new implementation of the ShapeFile API that implements the Z and M variants of the datatypes as well.
 Also added some basic tests for the API as well as a new version of the GML/Shape converters.

 </code>
 **************************************************************************************************/
