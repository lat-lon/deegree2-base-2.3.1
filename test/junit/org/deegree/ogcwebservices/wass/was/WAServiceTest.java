//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/ogcwebservices/wass/was/WAServiceTest.java $
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
package org.deegree.ogcwebservices.wass.was;

import java.io.File;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.ogcwebservices.wass.common.WASServiceFactory;
import org.deegree.ogcwebservices.wass.common.XMLFactory;
import org.deegree.ogcwebservices.wass.was.capabilities.WASCapabilities;

/**
 * This is the general test class for the new WAService implementation.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 2.0, $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Tue, 01 Apr 2008) $
 * 
 * @since 2.0
 */

public class WAServiceTest extends TestCase {

    private static final ILogger LOG = LoggerFactory.getLogger( WAServiceTest.class );

    private WAService service;

    /**
     * Constructor for WAServiceTest.
     * 
     * @param arg0
     */
    public WAServiceTest( String arg0 ) {
        super( arg0 );
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp()
                            throws Exception {
        super.setUp();
        try {
            fail( "Setting up WAS -- fixme" );
//            File file = new File( "./resources/wass/was/example/deegree/example_was_capabilities.xml" );
//            URL url = file.toURL();
//            WASServiceFactory.setConfiguration( url );
//            service = WASServiceFactory.getUncachedWAService();
//            LOG.logInfo( "Setting up WAS...done." );
        } catch ( Exception e ) {
            throw e;
        }
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown()
                            throws Exception {
        super.tearDown();
    }

    /**
     * @return the Test
     */
    public static Test suite() {
        return new TestSuite( WAServiceTest.class );
    }

    /**
     * @throws Exception
     */
    public void testGetCapabilities()
                            throws Exception {
        fail( "Not testing GetCapabilities -- fixme" );
//        assertNotNull( "Service not initialized", service );
//        WASCapabilities capabilities = (WASCapabilities) service.getCapabilities();
//        assertNotNull( "Capabilities are null", capabilities );
//        XMLFragment xml = XMLFactory.export( capabilities );
//        assertNotNull( "XMLFragment is null", xml );
//        xml.prettyPrint( System.out );
    }

    /*
     * public void _testAuthenticate() throws Exception { assertNotNull("Service not initialized",
     * service); HashMap<String, String> map = new HashMap<String, String>(); map.put( "VERSION",
     * "1.0.0" ); map.put( "ID", "754h" ); map.put( "METHOD",
     * "urn:oasis:names:tc:SAML:1.0:am:password" ); map.put( "CREDENTIALS", "SEC_ADMIN,JOSE67" );
     * Authenticate auth = Authenticate.create( map ); Object o = service.doService( auth );
     * assertTrue( o instanceof Session );
     * 
     * o = service.doService( auth ); assertTrue( o instanceof Session ); }
     */

    /*
     * public void _testDescribeUser() throws Exception { assertNotNull("Service not initialized",
     * service); Map map = new HashMap(); map.put( "VERSION", "1.0.0" ); map.put( "ID", "754h" );
     * map.put( "METHOD", "urn:oasis:names:tc:SAML:1.0:am:password" ); map.put( "CREDENTIALS",
     * "SEC_ADMIN,JOSE67" ); Authenticate auth = Authenticate.create( map ); Session session =
     * (Session)service.doService( auth ); map.put( "VERSION", "1.0.0" ); map.put( "ID", "754h" );
     * map.put( "METHOD", "urn:latlon:names:sessionid" ); map.put( "CREDENTIALS",
     * session.getSessionID().getId() );
     * 
     * DescribeUser du = DescribeUser.create( map ); User user = (User)service.doService( du );
     * assertEquals( user.getName(), "SEC_ADMIN" ); assertEquals( user.getPassword(), "JOSE67" ); }
     */

}