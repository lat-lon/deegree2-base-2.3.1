//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/ogcwebservices/wmps/capabilities/WMPSCapabilitiesDocumentTest.java $
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

package org.deegree.ogcwebservices.wmps.capabilities;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.ogcwebservices.wms.capabilities.Layer;
import org.w3c.dom.Node;

import alltests.AllTests;
import alltests.Configuration;

/**
 * Test class for wmps Capabilities Document
 * 
 * @author <a href="mailto:deshmukh@lat-lon.de">Anup Deshmukh</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Di, 01. Apr 2008) $
 * 
 */

public class WMPSCapabilitiesDocumentTest extends TestCase {
    private static ILogger LOG = LoggerFactory.getLogger( WMPSCapabilitiesDocumentTest.class );
    public static Test suite() {
        return new TestSuite( WMPSCapabilitiesDocumentTest.class );
    }

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

    /**
     * Constructor for GetCoverageTest
     * 
     * @param arg0
     */
    public WMPSCapabilitiesDocumentTest( String arg0 ) {
        super( arg0 );
    }

    public void testCreateEmptyDocument() {
        try {
            WMPSCapabilitiesDocument doc = new WMPSCapabilitiesDocument();
            doc.createEmptyDocument();
            Node rootNode = doc.getRootElement();
            assertNotNull( rootNode );
            assertEquals( "WMT_PS_Capabilities", rootNode.getNodeName() );
        } catch ( Exception e ) {
            LOG.logError( "Unit test failed", e );
            fail( "Error: " + e.getMessage() );
        }
    }

    public void testGetCapabilities() throws Exception {
        try {
            WMPSCapabilitiesDocument doc = new WMPSCapabilitiesDocument();
            doc.load( Configuration.getWMPSConfigurationURL() );
            WMPSCapabilities capabilities = (WMPSCapabilities) doc.parseCapabilities();
            assertNotNull( capabilities );
            assertEquals( "1.1.1", capabilities.getVersion() );
            Layer layer = capabilities.getLayer().getLayer()[0];
            assertNotNull( layer );
        } catch ( Exception e ) {
            LOG.logError( e.getLocalizedMessage(), e );
            throw e;
        }
    }
}