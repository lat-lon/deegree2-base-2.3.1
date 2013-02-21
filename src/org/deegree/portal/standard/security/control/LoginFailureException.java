//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/security/control/LoginFailureException.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 University of Bonn
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

 Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: klaus.greve@uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.portal.standard.security.control;

/**
 * This exception shall be thrown when a session(ID) will be used that has been expired.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: jmays $
 * 
 * @version $Revision: 11975 $, $Date: 2008-05-28 17:26:15 +0200 (Mi, 28. Mai 2008) $
 */
public class LoginFailureException extends Exception {

    private static final long serialVersionUID = 7588803913990047038L;

    /**
     * 
     */
    public LoginFailureException() {
        super();

    }

    /**
     * @param arg0
     */
    public LoginFailureException( String arg0 ) {
        super( arg0 );

    }

    /**
     * @param arg0
     * @param arg1
     */
    public LoginFailureException( String arg0, Throwable arg1 ) {
        super( arg0, arg1 );

    }

    /**
     * @param arg0
     */
    public LoginFailureException( Throwable arg0 ) {
        super( arg0 );

    }
}