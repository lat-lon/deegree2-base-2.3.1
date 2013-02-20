//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/was/configuration/WASConfiguration.java $
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

package org.deegree.ogcwebservices.wass.was.configuration;

import org.deegree.ogcwebservices.wass.was.capabilities.WASCapabilities;

/**
 * Encapsulates the configuration data for a WAS.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */

public class WASConfiguration extends WASCapabilities {

    private static final long serialVersionUID = -1135265074478652811L;

    private WASDeegreeParams deegreeParams = null;

    /**
     * @param cap
     * @param deegreeParams
     */
    public WASConfiguration( WASCapabilities cap, WASDeegreeParams deegreeParams ) {
        super( cap.getVersion(), cap.getUpdateSequence(), cap.getServiceIdentification(),
               cap.getServiceProvider(), cap.getOperationsMetadata(),
               cap.getAuthenticationMethods() );
        this.deegreeParams = deegreeParams;
    }

    /**
     * @return Returns the deegreeParams.
     */
    public WASDeegreeParams getDeegreeParams() {
        return deegreeParams;
    }

}
