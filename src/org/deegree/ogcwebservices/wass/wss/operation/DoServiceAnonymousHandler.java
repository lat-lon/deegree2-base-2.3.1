//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/wss/operation/DoServiceAnonymousHandler.java $
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

package org.deegree.ogcwebservices.wass.wss.operation;

import org.deegree.ogcwebservices.wass.common.AuthenticationData;
import org.deegree.ogcwebservices.wass.exceptions.DoServiceException;

/**
 * Enables the possibily for a client to make an anonymous DoService Request to the wss.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 2.0, $Revision: 10503 $, $Date: 2008-03-06 17:42:41 +0100 (Thu, 06 Mar 2008) $
 * 
 * @since 2.0
 */

public class DoServiceAnonymousHandler extends DoServiceHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.ogcwebservices.wass.wss.operation.DoServiceHandler#handleRequest(org.deegree.ogcwebservices.wass.wss.operation.DoService)
     */
    @Override
    public void handleRequest( DoService request )
                            throws DoServiceException {
        
        AuthenticationData authData = request.getAuthenticationData();
        if ( authData.usesAnonymousAuthentication() ) {
            /*
             * This was intentended to be a - mandatory email kind of thing.
             */
//            if ( !( "" ).equals( authData.getCredentials().trim() ) ) {
//                LOG.logError( "#handleRequest no anonymous name presented" );
//                throw new DoServiceException( "WSS: the given sessionid is unknown or expired.\n" );
//            }
            setRequestAllowed( true );
        }
        
    }

}
