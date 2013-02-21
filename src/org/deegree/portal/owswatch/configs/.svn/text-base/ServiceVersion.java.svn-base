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
import java.util.HashMap;
import java.util.Map;

/**
 * Holds the Requests that tht a given version of a service can support
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: elmasry $
 * 
 * @version $Revision: 1.1 $, $Date: 2008-03-07 14:49:53 $
 */
public class ServiceVersion implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -525010442855328620L;

    private String version = null;

    private Map<String, ServiceRequest> requests = null;

    /**
     * @param version
     */
    public ServiceVersion( String version ) {
        this.version = version;
        requests = new HashMap<String, ServiceRequest>();
    }

    /**
     * @return Map of requests
     */
    public Map<String, ServiceRequest> getRequests() {
        return requests;
    }

    /**
     * @param requests
     */
    public void setRequests( Map<String, ServiceRequest> requests ) {
        this.requests = requests;
    }

    /**
     * @return Version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion( String version ) {
        this.version = version;
    }

}
