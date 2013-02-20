//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/common/AuthenticationData.java $
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

package org.deegree.ogcwebservices.wass.common;

/**
 * Encapsulated data: authn:AuthenticationData element
 * 
 * Namespace: http://www.gdi-nrw.org/authentication
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */
public class AuthenticationData {

    private URN authenticationMethod = null;

    private String credentials = null;

    /**
     * @param authenticationMethod
     * @param credentials
     */
    public AuthenticationData( URN authenticationMethod, String credentials ) {
        this.authenticationMethod = authenticationMethod;
        this.credentials = credentials;
    }

    /**
     * @return the Method of authentication
     * @see org.deegree.ogcwebservices.wass.common.URN
     */
    public URN getAuthenticationMethod() {
        return authenticationMethod;
    }

    /**
     * @return the Credentials
     */
    public String getCredentials() {
        return credentials;
    }

    /**
     * @return true, if authenticationMethod is by password
     */
    public boolean usesPasswordAuthentication() {
        return authenticationMethod.isWellformedGDINRW()
               && authenticationMethod.getAuthenticationMethod().equals( "password" );
    }

    /**
     * @return true, if authenticationMethod is by session
     */
    public boolean usesSessionAuthentication() {
        return authenticationMethod.isWellformedGDINRW()
               && authenticationMethod.getAuthenticationMethod().equals( "session" );
    }

    /**
     * @return true, if authenticationMethod is by anonymous
     */
    public boolean usesAnonymousAuthentication() {
        return authenticationMethod.isWellformedGDINRW()
               && authenticationMethod.getAuthenticationMethod().equals( "anonymous" );
    }

    /**
     * @return the username of the credentials or null, if authenticationMethod is not password
     */
    public String getUsername() {
        if ( !usesPasswordAuthentication() ) {
            return null;
        }
        if ( credentials.indexOf( ',' ) > 0 ) {
            return credentials.substring( 0, credentials.indexOf( ',' ) );
        } 
        return credentials;
    }

    /**
     * @return the password of the credentials or null, if authenticationMethod is not password
     */
    public String getPassword() {
        if ( !usesPasswordAuthentication() || credentials.indexOf( ',' ) < 0 ) {
            return null;
        }
        return credentials.substring( credentials.indexOf( ',' ) + 1 );
       
    }

}
