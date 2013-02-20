//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/SOSHandler.java $
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
 Aennchenstraße 19  
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 lat/lon GmbH
 Aennchenstraße 19
 53177 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.enterprise.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.deegree.enterprise.ServiceException;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.sos.SOService;
import org.deegree.ogcwebservices.sos.SOServiceFactory;
import org.deegree.ogcwebservices.sos.XMLFactory;
import org.deegree.ogcwebservices.sos.capabilities.SOSCapabilities;
import org.deegree.ogcwebservices.sos.describeplatform.DescribePlatformResult;
import org.deegree.ogcwebservices.sos.describesensor.DescribeSensorResult;
import org.deegree.ogcwebservices.sos.getobservation.GetObservationResult;

/**
 * Web servlet client for WFS.
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 */
class SOSHandler extends AbstractOWServiceHandler {

    private static ILogger LOG = LoggerFactory.getLogger( SOSHandler.class );

    /**
     * 
     */
    SOSHandler() {
        LOG.logDebug( "New SOSHandler instance created: " + this.getClass().getName() );
    }

    /**
     * @param request
     * @param response
     * @throws ServiceException
     * @throws OGCWebServiceException
     * @see "org.deegree.enterprise.servlet.ServiceDispatcher#perform(org.deegree.services.AbstractOGCWebServiceRequest,javax.servlet.http.HttpServletResponse)"
     */
    public void perform( OGCWebServiceRequest request, HttpServletResponse response )
                            throws ServiceException, OGCWebServiceException {

        Object serviceRes = this.getService().doService( request );

        if ( serviceRes instanceof OGCWebServiceException ) {
            sendException( response, (OGCWebServiceException) serviceRes );
        } else if ( response instanceof Exception ) {
            sendException( response, (Exception) serviceRes );
        } else if ( serviceRes instanceof SOSCapabilities ) {
            XMLFragment doc = XMLFactory.export( (SOSCapabilities) serviceRes );
            createResponse( response, doc );
        } else if ( serviceRes instanceof DescribePlatformResult ) {
            XMLFragment doc = XMLFactory.export( (DescribePlatformResult) serviceRes );
            createResponse( response, doc );
        } else if ( serviceRes instanceof DescribeSensorResult ) {
            XMLFragment doc = XMLFactory.export( (DescribeSensorResult) serviceRes );
            createResponse( response, doc );
        } else if ( serviceRes instanceof GetObservationResult ) {
            XMLFragment doc = XMLFactory.export( (GetObservationResult) serviceRes );
            createResponse( response, doc );
        } else {

            String mesgFragment = null;
            if ( response == null ) {
                mesgFragment = "null response object";
            } else {
                mesgFragment = response.getClass().getName();
            }

            OGCWebServiceException e = new OGCWebServiceException( getClass().getName(), "Unknown response class: "
                                                                                         + mesgFragment );
            sendException( response, e );
        }
    }

    private void createResponse( HttpServletResponse response, XMLFragment doc )
                            throws OGCWebServiceException {

        try {
            OutputStream os = response.getOutputStream();
            doc.write( os );
            os.close();
        } catch ( IOException e ) {
            throw new OGCWebServiceException( "SCS Web Service failed." );
        }

    }

    /**
     * @return Service
     * @throws ServiceException
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.enterprise.servlet.ServiceDispatcher#setConfiguration(java.net.URL)
     */
    public SOService getService()
                            throws ServiceException {
        SOService service = null;
        try {
            service = SOServiceFactory.getService();
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            throw new ServiceException( e );
        }
        return service;
    }
}
