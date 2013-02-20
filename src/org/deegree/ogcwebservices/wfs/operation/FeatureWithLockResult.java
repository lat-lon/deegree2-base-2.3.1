//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wfs/operation/FeatureWithLockResult.java $
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
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstraße 19
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
package org.deegree.ogcwebservices.wfs.operation;

import org.deegree.model.feature.FeatureCollection;
import org.deegree.ogcwebservices.AbstractOGCWebServiceRequest;

/**
 * Represents the response to a {@link GetFeatureWithLock} request.
 * <p>
 * For the {@link GetFeatureWithLock request}, a WFS must generate a result that includes the lock
 * identifier.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 */
public class FeatureWithLockResult extends FeatureResult {

    private String lockId;

    /**
     * Creates a new instance of <code>FeatureWithLockResult</code>.
     * 
     * @param request 
     * @param lockId 
     * @param featureCollection 
     */
    FeatureWithLockResult( AbstractOGCWebServiceRequest request, String lockId,
                          FeatureCollection featureCollection ) {
        super( request, featureCollection );
        this.lockId = lockId;
    }

    /**
     * Returns the lock id.
     * 
     * @return the lock id
     */
    public String getLockId() {
        return this.lockId;
    }

    /**
     * Returns a string representation of the object.
     * 
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        String ret = this.getClass().getName()
            + ":\n";
        ret += super.toString();
        ret += ( "lockId: "
            + lockId + "\n" );
        return ret;
    }
}
