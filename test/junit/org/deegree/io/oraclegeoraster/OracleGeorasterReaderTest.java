// $HeadURL:
// /deegreerepository/deegree/test/org/deegree/io/datastore2/SelectBuilderTest.java,v
// 1.2 2005/05/24 15:37:33 mschneider Exp $
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
package org.deegree.io.oraclegeoraster;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.FileOutputStream;

import junit.framework.TestCase;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.ImageUtils;
import org.deegree.framework.util.StringTools;
import org.deegree.io.JDBCConnection;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;

/**
 * Tests the correct generation of SQL select statements for the example datastore configuration.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Tue, 01 Apr 2008) $
 * 
 */
public class OracleGeorasterReaderTest extends TestCase {
    private static ILogger LOG = LoggerFactory.getLogger( OracleGeorasterReaderTest.class );
    /*
     * @see TestCase#setUp()
     */
    protected void setUp()
                            throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown()
                            throws Exception {
        super.tearDown();
    }

    public void testExportRaster() {
        //LOG.logInfo(  );
        fail("Not testing the geoRaster -- fixme");
//        try {
//            JDBCConnection jdbc = new JDBCConnection( "oracle.jdbc.OracleDriver",
//                                                      "jdbc:oracle:thin:@localhost:1521:latlon",
//                                                      "berlin3d", "berlin3d", null, null, null );
//
//            String table = "raster_relief_IMP";
//            String rdtTable = "raster_relief_IMP_RDT";
//            String column = "rasterPROPERTY";
//            String identification = "ID = 1";
//            int level = 3;
//            GeoRasterDescription grd = new GeoRasterDescription( jdbc, table, rdtTable, column,
//                                                                 identification, level );
//            Envelope env = GeometryFactory.createEnvelope( 27200, 17200, 30400, 19600, null );
//            LOG.logInfo( "HIERE" );
//            RenderedImage ri = GeoRasterReader.exportRaster( grd, env );
//            FileOutputStream fos = new FileOutputStream( "raster.tif" );
//            ImageUtils.saveImage( (BufferedImage) ri, fos, "tif", 1 );
//            fos.close();
//            LOG.logInfo( "HIERE2" );
//
//        } catch ( Exception e ) {
//            //e.printStackTrace();
//            //fail( StringTools.stackTraceToString( e ) );
//            fail( e.getMessage() );
//        }
    }

}
