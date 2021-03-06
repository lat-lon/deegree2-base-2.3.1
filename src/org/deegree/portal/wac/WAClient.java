//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/wac/WAClient.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 University of Bonn
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

 Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: klaus.greve@uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.portal.wac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.deegree.enterprise.WebUtils;
import org.deegree.framework.util.CharsetUtils;
import org.deegree.framework.xml.XMLFragment;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The class offers methods to enable a using program/class to act as a client to a Web Security
 * Service (WSS) as specified in GDI-NRW. This implementation just supports authentication through
 * sessionIDs and user/password. If other authentication mechanisms are needed this class should be
 * extended by defining additional <tt>performDoService</tt> methods.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 */
public class WAClient {

    private String host = null;

    private String path = null;

    private int port = 443;

    private String contentType = null;

    /**
     * The constructor assumes that the the certificate to be used is set by starting the java VM
     * using -Djavax.net.ssl.trustStore parameter. The port to be used is set to SSL standard 443
     * 
     * @param host
     * @param path
     */
    public WAClient( String host, String path ) {
        this( host, path, 443 );
    }

    /**
     * The constructor assumes that the the certificate to be used is set by starting the java VM
     * using -Djavax.net.ssl.trustStore parameter.
     * 
     * @param host
     * @param path
     * @param port
     */
    public WAClient( String host, String path, int port ) {
        this( host, path, port, null );
    }

    /**
     * 
     * @param host
     * @param path
     * @param port
     * @param trustStore
     */
    public WAClient( String host, String path, int port, String trustStore ) {
        this.host = host;
        this.port = port;
        this.path = path;
        if ( trustStore != null ) {
            System.setProperty( "javax.net.ssl.trustStore", trustStore );
        }
    }

    /**
     * returns the name of the content type of the result to the last performed request
     * 
     * @return name of the content type
     */
    public String getContentType() {
        return contentType;
    }

    private void extractContentType( Header header ) {
        // code taken from deegree1:
        if ( header != null ) {
            HeaderElement[] element = header.getElements();
            if ( element != null && element.length > 0 ) {
                this.contentType = element[0].getValue();
            }
        }
    }

    /**
     * performs a GetCapabilities request against the WSS that is assigned to a client
     * 
     * @return Capabilities document if request was successful otherwise an exception document will
     *         be returned
     * @throws WACException
     */
    public Document performGetCapabilities()
                            throws WACException {
        Document doc;
        try {
            StringBuffer sb = new StringBuffer( 200 );
            sb.append( path ).append( "?service=WSS&request=GetCapabilities&version=1.0.0" );
            HttpClient httpclient = new HttpClient();
            httpclient = WebUtils.enableProxyUsage( httpclient, new URL( path ) );
            EasySSLProtocolSocketFactory fac = new EasySSLProtocolSocketFactory();
            Protocol myhttps = new Protocol( "https", (ProtocolSocketFactory) fac, port );
            httpclient.getHostConfiguration().setHost( host, port, myhttps );
            GetMethod httpget = new GetMethod( sb.toString() );
            httpclient.executeMethod( httpget );
            extractContentType( httpget.getResponseHeader( "" ) );

            XMLFragment xml = new XMLFragment();
            InputStream is = httpget.getResponseBodyAsStream();
            xml.load( is, path );
            is.close();
            doc = xml.getRootElement().getOwnerDocument();
        } catch ( IOException e ) {
            throw new WACException( "can not access WSS", e );
        } catch ( SAXException e ) {
            throw new WACException( "could not parse result from WSS GetCapabilities request", e );
        }
        return doc;
    }

    /**
     * performs a GetSession request against the WSS that is assigned to a client. The method
     * assumed that user/password (urn:x-gdi-nrw:authnMethod:1.0:password) is used for
     * authentication
     * 
     * @param user
     *            name of the user who like to get a session
     * @param password
     *            password of the user
     * @return GetSession result string if request was successful, otherwise ?
     * @throws WACException
     */
    public String performGetSession( String user, String password )
                            throws WACException {
        String s = null;
        try {
            StringBuffer sb = new StringBuffer( 200 );
            sb.append( path ).append( "?service=WSS&request=GetSession&version=1.0.0&" );
            sb.append( "AUTHMETHOD=urn:x-gdi-nrw:authnMethod:1.0:password&" );
            sb.append( "CREDENTIALS=" ).append( user ).append( ',' ).append( password );

            HttpClient httpclient = new HttpClient();
            httpclient = WebUtils.enableProxyUsage( httpclient, new URL( path ) );
            EasySSLProtocolSocketFactory fac = new EasySSLProtocolSocketFactory();
            Protocol myhttps = new Protocol( "https", (ProtocolSocketFactory) fac, port );
            httpclient.getHostConfiguration().setHost( host, port, myhttps );
            GetMethod httpget = new GetMethod( sb.toString() );
            httpclient.executeMethod( httpget );
            extractContentType( httpget.getResponseHeader( "" ) );

            InputStream is = httpget.getResponseBodyAsStream();
            InputStreamReader ireader = new InputStreamReader( is );
            BufferedReader br = new BufferedReader( ireader );
            StringBuffer sb2 = new StringBuffer( 5000 );
            while ( ( s = br.readLine() ) != null ) {
                sb2.append( s );
            }
            s = sb2.toString();
            br.close();
        } catch ( UnknownHostException e ) {
            throw new WACException( "Host: " + host + " is not known. Host must be set without protocol", e );
        } catch ( IOException e ) {
            throw new WACException( "can not access WSS", e );
        }
        return s;
    }

    /**
     * closes a Session by sending a CloseSession request against the WSS that is assigned to a
     * client. If the passed sessionID is not valid an WSS exception document will be returned
     * instead of the success message/answer.
     * 
     * @param sessionID
     * @return document that indicates that session has been closed otherwise an exception document
     *         will be returned
     * @throws WACException
     */
    public Document performCloseSession( String sessionID )
                            throws WACException {
        Document doc;
        try {
            StringBuffer sb = new StringBuffer( 200 );
            sb.append( path ).append( "?service=WSS&request=CloseSession&version=1.0.0&" );
            sb.append( "SESSIONID=" ).append( sessionID );
            HttpClient httpclient = new HttpClient();
            httpclient = WebUtils.enableProxyUsage( httpclient, new URL( path ) );
            EasySSLProtocolSocketFactory fac = new EasySSLProtocolSocketFactory();
            Protocol myhttps = new Protocol( "https", (ProtocolSocketFactory) fac, port );
            httpclient.getHostConfiguration().setHost( host, port, myhttps );
            GetMethod httpget = new GetMethod( sb.toString() );
            httpclient.executeMethod( httpget );
            extractContentType( httpget.getResponseHeader( "" ) );
            XMLFragment xml = new XMLFragment();
            InputStream is = httpget.getResponseBodyAsStream();
            xml.load( is, path );
            is.close();
            doc = xml.getRootElement().getOwnerDocument();
        } catch ( IOException e ) {
            throw new WACException( "can not access WSS", e );
        } catch ( SAXException e ) {
            throw new WACException( "could not parse result from WSS GetCapabilities request", e );
        }
        return doc;
    }

    /**
     * performs a DoService request against the WSS that is assigned to a client. According to the
     * WSS specification the request will be send using HTTP POST.<BR>
     * The method uses a user/password authentification
     * 
     * @see #performDoService(String, String)
     * 
     * @param request
     *            request to perform
     * @param user
     *            name of the user who like to get a session
     * @param password
     *            password of the user
     * @return result of the passed request. the type depends on target service and request
     * @throws WACException
     */
    public InputStream performDoService( String request, String user, String password )
                            throws WACException {
        InputStream is = null;
        try {
            StringBuffer sb = new StringBuffer( 2000 );
            sb.append( path ).append( "?service=WSS&request=DoService&version=1.0.0&" );
            sb.append( "AUTHMETHOD=USERPASSWORD&" );
            sb.append( "CREDENTIALS=" ).append( user ).append( ';' ).append( password );
            sb.append( "&SERVICEREQUEST=" ).append( URLEncoder.encode( request, CharsetUtils.getSystemCharset() ) );
            HttpClient httpclient = new HttpClient();
            httpclient = WebUtils.enableProxyUsage( httpclient, new URL( path ) );
            EasySSLProtocolSocketFactory fac = new EasySSLProtocolSocketFactory();
            Protocol myhttps = new Protocol( "https", (ProtocolSocketFactory) fac, port );
            httpclient.getHostConfiguration().setHost( host, port, myhttps );
            GetMethod httpget = new GetMethod( sb.toString() );
            httpclient.executeMethod( httpget );
            extractContentType( httpget.getResponseHeader( "" ) );
            is = httpget.getResponseBodyAsStream();
        } catch ( IOException e ) {
            throw new WACException( "can not access WSS", e );
        }
        return is;
    }

    /**
     * performs a DoService request against the WSS that is assigned to a client. According to the
     * WSS specification the request will be send using HTTP POST.<BR>
     * The method uses an authentification through a sessionID
     * 
     * @see #performDoService(String, String, String)
     * 
     * @param request
     *            request to perform
     * @param sessionID
     *            id to authentificate a user
     * @return result of the passed request. the type depends on target service and request
     * @throws WACException
     */
    public InputStream performDoService( String request, String sessionID )
                            throws WACException {
        InputStream is = null;
        try {
            StringBuffer sb = new StringBuffer( 2000 );
            sb.append( path ).append( "?service=WSS&request=DoService&version=1.0.0&" );
            sb.append( "AUTHMETHOD=urn:x-gdi-nrw:authnMethod:1.0:session&" );
            sb.append( "DCP=http_get&" );
            sb.append( "CREDENTIALS=" ).append( sessionID ).append( "&" );
            sb.append( "SERVICEREQUEST=" ).append( URLEncoder.encode( request, CharsetUtils.getSystemCharset() ) );

            HttpClient httpclient = new HttpClient();
            httpclient = WebUtils.enableProxyUsage( httpclient, new URL( path ) );
            EasySSLProtocolSocketFactory fac = new EasySSLProtocolSocketFactory();
            Protocol myhttps = new Protocol( "https", (ProtocolSocketFactory) fac, port );
            httpclient.getHostConfiguration().setHost( host, port, myhttps );
            GetMethod httpget = new GetMethod( sb.toString() );
            httpclient.executeMethod( httpget );
            extractContentType( httpget.getResponseHeader( "" ) );
            is = httpget.getResponseBodyAsStream();
        } catch ( IOException e ) {
            throw new WACException( "can not access WSS", e );
        }
        return is;
    }

}
