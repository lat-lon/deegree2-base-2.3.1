//$$Header: $$
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

package org.deegree.portal.owswatch.configs;

import java.io.Serializable;
import java.util.List;

/**
 * A Class to hold the information of an owsWatch user
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: elmasry $
 * 
 * @version $Revision: 1.1 $, $Date: 2008-03-07 14:49:53 $
 */
public class User implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8831467227767271591L;

    private String username = null;

    private String password = null;

    private String firstName = null;

    private String lastName = null;

    private String email = null;

    private List<String> roles = null;

    /**
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param email
     *            The email will be used to send error emails in case a service failed
     * @param roles
     *            indicates whether a user is allowed to add/edit services, or just watch them
     */
    public User( String username, String password, String firstName, String lastName, String email, List<String> roles ) {

        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    /**
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return List<String>
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * @return String
     */
    public String getUsername() {
        return username;
    }
}
