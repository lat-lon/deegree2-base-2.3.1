//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/was/WAService.java $
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

package org.deegree.ogcwebservices.wass.was;

import java.util.ArrayList;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.trigger.TriggerProvider;
import org.deegree.i18n.Messages;
import org.deegree.ogcwebservices.OGCWebService;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;
import org.deegree.ogcwebservices.wass.common.CloseSession;
import org.deegree.ogcwebservices.wass.common.CloseSessionHandler;
import org.deegree.ogcwebservices.wass.common.GetSession;
import org.deegree.ogcwebservices.wass.common.GetSessionAnonymousHandler;
import org.deegree.ogcwebservices.wass.common.GetSessionDispatcher;
import org.deegree.ogcwebservices.wass.common.GetSessionHandler;
import org.deegree.ogcwebservices.wass.common.GetSessionPasswordHandler;
import org.deegree.ogcwebservices.wass.common.Operation_1_0;
import org.deegree.ogcwebservices.wass.common.WASSSecurityManager;
import org.deegree.ogcwebservices.wass.was.configuration.WASConfiguration;
import org.deegree.ogcwebservices.wass.was.configuration.WASDeegreeParams;
import org.deegree.ogcwebservices.wass.was.operation.DescribeUser;
import org.deegree.ogcwebservices.wass.was.operation.DescribeUserHandler;
import org.deegree.ogcwebservices.wass.was.operation.WASGetCapabilities;
import org.deegree.security.GeneralSecurityException;
import org.deegree.security.session.SessionStatusException;

/**
 * This is the main WAService class that implements a WAS according to the GDI NRW spec V1.0.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 */
public class WAService implements OGCWebService {

    private WASConfiguration configuration = null;

    private static final ILogger LOG = LoggerFactory.getLogger( WAService.class );

    private static final TriggerProvider TP = TriggerProvider.create( WAService.class );

    private GetSessionHandler getSessionHandler = null;

    private CloseSessionHandler closeSessionHandler = null;

    private DescribeUserHandler describeUserHandler = null;

    /**
     * Creates a new service according to the given configuration.
     * 
     * @param configuration
     *            the config
     * @throws OGCWebServiceException
     */
    public WAService( WASConfiguration configuration ) throws OGCWebServiceException {

        this.configuration = configuration;

        // setup GetSession/CloseSession handler(s)
        WASDeegreeParams dgParams = configuration.getDeegreeParams();
        if ( configuration.isSessionAuthenticationSupported() ) {
            for ( Operation_1_0 operation : configuration.getOperationsMetadata().getAllOperations() ) {
                if ( "GetSession".equals( operation.getName() ) ) {
                    try {
                        ArrayList<GetSessionHandler> handlers = new ArrayList<GetSessionHandler>( 4 );
                        int lifetime = dgParams.getSessionLifetime();
                        if ( configuration.isPasswordAuthenticationSupported() ) {
                            WASSSecurityManager secManager = new WASSSecurityManager( dgParams.getDatabaseConnection() );
                            handlers.add( new GetSessionPasswordHandler( secManager, lifetime ) );
                        }
                        if ( configuration.isAnonymousAuthenticationSupported() ) {
                            handlers.add( new GetSessionAnonymousHandler( lifetime ) );
                        }
                        if ( handlers.size() == 0 )
                            throw new OGCWebServiceException( Messages.getMessage( "WASS_ERROR_NO_AUTHMETHOD_HANDLER",
                                                                                   "WAS" ) );
                        getSessionHandler = new GetSessionDispatcher( handlers );

                    } catch ( GeneralSecurityException e ) {
                        LOG.logError( e.getLocalizedMessage(), e );
                        throw new OGCWebServiceException( e.getLocalizedMessage() );
                    }
                } else if ( "CloseSession".equals( operation.getName() ) ) {
                    closeSessionHandler = new CloseSessionHandler();
                } else if ( "DescribeUser".equals( operation.getName() ) ) {
                    try {
                        WASSSecurityManager secManager = new WASSSecurityManager( dgParams.getDatabaseConnection() );
                        describeUserHandler = new DescribeUserHandler( secManager );
                    } catch ( GeneralSecurityException e ) {
                        LOG.logError( e.getLocalizedMessage(), e );
                        throw new OGCWebServiceException( e.getLocalizedMessage() );
                    }
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.ogcwebservices.OGCWebService#getCapabilities()
     */
    public OGCCapabilities getCapabilities() {
        return configuration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.ogcwebservices.OGCWebService#doService(org.deegree.ogcwebservices.OGCWebServiceRequest)
     */
    public Object doService( OGCWebServiceRequest request )
                            throws OGCWebServiceException {

        request = (OGCWebServiceRequest) TP.doPreTrigger( this, request )[0];

        Object response = null;

        // TODO exception handling: throw e after each different occasion with descriptive msg
        try {
            if ( request instanceof WASGetCapabilities ) {
                response = configuration;
            } else if ( ( getSessionHandler != null ) && ( request instanceof GetSession ) ) {
                response = getSessionHandler.handleRequest( (GetSession) request );
            } else if ( ( closeSessionHandler != null ) && ( request instanceof CloseSession ) ) {
                closeSessionHandler.handleRequest( (CloseSession) request );
            } else if ( ( describeUserHandler != null ) && ( request instanceof DescribeUser ) ) {
                response = describeUserHandler.handleRequest( (DescribeUser) request );
            } else {
                throw new OGCWebServiceException( Messages.getMessage( "WASS_ERROR_UNKNOWN_REQUEST",
                                                                       new Object[] { getClass().getName(),
                                                                                     request.getClass().getName() } ) );
            }
        } catch ( SessionStatusException e ) {
            LOG.logError( e.getLocalizedMessage(), e );
            // TODO Check if this particular message is needed for the GDI NRW spec V1.0.
            // Otherwise delete it and use e.getMessage() instead.
            // response = new OGCWebServiceException( Messages.getMessage(
            // "WASS_ERROR_INVALID_SESSION", "WAService" ) );
            response = new OGCWebServiceException( e.getLocalizedMessage() );
        } catch ( GeneralSecurityException e ) {
            LOG.logError( e.getLocalizedMessage(), e );
            // TODO Check if this particular message is needed for the GDI NRW spec V1.0.
            // Otherwise delete it and use e.getMessage() instead.
            // throw new OGCWebServiceException( Messages.getMessage(
            // "WASS_ERROR_SECURITY_SYSTEM", "WAService" ) );
            throw new OGCWebServiceException( e.getLocalizedMessage() );
        }

        return TP.doPostTrigger( this, response )[0];
    }

}
