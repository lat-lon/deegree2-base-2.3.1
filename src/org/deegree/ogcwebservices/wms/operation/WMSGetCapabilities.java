//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wms/operation/WMSGetCapabilities.java $
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

import java.util.Map;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.ogcwebservices.InconsistentRequestException;
import org.deegree.ogcwebservices.MissingParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.getcapabilities.GetCapabilities;

/**
 * This interface desribes the access to the parameters common to a OGC
 * GetCapabilities request. It inherits three accessor methods from the
 * general OGC web service request interface.
 *
 * <p>--------------------------------------------------------</p>
 *
 * @author Katharina Lupp <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 9345 $ $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 */
public class WMSGetCapabilities extends GetCapabilities {

    private static final long serialVersionUID = -7885976233890866824L;

    private static final ILogger LOG = LoggerFactory.getLogger( WMSGetCapabilities.class );

    /**
     * creates an WMS GetCapabilities Request
     * 
     * @param paramMap
     *            the parameters of the request
     * @return the GetCapabilities request
     * @throws InconsistentRequestException
     *             if the request is inconsistent
     * @throws MissingParameterValueException 
     */
    public static WMSGetCapabilities create( Map<String,String> paramMap )
                            throws InconsistentRequestException, MissingParameterValueException {
        LOG.logDebug( "Request parameters: " + paramMap );
        String version = getParam( "VERSION", paramMap, null );

        if ( version == null ) {
            version = getParam( "WMTVER", paramMap, null );
        }

        String service = getRequiredParam( "SERVICE", paramMap );
        String updateSequence = getParam( "UPDATESEQUENCE", paramMap, null );

        if ( !service.equals( "WMS" ) ) {
            throw new InconsistentRequestException( "Parameter 'SERVICE' must be 'WMS'." );
        }

        return new WMSGetCapabilities( getParam( "ID", paramMap, null ), version, 
                                       updateSequence, paramMap );
    }

    /**
     * Creates a new WMSGetCapabilities object.
     *
     * @param updateSequence 
     * @param version 
     * @param id 
     * @param vendorSpecific
     */
    WMSGetCapabilities( String version, String id, String updateSequence,
                        Map<String,String> vendorSpecific ) {
        super( version, id, updateSequence, null, null, null, vendorSpecific );
    }

    /** returns the URI of a HTTP GET request. 
     *
     */
    @Override
    public String getRequestParameter()
                            throws OGCWebServiceException {
        if ( getVersion().equals( "1.0.0" ) ) {
            return "service=WMS&version=" + getVersion() + "&request=capabilities";
        }
        return "service=WMS&version=" + getVersion() + "&request=GetCapabilities";
    }

    /**
     * returns 'WMS' as service name
     */
    public String getServiceName() {
        return "WMS";
    }
}
