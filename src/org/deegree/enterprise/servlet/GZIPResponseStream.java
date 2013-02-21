//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/GZIPResponseStream.java $
/*----------------------------------------
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

 Copyright 2003 Jayson Falkner (jayson@jspinsider.com)
 This code is from "Servlets and JavaServer pages; the J2EE Web Tier",
 http://www.jspbook.com.
 ----------------------------------------*/
package org.deegree.enterprise.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *
 *
 * @version $Revision: 12519 $
 * @author <a href="mailto:jayson@jspinsider.com">Jayson Falkner</a>
 * @author last edited by: $Author: aschmitz $
 *
 * @version 1.0. $Revision: 12519 $, $Date: 2008-06-25 11:37:30 +0200 (Mi, 25. Jun 2008) $
 *
 * @since 2.0
 */
public class GZIPResponseStream extends ServletOutputStream {
    /**
     * the output as a byte array
     */
    protected ByteArrayOutputStream baos = null;

    /**
     * the output as a gzipped stream
     */
    protected GZIPOutputStream gzipstream = null;

    /**
     * true if the stream is closed
     */
    protected boolean closed = false;

    /**
     * the response wrapper
     */
    protected HttpServletResponse response = null;

    /**
     * the output as a stream
     */
    protected ServletOutputStream output = null;

    /**
     *
     * @param response
     * @throws IOException
     */
    public GZIPResponseStream( HttpServletResponse response ) throws IOException {
        super();
        closed = false;
        this.response = response;
        this.output = response.getOutputStream();
        baos = new ByteArrayOutputStream();
        gzipstream = new GZIPOutputStream( baos );
    }

    /**
     * @throws IOException
     */
    @Override
    public void close()
                            throws IOException {
        if ( closed ) {
            throw new IOException( "This output stream has already been closed" );
        }
        gzipstream.finish();

        byte[] bytes = baos.toByteArray();

        response.addHeader( "Content-Length", Integer.toString( bytes.length ) );
        response.addHeader( "Content-Encoding", "gzip" );
        output.write( bytes );
        output.flush();
        output.close();
        closed = true;
    }

    /**
     * @throws IOException
     */
    @Override
    public void flush()
                            throws IOException {
        if ( closed ) {
            throw new IOException( "Cannot flush a closed output stream" );
        }
        gzipstream.flush();
    }

    /**
     * @param b
     *            data to write
     * @throws IOException
     */
    @Override
    public void write( int b )
                            throws IOException {
        if ( closed ) {
            throw new IOException( "Cannot write to a closed output stream" );
        }
        gzipstream.write( (byte) b );
    }

    /**
     * @param b
     *            data array to write
     * @throws IOException
     */
    @Override
    public void write( byte b[] )
                            throws IOException {
        write( b, 0, b.length );
    }

    /**
     * @param b
     *            data array to write
     * @param off
     *            index of the for byte
     * @param len
     *            number of bytes to write
     * @throws IOException
     */
    @Override
    public void write( byte b[], int off, int len )
                            throws IOException {
        System.out.println( "writing..." );
        if ( closed ) {
            throw new IOException( "Cannot write to a closed output stream" );
        }
        gzipstream.write( b, off, len );
    }

    /**
     *
     * @return true if already has been closed
     */
    public boolean closed() {
        return ( this.closed );
    }

    /**
     *
     *
     */
    public void reset() {
        // noop
    }
}