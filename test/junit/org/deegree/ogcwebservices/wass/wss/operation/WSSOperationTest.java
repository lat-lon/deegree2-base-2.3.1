//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/ogcwebservices/wass/wss/operation/WSSOperationTest.java $
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

package org.deegree.ogcwebservices.wass.wss.operation;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.httpclient.Header;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.ogcwebservices.wass.common.CloseSession;
import org.deegree.ogcwebservices.wass.common.GetSession;
import org.deegree.ogcwebservices.wass.common.SessionOperationsDocument;
import org.deegree.ogcwebservices.wass.common.WASServiceFactory;
import org.deegree.ogcwebservices.wass.common.XMLFactory;
import org.deegree.ogcwebservices.wass.wss.WSService;
import org.deegree.ogcwebservices.wass.wss.capabilities.WSSCapabilities;
import org.deegree.ogcwebservices.wass.wss.capabilities.WSSCapabilitiesDocument;
import org.deegree.security.session.MemoryBasedSessionManager;
import org.deegree.security.session.Session;
import org.deegree.security.session.SessionID;

/**
 * A <code>WSSOperationTest</code> class test the various operation of a wass.wsservice. The services include
 * <ul>
 * <li>DoService</li>
 * <li>GetSession</li>
 * <li>CloseSession</li>
 * <li>GetCapabilities</li>
 * </ul>
 * 
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version 2.0, $Revision: 10877 $, $Date: 2008-04-01 17:05:11 +0200 (Tue, 01 Apr 2008) $
 * 
 * @since 2.0
 */

public class WSSOperationTest extends TestCase {

    private final String resourceLocation = "resources/wass/wss/example/deegree/";

    private static final ILogger LOG = LoggerFactory.getLogger( WSSOperationTest.class );

    private WSService service;

    // private final String outputPath = "test.xml";

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp()
                            throws Exception {
        super.setUp();
        try {
            LOG.logInfo( "Setting up WSS...Should be fixed." );
            // hardcoded, but not to the local file system ;-)
//            File file = new File( resourceLocation + "example_wss_capabilities.xml" );
//            URL url = file.toURL();
//            WASServiceFactory.setConfiguration( url );
//            service = WASServiceFactory.getUncachedWSService();
            //LOG.logInfo( "Setting up WSS...done." );
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
        return new TestSuite( WSSOperationTest.class );
    }

    /**
     * @param request
     * @throws Exception
     */
    public void doDoService( DoService request )
                            throws Exception {
        if ( request.getAuthenticationData().usesSessionAuthentication() ) {
            MemoryBasedSessionManager sessionManager = MemoryBasedSessionManager.getInstance();
            sessionManager.addSession( new Session( new SessionID( request.getAuthenticationData().getCredentials(),
                                                                   200000 ) ) );
        }
        assertNotNull( "DoService request has not been instantiated", request );
        Object response = service.doService( request );
        assertTrue( "Not an instance of DoService", ( response instanceof DoServiceResponse ) );
        Header[] responseHeaders = ( (DoServiceResponse) response ).getHeaders();
        assertTrue( "headers length is null", responseHeaders.length > 0 );
        InputStream body = ( (DoServiceResponse) response ).getResponseBody();

        XMLFragment frag = new XMLFragment();
        frag.load( new InputStreamReader( body ), "http://127.0.0.1/" );
        frag.prettyPrint( System.out );
    }

    /**
     * @throws Exception
     */
    public void testDoService()
                            throws Exception {
        fail( "Not Testing DoService with xml_examplefiles -- fixme" );
        // assertNotNull( "Service not initialized", service );
        // DoServiceDocument doc = new DoServiceDocument();
        // assertNotNull( "Could not create empty doservice document", doc );
        // File[] doServiceExampleFiles = new File( resourceLocation + "requests/DoService/" ).listFiles();
        // for( File f : doServiceExampleFiles ){
        // if( f.isFile() ){
        // LOG.logInfo( "examplefile:" + f.toURL() );
        // doc.load( f.toURL() );
        // DoService request = doc.parseDoService("1", doc.getRootElement() );
        // doDoService( request );
        // }
        // }
    }

    /**
     * @throws Exception
     */
    public void testDoServiceKVP()
                            throws Exception {
        fail( "Not Testing DoService with kvp -- fixme" );
        // LOG.logInfo( "\n\nTesting DoService with kvp" );
        // HashMap<String, String> map = new HashMap<String, String>();
        // map.put( "SERVICE", "WSS" );
        // map.put( "VERSION", "1.0.0" );
        // map.put( "REQUEST", "DOSERVICE" );
        // map.put( "AUTHMETHOD", "urn:x-gdi-nrw:authnMethod:1.0:session" );
        // map.put( "CREDENTIALS", getValidSessionID() );
        // map.put( "SERVICEREQUEST", "SERVICE=WFS&REQUEST=GetCapabilities" );
        // map.put( "DCP", "HTTP_GET" );
        // map.put( "REQUESTPARAMS", "Mime-Type" );
        // map.put( "REQUESTPARAMVALUES", "text/xml" );
        // map.put( "FACADEURL", "http://localhost:8080/VIEL_BLUBBER_MIT_NICHTS" );
        // DoService request = new DoService( "1", map );
        // doDoService( request );
    }

    /**
     * @param request
     * @throws Exception
     */
    public void doGetSession( GetSession request )
                            throws Exception {
        assertNotNull( "GetSession request has not been instantiated", request );
        String id = service.doService( request ).toString();
        assertNotNull( "Didn't get a Session ID", id );
    }

    /**
     * @throws Exception
     */
    public void testGetSession()
                            throws Exception {
        fail( "Not testing GetSession -- fixme" );
        // LOG.logInfo( "\n\nTesting GetSession with xml_examplefile" );
        // assertNotNull( "Service not initialized", service );
        // SessionOperationsDocument doc = new SessionOperationsDocument();
        // assertNotNull( "Could not create empty GetSession document", doc );
        // File xml = new File( resourceLocation + "requests/GetSession/GetSessionExample_1.xml" );
        // doc.load( xml.toURL() );
        // GetSession request = doc.parseGetSession( "1", doc.getRootElement() );
        // doGetSession( request );
    }

    /**
     * @throws Exception
     */
    public void testGetSessionKVP()
                            throws Exception {
        fail( "Not testing GetSession with kvp -- fixme" );
        // fail( "Testing GetSession with kvp" );
        // HashMap<String, String> map = new HashMap<String, String>();
        // map.put( "SERVICE", "WSS" );
        // map.put( "VERSION", "1.0.0" );
        // map.put( "REQUEST", "GETSESSION" );
        // map.put( "AUTHMETHOD", "urn:x-gdi-nrw:authnMethod:1.0:password" );
        // map.put( "CREDENTIALS", "poth,poth" );
        // GetSession request = new GetSession( "1", map );
        // doGetSession( request );
    }

    /**
     * @param request
     * @throws Exception
     */
    public void doCloseSession( CloseSession request )
                            throws Exception {
        assertNotNull( "CloseSession request has not been instantiated", request );
        assertNull( "Session was not closed.", service.doService( request ) );
    }

    /**
     * @throws Exception
     */
    public void testCloseSession()
                            throws Exception {
        fail( "Not testing closeSession  -- fixme" );
        // LOG.logInfo( "\n\nTesting CloseSession with xml_examplefile" );
        // assertNotNull( "Service not initialized", service );
        // SessionOperationsDocument doc = new SessionOperationsDocument();
        // assertNotNull( "Could not create empty session document", doc );
        // File xml = new File( resourceLocation + "requests/CloseSession/CloseSessionExample_1.xml" );
        // doc.load( xml.toURL() );
        // CloseSession request = doc.parseCloseSession( "1", doc.getRootElement() );
        // /*
        // * Just add the read id, which is of course not a "real" test.
        // */
        // MemoryBasedSessionManager sessionManager = MemoryBasedSessionManager.getInstance();
        // sessionManager.addSession( new Session( new SessionID( request.getSessionID(), 200000 ) ) );
        // doCloseSession( request );
    }

    /**
     * @return the session id
     * @throws Exception
     */
    public String getValidSessionID()
                            throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put( "SERVICE", "WSS" );
        map.put( "VERSION", "1.0.0" );
        map.put( "REQUEST", "GETSESSION" );
        map.put( "AUTHMETHOD", "urn:x-gdi-nrw:authnMethod:1.0:password" );
        map.put( "CREDENTIALS", "poth,poth" );
        GetSession request = new GetSession( "1", map );
        return service.doService( request ).toString();
    }

    /**
     * @throws Exception
     */
    public void testCloseSessionKVP()
                            throws Exception {
        fail( "Not testing closeSession with kvp -- fixme" );
        // LOG.logInfo( "\n\nTesting GetSession with kvp" );
        // HashMap<String, String> map = new HashMap<String, String>();
        // map.put( "SERVICE", "WSS" );
        // map.put( "VERSION", "1.0.0" );
        // map.put( "REQUEST", "CLOSESESSION" );
        // map.put( "SESSIONID", getValidSessionID() );
        // CloseSession request = new CloseSession( "0.4", map );
        // doCloseSession( request );
    }

    /**
     * @param request
     * @throws Exception
     */
    public void doGetCapabilities( WSSGetCapabilities request )
                            throws Exception {
        assertNotNull( "GetCapabilities request has not been instantiated", request );
        Object response = service.doService( request );
        assertNotNull( "Response was null", response );
        assertTrue( "Response is not of type WSSCapabilities", response instanceof WSSCapabilities );
        WSSCapabilities cap = (WSSCapabilities) response;
        WSSCapabilitiesDocument resDoc = XMLFactory.export( cap );
        assertNotNull( "Could not parse back the capabilities", resDoc );
        resDoc.prettyPrint( System.out );
    }

    /**
     * @throws Exception
     */
    public void testGetCapabilities()
                            throws Exception {
        fail( "Not testing GetCapabilities -- fixme" );
        // LOG.logInfo( "\n\nTesting GetCapabilities with xml_examplefile" );
        // assertNotNull( "Service not initialized", service );
        // WSSGetCapabilitiesDocument doc = new WSSGetCapabilitiesDocument();
        // assertNotNull( "Could not create empty GetCapabilities document", doc );
        // File xml = new File( resourceLocation
        // + "requests/GetCapabilities/GetCapabilitiesExample_1.xml" );
        // doc.load( xml.toURL() );
        // WSSGetCapabilities request = doc.parseCapabilities( "2222", doc.getRootElement() );
        // doGetCapabilities( request );
    }

    /**
     * @throws Exception
     */
    public void testGetCapabilitiesKVP()
                            throws Exception {
        fail( "Not Testing GetCapabilities with kvp -- fixme" );
        // LOG.logInfo( "\n\nTesting GetCapabilities with kvp" );
        // HashMap<String, String> map = new HashMap<String, String>();
        // map.put( "SERVICE", "WSS" );
        // map.put( "VERSION", "1.0.0" );
        // map.put( "REQUEST", "GETCAPABILITIES" );
    }

}