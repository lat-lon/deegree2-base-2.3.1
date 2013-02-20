//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/wss/capabilities/WSSCapabilities.java $
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

package org.deegree.ogcwebservices.wass.wss.capabilities;

import java.util.ArrayList;

import org.deegree.ogcwebservices.getcapabilities.ServiceIdentification;
import org.deegree.ogcwebservices.getcapabilities.ServiceProvider;
import org.deegree.ogcwebservices.wass.common.OWSCapabilitiesBaseType_1_0;
import org.deegree.ogcwebservices.wass.common.OperationsMetadata_1_0;
import org.deegree.ogcwebservices.wass.common.SupportedAuthenticationMethod;

/**
 * A <code>WSSCapabilities</code> class encapsulates all the data which can be requested with a
 * GetCapabilities request. It's base class is OWSCapabilitiesBaseType_1_0 which is conform the
 * gdi-nrw access control version 1.0 specification.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 * 
 * @since 2.0
 */

public class WSSCapabilities extends OWSCapabilitiesBaseType_1_0 {
    /**
     * 
     */
    private static final long serialVersionUID = 2181625093642200664L;

    private String securedServiceType = null;

    /**
     * @param version
     * @param updateSequence
     * @param sf
     * @param sp
     * @param om
     * @param securedServiceType
     * @param am
     */
    public WSSCapabilities( String version, String updateSequence, ServiceIdentification sf,
                           ServiceProvider sp, OperationsMetadata_1_0 om, String securedServiceType,
                           ArrayList<SupportedAuthenticationMethod> am ) {
        super( version, updateSequence, sf, sp, om, am );

        this.securedServiceType = securedServiceType;
    }

    /**
     * @return the securedServiceType.
     */
    public String getSecuredServiceType() {
        return securedServiceType;
    }

}
