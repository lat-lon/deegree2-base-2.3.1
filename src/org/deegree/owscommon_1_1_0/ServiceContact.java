//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_1_1_0/ServiceContact.java $
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

package org.deegree.owscommon_1_1_0;

import org.deegree.framework.util.Pair;

/**
 * <code>ServiceContact</code> wraps the service contact information of ows 1.1.0.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10830 $, $Date: 2008-03-31 11:33:56 +0200 (Mo, 31. Mär 2008) $
 * 
 */

public class ServiceContact {

    private final String individualName;

    private final String positionName;

    private final ContactInfo contactInfo;

    private final Pair<String, String> role;

    /**
     * @param individualName
     * @param positionName
     * @param contactInfo
     * @param role
     *            a pair containing &lt; role, codeSpace &gt;
     */
    public ServiceContact( String individualName, String positionName, ContactInfo contactInfo,
                           Pair<String, String> role ) {
        this.individualName = individualName;
        this.positionName = positionName;
        this.contactInfo = contactInfo;
        this.role = role;
    }

    /**
     * @return the individualName.
     */
    public final String getIndividualName() {
        return individualName;
    }

    /**
     * @return the positionName.
     */
    public final String getPositionName() {
        return positionName;
    }

    /**
     * @return the contactInfo.
     */
    public final ContactInfo getContactInfo() {
        return contactInfo;
    }

    /**
     * @return the role as a &lt; role, codeSpace &gt; pair or <code>null</code>
     */
    public final Pair<String, String> getRole() {
        return role;
    }

}
