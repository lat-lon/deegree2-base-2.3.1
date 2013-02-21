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

package org.deegree.portal.portlet.modules.map.actions.portlets;

import org.apache.jetspeed.modules.actions.portlets.JspPortletAction;
import org.apache.jetspeed.portal.Portlet;
import org.apache.turbine.util.RunData;

/**
 * General action for managing WMC (saving, loading sharing). Currently saving to user's directory,
 * saving to a shared diractory and loading from shared are implemented.
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author last edited by: $Author: taddei $
 * 
 * @version $Revision: 1.5 $, $Date: 2007/01/04 11:18:33 $
 */

public class WMCManagementPortletAction extends JspPortletAction {

    @Override
    protected void buildNormalContext( Portlet portlet, RunData rundata )
                            throws Exception {

        WMCManagementPortletPerfom perform = new WMCManagementPortletPerfom( rundata.getRequest(), portlet,
                                                                             rundata.getServletContext() );
        perform.buildNormalContext();
    }

    /**
     * Saved the context into the "WEB_INF/wmc/shared" directory
     * 
     * @param rundata
     * @param portlet
     * @throws Exception
     */
    public void doSaveshared( RunData rundata, Portlet portlet )
                            throws Exception {

        WMCManagementPortletPerfom perform = new WMCManagementPortletPerfom( rundata.getRequest(), portlet,
                                                                             rundata.getServletContext() );

        try {
            perform.doSaveshared( rundata.getUser().getUserName() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    /**
     * Saved the context into the "WEB_INF/wmc/$USER_HOME" directory
     * 
     * @param rundata
     * @param portlet
     * @throws Exception
     */
    public void doSavecontext( RunData rundata, Portlet portlet )
                            throws Exception {

        WMCManagementPortletPerfom perform = new WMCManagementPortletPerfom( rundata.getRequest(), portlet,
                                                                             rundata.getServletContext() );

        try {
            perform.doSavecontext( rundata.getUser().getUserName() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Saved the context with user defined name into the "WEB_INF/wmc/$USER_HOME" directory
     * 
     * @param rundata
     * @param portlet
     * @throws Exception
     */
    public void doSavenamedcontext( RunData rundata, Portlet portlet )
                            throws Exception {

        WMCManagementPortletPerfom perform = new WMCManagementPortletPerfom( rundata.getRequest(), portlet,
                                                                             rundata.getServletContext() );

        try {
            perform.doSavenamedcontext( rundata.getUser().getUserName() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a context from the "WEB_INF/wmc/shared"
     * 
     * @param rundata
     * @param portlet
     * @throws Exception
     */
    public void doLoadcontext( RunData rundata, Portlet portlet )
                            throws Exception {

        WMCManagementPortletPerfom perform = new WMCManagementPortletPerfom( rundata.getRequest(), portlet,
                                                                             rundata.getServletContext() );

        try {
            perform.doLoadcontext();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }
}
