//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/owsrequestvalidator/csw/CSWValidator.java $
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
package org.deegree.security.owsrequestvalidator.csw;

import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.csw.discovery.DescribeRecord;
import org.deegree.ogcwebservices.csw.discovery.GetRecordById;
import org.deegree.ogcwebservices.csw.discovery.GetRecords;
import org.deegree.ogcwebservices.csw.discovery.GetRepositoryItem;
import org.deegree.ogcwebservices.csw.manager.Transaction;
import org.deegree.ogcwebservices.getcapabilities.GetCapabilities;
import org.deegree.security.UnauthorizedException;
import org.deegree.security.drm.model.User;
import org.deegree.security.owsrequestvalidator.OWSValidator;
import org.deegree.security.owsrequestvalidator.Policy;

/**
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Do, 27. Dez 2007) $
 */

public class CSWValidator extends OWSValidator {

//    private static final String MS_INVALIDREQUEST = Messages.getString( "CSW_INVALIDREQUEST" );

    private static CSWValidator self = null;

    private GetRecordsRequestValidator getRecordValidator;

    // private DescribeRecordTypeRequestValidator describeRecordTypeValidator;
    private TransactionValidator transactionValidator;

    private GetRecordByIdRequestValidator byIdValidator;
    
    private GetRecordByIdResponseValidator byIdResValidator;

    private GetRepositoryItemRequestValidator getRepositoryItem;

    /**
     * @param policy
     * @param proxyURL
     */
    public CSWValidator( Policy policy, String proxyURL ) {
        super( policy, proxyURL );
        this.getRepositoryItem = new GetRepositoryItemRequestValidator( policy );
        this.getRecordValidator = new GetRecordsRequestValidator( policy );
        // this.describeRecordTypeValidator = new DescribeRecordTypeRequestValidator( policy );
        this.transactionValidator = new TransactionValidator( policy );
        this.byIdValidator = new GetRecordByIdRequestValidator( policy );
        this.byIdResValidator = new GetRecordByIdResponseValidator( policy );
    }

    /**
     * returns an instance of <tt>WFSPolicyValidator</tt> --> singleton
     * <p>
     * before this method cann be called, WFSPolicyValidator.create(URL) must be called to intialize
     * the <tt>WFSPolicyValidator</tt> otherwise this method returns <tt>null</tt>
     * 
     * @return an instance of <tt>WFSPolicyValidator</tt>
     */
    public static CSWValidator getInstance() {
        return self;
    }

    @Override
    public void validateRequest( OGCWebServiceRequest request, User user )
                            throws InvalidParameterValueException, UnauthorizedException {

        if ( request instanceof GetCapabilities ) {
            getCapabilitiesValidator.validateRequest( request, user );
        } else if ( request instanceof GetRecords ) {
            getRecordValidator.validateRequest( request, user );
        } else if ( request instanceof GetRecordById ) {
            byIdValidator.validateRequest( request, user );
        } else if ( request instanceof DescribeRecord ) {
            //always allowed
        } else if ( request instanceof Transaction ) {
            transactionValidator.validateRequest( request, user );
        } else if ( request instanceof GetRepositoryItem ) {
            getRepositoryItem.validateRequest( request, user );
        } else {
            throw new InvalidParameterValueException( "The requested operation is unkwon to the security model, you are therefore not permitted acces." );
        }
    }

    @Override
    public byte[] validateResponse( OGCWebServiceRequest request, byte[] response, String mime,
                                    User user )
                            throws InvalidParameterValueException, UnauthorizedException {

        if ( request instanceof GetCapabilities ) {
            response = getCapabilitiesValidatorR.validateResponse( "CSW", response, mime, user );
        } else if ( request instanceof GetRecordById ) {
            response = byIdResValidator.validateResponse( "CSW", response, mime, user );
        } 
        // TODO responses to other requests
        return response;
    }
}