//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/common/WASSSecurityManager.java $
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

package org.deegree.ogcwebservices.wass.common;

import java.util.Properties;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;
import org.deegree.io.JDBCConnection;
import org.deegree.security.GeneralSecurityException;
import org.deegree.security.drm.SecurityAccessManager;

/**
 * This class will hold the SecurityAccessManager Instance and will be able to parse the
 * user/password key for the security database.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9348 $, $Date: 2007-12-27 17:59:14 +0100 (Thu, 27 Dec 2007) $
 */

public class WASSSecurityManager {

    private JDBCConnection databaseInfo = null;
    
    private SecurityAccessManager securityAccessManager = null;

    private static final ILogger LOG = LoggerFactory.getLogger( WASSSecurityManager.class );

    /**
     * This constructor initializes the connection to the security database.
     * 
     * @param dbInfo a database information object
     * 
     * @throws GeneralSecurityException
     */
    public WASSSecurityManager( JDBCConnection dbInfo ) throws GeneralSecurityException {
        databaseInfo = dbInfo;
        initializeSecurityAccessManager();
    }

    /**
     * Loads the deegree SecurityAccesManager if no instance is present jet.
     * 
     * @throws GeneralSecurityException
     *             if the no instance of the deegree securitymanager could be touched.
     */
    private void initializeSecurityAccessManager()
                            throws GeneralSecurityException {
        
        if( databaseInfo == null ) {
            LOG.logError( Messages.getMessage( "WASS_ERROR_SECURITYACCESSMANAGER_NO_DBINFO" ) );
            return;
        }
        Properties properties = new Properties();
        properties.setProperty( "driver", databaseInfo.getDriver() );
        properties.setProperty( "url", databaseInfo.getURL() );
        properties.setProperty( "user", databaseInfo.getUser() );
        properties.setProperty( "password", databaseInfo.getPassword() );
        try {
            securityAccessManager = SecurityAccessManager.getInstance();
        } catch ( GeneralSecurityException gse ) {
            try {
                SecurityAccessManager.initialize( "org.deegree.security.drm.SQLRegistry",
                                                  properties, 60 * 1000 );
                securityAccessManager = SecurityAccessManager.getInstance();
            } catch ( GeneralSecurityException gse2 ) {
                LOG.logError( Messages.getMessage( "WASS_ERROR_SECURITYACCESSMANAGER" ) );
                LOG.logError( gse2.getLocalizedMessage(), gse2 );
                throw new GeneralSecurityException(
                                                   Messages.getMessage( "WASS_ERROR_SECURITYACCESSMANAGER" ) );
            }
        }
        
    }

    /**
     * @return Returns the deegree securityAccessManager.
     * @throws GeneralSecurityException
     */
    public SecurityAccessManager getSecurityAccessManager()
                            throws GeneralSecurityException {
        
        if ( securityAccessManager == null ) {
            throw new GeneralSecurityException(
                                                Messages.getMessage( "WASS_ERROR_SECURITYACCESSMANAGER_NO_INIT" ) );
        }
        
        return securityAccessManager;
    }

}
