//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/owsrequestvalidator/wfs/WFSValidator.java $
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
package org.deegree.security.owsrequestvalidator.wfs;

import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.getcapabilities.GetCapabilities;
import org.deegree.ogcwebservices.wfs.operation.DescribeFeatureType;
import org.deegree.ogcwebservices.wfs.operation.GetFeature;
import org.deegree.ogcwebservices.wfs.operation.GetFeatureWithLock;
import org.deegree.ogcwebservices.wfs.operation.LockFeature;
import org.deegree.ogcwebservices.wfs.operation.transaction.Transaction;
import org.deegree.security.UnauthorizedException;
import org.deegree.security.drm.model.User;
import org.deegree.security.owsrequestvalidator.Messages;
import org.deegree.security.owsrequestvalidator.OWSValidator;
import org.deegree.security.owsrequestvalidator.Policy;

/**
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 10566 $, $Date: 2008-03-13 09:33:14 +0100 (Do, 13. Mär 2008) $
 */
public class WFSValidator extends OWSValidator {

    private static final String MS_INVALIDREQUEST = Messages.getString( "WFSValidator.WFS_INVALIDREQUEST" );

    private GetFeatureRequestValidator getFeatureValidator;

    private GetFeatureResponseValidator getFeatureRespValidator;

    private DescribeFeatureTypeRequestValidator describeFeatureTypeValidator;

    private TransactionValidator transactionValidator;

    /**
     * @param policy
     * @param proxyURL
     */
    public WFSValidator( Policy policy, String proxyURL ) {
        super( policy, proxyURL );
        getFeatureValidator = new GetFeatureRequestValidator( policy );
        getFeatureRespValidator = new GetFeatureResponseValidator( policy );
        describeFeatureTypeValidator = new DescribeFeatureTypeRequestValidator( policy );
        transactionValidator = new TransactionValidator( policy );
    }

    /**
     * validates the passed <tt>OGCWebServiceRequest</tt> if it is valid against the defined
     * conditions for WFS requests
     * 
     * @param request
     * @param user
     * @throws InvalidParameterValueException
     * @throws UnauthorizedException
     */
    @Override
    public void validateRequest( OGCWebServiceRequest request, User user )
                            throws InvalidParameterValueException, UnauthorizedException {

        if ( request instanceof GetCapabilities ) {
            getCapabilitiesValidator.validateRequest( request, user );
        } else if ( request instanceof GetFeature ) {
            getFeatureValidator.validateRequest( request, user );
        } else if ( request instanceof GetFeatureWithLock ) {
            throw new UnauthorizedException( "GetFeatureWithLock on the WFS are not allowed!" );
        } else if ( request instanceof LockFeature ) {
            throw new UnauthorizedException( "Lock on the WFS are not allowed!" );
        } else if ( request instanceof DescribeFeatureType ) {
            describeFeatureTypeValidator.validateRequest( request, user );
        } else if ( request instanceof Transaction ) {
            transactionValidator.validateRequest( request, user );
        } else {
            throw new InvalidParameterValueException( MS_INVALIDREQUEST
                                                      + request.getClass().getName() );
        }
    }

    /**
     * @param request
     * @param response
     * @param mime
     * @param user
     * @return the new response
     * @throws InvalidParameterValueException
     * @throws UnauthorizedException
     */
    @Override
    public byte[] validateResponse( OGCWebServiceRequest request, byte[] response, String mime,
                                    User user )
                            throws InvalidParameterValueException, UnauthorizedException {

        if ( request instanceof GetCapabilities ) {
            response = getCapabilitiesValidatorR.validateResponse( "WFS", response, mime, user );
        } else if ( request instanceof GetFeature ) {
            response = getFeatureRespValidator.validateResponse( "WFS", response, mime, user );
        }
        // TODO responses to other requests
        return response;
    }
}