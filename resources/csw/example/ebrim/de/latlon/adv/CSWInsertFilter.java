//$HeadURL: svn+ssh://mschneider@svn.wald.intevation.org/deegree/base/trunk/resources/eclipse/svn_classfile_header_template.xml $
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.ContentDisposition;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deegree.enterprise.servlet.ServletRequestWrapper;
import org.deegree.enterprise.servlet.ServletResponseWrapper;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.CharsetUtils;
import org.deegree.framework.util.IDGenerator;
import org.deegree.framework.util.WebappResourceResolver;
import org.deegree.framework.xml.InvalidConfigurationException;
import org.deegree.framework.xml.XMLException;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcbase.ExceptionCode;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.csw.CSWExceptionCode;
import org.deegree.ogcwebservices.csw.CatalogueService;
import org.deegree.ogcwebservices.csw.configuration.CatalogueConfiguration;
import org.deegree.ogcwebservices.csw.manager.Insert;
import org.deegree.ogcwebservices.csw.manager.InsertResults;
import org.deegree.ogcwebservices.csw.manager.Manager;
import org.deegree.ogcwebservices.csw.manager.Operation;
import org.deegree.ogcwebservices.csw.manager.Transaction;
import org.deegree.ogcwebservices.csw.manager.TransactionResult;
import org.deegree.ogcwebservices.csw.manager.TransactionResultDocument;
import org.deegree.ogcwebservices.csw.manager.XMLFactory;
import org.deegree.security.GeneralSecurityException;
import org.deegree.security.drm.model.User;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * The <code>CSWInsertFilter</code> class is able to handle an incoming csw/wrs ebrim Transaction.
 * This extra filter is necessary because the insertion of an ebrim object is handled different then
 * the original csw:Insert.
 * <p>
 * The first difference is the usage of the form/multipart header which allows for ExtrinsicObjects
 * to be defined outside the message Body and thus must be handled seperatly.
 * </p>
 * <p>
 * A second difference results after the insertion of an Object for every update and insertion leads
 * to the extra insertion of an auditableEvent. The AuditableEvents result in an audit-trail (a
 * version coontrol history ) for each inserted or updated app:RegistryObject.
 * </p>
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 1.13 $, $Date: 2007-11-27 12:50:25 $
 * 
 */

public class CSWInsertFilter implements Filter {

    private static ILogger LOG = LoggerFactory.getLogger( CSWInsertFilter.class );

    private Manager transactionManager = null;

    private URI appURI;

    private OWSProxyHandler proxyHandler = null;

    public void init( FilterConfig config )
                            throws ServletException {
        String configLocation = config.getInitParameter( "csw.config" );
        try {
            URL configURL = WebappResourceResolver.resolveFileLocation( configLocation, config.getServletContext(), LOG );
            CatalogueConfiguration cswConfig = CatalogueConfiguration.createConfiguration( configURL );
            CatalogueService cswService = CatalogueService.create( cswConfig );
            transactionManager = cswService.getManager();
            if ( transactionManager.getWfsService() == null ) {
                LOG.logError( "CSW (Ebrim) Insert-Filter: The InserFilter has no access to the localWFS" );
            }
        } catch ( MalformedURLException e ) {
            LOG.logError( "Could not initiate CSWInsertfilter: " + e.getMessage() );
        } catch ( IOException e ) {
            LOG.logError( "Could not initiate CSWInsertfilter: " + e.getMessage() );
        } catch ( SAXException e ) {
            LOG.logError( "Could not initiate CSWInsertfilter: " + e.getMessage() );
        } catch ( InvalidConfigurationException e ) {
            LOG.logError( "Could not initiate CSWInsertfilter: " + e.getMessage() );
        } catch ( OGCWebServiceException e ) {
            LOG.logError( "Could not initiate CSWInsertfilter: " + e.getMessage() );
        }
        if ( transactionManager != null ) {
            try {
                appURI = new URI( "http://www.deegree.org/app" );
                proxyHandler = new OWSProxyHandler( config );
            } catch ( URISyntaxException e ) {
                // This never happens but whatever
            } catch ( InvalidParameterException ipe ) {
                LOG.logInfo( "CSW (Ebrim) Insert-Filter: couldn't create an OWSProxyHandler, so no user authentification available, because: "
                             + ipe.getMessage() );
            }
            LOG.logInfo( "CSW (Ebrim) Insert Servlet Filter successfully initialized" );
        } else {
            LOG.logInfo( "CSW (Ebrim) Insert Servlet Filter was not initialized" );
        }
    }

    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
                            throws IOException, ServletException {
        if ( transactionManager == null ) {
            LOG.logInfo( "CSW (Ebrim) Insert-Filter: Local Catalogue service is not instantiated, therefore the CSW-Insert filter can not be used" );
            sendException( response, new OGCWebServiceException( "The transactionManager is not configured "
                                                                 + "so not accepting any requests.",
                                                                 CSWExceptionCode.WRS_NOTIMPLEMENTED ) );
            // chain.doFilter( request, response );
            return;
        }
        response.setCharacterEncoding( "UTF-8" );
        Map<String, String[]> params = request.getParameterMap();

        if ( params.size() != 0 ) {
            chain.doFilter( request, response );
            return;
        }

        // so no get request (or a get Request without parameters)
        ServletRequestWrapper requestWrapper = null;
        if ( request instanceof ServletRequestWrapper ) {
            LOG.logDebug( "the incoming request is actually an org.deegree.enterprise.servlet.RequestWrapper, so not creating new instance." );
            requestWrapper = (ServletRequestWrapper) request;
        } else {
            requestWrapper = new ServletRequestWrapper( (HttpServletRequest) request );
        }

        Transaction transaction = null;
        BufferedReader reader = new BufferedReader( new InputStreamReader( requestWrapper.getInputStream() ) );
        String firstLine = reader.readLine();
        LOG.logDebug( "first line of request: " + firstLine );
        if ( firstLine == null ) {
            LOG.logInfo( "CSW (Ebrim) Insert-Filter: no request characters found, not handling request" );
            // chain.doFilter( requestWrapper, response );
            sendException( response, new OGCWebServiceException( "no request characters found, not handling request",
                                                                 CSWExceptionCode.WRS_INVALIDREQUEST ) );
            return;
        }
        if ( ILogger.LOG_DEBUG == LOG.getLevel() ) {
            LOG.logDebug( "OUTPUTING as Strings" );
            LOG.logDebug( firstLine );
            while ( reader.ready() ) {
                LOG.logDebug( reader.readLine() );
            }
        }

        LOG.logDebug( "GetContentype(): " + requestWrapper.getContentType() );

        // These variables will be set according to the request properties
        CSWSOAPHandler soapHandler = new CSWSOAPHandler();
        if ( requestWrapper.getContentType() != null
             && requestWrapper.getContentType().contains( "multipart/form-data" ) ) {
            try {
                // because we have some multiparts, the soap/owsproxy handling is done in the
                // #handleMultiparts method
                transaction = handleMultiparts( requestWrapper, soapHandler );
                if ( transaction == null ) {
                    LOG.logDebug( "could not generate a Transaction object out of the multiparts, giving request to the chain" );
                    sendException(
                                   response,
                                   new OGCWebServiceException(
                                                               "Could not generate a Transaction object out of the multiparts",
                                                               CSWExceptionCode.WRS_INVALIDREQUEST ) );
                    // chain.doFilter( request, response );
                    return;
                }
            } catch ( OGCWebServiceException e ) {
                sendException( response, e );
                return;
            }
        } else {
            XMLFragment doc = null;
            try {
                doc = new XMLFragment( new InputStreamReader( requestWrapper.getInputStream() ),
                                       "http://some_server.org" );
            } catch ( SAXException e ) {
                LOG.logDebug( "couldn't create an xml Fragment of the incoming request, so not handling. "
                              + e.getMessage() );
                sendException( response,
                               new OGCWebServiceException( "Couldn't create an xml Fragment of the incoming request",
                                                           CSWExceptionCode.WRS_INVALIDREQUEST ) );
                // chain.doFilter( requestWrapper, response );
                return;
            }
            Element rootElement = doc.getRootElement();
            String ns = rootElement.getNamespaceURI();
            LOG.logDebug( "Decoded request as xml:\n " + doc.getAsPrettyString() );

            if ( !CommonNamespaces.CSWNS.toASCIIString().equals( ns )
                 && !CommonNamespaces.W3SOAP_ENVELOPE.toASCIIString().equals( ns ) ) {
                LOG.logDebug( "The namespace of the root element (" + ns + ") is neither: "
                              + CommonNamespaces.W3SOAP_ENVELOPE + " nor: " + CommonNamespaces.CSWNS.toASCIIString()
                              + ", so not using the servlet filter " );
                sendException( response, new OGCWebServiceException( "Service not known",
                                                                     CSWExceptionCode.WRS_INVALIDREQUEST ) );
                // chain.doFilter( requestWrapper, response );
                return;
            }
            soapHandler.setIncomingRequest( doc );
            try {
                doc = soapHandler.createCSWRequestFromSOAP();
                if ( doc == null ) {
                    LOG.logDebug( "the soap handler returned a null valued XMLFragment, cannot be!" );
                    sendException( response,
                                   new OGCWebServiceException( "The body of your request could not be parsed.",
                                                               CSWExceptionCode.WRS_INVALIDREQUEST ) );
                }
                rootElement = doc.getRootElement();
                LOG.logDebug( "after soap: " + doc.getAsPrettyString() );

                // reset the stream to handle the request without the soap
                if ( soapHandler.isSOAPRequest() ) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream( 50000 );
                    OutputStreamWriter osw = new OutputStreamWriter( bos, CharsetUtils.getSystemCharset() );
                    doc.write( osw );
                    requestWrapper.setInputStreamAsByteArray( bos.toByteArray() );
                }

                // checking the authorization of the user.
                OGCWebServiceRequest owsProxyRequest = null;
                if ( this.proxyHandler != null ) {
                    owsProxyRequest = proxyHandler.createOWSRequest( requestWrapper );
                    authorizeRequest( soapHandler, owsProxyRequest, requestWrapper );
                } else {
                    LOG.logDebug( "the proxyHandler is not defined so no user Authentification." );
                    sendException(
                                   response,
                                   new OGCWebServiceException(
                                                               "The proxyHandler is not defined so not accepting any requests.",
                                                               CSWExceptionCode.WRS_NOTIMPLEMENTED ) );
                    return;
                }

                if ( !"Transaction".equals( rootElement.getLocalName() ) ) {
                    LOG.logDebug( "The localname of the root element is not: 'Transaction', so not using the servlet filter. " );
                    // Try the user authorization of the response
                    ServletResponseWrapper responseWrapper = new ServletResponseWrapper( (HttpServletResponse) response );
                    responseWrapper.setCharacterEncoding( "UTF-8" );
                    chain.doFilter( requestWrapper, responseWrapper );
                    if ( this.proxyHandler != null ) {
                        authorizeResponse( soapHandler, owsProxyRequest, responseWrapper );
                    }
                    LOG.logDebug( "got following response: " );
                    OutputStream os = responseWrapper.getOutputStream();
                    String encoding = request.getCharacterEncoding();
                    LOG.logDebug( "the request uses following character encoding: " + encoding );
                    if ( !"UTF-8".equals( encoding ) ) {
                        LOG.logDebug( "the request uses following character encoding: " + encoding
                                      + " setting to UTF-8." );
                        encoding = "UTF-8";
                    }

                    String responseString = ( (ServletResponseWrapper.ProxyServletOutputStream) os ).toString( encoding );
                    LOG.logDebug( "got the string: " + responseString );
                    // byte[] b = ( (ServletResponseWrapper.ProxyServletOutputStream) os
                    // ).toByteArray();
                    // LOG.logDebug( "got the array: " + b.length );
                    // LOG.logDebug( "got the array: " );

                    os.close();
                    // ServletOutputStream so = response.getOutputStream();
                    // so.write( b );
                    // so.flush();
                    // so.close();
                    PrintWriter writer = response.getWriter();
                    writer.write( responseString );
                    writer.flush();
                    writer.close();
                    // responseWrapper.flushBuffer();
                    return;
                }

                // create the transaction
                transaction = Transaction.create( Long.toString( IDGenerator.getInstance().generateUniqueID() ),
                                                  rootElement );

            } catch ( OGCWebServiceException e ) {
                sendException( response, e );
                return;
            }
        }

        try {
            LOG.logDebug( "The request is a csw:Transaction parsed and handled by the CSWInsertFilter" );
            if ( transaction == null ) {
                LOG.logDebug( "the transaction is null, this cannot be" );
                sendException(
                               response,
                               new OGCWebServiceException(
                                                           "Failed to handle your transaction, please check your request.",
                                                           CSWExceptionCode.WRS_INVALIDREQUEST ) );
            }
            if ( soapHandler == null ) {
                LOG.logDebug( "the soaphandler is null, this cannot be" );
                sendException(
                               response,
                               new OGCWebServiceException(
                                                           "Failed to handle your transaction, please check your request.",
                                                           CSWExceptionCode.WRS_INVALIDREQUEST ) );
            }
            TransactionResult result = handleTransactions( transaction, soapHandler.getUserName() );
            if ( result != null ) {
                LOG.logDebug( "Creating xml representation of the csw-transaction " );
                TransactionResultDocument resultDoc = XMLFactory.export( result );
                String prettyString = resultDoc.getAsPrettyString();
                LOG.logDebug( "The result of the csw-transaction was not null, it has following xml-structure: \n"
                              + prettyString );
                response.setContentType( "application/xml" );
                ServletResponseWrapper responseWrapper = new ServletResponseWrapper( (HttpServletResponse) response );

                PrintWriter writer = new PrintWriter( new OutputStreamWriter( responseWrapper.getOutputStream() ) );
                writer.write( prettyString );
                writer.flush();
                if ( this.proxyHandler != null && soapHandler != null ) {
                    authorizeResponse( soapHandler, transaction, responseWrapper );
                }
                writer.close();
                writer = response.getWriter();
                writer.write( prettyString );
                writer.flush();
                writer.close();
                return;
            }
        } catch ( OGCWebServiceException e ) {
            sendException( response, e );
            return;
        } catch ( XMLParsingException e ) {
            sendException( response, new OGCWebServiceException( e.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST ) );
            return;
        }
        if ( response.isCommitted() ) {
            return;
        }
        // chain.doFilter( requestWrapper, response );

    }

    private void authorizeRequest( CSWSOAPHandler soapHandler, OGCWebServiceRequest owsProxyRequest,
                                   ServletRequestWrapper requestWrapper )
                            throws OGCWebServiceException {
        // checking the authorization of the user.
        if ( this.proxyHandler != null ) {
            try {
                User user = proxyHandler.authentificateFromUserPw( soapHandler.getUserName(), soapHandler.getPassword() );

                // owsProxyRequest = proxyHandler.createOWSRequest( requestWrapper );
                proxyHandler.doRequestValidation( requestWrapper, user, owsProxyRequest );
            } catch ( GeneralSecurityException e ) {
                // e.printStackTrace( System.out );
                LOG.logDebug( "User: " + soapHandler.getUserName() + " with password: " + soapHandler.getPassword()
                              + " couldn't get authentification because: " + e.getMessage() );
                throw new OGCWebServiceException( e.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST );
            }
        }
    }

    private void authorizeResponse( CSWSOAPHandler soapHandler, OGCWebServiceRequest owsProxyRequest,
                                    ServletResponseWrapper responseWrapper )
                            throws OGCWebServiceException {
        // checking the authorization of the user.
        if ( this.proxyHandler != null ) {
            try {
                User user = proxyHandler.authentificateFromUserPw( soapHandler.getUserName(), soapHandler.getPassword() );
                proxyHandler.doResponseValidation( responseWrapper, user, owsProxyRequest );
            } catch ( GeneralSecurityException e ) {
                LOG.logDebug( "User: " + soapHandler.getUserName() + " with password: " + soapHandler.getPassword()
                              + " couldn't get authentification because: " + e.getMessage() );
                throw new OGCWebServiceException( e.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST );
            } catch ( IOException e ) {
                LOG.logDebug( "couldn't get an outputstream for the repsponse because: " + e.getMessage() );
                throw new OGCWebServiceException( e.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST );
            }
        }
    }

    public void destroy() {
        // implements nottin.
    }

    /**
     * Sends the passed <tt>OGCWebServiceException</tt> to the calling client and flushes/closes
     * the writer.
     * 
     * @param responseWriter
     *            to write the message of the exception to
     * @param e
     *            the exception to 'send' e.g. write to the stream.
     * @throws IOException
     *             if an error occurred while getting the writer of the response.
     */
    private void sendException( ServletResponse response, OGCWebServiceException e )
                            throws IOException {
        if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
            Thread.dumpStack();
        }
        if ( response instanceof ServletResponseWrapper ) {
            ( (ServletResponseWrapper) response ).reset();
        }
        response.setContentType( "application/xml" );
        PrintWriter writer = response.getWriter();
        LOG.logInfo( "CSW (Ebrim) Insert-Filter: Sending OGCWebServiceException to client with message: ."
                     + e.getMessage() );
        Document doc = XMLTools.create();

        XMLFragment frag = new XMLFragment( doc.createElementNS( CommonNamespaces.OWSNS.toASCIIString(),
                                                                 "ows:ExceptionReport" ) );
        ExceptionCode code = e.getCode();
        String exceptionCode = CSWExceptionCode.WRS_INVALIDREQUEST.value;
        if ( code != null && code.value != null ) {
            exceptionCode = code.value;
        }
        Element soapFailed = XMLTools.appendElement( frag.getRootElement(), CommonNamespaces.WRS_EBRIMNS, exceptionCode );
        XMLTools.setNodeValue( soapFailed, e.getMessage() );
        writer.write( frag.getAsPrettyString() );
        writer.flush();
        writer.close();
    }

    /**
     * This method handles the operations in the given Transaction that is, it splits the
     * Transaction into update/delete and insert operations. The update and delete operations are
     * gathered as long as there are no insert operations. If an Insert opertation is encountered
     * the pending delete/update operations are first send to the csw after which all records in the
     * insert operation are sequently inserted into the registry, using the
     * {@link InsertTransactionHandler}. Note this method guarantees the sequential processing of
     * the operations in their defined order.
     * 
     * @param transaction
     *            bean representation of the incoming request.
     * @param username
     *            of the user which inserts the registryObjects.
     * @throws OGCWebServiceException
     *             if an error occurred while hanlding an operations.
     */
    private TransactionResult handleTransactions( Transaction transaction, String username )
                            throws OGCWebServiceException {
        List<Operation> ops = transaction.getOperations();
        // find all the ids which were referenced by the transaction.
        List<Operation> pendingOps = new ArrayList<Operation>();
        List<Node> briefInsertedRecords = new ArrayList<Node>();
        // 0 = insert
        // 1 = delete
        // 2 = update
        int[] resultValues = new int[3];
        for ( Operation op : ops ) {
            if ( "Insert".equalsIgnoreCase( op.getName() ) ) {
                // First do all transactions which were not insert(s)
                if ( pendingOps.size() > 0 ) {
                    TransactionResult tr = sendPendingOperations( transaction, pendingOps );
                    resultValues[0] += tr.getTotalInserted();
                    resultValues[1] += tr.getTotalDeleted();
                    resultValues[2] += tr.getTotalUpdated();
                }
                // handle all records inside the insert operation, e.g. set the
                // app:RegistryObject/app:status to invalid
                // if the id of the object allready exists.
                InsertTransactionHandler handler = new InsertTransactionHandler( transaction, (Insert) op, appURI,
                                                                                 username );
                briefInsertedRecords.addAll( handler.handleInsertTransaction( this.transactionManager, resultValues ) );

            } else {
                pendingOps.add( op );
            }
        }
        if ( pendingOps.size() > 0 ) {
            TransactionResult tr = sendPendingOperations( transaction, pendingOps );
            resultValues[0] += tr.getTotalInserted();
            resultValues[1] += tr.getTotalDeleted();
            resultValues[2] += tr.getTotalUpdated();
        }
        LOG.logDebug( "Number of brief inserted records: " + briefInsertedRecords.size() );
        InsertResults iresults = new InsertResults( briefInsertedRecords );
        return new TransactionResult( transaction, resultValues[0], resultValues[1], resultValues[2], iresults );
    }

    /**
     * Sends all operations in the list to the csw.
     * 
     * @param originalTransaction
     *            used to get the version, id and vendorspecific params from.
     * @param pendingOps
     *            the list of delete and update operations.
     * @return the Result of the csw.
     * @throws OGCWebServiceException
     */
    private TransactionResult sendPendingOperations( Transaction originalTransaction, List<Operation> pendingOps )
                            throws OGCWebServiceException {
        Transaction transaction = new Transaction( originalTransaction.getVersion(), originalTransaction.getId(),
                                                   originalTransaction.getVendorSpecificParameters(), pendingOps, false );

        return transactionManager.transaction( transaction );
    }

    /**
     * This method handles the multiparts of a ServletRequest. This method is called when the
     * content-type:form/multipart header is set. Inside the multiparts different extrinsice object
     * representations (as xml) may reside. They will be added inside the appropriate
     * InsertOperation (found by the extrinsicObject's/@id) and inserted in the xml representation
     * of the referenced (again while looking at the /@id attribute) extrinicObject. The definition
     * of the extrinsicObject (e.g. inside the multipart) will be put inside the following
     * xml-Element: 'deegreecsw:DescribedObject', at the end of the nodelist of the
     * 'rim:ExtrinsicOjbects'.
     * 
     * @param request
     *            the actual HttpServletRequest.
     * @param soapHandler
     * @return the Transaction Representation of the incoming request
     * @throws OGCWebServiceException
     *             if an exception occurred while processing the mime parts.
     */
    private Transaction handleMultiparts( HttpServletRequest request, CSWSOAPHandler soapHandler )
                            throws OGCWebServiceException {
        Transaction trans = null;
        // StreamDataSource sds = new StreamDataSource( request );
        try {
            ByteArrayDataSource bads = new ByteArrayDataSource( request.getInputStream(), "application/xml" );
            LOG.logInfo( "CSW (Ebrim) Insert-Filter: Setting the 'mail.mime.multipart.ignoremissingendboundary' System property to false." );
            System.setProperty( "mail.mime.multipart.ignoremissingendboundary", "false" );
            MimeMultipart multi = new MimeMultipart( bads );
            Map<String, Insert> insertOpContainingExtObj = new HashMap<String, Insert>();

            for ( int i = 0; i < multi.getCount(); i++ ) {
                MimeBodyPart content = (MimeBodyPart) multi.getBodyPart( i );
                LOG.logDebug( "multipart (" + ( i + 1 ) + " of " + multi.getCount() + ") content id: "
                              + content.getContentID() );
                LOG.logDebug( "multipart (" + ( i + 1 ) + " of " + multi.getCount() + ") content type: "
                              + content.getContentType() );
                String contentType = content.getContentType();
                if ( !( contentType.contains( "application/xml" ) || contentType.contains( "text/xml" ) ) ) {
                    throw new OGCWebServiceException(
                                                      "Other than xml-encoded data can not be handled in the multiparts",
                                                      CSWExceptionCode.WRS_INVALIDREQUEST );
                }
                String[] names = content.getHeader( "Content-Disposition" );
                String nameID = null;
                if ( names != null ) {
                    for ( String name : names ) {
                        ContentDisposition cd = new ContentDisposition( name );
                        String nm = cd.getParameter( "name" );
                        if ( nm != null ) {
                            nameID = nm;
                            break;
                        }
                    }
                }

                if ( nameID == null ) {
                    nameID = content.getContentID();
                    if ( nameID == null ) {
                        throw new OGCWebServiceException( "Exactly one 'name' parameter must be set in the header.",
                                                          CSWExceptionCode.WRS_INVALIDREQUEST );
                    }
                }
                LOG.logDebug( "Working with multipart (" + ( i + 1 ) + " of " + multi.getCount() + ") content name:"
                              + nameID );
                LOG.logDebug( "multipart (" + ( i + 1 ) + " of " + multi.getCount() + ") lineCount: "
                              + content.getLineCount() );
                LOG.logDebug( "multipart (" + ( i + 1 ) + " of " + multi.getCount() + ") encoding: "
                              + content.getEncoding() );
                if ( "Transaction".equalsIgnoreCase( nameID ) ) {
                    InputStream contentIS = null;
                    if ( !"UTF-8".equalsIgnoreCase( content.getEncoding() ) ) {
                        contentIS = MimeUtility.decode( content.getInputStream(), content.getEncoding() );
                    } else {
                        contentIS = content.getInputStream();
                    }

                    // first get an xml fragment of the request and check if it is a soap.
                    XMLFragment doc = new XMLFragment( new InputStreamReader( contentIS ), XMLFragment.DEFAULT_URL );
                    LOG.logDebug( "Decoded first multiPart:\n " + doc.getAsPrettyString() );
                    soapHandler.setIncomingRequest( doc );

                    doc = soapHandler.createCSWRequestFromSOAP();
                    Element rootElement = doc.getRootElement();

                    String ns = rootElement.getNamespaceURI();
                    if ( !CommonNamespaces.CSWNS.toASCIIString().equals( ns )
                         || !"Transaction".equals( rootElement.getLocalName() ) ) {
                        // create the transaction
                        LOG.logDebug( "The namespace of the root element (" + ns + ") is not"
                                      + CommonNamespaces.CSWNS.toASCIIString()
                                      + ", or the request isn't a Transaction so not creating a Transaction object. " );
                        // set the first multipart to the multipart without the soap.
                        content.setContent( doc.getAsPrettyString(), "application/xml" );
                        return null;
                    }
                    trans = Transaction.create( "0", rootElement );

                    if ( this.proxyHandler != null ) {
                        ServletRequestWrapper wrapper = new ServletRequestWrapper( request );
                        ByteArrayOutputStream bos = new ByteArrayOutputStream( 50000 );
                        OutputStreamWriter osw = new OutputStreamWriter( bos, CharsetUtils.getSystemCharset() );
                        doc.write( osw );
                        wrapper.setInputStreamAsByteArray( bos.toByteArray() );
                        authorizeRequest( soapHandler, trans, wrapper );
                    } else {
                        LOG.logDebug( "the proxyHandler is not defined so no user Authentification." );
                    }

                    List<Operation> ops = trans.getOperations();
                    // find all the ids which were referenced by the transaction.
                    int countInserts = 0;
                    for ( Operation op : ops ) {

                        if ( "Insert".equalsIgnoreCase( op.getName() ) ) {

                            Map<String, Element> objects = ( (Insert) op ).getExtrinsicObjects();
                            Set<String> keys = objects.keySet();
                            LOG.logDebug( "This insert operation (" + ++countInserts
                                          + ") contains the following keys: " + keys );
                            // set the keys to the operations they were referenced in.
                            for ( String key : keys ) {
                                if ( insertOpContainingExtObj.containsKey( key ) ) {
                                    throw new OGCWebServiceException( "The following key is not unique, " + key
                                                                      + ", the transaction therefore failed.",
                                                                      CSWExceptionCode.WRS_INVALIDREQUEST );
                                }
                                insertOpContainingExtObj.put( key, (Insert) op );
                            }
                        }
                    }
                } else {
                    if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
                        InputStream contentIS = null;

                        if ( !"UTF-8".equalsIgnoreCase( content.getEncoding() ) ) {
                            contentIS = MimeUtility.decode( content.getInputStream(), content.getEncoding() );
                        } else {
                            contentIS = content.getInputStream();
                        }

                        BufferedReader reader = new BufferedReader( new InputStreamReader( contentIS ) );
                        String firstLine = reader.readLine();
                        if ( !reader.ready() || firstLine == null ) {
                            LOG.logInfo( "CSW (Ebrim) Insert-Filter: no characters found in multipart, is this an error?" );
                        }
                        LOG.logDebug( "first line of multipart: " + firstLine );
                    }
                    LOG.logDebug( "found Keys: " + insertOpContainingExtObj.keySet() );
                    if ( insertOpContainingExtObj.size() > 0 ) {
                        if ( insertOpContainingExtObj.containsKey( nameID ) ) {
                            LOG.logDebug( "found the key to insert (" + nameID
                                          + ") trying to find associated multipart." );
                            Insert insertOperation = insertOpContainingExtObj.remove( nameID );
                            Map<String, Element> extrinsicObjects = insertOperation.getExtrinsicObjects();
                            // find the extrinsicobject element and add the multipartmime type
                            // containing it's id.
                            if ( extrinsicObjects.containsKey( nameID ) ) {
                                List<Element> records = insertOperation.getRecords();
                                Element extrinsicObject = extrinsicObjects.get( nameID );
                                if ( records.contains( extrinsicObject ) ) {
                                    int objectIndex = records.indexOf( extrinsicObject );
                                    LOG.logDebug( "removing extrinsicObject at index: " + objectIndex
                                                  + " from the records list" );
                                    records.remove( extrinsicObject );
                                    Element handledExtrinsicObject = addDescribedExtrinsicObject( content,
                                                                                                  extrinsicObject );
                                    if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
                                        XMLFragment testFrag = new XMLFragment( handledExtrinsicObject );
                                        LOG.logDebug( "The extrinsicObject after the insertion of the multipart: "
                                                      + testFrag.getAsPrettyString() );
                                    }
                                    records.add( objectIndex, handledExtrinsicObject );
                                    // records.add( handledExtrinsicObject );
                                } else {
                                    LOG.logDebug( "Following extrinsicObject was not found in the records list, this cannot be!: "
                                                  + extrinsicObject.toString() );
                                }
                            } else {
                                LOG.logDebug( "The given id: "
                                              + nameID
                                              + " was not found in the list of extrinsicObjects in the given InsertOperation, this cannot be!" );
                            }
                        }
                    } else {
                        throw new OGCWebServiceException( "Some mime multiparts remain, but there are no more "
                                                          + "referenced ids, something is wrong with the transaction",
                                                          CSWExceptionCode.WRS_INVALIDREQUEST );
                    }
                }
            }
        } catch ( IOException ioe ) {
            throw new OGCWebServiceException( "Can't handle your insert request, because an IOException "
                                              + "(with following message occured) while handling the mime multiparts:"
                                              + ioe.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST );
        } catch ( MessagingException me ) {
            throw new OGCWebServiceException( "Can't handle your insert request, because a MessagingException "
                                              + "(with following message occured) while handling the mime multiparts:"
                                              + me.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST );
        } catch ( XMLException xmle ) {
            throw new OGCWebServiceException( "Can't handle your insert request, because an XMLException (with "
                                              + "following message occured) while handling the mime multiparts:"
                                              + xmle.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST );
        } catch ( SAXException saxe ) {
            throw new OGCWebServiceException( "Can't handle your insert request, because an SAXException (with "
                                              + "following message) occured while handling the mime multiparts:"
                                              + saxe.getMessage(), CSWExceptionCode.WRS_INVALIDREQUEST );
        }
        // finished so lets give back the transaction as a result.
        return trans;
    }

    /**
     * Adds an deegreecsw:DescribedObject to the rim:EextrinsicObject, which will contain the xml
     * encoded object given in the MimeBodyPart.
     * 
     * @param content
     *            holding the described object
     * @param extrinsicObject
     *            which will receive the deegreecsw:DescribedObject
     * @return the modified extrinsicObject xml-Element.
     * @throws OGCWebServiceException
     * @throws MessagingException
     */
    private Element addDescribedExtrinsicObject( MimeBodyPart content, Element extrinsicObject )
                            throws OGCWebServiceException, MessagingException {

        String contentType = content.getContentType();
        LOG.logDebug( "in the multipart, we found the contentType: " + contentType );
        if ( contentType == null || !( contentType.contains( "application/xml" ) || contentType.contains( "text/xml" ) ) ) {
            throw new OGCWebServiceException( "Other than xml-encoded data can not be handled in the multiparts",
                                              CSWExceptionCode.WRS_INVALIDREQUEST );
        }
        try {
            InputStream contentIS = null;
            if ( !"UTF-8".equalsIgnoreCase( content.getEncoding() ) ) {
                contentIS = MimeUtility.decode( content.getInputStream(), content.getEncoding() );
            } else {
                contentIS = content.getInputStream();
            }

            BufferedReader reader = new BufferedReader( new InputStreamReader( contentIS ) );
            String firstLine = reader.readLine();
            if ( !reader.ready() || firstLine == null ) {
                LOG.logInfo( "CSW (Ebrim) Insert-Filter: no request characters found, not handling request" );
            }
            StringBuffer sb = new StringBuffer();
            while ( firstLine != null ) {
                sb.append( firstLine );
                firstLine = reader.readLine();
            }
            String resultString = sb.toString();
            LOG.logDebug( "content of multipart: " + resultString );

            // first delete the old element of the records list.
            // XMLFragment doc = new XMLFragment( new InputStreamReader( contentIS ),
            // XMLFragment.DEFAULT_URL );
            Element describedObject = XMLTools.appendElement( extrinsicObject, CommonNamespaces.DEEGREECSW,
                                                              CommonNamespaces.DEEGREECSW_PREFIX + ":DescribedObject" );
            CDATASection dataSection = describedObject.getOwnerDocument().createCDATASection( resultString );
            LOG.logDebug( "content of multipart(after cdata encoding): " + dataSection.getWholeText() );
            describedObject.appendChild( dataSection );
            // Document ownerDoc = extrinsicObject.getOwnerDocument();
            // Node newInsertNode = ownerDoc.importNode( doc.getRootElement(), true );
            // describedObject.appendChild( newInsertNode );

        } catch ( IOException e ) {
            throw new OGCWebServiceException( "An error occurred while processing a multipart, discarding",
                                              CSWExceptionCode.WRS_INVALIDREQUEST );
        }
        return extrinsicObject;
    }
}
