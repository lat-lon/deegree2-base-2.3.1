//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/control/WebEvent.java $
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

// $Id: WebEvent.java 11727 2008-05-15 09:39:23Z otonnhofer $
package org.deegree.enterprise.control;

// JDK 1.3
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author <a href="mailto:tfriebe@gmx.net">Torsten Friebe</a>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * 
 * @version $Revision: 11727 $
 */
public class WebEvent extends EventObject implements FormEvent {
    /**
     * 
     */
    private static final long serialVersionUID = 706936738051288390L;

    /**
     * Creates a new WebEvent object.
     * 
     * @param request
     */
    public WebEvent( HttpServletRequest request ) {
        super( request );
    }

    /**
     * @return the parameters
     */
    public Properties getParameter() {
        return this._getParameters( this._getRequest() );
    }

    /**
     * @return the document path
     */
    public String getDocumentPath() {
        return this._getRequest().getRequestURI();
    }

    /**
     * @return the request user
     */
    public RequestUser getRequestUser() {
        return this._getRequestUser( this._getRequest() );
    }

    @Override
    public String toString() {
        return this.getClass().getName().concat( " [ " ).concat( getDocumentPath() ).concat( " ]" );
    }

    /**
     * Returns a list of Properties with key value pairs created out of the incoming POST paramteres.
     */
    private Properties _getParameters( HttpServletRequest request ) {
        Properties p = new Properties();

        for ( Enumeration<?> e = request.getParameterNames(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            p.setProperty( key, request.getParameter( key ) );
        }

        return p;
    }

    private RequestUser _getRequestUser( HttpServletRequest request ) {
        return new RequestUser( request );
    }

    private HttpServletRequest _getRequest() {
        return (HttpServletRequest) getSource();
    }

    /** @link dependency */

    /* #RequestUser lnkRequestUser; */
}