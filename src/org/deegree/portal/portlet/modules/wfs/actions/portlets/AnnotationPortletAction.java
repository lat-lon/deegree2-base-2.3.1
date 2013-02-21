//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/portlet/modules/wfs/actions/portlets/AnnotationPortletAction.java $
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
package org.deegree.portal.portlet.modules.wfs.actions.portlets;

import org.apache.jetspeed.portal.Portlet;
import org.apache.turbine.util.RunData;

/**
 * Portlet for managing localized annotation. The assigned
 * 
 * @see org.deegree.portal.portlet.modules.wfs.actions.portlets.AnnotationPortletPerform extends
 * @see org.deegree.portal.portlet.modules.wfs.actions.portlets.WFSClientPortletPerform by adding capabilities to
 *      perform a transaction directly against a database
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: jmays $
 * 
 * @version $Revision: 12129 $, $Date: 2008-06-03 20:23:36 +0200 (Di, 03. Jun 2008) $
 */
public class AnnotationPortletAction extends WFSClientPortletAction {

    /**
     * @param portlet
     * @param data
     * @throws Exception
     */
    @Override
    protected void buildNormalContext( Portlet portlet, RunData data )
                            throws Exception {
        AnnotationPortletPerform wfspp = new AnnotationPortletPerform( data.getRequest(), portlet,
                                                                       data.getServletContext() );
        wfspp.buildNormalContext();
    }

    /**
     * 
     * @param data
     * @param portlet
     * @throws Exception
     */
    @Override
    public void doTransaction( RunData data, Portlet portlet )
                            throws Exception {

        try {
            AnnotationPortletPerform wfspp = new AnnotationPortletPerform( data.getRequest(), portlet,
                                                                           data.getServletContext() );
            wfspp.doTransaction( data.getUser().getUserName() );
        } catch ( Exception e ) {
            e.printStackTrace();
            throw e;
        }
    }
}