//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/drm/model/Privilege.java $
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
package org.deegree.security.drm.model;

/**
 * Default implementation of privilege-objects.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 9346 $
 */

public class Privilege {

    // predefined privileges
    public static final Privilege WRITE = new Privilege( 1, "write" );

    public static final Privilege ADDUSER = new Privilege( 2, "adduser" );

    public static final Privilege ADDGROUP = new Privilege( 3, "addgroup" );

    public static final Privilege ADDROLE = new Privilege( 4, "addrole" );

    public static final Privilege ADDOBJECT = new Privilege( 5, "addobject" );

    private int id;

    private String name;

    /**
     * Creates a new <code>Privilege</code>-instance.
     * 
     * @param id
     * @param name
     */
    public Privilege( int id, String name ) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the unique identifier of this privilege.
     */
    public int getID() {
        return id;
    }

    /**
     * Returns the name of this privilege.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Indicates whether some other privilege is "equal to" this one.
     * 
     * @param that
     */
    public boolean equals( Object that ) {
        if ( that instanceof Privilege ) {
            return ( ( (Privilege) that ).getID() == getID() );
        }
        return false;
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of
     * hashtables such as those provided by java.util.Hashtable.
     */
    public int hashCode() {
        return id;
    }
}
