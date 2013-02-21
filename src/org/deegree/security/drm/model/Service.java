//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/drm/model/Service.java $
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

package org.deegree.security.drm.model;

import static java.util.Collections.unmodifiableList;

import java.util.List;

import org.deegree.framework.util.StringPair;

/**
 * <code>Service</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 14855 $, $Date: 2008-11-14 11:56:48 +0100 (Fr, 14. Nov 2008) $
 */
public class Service extends SecurableObject {

    private final String address;

    private final String title;

    private final List<StringPair> objects;

    private final int id;

    private final String type;

    /**
     * @param id
     * @param address
     * @param title
     * @param objects
     * @param type
     */
    public Service( int id, String address, String title, List<StringPair> objects, String type ) {
        this.id = id;
        this.address = address;
        this.title = title;
        this.objects = objects;
        this.type = type;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the title
     */
    public String getServiceTitle() {
        return title;
    }

    /**
     * @return the objects of the service
     */
    public List<StringPair> getObjects() {
        return unmodifiableList( objects );
    }

    /**
     * @return the db id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the service type (WMS/WFS)
     */
    public String getServiceType() {
        return type;
    }

    @Override
    public String toString() {
        return "Service: ID " + id + ", address " + address + ", title '" + title + "'. Objects:\n" + objects;
    }

}
