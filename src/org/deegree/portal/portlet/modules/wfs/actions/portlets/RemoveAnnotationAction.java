//$Header: $
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

package org.deegree.portal.portlet.modules.wfs.actions.portlets;

import org.apache.jetspeed.portal.Portlet;
import org.apache.turbine.util.RunData;

/**
 * Removes a number of Features (listed by their ID). The actual implementation is done by the
 * Perform class.
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class RemoveAnnotationAction extends WFSClientPortletAction {

    /**
     * Builds the portlet up.
     * 
     * @param portlet
     * @param data
     * @throws Exception
     */
    protected void buildNormalContext( Portlet portlet, RunData data )
                            throws Exception {
        RemoveAnnotationPerform arp = new RemoveAnnotationPerform( data.getRequest(), portlet, data.getServletContext() );
        arp.buildNormalContext( data );
    }

    /**
     * 
     * Delegates to RemoveAnnotationPerform, which does the deleting.
     * 
     * @param data
     * @param portlet
     * @throws Exception
     */
    public void doDeletetransaction( RunData data, Portlet portlet )
                            throws Exception {
        try {
            RemoveAnnotationPerform arp = new RemoveAnnotationPerform( data.getRequest(), portlet,
                                                                       data.getServletContext() );
            arp.doDeletetransaction( data );
        } catch ( Exception e ) {
            e.printStackTrace();
            throw e;
        }
    }

}