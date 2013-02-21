//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/wac/WACException.java $
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
package org.deegree.portal.wac;

import org.deegree.framework.util.StringTools;
import org.deegree.portal.PortalException;

/**
 * Exception that will be thrown if a <tt>WAClient</tt> could not perform a request against a WSS
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.1, $Revision: 11011 $, $Date: 2008-04-10 09:52:41 +0200 (Do, 10. Apr 2008) $
 * 
 * @since 1.1
 */
public class WACException extends PortalException {

    /**
     * 
     */
    private static final long serialVersionUID = -5415383174863467987L;

    /**
     * 
     */
    public WACException() {
        super( "WACException has been thrown" );
    }

    /**
     * @param msg
     */
    public WACException( String msg ) {
        super( msg );
    }

    /**
     * @param msg
     * @param e
     */
    public WACException( String msg, Exception e ) {
        super( msg + StringTools.stackTraceToString( e ), e );
    }

}