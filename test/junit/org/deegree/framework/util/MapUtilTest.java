//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/framework/util/MapUtilTest.java $
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
package org.deegree.framework.util;

import junit.framework.TestCase;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;

/**
 * 
 * 
 * 
 * @version $Revision: 10877 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Tue, 01 Apr 2008) $
 * 
 * @since 2.0
 */
public class MapUtilTest extends TestCase {

    private static ILogger LOG = LoggerFactory.getLogger( MapUtilTest.class );

    public void testScale1()
                            throws Exception {
        CoordinateSystem crs = new LocalCRS( "m" );
        Envelope bbox = GeometryFactory.createEnvelope( 0, 0, 100, 100, crs );
        double sc = MapUtils.calcScale( 100, 100, bbox, crs, 1 );
        LOG.logInfo( Double.toString(  sc ) );
        assertEquals( sc > 1 - 0.00001, true );
        assertEquals( sc < 1 + 0.00001, true );
    }

    /*
     * required to avoid proj4 depency
     */
    private class LocalCRS extends CoordinateSystem {

        private String units = null;

        public LocalCRS( String units ) {
            super( "crs" );
            this.units = units;
        }

        @Override
        public String getCode() {
            return null;
        }

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public String getIdentifier() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getUnits() {
            return units;
        }

    }

}

/***********************************************************************************************************************
 * <code>
 Changes to this class. What the people have been up to:

 $Log$
 Revision 1.5  2007/02/12 09:40:29  wanhoff
 fixed header and footer

 Revision 1.4  2006/11/27 09:15:54  poth
 JNI integration of proj4 has been removed. The CRS functionality now will be done by native deegree code.

 Revision 1.3  2006/11/02 21:06:21  poth
 bug fix - scale calculation

 Revision 1.2  2006/10/17 20:46:07  poth
 *** empty log message ***

 Revision 1.1  2006/09/26 14:23:20  poth
 inital check in

 Revision 1.1  2006/01/19 21:15:51  mschneider
 Initial version.

 </code>
 **********************************************************************************************************************/
