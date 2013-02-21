// $HeadURL:
// /cvsroot/deegree/src/org/deegree/ogcwebservices/wms/protocol/WMSProtocolFactory.java,v
// 1.7 2004/07/12 06:12:11 ap Exp $
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
package org.deegree.ogcwebservices.wms.operation;

import org.deegree.ogcwebservices.AbstractOGCWebServiceRequest;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.wms.configuration.WMSConfigurationType;
import org.w3c.dom.Document;

/**
 * Factory that builds the different types of WMS-Requests & Responses.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author <a href="mailto:wanhoff@uni-bonn.de">Jeronimo Wanhoff </a>
 * @version $Revision: 9345 $ $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 */
public class WMSProtocolFactory {

    /**
     * creates an instance of a <tt>WMSGetCapabilitiesResult</tt> object
     * 
     * @param request
     *            request that lead to the response
     * @param exception
     *            exception if one occuered
     * @param capabilities
     *            WMS capabilities
     * 
     * @return <tt>WMSGetCapabilitiesResult</tt>
     */
    public static WMSGetCapabilitiesResult createGetCapabilitiesResponse( OGCWebServiceRequest request,
                                                                          OGCWebServiceException exception,
                                                                          WMSConfigurationType capabilities ) {

        WMSGetCapabilitiesResult res = null;
        if ( exception != null ) {
            res = new WMSGetCapabilitiesResult( request, exception );
        } else {
            res = new WMSGetCapabilitiesResult( request, capabilities );
        }

        return res;
    }

    /**
     * creates a <tt>WFSGetMapResponse</tt> object
     * 
     * @param request
     *            a copy of the request that leads to this response
     * @param exception
     *            a describtion of an excetion (only if raised)
     * @param response
     *            the response to the request
     * @return the result
     */
    public static GetMapResult createGetMapResponse( OGCWebServiceRequest request, OGCWebServiceException exception,
                                                     Object response ) {

        GetMapResult res = null;
        if ( exception != null ) {
            res = new GetMapResult( request, exception );
        } else {
            res = new GetMapResult( request, response );
        }

        return res;
    }

    /**
     * creates a <tt>WFSGetFeatureInfoResponse</tt> object
     * 
     * @param request
     *            a copy of the request that leads to this response
     * @param exception
     *            a describtion of an excetion (only if raised)
     * @param featureInfo
     * @return the result object
     */
    public static GetFeatureInfoResult createGetFeatureInfoResponse( OGCWebServiceRequest request,
                                                                     OGCWebServiceException exception,
                                                                     String featureInfo ) {

        GetFeatureInfoResult res = null;
        if ( exception != null ) {
            res = new GetFeatureInfoResult( request, exception );
        } else {
            res = new GetFeatureInfoResult( request, featureInfo );
        }

        return res;
    }

    /**
     * @param request
     * @param legendGraphic
     * @return the result object
     */
    public static GetLegendGraphicResult createGetLegendGraphicResponse( OGCWebServiceRequest request,
                                                                         Object legendGraphic ) {
        return new GetLegendGraphicResult( request, legendGraphic );
    }

    /**
     * @param request
     * @param exception
     * @return the result object
     */
    public static GetLegendGraphicResult createGetLegendGraphicResponse( AbstractOGCWebServiceRequest request,
                                                                         Document exception ) {
        return new GetLegendGraphicResult( request, exception );
    }

}
