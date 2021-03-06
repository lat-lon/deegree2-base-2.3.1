//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/ogcwebservices/wass/wss/WSServiceTest.java $
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
package org.deegree.ogcwebservices.wass.wss;

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
import org.deegree.ogcwebservices.wass.wss.capabilities.WSSCapabilities;

/**
 * General test class for the WSS.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 2.0, $Revision: 11907 $, $Date: 2008-05-26 19:36:11 +0200 (Mo, 26. Mai 2008) $
 * 
 * @since 2.0
 */

public class WSServiceTest extends TestCase {

    private static final ILogger LOG = LoggerFactory.getLogger( WSServiceTest.class );

    private WSService service;

    private final String resourceLocation = "./resources/wass/wss/example/deegree/";

    /**
     * Constructor for WSServiceTest.
     * 
     * @param arg0
     */
    public WSServiceTest( String arg0 ) {
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
            // hardcoded, but not to the local file system ;-)
            File file = new File( resourceLocation + "example_wss_capabilities.xml" );
            URL url = file.toURL();
            WASServiceFactory.setConfiguration( url );
            service = WASServiceFactory.getUncachedWSService();
            LOG.logInfo( "Setting up WSS...done." );
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
        return new TestSuite( WSServiceTest.class );
    }

    /**
     * @throws Exception
     */
    public void testGetCapabilities()
                            throws Exception {
        assertNotNull( "Service not initialized", service );
        WSSCapabilities capabilities = (WSSCapabilities) service.getCapabilities();
        assertNotNull( "Capabilities are null", capabilities );
        XMLFragment xml = XMLFactory.export( capabilities );
        assertNotNull( "XMLFragment is null", xml );
        xml.prettyPrint( System.out );
    }

}