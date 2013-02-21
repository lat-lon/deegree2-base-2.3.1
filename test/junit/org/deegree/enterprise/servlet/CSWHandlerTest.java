//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/enterprise/servlet/CSWHandlerTest.java $
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
package org.deegree.enterprise.servlet;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deegree.enterprise.ServiceException;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.csw.CSWFactory;
import org.deegree.ogcwebservices.csw.capabilities.CatalogueGetCapabilities;
import org.deegree.ogcwebservices.csw.configuration.CatalogueConfiguration;
import org.deegree.ogcwebservices.csw.configuration.CatalogueConfigurationDocument;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import alltests.AllTests;
import alltests.Configuration;

/**
 * Test with mock object to perform http get/post request.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version 2.0, $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Di, 01. Apr 2008) $
 * 
 * @since 2.0
 */
public class CSWHandlerTest extends TestCase {
    private static ILogger LOG = LoggerFactory.getLogger( CSWHandlerTest.class );
    private StringWriter writer;

    public static Test suite() {
        return new TestSuite( CSWHandlerTest.class );
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp()
                            throws Exception {
        super.setUp();
        CatalogueConfigurationDocument configDocument = new CatalogueConfigurationDocument();
        configDocument.load( Configuration.getCSWConfigurationURL() );
        LOG.logInfo( Configuration.getCSWConfigurationURL().toExternalForm() );
        CatalogueConfiguration config = configDocument.getConfiguration();
        CSWFactory.setConfiguration( config );
        writer = new StringWriter();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown()
                            throws Exception {
        super.tearDown();
        writer.flush();
        writer.close();
        writer = null;
    }

    public void testPerformGetCapabilities()
                            throws OGCWebServiceException, ServiceException, IOException, SAXException {
        CSWHandler handler = new CSWHandler();
        assertNotNull( handler );
        Map<String, String> kvpRequest = new HashMap<String, String>();
        kvpRequest.put( "ID", this.getName() );
        kvpRequest.put( "VERSION", "2.0.0" );
        kvpRequest.put( "ACCEPTVERSION", "2.0.0" );
        kvpRequest.put( "OUTPUTFORMAT", "text/xml" );
        OGCWebServiceRequest request = CatalogueGetCapabilities.create( kvpRequest );
        assertNotNull( request );
        HttpServletResponse response = new MockHttpServletResponse( writer );
        handler.perform( request, response );
        String responseAsXml = writer.toString();
        LOG.logDebug( responseAsXml );
        assertNotNull( responseAsXml );
        assertTrue( responseAsXml.length() > 0 );
        StringReader reader = new StringReader( responseAsXml );
        Document responseAsDom = XMLTools.parse( reader );
        assertNotNull( responseAsDom );
        assertEquals( "csw:Capabilities", responseAsDom.getDocumentElement().getNodeName() );
        assertEquals( "2.0.0", responseAsDom.getDocumentElement().getAttribute( "version" ) );
    }

}
