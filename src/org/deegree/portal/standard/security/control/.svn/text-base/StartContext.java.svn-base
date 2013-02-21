//$Header: $
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

package org.deegree.portal.standard.security.control;

/**
 * TODO add documentation here
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: elmasri$
 * 
 * @version $Revision: $, $Date: 01-Mar-2007 16:45:38$
 */

public class StartContext {

    /**
     * indicates weather this context the selected one in the list or not
     */
    public static final String DEFAULT_CONTEXT = "DEFAULT_CONTEXT";

    private String contextName;

    private String path;

    boolean isDefault = false;

    /**
     * @param contextName
     * @param path
     * @param isDefault
     */
    public StartContext( String contextName, String path, boolean isDefault ) {
        this.contextName = contextName;
        this.path = path;
        this.isDefault = isDefault;
    }

    /**
     * @return context name
     */
    public String getContextName() {
        return contextName;
    }

    /**
     * @return context path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param contextName
     */
    public void setContextName( String contextName ) {
        this.contextName = contextName;
    }

    /**
     * @param path
     */
    public void setPath( String path ) {
        this.path = path;
    }

    /**
     * 
     * @return boolean
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * @param isDefault
     */
    public void setDefault( boolean isDefault ) {
        this.isDefault = isDefault;
    }
}
