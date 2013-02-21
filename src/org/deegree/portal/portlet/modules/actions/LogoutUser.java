//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/portlet/modules/actions/LogoutUser.java $
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
package org.deegree.portal.portlet.modules.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.deegree.framework.xml.XMLFragment;
import org.deegree.portal.context.ViewContext;
import org.deegree.portal.context.XMLFactory;

/**
 * This class can be used within listener classes that will be called if a user logs out from a
 * portal. All contexts used at this moment will be stored in the users WMC directory having
 * filenames starting with 'VIEWCONTEXT'.<BR>
 * At the moment a concrete listener is available for Jetspeed 1.6
 * 
 * @see org.deegree.portal.portlet.modules.actions.IGeoJetspeed16LogoutUser
 * 
 * 
 * @version $Revision: 10660 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 * 
 * @since 2.0
 */
class LogoutUser {

    /**
     * 
     * @param dir
     * @param ses
     * @throws IOException
     * @throws ParserConfigurationException
     */
    void storeCurrentContexts( File dir, HttpSession ses )
                            throws ParserConfigurationException, IOException {

        Enumeration en = ses.getAttributeNames();
        // because a porlat may use more than one web map context
        // parallel at different map windows we must iterate over
        // all session attributes indicating a WMC currently in use
        if ( en != null ) {

            while ( en.hasMoreElements() ) {
                String name = (String) en.nextElement();
                Object val = ses.getAttribute( name );
                if ( val != null && val instanceof ViewContext
                     && name.indexOf( AbstractPortletPerform.CURRENT_WMC ) > -1 ) {
                    storeContext( dir, name, (ViewContext) val );
                }
            }
        }
    }

    /**
     * stores the passed web map context as loggedout WMC into the current users WMC directory
     * 
     * @param dir
     * @param name
     * @param context
     * @throws ParserConfigurationException
     * @throws IOException
     */
    private void storeContext( File dir, String name, ViewContext context )
                            throws ParserConfigurationException, IOException {

        XMLFragment xml = XMLFactory.export( context );

        File file = new File( dir.getAbsolutePath() + '/' + name + ".xml" );

        FileOutputStream fos = new FileOutputStream( file );
        xml.write( fos );
        fos.close();

    }

}
