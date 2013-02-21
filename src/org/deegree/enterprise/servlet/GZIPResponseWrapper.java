//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/GZIPResponseWrapper.java $
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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 *
 *
 * @version $Revision: 12519 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 *
 * @version 1.0. $Revision: 12519 $, $Date: 2008-06-25 11:37:30 +0200 (Mi, 25. Jun 2008) $
 *
 * @since 2.0
 */
public class GZIPResponseWrapper extends HttpServletResponseWrapper {
    /**
     * the original response
     */
    protected HttpServletResponse origResponse = null;

    /**
     * the output as a stream
     */
    protected ServletOutputStream stream = null;

    /**
     * the output as a writer
     */
    protected PrintWriter writer = null;

    /**
     *
     * @param response
     */
    public GZIPResponseWrapper( HttpServletResponse response ) {
        super( response );
        origResponse = response;
    }

    /**
     *
     * @return response stream
     * @throws IOException
     */
    public ServletOutputStream createOutputStream()
                            throws IOException {
        return ( new GZIPResponseStream( origResponse ) );
    }

    /**
     *
     *
     */
    public void finishResponse() {
        try {
            if ( writer != null ) {
                writer.close();
            } else {
                if ( stream != null ) {
                    stream.close();
                }
            }
        } catch ( IOException e ) {
            //notting todo?
        }
    }

    /**
     * @throws IOException
     */
    @Override
    public void flushBuffer()
                            throws IOException {
        stream.flush();
    }

    /**
     * @return ServletOutputStream
     * @throws IOException
     */
    @Override
    public ServletOutputStream getOutputStream()
                            throws IOException {
        if ( writer != null ) {
            throw new IllegalStateException( "getWriter() has already been called!" );
        }

        if ( stream == null )
            stream = createOutputStream();
        return ( stream );
    }

    /**
     * @return PrintWriter
     * @throws IOException
     */
    @Override
    public PrintWriter getWriter()
                            throws IOException {
        if ( writer != null ) {
            return ( writer );
        }

        if ( stream != null ) {
            throw new IllegalStateException( "getOutputStream() has already been called!" );
        }

        stream = createOutputStream();
        writer = new PrintWriter( new OutputStreamWriter( stream, "UTF-8" ) );
        return ( writer );
    }

    @Override
    public void setContentLength( int length ) {
        super.setContentLength( length );
    }
}