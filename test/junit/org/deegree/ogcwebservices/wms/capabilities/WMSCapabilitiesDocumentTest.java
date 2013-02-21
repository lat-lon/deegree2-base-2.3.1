//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/ogcwebservices/wms/capabilities/WMSCapabilitiesDocumentTest.java $
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
package org.deegree.ogcwebservices.wms.capabilities;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.ogcwebservices.InconsistentRequestException;
import org.deegree.ogcwebservices.MissingParameterValueException;
import org.deegree.ogcwebservices.getcapabilities.InvalidCapabilitiesException;
import org.deegree.ogcwebservices.wms.XMLFactory;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import alltests.AllTests;
import alltests.Configuration;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: rbezema $
 * @version 1.0. $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Di, 01. Apr 2008) $
 * @since 2.0
 */
public class WMSCapabilitiesDocumentTest extends TestCase {

    private static ILogger LOG = LoggerFactory.getLogger( WMSCapabilitiesDocumentTest.class );
    /**
     * @return the test
     */
    public static Test suite() {
        return new TestSuite( WMSCapabilitiesDocumentTest.class );
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp()
                            throws Exception {
        super.setUp();
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
     * Constructor for GetCoverageTest
     * 
     * @param arg0
     */
    public WMSCapabilitiesDocumentTest( String arg0 ) {
        super( arg0 );
    }

    /**
     * 
     */
    public void testCreateEmptyDocument() {
        try {
            WMSCapabilitiesDocument doc = new WMSCapabilitiesDocument();
            doc.createEmptyDocument();
            Node rootNode = doc.getRootElement();
            assertNotNull( rootNode );
            assertEquals( "WMT_MS_Capabilities", rootNode.getNodeName() );
        } catch ( Exception e ) {
            LOG.logError( "Unit test failed", e );
            fail( "Error: " + e.getMessage() );
        }
    }

    /**
     * 
     */
    public void testGetCapabilities() {
        try {
            WMSCapabilitiesDocument doc = WMSCapabilitiesDocumentFactory.getWMSCapabilitiesDocument( Configuration.getWMSConfigurationURL() );
            WMSCapabilities capabilities = (WMSCapabilities) doc.parseCapabilities();
            assertNotNull( capabilities );
            assertEquals( "1.1.1", capabilities.getVersion() );
            // assertEquals( "<DEMO WMS>", capabilities.getLayer().getTitle() );
            assertEquals( 12, capabilities.getLayer().getLayer().length );
            Layer layer = capabilities.getLayer().getLayer()[1];
            assertEquals( -180, layer.getLatLonBoundingBox().getMin().getX(), 0.01 );
        } catch ( Exception e ) {
            LOG.logError( e.getLocalizedMessage(), e );
            fail( e.getLocalizedMessage() );
        }
    }

    /**
     * @throws InvalidCapabilitiesException
     * @throws SAXException
     * @throws IOException
     * @throws MissingParameterValueException
     * @throws InconsistentRequestException
     * @throws XMLParsingException
     * 
     */
    public void testXML2JavaAndBack()
                            throws InvalidCapabilitiesException, SAXException, IOException, XMLParsingException {

        LOG.logInfo( "loading " + Configuration.getWMSConfigurationURL().toString() );
        WMSCapabilitiesDocument doc = WMSCapabilitiesDocumentFactory.getWMSCapabilitiesDocument( Configuration.getWMSConfigurationURL() );
        WMSCapabilities capabilities = (WMSCapabilities) doc.parseCapabilities();
        assertNotNull( doc.getRootElement() );
        WMSCapabilitiesDocument cd = XMLFactory.export( capabilities );
    }

}