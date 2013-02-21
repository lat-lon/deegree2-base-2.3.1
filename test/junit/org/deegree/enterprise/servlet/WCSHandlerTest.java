//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/enterprise/servlet/WCSHandlerTest.java $
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

import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.deegree.enterprise.ServiceException;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.wcs.WCServiceFactory;
import org.deegree.ogcwebservices.wcs.configuration.WCSConfiguration;
import org.deegree.ogcwebservices.wcs.getcapabilities.WCSGetCapabilities;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import alltests.Configuration;

/**
 * Test with mock object to perform http get/post request.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version 2.0, $Revision: 11917 $, $Date: 2008-05-27 12:07:06 +0200 (Di, 27. Mai 2008) $
 * 
 * @since 2.0
 */
public class WCSHandlerTest extends TestCase {

    private static ILogger LOG = LoggerFactory.getLogger( WCSHandlerTest.class );

    private StringWriter writer;

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp()
                            throws Exception {
        super.setUp();
        WCSConfiguration wcsConfiguration = WCSConfiguration.create( Configuration.getWCSConfigurationURL() );
        WCServiceFactory.setConfiguration( wcsConfiguration );
        writer = new StringWriter();
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown()
                            throws Exception {
        super.tearDown();
        writer.flush();
        writer.close();
        writer = null;
    }

    /**
     * Test the perform method with a GetCapabilities request
     * 
     * @throws ServiceException
     * @throws OGCWebServiceException
     * @throws SAXException
     * @throws IOException
     * 
     */
    public void testPerformGetCapabilities()
                            throws OGCWebServiceException, ServiceException, IOException, SAXException {
        WCSHandler handler = new WCSHandler();
        assertNotNull( handler );
        WCSGetCapabilities request = new WCSGetCapabilities( this.toString(), "1.0.0", "0", new String[] {}, null );
        HttpServletResponse response = new MockHttpServletResponse( writer );
        handler.perform( request, response );
        String responseAsXml = writer.toString();
        LOG.logDebug( responseAsXml );
        assertNotNull( responseAsXml );
        assertTrue( responseAsXml.length() > 0 );
        StringReader reader = new StringReader( responseAsXml );
        Document responseAsDom = XMLTools.parse( reader );
        assertNotNull( responseAsDom );
        assertEquals( "WCS_Capabilities", responseAsDom.getDocumentElement().getNodeName() );

    }

    /**
     * @throws OGCWebServiceException
     * @throws ServiceException
     * @throws IOException
     * @throws SAXException
     */
    public void testPerformGetCapabilitiesSection()
                            throws OGCWebServiceException, ServiceException, IOException, SAXException {
        WCSHandler handler = new WCSHandler();
        assertNotNull( handler );
        OGCWebServiceRequest request = new WCSGetCapabilities( this.toString(), "1.0.0", "0",
                                                               new String[] { "/WCS_Capabilities/Service" }, null );
        HttpServletResponse response = new MockHttpServletResponse( writer );
        handler.perform( request, response );
        String responseAsXml = writer.toString();
        LOG.logDebug( responseAsXml );
        assertNotNull( responseAsXml );
        assertTrue( responseAsXml.length() > 0 );
        StringReader reader = new StringReader( responseAsXml );
        Document responseAsDom = XMLTools.parse( reader );
        assertNotNull( responseAsDom );
        assertEquals( "Service", responseAsDom.getDocumentElement().getNodeName() );
    }

    /**
     * @throws ServiceException
     */
    public void testGetService()
                            throws ServiceException {
        WCSHandler handler = new WCSHandler();
        assertNotNull( handler.getService() );
    }

}