//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/SimpleProxyServlet.java $
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.deegree.enterprise.WebUtils;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.CharsetUtils;
import org.deegree.framework.util.KVP2Map;
import org.deegree.framework.util.StringTools;
import org.deegree.i18n.Messages;
import org.deegree.ogcwebservices.OGCRequestFactory;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;

/**
 * simple proxy servlet The servlet is intended to run in its own context combined with ServletFilter (e.g.
 * OWSProxyServletFilter ) to filter out invalid requests/responses
 * 
 * @version $Revision: 15057 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 1.0. $Revision: 15057 $, $Date: 2008-11-24 18:34:25 +0100 (Mo, 24. Nov 2008) $
 * 
 * @since 1.1
 */
public class SimpleProxyServlet extends HttpServlet {

    private ILogger LOG = LoggerFactory.getLogger( SimpleProxyServlet.class );

    private static final long serialVersionUID = 3086952074808203858L;

    private Map<String, String> host = null;

    /**
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init()
                            throws ServletException {
        super.init();
        host = new HashMap<String, String>();
        Enumeration<String> enu = getInitParameterNames();
        while ( enu.hasMoreElements() ) {
            String pn = enu.nextElement();
            String[] tmp = StringTools.toArray( pn, ":", false );
            String hostAddr = this.getInitParameter( pn );
            host.put( tmp[0], hostAddr );
        }
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
                            throws ServletException, IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            String query = request.getQueryString();
            String service = getService( KVP2Map.toMap( query ) );
            String hostAddr = host.get( service );
            String req = hostAddr + "?" + query;
            URL url = new URL( req );
            LOG.logDebug( "forward URL: " + url );

            URLConnection con = url.openConnection();
            con.setDoInput( true );
            con.setDoOutput( false );
            is = con.getInputStream();
            response.setContentType( con.getContentType() );
            response.setCharacterEncoding( con.getContentEncoding() );
            os = response.getOutputStream();
            int read = 0;
            byte[] buf = new byte[16384];
            if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
                while ( ( read = is.read( buf ) ) > -1 ) {
                    os.write( buf, 0, read );
                    LOG.logDebug( new String( buf, 0, read, con.getContentEncoding() ) );
                }
            } else {
                while ( ( read = is.read( buf ) ) > -1 ) {
                    os.write( buf, 0, read );
                }
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            response.setContentType( "text/plain; charset=" + CharsetUtils.getSystemCharset() );
            os.write( StringTools.stackTraceToString( e ).getBytes() );
        } finally {
            try {
                is.close();
            } catch ( Exception e ) {
                // try to do what ?
            }
            try {
                os.close();
            } catch ( Exception e ) {
                // try to do what ?
            }
        }
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost( HttpServletRequest origReq, HttpServletResponse response )
                            throws ServletException, IOException {
        // wrap request to enable access of the requests InputStream more
        // than one time
        ServletRequestWrapper request = new ServletRequestWrapper( origReq );
        OutputStream os = null;
        try {
            if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
                // because this is an expensive operation it just will
                // performed if debug level is set too DEBUG
                InputStream reqIs = request.getInputStream();
                StringBuffer sb = new StringBuffer( 10000 );
                int c = 0;
                while ( ( c = reqIs.read() ) > -1 ) {
                    sb.append( (char) c );
                }
                reqIs.close();
                LOG.logDebug( "Request: " + sb );
            }
            OGCWebServiceRequest req = OGCRequestFactory.create( request );

            String hostAddr = host.get( req.getServiceName() );
            LOG.logDebug( "forward URL: " + hostAddr );
            if ( hostAddr == null ) {
                throw new Exception( Messages.getMessage( "PROXY_SERVLET_UNDEFINED_HOST", req.getServiceName() ) );
            }

            // determine charset for setting request content type
            // use system charset if no charset can be determined
            // from incoming request
            String charset = origReq.getCharacterEncoding();
            LOG.logDebug( "request character encoding: ", charset );
            if ( charset == null ) {
                charset = CharsetUtils.getSystemCharset();
                LOG.logDebug( "use sytem character encoding: ", charset );
            }

            HttpClient client = new HttpClient();
            client = WebUtils.enableProxyUsage( client, new URL( hostAddr ) );
            PostMethod post = new PostMethod( hostAddr );
            post.setRequestHeader( "Content-type", "text/xml; charset=" + charset );
            post.setRequestEntity( new InputStreamRequestEntity( request.getInputStream() ) );
            client.executeMethod( post );

            LOG.logDebug( "Content-type: ", post.getResponseHeader( "Content-type" ) );

            os = response.getOutputStream();
            os.write( post.getResponseBody() );
        } catch ( Exception e ) {
            e.printStackTrace();
            response.setContentType( "text/plain; charset=" + CharsetUtils.getSystemCharset() );
            os.write( StringTools.stackTraceToString( e ).getBytes() );
        } finally {
            try {
                os.close();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the name of the service that is targeted by the passed KVP encoded request
     * 
     * @param map
     * @throws Exception
     */
    private String getService( Map<String, String> map )
                            throws Exception {
        String service = null;
        String req = map.get( "REQUEST" );
        if ( "WMS".equals( map.get( "SERVICE" ) ) || req.equals( "GetMap" ) || req.equals( "GetFeatureInfo" )
             || req.equals( "GetLegendGraphic" ) ) {
            service = "WMS";
        } else if ( "WFS".equals( map.get( "SERVICE" ) ) || req.equals( "DescribeFeatureType" )
                    || req.equals( "GetFeature" ) ) {
            service = "WFS";
        } else if ( "WCS".equals( map.get( "SERVICE" ) ) || req.equals( "GetCoverage" )
                    || req.equals( "DescribeCoverage" ) ) {
            service = "WCS";
        } else if ( "CSW".equals( map.get( "SERVICE" ) ) || req.equals( "GetRecords" ) || req.equals( "GetRecordById" )
                    || req.equals( "Harvest" ) || req.equals( "DescribeRecord" ) ) {
            service = "CSW";
        } else {
            throw new OGCWebServiceException( "unknown service/request: " + map.get( "SERVICE" ) );
        }
        return service;
    }

}
