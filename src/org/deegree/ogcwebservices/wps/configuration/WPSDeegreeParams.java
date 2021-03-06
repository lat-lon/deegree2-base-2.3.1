//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wps/configuration/WPSDeegreeParams.java $
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

package org.deegree.ogcwebservices.wps.configuration;

import org.deegree.enterprise.DeegreeParams;
import org.deegree.model.metadata.iso19115.OnlineResource;
import org.deegree.ogcwebservices.wps.execute.RequestQueueManager;

/**
 * WPSDeegreeParams.java
 * 
 * Created on 08.03.2006. 18:40:24h
 * 
 * @author <a href="mailto:christian@kiehle.org">Christian Kiehle</a>
 * @author <a href="mailto:christian.heier@gmx.de">Christian Heier</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 12842 $, $Date: 2008-07-11 16:05:59 +0200 (Fr, 11. Jul 2008) $
 */
public class WPSDeegreeParams extends DeegreeParams {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5341980035537747859L;

	private String[] processDirectories;

    private RequestQueueManager requestQueueManager;

    /**
     * 
     * @param defaultOnlineResource
     * @param cacheSize
     * @param requestTimeLimit
     * @param processDirectories
     * @param requestQueueManager
     */
    public WPSDeegreeParams( OnlineResource defaultOnlineResource, int cacheSize,
                             int requestTimeLimit, String[] processDirectories,
                             RequestQueueManager requestQueueManager ) {
        super( defaultOnlineResource, cacheSize, requestTimeLimit );
        this.processDirectories = processDirectories;
        this.requestQueueManager = requestQueueManager;
    }

    /**
     * Returns the resolved (absolute) process directory paths.
     * 
     * @return the resolved (absolute) process directory paths
     */
    public String[] getProcessDirectories() {
        return processDirectories;
    }

    /**
     * 
     * @return requestQueueManager
     */
    public RequestQueueManager getRequestQueueManager() {
        return requestQueueManager;
    }
}
