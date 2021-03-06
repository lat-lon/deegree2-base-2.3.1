//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/portlet/modules/actions/IGeoJetspeed16LoginUser.java $
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

import javax.servlet.http.HttpSession;

import org.apache.jetspeed.modules.actions.JLoginUser;
import org.apache.turbine.util.RunData;
import org.deegree.security.drm.model.User;

/**
 * 
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
public class IGeoJetspeed16LoginUser extends JLoginUser {

    @Override
    public void doPerform( RunData data )
                            throws Exception {
        super.doPerform( data );
        String user = data.getUser().getUserName();
        HttpSession ses = data.getRequest().getSession( false );
        if ( ses != null ) {
            LoginUser liu = new LoginUser();
            File dir = liu.ensureDirectory( data.getServletContext(), user );
            org.apache.turbine.om.security.User tu = data.getUser();

            String username = tu.getUserName();
            String password = tu.getPassword();
            String firstname = tu.getFirstName();
            String lastname = tu.getLastName();
            String email = tu.getEmail();

            User dgUser = new User( 0, username, password, firstname, lastname, email, null );
            liu.readContextDocuments( dir, ses, dgUser );
        }

    }

}
