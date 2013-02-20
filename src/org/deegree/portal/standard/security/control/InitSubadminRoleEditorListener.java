//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/security/control/InitSubadminRoleEditorListener.java $
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
package org.deegree.portal.standard.security.control;

import org.deegree.enterprise.control.AbstractListener;
import org.deegree.enterprise.control.FormEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;
import org.deegree.security.GeneralSecurityException;
import org.deegree.security.drm.SecurityAccess;
import org.deegree.security.drm.SecurityAccessManager;
import org.deegree.security.drm.model.User;

/**
 * This <tt>Listener</tt> reacts on 'initSubadminRoleAdministration' events, queries the
 * <tt>SecurityManager</tt> and passes the subadmin- roles on to be displayed by the JSP.
 * <p>
 * Access constraints:
 * <ul>
 * <li>only users that have the 'Administrator'-role are allowed</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mschneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Thu, 27 Dec 2007) $
 */
public class InitSubadminRoleEditorListener extends AbstractListener {

    private static final ILogger LOG = LoggerFactory.getLogger( InitSubadminRoleEditorListener.class );

    @Override
    public void actionPerformed( FormEvent event ) {

        try {
            // get user
            SecurityAccessManager manager = SecurityAccessManager.getInstance();
            User user = manager.getUserByName( toModel().get( ClientHelper.KEY_USERNAME ) );
            SecurityAccess access = manager.acquireAccess( user );

            // perform access check
            ClientHelper.checkForAdminRole( access );

            // data objects for the JSPas
            getRequest().setAttribute( "ROLES", access.getRolesByNS( "SUBADMIN" ) );
            getRequest().setAttribute( "GROUPS", access.getAllGroups() );
            getRequest().setAttribute( "TOKEN", access );
        } catch ( GeneralSecurityException e ) {
            getRequest().setAttribute( "SOURCE", this.getClass().getName() );
            getRequest().setAttribute(
                                       "MESSAGE",
                                       Messages.getMessage( "IGEO_STD_SEC_FAIL_INIT_SUBADMINROLE_EDITOR",
                                                            e.getMessage() ) );
            setNextPage( "admin/admin_error.jsp" );
            LOG.logError( e.getMessage(), e );
        }
    }
}
