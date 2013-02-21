//$HeadURL: $
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

package org.deegree.ogcwebservices.csw.discovery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;
import org.deegree.ogcwebservices.OGCRequestFactory;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.csw.AbstractCSWRequest;
import org.deegree.ogcwebservices.csw.CSWExceptionCode;

/**
 * The <code>GetRepositoryItem</code> class encapsulates the data of a request for a repository
 * item.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 * 
 */

public class GetRepositoryItem extends AbstractCSWRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 6140080070986019397L;

    private static ILogger LOG = LoggerFactory.getLogger( GetRepositoryItem.class );

    private URI reposItem;

    private String service;

    /**
     * Creates a new <code>GetRepositoryItem</code> instance.
     * 
     * @param id
     * @param version
     * @param vendorSpecificParameters
     * @param reposItem
     *            some uri identifying some ebrim extrinsic object
     */
    public GetRepositoryItem( String id, String version, Map<String, String> vendorSpecificParameters, URI reposItem ) {
        super( version, id, vendorSpecificParameters );
        this.service = OGCRequestFactory.CSW_SERVICE_NAME_EBRIM;
        this.reposItem = reposItem;
    }

    /**
     * @param id
     *            of the request
     * @param version
     *            of the csw
     */
    public GetRepositoryItem( String id, String version ) {
        super( version, id, null );
    }

    /**
     * Creates a new <code>GetRepositoryItem</code> instance from the values stored in the
     * submitted Map. Keys (parameter names) in the Map must be uppercase.
     * 
     * @TODO evaluate vendorSpecificParameter
     * 
     * @param kvp
     *            Map containing the parameters
     * @return a GetRepositoryItem instance with given id and values from the kvp
     * @exception OGCWebServiceException
     */
    public static GetRepositoryItem create( Map<String, String> kvp )
                            throws OGCWebServiceException {

        String service = getRequiredParam( "SERVICE", kvp );
        if ( !OGCRequestFactory.CSW_SERVICE_NAME_EBRIM.equals( service ) ) {
            throw new OGCWebServiceException( Messages.getMessage( "CSW_INVALID_REQUEST_PARAM", "service",
                                                                   OGCRequestFactory.CSW_SERVICE_NAME_EBRIM, service ),
                                              CSWExceptionCode.WRS_INVALIDREQUEST );
        }

        String request = getRequiredParam( "REQUEST", kvp );
        if ( !"GetRepositoryItem".equals( request ) ) {
            throw new OGCWebServiceException( Messages.getMessage( "CSW_INVALID_REQUEST_PARAM", "request",
                                                                   "GetRepositoryItem", request ),
                                              CSWExceptionCode.WRS_INVALIDREQUEST );
        }

        String requestID = getParam( "REQUESTID", kvp, "0" );

        String id = getRequiredParam( "ID", kvp );
        URI reposItem = null;
        try {
            reposItem = new URI( id );
        } catch ( URISyntaxException urise ) {
            throw new OGCWebServiceException( Messages.getMessage( "CSW_INVALID_REQUEST_PARAM", "id",
                                                                   "some kind of valid repository URI", id ),
                                              CSWExceptionCode.WRS_INVALIDREQUEST );
        }
        LOG.logDebug( "GetRepositoryItem id=" + reposItem );

        // for GetRepositoryItem default version is 2.0.1 because ebRIM profile set up
        // on CSW 2.0.1 specification
        String version = getParam( "REQUESTID", kvp, "2.0.1" );

        return new GetRepositoryItem( requestID, version, kvp, reposItem );
    }

    /**
     * @return the value 'urn:x-ogc:specification:cswebrim:Service:OGC-CSW:ebRIM'
     */
    @Override
    public String getServiceName() {
        return service;
    }

    /**
     * @return the requested repository Item's id.
     */
    public URI getRepositoryItemID() {
        return reposItem;
    }
}