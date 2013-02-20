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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataSource;
import javax.mail.internet.ContentDisposition;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.deegree.enterprise.ServiceException;
import org.deegree.enterprise.servlet.OGCServletController;
import org.deegree.enterprise.servlet.ServiceLookup;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.CharsetUtils;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.ogcbase.ExceptionCode;
import org.deegree.ogcwebservices.ExceptionReport;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.OGCRequestFactory;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.csw.CSWExceptionCode;
import org.deegree.ogcwebservices.csw.CSWFactory;
import org.deegree.ogcwebservices.csw.CatalogueService;
import org.deegree.ogcwebservices.csw.capabilities.CatalogueCapabilities;
import org.deegree.ogcwebservices.csw.capabilities.CatalogueGetCapabilities;
import org.deegree.ogcwebservices.csw.discovery.DescribeRecord;
import org.deegree.ogcwebservices.csw.discovery.GetDomain;
import org.deegree.ogcwebservices.csw.discovery.GetRecordById;
import org.deegree.ogcwebservices.csw.discovery.GetRecords;
import org.deegree.ogcwebservices.csw.discovery.GetRepositoryItem;
import org.deegree.ogcwebservices.csw.manager.Harvest;
import org.deegree.ogcwebservices.csw.manager.Insert;
import org.deegree.ogcwebservices.csw.manager.Operation;
import org.deegree.ogcwebservices.csw.manager.Transaction;
import org.deegree.owscommon.XMLFactory;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Simple servlet facade for receiving csw/ebrim request and sending fake responses. This is not ususable anymore.
 * 
 * 
 * @version $Revision: 1.6 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: bezema $
 * 
 * @version 1.0. $Revision: 1.6 $, $Date: 2007-06-14 12:07:04 $
 * 
 * @since 2.0
 */
public class RIMServletHandler extends OGCServletController {

    /**
     * 
     */
    private static final long serialVersionUID = -4642387506442697837L;

    private final ILogger LOG = LoggerFactory.getLogger( RIMServletHandler.class );

    private XMLFragment getRecordsByIdResponse;

    private XMLFragment getRecordsResponse;

    private XMLFragment transactionResponse;

    private XMLFragment getRepositoryItemResponse;

    /**
     * A simple main, to check the multiparts sending (does not actually work :-)
     * @param args not used
     */
    public static void main( String[] args ) {

        // File transaction = new File( );
        XMLFragment transaction = null;
        XMLFragment extObj1 = null;
        XMLFragment extObj2 = null;
        try {
            transaction = new XMLFragment( new File( "resources/requests/insert1.xml" ) );
            extObj1 = new XMLFragment( new File( "resources/requests/urn_adv_crs_ETRS89_UTM32.xml" ) );
            extObj2 = new XMLFragment(
                                       new File(
                                                 "resources/requests/urn_adv_coordinateOperation_UTM32.xml" ) );
        } catch ( MalformedURLException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch ( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch ( SAXException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        PostMethod filePost = new PostMethod( "http://localhost:8081/deegree/services" );

        StringPart trans = new StringPart( "Transaction", transaction.getAsPrettyString(), "UTF-8" );
        trans.setContentType( "application/xml" );
        trans.setName( "Transaction" );
        System.out.println( transaction.getAsPrettyString() );
        
        StringPart obj1 = new StringPart( "urn:adv:crs:ETRS89_UTM32", extObj1.getAsPrettyString(),
                                          "UTF-8" );
        obj1.setContentType( "application/xml" );
        obj1.setName( "urn:adv:crs:ETRS89_UTM32" );
        
        StringPart obj2 = new StringPart( "urn:adv:coordinateOperation:UTM32",
                                          extObj2.getAsPrettyString(), "UTF-8" );
        obj2.setContentType( "application/xml" );
        obj2.setName( "urn:adv:coordinateOperation:UTM32" );

        Part[] parts = { trans, obj1, obj2 };
        filePost.setRequestEntity( new MultipartRequestEntity( parts, filePost.getParams() ) );
        HttpClient client = new HttpClient();
        try {
            int status = client.executeMethod( filePost );
            System.out.println( "status: " + status );
        } catch ( HttpException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch ( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @Override
    public void init()
                            throws ServletException {
        super.init();
    }

    /**
     * 
     * 
     * @param request
     * @param response
     * @throws ServiceException
     * @TODO refactor and optimize code for initializing handler
     */
    @Override
    public void doService( HttpServletRequest request, HttpServletResponse response )
                            throws ServiceException {
        if ( response.isCommitted() ) {
            LOG.logWarning( "The response object is already committed, cannot proceed!" );
            return;
        }

        long startTime = System.currentTimeMillis();
        address = request.getRequestURL().toString();

        try {
            OGCWebServiceRequest ogcRequest = OGCRequestFactory.create( request );

            LOG.logInfo( StringTools.concat( 500, "Handling request '", ogcRequest.getId(),
                                             "' from '", request.getRemoteAddr(),
                                             "' to service: '", ogcRequest.getServiceName(), "'" ) );

            // get service from request
            String service = ogcRequest.getServiceName().toUpperCase();

            // get handler instance
            ServiceLookup.getInstance().getHandler( service );

            // dispatch request to fake csw do service method
            LOG.logDebug( "Performing request: " + request.toString() );
            doCSWService( ogcRequest, response );
        } catch ( OGCWebServiceException e ) {
            e.printStackTrace();
            LOG.logError( e.getMessage(), e );
            sendException( response, e );
        }
        if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
            LOG.logDebug( "OGCServletController: request performed in "
                          + Long.toString( System.currentTimeMillis() - startTime )
                          + " milliseconds." );
        }
    }

    private void doCSWService( OGCWebServiceRequest request, HttpServletResponse response )
                            throws OGCWebServiceException {
        File requestBaseDir = new File( getServletContext().getRealPath( "/WEB-INF/conf/csw/" ) );
        LOG.logDebug( "basedir: " + requestBaseDir.toString() );

        try {
            if ( request instanceof DescribeRecord ) {
                sendException(
                               response,
                               new OGCWebServiceException(
                                                           "Operation DescribeRecord is not implement yet",
                                                           ExceptionCode.OPERATIONNOTSUPPORTED ) );
            } else if ( request instanceof GetDomain ) {
                sendException(
                               response,
                               new OGCWebServiceException(
                                                           "Operation GetDomain is not implement yet",
                                                           ExceptionCode.OPERATIONNOTSUPPORTED ) );
            } else if ( request instanceof GetRecords ) {
                getRecordsResponse = new XMLFragment(
                                                      new File( requestBaseDir
                                                                + "/getrecords_response1.xml" ).toURI().toURL() );
                sendFakeResponse( response, getRecordsResponse );
            } else if ( request instanceof GetRecordById ) {
                getRecordsByIdResponse = new XMLFragment(
                                                          new File(
                                                                    requestBaseDir
                                                                                            + "/getrecordbyid_response1.xml" ).toURI().toURL() );

                sendFakeResponse( response, getRecordsByIdResponse );
            } else if ( request instanceof GetRepositoryItem ) {
                getRepositoryItemResponse = new XMLFragment(
                                                             new File(
                                                                       requestBaseDir
                                                                                               + "/getrepositoryitem_response1.xml" ).toURI().toURL() );
                sendFakeResponse( response, getRepositoryItemResponse );
            } else if ( request instanceof Transaction ) {
                transactionResponse = new XMLFragment(
                                                       new File( requestBaseDir
                                                                 + "/transaction_response1.xml" ).toURI().toURL() );

                sendFakeResponse( response, transactionResponse );
            } else if ( request instanceof Harvest ) {
                sendException(
                               response,
                               new OGCWebServiceException(
                                                           "Operation Harvest is not implement yet",
                                                           ExceptionCode.OPERATIONNOTSUPPORTED ) );
            } else if ( request instanceof CatalogueGetCapabilities ) {
                LOG.logDebug( "GetCapabilities for version:" + request.getVersion(), request );
                String[] acceptVersions = ( (CatalogueGetCapabilities) request ).getAcceptVersions();
                boolean versionOk = false;
                if ( acceptVersions == null || acceptVersions.length == 0 ) {
                    versionOk = true;
                } else {
                    for ( int i = 0; i < acceptVersions.length; i++ ) {
                        if ( "2.0.0".equals( acceptVersions[i] )
                             || "2.0.1".equals( acceptVersions[i] )
                             || "2.0.2".equals( acceptVersions[i] ) ) {
                            versionOk = true;
                            break;
                        }
                    }
                }
                if ( versionOk ) {
                    CatalogueService service = CSWFactory.getService();
                    CatalogueCapabilities requestResponse = (CatalogueCapabilities) service.doService( request );
                    sendCapabilities( response, (CatalogueGetCapabilities) request, requestResponse );
                } else {
                    throw new InvalidParameterValueException(
                                                              "Unsupported version requested, only version 2.0.0 is supported." );
                }
            } else {
                sendException( response, new OGCWebServiceException( "Invalid request type: '"
                                                                     + request.getClass().getName()
                                                                     + "'." ) );

            }
        } catch ( MalformedURLException e ) {
            e.printStackTrace();
            sendException( response, new OGCWebServiceException( e.getMessage() ) );
        } catch ( IOException e ) {
            e.printStackTrace();
            sendException( response, new OGCWebServiceException( e.getMessage() ) );
        } catch ( SAXException e ) {
            e.printStackTrace();
            sendException( response, new OGCWebServiceException( e.getMessage() ) );
        }
    }

    /**
     * Sends the passed <tt>CatalogCapabilities</tt> to the http client.
     * 
     * @param response
     *            http connection to the client
     * @param capabilities
     *            object to send
     */
    private void sendCapabilities( HttpServletResponse response,
                                   CatalogueGetCapabilities getCapabilities,
                                   CatalogueCapabilities capabilities )
                            throws IOException {

        boolean xmlOk = false;
        String[] formats = getCapabilities.getAcceptFormats();
        if ( formats == null || formats.length == 0 ) {
            xmlOk = true;
        } else {
            for ( int i = 0; i < formats.length; i++ ) {
                if ( formats[i].equals( "text/xml" ) ) {
                    xmlOk = true;
                    break;
                }
            }
        }
        if ( !xmlOk ) {
            ExceptionCode code = ExceptionCode.INVALIDPARAMETERVALUE;
            InvalidParameterValueException e = new InvalidParameterValueException(
                                                                                   this.getClass().getName(),
                                                                                   "OutputFormat must be 'text/xml'.",
                                                                                   code );
            sendException( response, e );
        } else {

            XMLFragment doc = org.deegree.ogcwebservices.csw.XMLFactory.export(
                                                                                capabilities,
                                                                                getCapabilities.getSections() );
            response.setContentType( "text/xml; charset=" + CharsetUtils.getSystemCharset() );
            OutputStream os = response.getOutputStream();
            doc.write( os );
            os.close();
        }
    }

    @Override
    public void doGet( HttpServletRequest request, HttpServletResponse response )
                            throws IOException, ServletException {

        LOG.logDebug( "query string ", request.getQueryString() );
        try {
            this.doService( request, response );
        } catch ( ServiceException e ) {
            LOG.logError( e.getMessage(), e );
            this.sendException( response, new OGCWebServiceException(e.getLocalizedMessage()) );
        }
    }

    @Override
    public void doPost( HttpServletRequest req, HttpServletResponse res )
                            throws IOException, ServletException {
        try {
            if ( req.getContentType().contains( "multipart/form-data" ) ) {
                Transaction trans = handleMultiparts( req, res );
                if ( trans != null ) {
                    sendFakeResponse( res, transactionResponse );
                }
            } else {
                this.doService( req, res );
            }
        } catch ( ServiceException e ) {
            LOG.logError( e.getMessage(), e );
            e.printStackTrace();
            this.sendException( res, new OGCWebServiceException(e.getLocalizedMessage()) );
        }

    }

    private Transaction handleMultiparts( HttpServletRequest request, HttpServletResponse response ) {
        Transaction trans = null;
        try {
            StreamDataSource sds = new StreamDataSource( request );
            MimeMultipart multi = new MimeMultipart( sds );
            Map<String, Operation> extrinsObjects = new HashMap<String, Operation>();
            System.out.println( "multi: " + multi.getCount() );
            for ( int i = 0; i < multi.getCount(); i++ ) {
                MimeBodyPart content = (MimeBodyPart) multi.getBodyPart( i );
                System.out.println( "id: " + content.getContentID() );
                System.out.println( "type: " + content.getContentType() );
                String contentType = content.getContentType();
                if ( !( contentType.contains( "application/xml" ) || contentType.contains( "text/xml" ) ) ) {
                    throw new OGCWebServiceException(
                                                      "Other than xml-encoded data can not be handled in the multiparts",
                                                      CSWExceptionCode.WRS_INVALIDREQUEST );
                }
                Enumeration en = content.getAllHeaderLines();
                while( en.hasMoreElements() ){
                    System.out.println( (String) en.nextElement() );
                }
                String[] names = content.getHeader( "Content-Disposition" );
                String nameID = null;
                if ( names != null ) {
                    for( String name : names ){
                        ContentDisposition cd = new ContentDisposition( name );
                        String nm = cd.getParameter( "name" );
                        if( nm != null){
                            nameID = nm;
                            break;
                        }
                    }
                }

                if ( nameID == null ) {
                    nameID = content.getContentID();
                    if ( nameID == null ) {
                        throw new OGCWebServiceException(
                                                          "Exactly one 'name' parameter must be set in the header.",
                                                          CSWExceptionCode.WRS_INVALIDREQUEST );
                    }
                }
                System.out.println( "name:" + nameID );
                if ( "Transaction".equalsIgnoreCase( nameID ) ) {
                    XMLFragment doc = new XMLFragment();
                    System.out.println( "lineCount: " + content.getLineCount() );

                    System.out.println( "string: " + content.toString() );
                    
                    System.out.println( "encoding: " + content.getEncoding() );
                    InputStream contentIS = null;
                    if( ! "UTF-8".equalsIgnoreCase( content.getEncoding() ) ){
                        contentIS = MimeUtility.decode( content.getInputStream(), content.getEncoding() );
                    } else {
                        contentIS = content.getInputStream();
                    }
                    doc.load( contentIS, XMLFragment.DEFAULT_URL );
                    // create the transaction
                    trans = Transaction.create( "0", doc.getRootElement() );
                    List<Operation> ops = trans.getOperations();
                    // find all the ids which were referenced by the transaction.
                    for ( Operation op : ops ) {
                        if ( "Insert".equalsIgnoreCase( op.getName() ) ) {
                            Map<String, Element> objects = ( (Insert) op ).getExtrinsicObjects();
                            Set<String> keys = objects.keySet();
                            // set the keys to the operations they were referenced in.
                            for ( String key : keys ) {
                                if ( extrinsObjects.containsKey( key ) ) {
                                    throw new OGCWebServiceException(
                                                                      "The following key is not unique, "
                                                                                              + key
                                                                                              + ", the transaction therefore failed.",
                                                                      CSWExceptionCode.WRS_INVALIDREQUEST );
                                }
                                extrinsObjects.put( key, op );
                            }
                        }
                    }
                } else {
                    if ( extrinsObjects.size() > 0 ) {
                        if ( extrinsObjects.containsKey( nameID ) ) {
                            // handleRIMObject( content );
                            // todo handle the object.
                        }
                    } else {
                        throw new OGCWebServiceException(
                                                          "Some parts remain, but there are no more referenced ids, something is wrong with the transaction",
                                                          CSWExceptionCode.WRS_INVALIDREQUEST );
                    }
                }
            }
        } catch ( OGCWebServiceException e ) {
            e.printStackTrace();
            sendException( response, e );
        } catch ( Exception e ) {
            e.printStackTrace();
            sendException( response,
                           new OGCWebServiceException( e.getMessage(),
                                                       CSWExceptionCode.WRS_TRANSACTIONFAILED ) );
        }

        // finished so lets give back the transaction as a result.
        return trans;
    }

    /**
     * Sends the passed <tt>OGCWebServiceException</tt> to the calling client.
     * 
     * @param response
     * @param e
     */
    private void sendException( HttpServletResponse response, OGCWebServiceException e ) {
        LOG.logInfo( "Sending OGCWebServiceException to client." );
        ExceptionReport report = new ExceptionReport( new OGCWebServiceException[] { e } );

        try {
            response.setContentType( "application/vnd.ogc.se_xml" );
            XMLFragment doc = XMLFactory.export( report );
            OutputStream os = response.getOutputStream();
            doc.write( os );
            os.close();
        } catch ( Exception ex ) {
            LOG.logError( "ERROR: " + ex.getMessage(), ex );
        }
    }

    /**
     * 
     * @param response
     * @param getRecordResponse
     * @throws IOException
     * @throws SAXException
     */
    private void sendFakeResponse( HttpServletResponse response, XMLFragment docResponse )
                            throws IOException {
        response.setContentType( "text/xml; charset=UTF-8" );
        OutputStream os = response.getOutputStream();

        docResponse.write( os );
        os.close();
    }


    /**
     * This class maps the request stream to the content parser that is able to pick files from it.
     */
    private class StreamDataSource implements DataSource {
        private HttpServletRequest m_req;

        /**
         * @param req
         */
        public StreamDataSource( HttpServletRequest req ) {
            m_req = req;
        }

        /**
         * @return the content type for the request stream.
         */
        public String getContentType() {
            return m_req.getContentType();
        }

        /**
         * @return a stream from the request.
         */
        public InputStream getInputStream()
                                throws IOException {
            return m_req.getInputStream();
        }

        /**
         * Maps output to System.out. Do something more sensible here...
         */
        public OutputStream getOutputStream() {
            return System.out;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.activation.DataSource#getName()
         */
        public String getName() {
            // TODO Auto-generated method stub
            return null;
        }

    }

}