//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/ServletRequestWrapper.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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

package org.deegree.enterprise.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.CharsetUtils;
import org.deegree.framework.util.StringTools;

/**
 * TODO describe function and usage of the class here.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: mays$
 * 
 * @version $Revision: 11768 $, $Date: 23.05.2007 18:09:52$
 */
public class ServletRequestWrapper extends HttpServletRequestWrapper {

    private static ILogger LOG = LoggerFactory.getLogger( ServletRequestWrapper.class );

    private static final String BUNDLE_NAME = "org.deegree.enterprise.servlet.ServletRequestWrapper";

    /**
     * The resource to load the users from.
     */
    static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

    private HttpServletRequest origReq = null;

    private byte[] bytes = null;

    private Map<String, Object> paramMap;

    private String queryString;

    /**
     * @param request
     */
    public ServletRequestWrapper( HttpServletRequest request ) {
        super( request );

        this.origReq = request;

        ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
        try {
            InputStream is = origReq.getInputStream();
            int c = 0;
            while ( ( c = is.read() ) > -1 ) {
                bos.write( c );
            }
            bytes = bos.toByteArray();
            LOG.logDebug( "The constructor created a new bytearray in the HttpServletRequestWrapper" );
        } catch ( IOException ioe ) {
            LOG.logError(
                          "An error occured while creating a byte-buffered inputstream from the HttpServletRequest inputstream because: "
                                                  + ioe.getMessage(), ioe );
            bytes = null;
        }
        queryString = request.getQueryString();
    }

    // /**
    // * creates a new ServletInputStream with a copy of the content of the original one
    // *
    // * @return
    // * @throws IOException
    // */
    // private ServletInputStream createInputStream()
    // throws IOException {
    //
    // if ( bytes == null ) {
    // LOG.logDebug( "Creating new bytearray in the HttpServletRequestWrapper" );
    // ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
    // InputStream is = origReq.getInputStream();
    // int c = 0;
    // while ( ( c = is.read() ) > -1 ) {
    // bos.write( c );
    // }
    // bytes = bos.toByteArray();
    // }
    //
    // return new ProxyServletInputStream( new ByteArrayInputStream( bytes ), bytes.length );
    // }

    @Override
    public Map getParameterMap() {
        if ( paramMap == null ) {
            paramMap = super.getParameterMap();
        }
        return paramMap;
    }

    @Override
    public String getParameter( String key ) {
        if ( paramMap == null ) {
            paramMap = super.getParameterMap();
        }
        Object o = paramMap.get( key );
        String tmp = null;
        if ( o != null && o.getClass() == String[].class ) {
            tmp = StringTools.arrayToString( (String[]) o, ',' );
        } else {
            tmp = (String) o;
        }
        return tmp;
    }

    @Override
    public String[] getParameterValues( String arg0 ) {
        if ( paramMap == null ) {
            paramMap = super.getParameterMap();
        }
        Object o = paramMap.get( arg0 );
        if ( o instanceof String ) {
            return new String[] { (String) o };
        }
        return (String[]) o;
    }

    /**
     * 
     * @param param
     */
    public void setParameter( Map<String, String> param ) {
        this.paramMap = new HashMap<String, Object>( param.size() );

        Iterator<String> iter = param.keySet().iterator();
        StringBuffer sb = new StringBuffer( 500 );
        while ( iter.hasNext() ) {
            String key = iter.next();
            String value = param.get( key );
            sb.append( key ).append( '=' ).append( value );
            if ( iter.hasNext() ) {
                sb.append( '&' );
            }
            this.paramMap.put( key, StringTools.toArray( value, ",", false ) );
        }
        this.queryString = sb.toString();
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    /**
     * sets the content of the inputstream returned by the
     * 
     * @see #getReader() and the
     * @see #getInputStream() method as a byte array. Calling this method will override the content
     *      that may has been read from the <code>HttpServletRequest</code> that has been passed
     *      to the constructor
     * 
     * @param b
     */
    public void setInputStreamAsByteArray( byte[] b ) {
        LOG.logDebug( "ServletRequestWrapper: setting inputstream#byteArray to given bytearra" );
        this.bytes = b;
    }

    @Override
    public BufferedReader getReader()
                            throws IOException {
        return new BufferedReader( new InputStreamReader( getInputStream(), CharsetUtils.getSystemCharset() ) );
    }

    /**
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream()
                            throws IOException {
        if ( bytes == null ) {
            LOG.logDebug( "Creating new bytearray in the HttpServletRequestWrapper#getInputStream" );
            ByteArrayOutputStream bos = new ByteArrayOutputStream( 10000 );
            InputStream is = origReq.getInputStream();
            int c = 0;
            while ( ( c = is.read() ) > -1 ) {
                bos.write( c );
            }
            bytes = bos.toByteArray();
        }

        return new ProxyServletInputStream( new ByteArrayInputStream( bytes ), bytes.length );
    }

    @Override
    public Principal getUserPrincipal() {
        if ( origReq.getUserPrincipal() != null ) {
            return origReq.getUserPrincipal();
        }
        return new Principal() {
            public String getName() {
                return RESOURCE_BUNDLE.getString( "defaultuser" );
            }
        };

    }

    // ///////////////////////////////////////////////////////////////////////
    // inner classes //
    // ///////////////////////////////////////////////////////////////////////

    /**
     * @author Administrator
     * 
     * TODO To change the template for this generated type comment go to Window - Preferences - Java -
     * Code Style - Code Templates
     */
    private class ProxyServletInputStream extends ServletInputStream {

        private BufferedInputStream buffered;

        /**
         * @param in
         *            the InputStream which will be buffered.
         * @param length
         */
        public ProxyServletInputStream( InputStream in, int length ) {
            if ( length > 0 )
                buffered = new BufferedInputStream( in, length );
            else
                buffered = new BufferedInputStream( in );
        }

        @Override
        public synchronized int read()
                                throws IOException {
            return buffered.read();
        }

        @Override
        public synchronized int read( byte b[], int off, int len )
                                throws IOException {
            return buffered.read( b, off, len );
        }

        @Override
        public synchronized long skip( long n )
                                throws IOException {
            return buffered.skip( n );
        }

        @Override
        public synchronized int available()
                                throws IOException {
            return buffered.available();
        }

        @Override
        public synchronized void mark( int readlimit ) {
            buffered.mark( readlimit );
        }

        @Override
        public synchronized void reset()
                                throws IOException {
            buffered.reset();
        }

        @Override
        public boolean markSupported() {
            return buffered.markSupported();
        }

        @Override
        public void close()
                                throws IOException {
            buffered.close();
        }
    }

}
