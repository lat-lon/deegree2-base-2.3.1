//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/WebUtils.java $
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
 53177 Bonn
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
package org.deegree.enterprise;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.deegree.framework.util.CharsetUtils;
import org.deegree.framework.util.StringTools;

/**
 * 
 * 
 * 
 * @version $Revision: 9338 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 9338 $, $Date: 2007-12-27 13:31:31 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */
public class WebUtils {

    /**
     * returns the root path (address - port - context) of a web application read from the passed
     * request<br>
     * e.g. http://myserver:8080/deegree/services?request=GetCapabilites ... <br>
     * will return 'http://myserver:8080/deegree'
     * 
     * @param request
     * @return the root path (address - port - context) of a web application read from the passed
     *         request
     */
    public static String getAbsoluteContextPath( HttpServletRequest request ) {

        String s = request.getRequestURL().toString();
        String[] parts = org.deegree.framework.util.StringTools.toArray( s, "/", false );
        return parts[0] + "//" + parts[1] + request.getContextPath();

    }

    /**
     * reads proxyHost and proxyPort from system parameters and sets them to the passed HttpClient
     * instance
     * 
     * @see HostConfiguration of the passed
     * @see HttpClient
     * @param client
     * @param url
     * @return HttpClient with proxy configuration
     */
    public static synchronized HttpClient enableProxyUsage( HttpClient client, URL url ) {
        String host = url.getHost();
        String protocol = url.getProtocol().toLowerCase();
        HostConfiguration hc = client.getHostConfiguration();
        if ( System.getProperty( "http.proxyHost" ) != null && "http".equals( protocol ) ) {
            String nop = System.getProperty( "http.noProxyHosts" );
            if ( nop != null && nop.indexOf( host ) < 0 ) {
                hc.setProxy( System.getProperty( "http.proxyHost" ),
                             Integer.parseInt( System.getProperty( "http.proxyPort" ) ) );
            }
        } else if ( System.getProperty( "https.proxyHost" ) != null && "https".equals( protocol ) ) {
            String nop = System.getProperty( "https.noProxyHosts" );
            if ( nop != null && nop.indexOf( host ) < 0 ) {
                hc.setProxy( System.getProperty( "https.proxyHost" ),
                             Integer.parseInt( System.getProperty( "https.proxyPort" ) ) );
            }
        } else if ( System.getProperty( "ftp.proxyHost" ) != null && "ftp".equals( protocol ) ) {
            String nop = System.getProperty( "ftp.noProxyHosts" );
            if ( nop != null && nop.indexOf( host ) < 0 ) {
                hc.setProxy( System.getProperty( "ftp.proxyHost" ),
                             Integer.parseInt( System.getProperty( "https.proxyPort" ) ) );
            }
        } else if ( System.getProperty( "proxyHost" ) != null ) {
            String nop = System.getProperty( "noProxyHosts" );
            if ( nop != null && nop.indexOf( host ) < 0 ) {
                hc.setProxy( System.getProperty( "proxyHost" ), Integer.parseInt( System.getProperty( "proxyPort" ) ) );
            }
        }
        client.setHostConfiguration( hc );

        return client;
    }

    /**
     * returns the charset read from the content type of the passed {@link HttpServletRequest}. If
     * no content type or no charset is defined the charset read from {@link CharsetUtils} will be
     * returned instead. So it is gauranteed that the method will not return <code>null</code>
     * 
     * @param request
     * @return charset
     */
    public static String readCharsetFromContentType( HttpServletRequest request ) {

        String contentType = request.getHeader( "Content-Type" );
        return readCharsetFromContentType( contentType );
    }

    /**
     * @see #readCharsetFromContentType(HttpServletRequest)
     * @param contentType
     * @return charset
     */
    public static String readCharsetFromContentType( String contentType ) {
        String charset = null;
        if ( contentType != null ) {
            String[] tmp = StringTools.toArray( contentType, ";", false );
            if ( tmp.length == 2 ) {
                charset = tmp[1].substring( tmp[1].indexOf( '=' ) + 1, tmp[1].length() );
            } else {
                charset = CharsetUtils.getSystemCharset();
            }
        } else {
            charset = CharsetUtils.getSystemCharset();
        }
        return charset;
    }

}