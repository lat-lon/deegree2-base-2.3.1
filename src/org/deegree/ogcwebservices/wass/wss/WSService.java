//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/wss/WSService.java $
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

package org.deegree.ogcwebservices.wass.wss;

import java.util.ArrayList;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.trigger.TriggerProvider;
import org.deegree.i18n.Messages;
import org.deegree.ogcwebservices.OGCWebService;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.getcapabilities.OGCCapabilities;
import org.deegree.ogcwebservices.wass.common.AuthenticationData;
import org.deegree.ogcwebservices.wass.common.CloseSession;
import org.deegree.ogcwebservices.wass.common.CloseSessionHandler;
import org.deegree.ogcwebservices.wass.common.GetSession;
import org.deegree.ogcwebservices.wass.common.GetSessionAnonymousHandler;
import org.deegree.ogcwebservices.wass.common.GetSessionDispatcher;
import org.deegree.ogcwebservices.wass.common.GetSessionHandler;
import org.deegree.ogcwebservices.wass.common.GetSessionPasswordHandler;
import org.deegree.ogcwebservices.wass.common.Operation_1_0;
import org.deegree.ogcwebservices.wass.common.WASSSecurityManager;
import org.deegree.ogcwebservices.wass.exceptions.DoServiceException;
import org.deegree.ogcwebservices.wass.wss.configuration.WSSConfiguration;
import org.deegree.ogcwebservices.wass.wss.configuration.WSSDeegreeParams;
import org.deegree.ogcwebservices.wass.wss.operation.DoService;
import org.deegree.ogcwebservices.wass.wss.operation.DoServiceAnonymousHandler;
import org.deegree.ogcwebservices.wass.wss.operation.DoServiceHandler;
import org.deegree.ogcwebservices.wass.wss.operation.DoServicePasswordHandler;
import org.deegree.ogcwebservices.wass.wss.operation.DoServiceSessionHandler;
import org.deegree.ogcwebservices.wass.wss.operation.WSSGetCapabilities;
import org.deegree.security.GeneralSecurityException;
import org.deegree.security.session.SessionStatusException;

/**
 * The Web Security Service - <code>WSService</code> - is the dispatcher of the entire WSS. It
 * calls the appropriate classes according to a given request.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Thu, 27 Dec 2007) $
 */
public class WSService implements OGCWebService {

    private WSSConfiguration configuration = null;

    private static final ILogger LOG = LoggerFactory.getLogger( WSService.class );

    private static final TriggerProvider TP = TriggerProvider.create( WSService.class );

    private GetSessionHandler getSessionHandler = null;

    private CloseSessionHandler closeSessionHandler = null;

    private DoServiceHandler doServiceHandler = null;

    private WASSSecurityManager secManager = null;

    /**
     * Creates a new WebSecurityService with the given configuration( = capabilities) bean.
     * 
     * @param config
     * @throws OGCWebServiceException
     */
    public WSService( WSSConfiguration config ) throws OGCWebServiceException {
        configuration = config;

        WSSDeegreeParams dgParams = configuration.getDeegreeParams();
        if ( configuration.isSessionAuthenticationSupported() ) {
            for ( Operation_1_0 operation : configuration.getOperationsMetadata().getAllOperations() ) {
                if ( "GetSession".equals( operation.getName() ) ) {
                    try {
                        ArrayList<GetSessionHandler> handlers = new ArrayList<GetSessionHandler>();
                        int lifetime = dgParams.getSessionLifetime();
                        if ( configuration.isPasswordAuthenticationSupported() ) {
                            secManager = new WASSSecurityManager( dgParams.getDatabaseConnection() );
                            handlers.add( new GetSessionPasswordHandler( secManager, lifetime ) );
                        }
                        if ( configuration.isAnonymousAuthenticationSupported() ) {
                            handlers.add( new GetSessionAnonymousHandler( lifetime ) );
                        }
                        if ( handlers.size() == 0 )
                            throw new OGCWebServiceException( Messages.getMessage( "WASS_ERROR_NO_AUTHMETHOD_HANDLER",
                                                                               "WSS" ) );
                        getSessionHandler = new GetSessionDispatcher( handlers );

                    } catch ( GeneralSecurityException e ) {
                        LOG.logError( e.getLocalizedMessage(), e );
                        throw new OGCWebServiceException( e.getLocalizedMessage() );
                    }
                } else if ( "CloseSession".equals( operation.getName() ) ) {
                    closeSessionHandler = new CloseSessionHandler();
                }
            }
        }
    }

    /*
     * Returns the capabilities of the WSS. This is not the correct default behaviour, for a
     * GetCapabalities request must be able to request only parts of the capabilies of this wss .
     * 
     * @see org.deegree.ogcwebservices.OGCWebService#getCapabilities()
     */
    public OGCCapabilities getCapabilities() {
        return configuration;
    }

    /*
     * The core method. It dispatches the request to the appropriate classes which handle them.
     * 
     * @see org.deegree.ogcwebservices.OGCWebService#doService(org.deegree.ogcwebservices.OGCWebServiceRequest)
     */
    public Object doService( OGCWebServiceRequest request )
                            throws OGCWebServiceException {

        request = (OGCWebServiceRequest) TP.doPreTrigger( this, request )[0];

        Object response = null;

        // TODO exception handling: throw e after each different occasion with descriptive msg
        try {
            if ( request instanceof WSSGetCapabilities ) {
                response = getCapabilities();
            } else if ( ( getSessionHandler != null ) && ( request instanceof GetSession ) ) {
                response = getSessionHandler.handleRequest( (GetSession) request );
            } else if ( ( closeSessionHandler != null ) && ( request instanceof CloseSession ) ) {
                closeSessionHandler.handleRequest( (CloseSession) request );
            } else if ( request instanceof DoService ) {
                AuthenticationData authData = ( (DoService) request ).getAuthenticationData();
                // password authentication used?
                if ( authData.usesPasswordAuthentication() ) {
                    if ( configuration.isPasswordAuthenticationSupported() )
                        doServiceHandler = new DoServicePasswordHandler( secManager );
                    else
                        response = new OGCWebServiceException( Messages.getMessage( "WASS_ERROR_AUTHENTICATION_PASSWORD_NOT_SUPPORTED",
                                                                                "WSS" ) );
                } else if ( authData.usesSessionAuthentication() ) {
                    if ( configuration.isSessionAuthenticationSupported() )
                        doServiceHandler = new DoServiceSessionHandler();
                    else
                        response = new OGCWebServiceException( Messages.getMessage( "WASS_ERROR_AUTHENTICATION_SESSION_NOT_SUPPORTED",
                                                                                "WSS" ) );
                } else if ( authData.usesAnonymousAuthentication() ) {
                    if ( configuration.isAnonymousAuthenticationSupported() )
                        doServiceHandler = new DoServiceAnonymousHandler();
                    else
                        response = new OGCWebServiceException( Messages.getMessage( "WASS_ERROR_AUTHENTICATION_ANONYMOUS_NOT_SUPPORTED",
                                                                                "WSS" ) );
                }
                if ( response == null ) {
                    doServiceHandler.handleRequest( (DoService) request );
                    if ( doServiceHandler.requestAllowed() )
                        response = doServiceHandler.sendRequest( (DoService) request,
                                                                 ( configuration.getDeegreeParams() ).getSecuredServiceAddress().getLinkage().getHref(),
                                                                 /* configuration.getDeegreeParams().getCharacterSet() */null,
                                                                 /* configuration.getDeegreeParams().getRequestTimeLimit() */0,
                                                                 configuration.getSecuredServiceType() );
                }
            } else {
                LOG.logError( Messages.getMessage( "WASS_ERROR_UNKNOWN_REQUEST",
                                               new Object[] { "WSS", request.getClass().getName() } ) );
                throw new OGCWebServiceException( Messages.getMessage( "WASS_ERROR_UNKNOWN_REQUEST",
                                                                   new Object[] {"WSS",
                                                                                 request.getClass().getName() } ) );
            }
        } catch ( DoServiceException e ) {
            LOG.logError( e.getLocalizedMessage(), e );
            response = new OGCWebServiceException( e.getLocalizedMessage() );
        } catch ( SessionStatusException e ) {
            LOG.logError( e.getLocalizedMessage(), e );
            // TODO Check if this particular message is needed for the GDI NRW spec V1.0.
            // Otherwise delete it and use e.getLocalizedMessage() instead.
            response = new OGCWebServiceException( Messages.getMessage( "WASS_ERROR_INVALID_SESSION",
                                                                    "WSService" ) );
        } catch ( GeneralSecurityException e ) {
            LOG.logError( e.getLocalizedMessage(), e );
            // TODO Check if this particular message is needed for the GDI NRW spec V1.0.
            // Otherwise delete it and use e.getMessage() instead.
            // throw new OGCWebServiceException( e.getLocalizedMessage() );
            throw new OGCWebServiceException( Messages.getMessage( "WASS_ERROR_SECURITY_SYSTEM",
                                                               "WSService" ) );
        }

        return TP.doPostTrigger( this, response )[0];
    }

}
