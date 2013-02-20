//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/CSWHandler.java $
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
package org.deegree.enterprise.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import org.deegree.enterprise.ServiceException;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.CharsetUtils;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.ogcbase.ExceptionCode;
import org.deegree.ogcwebservices.EchoRequest;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.csw.CSWFactory;
import org.deegree.ogcwebservices.csw.CSWPropertiesAccess;
import org.deegree.ogcwebservices.csw.CatalogueService;
import org.deegree.ogcwebservices.csw.capabilities.CatalogueCapabilities;
import org.deegree.ogcwebservices.csw.capabilities.CatalogueGetCapabilities;
import org.deegree.ogcwebservices.csw.discovery.DescribeRecordResult;
import org.deegree.ogcwebservices.csw.discovery.GetRecordByIdResult;
import org.deegree.ogcwebservices.csw.discovery.GetRecordsResult;
import org.deegree.ogcwebservices.csw.manager.HarvestResult;
import org.deegree.ogcwebservices.csw.manager.TransactionResult;

/**
 * Web servlet client for CSW.
 * 
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </A>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 9499 $, $Date: 2008-01-09 16:47:04 +0100 (Wed, 09 Jan 2008) $
 * 
 * @see <a href="http://www.dofactory.com/patterns/PatternChain.aspx">Chain of Responsibility Design
 *      Pattern </a>
 */

public class CSWHandler extends AbstractOWServiceHandler {

    private static final ILogger LOG = LoggerFactory.getLogger( CSWHandler.class );

    /**
     * @param request
     * @param httpResponse
     * @throws ServiceException
     * @throws OGCWebServiceException
     * @see "org.deegree.enterprise.servlet.ServiceDispatcher#perform(org.deegree.services.AbstractOGCWebServiceRequest,javax.servlet.http.HttpServletResponse)"
     */
    public void perform( OGCWebServiceRequest request, HttpServletResponse httpResponse )
                            throws ServiceException, OGCWebServiceException {

        LOG.logDebug( "Performing request: " + request.toString() );

        try {
            CatalogueService service = CSWFactory.getService();
            Object response = service.doService( request );
            if ( response instanceof OGCWebServiceException ) {
                sendExceptionReport( httpResponse, (OGCWebServiceException) response );
            } else if ( response instanceof Exception ) {
                sendExceptionReport( httpResponse, (Exception) response );
            } else if ( response instanceof CatalogueCapabilities ) {
                sendCapabilities( httpResponse, (CatalogueGetCapabilities) request, (CatalogueCapabilities) response );
            } else if ( response instanceof GetRecordsResult ) {
                sendGetRecord( httpResponse, (GetRecordsResult) response );
            } else if ( response instanceof GetRecordByIdResult ) {
                sendGetRecordById( httpResponse, (GetRecordByIdResult) response );
            } else if ( response instanceof DescribeRecordResult ) {
                sendDescribeRecord( httpResponse, (DescribeRecordResult) response );
            } else if ( response instanceof TransactionResult ) {
                sendTransactionResult( httpResponse, (TransactionResult) response );
            } else if ( response instanceof HarvestResult ) {
                sendHarvestResult( httpResponse, (HarvestResult) response );
            } else if ( response instanceof EchoRequest ) {
                sendHarvestResult( httpResponse );
            } else {
                OGCWebServiceException e = new OGCWebServiceException(
                                                                       this.getClass().getName(),
                                                                       "Unknown response class: "
                                                                                               + ( response == null ? "null response object"
                                                                                                                   : response.getClass().getName() )
                                                                                               + "." );
                sendExceptionReport( httpResponse, e );
            }
        } catch ( IOException ex ) {
            throw new ServiceException( "Error while sending response: " + ex.getMessage(), ex );
        } catch ( OGCWebServiceException ogc ) {
            sendExceptionReport( httpResponse, ogc );
        }

    }

    /**
     * Sends the passed <tt>HarvestResult</tt> to the http client.
     * 
     * @param httpResponse
     *            http connection to the client
     * @param result
     *            object to send
     * @throws IOException
     * @throws XMLParsingException
     */
    private void sendHarvestResult( HttpServletResponse httpResponse, HarvestResult result )
                            throws IOException {
        XMLFragment doc = null;
        try {
            doc = org.deegree.ogcwebservices.csw.manager.XMLFactory.export( result );
        } catch ( XMLParsingException e ) {
            throw new IOException( "could not export TransactionResult as XML: " + e.getMessage() );
        }
        httpResponse.setContentType( "text/xml; charset=" + CharsetUtils.getSystemCharset() );
        OutputStream os = httpResponse.getOutputStream();
        doc.write( os );
        os.close();
    }

    /**
     * 
     * @param httpResponse
     * @throws IOException
     */
    private void sendHarvestResult( HttpServletResponse httpResponse )
                            throws IOException {

        httpResponse.setContentType( "text/xml; charset=" + CharsetUtils.getSystemCharset() );
        PrintWriter pw = httpResponse.getWriter();
        pw.write( "<HarvestResponse>Harvest request has been received " );
        pw.write( "and will be performed</HarvestResponse>" );
        pw.close();
    }

    /**
     * Sends the passed <tt>TransactionResult</tt> to the http client.
     * 
     * @param httpResponse
     *            http connection to the client
     * @param result
     *            object to send
     * @throws XMLParsingException
     */
    private void sendTransactionResult( HttpServletResponse httpResponse, TransactionResult result )
                            throws IOException {
        XMLFragment doc = null;
        try {
            doc = org.deegree.ogcwebservices.csw.manager.XMLFactory.export( result );
        } catch ( XMLParsingException e ) {
            throw new IOException( "could not export TransactionResult as XML: " + e.getMessage() );
        }
        httpResponse.setContentType( "text/xml; charset=" + CharsetUtils.getSystemCharset() );
        OutputStream os = httpResponse.getOutputStream();
        doc.write( os );
        os.close();
    }

    /**
     * Sends the passed <tt>CatalogCapabilities</tt> to the http client.
     * 
     * @param response
     *            http connection to the client
     * @param capabilities
     *            object to send
     */
    private void sendCapabilities( HttpServletResponse response, CatalogueGetCapabilities getCapabilities,
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
            InvalidParameterValueException e = new InvalidParameterValueException( this.getClass().getName(),
                                                                                   "OutputFormat must be 'text/xml'.",
                                                                                   code );
            sendExceptionReport( response, e );
        } else {
            String version = getCapabilities.getVersion();
            String className = CSWPropertiesAccess.getString( "XMLFactory" + version );
            XMLFragment doc = null;
            try {
                Class<?> clzz = Class.forName( className );
                Class[] parameterTypes = new Class[] { CatalogueCapabilities.class, String[].class };
                Object[] parameters = new Object[] { capabilities, getCapabilities.getSections() };
                Method method = clzz.getMethod( "export", parameterTypes );
                doc = (XMLFragment) method.invoke( null, parameters );
            } catch ( Exception e ) {
                e.printStackTrace();
            }

            response.setContentType( "text/xml; charset=" + CharsetUtils.getSystemCharset() );
            OutputStream os = response.getOutputStream();
            doc.write( os );
            os.close();
        }
    }

    /**
     * 
     * @param response
     * @param getRecordResponse
     * @throws IOException
     */
    private void sendGetRecord( HttpServletResponse response, GetRecordsResult getRecordResponse )
                            throws IOException {
        XMLFragment doc = org.deegree.ogcwebservices.csw.discovery.XMLFactory.export( getRecordResponse );
        if ( LOG.isDebug() ) {
            LOG.logDebug( "GetRecord response", doc.getAsPrettyString() );
        }
        response.setContentType( "text/xml; charset=" + CharsetUtils.getSystemCharset() );
        OutputStream os = response.getOutputStream();
        doc.write( os );
        os.close();
    }

    /**
     * 
     * @param response
     * @param getRecordByResponse
     * @throws IOException
     */
    private void sendGetRecordById( HttpServletResponse response, GetRecordByIdResult getRecordByIdResponse )
                            throws IOException {
        XMLFragment doc = org.deegree.ogcwebservices.csw.discovery.XMLFactory.export( getRecordByIdResponse );
        if ( LOG.isDebug() ) {
            LOG.logDebug( "GetRecordById response", doc.getAsPrettyString() );
        }
        response.setContentType( "text/xml" );
        OutputStream os = response.getOutputStream();
        doc.write( os );
        os.close();
    }

    /**
     * 
     * @param response
     * @param describeRecordRequest
     * @param describeRecordResponse
     * @throws IOException
     */
    private void sendDescribeRecord( HttpServletResponse response, DescribeRecordResult describeRecordResponse )
                            throws IOException {
        XMLFragment doc = org.deegree.ogcwebservices.csw.discovery.XMLFactory.export( describeRecordResponse );
        if ( LOG.isDebug() ) {
            LOG.logDebug( "DescribeRecord response", doc.getAsPrettyString() );
        }
        response.setContentType( "text/xml; charset=" + CharsetUtils.getSystemCharset() );
        OutputStream os = response.getOutputStream();
        doc.write( os );
        os.close();
    }
}
