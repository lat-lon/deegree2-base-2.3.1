//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/ogcwebservices/wms/configuration/WMSConfigurationDocumentTest.java $
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
package org.deegree.ogcwebservices.wms.configuration;

import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.InvalidConfigurationException;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.ogcwebservices.wms.XMLFactory;
import org.deegree.ogcwebservices.wms.capabilities.WMSCapabilities;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import alltests.Configuration;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: rbezema $
 * @version 1.0. $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Di, 01. Apr 2008) $
 * @since 2.0
 */
public class WMSConfigurationDocumentTest extends TestCase {
    private static ILogger LOG = LoggerFactory.getLogger( WMSConfigurationDocumentTest.class );

    /**
     * @return the test
     */
    public static Test suite() {
        return new TestSuite( WMSConfigurationDocumentTest.class );
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
    public WMSConfigurationDocumentTest( String arg0 ) {
        super( arg0 );
    }

    /**
     * 
     */
    public void testCreateEmptyDocument() {
        try {
            WMSConfigurationDocument doc = new WMSConfigurationDocument();
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
     * @throws MalformedURLException
     * @throws SAXException
     * @throws IOException
     * @throws InvalidConfigurationException
     * @throws XMLParsingException
     */
    public void testGetCapabilities()
                            throws MalformedURLException, SAXException, IOException, InvalidConfigurationException {
        WMSConfigurationDocument doc = new WMSConfigurationDocument();
        doc.load( Configuration.getWMSConfigurationURL() );
        WMSCapabilities conf = doc.parseConfiguration();
        XMLFragment xml = XMLFactory.export( conf );
        xml.write( System.out );
        // assertNotNull(capabilities);
        // assertEquals("1.1.1", capabilities.getVersion());
    }

    /**
     * @throws IOException
     * 
     */
    public void testXML2JavaAndBack()
                            throws IOException {

        LOG.logInfo( "loading " + Configuration.getWMSConfigurationURL().toString() );

    }

}