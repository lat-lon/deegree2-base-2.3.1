//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/SecurityConfigurationException.java $
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
package org.deegree.security;

/**
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.1, $Revision: 13924 $, $Date: 2008-09-15 10:12:49 +0200 (Mo, 15. Sep 2008) $
 * 
 * @since 1.1
 * 
 */
public class SecurityConfigurationException extends Exception {

    private static final long serialVersionUID = 8213364034636799878L;

    /**
     * 
     */
    public SecurityConfigurationException() {
        super();
    }

    /**
     * @param arg0
     */
    public SecurityConfigurationException( String arg0 ) {
        super( arg0 );
    }

    /**
     * @param arg0
     * @param arg1
     */
    public SecurityConfigurationException( String arg0, Throwable arg1 ) {
        super( arg0, arg1 );
    }

    /**
     * @param arg0
     */
    public SecurityConfigurationException( Throwable arg0 ) {
        super( arg0 );
    }

}