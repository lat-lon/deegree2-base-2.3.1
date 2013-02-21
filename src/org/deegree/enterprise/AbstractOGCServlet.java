//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/AbstractOGCServlet.java $
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

 ----------------------------------------------------------------------------*/
package org.deegree.enterprise;

import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.Marshallable;
import org.deegree.ogcwebservices.OGCWebServiceException;

/**
 * Abstract servlet that serves as an OCC-compliant HTTP-frontend to any OGC-WebService (WFS, WMS,
 * ...).
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9338 $ $Date: 2007-12-27 13:31:31 +0100 (Do, 27. Dez 2007) $
 * 
 * @todo refactoring required, move to package servlet
 */
public abstract class AbstractOGCServlet extends HttpServlet {

    /**
     * Called by the servlet container to indicate that the servlet is being placed into service.
     * Sets the debug level according to the debug parameter defined in the ServletEngine's
     * environment.
     * <p>
     * <p>
     * 
     * @param servletConfig
     *            servlet configuration
     * @throws ServletException
     *             exception if something occurred that interferes with the servlet's normal
     *             operation
     */
    @Override
    public void init( ServletConfig servletConfig )
                            throws ServletException {
        super.init( servletConfig );
    }

    /**
     * handles fatal errors by creating a OGC exception XML and sending it back to the client
     * 
     * @param msg
     * @param ex
     * @param response
     * 
     * @deprecated
     */
    @Deprecated
    protected void handleException( String msg, Exception ex, HttpServletResponse response ) {
        String tmp = StringTools.stackTraceToString( ex );
        getServletContext().log( msg + tmp );
        OGCWebServiceException wex = new OGCWebServiceException( this.getClass().getName(), tmp );
        try {
            PrintWriter pw = response.getWriter();
            pw.write( ( (Marshallable) wex ).exportAsXML() );
            pw.close();
        } catch ( Exception e ) {
            getServletContext().log( e.toString() );
        }
    }
}