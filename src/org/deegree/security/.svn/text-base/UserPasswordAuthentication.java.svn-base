//$HeadURL$
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
package org.deegree.security;

import java.util.Map;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;
import org.deegree.security.drm.SecurityAccessManager;
import org.deegree.security.drm.WrongCredentialsException;
import org.deegree.security.drm.model.User;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 6251 $, $Date: 2007-03-19 16:59:28 +0100 (Mo, 19 Mrz 2007) $
 */
public class UserPasswordAuthentication extends AbstractAuthentication {

    private static final ILogger LOG = LoggerFactory.getLogger( UserPasswordAuthentication.class );

    protected static final String AUTH_PARAM_USER = "USER";

    protected static final String AUTH_PARAM_PASSWORD = "PASSWORD";

    /**
     * 
     * @param authenticationName
     * @param initParams
     */
    public UserPasswordAuthentication( String authenticationName, Map<String, String> initParams ) {
        super( authenticationName, initParams );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.security.AbstractAuthentication#authenticate(java.util.Map)
     */
    public User authenticate( Map<String, String> params )
                            throws WrongCredentialsException {
        String user = params.get( AUTH_PARAM_USER );
        String password = params.get( AUTH_PARAM_PASSWORD );

        LOG.logDebug( "USER: ", user );
        LOG.logDebug( "PASSWORD: ", password );
        User usr = null;
        if ( password != null && user != null ) {
            try {
                SecurityAccessManager sam = SecurityAccessManager.getInstance();
                usr = sam.getUserByName( user );
                usr.authenticate( password );
            } catch ( Exception e ) {
                LOG.logError( e.getMessage() );
                throw new WrongCredentialsException( Messages.getMessage( "OWSPROXY_USER_AUTH_ERROR", user ) );
            }
        }

        return usr;
    }

}
