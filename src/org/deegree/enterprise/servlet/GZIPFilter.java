//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/GZIPFilter.java $
/*
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
 */
package org.deegree.enterprise.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;

/**
 * ServletFilter that enables compression of response content, if the client supports GZIP
 * compression (accept-encoding=gzip ...).
 * 
 * @version $Revision: 10660 $
 * @author <a href="mailto:jayson@jspinsider.com">Jayson Falkner</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 * 
 * @since 2.0
 */
public class GZIPFilter implements Filter {

    private static final ILogger LOG = LoggerFactory.getLogger( GZIPFilter.class );

    /**
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain )
                            throws IOException, ServletException {
        if ( req instanceof HttpServletRequest ) {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            String ae = request.getHeader( "accept-encoding" );
            if ( ae != null && ae.indexOf( "gzip" ) != -1 ) {
                LOG.logDebug( "GZIP supported, compressing." );
                GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper( response );
                chain.doFilter( req, wrappedResponse );
                wrappedResponse.finishResponse();
                return;
            }
            chain.doFilter( req, res );
        }
    }

    /**
     * @param filterConfig
     */
    public void init( FilterConfig filterConfig ) {
        // noop
    }

    /**
     * 
     */
    public void destroy() {
        // noop
    }
}