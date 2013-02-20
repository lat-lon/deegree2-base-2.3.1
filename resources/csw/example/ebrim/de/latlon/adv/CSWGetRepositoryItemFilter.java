/*----------------    FILE HEADER  ------------------------------------------
 This file is part of adv ebrim project.
 Copyright (C) 2007 by:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 ---------------------------------------------------------------------------*/
package de.latlon.adv;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deegree.datatypes.QualifiedName;
import org.deegree.enterprise.servlet.ServletRequestWrapper;
import org.deegree.enterprise.servlet.ServletResponseWrapper;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.IDGenerator;
import org.deegree.framework.util.WebappResourceResolver;
import org.deegree.framework.xml.InvalidConfigurationException;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.io.datastore.PropertyPathResolvingException;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureCollection;
import org.deegree.model.feature.FeatureProperty;
import org.deegree.model.filterencoding.ComplexFilter;
import org.deegree.model.filterencoding.Expression;
import org.deegree.model.filterencoding.Literal;
import org.deegree.model.filterencoding.OperationDefines;
import org.deegree.model.filterencoding.PropertyIsCOMPOperation;
import org.deegree.model.filterencoding.PropertyName;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcbase.ExceptionCode;
import org.deegree.ogcbase.PropertyPath;
import org.deegree.ogcbase.PropertyPathFactory;
import org.deegree.ogcwebservices.OGCRequestFactory;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.csw.CSWExceptionCode;
import org.deegree.ogcwebservices.csw.CatalogueService;
import org.deegree.ogcwebservices.csw.configuration.CatalogueConfiguration;
import org.deegree.ogcwebservices.csw.discovery.GetRepositoryItem;
import org.deegree.ogcwebservices.csw.manager.Manager;
import org.deegree.ogcwebservices.wfs.WFService;
import org.deegree.ogcwebservices.wfs.XMLFactory;
import org.deegree.ogcwebservices.wfs.operation.FeatureResult;
import org.deegree.ogcwebservices.wfs.operation.GetFeature;
import org.deegree.ogcwebservices.wfs.operation.GetFeatureDocument;
import org.deegree.ogcwebservices.wfs.operation.Query;
import org.deegree.ogcwebservices.wfs.operation.GetFeature.RESULT_TYPE;
import org.deegree.security.GeneralSecurityException;
import org.deegree.security.drm.model.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * The <code>CSWGetRepositoryItem</code> class, is a servlet filter, which can handle incoming GetRepositoryItem
 * requests (defined tobe a Http-GET request). This filter directly talks to the wfs-backend of the csw and requests for
 * a given rim:ExtrinsicObject it's app:RegistryObject/app:extrinsicObject/app:ExtrinsicObject/app:object. The value
 * returned from the localwfs is then send (according to the mimetype) back to the requester.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: bezema $
 * 
 * @version $Revision: 1.6 $, $Date: 2007-06-21 13:53:15 $
 * 
 */

public class CSWGetRepositoryItemFilter implements Filter {

    private static ILogger LOG = LoggerFactory.getLogger( CSWGetRepositoryItemFilter.class );

    private WFService localWFS = null;

    private URI appURI;

    private OWSProxyHandler proxyHandler = null;

    /**
     * Inits this filter, it reads the configuration file (given by the <filter-param> 'csw.config' and with it
     * constructs a transaction manager. This manager can deliver a localwfs, to which this filter will do a direct
     * communication.
     */
    public void init( FilterConfig config )
                            throws ServletException {
        String configLocation = config.getInitParameter( "csw.config" );
        try {
            URL configURL = WebappResourceResolver.resolveFileLocation( configLocation, config.getServletContext(), LOG );
            CatalogueConfiguration cswConfig = CatalogueConfiguration.createConfiguration( configURL );
            CatalogueService cswService = CatalogueService.create( cswConfig );
            Manager transactionManager = cswService.getManager();
            if ( transactionManager.getWfsService() == null ) {
                LOG.logError( "The GetRepositoryItemFilter has no access to the localWFS" );
            } else {
                localWFS = transactionManager.getWfsService();
            }

        } catch ( MalformedURLException e ) {
            LOG.logError( "Could not initiate GetRepositoryItemFilter: " + e.getMessage() );
        } catch ( IOException e ) {
            LOG.logError( "Could not initiate GetRepositoryItemFilter: " + e.getMessage() );
        } catch ( SAXException e ) {
            LOG.logError( "Could not initiate GetRepositoryItemFilter: " + e.getMessage() );
        } catch ( InvalidConfigurationException e ) {
            LOG.logError( "Could not initiate GetRepositoryItemFilter: " + e.getMessage() );
        } catch ( OGCWebServiceException e ) {
            LOG.logError( "Could not initiate GetRepositoryItemFilter: " + e.getMessage() );
        }
        if ( localWFS != null ) {
            try {
                appURI = new URI( "http://www.deegree.org/app" );
                proxyHandler = new OWSProxyHandler( config );
            } catch ( URISyntaxException e ) {
                // This never happens but whatever
            } catch ( InvalidParameterException ipe ) {
                LOG.logInfo( "CSW (Ebrim) GetRepositoryItem Servlet Filter: couldn't create an OWSProxyHandler, so no user authentification available, because: "
                             + ipe.getMessage() );
            }
            LOG.logInfo( "CSW (Ebrim) GetRepositoryItem Servlet Filter: successfully initialized" );
        } else {
            LOG.logInfo( "CSW (Ebrim) GetRepositoryItem Servlet Filter: was not initialized" );
        }
    }

    public void destroy() {
        // nottin to do
    }

    /**
     * The actual doFilter method, will check if the incoming request is a wrs:GetRepositoryItem request (by checking
     * the service parameter of the httpGet request.
     */
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
                            throws IOException, ServletException {
        if ( localWFS == null ) {
            LOG.logInfo( "CSW (Ebrim) GetRepositoryItem-Filter: Local Catalogue service is not instantiated, therefore the CSW-GetRepositoryItem filter can not be used" );
//            chain.doFilter( request, response );
            sendException( response, new OGCWebServiceException( "The backend is not configured so not accepting any requests.", CSWExceptionCode.WRS_NOTIMPLEMENTED ) );
            return;            
        }
        Map<String, String[]> parameters = request.getParameterMap();
        if ( parameters.size() != 0 ) {
            Map<String, String> params = new HashMap<String, String>();
            for ( String key : parameters.keySet() ) {
                String[] tmp = parameters.get( key );
                for ( int i = 0; i < tmp.length; i++ ) {
                    tmp[i] = tmp[i].trim();
                    LOG.logDebug( "for key: " + key + " found param: " + tmp[i] );
                }
                //params.put( key.toUpperCase(), StringTools.arrayToString( tmp, ',' ) );
                if( tmp.length > 0 ){
                    params.put( key.toUpperCase(), tmp[0] );
                }
            }
            String service = params.get( "SERVICE" );
            if ( service == null ) {
                // a profile of a service will be treated as a service
                service = params.get( "PROFILE" );
                if ( service == null ) {
                    sendException( response, new OGCWebServiceException( "The SERVICE or PROFILE keyword isn't set.", CSWExceptionCode.WRS_INVALIDREQUEST ) );
                    //chain.doFilter( request, response );
                    return;
                }
            }

            ServletRequestWrapper requestWrapper = null;
            if ( request instanceof ServletRequestWrapper ) {
                LOG.logDebug( "the incoming request is actually an org.deegree.enterprise.servlet.RequestWrapper, so not creating new instance." );
                requestWrapper = (ServletRequestWrapper) request;
            } else {
                requestWrapper = new ServletRequestWrapper( (HttpServletRequest) request );
            }

            OGCWebServiceRequest owsProxyRequest = null;
            User user = null;
            //put them to uppercase *sigh*
            String userName = params.get( "USER" );
            String password = params.get( "PASSWORD" );

            if ( this.proxyHandler != null ) {
                try {
                    LOG.logDebug( "trying to get authentication for user: " + userName + " with password: " + password);
                    user = proxyHandler.authentificateFromUserPw( userName, password );
                    owsProxyRequest = proxyHandler.createOWSRequest( requestWrapper );
                    authorizeRequest( user, owsProxyRequest, requestWrapper );
                } catch ( GeneralSecurityException e ) {
                    LOG.logDebug( "User: " + userName + " with password: " + password
                                  + " couldn't get authentification because: " + e.getMessage() );
                    sendException( response, new OGCWebServiceException( e.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST ) );
                    return;
                }
                catch ( OGCWebServiceException e ) {
                    sendException( response, e );
                    return;
                }
            } else {
                LOG.logDebug( "CSW (Ebrim) Insert-Filter: the proxyHandler is not defined so no user Authentification." );
                sendException( response, new OGCWebServiceException( "The proxyHandler is not defined so not accepting any requests.", CSWExceptionCode.WRS_NOTIMPLEMENTED ) );
                return;
            }
            if ( !OGCRequestFactory.CSW_SERVICE_NAME_EBRIM.equals( service ) ) {
                //sendException( response, new OGCWebServiceException( "This service only supplies ", CSWExceptionCode.WRS_INVALIDREQUEST ) );
                chain.doFilter( request, response );
                return;
            }
            LOG.logDebug( "The CSWGetRepositoryItemFilter is handling the request with following parameters: "
                          + params );
            String id = Long.toString( IDGenerator.getInstance().generateUniqueID() );
            params.put( "REQUESTID", id );
            GetRepositoryItem getReposItem = null;
            try {
                getReposItem = GetRepositoryItem.create( params );
                handleGetRepositoryItem( response, getReposItem, user );
            } catch ( OGCWebServiceException e ) {
                sendException( response, e );
                return;
            }
        }
        if( !response.isCommitted() ){
            chain.doFilter( request, response );
        }
    }

    private void authorizeRequest( User user, OGCWebServiceRequest owsProxyRequest,
                                   ServletRequestWrapper requestWrapper )
                            throws OGCWebServiceException {
        // checking the authorization of the user.
        if ( this.proxyHandler != null ) {
            try {
                // owsProxyRequest = proxyHandler.createOWSRequest( requestWrapper );
                proxyHandler.doRequestValidation( requestWrapper, user, owsProxyRequest );
            } catch ( GeneralSecurityException e ) {
                if( LOG.getLevel() == ILogger.LOG_DEBUG ){
                    Thread.dumpStack();
                }
                String userName = null;
                String password = null;
                if( user!= null ){
                    userName = user.getName();
                    password = user.getPassword();
                }
                LOG.logDebug( "User: " + userName + " with password: " + password
                              + " couldn't get authentification because: " + e.getMessage() );
                throw new OGCWebServiceException( e.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST );
            }
        }
    }

    private void authorizeResponse( User user, OGCWebServiceRequest owsProxyRequest,
                                    ServletResponseWrapper responseWrapper )
                            throws OGCWebServiceException {
        // checking the authorization of the user.
        if ( this.proxyHandler != null ) {
            try {
                proxyHandler.doResponseValidation( responseWrapper, user, owsProxyRequest );
            } catch ( GeneralSecurityException e ) {
                String userName = null;
                String password = null;
                if( user!= null ){
                    userName = user.getName();
                    userName = user.getPassword();
                }
                LOG.logDebug( "User: " + userName + " with password: " + password
                              + " couldn't get authentification because: " + e.getMessage() );
                throw new OGCWebServiceException( e.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST );
            } catch ( IOException e ) {
                LOG.logDebug( "couldn't get an outputstream for the response because: "
                              + e.getMessage() );
                throw new OGCWebServiceException( e.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST );
            }
        }
    }

    /**
     * Contacts the localWFS to find a rim:ExtrinsicObject which contains the
     * {@link GetRepositoryItem#getRepositoryItemID()} and retrieves it's
     * app:RegistryObject/app:extrinsicObject/app:ExtrinsicObject/app:object. The value in this property will then be
     * written to the response stream (e.g. sent to the requester).
     * 
     * @param response
     *            the response object to which the data will be returned
     * @param reposItem
     *            the created OGCRequest
     * @throws IOException
     *             if the stream cannot be retrieved from the resonse
     * @throws OGCWebServiceException
     *             if something went wrong while processing the request.
     */
    private void handleGetRepositoryItem( ServletResponse response, GetRepositoryItem reposItem, User user )
                            throws IOException, OGCWebServiceException {
        // Some properterypaths which are used for the creation of a complex filter.
        QualifiedName registryObject = new QualifiedName( "app", "RegistryObject", appURI );
        Expression iduriExpr = new PropertyName( new QualifiedName( "app", "iduri", appURI ) );
        Expression idLiteral = new Literal( reposItem.getRepositoryItemID().toString() );
        PropertyIsCOMPOperation idOperator = new PropertyIsCOMPOperation( OperationDefines.PROPERTYISEQUALTO,
                                                                          iduriExpr, idLiteral );
        ComplexFilter idFilter = new ComplexFilter( idOperator );

        FeatureCollection featureCollectionOnId = null;
        try {
            FeatureResult fr = sendWFSGetFeature( registryObject, idFilter );
            if ( fr != null ) {
                featureCollectionOnId = (FeatureCollection) fr.getResponse();
            }
        } catch ( OGCWebServiceException e ) {
            throw new OGCWebServiceException( "The requested item " + reposItem.getRepositoryItemID()
                                              + " could not be retrieved from the csw backend: " + e.getMessage(),
                                              CSWExceptionCode.WRS_NOTFOUND );
        }
        String numbOfFeatures = featureCollectionOnId.getAttribute( "numberOfFeatures" );
        int featureCount = 0;
        try {
            featureCount = Integer.parseInt( numbOfFeatures );
            LOG.logDebug( "the number of features in the GetFeature was: "
                          + featureCount );
        } catch ( NumberFormatException nfe ) {
            // nottin
        }
        // Check the number of hits we've found, if the id allready exists it means we want to set the status of the
        // object to invalid.
        // String newID = id;
        if ( featureCount > 1 ) {
            throw new OGCWebServiceException( "The id : " + reposItem.getRepositoryItemID()
                                              + " is not unique. This repositoryItem can therefore not be retrieved.",
                                              CSWExceptionCode.WRS_NOTFOUND );
        } else if ( featureCount == 0 ) {
            throw new OGCWebServiceException(
                                              "The id: "
                                                                      + reposItem.getRepositoryItemID()
                                                                      + " corresponds to no rim:ExtrinsicObject. This repositoryItem can therefore not be retrieved.",
                                              CSWExceptionCode.WRS_NOTFOUND );

        } else {
            Feature f = featureCollectionOnId.getFeature( 0 );
            if ( f != null ) {
                PropertyPath pp = PropertyPathFactory.createPropertyPath( registryObject );
                pp.append( PropertyPathFactory.createPropertyPathStep( new QualifiedName( "app", "extrinsicObject",
                                                                                          appURI ) ) );
                pp.append( PropertyPathFactory.createPropertyPathStep( new QualifiedName( "app", "ExtrinsicObject",
                                                                                          appURI ) ) );
                pp.append( PropertyPathFactory.createPropertyPathStep( new QualifiedName( "app", "object", appURI ) ) );
                FeatureProperty retrievedObject = null;
                try {
                    retrievedObject = f.getDefaultProperty( pp );
                } catch ( PropertyPathResolvingException ppre ) {
                    throw new OGCWebServiceException(
                                                      "The id: "
                                                                              + reposItem.getRepositoryItemID()
                                                                              + " has no repository item stored, there is nothing to be retrieved.",
                                                      CSWExceptionCode.WRS_NOTFOUND );

                }
                if ( retrievedObject == null || retrievedObject.getValue() == null ) {
                    throw new OGCWebServiceException(
                                                      "The id: "
                                                                              + reposItem.getRepositoryItemID()
                                                                              + " has no repository item stored, there is nothing to be retrieved.",
                                                      CSWExceptionCode.WRS_NOTFOUND );
                }

                String repositoryItem = (String) retrievedObject.getValue();
                LOG.logDebug( "found the repositoryItem: " + repositoryItem );

                pp = PropertyPathFactory.createPropertyPath( registryObject );
                pp.append( PropertyPathFactory.createPropertyPathStep( new QualifiedName( "app", "extrinsicObject",
                                                                                          appURI ) ) );
                pp.append( PropertyPathFactory.createPropertyPathStep( new QualifiedName( "app", "ExtrinsicObject",
                                                                                          appURI ) ) );
                pp.append( PropertyPathFactory.createPropertyPathStep( new QualifiedName( "app", "mimeType", appURI ) ) );
                FeatureProperty mimeType = null;
                try {
                    mimeType = f.getDefaultProperty( pp );
                } catch ( PropertyPathResolvingException ppre ) {
                    LOG.logError( "The mimetype value (of the GetRepositoryItem: " + reposItem.getRepositoryItemID()
                                  + ") was not set, setting content header to 'application/xml' " );
                }
                if ( mimeType == null || mimeType.getValue() == null ) {
                    LOG.logError( "The mimetype value (of the GetRepositoryItem: " + reposItem.getRepositoryItemID()
                                  + ") was not set, setting content header to 'application/xml' " );
                } else {
                    response.setContentType( (String) mimeType.getValue() );
                }
                ServletResponseWrapper wrapper = new ServletResponseWrapper( (HttpServletResponse) response );
                authorizeResponse( user, reposItem, wrapper );
                PrintWriter writer = new PrintWriter( new OutputStreamWriter( response.getOutputStream() ) );
                writer.write( repositoryItem );
                writer.flush();
                writer.close();
            }
        }

    }

    /**
     * Generates and sends a GetFeature to the localwfs (which was instantiated in the init method).
     * 
     * @param registryObject
     *            the QName of the registryObject e.g. app:RegistryObject (xmlns:app="http://www.deegree.org/app")
     * @param filter
     *            a ogc:Filter representation containing the (app:iduri isequal requestID) mapping.
     * @return the FeatureResult of the given filter or <code>null</code> if something went wrong.
     * @throws OGCWebServiceException
     *             thrown if the localWFS encounters any problems
     */
    private FeatureResult sendWFSGetFeature( QualifiedName registryObject, ComplexFilter filter )
                            throws OGCWebServiceException {
        Query q = Query.create( registryObject, filter );
        GetFeature gfwl = GetFeature.create( "1.1.0", "0", RESULT_TYPE.RESULTS, "text/xml; subtype=gml/3.1.1",
                                             "no_handle", -1, 0, -1, -1, new Query[] { q } );
        if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
            try {
                GetFeatureDocument gd = XMLFactory.export( gfwl );
                LOG.logDebug( " The getFeature:\n" + gd.getAsPrettyString() );
            } catch ( IOException e ) {
                LOG.logError( "CSW (Ebrim) GetRepositoryItem-Filter:  An error occurred while trying to get a debugging output for the generated GetFeatureDocument: "
                              + e.getMessage() );
            } catch ( XMLParsingException e ) {
                LOG.logError( "CSW (Ebrim) GetRepositoryItem-Filter:  An error occurred while trying to get a debugging output for the generated GetFeatureDocument: "
                              + e.getMessage() );
            }
        }

        Object response = localWFS.doService( gfwl );
        if ( response instanceof FeatureResult ) {
            return (FeatureResult) response;
        }
        LOG.logDebug( " got no valid response from the localwfs, returning null" );
        return null;
    }

    /**
     * Sends the passed <tt>OGCWebServiceException</tt> to the calling client and flushes/closes the writer.
     * 
     * @param responseWriter
     *            to write the message of the exception to
     * @param e
     *            the exception to 'send' e.g. write to the stream.
     * @throws IOException
     *             if a writer could not be created from the response.
     */
    private void sendException( ServletResponse response, OGCWebServiceException e )
                            throws IOException {
        if( LOG.getLevel() == ILogger.LOG_DEBUG ){
            Thread.dumpStack();
        }
        response.setContentType( "application/xml" );
        LOG.logDebug( "Sending OGCWebServiceException to client with message: " + e.getMessage() );
        Document doc = XMLTools.create();

        XMLFragment frag = new XMLFragment( doc.createElementNS( CommonNamespaces.OWSNS.toString(),
                                                                 "ows:ExceptionReport" ) );
        Element message = null;
        ExceptionCode code = e.getCode();
        if ( code == null ) {
            code = CSWExceptionCode.WRS_INVALIDREQUEST;
        }
        if ( code == CSWExceptionCode.WRS_INVALIDREQUEST ) {
            message = XMLTools.appendElement( frag.getRootElement(), CommonNamespaces.WRS_EBRIMNS, code.value );
        } else {
            message = XMLTools.appendElement( frag.getRootElement(), CommonNamespaces.WRS_EBRIMNS,
                                              CSWExceptionCode.WRS_NOTFOUND.value );
        }
        XMLTools.setNodeValue( message, e.getMessage() );
        PrintWriter writer = response.getWriter();
        writer.write( frag.getAsPrettyString() );
        writer.flush();
        writer.close();
    }

}
