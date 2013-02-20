//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/common/URN.java $
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
 * Encapsulates a Uniform Resource Name (URN) which encodes an authentication method according to
 * the GDI NRW access control specification.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */
public class URN {

    private String urn;

    /**
     * Creates new one from a String.
     * 
     * @param urn
     *            the string
     */
    public URN( String urn ) {
        this.urn = urn;
    }

    /**
     * Returns the last part of the name, or null, if it is not a wellformed GDI NRW authentication.
     * method URN.
     * 
     * @return the name, or null
     */
    public String getAuthenticationMethod() {
        if ( !isWellformedGDINRW() )
            return null;
        return getLastName();
    }

    /**
     * Returns the last part of the name, or null, if it is not a URN.
     * 
     * @return the last part of this URN
     */
    public String getLastName() {
        if ( urn == null )
            return null;
        if ( !urn.startsWith( "urn:" ) )
            return null;
        return urn.substring( urn.lastIndexOf( ':' ) + 1 );
    }

    /**
     * Returns, whether this is a wellformed GDI NRW authentication method URN.
     * 
     * @return true, if it is
     */
    public boolean isWellformedGDINRW() {
        if ( urn == null )
            return false;
        String lastName = getLastName();
        if ( urn.startsWith( "urn:x-gdi-nrw:authnMethod:1.0:" ) )
            if ( lastName.equalsIgnoreCase( "password" ) || lastName.equalsIgnoreCase( "was" )
                 || lastName.equalsIgnoreCase( "session" )
                 || lastName.equalsIgnoreCase( "anonymous" ) )
                return true;
        return false;
    }
    
    /**
     * @param other
     * @return true if other equals this URN
     */
    public boolean equals ( URN other ){
        if( other == null )
            return false;
        if( !other.isWellformedGDINRW() || !this.isWellformedGDINRW() )
            return false;
        return other.getLastName().equals( this.getLastName() );
    }

    @Override
    public String toString() {
        return urn;
    }

}
