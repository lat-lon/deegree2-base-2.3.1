//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/was/operation/DescribeUserHandler.java $
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
package org.deegree.ogcwebservices.wass.was.operation;

import java.io.IOException;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.i18n.Messages;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.wass.common.WASSSecurityManager;
import org.deegree.security.GeneralSecurityException;
import org.deegree.security.drm.SecurityAccessManager;
import org.deegree.security.drm.model.User;
import org.deegree.security.session.MemoryBasedSessionManager;
import org.deegree.security.session.Session;
import org.deegree.security.session.SessionStatusException;
import org.xml.sax.SAXException;

/**
 * <code>DescribeUserHandler</code> is the handler class for the deegree specific DescribeUser
 * operation.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */

public class DescribeUserHandler {

    private WASSSecurityManager manager;
    
    private MemoryBasedSessionManager sessionManager;
    
    private static final ILogger LOG = LoggerFactory.getLogger( DescribeUserHandler.class );
    
    /**
     * Constructs new handler with the specified manager as its data source.
     * 
     * @param manager the source of the user data
     */
    public DescribeUserHandler( WASSSecurityManager manager ) {
        this.manager = manager;
        this.sessionManager = MemoryBasedSessionManager.getInstance();
    }
    
    /**
     * Handles a DescribeUser request.
     * 
     * @param request the request to handle
     * @throws OGCWebServiceException
     * @throws GeneralSecurityException 
     * @return an XML document containing the requested information
     */
    public DescribeUserResponse handleRequest( DescribeUser request )
                                 throws OGCWebServiceException, GeneralSecurityException {
        SecurityAccessManager sam = manager.getSecurityAccessManager();
        Session session = null;
        try {
            session = sessionManager.getSessionByID( request.getSessionID() );
        } catch ( SessionStatusException e ) {
            LOG.logError( e.getLocalizedMessage(), e );
            throw new OGCWebServiceException( "DescribeUserHandler: ", Messages.getMessage( "WASS_ERROR_INVALID_SESSION", "WASS" ) );
        }
        
        if( session == null ) throw new OGCWebServiceException( "DescribeUserHandler: ",
                                                                Messages.getMessage( "WASS_ERROR_INVALID_SESSION", "WASS" ) );
        
        if( ! session.isAlive() ) throw new OGCWebServiceException( "DescribeUserHandler: ",
                                                                    Messages.getMessage( "WASS_ERROR_SESSION_EXPIRED", "WASS" ) );
        
        String username = session.getUser();
        
        User user = sam.getUserByName( username );

        DescribeUserResponse response = null;
        
        try{
            response = new DescribeUserResponse( user, request.getSessionID() );
        } catch ( SAXException saxe ) {
            LOG.logError( saxe.getLocalizedMessage(), saxe );
            throw new OGCWebServiceException( "DescribeUserHandler: ",
                                              Messages.getMessage( "WASS_" +
                                                               "ERROR_RESOURCE_WRONG_FORMAT",
                                                               DescribeUserResponse.XML_TEMPLATE ) );
        } catch ( IOException ioe ) {
            LOG.logError( ioe.getLocalizedMessage(), ioe );
            throw new OGCWebServiceException( "DescribeUserHandler: ",
                                              Messages.getMessage( "WASS_" +
                                                               "ERROR_RESOURCE_NOT_FOUND",
                                                               DescribeUserResponse.XML_TEMPLATE ) );
        } catch ( XMLParsingException xmlpe ) {
            LOG.logError( xmlpe.getLocalizedMessage(), xmlpe );
            throw new OGCWebServiceException( "DescribeUserHandler: ",
                                              Messages.getMessage( "WASS_" +
                                                               "ERROR_RESOURCE_WRONG_FORMAT",
                                                               DescribeUserResponse.XML_TEMPLATE ) );
        }
        
        return response;
    }
    
}
