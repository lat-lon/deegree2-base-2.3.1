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

/**
 * A class that holds the information of the configurations file of owsWatch
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: elmasry $
 * 
 * @version $Revision: 1.1 $, $Date: 2008-03-07 14:49:52 $
 */
public class OwsWatchConfig implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 381765455357247965L;

    private GeneralConfig general = null;

    private ServiceDescription serviceConfig = null;

    /**
     * @param general
     * @param serviceConfig
     */
    public OwsWatchConfig( GeneralConfig general, ServiceDescription serviceConfig ) {
        this.general = general;
        this.serviceConfig = serviceConfig;
    }

    /**
     * @return GeneralConfig
     */
    public GeneralConfig getGeneral() {
        return general;
    }

    /**
     * @param general
     */
    public void setGeneral( GeneralConfig general ) {
        this.general = general;
    }

    /**
     * @return ServiceDescription
     */
    public ServiceDescription getServiceConfig() {
        return serviceConfig;
    }

    /**
     * @param serviceConfig
     */
    public void setServiceConfig( ServiceDescription serviceConfig ) {
        this.serviceConfig = serviceConfig;
    }

    /**
     * checks the userName and password of the administrator
     * 
     * @param username
     * @param pwd
     * @return boolean whether this user is logged in or not
     */
    public boolean isAuthenticatedUser( String username, String pwd ) {

        User user = getGeneral().getUsers().get( username );
        return user != null && user.getPassword().equals( pwd );
    }
}
